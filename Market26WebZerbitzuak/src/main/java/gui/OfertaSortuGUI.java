package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;

import businessLogic.BLFacade;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.awt.event.ActionEvent;

import domain.Eskaera;


import javax.swing.JTextField;

public class OfertaSortuGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Eskaera eskaera;
	private JTextField textTitle;
	private JTextField textDesk;
	private JTextField textPrice;
	private JButton btnSortu;
	private JButton btnClose;
	private JLabel lblTitle;
	private JLabel lblDesk;
	private JLabel lblPrice;
	private String userMail;

	/**
	 * Create the frame.
	 */
	public OfertaSortuGUI(Eskaera eskaera, String userMail) {
		this.eskaera = eskaera;
		this.userMail = userMail;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 499, 340);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setTitle(userMail);
		
		btnClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("OfertaSortuGUI.btnClose"));
		btnClose.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnClose.setBounds(375, 268, 100, 25);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		contentPane.add(btnClose);
		
		lblTitle = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("OfertaSortuGUI.title"));
		lblTitle.setBounds(37, 38, 149, 12);
		contentPane.add(lblTitle);
		
		lblDesk = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("OfertaSortuGUI.description"));
		lblDesk.setBounds(37, 78, 149, 12);
		contentPane.add(lblDesk);
		
		lblPrice = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("OfertaSortuGUI.price"));
		lblPrice.setBounds(37, 158, 149, 12);
		contentPane.add(lblPrice);
		
		textTitle = new JTextField();
		textTitle.setBounds(196, 35, 234, 18);
		contentPane.add(textTitle);
		textTitle.setColumns(10);
		
		textDesk = new JTextField();
		textDesk.setBounds(196, 78, 234, 59);
		contentPane.add(textDesk);
		textDesk.setColumns(10);
		
		textPrice = new JTextField();
		textPrice.setBounds(196, 155, 96, 18);
		contentPane.add(textPrice);
		textPrice.setColumns(10);
		
		btnSortu = new JButton(ResourceBundle.getBundle("Etiquetas").getString("OfertaSortuGUI.btnSortu"));
		btnSortu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String title = textTitle.getText();
					String description = textDesk.getText();
					double price = Double.parseDouble(textPrice.getText());
					BLFacade facade = MainGUI.getBusinessLogic();
					facade.createOferta(title, description, price, userMail, eskaera.getId());
					
					JOptionPane.showMessageDialog(OfertaSortuGUI.this, 
						ResourceBundle.getBundle("Etiquetas").getString("OfertaSortuGUI.successMessage"), 
						ResourceBundle.getBundle("Etiquetas").getString("OfertaSortuGUI.successTitle"), 
						JOptionPane.INFORMATION_MESSAGE);
					
					dispose();
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(OfertaSortuGUI.this, 
						ResourceBundle.getBundle("Etiquetas").getString("OfertaSortuGUI.errorPrice"), 
						ResourceBundle.getBundle("Etiquetas").getString("OfertaSortuGUI.errorTitle"), 
						JOptionPane.ERROR_MESSAGE);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(OfertaSortuGUI.this, 
						ResourceBundle.getBundle("Etiquetas").getString("OfertaSortuGUI.errorCreating"), 
						ResourceBundle.getBundle("Etiquetas").getString("OfertaSortuGUI.errorTitle"), 
						JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		});
		btnSortu.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnSortu.setBounds(151, 212, 171, 37);
		contentPane.add(btnSortu);

	}
}