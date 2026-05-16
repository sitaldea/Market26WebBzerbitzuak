package gui;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import businessLogic.BLFacade;
import domain.*;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.awt.event.ActionEvent;

public class RegisterGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldEmail;
	private JPasswordField passwordFieldPass;
	private JPasswordField passwordFieldPass2;
	private JTextField textTelefonoa;
	private JTextField textIzena;

	/**
	 * Launch the application.
	 */
	
	/**
	 * Create the frame.
	 * @param sellerMail 
	 */
	public RegisterGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 631, 397);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblEmail = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Email"));
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEmail.setBounds(93, 67, 109, 12);
		contentPane.add(lblEmail);
		
		JLabel lblPasahitza = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Password"));
		lblPasahitza.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPasahitza.setBounds(93, 140, 109, 12);
		contentPane.add(lblPasahitza);
		
		JLabel lblPasahitzaRep = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.ConfirmPassword"));
		lblPasahitzaRep.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPasahitzaRep.setBounds(89, 179, 120, 18);
		contentPane.add(lblPasahitzaRep);
		
		textFieldEmail = new JTextField();
		textFieldEmail.setBounds(212, 65, 237, 18);
		contentPane.add(textFieldEmail);
		textFieldEmail.setColumns(10);
		
		passwordFieldPass = new JPasswordField();
		passwordFieldPass.setBounds(212, 138, 237, 18);
		contentPane.add(passwordFieldPass);
		
		passwordFieldPass2 = new JPasswordField();
		passwordFieldPass2.setBounds(212, 180, 237, 18);
		contentPane.add(passwordFieldPass2);
		
		JLabel mensaje = new JLabel("");
		mensaje.setFont(new Font("Tahoma", Font.PLAIN, 12));
		mensaje.setBounds(161, 312, 300, 18);
		contentPane.add(mensaje);
		
		JButton btnErregistratu = new JButton(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Register"));
		btnErregistratu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			    BLFacade facade = MainGUI.getBusinessLogic();
			    String email = textFieldEmail.getText().trim();
			    String telefonoa = textTelefonoa.getText().trim();
			    String izena = textIzena.getText().trim();
			    String pasword1 = new String(passwordFieldPass.getPassword());
			    String pasword2 = new String(passwordFieldPass2.getPassword());

			    if(email.isEmpty() || telefonoa.isEmpty() || izena.isEmpty() || pasword1.isEmpty() || pasword2.isEmpty()) {
			        mensaje.setForeground(Color.RED);
			        mensaje.setText(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.ErrorQueary"));
			        return;
			    }

			    boolean userExists = facade.isLoggedIn(email);
			    if(userExists) {
			        mensaje.setForeground(Color.RED);
			        mensaje.setText(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.ErrorEmail"));
			        return;
			    }

			    if(!pasword1.equals(pasword2)) {
			        mensaje.setForeground(Color.RED);
			        mensaje.setText(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.ErrorPassword"));
			        return;
			    }

			    facade.addUser(email, pasword1, izena, telefonoa);
			    mensaje.setForeground(new Color(0, 128, 0));
			    mensaje.setText(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Ondo"));

			    textFieldEmail.setText("");
			    textTelefonoa.setText("");
			    textIzena.setText("");
			    passwordFieldPass.setText("");
			    passwordFieldPass2.setText("");
			}
		});
		btnErregistratu.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnErregistratu.setBounds(167, 248, 116, 33);
		contentPane.add(btnErregistratu);
		
		JLabel lblTelefonoa = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Telephone"));
		lblTelefonoa.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTelefonoa.setBounds(93, 105, 109, 12);
		contentPane.add(lblTelefonoa);
		
		JLabel lblIzena = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Name"));
		lblIzena.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblIzena.setBounds(93, 38, 109, 12);
		contentPane.add(lblIzena);
		
		textTelefonoa = new JTextField();
		textTelefonoa.setBounds(212, 103, 237, 18);
		contentPane.add(textTelefonoa);
		textTelefonoa.setColumns(10);
		
		textIzena = new JTextField();
		textIzena.setBounds(212, 36, 237, 18);
		contentPane.add(textIzena);
		textIzena.setColumns(10);
		
		JButton btnNewButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.CancelButton"));
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton.setBounds(333, 249, 128, 33);
		contentPane.add(btnNewButton);

	}
}
