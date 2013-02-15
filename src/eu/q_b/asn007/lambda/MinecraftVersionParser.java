package eu.q_b.asn007.lambda;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JComboBox;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MinecraftVersionParser extends Thread {

	private ArrayList<Minecraft> minecrafts;
	private String xml;
	private JComboBox box;

	public MinecraftVersionParser(String xml, JComboBox box) {

		this.xml = xml;
		this.box = box;
		this.minecrafts = new ArrayList<Minecraft>();
	}

	public void run() {
		if (Main.getFramework().getNetmodule().isOnline())
			parse();
		else
			parseOffline();
	}

	private void parseOffline() {
		Main.getFramework()
				.log("Well.. It seems that we're offline, but I'll try to look for available Minecraft installs and add them to list.",
						getClass());
		Main._instance.getForceUpdate().setEnabled(false);
		String[] s = new File(Main.getFramework().getFileModule()
				.getWorkingDirectory()
				+ File.separator + "bin").list(new MinecraftFileFilter());
		for (String string : s) {
			if (string.replace(".jar", "").equals("minecraft"))
				box.addItem(new DefaultMinecraft("", "", ""));
			else
				box.addItem(new Minecraft(string.replace(".jar", ""), "", ""));
		}

	}

	public void parse() {
		Main.getFramework().log("XML document parsing operation started...",
				this.getClass());
		long l = System.currentTimeMillis();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse(this.xml);
			parseDocument("minecraft", dom);
			Collections.sort(minecrafts, new MinecraftComparator());
			for (Minecraft mc : minecrafts)
				box.addItem(mc);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		Main.getFramework().log(
				"XML document parsing operation finished in "
						+ (System.currentTimeMillis() - l) + "ms",
				this.getClass());

	}

	public ArrayList<Minecraft> parseDocument(String nodeName, Document dom) {
		Element docEle = (Element) dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName(nodeName);
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				minecrafts.add(getMinecraft((Element) nl.item(i)));
			}
		}
		if (new File(Main.getFramework().getFileModule().getWorkingDirectory()
				+ File.separator + "bin" + File.separator + "minecraft.jar")
				.exists())
			minecrafts.add(new DefaultMinecraft("", "", ""));
		return minecrafts;
	}

	private Minecraft getMinecraft(Element mc) {
		return new Minecraft(mc.getAttribute("name"),
				getTextValue(mc, "patch"), mc.getAttribute("type"));
	}

	public JComboBox getBox() {
		if (box.getItemCount() <= 1)
			parse();
		return box;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public ArrayList<Minecraft> getMinecrafts() {
		return minecrafts;
	}

	public void setMinecrafts(ArrayList<Minecraft> minecrafts) {
		this.minecrafts = minecrafts;
	}

	public String getAttribute(Node element, String attName) {
		NamedNodeMap attrs = element.getAttributes();
		if (attrs == null) {
			return null;
		}
		Node attN = attrs.getNamedItem(attName);
		if (attN == null) {
			return null;
		}
		return attN.getNodeValue();
	}

	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if (nl != null && nl.getLength() > 0) {
			Element el = (Element) nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}

}

class MinecraftComparator implements Comparator<Minecraft> {
	@Override
	public int compare(Minecraft o1, Minecraft o2) {
		return o1.compareTo(o2);
	}
}
