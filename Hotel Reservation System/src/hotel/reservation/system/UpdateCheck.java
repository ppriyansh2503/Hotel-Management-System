package hotel.reservation.system;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

/**
 * Update Guest Check-in Details & Payments.
 * Manages deposit top-ups, room changes with atomic room availability re-allocation,
 * and pending balance calculation.
 */
public class UpdateCheck extends JFrame implements ActionListener {
    private JComboBox<CustomerEntry> customerCombo;
    private JTextField nameField, roomField, checkinField, depositField, pendingField;
    private JButton checkButton, updateButton, backButton;

    private static class CustomerEntry {
        String idNumber;
        String name;

        CustomerEntry(String idNumber, String name) {
            this.idNumber = idNumber;
            this.name = name;
        }

        @Override
        public String toString() {
            return name + " (" + idNumber + ")";
        }
    }

    public UpdateCheck() {
        UIUtils.applyModernTheme();

        setTitle("The Grand Hotel - Update Guest Check-in & Deposit");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(720, 460);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Header
        JLabel titleLabel = new JLabel("UPDATE GUEST STAY & DEPOSIT STATUS");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        titleLabel.setForeground(new Color(24, 43, 73));
        titleLabel.setBounds(40, 15, 450, 25);
        add(titleLabel);

        // Form Coordinates
        int labelX = 40;
        int fieldX = 180;
        int fieldWidth = 170;
        int startY = 60;
        int gap = 38;

        // 1. Select Customer
        JLabel custLabel = new JLabel("Select Active Guest:");
        custLabel.setBounds(labelX, startY, 140, 22);
        add(custLabel);

        customerCombo = new JComboBox<>();
        customerCombo.setBounds(fieldX, startY, fieldWidth, 26);
        customerCombo.addActionListener(e -> populateGuestData());
        add(customerCombo);

        // 2. Guest Name
        JLabel nameLabel = new JLabel("Guest Name:");
        nameLabel.setBounds(labelX, startY + gap, 140, 22);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(fieldX, startY + gap, fieldWidth, 24);
        add(nameField);

        // 3. Room Number
        JLabel roomLabel = new JLabel("Allocated Room No:");
        roomLabel.setBounds(labelX, startY + gap * 2, 140, 22);
        add(roomLabel);

        roomField = new JTextField();
        roomField.setBounds(fieldX, startY + gap * 2, fieldWidth, 24);
        add(roomField);

        // 4. Check-in Time
        JLabel checkinLabel = new JLabel("Check-in Timestamp:");
        checkinLabel.setBounds(labelX, startY + gap * 3, 140, 22);
        add(checkinLabel);

        checkinField = new JTextField();
        checkinField.setEditable(false);
        checkinField.setBounds(fieldX, startY + gap * 3, fieldWidth, 24);
        add(checkinField);

        // 5. Total Deposit Paid
        JLabel depositLabel = new JLabel("Deposit Paid (₹):");
        depositLabel.setBounds(labelX, startY + gap * 4, 140, 22);
        add(depositLabel);

        depositField = new JTextField();
        depositField.setBounds(fieldX, startY + gap * 4, fieldWidth, 24);
        add(depositField);

        // 6. Estimated Amount Pending
        JLabel pendingLabel = new JLabel("Estimated Pending (₹):");
        pendingLabel.setBounds(labelX, startY + gap * 5, 140, 22);
        add(pendingLabel);

        pendingField = new JTextField();
        pendingField.setEditable(false);
        pendingField.setBackground(new Color(245, 247, 250));
        pendingField.setFont(new Font("Tahoma", Font.BOLD, 13));
        pendingField.setBounds(fieldX, startY + gap * 5, fieldWidth, 24);
        add(pendingField);

        // Action Buttons
        checkButton = new JButton("Calculate Balance");
        checkButton.setBounds(labelX, startY + gap * 6 + 10, 150, 34);
        checkButton.setBackground(new Color(24, 43, 73));
        checkButton.setForeground(Color.WHITE);
        checkButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        checkButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkButton.addActionListener(this);
        add(checkButton);

        updateButton = new JButton("Save Updates");
        updateButton.setBounds(labelX + 160, startY + gap * 6 + 10, 130, 34);
        updateButton.setBackground(new Color(40, 167, 69));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateButton.addActionListener(this);
        add(updateButton);

        backButton = new JButton("Cancel");
        backButton.setBounds(labelX + 300, startY + gap * 6 + 10, 90, 34);
        backButton.setBackground(new Color(108, 117, 125));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(this);
        add(backButton);

        // Side Image
        Image sideImg = UIUtils.getScaledImage("icons/nine.jpg", 280, 200);
        JLabel imgLabel = new JLabel(new ImageIcon(sideImg));
        imgLabel.setBounds(390, 60, 280, 200);
        add(imgLabel);

        loadActiveCustomers();
        setVisible(true);
    }

    private void loadActiveCustomers() {
        customerCombo.removeAllItems();
        String sql = "SELECT number, name FROM customer WHERE status = 'Checked-In' ORDER BY name ASC";
        try (Conn conn = new Conn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                customerCombo.addItem(new CustomerEntry(rs.getString("number"), rs.getString("name")));
            }
        } catch (Exception e) {
            System.err.println("Error loading customers: " + e.getMessage());
        }

        if (customerCombo.getItemCount() > 0) {
            populateGuestData();
        }
    }

    private void populateGuestData() {
        CustomerEntry entry = (CustomerEntry) customerCombo.getSelectedItem();
        if (entry == null) return;

        String query = "SELECT name, room, checkintime, deposit FROM customer WHERE number = ? AND status = 'Checked-In'";
        try (Conn conn = new Conn();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, entry.idNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    nameField.setText(rs.getString("name"));
                    roomField.setText(rs.getString("room"));
                    checkinField.setText(rs.getString("checkintime"));
                    depositField.setText(rs.getString("deposit"));
                    calculateBalance();
                }
            }
        } catch (Exception e) {
            System.err.println("Error populating guest data: " + e.getMessage());
        }
    }

    private void calculateBalance() {
        String currentRoom = roomField.getText().trim();
        String depositStr = depositField.getText().trim();

        if (currentRoom.isEmpty() || !ValidationUtils.isNonNegativeDouble(depositStr)) {
            pendingField.setText("₹0.00");
            return;
        }

        double deposit = Double.parseDouble(depositStr);
        double price = 2500.0;

        String query = "SELECT price FROM room WHERE roomNo = ?";
        try (Conn conn = new Conn();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, currentRoom);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    price = Double.parseDouble(rs.getString("price"));
                }
            }
        } catch (Exception ignored) {}

        double pending = Math.max(0.0, price - deposit);
        pendingField.setText(String.format("₹%.2f", pending));
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == checkButton) {
            calculateBalance();
        } else if (ae.getSource() == updateButton) {
            handleSave();
        } else if (ae.getSource() == backButton) {
            setVisible(false);
            dispose();
        }
    }

    private void handleSave() {
        CustomerEntry entry = (CustomerEntry) customerCombo.getSelectedItem();
        if (entry == null) return;

        String idNumber = entry.idNumber;
        String newName = nameField.getText().trim();
        String newRoom = roomField.getText().trim();
        String newDeposit = depositField.getText().trim();

        if (!ValidationUtils.isNotEmpty(newName)) {
            JOptionPane.showMessageDialog(this, "Guest name cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!ValidationUtils.isNonNegativeDouble(newDeposit)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid deposit amount.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Conn conn = new Conn()) {
            conn.c.setAutoCommit(false);
            try {
                // Check if room changed
                String oldRoom = "";
                try (PreparedStatement ps = conn.prepareStatement("SELECT room FROM customer WHERE number = ?")) {
                    ps.setString(1, idNumber);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) oldRoom = rs.getString("room");
                    }
                }

                if (!oldRoom.equals(newRoom)) {
                    // Check if new room exists and is available
                    try (PreparedStatement ps = conn.prepareStatement("SELECT available FROM room WHERE roomNo = ?")) {
                        ps.setString(1, newRoom);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (!rs.next()) {
                                JOptionPane.showMessageDialog(this, "Room " + newRoom + " does not exist.", "Invalid Room", JOptionPane.WARNING_MESSAGE);
                                conn.c.rollback();
                                return;
                            }
                            if (!"Available".equalsIgnoreCase(rs.getString("available"))) {
                                JOptionPane.showMessageDialog(this, "Room " + newRoom + " is already occupied or under maintenance.", "Room Unavailable", JOptionPane.WARNING_MESSAGE);
                                conn.c.rollback();
                                return;
                            }
                        }
                    }

                    // Free old room
                    try (PreparedStatement ps = conn.prepareStatement("UPDATE room SET available = 'Available', status = 'Dirty' WHERE roomNo = ?")) {
                        ps.setString(1, oldRoom);
                        ps.executeUpdate();
                    }

                    // Occupy new room
                    try (PreparedStatement ps = conn.prepareStatement("UPDATE room SET available = 'Occupied' WHERE roomNo = ?")) {
                        ps.setString(1, newRoom);
                        ps.executeUpdate();
                    }
                }

                // Update customer record
                String updateCust = "UPDATE customer SET name = ?, room = ?, deposit = ? WHERE number = ?";
                try (PreparedStatement ps = conn.prepareStatement(updateCust)) {
                    ps.setString(1, newName);
                    ps.setString(2, newRoom);
                    ps.setString(3, newDeposit);
                    ps.setString(4, idNumber);
                    ps.executeUpdate();
                }

                conn.c.commit();

                JOptionPane.showMessageDialog(this, "Guest details and room allocations updated successfully!", "Update Saved", JOptionPane.INFORMATION_MESSAGE);
                setVisible(false);
                dispose();

            } catch (Exception ex) {
                conn.c.rollback();
                throw ex;
            } finally {
                conn.c.setAutoCommit(true);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to update record: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpdateCheck());
    }
}
