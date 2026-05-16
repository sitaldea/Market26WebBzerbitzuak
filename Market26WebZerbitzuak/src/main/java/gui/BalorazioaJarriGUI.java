package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import businessLogic.BLFacade;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;

import domain.*;

public class BalorazioaJarriGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel[] starLabels;
	private int selectedRating = 0;
	private JTextArea commentTextArea;
	private JLabel ratingLabel;
	private Sale sale;

	/**
	 * Create the frame.
	 */

	public BalorazioaJarriGUI(Sale sale) {
		this.sale = sale;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JLabel titleLabel = new JLabel("Balorazioa Jarri");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
		titleLabel.setBounds(200, 20, 200, 30);
		contentPane.add(titleLabel);

		starLabels = new JLabel[5];
		int starX = 120;
		for (int i = 0; i < 5; i++) {
			JLabel star = new JLabel("★");
			star.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 60));
			star.setForeground(Color.LIGHT_GRAY);
			star.setBounds(starX + (i * 70), 70, 60, 60);
			star.setHorizontalAlignment(SwingConstants.CENTER);
			star.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
			final int starIndex = i;

			star.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					highlightStars(starIndex);
				}

				public void mouseExited(MouseEvent e) {
					if (selectedRating == 0) {
						clearStars();
					} else {
						highlightStars(selectedRating - 1);
					}
				}

				public void mouseClicked(MouseEvent e) {
					selectedRating = starIndex + 1;
					highlightStars(starIndex);
					ratingLabel.setText("Puntuazioa: " + selectedRating + "/5");
				}
			});

			contentPane.add(star);
			starLabels[i] = star;
		}

		ratingLabel = new JLabel("Puntuazioa: 0/5");
		ratingLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		ratingLabel.setBounds(200, 150, 200, 20);
		contentPane.add(ratingLabel);

		JLabel commentLabel = new JLabel("Balorazioa:");
		commentLabel.setFont(new Font("Arial", Font.BOLD, 12));
		commentLabel.setBounds(50, 190, 100, 20);
		contentPane.add(commentLabel);

		commentTextArea = new JTextArea();
		commentTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
		commentTextArea.setLineWrap(true);
		commentTextArea.setWrapStyleWord(true);
		commentTextArea.setBounds(50, 220, 500, 100);
		
		JScrollPane scrollPane = new JScrollPane(commentTextArea);
		scrollPane.setBounds(50, 220, 500, 100);
		contentPane.add(scrollPane);

		JButton saveButton = new JButton("Gorde");
		saveButton.setFont(new Font("Arial", Font.PLAIN, 12));
		saveButton.setBounds(200, 340, 100, 35);
		saveButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        if (selectedRating > 0) {
		            BLFacade facade = MainGUI.getBusinessLogic();
		            String comment = commentTextArea.getText().trim();
		            int puntuazioa = selectedRating;
		            if (sale.getBalorazioProfila() == null) {
		                // sale.getSeller() beharrean saleNumber pasa
		                facade.createBalorazioa(comment, puntuazioa, sale.getSaleNumber());
		                JOptionPane.showMessageDialog(BalorazioaJarriGUI.this, 
		                    "Balorazioa bidali da", "Info", JOptionPane.INFORMATION_MESSAGE);
		                dispose();
		            } else {
		                JOptionPane.showMessageDialog(BalorazioaJarriGUI.this, 
		                    "Badu balorazioa", "Error", JOptionPane.ERROR_MESSAGE);
		            }
		        }
		    }
		});
		contentPane.add(saveButton);

		JButton btnClose = new JButton("Itxi");
		btnClose.setFont(new Font("Arial", Font.PLAIN, 12));
		btnClose.setBounds(320, 340, 100, 35);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		contentPane.add(btnClose);
	}

	private void highlightStars(int index) {
		for (int i = 0; i < starLabels.length; i++) {
			if (i <= index) {
				starLabels[i].setForeground(new Color(255, 215, 0));
			} else {
				starLabels[i].setForeground(Color.LIGHT_GRAY);
			}
		}
	}

	private void clearStars() {
		for (JLabel star : starLabels) {
			star.setForeground(Color.LIGHT_GRAY);
		}
	}

	public int getSelectedRating() {
		return selectedRating;
	}
}