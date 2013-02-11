package eu.q_b.asn007.lambda;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.net.URL;

import javax.swing.JFrame;

public class MinecraftLoader extends JFrame {

	private static final long serialVersionUID = -2287752224571892380L;

	public MinecraftLoader(String u, String s, Minecraft minecraft2) {

		URL[] urls = new URL[4];

		try {
			urls[0] = new File(Main.getFramework().getFileModule()
					.getWorkingDirectory()
					+ File.separator + "bin" + File.separator,
					minecraft2.getMinecraftJarName()).toURI().toURL();
			urls[1] = new File(Main.getFramework().getFileModule()
					.getWorkingDirectory()
					+ File.separator + "bin" + File.separator, "lwjgl.jar")
					.toURI().toURL();
			urls[2] = new File(Main.getFramework().getFileModule()
					.getWorkingDirectory()
					+ File.separator + "bin" + File.separator, "jinput.jar")
					.toURI().toURL();
			urls[3] = new File(Main.getFramework().getFileModule()
					.getWorkingDirectory()
					+ File.separator + "bin" + File.separator, "lwjgl_util.jar")
					.toURI().toURL();

			final Launcher gameapplet = new Launcher(Main.getFramework()
					.getFileModule().getWorkingDirectory()
					+ File.separator + "bin" + File.separator, urls);
			gameapplet.customParameters.put("username", u);
			gameapplet.customParameters.put("sessionid", s);
			gameapplet.customParameters.put("stand-alone", "true");
			gameapplet.customParameters.put("demo", "false");

			this.addWindowListener(new WindowListener() {

				@Override
				public void windowOpened(WindowEvent e) {

				}

				@Override
				public void windowClosing(WindowEvent e) {
					System.out.println("Stopping game and exiting...");
					gameapplet.stop();
					gameapplet.destroy();
					System.exit(0);
				}

				@Override
				public void windowClosed(WindowEvent e) {

				}

				@Override
				public void windowIconified(WindowEvent e) {

				}

				@Override
				public void windowDeiconified(WindowEvent e) {

				}

				@Override
				public void windowActivated(WindowEvent e) {

				}

				@Override
				public void windowDeactivated(WindowEvent e) {

				}

			});
			this.setTitle("Minecraft " + minecraft2.getVersion());

			this.setBounds(0, 0, 1000, 600);
			this.setLocationRelativeTo(null);

			this.setLayout(new BorderLayout());
			this.setBackground(Color.WHITE);
			this.setIconImage(Main._instance.frame.getIconImage());
			this.add(gameapplet, "Center");

			validate();
			this.setVisible(true);
			validate();
			repaint();
			gameapplet.init(u, s);
			gameapplet.start();
			repaint();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			System.exit(0);
		}
	}
}