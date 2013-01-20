package eu.q_b.asn007.lambda;

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
		parse();
	}
	
	public void parse() {
		Main.getFramework().log("XML document parsing operation started...", this.getClass());
		long l = System.currentTimeMillis();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse(this.xml);
			parseDocument("minecraft", dom);
			Collections.sort(minecrafts, new MinecraftComparator());
			for(Minecraft mc: minecrafts) box.addItem(mc);
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		Main.getFramework().log("XML document parsing operation finished in " + (System.currentTimeMillis() - l) + "ms", this.getClass());
		
	}
	
	
	public ArrayList<Minecraft> parseDocument(String nodeName, Document dom){
		Element docEle = (Element) dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName(nodeName);
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				minecrafts.add(getMinecraft((Element)nl.item(i)));
			}
		}
		return minecrafts;
	}
	
	private Minecraft getMinecraft(Element mc) {
		return new Minecraft(mc.getAttribute("name"), getTextValue(mc, "jar"), mc.getAttribute("type"));
	}

	public JComboBox getBox() {
		if(box.getItemCount() <= 1) parse(); return box;
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
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
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
