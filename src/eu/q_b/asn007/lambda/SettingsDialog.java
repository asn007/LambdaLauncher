package eu.q_b.asn007.lambda;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class SettingsDialog extends JDialog {

	private static final long serialVersionUID = -897064563083686712L;
	private JTextField memoryField;

	/**
	 * Create the dialog.
	 */
	public SettingsDialog() {
		setBounds(100, 100, 450, 140);
		setResizable(false);
		setLocationRelativeTo(Main._instance.frame);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle(Main.bundle.getString("lambda.settings"));
		setIconImage(Main._instance.frame.getIconImage());
		JPanel container = new JPanel();
		container.setLayout(new SpringLayout());
		getContentPane().add(container);

		memoryField = new JTextField(Main
				.getFramework()
				.getFileModule()
				.readFileAsString(
						Main.getFramework().getFileModule()
								.getWorkingDirectory()
								+ File.separator + "memory", "1024"));

		JLabel j = new JLabel(Main.bundle.getString("lambda.settings.after"),
				JLabel.CENTER);
		j.setFont(j.getFont().deriveFont(10F));

		JButton saveButton = new JButton(
				Main.bundle.getString("lambda.settings.save"));
		saveButton.setPreferredSize(new Dimension(200, 25));
		saveButton.setMaximumSize(new Dimension(200, 25));

		JButton wipeButton = new JButton(
				Main.bundle.getString("lambda.settings.wipe"));
		wipeButton.setPreferredSize(new Dimension(200, 25));
		wipeButton.setMaximumSize(new Dimension(200, 25));

		JButton forgetButton = new JButton(
				Main.bundle.getString("lambda.settings.forget"));
		forgetButton.setPreferredSize(new Dimension(400, 25));
		forgetButton.setMaximumSize(new Dimension(400, 25));

		JButton purgeButton = new JButton(
				Main.bundle.getString("lambda.settings.purge"));
		purgeButton.setPreferredSize(new Dimension(400, 25));
		purgeButton.setMaximumSize(new Dimension(400, 25));
		purgeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JButton j = (JButton) e.getSource();
				j.setText(Main.bundle.getString("lambda.settings.purging"));
				try {
					Main.getFramework()
							.getFileModule()
							.recursiveDelete(
									Main.getFramework().getFileModule()
											.getWorkingDirectory());
					j.setText(Main.bundle
							.getString("lambda.settings.purgingcompleted"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					Main.getFramework().log(
							Main.getFramework().stack2string(e1),
							this.getClass());
					j.setText(Main.bundle
							.getString("lambda.settings.purgingerror"));
				}

			}
		});

		container.add(new JLabel(Main.bundle
				.getString("lambda.settings.memory")));
		container.add(memoryField);
		container.add(j);
		container.add(Box.createRigidArea(new Dimension(0, 0)));
		container.add(saveButton);
		container.add(wipeButton);
		container.add(purgeButton);
		container.add(forgetButton);
		SpringUtilities.makeCompactGrid(container, 4, 2, 0, 10, 0, 5);

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

		purgeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

	}
}
