package eu.q_b.asn007.lambda;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class SettingsDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -897064563083686712L;
	private JTextField memoryField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SettingsDialog dialog = new SettingsDialog();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public SettingsDialog() {
		setBounds(100, 100, 450, 115);
		setResizable(false);
		setLocationRelativeTo(Main._instance.frame);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle(Main.bundle.getString("lambda.settings"));
		setIconImage(Main._instance.frame.getIconImage());
		JPanel container = new JPanel();
		container.setLayout(new SpringLayout());
		getContentPane().add(container);
		container.add(new JLabel(Main.bundle
				.getString("lambda.settings.memory")));
		memoryField = new JTextField(Main
				.getFramework()
				.getFileModule()
				.readFileAsString(
						Main.getFramework().getFileModule()
								.getWorkingDirectory()
								+ File.separator + "memory", "1024"));
		container.add(memoryField);
		JLabel j = new JLabel(Main.bundle.getString("lambda.settings.after"),
				JLabel.CENTER);
		j.setFont(j.getFont().deriveFont(10F));
		container.add(j);
		container.add(Box.createRigidArea(new Dimension(0, 0)));
		JButton saveButton = new JButton(
				Main.bundle.getString("lambda.settings.save"));
		saveButton.setPreferredSize(new Dimension(200, 25));
		saveButton.setMaximumSize(new Dimension(200, 25));

		JButton wipeButton = new JButton(
				Main.bundle.getString("lambda.settings.wipe"));
		wipeButton.setPreferredSize(new Dimension(200, 25));
		wipeButton.setMaximumSize(new Dimension(200, 25));

		container.add(saveButton);
		container.add(wipeButton);
		SpringUtilities.makeCompactGrid(container, 3, 2, 0, 10, 0, 5);

		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Main.getFramework()
						.getFileModule()
						.writeString(
								Main.getFramework().filterString(
										memoryField.getText()),
								new File(Main.getFramework().getFileModule()
										.getWorkingDirectory()
										+ File.separator + "memory"));
			}
		});

		wipeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JButton j = (JButton) e.getSource();
				j.setText(Main.bundle.getString("lambda.settings.wiping"));
				for (int i = 0; i < Main._instance.getComboBox().getItemCount(); i++) {
					new File(Main.getFramework().getFileModule()
							.getWorkingDirectory()
							+ File.separator
							+ "bin"
							+ File.separator
							+ ((Minecraft) Main._instance.getComboBox()
									.getItemAt(i)).getVersion() + ".jar")
							.delete();
				}
				j.setText(Main.bundle.getString("lambda.settings.wiped"));
			}

		});

	}
}
