package eu.q_b.asn007.lambda;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.UIManager;

import eu.q_b.asn007.encore.EnCoreBasic;

public class Main {

	public JFrame frame;
	private static EnCoreBasic framework;
	public static Main _instance;
	private JPanel container;

	private JTextField textField;
	private JPasswordField passField;

	private JCheckBox doAuth;
	private JCheckBox rememberMe;
	private JCheckBox forceUpdate;

	private JButton go;

	private JComboBox comboBox;

	private JProgressBar progressBar;
	private Session session;

	public static final ResourceBundle bundle = ResourceBundle
			.getBundle("eu.q_b.asn007.lambda.locale");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					long l = System.currentTimeMillis();
					System.setProperty("minecraft.applet.WrapperClass",
							"eu.q_b.asn007.lambda.Launcher"); // Fuck Forge
					framework = new EnCoreBasic();
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
					_instance = new Main();
					_instance.frame.setIconImage(Main.getFramework()
							.getIntmodule().getImage("icon.png"));
					_instance.frame.setTitle("λauncher version 0.4.1 public");
					_instance.frame.setVisible(true);
					_instance.frame.setLocationRelativeTo(null);
					framework.log(
							"Initialized in "
									+ (System.currentTimeMillis() - l) + "ms",
							Main.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 360, 310);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		JLabel j = new JLabel("λauncher", JLabel.CENTER);
		j.setFont(j.getFont().deriveFont(30F).deriveFont(Font.BOLD));
		j.setAlignmentX(Component.CENTER_ALIGNMENT);
		j.setAlignmentY(Component.CENTER_ALIGNMENT);

		textField = new JTextField(bundle.getString("lambda.textFieldDefault"));
		textField.setPreferredSize(new Dimension(300, 25));
		textField.setMaximumSize(new Dimension(300, 25));
		textField.setMinimumSize(new Dimension(300, 25));
		textField.setAlignmentX(Component.CENTER_ALIGNMENT);
		textField.setAlignmentY(Component.CENTER_ALIGNMENT);
		textField.setHorizontalAlignment(JTextField.CENTER);

		doAuth = new JCheckBox(bundle.getString("lambda.offlineMode"));
		doAuth.setAlignmentX(Component.LEFT_ALIGNMENT);
		doAuth.setAlignmentY(Component.CENTER_ALIGNMENT);
		doAuth.setPreferredSize(new Dimension(300, 25));
		doAuth.setMaximumSize(new Dimension(300, 25));
		doAuth.setMinimumSize(new Dimension(300, 25));

		rememberMe = new JCheckBox(bundle.getString("lambda.rememberMe"));
		rememberMe.setAlignmentX(Component.LEFT_ALIGNMENT);
		rememberMe.setAlignmentY(Component.CENTER_ALIGNMENT);
		rememberMe.setPreferredSize(new Dimension(300, 25));
		rememberMe.setMaximumSize(new Dimension(300, 25));
		rememberMe.setMinimumSize(new Dimension(300, 25));

		forceUpdate = new JCheckBox(bundle.getString("lambda.forceUpdate"));
		forceUpdate.setAlignmentX(Component.LEFT_ALIGNMENT);
		forceUpdate.setAlignmentY(Component.CENTER_ALIGNMENT);
		forceUpdate.setPreferredSize(new Dimension(300, 25));
		forceUpdate.setMaximumSize(new Dimension(300, 25));
		forceUpdate.setMinimumSize(new Dimension(300, 25));

		go = new JButton(bundle.getString("lambda.play"));
		go.setAlignmentX(Component.CENTER_ALIGNMENT);
		go.setAlignmentY(Component.CENTER_ALIGNMENT);

		go.setPreferredSize(new Dimension(300, 25));
		go.setMaximumSize(new Dimension(300, 25));
		go.setMinimumSize(new Dimension(300, 25));

		passField = new JPasswordField(
				bundle.getString("lambda.passFieldDefault"));

		passField.setPreferredSize(new Dimension(300, 25));
		passField.setMaximumSize(new Dimension(300, 25));
		textField.setMinimumSize(new Dimension(300, 25));
		passField.setAlignmentX(Component.CENTER_ALIGNMENT);
		passField.setAlignmentY(Component.CENTER_ALIGNMENT);
		passField.setHorizontalAlignment(JTextField.CENTER);

		comboBox = new JComboBox();
		comboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		comboBox.setAlignmentY(Component.CENTER_ALIGNMENT);

		comboBox.setPreferredSize(new Dimension(300, 25));
		comboBox.setMaximumSize(new Dimension(300, 25));
		comboBox.setMinimumSize(new Dimension(300, 25));

		progressBar = new JProgressBar();
		progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
		progressBar.setAlignmentY(Component.CENTER_ALIGNMENT);
		progressBar.setPreferredSize(new Dimension(300, 30));
		progressBar.setMaximumSize(new Dimension(300, 30));
		progressBar.setMinimumSize(new Dimension(300, 30));

		progressBar.setValue(0);
		progressBar.setVisible(false);

		Box checkboxBox = Box.createVerticalBox();
		checkboxBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		checkboxBox.setAlignmentY(Component.CENTER_ALIGNMENT);

		checkboxBox.add(doAuth);
		checkboxBox.add(Box.createVerticalGlue());

		checkboxBox.add(rememberMe);
		checkboxBox.add(Box.createVerticalGlue());
		checkboxBox.add(forceUpdate);
		checkboxBox.add(Box.createVerticalGlue());

		Box loginBox = Box.createVerticalBox();
		loginBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		loginBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		loginBox.add(Box.createVerticalGlue());
		loginBox.add(textField);
		loginBox.add(Box.createVerticalStrut(5));
		loginBox.add(passField);
		loginBox.add(checkboxBox);
		loginBox.add(comboBox);
		loginBox.add(Box.createVerticalStrut(10));
		loginBox.add(go);
		loginBox.add(Box.createVerticalStrut(10));
		loginBox.add(progressBar);
		// loginBox.add(Box.createVerticalGlue());
		container.add(j);
		container.add(Box.createVerticalGlue());
		container.add(loginBox);
		frame.getContentPane().add(container, "North");
		new MinecraftVersionParser(LauncherConf.xmlAPIURL + "?act=versions",
				comboBox).start();
		go.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread() {

					public void run() {
						String login = (textField.getText() == null || textField
								.getText().equals("")) ? "Player" : textField
								.getText().trim();
						String pass = (new String(passField.getPassword()) == null) ? ""
								: new String(passField.getPassword()).trim();
						if (!doAuth.isSelected()
								&& Main.getFramework().getNetmodule()
										.isOnline() && !login.equals("Player")
								&& !pass.equals("")) {
							String st = Main
									.getFramework()
									.getNetmodule()
									.runPOST(
											LauncherConf.loginURL,
											"user=" + login + "&password="
													+ pass + "&version=13");
							if (!st.contains(":")) {
								if (st.equals("Bad login"))
									JOptionPane.showMessageDialog(
											frame,
											bundle.getString("lambda.badLogin"),
											"ERROR: BAD LOGIN",
											JOptionPane.ERROR_MESSAGE);
								else if (st.equals("User not premium"))
									JOptionPane.showMessageDialog(frame, bundle
											.getString("lambda.notPremium"),
											"ERROR: USER NOT PREMIUM",
											JOptionPane.ERROR_MESSAGE);
								else
									JOptionPane.showMessageDialog(
											frame,
											bundle.getString("lambda.unknownResponse")
													+ st,
											"ERROR: UNKNOWN RESPONSE",
											JOptionPane.ERROR_MESSAGE);
								return;
							} else {
								String[] authResult = st.split(":");
								session = new Session(authResult[2],
										authResult[3]);
							}
						}
						if (session == null)
							session = new Session(login, Main.getFramework()
									.buildRandomSession());
						if (rememberMe.isSelected())
							Main.getFramework()
									.getFileModule()
									.writeString(
											login + "::::" + pass,
											new File(Main.getFramework()
													.getFileModule()
													.getWorkingDirectory()
													+ File.separator + "data"));
						new GameUpdaterThread(session,
								forceUpdate.isSelected(), (Minecraft) comboBox
										.getSelectedItem()).start(); // Знаете,
																		// что я
																		// ненавижу
																		// больше
																		// всего?
																		// Писать
																		// аутентификацию.
					}
				}.start();
			}
		});
		String s[] = Main
				.getFramework()
				.getFileModule()
				.readFileAsString(
						Main.getFramework().getFileModule()
								.getWorkingDirectory()
								+ File.separator + "data",
						bundle.getString("lambda.textFieldDefault") + "::::"
								+ bundle.getString("lambda.passFieldDefault"))
				.split("::::");
		textField.setText(s[0]);
		passField.setText(s[1]);
		textField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if (textField.getText().equals(
						bundle.getString("lambda.textFieldDefault")))
					textField.setText("");

			}

			@Override
			public void focusLost(FocusEvent e) {
				if (textField.getText().equals(""))
					textField.setText(bundle
							.getString("lambda.textFieldDefault"));

			}
		});
		passField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if (new String(passField.getPassword()).equals(bundle
						.getString("lambda.passFieldDefault")))
					passField.setText("");

			}

			@Override
			public void focusLost(FocusEvent e) {
				if (new String(passField.getPassword()).equals(""))
					passField.setText(bundle
							.getString("lambda.passFieldDefault"));

			}
		});
	}

	public static EnCoreBasic getFramework() {
		return framework;
	}

	public static JProgressBar getProgressBar() {
		return _instance.progressBar;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

}
