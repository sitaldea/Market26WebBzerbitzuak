package gui;

import java.awt.Font;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import businessLogic.BLFacade;
import domain.Mugimendua;

public class MugimenduakIkusiGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private String userMail;
    private JComboBox<String> comboBoxKontuak;
    private JTable table;
    private DefaultTableModel tableModel;

    public MugimenduakIkusiGUI(String email) {
        this.userMail = email;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 330);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);
        setTitle(userMail);

        JLabel lblKontuak = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("BuyProductGUI.kontu"));
        lblKontuak.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblKontuak.setBounds(10, 20, 120, 20);
        contentPane.add(lblKontuak);

        comboBoxKontuak = new JComboBox<String>();
        comboBoxKontuak.setBounds(140, 20, 280, 22);
        contentPane.add(comboBoxKontuak);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 60, 410, 180);
        contentPane.add(scrollPane);

        tableModel = new DefaultTableModel(new Object[]{"Date", "Amount", "Product", "Type"}, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        scrollPane.setViewportView(table);

        JButton btnClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
        btnClose.setBounds(160, 250, 120, 33);
        btnClose.addActionListener(e -> dispose());
        contentPane.add(btnClose);

        BLFacade facade = MainGUI.getBusinessLogic();
        if (facade == null) return;

        // getUserAccounts beharrean getUserKontuak erabili
        List<String> kontuak = facade.getUserKontuak(userMail);
        if (kontuak != null) {
            for (String kontuZenb : kontuak) {
                comboBoxKontuak.addItem(kontuZenb);
            }
        }

        // getMugimenduak — mugimendua guztiak kargatu
        List<Mugimendua> mugimenduakGuztiak = facade.getMugimenduak(userMail);

        comboBoxKontuak.addActionListener(e -> loadMovementsForSelected(mugimenduakGuztiak));

        if (comboBoxKontuak.getItemCount() > 0) {
            comboBoxKontuak.setSelectedIndex(0);
            loadMovementsForSelected(mugimenduakGuztiak);
        }

        setVisible(true);
    }

    private void loadMovementsForSelected(List<Mugimendua> mugimenduakGuztiak) {
        tableModel.setRowCount(0);
        String selected = (String) comboBoxKontuak.getSelectedItem();
        if (mugimenduakGuztiak != null && selected != null) {
            for (Mugimendua m : mugimenduakGuztiak) {
                if (selected.equals(m.getKontuZenb())) {
                    tableModel.addRow(new Object[]{
                        m.getData(),
                        m.getDiruKop(),
                        m.getProductName(),
                        m.getMota()
                    });
                }
            }
        }
    }
}