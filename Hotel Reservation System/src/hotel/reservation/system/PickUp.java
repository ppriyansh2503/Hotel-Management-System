package hotel.reservation.system;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import net.proteanit.sql.DbUtils;

/**
 * Driver Pickup & Transport Dispatch Service.
 * Allows filtering by vehicle brand, viewing availability, and assigning drivers to guests.
 */
public class PickUp extends JFrame implements ActionListener {
    private JTable table;
    private JComboBox<String> brandCombo;
    private JButton filterButton, showAllButton, assignButton, backButton;

    public PickUp() {
        UIUtils.applyModernTheme();

        setTitle("The Grand Hotel - Airport & Station Pickup Services");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(880, 560);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        // Header and Filter Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 12));
        topPanel.setBackground(new Color(24, 43, 73));

        JLabel titleLabel = new JLabel("PICKUP & CHAUFFEUR SERVICES");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);

        JLabel brandLabel = new JLabel("Vehicle Model:");
        brandLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        brandLabel.setForeground(Color.WHITE);
        topPanel.add(brandLabel);

        brandCombo = new JComboBox<>();
        brandCombo.setPreferredSize(new Dimension(170, 26));
        loadDistinctBrands();
        topPanel.add(brandCombo);

        filterButton = new JButton("Filter");
        filterButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        filterButton.addActionListener(this);
        topPanel.add(filterButton);

        showAllButton = new JButton("Show All");
        showAllButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
        showAllButton.addActionListener(this);
        topPanel.add(showAllButton);

        add(topPanel, BorderLayout.NORTH);

        // Center Table in JScrollPane
        table = new JTable();
        table.setRowHeight(25);
        table.setFont(new Font("Tahoma", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(230, 235, 245));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Footer Actions
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        footerPanel.setBackground(Color.WHITE);

        assignButton = new JButton("Dispatch Driver for Guest");
        assignButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        assignButton.setBackground(new Color(24, 43, 73));
        assignButton.setForeground(Color.WHITE);
        assignButton.addActionListener(this);
        footerPanel.add(assignButton);

        backButton = new JButton("Back to Reception");
        backButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
        backButton.setBackground(new Color(108, 117, 125));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(this);
        footerPanel.add(backButton);

        add(footerPanel, BorderLayout.SOUTH);

        loadDrivers(null);
        setVisible(true);
    }

    private void loadDistinctBrands() {
        brandCombo.removeAllItems();
        String sql = "SELECT DISTINCT brand FROM driver ORDER BY brand ASC";
        try (Conn conn = new Conn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                brandCombo.addItem(rs.getString("brand"));
            }
        } catch (Exception e) {
            System.err.println("Error loading brands: " + e.getMessage());
        }
    }

    private void loadDrivers(String brandFilter) {
        String sql;
        if (brandFilter != null && !brandFilter.isEmpty()) {
            sql = "SELECT name, age, gender, company, brand, available, location FROM driver WHERE brand = ? ORDER BY name ASC";
        } else {
            sql = "SELECT name, age, gender, company, brand, available, location FROM driver ORDER BY name ASC";
        }

        try (Conn conn = new Conn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (brandFilter != null && !brandFilter.isEmpty()) {
                ps.setString(1, brandFilter);
            }

            try (ResultSet rs = ps.executeQuery()) {
                table.setModel(DbUtils.resultSetToTableModel(rs));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load drivers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == filterButton) {
            String selectedBrand = (String) brandCombo.getSelectedItem();
            loadDrivers(selectedBrand);
        } else if (ae.getSource() == showAllButton) {
            loadDrivers(null);
        } else if (ae.getSource() == assignButton) {
            handleDispatch();
        } else if (ae.getSource() == backButton) {
            setVisible(false);
            dispose();
        }
    }

    private void handleDispatch() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an available driver from the table.", "Select Driver", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String driverName = String.valueOf(table.getValueAt(row, 0));
        String currentStatus = String.valueOf(table.getValueAt(row, 5));

        if ("Busy".equalsIgnoreCase(currentStatus)) {
            JOptionPane.showMessageDialog(this, "Driver " + driverName + " is currently busy on an active trip.", "Driver Busy", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String guestDestination = JOptionPane.showInputDialog(this,
            "Enter Guest Name & Pickup / Drop Destination for " + driverName + ":",
            "Dispatch Chauffeur", JOptionPane.QUESTION_MESSAGE);

        if (guestDestination != null && !guestDestination.trim().isEmpty()) {
            String updateSql = "UPDATE driver SET available = 'Busy' WHERE name = ?";
            try (Conn conn = new Conn();
                 PreparedStatement ps = conn.prepareStatement(updateSql)) {

                ps.setString(1, driverName);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,
                    "Driver " + driverName + " dispatched successfully for: " + guestDestination + "\nStatus set to: Busy",
                    "Dispatch Confirmed", JOptionPane.INFORMATION_MESSAGE);

                loadDrivers(null);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to update driver status: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PickUp());
    }
}
