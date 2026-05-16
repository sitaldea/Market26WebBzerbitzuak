package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import businessLogic.BLFacade;

import java.awt.Font;

public class EskaeraEginGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String userMail;
	private JLabel label;
	private JButton sendButton;
	private JButton closeButton;

	/**
	 * Create the frame.
	 */
	public EskaeraEginGUI(String email) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 511, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel centerPanel = new JPanel();
		centerPanel.setBounds(5, 5, 487, 144);
		label = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("EskaerakEginGUI.label"));
		label.setFont(new Font("Tahoma", Font.BOLD, 11));
		label.setBounds(50, 81, 135, 13);
		JTextField textField = new JTextField(30);
		textField.setBounds(195, 78, 246, 19);
		centerPanel.setLayout(null);
		centerPanel.add(label);
		centerPanel.add(textField);
		contentPane.add(centerPanel);

		JPanel southPanel = new JPanel();
		southPanel.setBounds(5, 178, 487, 57);
		sendButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("EskaerakEginGUI.send"));
		sendButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		sendButton.setBounds(110, 5, 127, 38);
		closeButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.CancelButton"));
		closeButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		closeButton.setBounds(247, 5, 127, 38);

		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String eskaera = textField.getText();
				if(eskaera.isEmpty()) {
					JOptionPane.showMessageDialog(EskaeraEginGUI.this, ResourceBundle.getBundle("Etiquetas").getString("EskaerakEginGUI.error"), "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					BLFacade facade = MainGUI.getBusinessLogic();
					facade.createEskaera(eskaera, userMail);
					JOptionPane.showMessageDialog(EskaeraEginGUI.this, ResourceBundle.getBundle("Etiquetas").getString("EskaerakEginGUI.success"), "Success", JOptionPane.INFORMATION_MESSAGE);
					textField.setText("");
					
				}
			}
		});

		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		southPanel.setLayout(null);

		southPanel.add(sendButton);
		southPanel.add(closeButton);
		contentPane.add(southPanel);

		this.userMail = email;
		this.setTitle(userMail);
	}

}