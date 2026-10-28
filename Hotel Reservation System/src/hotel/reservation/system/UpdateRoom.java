package hotel.reservation.system;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

/**
 * Room Status & Maintenance Management.
 * Allows updating availability (Available, Occupied, Under Maintenance)
 * and housekeeping cleaning status (Cleaned, Dirty, In Progress) for any room.
 */
public class UpdateRoom extends JFrame implements ActionListener {
    private JComboBox<String> roomCombo;
    private JComboBox<String> availabilityCombo;
    private JComboBox<String> statusCombo;
    private JLabel roomTypeLabel, roomPriceLabel;
    private JButton checkButton, updateButton, backButton;

    public UpdateRoom() {
        UIUtils.applyModernTheme();

        setTitle("The Grand Hotel - Update Room Status");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(750, 420);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Header
        JLabel titleLabel = new JLabel("UPDATE ROOM & HOUSEKEEPING STATUS");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        titleLabel.setForeground(new Color(24, 43, 73));
        titleLabel.setBounds(40, 15, 450, 25);
        add(titleLabel);

        // Form Coordinates
        int labelX = 40;
        int fieldX = 180;
        int fieldWidth = 180;
        int startY = 65;
        int gap = 38;

        // 1. Select Room Number
        JLabel roomLabel = new JLabel("Select Room No:");
        roomLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        roomLabel.setBounds(labelX, startY, 130, 22);
        add(roomLabel);

        roomCombo = new JComboBox<>();
        roomCombo.setBounds(fieldX, startY, fieldWidth, 26);
        roomCombo.addActionListener(e -> loadRoomDetails());
        add(roomCombo);

        // 2. Room Type & Price Display
        JLabel typeTitle = new JLabel("Room Details:");
        typeTitle.setFont(new Font("Tahoma", Font.PLAIN, 14));
        typeTitle.setBounds(labelX, startY + gap, 130, 22);
        add(typeTitle);

        roomTypeLabel = new JLabel("Type: -");
        roomTypeLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        roomTypeLabel.setBounds(fieldX, startY + gap, 180, 22);
        add(roomTypeLabel);

        // 3. Availability Dropdown
        JLabel availLabel = new JLabel("Availability:");
        availLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        availLabel.setBounds(labelX, startY + gap * 2, 130, 22);
        add(availLabel);

        availabilityCombo = new JComboBox<>(new String[]{"Available", "Occupied", "Under Maintenance"});
        availabilityCombo.setBounds(fieldX, startY + gap * 2, fieldWidth, 26);
        add(availabilityCombo);

        // 4. Cleaning Status Dropdown
        JLabel statusLabel = new JLabel("Cleaning Status:");
        statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        statusLabel.setBounds(labelX, startY + gap * 3, 130, 22);
        add(statusLabel);

        statusCombo = new JComboBox<>(new String[]{"Cleaned", "Dirty", "Cleaning In Progress"});
        statusCombo.setBounds(fieldX, startY + gap * 3, fieldWidth, 26);
        add(statusCombo);

        // Action Buttons
        updateButton = new JButton("Save Status");
        updateButton.setBounds(labelX, startY + gap * 4 + 10, 140, 35);
        updateButton.setBackground(new Color(24, 43, 73));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateButton.addActionListener(this);
        add(updateButton);

        backButton = new JButton("Cancel");
        backButton.setBounds(labelX + 160, startY + gap * 4 + 10, 110, 35);
        backButton.setBackground(new Color(108, 117, 125));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(this);
        add(backButton);

        // Side Graphic
        Image sideImg = UIUtils.getScaledImage("icons/seventh.jpg", 320, 250);
        JLabel imgLabel = new JLabel(new ImageIcon(sideImg));
        imgLabel.setBounds(390, 50, 320, 250);
        add(imgLabel);

        loadAllRooms();
        setVisible(true);
    }

    private void loadAllRooms() {
        roomCombo.removeAllItems();
        String query = "SELECT roomNo FROM room ORDER BY roomNo ASC";
        try (Conn conn = new Conn();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                roomCombo.addItem(rs.getString("roomNo"));
            }
        } catch (Exception e) {
            System.err.println("Error loading rooms: " + e.getMessage());
        }

        if (roomCombo.getItemCount() > 0) {
            loadRoomDetails();
        }
    }

    private void loadRoomDetails() {
        String selectedRoom = (String) roomCombo.getSelectedItem();
        if (selectedRoom == null) return;

        String query = "SELECT available, status, type, price FROM room WHERE roomNo = ?";
        try (Conn conn = new Conn();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, selectedRoom);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String avail = rs.getString("available");
                    String status = rs.getString("status");
                    String type = rs.getString("type");
                    String price = rs.getString("price");

                    roomTypeLabel.setText(type + " (₹" + price + ")");
                    availabilityCombo.setSelectedItem(avail);
                    statusCombo.setSelectedItem(status);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading room details: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == updateButton) {
            handleUpdate();
        } else if (ae.getSource() == backButton) {
            setVisible(false);
            dispose();
        }
    }

    private void handleUpdate() {
        String selectedRoom = (String) roomCombo.getSelectedItem();
        String newAvail = (String) availabilityCombo.getSelectedItem();
        String newStatus = (String) statusCombo.getSelectedItem();

        if (selectedRoom == null) return;

        String updateSql = "UPDATE room SET available = ?, status = ? WHERE roomNo = ?";
        try (Conn conn = new Conn();
             PreparedStatement ps = conn.prepareStatement(updateSql)) {

            ps.setString(1, newAvail);
            ps.setString(2, newStatus);
            ps.setString(3, selectedRoom);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this,
                    "Room " + selectedRoom + " updated successfully to: " + newAvail + " (" + newStatus + ")",
                    "Update Successful", JOptionPane.INFORMATION_MESSAGE);
                setVisible(false);
                dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to update room: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpdateRoom());
    }
}
