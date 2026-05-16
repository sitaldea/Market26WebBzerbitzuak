package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class MainGUIAdmin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String usermail;
	private JLabel lblAukeratu;
	private JButton btnErreklamazioaIkusi;
	private JButton btnLogOut;
	private JRadioButton rdbtnEnglish;
	private JRadioButton rdbtnEuskara;
	private JRadioButton rdbtnCastellano;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	/**
	 * Create the frame.
	 */
	public MainGUIAdmin(String email) {
		this.usermail = email;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(4, 0, 0, 0));

		lblAukeratu = new JLabel();
		lblAukeratu.setText(
				ResourceBundle.getBundle("Etiquetas").getString("MainGUIAdmin.aukera"));
		lblAukeratu.setHorizontalAlignment(SwingConstants.CENTER);
		lblAukeratu.setFont(new Font("Tahoma", Font.BOLD, 12));
		contentPane.add(lblAukeratu);

		btnErreklamazioaIkusi = new JButton();
		btnErreklamazioaIkusi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ErreklamazioakEbatziGUI erreklamazioakAdminGui = new ErreklamazioakEbatziGUI(usermail);
				erreklamazioakAdminGui.setVisible(true);
			}
		});
		btnErreklamazioaIkusi.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUIAdmin.erreklamazioakIkusi"));
		btnErreklamazioaIkusi.setFont(new Font("Tahoma", Font.BOLD, 12));
		contentPane.add(btnErreklamazioaIkusi);

		btnLogOut = new JButton();
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnLogOut.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUIAdmin.logOut"));
		btnLogOut.setFont(new Font("Tahoma", Font.BOLD, 12));
		contentPane.add(btnLogOut);

		JPanel panel = new JPanel();

		rdbtnEnglish = new JRadioButton("English");
		rdbtnEnglish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Locale.setDefault(new Locale("en"));
				paintAgain();
			}
		});
		buttonGroup.add(rdbtnEnglish);

		rdbtnEuskara = new JRadioButton("Euskara");
		rdbtnEuskara.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Locale.setDefault(new Locale("eus"));
				paintAgain();
			}
		});
		buttonGroup.add(rdbtnEuskara);

		rdbtnCastellano = new JRadioButton("Castellano");
		rdbtnCastellano.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Locale.setDefault(new Locale("es"));
				paintAgain();
			}
		});
		buttonGroup.add(rdbtnCastellano);

		panel.add(rdbtnEuskara);
		panel.add(rdbtnCastellano);
		panel.add(rdbtnEnglish);
		contentPane.add(panel);

		setTitle(this.usermail);
		setVisible(true);
	}

	private void paintAgain() {
		lblAukeratu.setText(
				ResourceBundle.getBundle("Etiquetas").getString("MainGUIAdmin.aukera"));
		btnErreklamazioaIkusi.setText(
				ResourceBundle.getBundle("Etiquetas").getString("MainGUIAdmin.erreklamazioakIkusi"));
		btnLogOut.setText(
				ResourceBundle.getBundle("Etiquetas").getString("MainGUIAdmin.logOut"));
	}
}