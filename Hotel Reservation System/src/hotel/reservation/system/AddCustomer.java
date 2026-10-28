package hotel.reservation.system;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

/**
 * New Customer Registration & Check-in Form.
 * Validates inputs, handles room availability safely via transactions,
 * and records check-in timestamp.
 */
public class AddCustomer extends JFrame implements ActionListener {
    private JComboBox<String> idDocCombo;
    private JTextField numberField, nameField, countryField, depositField;
    private JRadioButton maleRadio, femaleRadio;
    private ButtonGroup genderGroup;
    private JComboBox<String> roomCombo;
    private JLabel checkinTimeLabel;
    private JButton submitButton, backButton;

    public AddCustomer() {
        UIUtils.applyModernTheme();

        setTitle("The Grand Hotel - New Customer Registration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 480);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Header Title
        JLabel titleLabel = new JLabel("NEW GUEST CHECK-IN REGISTRATION");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        titleLabel.setForeground(new Color(24, 43, 73));
        titleLabel.setBounds(50, 15, 400, 25);
        add(titleLabel);

        // Form Fields
        int labelX = 50;
        int fieldX = 220;
        int fieldWidth = 180;
        int startY = 60;
        int gap = 38;

        // 1. ID Type
        JLabel idLabel = new JLabel("ID Proof Type:");
        idLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        idLabel.setBounds(labelX, startY, 150, 22);
        add(idLabel);

        idDocCombo = new JComboBox<>(new String[]{"Aadhar Card", "Passport", "Driving license", "Voter Id"});
        idDocCombo.setBounds(fieldX, startY, fieldWidth, 24);
        add(idDocCombo);

        // 2. ID Number
        JLabel numLabel = new JLabel("ID Document No:");
        numLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        numLabel.setBounds(labelX, startY + gap, 150, 22);
        add(numLabel);

        numberField = new JTextField();
        numberField.setBounds(fieldX, startY + gap, fieldWidth, 24);
        add(numberField);

        // 3. Guest Name
        JLabel nameLabel = new JLabel("Guest Full Name:");
        nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        nameLabel.setBounds(labelX, startY + gap * 2, 150, 22);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(fieldX, startY + gap * 2, fieldWidth, 24);
        add(nameField);

        // 4. Gender
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        genderLabel.setBounds(labelX, startY + gap * 3, 150, 22);
        add(genderLabel);

        maleRadio = new JRadioButton("Male");
        maleRadio.setBackground(Color.WHITE);
        maleRadio.setSelected(true);
        maleRadio.setBounds(fieldX, startY + gap * 3, 70, 22);
        add(maleRadio);

        femaleRadio = new JRadioButton("Female");
        femaleRadio.setBackground(Color.WHITE);
        femaleRadio.setBounds(fieldX + 80, startY + gap * 3, 90, 22);
        add(femaleRadio);

        genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);

        // 5. Country
        JLabel countryLabel = new JLabel("Country / City:");
        countryLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        countryLabel.setBounds(labelX, startY + gap * 4, 150, 22);
        add(countryLabel);

        countryField = new JTextField("India");
        countryField.setBounds(fieldX, startY + gap * 4, fieldWidth, 24);
        add(countryField);

        // 6. Allocated Room
        JLabel roomLabel = new JLabel("Allocated Room:");
        roomLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        roomLabel.setBounds(labelX, startY + gap * 5, 150, 22);
        add(roomLabel);

        roomCombo = new JComboBox<>();
        roomCombo.setBounds(fieldX, startY + gap * 5, fieldWidth, 24);
        loadAvailableRooms();
        add(roomCombo);

        // 7. Check-in Timestamp (Fixed coordinate overlap)
        JLabel timeLabel = new JLabel("Check-in Time:");
        timeLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        timeLabel.setBounds(labelX, startY + gap * 6, 150, 22);
        add(timeLabel);

        String currentTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        checkinTimeLabel = new JLabel(currentTimestamp);
        checkinTimeLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        checkinTimeLabel.setForeground(new Color(24, 43, 73));
        checkinTimeLabel.setBounds(fieldX, startY + gap * 6, 200, 22);
        add(checkinTimeLabel);

        // 8. Advance Deposit
        JLabel depositLabel = new JLabel("Advance Deposit (₹):");
        depositLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        depositLabel.setBounds(labelX, startY + gap * 7, 150, 22);
        add(depositLabel);

        depositField = new JTextField("2000");
        depositField.setBounds(fieldX, startY + gap * 7, fieldWidth, 24);
        add(depositField);

        // Buttons
        submitButton = new JButton("Register & Check-in");
        submitButton.setBounds(labelX, startY + gap * 8 + 10, 170, 35);
        submitButton.setBackground(new Color(24, 43, 73));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.addActionListener(this);
        add(submitButton);

        backButton = new JButton("Cancel");
        backButton.setBounds(labelX + 190, startY + gap * 8 + 10, 130, 35);
        backButton.setBackground(new Color(108, 117, 125));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(this);
        add(backButton);

        // Graphic Illustration
        Image sideImg = UIUtils.getScaledImage("icons/fifth.jpg", 370, 360);
        JLabel imgLabel = new JLabel(new ImageIcon(sideImg));
        imgLabel.setBounds(440, 45, 370, 360);
        add(imgLabel);

        setVisible(true);
    }

    private void loadAvailableRooms() {
        roomCombo.removeAllItems();
        String query = "SELECT roomNo, type, price FROM room WHERE available = 'Available' ORDER BY roomNo ASC";
        try (Conn conn = new Conn();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String roomNo = rs.getString("roomNo");
                String type = rs.getString("type");
                String price = rs.getString("price");
                roomCombo.addItem(roomNo + " (" + type + " - ₹" + price + ")");
            }
        } catch (Exception e) {
            System.err.println("Error loading rooms: " + e.getMessage());
        }

        if (roomCombo.getItemCount() == 0) {
            roomCombo.addItem("No Rooms Available");
            if (submitButton != null) submitButton.setEnabled(false);
        } else {
            if (submitButton != null) submitButton.setEnabled(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submitButton) {
            handleCheckin();
        } else if (ae.getSource() == backButton) {
            setVisible(false);
            dispose();
        }
    }

    private void handleCheckin() {
        String idDoc = (String) idDocCombo.getSelectedItem();
        String idNumber = numberField.getText().trim();
        String guestName = nameField.getText().trim();
        String gender = maleRadio.isSelected() ? "Male" : "Female";
        String country = countryField.getText().trim();
        String selectedRoomRaw = (String) roomCombo.getSelectedItem();
        String checkinTime = checkinTimeLabel.getText();
        String deposit = depositField.getText().trim();

        // Validation
        if (!ValidationUtils.isNotEmpty(idNumber)) {
            JOptionPane.showMessageDialog(this, "Please provide an ID Document Number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            numberField.requestFocusInWindow();
            return;
        }

        if ("Aadhar Card".equalsIgnoreCase(idDoc) && !ValidationUtils.isValidAadhar(idNumber)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid 12-digit Aadhar Number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            numberField.requestFocusInWindow();
            return;
        }

        if (!ValidationUtils.isNotEmpty(guestName)) {
            JOptionPane.showMessageDialog(this, "Please provide the Guest's Full Name.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            nameField.requestFocusInWindow();
            return;
        }

        if (!ValidationUtils.isNotEmpty(country)) {
            JOptionPane.showMessageDialog(this, "Please enter Country/City.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            countryField.requestFocusInWindow();
            return;
        }

        if (selectedRoomRaw == null || selectedRoomRaw.startsWith("No Rooms")) {
            JOptionPane.showMessageDialog(this, "No rooms are currently available for allocation.", "Room Unavailable", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!ValidationUtils.isNonNegativeDouble(deposit)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric deposit amount (0 or positive).", "Validation Error", JOptionPane.WARNING_MESSAGE);
            depositField.requestFocusInWindow();
            return;
        }

        // Extract Room Number
        String roomNo = selectedRoomRaw.split(" ")[0].trim();

        // Database Transaction for Atomic Insert & Room Occupancy Update
        try (Conn conn = new Conn()) {
            conn.c.setAutoCommit(false);
            try {
                // 1. Insert customer record
                String insertSql = "INSERT INTO customer (document, number, name, gender, country, room, checkintime, deposit, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'Checked-In')";
                try (PreparedStatement psCust = conn.prepareStatement(insertSql)) {
                    psCust.setString(1, idDoc);
                    psCust.setString(2, idNumber);
                    psCust.setString(3, guestName);
                    psCust.setString(4, gender);
                    psCust.setString(5, country);
                    psCust.setString(6, roomNo);
                    psCust.setString(7, checkinTime);
                    psCust.setString(8, deposit);
                    psCust.executeUpdate();
                }

                // 2. Mark Room as Occupied
                String updateRoomSql = "UPDATE room SET available = 'Occupied' WHERE roomNo = ?";
                try (PreparedStatement psRoom = conn.prepareStatement(updateRoomSql)) {
                    psRoom.setString(1, roomNo);
                    psRoom.executeUpdate();
                }

                conn.c.commit();

                JOptionPane.showMessageDialog(this,
                    "Guest " + guestName + " successfully registered and checked into Room " + roomNo + "!",
                    "Check-in Successful", JOptionPane.INFORMATION_MESSAGE);

                setVisible(false);
                dispose();

            } catch (Exception ex) {
                conn.c.rollback();
                throw ex;
            } finally {
                conn.c.setAutoCommit(true);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to register customer: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddCustomer());
    }
}
