package hotel.reservation.system;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import net.proteanit.sql.DbUtils;

/**
 * Hotel Department Budgets Screen.
 * Displays all operational divisions and allocated operational budgets.
 */
public class Department extends JFrame implements ActionListener {
    private JTable table;
    private JLabel totalBudgetLabel;
    private JButton backButton;

    public Department() {
        UIUtils.applyModernTheme();

        setTitle("The Grand Hotel - Department Budgets");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(720, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        headerPanel.setBackground(new Color(24, 43, 73));

        JLabel titleLabel = new JLabel("HOTEL OPERATIONAL DEPARTMENTS & BUDGETS");
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

        // Footer with Total and Back Button (VISIBLE & CLICKABLE)
        JPanel footerPanel = new JPanel(new BorderLayout(15, 10));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));
        footerPanel.setBackground(Color.WHITE);

        totalBudgetLabel = new JLabel("Total Operational Budget: ₹0");
        totalBudgetLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        totalBudgetLabel.setForeground(new Color(24, 43, 73));
        footerPanel.add(totalBudgetLabel, BorderLayout.WEST);

        backButton = new JButton("Back to Reception");
        backButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        backButton.setBackground(new Color(108, 117, 125));
        backButton.setForeground(Color.WHITE);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(this);
        footerPanel.add(backButton, BorderLayout.EAST);

        add(footerPanel, BorderLayout.SOUTH);

        loadDepartments();
        setVisible(true);
    }

    private void loadDepartments() {
        String sql = "SELECT department AS 'Department Name', budget AS 'Allocated Budget (₹)' FROM department ORDER BY budget DESC";
        try (Conn conn = new Conn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            table.setModel(DbUtils.resultSetToTableModel(rs));

            // Calculate total budget
            long total = 0;
            try (Statement s = conn.c.createStatement();
                 ResultSet sumRs = s.executeQuery("SELECT budget FROM department")) {
                while (sumRs.next()) {
                    try {
                        total += Long.parseLong(sumRs.getString(1).replaceAll("[^0-9]", ""));
                    } catch (Exception ignored) {}
                }
            }
            totalBudgetLabel.setText(String.format("Total Operational Budget: ₹%,d", total));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load departments: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        setVisible(false);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Department());
    }
}
