package gui;

import java.awt.EventQueue;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import businessLogic.BLFacade;
import domain.Erreklamazioa;
import domain.Erabiltzailea;
import domain.User;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ErreklamazioakEbatziGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String userMail;

	private List<Erreklamazioa> reclamList;
	private int currentIndex = 0;

	private JLabel lblTitle;
	private JLabel lblDescription;
	private JLabel lblImage;
	private JButton btnPrev;
	private JButton btnNext;
	private JButton btnOnartu;
	private JButton btnDeuseztatu;


	/**
	 * Create the frame.
	 */
	public ErreklamazioakEbatziGUI(String email) {
		this.userMail = email;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setTitle(userMail);

		lblTitle = new JLabel("");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTitle.setBounds(20, 20, 540, 25);
		contentPane.add(lblTitle);

		lblDescription = new JLabel(" ");
		lblDescription.setBounds(20, 60, 540, 80);
		contentPane.add(lblDescription);

		lblImage = new JLabel();
		lblImage.setBounds(20, 150, 160, 160);
		contentPane.add(lblImage);

		btnPrev = new JButton("<");
		btnPrev.setBounds(30, 320, 60, 30);
		contentPane.add(btnPrev);

		btnNext = new JButton(">");
		btnNext.setBounds(100, 320, 60, 30);
		contentPane.add(btnNext);

		btnOnartu = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ErreklamazioakEbatziGUI.Onartu"));
		btnOnartu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BLFacade facade = MainGUI.getBusinessLogic();
				Erreklamazioa erre = reclamList.get(currentIndex);
				facade.updateEgoeraErreklamazioa(erre.getErreklamazioId(), "Administradoreak onartu du");
				updateView();
				JOptionPane.showMessageDialog(ErreklamazioakEbatziGUI.this, "Egoera eguneratua: Onartua", "Info", JOptionPane.INFORMATION_MESSAGE);

			}
		});
		btnOnartu.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnOnartu.setBounds(300, 209, 124, 41);
		contentPane.add(btnOnartu);
		
		JButton btnNewButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ErreklamazioakEbatziGUI.Deuseztatu"));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BLFacade facade = MainGUI.getBusinessLogic();
				Erreklamazioa erre = reclamList.get(currentIndex);
				facade.updateEgoeraErreklamazioa(erre.getErreklamazioId(), "Administradoreak deuseztatu du");
				updateView();
				JOptionPane.showMessageDialog(ErreklamazioakEbatziGUI.this, "Egoera eguneratua: Onartua", "Info", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnNewButton.setBounds(434, 209, 116, 41);
		contentPane.add(btnNewButton);

		BLFacade facade = MainGUI.getBusinessLogic();
		reclamList = new ArrayList<>();
		if (facade != null) {
			List<Erreklamazioa> all = facade.getErreklamazioakByEgoera("Deuseztatu");
			if (all != null) reclamList.addAll(all);
		}

		if (reclamList.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Ez dago 'Deuseztatu' egoerako erreklamaziorik.", "Info", JOptionPane.INFORMATION_MESSAGE);
			dispose();
			return;
		}

		updateView();

		btnPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentIndex > 0) {
					currentIndex--;
					updateView();
				}
			}
		});

		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentIndex < reclamList.size() - 1) {
					currentIndex++;
					updateView();
				}
			}
		});
	}

	private void updateView() {
		if (reclamList == null || reclamList.isEmpty()) return;
		Erreklamazioa r = reclamList.get(currentIndex);
		lblTitle.setText((currentIndex+1) + "/" + reclamList.size() + " - " + r.getIzenburua());
		lblDescription.setText("<html>" + r.getDeskripzioa() + "</html>");
		lblImage.setIcon(null);
		if (r.getIrudia() != null && !r.getIrudia().isEmpty()) {
		    BLFacade f = MainGUI.getBusinessLogic();
		    if (f != null) {
		        try {
		            byte[] imgBytes = f.downloadImage(r.getIrudia());
		            if (imgBytes != null) {
		                BufferedImage img = ImageIO.read(new ByteArrayInputStream(imgBytes));
		                if (img != null) {
		                    lblImage.setIcon(new ImageIcon(img.getScaledInstance(160, 160, Image.SCALE_SMOOTH)));
		                }
		            }
		        } catch (IOException ex) {
		            ex.printStackTrace();
		        }
		    }
		}
		btnPrev.setEnabled(currentIndex > 0);
		btnNext.setEnabled(currentIndex < reclamList.size() - 1);
	}
}