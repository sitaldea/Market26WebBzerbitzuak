package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import businessLogic.BLFacade;
import domain.Sale;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ErositakoProduktuakIkusiGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel imagePanel;
	private JTextField fieldTitle;
	private JTextField fieldDescription;
	private JTextField fieldPrice;
	private JLabel labelDate;
	private JLabel statusField;
	private JLabel jLabelMsg;
	private JButton btnPrev;
	private JButton btnNext;
	private JButton btnClose;
	private String userMail;

	private List<Sale> purchased;
	private int index = 0;
	private static final int baseSize = 160;

	public ErositakoProduktuakIkusiGUI(String email) {
		this.userMail = email;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 824, 420);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		setTitle(userMail);

		fieldTitle = new JTextField();
		fieldTitle.setEditable(false);
		fieldTitle.setBounds(140, 20, 400, 28);
		contentPane.add(fieldTitle);

		JLabel lblTitle = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ShowSaleGUI.Title"));
		lblTitle.setBounds(20, 20, 120, 28);
		contentPane.add(lblTitle);

		JLabel lblDescription = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.Description"));
		lblDescription.setBounds(20, 60, 120, 16);
		contentPane.add(lblDescription);

		fieldDescription = new JTextField();
		fieldDescription.setEditable(false);
		fieldDescription.setBounds(140, 60, 400, 80);
		contentPane.add(fieldDescription);

		JLabel lblPrice = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("QuerySalesGUI.Price"));
		lblPrice.setBounds(20, 150, 120, 20);
		contentPane.add(lblPrice);

		fieldPrice = new JTextField();
		fieldPrice.setEditable(false);
		fieldPrice.setBounds(140, 150, 80, 20);
		contentPane.add(fieldPrice);

		labelDate = new JLabel();
		labelDate.setBounds(20, 180, 300, 20);
		labelDate.setFont(new Font("Lucida Grande", Font.BOLD, 12));
		contentPane.add(labelDate);

		statusField = new JLabel();
		statusField.setBounds(140, 180, 200, 20);
		contentPane.add(statusField);

		imagePanel = new JPanel();
		imagePanel.setBounds(560, 20, 160, 160);
		contentPane.add(imagePanel);

		jLabelMsg = new JLabel();
		jLabelMsg.setForeground(Color.RED);
		jLabelMsg.setBounds(140, 210, 400, 20);
		contentPane.add(jLabelMsg);

		btnPrev = new JButton("<<");
		btnPrev.setBounds(20, 300, 120, 40);
		contentPane.add(btnPrev);

		btnNext = new JButton(">>");
		btnNext.setBounds(160, 300, 120, 40);
		contentPane.add(btnNext);

		btnClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
		btnClose.setBounds(560, 328, 120, 40);
		contentPane.add(btnClose);

		JButton btnErreklamazioaIpini = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ErositakoProduktuakIkusiGUI.ErreklamazioaIpini"));
		btnErreklamazioaIpini.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sale sale = purchased.get(index);
				ErreklamazioakIpiniGUI erreklamatuGUI = new ErreklamazioakIpiniGUI(sale, userMail);
				erreklamatuGUI.setVisible(true);
			}
		});
		btnErreklamazioaIpini.setBounds(440, 279, 167, 40);
		contentPane.add(btnErreklamazioaIpini);

		JButton btnBalorazioaJarri = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ErositakoProduktuakIkusiGUI.BalorazioaJarri"));
		btnBalorazioaJarri.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Sale sale = purchased.get(index);
				BalorazioaJarriGUI balorazioGUI = new BalorazioaJarriGUI(sale);
				balorazioGUI.setVisible(true);
			}
		});
		btnBalorazioaJarri.setBounds(629, 279, 167, 40);
		contentPane.add(btnBalorazioaJarri);

		BLFacade facade = MainGUI.getBusinessLogic();
		if (facade == null) {
			JOptionPane.showMessageDialog(this, "Business logic not available", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// getUser beharrean getErositakoak erabili
		purchased = facade.getErositakoak(email);
		if (purchased == null || purchased.isEmpty()) {
			JOptionPane.showMessageDialog(this,
				ResourceBundle.getBundle("Etiquetas").getString("QuerySalesGUI.NoProducts"),
				"Info", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		index = 0;
		updateDisplay();

		btnPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (index > 0) {
					index--;
					updateDisplay();
				}
			}
		});

		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (index < purchased.size() - 1) {
					index++;
					updateDisplay();
				}
			}
		});

		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		setVisible(true);
	}

	private void updateDisplay() {
		Sale sale = purchased.get(index);
		fieldTitle.setText(sale.getTitle());
		fieldDescription.setText(sale.getDescription());
		fieldPrice.setText(Float.toString(sale.getPrice()));
		labelDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(sale.getPublicationDate()));
		statusField.setText(Utils.getStatus(sale.getStatus()));
		jLabelMsg.setText("(" + (index + 1) + " / " + purchased.size() + ")");

		imagePanel.removeAll();
		BLFacade facade = MainGUI.getBusinessLogic();
		String file = sale.getFile();
		if (file != null) {
			try {
				byte[] imgBytes = facade.downloadImage(file);
				if (imgBytes != null) {
					BufferedImage buf = ImageIO.read(new ByteArrayInputStream(imgBytes));
					if (buf != null) {
						BufferedImage scaled = rescale(buf);
						imagePanel.setLayout(new BorderLayout());
						imagePanel.add(new JLabel(new ImageIcon(scaled)), BorderLayout.CENTER);
					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		btnPrev.setEnabled(index > 0);
		btnNext.setEnabled(index < purchased.size() - 1);
		imagePanel.revalidate();
		imagePanel.repaint();
	}

	public BufferedImage rescale(BufferedImage originalImage) {
		BufferedImage resizedImage = new BufferedImage(baseSize, baseSize, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, baseSize, baseSize, null);
		g.dispose();
		return resizedImage;
	}
}