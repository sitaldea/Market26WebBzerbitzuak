package gui;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import businessLogic.BLFacade;
import domain.Sale;
import domain.User;
import domain.DiruKontua;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BuyProductGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldPrice;
	private static final int baseSize = 160;
	private String userMail;
	private JComboBox<String> comboBoxKontuak;

	public BuyProductGUI(Sale sale, String mail) {

		this.userMail = mail;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 587, 344);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		setTitle(userMail);

		BLFacade facade = MainGUI.getBusinessLogic();

		JLabel lblIzKontuZenb = new JLabel(
				ResourceBundle.getBundle("Etiquetas").getString("BuyProductGUI.kontu"));
		lblIzKontuZenb.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblIzKontuZenb.setBounds(120, 51, 126, 14);
		contentPane.add(lblIzKontuZenb);

		comboBoxKontuak = new JComboBox<>();
		comboBoxKontuak.setBounds(273, 49, 215, 20);
		contentPane.add(comboBoxKontuak);

		List<String> kontuak = facade.getUserKontuak(userMail);
		if (kontuak != null) {
		    for (String kontuZenb : kontuak) {
		        comboBoxKontuak.addItem(kontuZenb);
		    }
		}

		JLabel jLabelPrice = new JLabel(
				ResourceBundle.getBundle("Etiquetas").getString("QuerySalesGUI.Price"));
		jLabelPrice.setFont(new Font("Tahoma", Font.BOLD, 12));
		jLabelPrice.setBounds(120, 100, 101, 20);
		contentPane.add(jLabelPrice);

		textFieldPrice = new JTextField();
		textFieldPrice.setText(Float.toString(sale.getPrice()));
		textFieldPrice.setEditable(false);
		textFieldPrice.setBounds(251, 100, 60, 20);
		contentPane.add(textFieldPrice);

		JPanel panelImage = new JPanel();
		panelImage.setBounds(388, 111, 141, 121);
		contentPane.add(panelImage);

		String file = sale.getFile();
		if (file != null) {
		    try {
		        byte[] imgBytes = facade.downloadImage(file);
		        if (imgBytes != null) {
		            BufferedImage targetImg = ImageIO.read(new ByteArrayInputStream(imgBytes));
		            if (targetImg != null) {
		                targetImg = rescale(targetImg);
		                panelImage.setLayout(new BorderLayout());
		                panelImage.add(new JLabel(new ImageIcon(targetImg)));
		            }
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}

		JButton btnErosi = new JButton(
				ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.Erosi"));
		btnErosi.setFont(new Font("Tahoma", Font.BOLD, 12));

		btnErosi.setBounds(199, 166, 119, 40);
		contentPane.add(btnErosi);

		JButton btnClose =  new JButton(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.CancelButton"));;
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BuyProductGUI.this.dispose();
			}
		});
		btnClose.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnClose.setBounds(199, 244, 119, 40);
		contentPane.add(btnClose);

		btnErosi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String buyerKontuZenb = comboBoxKontuak.getSelectedItem().toString();
				double buyerDiruKop = facade.getDiruKop(buyerKontuZenb);
				if(sale.getEgoera().equals("Erosita")) {
					JOptionPane.showMessageDialog(BuyProductGUI.this, ResourceBundle.getBundle("Etiquetas").getString("BuyProductGUI.AlreadySold"),
							"Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (buyerDiruKop > sale.getPrice()) {
					facade.buyProduct(sale.getSaleNumber(), userMail);
					sale.setEgoera("Erosita");
					JOptionPane.showMessageDialog(BuyProductGUI.this, ResourceBundle.getBundle("Etiquetas").getString("BuyProductGUI.Success"));
					BuyProductGUI.this.dispose();
				} else {
					JOptionPane.showMessageDialog(BuyProductGUI.this, ResourceBundle.getBundle("Etiquetas").getString("BuyProductGUI.InsufficientFunds"),
							"Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	public BufferedImage rescale(BufferedImage originalImage) {
		BufferedImage resizedImage =
				new BufferedImage(baseSize, baseSize, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, baseSize, baseSize, null);
		g.dispose();
		return resizedImage;
	}
}