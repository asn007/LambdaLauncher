package eu.q_b.asn007.lambda;

import java.io.File;
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

	// TODO: Code better update algorithm

	public GameUpdaterThread(Session session, boolean b, Minecraft minecraft) {
		this.session = session;
		this.forceUpdate = b;
		this.minecraft = minecraft;

		map.put(new File(Main.getFramework().getFileModule()
				.getWorkingDirectory()
				+ File.separator
				+ "bin"
				+ File.separator
				+ minecraft.getVersion() + ".jar"),
				Main.getFramework().toURL(
						"http://lambda.q-b.eu/"
								+ minecraft.getMinecrafJarPath()));

		if (!new File(Main.getFramework().getFileModule().getWorkingDirectory()
				+ File.separator + "bin" + File.separator + "lwjgl.jar")
				.exists() || forceUpdate) {
			map.put(new File(Main.getFramework().getFileModule()
					.getWorkingDirectory()
					+ File.separator + "bin" + File.separator + "lwjgl.jar"),
					Main.getFramework().toURL(
							LauncherConf.downloadURL + "lwjgl.jar"));
		}

		if (!new File(Main.getFramework().getFileModule().getWorkingDirectory()
				+ File.separator + "bin" + File.separator + "lwjgl_util.jar")
				.exists() || forceUpdate) {
			map.put(new File(Main.getFramework().getFileModule()
					.getWorkingDirectory()
					+ File.separator
					+ "bin"
					+ File.separator
					+ "lwjgl_util.jar"),
					Main.getFramework().toURL(
							LauncherConf.downloadURL + "lwjgl_util.jar"));
		}
		if (!new File(Main.getFramework().getFileModule().getWorkingDirectory()
				+ File.separator + "bin" + File.separator + "jinput.jar")
				.exists() || forceUpdate) {
			map.put(new File(Main.getFramework().getFileModule()
					.getWorkingDirectory()
					+ File.separator + "bin" + File.separator + "jinput.jar"),
					Main.getFramework().toURL(
							LauncherConf.downloadURL + "jinput.jar"));
		}
		if (!new File(Main.getFramework().getFileModule().getWorkingDirectory()
				+ File.separator + "bin" + File.separator + "natives").exists()
				|| forceUpdate) {
			map.put(new File(Main.getFramework().getFileModule()
					.getWorkingDirectory()
					+ File.separator + "bin" + File.separator + "natives.zip"),
					Main.getFramework().toURL(
							LauncherConf.downloadURL
									+ Main.getFramework().getOsmodule()
											.getPlatform().toString()
									+ "_natives.zip"));
		}

	}

	public void run() {
		if (needsUpdate())
			runUpdate();
		loadGame(session, minecraft);
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

		if (!new File(Main.getFramework().getFileModule().getWorkingDirectory()
				+ File.separator + "bin" + File.separator + "lwjgl.jar")
				.exists()
				|| !new File(Main.getFramework().getFileModule()
						.getWorkingDirectory()
						+ File.separator
						+ "bin"
						+ File.separator
						+ "lwjgl_util.jar").exists()
				|| !new File(Main.getFramework().getFileModule()
						.getWorkingDirectory()
						+ File.separator
						+ "bin"
						+ File.separator
						+ "jinput.jar").exists()
				|| !new File(Main.getFramework().getFileModule()
						.getWorkingDirectory()
						+ File.separator + "bin" + File.separator + "natives")
						.exists()
				|| !new File(Main.getFramework().getFileModule()
						.getWorkingDirectory()
						+ File.separator
						+ "bin"
						+ File.separator
						+ minecraft2.getVersion() + ".jar").exists())
			return false;

		return true;
	}

	private void runUpdate() {
		Main.getProgressBar().setVisible(true);
		Main.getProgressBar().setStringPainted(true);
		for (Entry<File, URL> e : map.entrySet()) {
			try {
				Main.getFramework()
						.getNetmodule()
						.download(e.getValue(), e.getKey(), null,
								Main.getProgressBar());
			} catch (Exception e1) {
				Main.getFramework().log(
						"Error while downloading file " + e.getKey().getName()
								+ ", stacktrace below:\n"
								+ Main.getFramework().stack2string(e1),
						this.getClass());
			}
		}

		UnZipper uz = new UnZipper();
		if (!natives)
			uz.recursiveUnzip(new File(Main.getFramework().getFileModule()
					.getWorkingDirectory()
					+ File.separator + "bin" + File.separator + "natives.zip"),
					new File(Main.getFramework().getFileModule()
							.getWorkingDirectory()
							+ File.separator
							+ "bin"
							+ File.separator
							+ "natives"));
		uz.removeAllZipFiles(new File(Main.getFramework().getFileModule()
				.getWorkingDirectory()
				+ File.separator + "bin"));

	}

	private void loadGame(Session session2, Minecraft minecraft2) {
		// Поехали :3
		Main._instance.frame.setVisible(false);
		new MinecraftLoader(session2.getUser(), session2.getSession(),
				minecraft2);

	}

}
