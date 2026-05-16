package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import businessLogic.BLFacade;
import domain.Eskaera;
import domain.Oferta;
import domain.Sale;
import domain.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.JTable;

public class OfertakIkusiGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String userMail;
	private JTable table;
	private DefaultTableModel tableModel;
	private List<Oferta> allOfertas;

	public OfertakIkusiGUI(String userMail) {
		this.userMail = userMail;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1000, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		setTitle(userMail);

		String[] columnNames = {
			ResourceBundle.getBundle("Etiquetas").getString("OfertakIkusiGUI.eskaera"),
			ResourceBundle.getBundle("Etiquetas").getString("OfertakIkusiGUI.title"),
			ResourceBundle.getBundle("Etiquetas").getString("OfertakIkusiGUI.description"),
			ResourceBundle.getBundle("Etiquetas").getString("OfertakIkusiGUI.price"),
			ResourceBundle.getBundle("Etiquetas").getString("OfertakIkusiGUI.seller"),
			ResourceBundle.getBundle("Etiquetas").getString("OfertakIkusiGUI.action")
		};
		
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 5;
			}
		};
		
		table = new JTable(tableModel);
		table.setFont(new Font("Tahoma", Font.PLAIN, 11));
		table.setRowHeight(30);
		
		TableColumn actionColumn = table.getColumnModel().getColumn(5);
		actionColumn.setCellRenderer(new ButtonRenderer());
		actionColumn.setCellEditor(new ButtonEditor(new JButton("Buy"), this));
		actionColumn.setPreferredWidth(80);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 50, 975, 380);
		contentPane.add(scrollPane);
		
		JLabel lblTitle = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("OfertakIkusiGUI.title"));
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTitle.setBounds(10, 10, 400, 30);
		contentPane.add(lblTitle);

		JButton btnClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("OfertakIkusiGUI.btnClose"));
		btnClose.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnClose.setBounds(860, 440, 115, 30);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		contentPane.add(btnClose);
		loadOfertas();
	}
	
	private void loadOfertas() {
	    try {
	        BLFacade facade = MainGUI.getBusinessLogic();

	        // getUser beharrean getEskaerakByUser erabili
	        List<Eskaera> eskaerak = facade.getEskaerakByUser(userMail);

	        if (eskaerak == null || eskaerak.isEmpty()) {
	            JOptionPane.showMessageDialog(this,
	                ResourceBundle.getBundle("Etiquetas").getString("OfertakIkusiGUI.noEskaerak"),
	                ResourceBundle.getBundle("Etiquetas").getString("OfertakIkusiGUI.info"),
	                JOptionPane.INFORMATION_MESSAGE);
	            return;
	        }

	        allOfertas = new java.util.ArrayList<>();

	        for (Eskaera eskaera : eskaerak) {
	            List<Oferta> ofertak = eskaera.getOfertak();
	            if (ofertak != null && !ofertak.isEmpty()) {
	                for (Oferta oferta : ofertak) {
	                    allOfertas.add(oferta);
	                    Object[] row = {
	                        eskaera.getProductName(),
	                        oferta.getTitle(),
	                        oferta.getDescription(),
	                        String.format("%.2f€", oferta.getPrice()),
	                        oferta.getUser() != null ? oferta.getUser().getName() : "",
	                        ResourceBundle.getBundle("Etiquetas").getString("OfertakIkusiGUI.btnBuy")
	                    };
	                    tableModel.addRow(row);
	                }
	            }
	        }

	        if (tableModel.getRowCount() == 0) {
	            JOptionPane.showMessageDialog(this,
	                ResourceBundle.getBundle("Etiquetas").getString("OfertakIkusiGUI.noOfertas"),
	                ResourceBundle.getBundle("Etiquetas").getString("OfertakIkusiGUI.info"),
	                JOptionPane.INFORMATION_MESSAGE);
	        }

	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(this,
	            ResourceBundle.getBundle("Etiquetas").getString("OfertakIkusiGUI.errorLoading"),
	            ResourceBundle.getBundle("Etiquetas").getString("OfertakIkusiGUI.error"),
	            JOptionPane.ERROR_MESSAGE);
	        e.printStackTrace();
	    }
	}
	
	public void buyOferta(int row) {
	    try {
	        if (row >= 0 && row < allOfertas.size()) {
	            Oferta oferta = allOfertas.get(row);

	            int confirm = JOptionPane.showConfirmDialog(this,
	                "Are you sure you want to buy this offer?\nPrice: " +
	                String.format("%.2f€", oferta.getPrice()),
	                ResourceBundle.getBundle("Etiquetas").getString("OfertakIkusiGUI.buyConfirmation"),
	                JOptionPane.YES_NO_OPTION);

	            if (confirm == JOptionPane.YES_OPTION) {
	                BLFacade facade = MainGUI.getBusinessLogic();

	                facade.removeOfertaAndEskaera(oferta.getId());

	                tableModel.setRowCount(0);
	                allOfertas.clear();
	                loadOfertas();

	                JOptionPane.showMessageDialog(this,
	                    ResourceBundle.getBundle("Etiquetas").getString("OfertakIkusiGUI.buySuccess"),
	                    ResourceBundle.getBundle("Etiquetas").getString("OfertakIkusiGUI.info"),
	                    JOptionPane.INFORMATION_MESSAGE);
	            }
	        }
	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(this,
	            ResourceBundle.getBundle("Etiquetas").getString("OfertakIkusiGUI.buyError"),
	            ResourceBundle.getBundle("Etiquetas").getString("OfertakIkusiGUI.error"),
	            JOptionPane.ERROR_MESSAGE);
	        e.printStackTrace();
	    }
	}
	
	class ButtonRenderer extends JButton implements TableCellRenderer {
		public ButtonRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			setText((value == null) ? "" : value.toString());
			return this;
		}
	}

	class ButtonEditor extends javax.swing.DefaultCellEditor {
		private JButton button;
		private OfertakIkusiGUI parent;

		public ButtonEditor(JButton btn, OfertakIkusiGUI parent) {
			super(new javax.swing.JCheckBox());
			this.button = btn;
			this.parent = parent;
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					final int row = table.getSelectedRow();
					fireEditingStopped();
					javax.swing.SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							parent.buyOferta(row);
						}
					});
				}
			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value,
				boolean isSelected, int row, int column) {
			button.setText((value == null) ? "" : value.toString());
			return button;
		}

		@Override
		public Object getCellEditorValue() {
			return button.getText();
		}
	}

}