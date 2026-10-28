package hotel.reservation.system;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import net.proteanit.sql.DbUtils;

/**
 * Employee Staff Directory.
 * Displays all hotel staff members across front office, housekeeping, kitchen, and management.
 */
public class EmployeeInfo extends JFrame implements ActionListener {
    private JTable table;
    private JButton backButton;

    public EmployeeInfo() {
        UIUtils.applyModernTheme();

        setTitle("The Grand Hotel - All Employees Directory");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(960, 560);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        headerPanel.setBackground(new Color(24, 43, 73));

        JLabel titleLabel = new JLabel("HOTEL STAFF & EMPLOYEE DIRECTORY");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Center Table in JScrollPane
        table = new JTable();
        table.setRowHeight(25);
        table.setFont(new Font("Tahoma", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(230, 235, 245));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 12));
        footerPanel.setBackground(Color.WHITE);

        backButton = new JButton("Back to Reception");
        backButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        backButton.setBackground(new Color(108, 117, 125));
        backButton.setForeground(Color.WHITE);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(this);
        footerPanel.add(backButton);

        add(footerPanel, BorderLayout.SOUTH);

        loadEmployees();
        setVisible(true);
    }

    private void loadEmployees() {
        String sql = "SELECT name, age, gender, job, salary, phone, email, aadhar FROM employee ORDER BY id ASC";
        try (Conn conn = new Conn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load employees: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        setVisible(false);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeeInfo());
    }
}
