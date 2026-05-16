package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import businessLogic.BLFacade;
import domain.Sale;
import exceptions.FileNotUploadedException;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Base64;
import java.util.ResourceBundle;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.event.ActionEvent;

import java.nio.file.Files;

public class ErreklamazioakIpiniGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Sale sale;
	private String userMail;
	private JTextField textFieldTitulua;
	private JTextField textFieldDeskripzioa;
	private JLabel lblTitulua;
	private JLabel lblDeskripzioa;
	private JLabel lblAukeratu;
	private JButton btnAukeratu;
	private JButton btnErreklamazioaIpini;
	private JButton btnItxi;
	private JPanel panel;
	File targetFile;
	BufferedImage targetImg;

	private static final int baseSize = 128;
	private static final String basePath="src/main/resources/images/";
	String encodedfile = null;



	/**
	 * Create the frame.
	 */
	public ErreklamazioakIpiniGUI(Sale sale, String email) {
		this.sale = sale;
		BLFacade facade = MainGUI.getBusinessLogic();
		this.userMail = email;
		setTitle(userMail);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 721, 463);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitulua = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ErreklamazioakIpiniGUI.Titulua"));
		lblTitulua.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTitulua.setBounds(74, 52, 135, 14);
		contentPane.add(lblTitulua);

		textFieldTitulua = new JTextField();
		textFieldTitulua.setBounds(345, 51, 278, 19);
		contentPane.add(textFieldTitulua);
		textFieldTitulua.setColumns(10);

		textFieldDeskripzioa = new JTextField();
		textFieldDeskripzioa.setBounds(345, 95, 278, 112);
		contentPane.add(textFieldDeskripzioa);
		textFieldDeskripzioa.setColumns(10);

		JLabel lblAukeratu = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ErreklamazioakIpiniGUI.Aukeratu"));
		lblAukeratu.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblAukeratu.setBounds(74, 235, 261, 14);
		contentPane.add(lblAukeratu);

		JButton btnAukeratu = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ErreklamazioakIpiniGUI.botoiAukeratu"));
		btnAukeratu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF", "jpg", "gif");
				fileChooser.setFileFilter(filter);
				int result = fileChooser.showOpenDialog(null);  

				fileChooser.setBounds(30, 148, 320, 80);

				if (result == JFileChooser.APPROVE_OPTION) {
					targetFile = fileChooser.getSelectedFile();
					panel.removeAll();
					panel.repaint();

					try {
						targetImg = rescale(ImageIO.read(targetFile));
						encodeFileToBase64Binary(targetFile);
					} catch (IOException ex) {
						//Logger.getLogger(MainAppFrame.class.getName()).log(Level.SEVERE, null, ex);
					}

					panel.setLayout(new BorderLayout(0, 0));
					panel.add(new JLabel(new ImageIcon(targetImg))); 
					setVisible(true);

				}
			}
		});
		btnAukeratu.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnAukeratu.setBounds(345, 227, 174, 31);
		contentPane.add(btnAukeratu);

		JButton btnErreklamazioaIpini = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ErreklamazioakIpiniGUI.erreklamazioaIpini"));
		btnErreklamazioaIpini.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String titulua = textFieldTitulua.getText();
				String deskripzioa = textFieldDeskripzioa.getText();
				if (titulua == null || titulua.trim().isEmpty()) {
					JOptionPane.showMessageDialog(ErreklamazioakIpiniGUI.this, ResourceBundle.getBundle("Etiquetas").getString("ErreklamazioakIpiniGUI.Titulua") + " " + "is required", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (deskripzioa == null || deskripzioa.trim().isEmpty()) {
					JOptionPane.showMessageDialog(ErreklamazioakIpiniGUI.this, ResourceBundle.getBundle("Etiquetas").getString("ErreklamazioakIpiniGUI.deskripzioa") + " " + "is required", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				BLFacade facade = MainGUI.getBusinessLogic();
				try {
					if (targetFile != null) {
					    if (sale.getErreklamazioa() == null) {
					        try {
					            byte[] fileContent = Files.readAllBytes(targetFile.toPath());
					            String fileName = targetFile.getName();
					            facade.createErreklamazio(titulua, deskripzioa, fileContent, fileName, ErreklamazioakIpiniGUI.this.sale.getSaleNumber());
					            JOptionPane.showMessageDialog(ErreklamazioakIpiniGUI.this, "Reclamación enviada.", "Info", JOptionPane.INFORMATION_MESSAGE);
					            ErreklamazioakIpiniGUI.this.dispose();
					        } catch (IOException ex) {
					            ex.printStackTrace();
					            JOptionPane.showMessageDialog(ErreklamazioakIpiniGUI.this, "Error leyendo el fichero: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					        }
					    } else {
					        JOptionPane.showMessageDialog(ErreklamazioakIpiniGUI.this, "Badu erreklamazioa", "Error", JOptionPane.ERROR_MESSAGE);
					    }
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(ErreklamazioakIpiniGUI.this, "Error sending reclamation: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnErreklamazioaIpini.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnErreklamazioaIpini.setBounds(229, 320, 148, 31);
		contentPane.add(btnErreklamazioaIpini);

		JButton btnItxi = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ErreklamazioakIpiniGUI.itxi"));
		btnItxi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnItxi.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnItxi.setBounds(405, 320, 148, 31);
		contentPane.add(btnItxi);

		JLabel lblDeskripzioa = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ErreklamazioakIpiniGUI.deskripzioa"));
		lblDeskripzioa.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblDeskripzioa.setBounds(74, 95, 261, 14);
		contentPane.add(lblDeskripzioa);

		panel = new JPanel();
		panel.setBounds(572, 235, 111, 90);
		contentPane.add(panel);

	}

	public BufferedImage rescale(BufferedImage originalImage)
	{
		BufferedImage resizedImage = new BufferedImage(baseSize, baseSize, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, baseSize, baseSize, null);
		g.dispose();
		return resizedImage;
	}

	public  String encodeFileToBase64Binary(File file){
		try {
			@SuppressWarnings("resource")
			FileInputStream fileInputStreamReader = new FileInputStream(file);
			byte[] bytes = new byte[(int)file.length()];
			fileInputStreamReader.read(bytes);
			encodedfile=new String(Base64.getEncoder().encode(bytes));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return encodedfile;
	}
}
