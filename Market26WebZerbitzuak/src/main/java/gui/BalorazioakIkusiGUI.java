package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Component;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import businessLogic.BLFacade;
import domain.BalorazioProfila;

public class BalorazioakIkusiGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private String userMail;
    private JTable balorazioTable;
    private DefaultTableModel tableModel;

    public BalorazioakIkusiGUI(String email) {
        this.userMail = email;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 800, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);
        this.setTitle(userMail);

        JLabel titleLabel = new JLabel("Balorazioak");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(300, 20, 200, 30);
        contentPane.add(titleLabel);

        String[] columnNames = {"Puntuación", "Comentario"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        balorazioTable = new JTable(tableModel);
        balorazioTable.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 16));
        balorazioTable.setRowHeight(60);
        balorazioTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        balorazioTable.getColumnModel().getColumn(1).setPreferredWidth(600);
        balorazioTable.getColumnModel().getColumn(0).setCellRenderer(new StarsRenderer());

        JScrollPane scrollPane = new JScrollPane(balorazioTable);
        scrollPane.setBounds(20, 70, 750, 350);
        contentPane.add(scrollPane);

        JButton btnClose = new JButton("Itxi");
        btnClose.setFont(new Font("Arial", Font.PLAIN, 12));
        btnClose.setBounds(650, 430, 120, 30);
        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { dispose(); }
        });
        contentPane.add(btnClose);

        loadBalorazioak();
    }

    private void loadBalorazioak() {
        try {
            BLFacade facade = MainGUI.getBusinessLogic();
            if (facade == null) {
                JOptionPane.showMessageDialog(this, "Business logic not available", "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // getUser beharrean getBalorazioakByUser erabili
            List<BalorazioProfila> balorazioak = facade.getBalorazioakByUser(userMail);

            if (balorazioak == null || balorazioak.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ez dituzu balorazioik", "Info",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            tableModel.setRowCount(0);
            for (BalorazioProfila balorazio : balorazioak) {
                String puntuazioa = balorazio.getPuntuazioa() + "";
                String comentario = balorazio.getBalorazioa() != null ? 
                    balorazio.getBalorazioa() : "(Sin comentario)";
                tableModel.addRow(new Object[]{puntuazioa, comentario});
            }

            setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errorea balorazioak kargatzen: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public List<BalorazioProfila> getBalorazioak() {
        try {
            BLFacade facade = MainGUI.getBusinessLogic();
            return facade.getBalorazioakByUser(userMail);
        } catch (Exception e) {
            return null;
        }
    }

    class StarsRenderer extends JLabel implements TableCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            int puntuazioa = 0;
            try {
                puntuazioa = Integer.parseInt(value.toString());
            } catch (Exception e) {
                puntuazioa = 0;
            }

            StringBuilder stars = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                if (i < puntuazioa) {
                    stars.append("<span style='color: rgb(255, 215, 0);'>★</span>");
                } else {
                    stars.append("<span style='color: black;'>☆</span>");
                }
            }

            setText("<html>" + stars.toString() + " <span style='color: black;'>(" + puntuazioa + "/5)</span></html>");
            setFont(new Font("Segoe UI Symbol", Font.PLAIN, 20));
            setHorizontalAlignment(JLabel.CENTER);
            setVerticalAlignment(JLabel.CENTER);

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setOpaque(true);
            } else {
                setBackground(Color.WHITE);
                setOpaque(true);
            }

            return this;
        }
    }
}