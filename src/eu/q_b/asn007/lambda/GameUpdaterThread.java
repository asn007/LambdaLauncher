package eu.q_b.asn007.lambda;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import eu.q_b.asn007.encore.UnZipper;

public class GameUpdaterThread extends Thread {
	private Minecraft minecraft;
	private Session session;
	private boolean forceUpdate;
	private HashMap<File, URL> map = new HashMap<File, URL>();
	private boolean natives = new File(Main.getFramework().getFileModule()
			.getWorkingDirectory()
			+ File.separator + "bin" + File.separator + "natives").exists();
	private File origJar = new File(Main.getFramework().getFileModule()
			.getWorkingDirectory()
			+ File.separator + "bin" + File.separator + "orig.jar");
	private File binFolder = new File(Main.getFramework().getFileModule()
			.getWorkingDirectory()
			+ File.separator + "bin");
	private boolean lwjgl = new File(binFolder + File.separator + "lwjgl.jar")
			.exists();
	private boolean lwjgl_util = new File(binFolder + File.separator
			+ "lwjgl_util.jar").exists();
	private boolean jinput = new File(binFolder + File.separator + "jinput.jar")
			.exists();

	// TODO: Code better update algorithm
	public GameUpdaterThread(Session session, boolean b, Minecraft mc) {
		this.session = session;
		this.forceUpdate = b;
		this.minecraft = mc;
		if (!origJar.exists() || isOrigJarBroken()) {
			map.put(origJar,
					Main.getFramework().toURL(
							"http://assets.minecraft.net/1_4_7/minecraft.jar"));
		}
		if (!new File(binFolder + File.separator
				+ minecraft.getMinecraftJarName()).exists()
				|| forceUpdate) {
			map.put(new File(binFolder + File.separator
					+ minecraft.getMinecraftPatchName()), Main.getFramework()
					.toURL(minecraft.getJarURL()));
		}
		if (!natives || forceUpdate) {
			map.put(new File(Main.getFramework().getFileModule()
					.getWorkingDirectory()
					+ File.separator + "bin" + File.separator + "natives.zip"),
					Main.getFramework().toURL(
							LauncherConf.downloadURL
									+ Main.getFramework().getOsmodule()
											.getPlatform().toString()
									+ "_natives.zip"));
		}

		if (!lwjgl || forceUpdate) {
			map.put(new File(binFolder + File.separator + "lwjgl.jar"),
					Main.getFramework().toURL(
							LauncherConf.downloadURL + "lwjgl.jar"));
		}

		if (!lwjgl_util || forceUpdate) {
			map.put(new File(binFolder + File.separator + "lwjgl_util.jar"),
					Main.getFramework().toURL(
							LauncherConf.downloadURL + "lwjgl_util.jar"));
		}

	}

	public void run() {
		if (needsUpdate())
			runUpdate();
		loadGame(session, minecraft);
	}

	private void runUpdate() {
		Main.getProgressBar().setVisible(true);
		Main.getProgressBar().setStringPainted(true);
		for (Entry<File, URL> entry : map.entrySet()) {
			Main.getFramework().log(
					"Downloading file " + entry.getKey().getName() + " from "
							+ entry.getValue(), this.getClass());
			try {
				Main.getFramework()
						.getNetmodule()
						.download(entry.getValue(), entry.getKey(), null,
								Main.getProgressBar());
			} catch (Exception e1) {
				Main.getFramework().log(
						"Error while downloading file "
								+ entry.getKey().getName()
								+ ", stacktrace below:\n"
								+ Main.getFramework().stack2string(e1),
						this.getClass());
			}
		}
		File minecraftJar = new File(binFolder + File.separator
				+ minecraft.getMinecraftJarName());

		try {
			minecraftJar.createNewFile();
			BSPatcher.bspatch(
					origJar,
					new FileOutputStream(minecraftJar),
					new File(binFolder + File.separator
							+ minecraft.getMinecraftPatchName()));

			UnZipper uz = new UnZipper();
			uz.recursiveUnzip(new File(binFolder + File.separator
					+ "natives.zip"), new File(binFolder + File.separator
					+ "natives"));

			// Perform cleanup
			new File(binFolder + File.separator
					+ minecraft.getMinecraftPatchName()).delete();

			uz.removeAllZipFiles(binFolder);
		} catch (IOException e) {
			Main.getFramework().log(Main.getFramework().stack2string(e),
					this.getClass());
		}

	}

	private void loadGame(Session session2, Minecraft minecraft2) {
		new MinecraftLoader(session2.getUser(), session2.getSession(),
				minecraft2);
		Main._instance.frame.setVisible(false);
	}

	private boolean isOrigJarBroken() {
		String jarsum = Main.getFramework().getNetmodule()
				.runGET(LauncherConf.xmlAPIURL, "act=origjarsum");
		if (!jarsum.equals(Main.getFramework().getFileModule().getMD5(origJar)))
			return true;
		return false;
	}

	private boolean needsUpdate() {
		if (!Main.getFramework().getNetmodule().isOnline()
				&& !isInstalled(minecraft)) {
			JOptionPane.showMessageDialog(Main._instance.frame,
					Main.bundle.getString("lambda.updaterNotOnline"),
					"ERROR: NOT ONLINE", JOptionPane.ERROR_MESSAGE);
		}
		if (forceUpdate && Main.getFramework().getNetmodule().isOnline())
			return true;
		if (!isInstalled(minecraft))
			return true;
		return false;
	}

	private boolean isInstalled(Minecraft minecraft2) {

		if (!lwjgl
				|| !jinput
				|| !natives
				|| !new File(binFolder + File.separator
						+ minecraft.getMinecraftJarName()).exists())
			return false;
		return true;
	}

}
