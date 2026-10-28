package hotel.reservation.system;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

/**
 * Add Employee Form.
 * Validates staff credentials, Indian phone/email/Aadhar formats,
 * and inserts employee records using parameterized queries.
 */
public class AddEmployee extends JFrame implements ActionListener {
    private JTextField nameField, ageField, salaryField, phoneField, emailField, aadharField;
    private JRadioButton maleRadio, femaleRadio;
    private ButtonGroup genderGroup;
    private JComboBox<String> jobCombo;
    private JButton submitButton, cancelButton;

    public AddEmployee() {
        UIUtils.applyModernTheme();

        setTitle("The Grand Hotel - Add Staff Member");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(880, 560);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel("ADD EMPLOYEE DETAILS");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        titleLabel.setForeground(new Color(24, 43, 73));
        titleLabel.setBounds(50, 15, 300, 25);
        add(titleLabel);

        int labelX = 50;
        int fieldX = 180;
        int fieldWidth = 170;
        int startY = 60;
        int gap = 38;

        // 1. Name
        JLabel l1 = new JLabel("Staff Name:");
        l1.setFont(new Font("Tahoma", Font.PLAIN, 14));
        l1.setBounds(labelX, startY, 120, 22);
        add(l1);

        nameField = new JTextField();
        nameField.setBounds(fieldX, startY, fieldWidth, 24);
        add(nameField);

        // 2. Age
        JLabel l2 = new JLabel("Age:");
        l2.setFont(new Font("Tahoma", Font.PLAIN, 14));
        l2.setBounds(labelX, startY + gap, 120, 22);
        add(l2);

        ageField = new JTextField();
        ageField.setBounds(fieldX, startY + gap, fieldWidth, 24);
        add(ageField);

        // 3. Gender
        JLabel l3 = new JLabel("Gender:");
        l3.setFont(new Font("Tahoma", Font.PLAIN, 14));
        l3.setBounds(labelX, startY + gap * 2, 120, 22);
        add(l3);

        maleRadio = new JRadioButton("Male");
        maleRadio.setBackground(Color.WHITE);
        maleRadio.setSelected(true);
        maleRadio.setBounds(fieldX, startY + gap * 2, 70, 22);
        add(maleRadio);

        femaleRadio = new JRadioButton("Female");
        femaleRadio.setBackground(Color.WHITE);
        femaleRadio.setBounds(fieldX + 80, startY + gap * 2, 90, 22);
        add(femaleRadio);

        genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);

        // 4. Job Role
        JLabel l4 = new JLabel("Department / Job:");
        l4.setFont(new Font("Tahoma", Font.PLAIN, 14));
        l4.setBounds(labelX, startY + gap * 3, 130, 22);
        add(l4);

        String[] roles = {"Front Desk Clerks", "Housekeeping", "Kitchen Staff", "Room Service", "Waiter/Waitress", "Manager", "Accountant", "Chef", "Security Guard"};
        jobCombo = new JComboBox<>(roles);
        jobCombo.setBounds(fieldX, startY + gap * 3, fieldWidth, 24);
        add(jobCombo);

        // 5. Salary
        JLabel l5 = new JLabel("Monthly Salary (₹):");
        l5.setFont(new Font("Tahoma", Font.PLAIN, 14));
        l5.setBounds(labelX, startY + gap * 4, 130, 22);
        add(l5);

        salaryField = new JTextField();
        salaryField.setBounds(fieldX, startY + gap * 4, fieldWidth, 24);
        add(salaryField);

        // 6. Phone
        JLabel l6 = new JLabel("Mobile Number:");
        l6.setFont(new Font("Tahoma", Font.PLAIN, 14));
        l6.setBounds(labelX, startY + gap * 5, 120, 22);
        add(l6);

        phoneField = new JTextField();
        phoneField.setBounds(fieldX, startY + gap * 5, fieldWidth, 24);
        add(phoneField);

        // 7. Email
        JLabel l7 = new JLabel("Email Address:");
        l7.setFont(new Font("Tahoma", Font.PLAIN, 14));
        l7.setBounds(labelX, startY + gap * 6, 120, 22);
        add(l7);

        emailField = new JTextField();
        emailField.setBounds(fieldX, startY + gap * 6, fieldWidth, 24);
        add(emailField);

        // 8. Aadhar
        JLabel l8 = new JLabel("12-Digit Aadhar:");
        l8.setFont(new Font("Tahoma", Font.PLAIN, 14));
        l8.setBounds(labelX, startY + gap * 7, 120, 22);
        add(l8);

        aadharField = new JTextField();
        aadharField.setBounds(fieldX, startY + gap * 7, fieldWidth, 24);
        add(aadharField);

        // Buttons
        submitButton = new JButton("Submit Employee");
        submitButton.setBounds(labelX, startY + gap * 8 + 10, 150, 35);
        submitButton.setBackground(new Color(24, 43, 73));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.addActionListener(this);
        add(submitButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(labelX + 170, startY + gap * 8 + 10, 110, 35);
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(this);
        add(cancelButton);

        // Graphic Illustration
        Image sideImg = UIUtils.getScaledImage("icons/tenth.jpg", 400, 380);
        JLabel imgLabel = new JLabel(new ImageIcon(sideImg));
        imgLabel.setBounds(420, 50, 420, 380);
        add(imgLabel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submitButton) {
            handleAddEmployee();
        } else if (ae.getSource() == cancelButton) {
            setVisible(false);
            dispose();
        }
    }

    private void handleAddEmployee() {
        String name = nameField.getText().trim();
        String age = ageField.getText().trim();
        String gender = maleRadio.isSelected() ? "male" : "female";
        String job = (String) jobCombo.getSelectedItem();
        String salary = salaryField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String aadhar = aadharField.getText().trim();

        if (!ValidationUtils.isNotEmpty(name)) {
            JOptionPane.showMessageDialog(this, "Please enter Employee Name.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            nameField.requestFocusInWindow();
            return;
        }

        if (!ValidationUtils.isPositiveInteger(age) || Integer.parseInt(age) < 18 || Integer.parseInt(age) > 75) {
            JOptionPane.showMessageDialog(this, "Please enter a valid age (18 - 75).", "Validation Error", JOptionPane.WARNING_MESSAGE);
            ageField.requestFocusInWindow();
            return;
        }

        if (!ValidationUtils.isPositiveDouble(salary)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid monthly salary.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            salaryField.requestFocusInWindow();
            return;
        }

        if (!ValidationUtils.isValidPhone(phone)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid 10-digit mobile number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            phoneField.requestFocusInWindow();
            return;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            emailField.requestFocusInWindow();
            return;
        }

        if (!ValidationUtils.isValidAadhar(aadhar)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid 12-digit Aadhar number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            aadharField.requestFocusInWindow();
            return;
        }

        String sql = "INSERT INTO employee (name, age, gender, job, salary, phone, email, aadhar) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Conn conn = new Conn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, age);
            ps.setString(3, gender);
            ps.setString(4, job);
            ps.setString(5, salary);
            ps.setString(6, phone);
            ps.setString(7, email);
            ps.setString(8, aadhar);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Employee " + name + " added successfully!", "Staff Registered", JOptionPane.INFORMATION_MESSAGE);
            setVisible(false);
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to register employee: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddEmployee());
    }
}
