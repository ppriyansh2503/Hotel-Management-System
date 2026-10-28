package hotel.reservation.system;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

/**
 * Add Room Screen.
 * Validates room uniqueness, tariffs, and adds rooms to the hotel inventory.
 */
public class AddRooms extends JFrame implements ActionListener {
    private JTextField roomNoField, priceField;
    private JComboBox<String> availableCombo, statusCombo, bedTypeCombo;
    private JButton submitButton, cancelButton;

    public AddRooms() {
        UIUtils.applyModernTheme();

        setTitle("The Grand Hotel - Add New Room");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 420);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Header
        JLabel titleLabel = new JLabel("ADD NEW ROOM INVENTORY");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        titleLabel.setForeground(new Color(24, 43, 73));
        titleLabel.setBounds(60, 20, 300, 25);
        add(titleLabel);

        int labelX = 60;
        int fieldX = 200;
        int fieldWidth = 160;
        int startY = 70;
        int gap = 38;

        // 1. Room Number
        JLabel l1 = new JLabel("Room Number:");
        l1.setFont(new Font("Tahoma", Font.PLAIN, 14));
        l1.setBounds(labelX, startY, 130, 22);
        add(l1);

        roomNoField = new JTextField();
        roomNoField.setBounds(fieldX, startY, fieldWidth, 24);
        add(roomNoField);

        // 2. Availability
        JLabel l2 = new JLabel("Availability:");
        l2.setFont(new Font("Tahoma", Font.PLAIN, 14));
        l2.setBounds(labelX, startY + gap, 130, 22);
        add(l2);

        availableCombo = new JComboBox<>(new String[]{"Available", "Occupied", "Under Maintenance"});
        availableCombo.setBounds(fieldX, startY + gap, fieldWidth, 24);
        add(availableCombo);

        // 3. Cleaning Status
        JLabel l3 = new JLabel("Cleaning Status:");
        l3.setFont(new Font("Tahoma", Font.PLAIN, 14));
        l3.setBounds(labelX, startY + gap * 2, 130, 22);
        add(l3);

        statusCombo = new JComboBox<>(new String[]{"Cleaned", "Dirty", "Cleaning In Progress"});
        statusCombo.setBounds(fieldX, startY + gap * 2, fieldWidth, 24);
        add(statusCombo);

        // 4. Tariff / Price
        JLabel l4 = new JLabel("Tariff per Night (₹):");
        l4.setFont(new Font("Tahoma", Font.PLAIN, 14));
        l4.setBounds(labelX, startY + gap * 3, 140, 22);
        add(l4);

        priceField = new JTextField();
        priceField.setBounds(fieldX, startY + gap * 3, fieldWidth, 24);
        add(priceField);

        // 5. Bed Type
        JLabel l5 = new JLabel("Bed Configuration:");
        l5.setFont(new Font("Tahoma", Font.PLAIN, 14));
        l5.setBounds(labelX, startY + gap * 4, 130, 22);
        add(l5);

        bedTypeCombo = new JComboBox<>(new String[]{"Single Bed", "Double Bed"});
        bedTypeCombo.setBounds(fieldX, startY + gap * 4, fieldWidth, 24);
        add(bedTypeCombo);

        // Buttons
        submitButton = new JButton("Add Room");
        submitButton.setBounds(labelX, startY + gap * 5 + 10, 130, 35);
        submitButton.setBackground(new Color(24, 43, 73));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.addActionListener(this);
        add(submitButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(labelX + 150, startY + gap * 5 + 10, 110, 35);
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(this);
        add(cancelButton);

        // Graphic Illustration
        Image sideImg = UIUtils.getScaledImage("icons/twelve.jpg", 380, 280);
        JLabel imgLabel = new JLabel(new ImageIcon(sideImg));
        imgLabel.setBounds(410, 45, 380, 280);
        add(imgLabel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submitButton) {
            handleAddRoom();
        } else if (ae.getSource() == cancelButton) {
            setVisible(false);
            dispose();
        }
    }

    private void handleAddRoom() {
        String roomNo = roomNoField.getText().trim();
        String available = (String) availableCombo.getSelectedItem();
        String status = (String) statusCombo.getSelectedItem();
        String price = priceField.getText().trim();
        String type = (String) bedTypeCombo.getSelectedItem();

        if (!ValidationUtils.isNotEmpty(roomNo)) {
            JOptionPane.showMessageDialog(this, "Please enter a Room Number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            roomNoField.requestFocusInWindow();
            return;
        }

        if (!ValidationUtils.isPositiveDouble(price)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid positive room rate/tariff.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            priceField.requestFocusInWindow();
            return;
        }

        // Check if room number already exists
        String checkSql = "SELECT roomNo FROM room WHERE roomNo = ?";
        String insertSql = "INSERT INTO room (roomNo, available, status, price, type) VALUES (?, ?, ?, ?, ?)";

        try (Conn conn = new Conn();
             PreparedStatement psCheck = conn.prepareStatement(checkSql)) {

            psCheck.setString(1, roomNo);
            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Room Number " + roomNo + " already exists in inventory.", "Duplicate Room", JOptionPane.WARNING_MESSAGE);
                    roomNoField.requestFocusInWindow();
                    return;
                }
            }

            try (PreparedStatement psInsert = conn.prepareStatement(insertSql)) {
                psInsert.setString(1, roomNo);
                psInsert.setString(2, available);
                psInsert.setString(3, status);
                psInsert.setString(4, price);
                psInsert.setString(5, type);
                psInsert.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Room " + roomNo + " added successfully!", "Room Added", JOptionPane.INFORMATION_MESSAGE);
            setVisible(false);
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to add room: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddRooms());
    }
}
