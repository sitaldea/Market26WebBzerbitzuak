package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Panel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import net.miginfocom.swing.MigLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import businessLogic.BLFacade;
import domain.User;

public class MainGUIErregistratuta extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String userMail;
	private JRadioButton rdbtnNewRadioButton;
	private JRadioButton rdbtnNewRadioButton_1;
	private JRadioButton rdbtnNewRadioButton_2;
	private JPanel panel;
	private JButton btnQuearySale;
	private JButton btnCreateSale;
	private JLabel lblSelectOption;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JButton btnErositakoProduktuak;
	private JButton btnDirua;
	private JButton btnMugimenduak;
	private JButton btnNeLogOut;
	private JButton btnErreklamazioakIkusi;
	private JButton btnEskaerakEgin;
	private JButton btnBalorazioakIkusi;
	private JButton btnEskaerakIkusi;
	private JButton btnOfertakIkusi;

	/**
	 * Create the frame.
	 * @param email 
	 */
	public MainGUIErregistratuta(String email) {
		this.userMail = email;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 552, 416);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblSelectOption = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SelectOption"));
		lblSelectOption.setBounds(15, 17, 506, 29);
		lblSelectOption.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblSelectOption);

		btnCreateSale = new JButton();
		btnCreateSale.setBounds(15, 52, 238, 38);
		btnCreateSale.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.CreateSale"));
		btnCreateSale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame a = new CreateSaleGUI(userMail);
				a.setVisible(true);
			}
		});
		contentPane.add(btnCreateSale);

		btnDirua = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUIErregistratuta.Dirua"));
		btnDirua.setBounds(284, 52, 237, 38);
		btnDirua.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new DiruaSartuAteraGUI(userMail);
				a.setVisible(true);
			}
		});
		contentPane.add(btnDirua);

		btnQuearySale = new JButton();
		btnQuearySale.setBounds(15, 96, 238, 37);
		btnQuearySale.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.QuerySales"));
		btnQuearySale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame a = new QuerySalesGUI(userMail, 0);
				a.setVisible(true);
			}
		});
		contentPane.add(btnQuearySale);

		btnMugimenduak = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUIErregistratuta.Mugimenduak"));
		btnMugimenduak.setBounds(284, 96, 237, 37);
		btnMugimenduak.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame a = new MugimenduakIkusiGUI(userMail);
				a.setVisible(true);
			}
		});
		contentPane.add(btnMugimenduak);

		btnErositakoProduktuak = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUIErregistratuta.ErositakoProduktuakIkusi"));
		btnErositakoProduktuak.setBounds(15, 139, 238, 38);
		btnErositakoProduktuak.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent arg0) {
		        BLFacade facade = MainGUI.getBusinessLogic();
		        if (!facade.hasErositakoak(userMail)) {
		            JOptionPane.showMessageDialog(MainGUIErregistratuta.this, 
		                "Ez dago erositako produkturik.", "Info", JOptionPane.INFORMATION_MESSAGE);
		        } else {
		            JFrame a = new ErositakoProduktuakIkusiGUI(userMail);
		            a.setVisible(true);
		        }
		    }
		});
		contentPane.add(btnErositakoProduktuak);

		btnErreklamazioakIkusi = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUIErregistratuta.ErreklamazioakIkusi"));
		btnErreklamazioakIkusi.setBounds(284, 139, 237, 38);
		btnErreklamazioakIkusi.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent arg0) {
		        BLFacade facade = MainGUI.getBusinessLogic();
		        if (!facade.hasErreklamazioak(userMail)) {
		            JOptionPane.showMessageDialog(MainGUIErregistratuta.this, 
		                "Ez dago erreklamaziorik.", "Info", JOptionPane.INFORMATION_MESSAGE);
		        } else {
		            JFrame a = new ErreklamazioakOnartuDeuseztatuGUI(userMail);
		            a.setVisible(true);
		        }
		    }
		});
		contentPane.add(btnErreklamazioakIkusi);
				
						rdbtnNewRadioButton = new JRadioButton("English");
						rdbtnNewRadioButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								Locale.setDefault(new Locale("en"));
								paintAgain();
							}
						});
						buttonGroup.add(rdbtnNewRadioButton);
						
								rdbtnNewRadioButton_1 = new JRadioButton("Euskara");
								rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent arg0) {
										Locale.setDefault(new Locale("eus"));
										paintAgain();
									}
								});
								buttonGroup.add(rdbtnNewRadioButton_1);
								
										rdbtnNewRadioButton_2 = new JRadioButton("Castellano");
										rdbtnNewRadioButton_2.addActionListener(new ActionListener() {
											public void actionPerformed(ActionEvent e) {
												Locale.setDefault(new Locale("es"));
												paintAgain();
											}
										});
										
										btnEskaerakEgin = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUIErregistratuta.EskaerakEgin")); 
										btnEskaerakEgin.setBounds(15, 187, 238, 38);
										btnEskaerakEgin.addActionListener(new ActionListener() {
											public void actionPerformed(ActionEvent arg0) {
												JFrame a = new EskaeraEginGUI(userMail);
												a.setVisible(true);
											}
										});
										
										
										btnEskaerakIkusi = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUIErregistratuta.EskaerakIkusi"));
										btnEskaerakIkusi.addActionListener(new ActionListener() {
											public void actionPerformed(ActionEvent e) {
												JFrame a = new EskaerakIkusiGUI(userMail);
												a.setVisible(true);
											}
										});
										btnEskaerakIkusi.setBounds(15, 236, 238, 38);
										contentPane.add(btnEskaerakIkusi);
										
										
										contentPane.add(btnEskaerakEgin);
										
										btnBalorazioakIkusi = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUIErregistratuta.BalorazioakIkusi"));
										btnBalorazioakIkusi.setBounds(284, 187, 237, 38);
										btnBalorazioakIkusi.addActionListener(new ActionListener() {
											public void actionPerformed(ActionEvent e) {
												JFrame a = new BalorazioakIkusiGUI(userMail);
												a.setVisible(true);
											}
										});
										contentPane.add(btnBalorazioakIkusi);
										buttonGroup.add(rdbtnNewRadioButton_2);
										
												panel = new JPanel();
												panel.setBounds(15, 286, 506, 32);
												panel.add(rdbtnNewRadioButton_1);
												panel.add(rdbtnNewRadioButton_2);
												panel.add(rdbtnNewRadioButton);
												contentPane.add(panel);
				
						btnNeLogOut = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUIErregistratuta.SaioaItxi"));
						btnNeLogOut.setBounds(201, 328, 137, 30);
						btnNeLogOut.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								dispose();	
							}
						});
						contentPane.add(btnNeLogOut);
						
						btnOfertakIkusi = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUIErregistratuta.OfertakIkusi")); 
						btnOfertakIkusi.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								JFrame a = new OfertakIkusiGUI(userMail);
								a.setVisible(true);
							}
						});
						btnOfertakIkusi.setBounds(284, 236, 237, 38);
						contentPane.add(btnOfertakIkusi);


		setTitle(userMail);
	}

	private void paintAgain() {
		lblSelectOption.setText(
				ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SelectOption"));
		btnCreateSale.setText(
				ResourceBundle.getBundle("Etiquetas").getString("MainGUI.CreateSale"));
		btnQuearySale.setText(
				ResourceBundle.getBundle("Etiquetas").getString("MainGUI.QuerySales"));
		btnErositakoProduktuak.setText(
				ResourceBundle.getBundle("Etiquetas").getString("MainGUIErregistratuta.ErositakoProduktuakIkusi"));
		btnDirua.setText(
				ResourceBundle.getBundle("Etiquetas").getString("MainGUIErregistratuta.Dirua"));
		btnMugimenduak.setText(
				ResourceBundle.getBundle("Etiquetas").getString("MainGUIErregistratuta.Mugimenduak"));
		btnNeLogOut.setText(
				ResourceBundle.getBundle("Etiquetas").getString("MainGUIErregistratuta.SaioaItxi"));
		btnErreklamazioakIkusi.setText(
				ResourceBundle.getBundle("Etiquetas").getString("MainGUIErregistratuta.ErreklamazioakIkusi"));
		btnEskaerakEgin.setText(
				ResourceBundle.getBundle("Etiquetas").getString("MainGUIErregistratuta.EskaerakEgin"));
		btnBalorazioakIkusi.setText(
				ResourceBundle.getBundle("Etiquetas").getString("MainGUIErregistratuta.BalorazioakIkusi"));
		btnEskaerakIkusi.setText(
				ResourceBundle.getBundle("Etiquetas").getString("MainGUIErregistratuta.EskaerakIkusi"));
		btnOfertakIkusi.setText(
				ResourceBundle.getBundle("Etiquetas").getString("MainGUIErregistratuta.OfertakIkusi"));
		this.setTitle(
				ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle")
				+ ": " + userMail);
	}
}