package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;

import businessLogic.BLFacade;
import domain.Eskaera;
import domain.User;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class EskaerakIkusiGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String userMail;
	private JTable eskaeraTable;
	private DefaultTableModel tableModel;
	private java.util.List<Eskaera> displayedEskaerak = new ArrayList<>();

	/**
	 * Create the frame.
	 */
	public EskaerakIkusiGUI(String email) {
		this.userMail = email;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		this.setTitle(userMail);

		JLabel titleLabel = new JLabel("Eskaerak");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
		titleLabel.setBounds(250, 10, 200, 30);
		contentPane.add(titleLabel);

		String[] columnNames = { "Produktua" };
		tableModel = new DefaultTableModel(columnNames, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		eskaeraTable = new JTable(tableModel);
		eskaeraTable.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
		eskaeraTable.setRowHeight(28);

		eskaeraTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int row = eskaeraTable.getSelectedRow();
					if (row >= 0 && row < displayedEskaerak.size()) {
						Eskaera selected = displayedEskaerak.get(row);
						OfertaSortuGUI ofertaGui = new OfertaSortuGUI(selected, userMail);
						ofertaGui.setVisible(true);
					}
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(eskaeraTable);
		scrollPane.setBounds(20, 50, 540, 260);
		contentPane.add(scrollPane);

		JButton btnClose = new JButton("Itxi");
		btnClose.setFont(new Font("Arial", Font.PLAIN, 12));
		btnClose.setBounds(440, 320, 120, 30);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		contentPane.add(btnClose);

		loadEskaerak();
	}

	private void loadEskaerak() {
		try {
			BLFacade facade = MainGUI.getBusinessLogic();
			if (facade == null) {
				JOptionPane.showMessageDialog(this, "Business logic not available", "Error",
					JOptionPane.ERROR_MESSAGE);
				return;
			}

			List<Eskaera> eskaerak = facade.getAllEskaerak();
			if (eskaerak == null || eskaerak.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Ez dago eskaerarik", "Info",
					JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			tableModel.setRowCount(0);
			displayedEskaerak.clear();
			for (Eskaera e : eskaerak) {
				User u = e.getUser();
				String email = u != null ? u.getEmail() : "(unknown)";
				if (email.equalsIgnoreCase(userMail)) continue; 
				String product = e.getProductName() != null ? e.getProductName() : "(no name)";
				tableModel.addRow(new Object[] { product });
				displayedEskaerak.add(e);
			}

			setVisible(true);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Errorea eskaerak kargatzen: " + ex.getMessage(), "Error",
				JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

}