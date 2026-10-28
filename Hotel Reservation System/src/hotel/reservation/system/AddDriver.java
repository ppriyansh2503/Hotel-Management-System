package hotel.reservation.system;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

/**
 * Add Chauffeur / Driver Form.
 * Validates driver and vehicle data, inserting records via parameterized queries.
 */
public class AddDriver extends JFrame implements ActionListener {
    private JTextField nameField, ageField, companyField, brandField, locationField;
    private JComboBox<String> genderCombo, availableCombo;
    private JButton submitButton, cancelButton;

    public AddDriver() {
        UIUtils.applyModernTheme();

        setTitle("The Grand Hotel - Add Driver / Chauffeur");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 480);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Header
        JLabel titleLabel = new JLabel("ADD CHAUFFEUR & VEHICLE DETAILS");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        titleLabel.setForeground(new Color(24, 43, 73));
        titleLabel.setBounds(60, 15, 400, 25);
        add(titleLabel);

        int labelX = 60;
        int fieldX = 200;
        int fieldWidth = 160;
        int startY = 60;
        int gap = 38;

        // 1. Name
        JLabel l1 = new JLabel("Driver Name:");
        l1.setFont(new Font("Tahoma", Font.PLAIN, 14));
        l1.setBounds(labelX, startY, 130, 22);
        add(l1);

        nameField = new JTextField();
        nameField.setBounds(fieldX, startY, fieldWidth, 24);
        add(nameField);

        // 2. Age
        JLabel l2 = new JLabel("Age:");
        l2.setFont(new Font("Tahoma", Font.PLAIN, 14));
        l2.setBounds(labelX, startY + gap, 130, 22);
        add(l2);

        ageField = new JTextField();
        ageField.setBounds(fieldX, startY + gap, fieldWidth, 24);
        add(ageField);

        // 3. Gender
        JLabel l3 = new JLabel("Gender:");
        l3.setFont(new Font("Tahoma", Font.PLAIN, 14));
        l3.setBounds(labelX, startY + gap * 2, 130, 22);
        add(l3);

        genderCombo = new JComboBox<>(new String[]{"Male", "Female"});
        genderCombo.setBounds(fieldX, startY + gap * 2, fieldWidth, 24);
        add(genderCombo);

        // 4. Car Company / Make
        JLabel l4 = new JLabel("Vehicle Make:");
        l4.setFont(new Font("Tahoma", Font.PLAIN, 14));
        l4.setBounds(labelX, startY + gap * 3, 130, 22);
        add(l4);

        companyField = new JTextField();
        companyField.setBounds(fieldX, startY + gap * 3, fieldWidth, 24);
        add(companyField);

        // 5. Car Model / Brand
        JLabel l5 = new JLabel("Vehicle Model:");
        l5.setFont(new Font("Tahoma", Font.PLAIN, 14));
        l5.setBounds(labelX, startY + gap * 4, 130, 22);
        add(l5);

        brandField = new JTextField();
        brandField.setBounds(fieldX, startY + gap * 4, fieldWidth, 24);
        add(brandField);

        // 6. Availability
        JLabel l6 = new JLabel("Availability:");
        l6.setFont(new Font("Tahoma", Font.PLAIN, 14));
        l6.setBounds(labelX, startY + gap * 5, 130, 22);
        add(l6);

        availableCombo = new JComboBox<>(new String[]{"Available", "Busy"});
        availableCombo.setBounds(fieldX, startY + gap * 5, fieldWidth, 24);
        add(availableCombo);

        // 7. Base Location
        JLabel l7 = new JLabel("Station / Base:");
        l7.setFont(new Font("Tahoma", Font.PLAIN, 14));
        l7.setBounds(labelX, startY + gap * 6, 130, 22);
        add(l7);

        locationField = new JTextField();
        locationField.setBounds(fieldX, startY + gap * 6, fieldWidth, 24);
        add(locationField);

        // Buttons
        submitButton = new JButton("Add Driver");
        submitButton.setBounds(labelX, startY + gap * 7 + 10, 130, 35);
        submitButton.setBackground(new Color(24, 43, 73));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.addActionListener(this);
        add(submitButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(labelX + 150, startY + gap * 7 + 10, 110, 35);
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(this);
        add(cancelButton);

        // Side Image
        Image sideImg = UIUtils.getScaledImage("icons/eleven.jpg", 380, 280);
        JLabel imgLabel = new JLabel(new ImageIcon(sideImg));
        imgLabel.setBounds(400, 50, 380, 280);
        add(imgLabel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submitButton) {
            handleAddDriver();
        } else if (ae.getSource() == cancelButton) {
            setVisible(false);
            dispose();
        }
    }

    private void handleAddDriver() {
        String name = nameField.getText().trim();
        String age = ageField.getText().trim();
        String gender = (String) genderCombo.getSelectedItem();
        String company = companyField.getText().trim();
        String brand = brandField.getText().trim();
        String available = (String) availableCombo.getSelectedItem();
        String location = locationField.getText().trim();

        if (!ValidationUtils.isNotEmpty(name)) {
            JOptionPane.showMessageDialog(this, "Please enter Driver Name.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            nameField.requestFocusInWindow();
            return;
        }

        if (!ValidationUtils.isPositiveInteger(age) || Integer.parseInt(age) < 18 || Integer.parseInt(age) > 75) {
            JOptionPane.showMessageDialog(this, "Please enter a valid age (18 - 75).", "Validation Error", JOptionPane.WARNING_MESSAGE);
            ageField.requestFocusInWindow();
            return;
        }

        if (!ValidationUtils.isNotEmpty(company)) {
            JOptionPane.showMessageDialog(this, "Please enter Vehicle Make/Company (e.g. Toyota, Maruti).", "Validation Error", JOptionPane.WARNING_MESSAGE);
            companyField.requestFocusInWindow();
            return;
        }

        if (!ValidationUtils.isNotEmpty(brand)) {
            JOptionPane.showMessageDialog(this, "Please enter Vehicle Model (e.g. Innova Crysta, Ertiga).", "Validation Error", JOptionPane.WARNING_MESSAGE);
            brandField.requestFocusInWindow();
            return;
        }

        if (!ValidationUtils.isNotEmpty(location)) {
            JOptionPane.showMessageDialog(this, "Please enter Station/Base location.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            locationField.requestFocusInWindow();
            return;
        }

        String sql = "INSERT INTO driver (name, age, gender, company, brand, available, location) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Conn conn = new Conn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, age);
            ps.setString(3, gender);
            ps.setString(4, company);
            ps.setString(5, brand);
            ps.setString(6, available);
            ps.setString(7, location);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Driver " + name + " added successfully!", "Driver Registered", JOptionPane.INFORMATION_MESSAGE);
            setVisible(false);
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to register driver: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddDriver());
    }
}
