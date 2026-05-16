package gui;

import java.awt.Image;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

import businessLogic.BLFacade;
import domain.Erreklamazioa;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.SwingConstants;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class ErreklamazioakOnartuDeuseztatuGUI extends JFrame {

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
    private JLabel lblMessage;

    public ErreklamazioakOnartuDeuseztatuGUI(String email) {
        this.userMail = email;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 620, 400);
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
        btnPrev.setBounds(200, 220, 60, 30);
        contentPane.add(btnPrev);

        btnNext = new JButton(">");
        btnNext.setBounds(270, 220, 60, 30);
        contentPane.add(btnNext);

        btnOnartu = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ErreklamazioakEbatziGUI.Onartu"));
        btnOnartu.setBounds(360, 220, 100, 30);
        contentPane.add(btnOnartu);

        btnDeuseztatu = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ErreklamazioakEbatziGUI.Deuseztatu"));
        btnDeuseztatu.setBounds(470, 220, 110, 30);
        contentPane.add(btnDeuseztatu);

        lblMessage = new JLabel("");
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
        lblMessage.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblMessage.setBounds(231, 164, 349, 30);
        contentPane.add(lblMessage);

        JButton btnCerrar = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
        btnCerrar.setBounds(470, 320, 110, 30);
        btnCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { dispose(); }
        });
        contentPane.add(btnCerrar);

        BLFacade facade = MainGUI.getBusinessLogic();
        if (facade != null) {
            // getUser beharrean getErreklamazioakByUser erabili
            reclamList = facade.getErreklamazioakByUser(userMail);
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

        btnOnartu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Erreklamazioa erre = reclamList.get(currentIndex);
                if (!erre.getEgoera().equals("Administradoreak onartu du")) {
                    // updateEgoeraErreklamazioa(Integer, String) erabili
                    facade.updateEgoeraErreklamazioa(erre.getErreklamazioId(), "Onartua");
                    erre.setEgoera("Onartua");
                    updateView();
                    JOptionPane.showMessageDialog(ErreklamazioakOnartuDeuseztatuGUI.this,
                        "Egoera eguneratua: Onartua", "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(ErreklamazioakOnartuDeuseztatuGUI.this,
                        "Administradoreak onartua du, ezin duzu berriro egin", "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnDeuseztatu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Erreklamazioa erre = reclamList.get(currentIndex);
                if (!erre.getEgoera().equals("Administradoreak deuseztatu du")) {
                    facade.updateEgoeraErreklamazioa(erre.getErreklamazioId(), "Deuseztatu");
                    erre.setEgoera("Deuseztatu");
                    updateView();
                    JOptionPane.showMessageDialog(ErreklamazioakOnartuDeuseztatuGUI.this,
                        "Egoera eguneratua: Deuseztatu", "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(ErreklamazioakOnartuDeuseztatuGUI.this,
                        "Administradoreak deuseztatua du, ezin duzu berriro egin", "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void updateView() {
        if (reclamList == null || reclamList.isEmpty()) return;
        Erreklamazioa r = reclamList.get(currentIndex);
        lblTitle.setText((currentIndex + 1) + "/" + reclamList.size() + " - " + r.getIzenburua());
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
        lblMessage.setText(r.getEgoera() != null ? r.getEgoera() : "");
        btnPrev.setEnabled(currentIndex > 0);
        btnNext.setEnabled(currentIndex < reclamList.size() - 1);
    }
}