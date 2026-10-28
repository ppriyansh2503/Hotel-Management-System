package hotel.reservation.system;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

/**
 * Authentication Screen.
 * Uses parameterized queries, salted cryptographic password verification,
 * and passes the authenticated user role to the Dashboard.
 */
public class Login extends JFrame implements ActionListener {
    private JLabel userLabel, passLabel, titleLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, cancelButton;

    public Login() {
        UIUtils.applyModernTheme();

        setTitle("Hotel Management - Staff Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(620, 320);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        titleLabel = new JLabel("STAFF PORTAL LOGIN");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        titleLabel.setForeground(new Color(24, 43, 73));
        titleLabel.setBounds(40, 20, 300, 25);
        add(titleLabel);

        userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        userLabel.setBounds(40, 70, 100, 30);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        usernameField.setBounds(140, 70, 180, 32);
        add(usernameField);

        passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        passLabel.setBounds(40, 120, 100, 30);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        passwordField.setBounds(140, 120, 180, 32);
        add(passwordField);

        // Allow pressing Enter in password field to trigger login
        passwordField.addActionListener(this);

        loginButton = new JButton("Login");
        loginButton.setBounds(40, 185, 130, 38);
        loginButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(24, 43, 73));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(this);
        add(loginButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(190, 185, 130, 38);
        cancelButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(this);
        add(cancelButton);

        // Side Icon
        Image scaledIcon = UIUtils.getScaledImage("icons/second.jpg", 180, 180);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledIcon));
        iconLabel.setBounds(370, 35, 190, 190);
        add(iconLabel);

        // Demo credentials hint
        JLabel hintLabel = new JLabel("<html><small>Demo: <b>admin / Admin@123</b> (Admin) &bull; <b>sunita / Sunita@123</b> (Reception)</small></html>");
        hintLabel.setForeground(new Color(110, 120, 130));
        hintLabel.setBounds(40, 245, 520, 25);
        add(hintLabel);

        getRootPane().setDefaultButton(loginButton);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == loginButton || ae.getSource() == passwordField) {
            handleLogin();
        } else if (ae.getSource() == cancelButton) {
            System.exit(0);
        }
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "SELECT username, password, role, name FROM login WHERE username = ?";
        try (Conn conn = new Conn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    String role = rs.getString("role");
                    String fullName = rs.getString("name");

                    if (SecurityUtils.verifyPassword(password, storedHash)) {
                        setVisible(false);
                        dispose();
                        new Dashboard(username, role, fullName).setVisible(true);
                        return;
                    }
                }
            }

            // Authentication failure
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            passwordField.requestFocusInWindow();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login());
    }
}
