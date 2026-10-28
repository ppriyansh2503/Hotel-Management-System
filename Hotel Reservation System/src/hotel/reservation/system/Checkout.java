package hotel.reservation.system;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

/**
 * Guest Checkout and Automated Billing Settlement.
 * Accurately tracks stay duration, calculates room charges, applies GST,
 * deducts deposits, safely updates room status to Dirty/Available,
 * and records complete financial transaction in billing_history.
 */
public class Checkout extends JFrame implements ActionListener {
    private JComboBox<CustomerEntry> customerCombo;
    private JLabel guestNameVal, roomNoVal, checkinTimeVal, checkoutTimeVal;
    private JLabel rateVal, nightsVal, grossVal, gstVal, depositVal, netDueVal;
    private JComboBox<String> paymentModeCombo;
    private JButton checkoutButton, backButton;

    private static class CustomerEntry {
        String idNumber;
        String name;
        String roomNo;

        CustomerEntry(String idNumber, String name, String roomNo) {
            this.idNumber = idNumber;
            this.name = name;
            this.roomNo = roomNo;
        }

        @Override
        public String toString() {
            return name + " (" + idNumber + ") - Room " + roomNo;
        }
    }

    public Checkout() {
        UIUtils.applyModernTheme();

        setTitle("The Grand Hotel - Guest Checkout & Billing Settlement");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 560);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Header
        JLabel titleLabel = new JLabel("GUEST CHECKOUT & BILL SETTLEMENT");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        titleLabel.setForeground(new Color(24, 43, 73));
        titleLabel.setBounds(40, 15, 450, 25);
        add(titleLabel);

        // Form Coordinates
        int labelX = 40;
        int valueX = 200;
        int startY = 55;
        int gap = 32;

        // 1. Select Customer Dropdown
        JLabel selectLabel = new JLabel("Select Active Guest:");
        selectLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        selectLabel.setBounds(labelX, startY, 150, 22);
        add(selectLabel);

        customerCombo = new JComboBox<>();
        customerCombo.setBounds(valueX, startY, 260, 26);
        customerCombo.addActionListener(e -> updateBillCalculation());
        add(customerCombo);

        // 2. Guest Name
        JLabel nameLabel = new JLabel("Guest Full Name:");
        nameLabel.setBounds(labelX, startY + gap, 150, 20);
        add(nameLabel);
        guestNameVal = new JLabel("-");
        guestNameVal.setFont(new Font("Tahoma", Font.BOLD, 13));
        guestNameVal.setBounds(valueX, startY + gap, 250, 20);
        add(guestNameVal);

        // 3. Room Number
        JLabel roomLabel = new JLabel("Allocated Room No:");
        roomLabel.setBounds(labelX, startY + gap * 2, 150, 20);
        add(roomLabel);
        roomNoVal = new JLabel("-");
        roomNoVal.setFont(new Font("Tahoma", Font.BOLD, 13));
        roomNoVal.setBounds(valueX, startY + gap * 2, 250, 20);
        add(roomNoVal);

        // 4. Check-in Time
        JLabel inLabel = new JLabel("Check-in Timestamp:");
        inLabel.setBounds(labelX, startY + gap * 3, 150, 20);
        add(inLabel);
        checkinTimeVal = new JLabel("-");
        checkinTimeVal.setBounds(valueX, startY + gap * 3, 250, 20);
        add(checkinTimeVal);

        // 5. Check-out Time
        JLabel outLabel = new JLabel("Check-out Timestamp:");
        outLabel.setBounds(labelX, startY + gap * 4, 150, 20);
        add(outLabel);
        String currentOutTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        checkoutTimeVal = new JLabel(currentOutTime);
        checkoutTimeVal.setFont(new Font("Tahoma", Font.BOLD, 12));
        checkoutTimeVal.setBounds(valueX, startY + gap * 4, 250, 20);
        add(checkoutTimeVal);

        // 6. Stay Duration & Rate
        JLabel durationLabel = new JLabel("Stay Duration (Nights):");
        durationLabel.setBounds(labelX, startY + gap * 5, 150, 20);
        add(durationLabel);
        nightsVal = new JLabel("1 night(s)");
        nightsVal.setBounds(valueX, startY + gap * 5, 100, 20);
        add(nightsVal);

        JLabel rateLabel = new JLabel("Tariff: ₹");
        rateLabel.setBounds(valueX + 90, startY + gap * 5, 60, 20);
        add(rateLabel);
        rateVal = new JLabel("0.00");
        rateVal.setBounds(valueX + 145, startY + gap * 5, 100, 20);
        add(rateVal);

        // 7. Gross Charges
        JLabel grossLabel = new JLabel("Room Charges:");
        grossLabel.setBounds(labelX, startY + gap * 6, 150, 20);
        add(grossLabel);
        grossVal = new JLabel("₹0.00");
        grossVal.setBounds(valueX, startY + gap * 6, 200, 20);
        add(grossVal);

        // 8. Applicable Tax (GST)
        JLabel gstLabel = new JLabel("Applicable GST (12%):");
        gstLabel.setBounds(labelX, startY + gap * 7, 150, 20);
        add(gstLabel);
        gstVal = new JLabel("₹0.00");
        gstVal.setBounds(valueX, startY + gap * 7, 200, 20);
        add(gstVal);

        // 9. Deposit Paid
        JLabel depLabel = new JLabel("Deposit Already Paid:");
        depLabel.setBounds(labelX, startY + gap * 8, 150, 20);
        add(depLabel);
        depositVal = new JLabel("₹0.00");
        depositVal.setBounds(valueX, startY + gap * 8, 200, 20);
        add(depositVal);

        // 10. Net Due (Highlighted)
        JLabel netLabel = new JLabel("NET AMOUNT PAYABLE:");
        netLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        netLabel.setForeground(new Color(180, 40, 40));
        netLabel.setBounds(labelX, startY + gap * 9, 160, 22);
        add(netLabel);

        netDueVal = new JLabel("₹0.00");
        netDueVal.setFont(new Font("Tahoma", Font.BOLD, 16));
        netDueVal.setForeground(new Color(180, 40, 40));
        netDueVal.setBounds(valueX, startY + gap * 9, 200, 22);
        add(netDueVal);

        // 11. Payment Mode
        JLabel modeLabel = new JLabel("Payment Method:");
        modeLabel.setBounds(labelX, startY + gap * 10, 150, 22);
        add(modeLabel);

        paymentModeCombo = new JComboBox<>(new String[]{"UPI / QR", "Credit/Debit Card", "Cash", "Net Banking"});
        paymentModeCombo.setBounds(valueX, startY + gap * 10, 160, 24);
        add(paymentModeCombo);

        // Action Buttons
        checkoutButton = new JButton("Confirm Checkout & Settle Bill");
        checkoutButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        checkoutButton.setBackground(new Color(24, 43, 73));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setBounds(labelX, startY + gap * 11 + 10, 240, 36);
        checkoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkoutButton.addActionListener(this);
        add(checkoutButton);

        backButton = new JButton("Cancel");
        backButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        backButton.setBackground(new Color(108, 117, 125));
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(labelX + 260, startY + gap * 11 + 10, 120, 36);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(this);
        add(backButton);

        // Side Illustration
        Image sideImg = UIUtils.getScaledImage("icons/sixth.jpg", 280, 200);
        JLabel sideLabel = new JLabel(new ImageIcon(sideImg));
        sideLabel.setBounds(480, 70, 280, 200);
        add(sideLabel);

        loadActiveCustomers();
        setVisible(true);
    }

    private void loadActiveCustomers() {
        customerCombo.removeAllItems();
        String query = "SELECT document, number, name, room FROM customer WHERE status = 'Checked-In' ORDER BY name ASC";
        try (Conn conn = new Conn();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                customerCombo.addItem(new CustomerEntry(
                    rs.getString("number"),
                    rs.getString("name"),
                    rs.getString("room")
                ));
            }
        } catch (Exception e) {
            System.err.println("Error loading checkout customers: " + e.getMessage());
        }

        if (customerCombo.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "There are currently no active checked-in guests.", "No Active Stays", JOptionPane.INFORMATION_MESSAGE);
            checkoutButton.setEnabled(false);
        } else {
            checkoutButton.setEnabled(true);
            updateBillCalculation();
        }
    }

    private void updateBillCalculation() {
        CustomerEntry entry = (CustomerEntry) customerCombo.getSelectedItem();
        if (entry == null) return;

        guestNameVal.setText(entry.name);
        roomNoVal.setText(entry.roomNo);

        String custSql = "SELECT checkintime, deposit FROM customer WHERE number = ? AND status = 'Checked-In'";
        String roomSql = "SELECT price FROM room WHERE roomNo = ?";

        try (Conn conn = new Conn()) {
            String checkinStr = "";
            double deposit = 0.0;
            double rate = 2500.0;

            try (PreparedStatement psCust = conn.prepareStatement(custSql)) {
                psCust.setString(1, entry.idNumber);
                try (ResultSet rs = psCust.executeQuery()) {
                    if (rs.next()) {
                        checkinStr = rs.getString("checkintime");
                        try {
                            deposit = Double.parseDouble(rs.getString("deposit"));
                        } catch (Exception ignored) {}
                    }
                }
            }

            try (PreparedStatement psRoom = conn.prepareStatement(roomSql)) {
                psRoom.setString(1, entry.roomNo);
                try (ResultSet rs = psRoom.executeQuery()) {
                    if (rs.next()) {
                        try {
                            rate = Double.parseDouble(rs.getString("price"));
                        } catch (Exception ignored) {}
                    }
                }
            }

            checkinTimeVal.setText(checkinStr);

            // Compute stay duration
            long nights = 1;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date checkinDate = sdf.parse(checkinStr);
                Date now = new Date();
                long diffMillis = now.getTime() - checkinDate.getTime();
                long calcNights = TimeUnit.MILLISECONDS.toDays(diffMillis);
                if (calcNights >= 1) {
                    nights = calcNights;
                }
            } catch (Exception ignored) {}

            double gross = nights * rate;
            double taxRate = rate >= 7500.0 ? 0.18 : 0.12; // 18% for luxury suites, 12% standard
            double tax = gross * taxRate;
            double netDue = Math.max(0.0, (gross + tax) - deposit);

            rateVal.setText(String.format("%.2f", rate));
            nightsVal.setText(nights + " night(s)");
            grossVal.setText(String.format("₹%.2f", gross));
            gstVal.setText(String.format("₹%.2f (%.0f%%)", tax, taxRate * 100));
            depositVal.setText(String.format("₹%.2f", deposit));
            netDueVal.setText(String.format("₹%.2f", netDue));

        } catch (Exception e) {
            System.err.println("Bill calc error: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == checkoutButton) {
            handleCheckout();
        } else if (ae.getSource() == backButton) {
            setVisible(false);
            dispose();
        }
    }

    private void handleCheckout() {
        CustomerEntry entry = (CustomerEntry) customerCombo.getSelectedItem();
        if (entry == null) return;

        String idNumber = entry.idNumber;
        String guestName = guestNameVal.getText();
        String roomNo = roomNoVal.getText();
        String checkinTime = checkinTimeVal.getText();
        String checkoutTime = checkoutTimeVal.getText();
        String paymentMode = (String) paymentModeCombo.getSelectedItem();

        int nights = Integer.parseInt(nightsVal.getText().split(" ")[0]);
        double rate = Double.parseDouble(rateVal.getText());
        double gross = nights * rate;
        double tax = gross * (rate >= 7500.0 ? 0.18 : 0.12);
        double deposit = 0.0;
        try {
            deposit = Double.parseDouble(depositVal.getText().replace("₹", "").trim());
        } catch (Exception ignored) {}
        double netDue = Double.parseDouble(netDueVal.getText().replace("₹", "").trim());

        String billNo = "INV-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "-" + (int)(Math.random() * 9000 + 1000);

        // Transaction Execution: Atomic Checkout
        try (Conn conn = new Conn()) {
            conn.c.setAutoCommit(false);
            try {
                // 1. Mark customer status as 'Checked-Out' (PRESERVING GUEST HISTORY)
                String updateCust = "UPDATE customer SET status = 'Checked-Out' WHERE number = ? AND status = 'Checked-In'";
                try (PreparedStatement ps = conn.prepareStatement(updateCust)) {
                    ps.setString(1, idNumber);
                    ps.executeUpdate();
                }

                // 2. Mark the CORRECT room as Available & Dirty (for housekeeping)
                String updateRoom = "UPDATE room SET available = 'Available', status = 'Dirty' WHERE roomNo = ?";
                try (PreparedStatement ps = conn.prepareStatement(updateRoom)) {
                    ps.setString(1, roomNo);
                    ps.executeUpdate();
                }

                // 3. Record full billing transaction in billing_history
                String insertBill = "INSERT INTO billing_history (bill_no, customer_name, customer_id, room_no, checkin_time, checkout_time, days, room_rate, room_charges, tax_amount, deposit_paid, net_amount, payment_mode, payment_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Settled')";
                try (PreparedStatement ps = conn.prepareStatement(insertBill)) {
                    ps.setString(1, billNo);
                    ps.setString(2, guestName);
                    ps.setString(3, idNumber);
                    ps.setString(4, roomNo);
                    ps.setString(5, checkinTime);
                    ps.setString(6, checkoutTime);
                    ps.setInt(7, nights);
                    ps.setDouble(8, rate);
                    ps.setDouble(9, gross);
                    ps.setDouble(10, tax);
                    ps.setDouble(11, deposit);
                    ps.setDouble(12, netDue);
                    ps.setString(13, paymentMode);
                    ps.executeUpdate();
                }

                conn.c.commit();

                // Display Invoice Receipt
                String invoiceText = String.format(
                    "=====================================================\n" +
                    "             THE GRAND HOTEL & RESORT                \n" +
                    "            Official Final Tax Invoice               \n" +
                    "=====================================================\n" +
                    "Invoice ID     : %s\n" +
                    "Settlement Date: %s\n" +
                    "Payment Mode   : %s (Paid & Settled)\n" +
                    "-----------------------------------------------------\n" +
                    "Guest Name     : %s\n" +
                    "ID Document    : %s\n" +
                    "Room Vacated   : Room %s (Sent to Housekeeping)\n" +
                    "Check-in Time  : %s\n" +
                    "Check-out Time : %s\n" +
                    "Duration       : %d night(s)\n" +
                    "-----------------------------------------------------\n" +
                    "Room Tariff    : ₹%.2f per night\n" +
                    "Room Charges   : ₹%.2f\n" +
                    "GST Tax        : ₹%.2f\n" +
                    "Deposit Applied: -₹%.2f\n" +
                    "-----------------------------------------------------\n" +
                    "TOTAL NET PAID : ₹%.2f\n" +
                    "=====================================================\n" +
                    "       Thank you, %s! Please visit us again.         \n",
                    billNo, checkoutTime, paymentMode, guestName, idNumber, roomNo,
                    checkinTime, checkoutTime, nights, rate, gross, tax, deposit, netDue, guestName
                );

                JTextArea area = new JTextArea(invoiceText);
                area.setFont(new Font("Monospaced", Font.PLAIN, 12));
                area.setEditable(false);
                JScrollPane sp = new JScrollPane(area);
                sp.setPreferredSize(new Dimension(500, 420));

                JOptionPane.showMessageDialog(this, sp, "Checkout Successful - Invoice " + billNo, JOptionPane.INFORMATION_MESSAGE);

                setVisible(false);
                dispose();

            } catch (Exception ex) {
                conn.c.rollback();
                throw ex;
            } finally {
                conn.c.setAutoCommit(true);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Checkout failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Checkout());
    }
}