package hotel.reservation.system;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 * Historical Billing and Invoices Screen.
 * Allows viewing completed guest stays, receipts, and invoices.
 */
public class BillingHistory extends JFrame implements ActionListener {
    private JTable table;
    private JButton backButton, viewReceiptButton, refreshButton;

    public BillingHistory() {
        UIUtils.applyModernTheme();

        setTitle("The Grand Hotel - Billing History & Invoices");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        // Header Panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        headerPanel.setBackground(new Color(24, 43, 73));

        JLabel titleLabel = new JLabel("GUEST BILLING HISTORY & INVOICE ARCHIVE");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Table in ScrollPane
        table = new JTable();
        table.setRowHeight(25);
        table.setFont(new Font("Tahoma", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(230, 235, 245));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Footer Actions
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        footerPanel.setBackground(Color.WHITE);

        refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
        refreshButton.addActionListener(this);
        footerPanel.add(refreshButton);

        viewReceiptButton = new JButton("View Selected Invoice");
        viewReceiptButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        viewReceiptButton.setBackground(new Color(24, 43, 73));
        viewReceiptButton.setForeground(Color.WHITE);
        viewReceiptButton.addActionListener(this);
        footerPanel.add(viewReceiptButton);

        backButton = new JButton("Back to Reception");
        backButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
        backButton.setBackground(new Color(108, 117, 125));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(this);
        footerPanel.add(backButton);

        add(footerPanel, BorderLayout.SOUTH);

        loadBillingHistory();
        setVisible(true);
    }

    private void loadBillingHistory() {
        String query = "SELECT bill_no, customer_name, room_no, checkin_time, checkout_time, days, room_charges, tax_amount, deposit_paid, net_amount, payment_mode, payment_status FROM billing_history ORDER BY id DESC";
        try (Conn conn = new Conn();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load billing records: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == refreshButton) {
            loadBillingHistory();
        } else if (ae.getSource() == viewReceiptButton) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select an invoice row from the table.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String billNo = String.valueOf(table.getValueAt(selectedRow, 0));
            showInvoiceModal(billNo);
        } else if (ae.getSource() == backButton) {
            setVisible(false);
            dispose();
        }
    }

    private void showInvoiceModal(String billNo) {
        String query = "SELECT * FROM billing_history WHERE bill_no = ?";
        try (Conn conn = new Conn();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, billNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String receipt = String.format(
                        "=====================================================\n" +
                        "             THE GRAND HOTEL & RESORT                \n" +
                        "        Hospitality at its Finest &bull; Tax Invoice  \n" +
                        "=====================================================\n" +
                        "Invoice Number : %s\n" +
                        "Date of Issue  : %s\n" +
                        "Payment Status : %s (%s)\n" +
                        "-----------------------------------------------------\n" +
                        "Guest Name     : %s\n" +
                        "Identity Ref   : %s\n" +
                        "Allocated Room : Room %s\n" +
                        "Check-in Date  : %s\n" +
                        "Check-out Date : %s\n" +
                        "Total Nights   : %d night(s)\n" +
                        "-----------------------------------------------------\n" +
                        "Room Tariff    : ₹%.2f per night\n" +
                        "Gross Charges  : ₹%.2f\n" +
                        "Applicable GST : ₹%.2f (12%% / 18%%)\n" +
                        "Less Deposit   : -₹%.2f\n" +
                        "-----------------------------------------------------\n" +
                        "TOTAL NET DUE  : ₹%.2f\n" +
                        "=====================================================\n" +
                        "         Thank you for staying with us!              \n",
                        rs.getString("bill_no"),
                        rs.getString("checkout_time"),
                        rs.getString("payment_status"),
                        rs.getString("payment_mode"),
                        rs.getString("customer_name"),
                        rs.getString("customer_id"),
                        rs.getString("room_no"),
                        rs.getString("checkin_time"),
                        rs.getString("checkout_time"),
                        rs.getInt("days"),
                        rs.getDouble("room_rate"),
                        rs.getDouble("room_charges"),
                        rs.getDouble("tax_amount"),
                        rs.getDouble("deposit_paid"),
                        rs.getDouble("net_amount")
                    );

                    JTextArea area = new JTextArea(receipt);
                    area.setFont(new Font("Monospaced", Font.PLAIN, 12));
                    area.setEditable(false);
                    JScrollPane sp = new JScrollPane(area);
                    sp.setPreferredSize(new Dimension(500, 420));

                    JOptionPane.showMessageDialog(this, sp, "Tax Invoice - " + billNo, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching invoice: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BillingHistory());
    }
}
