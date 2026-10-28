package hotel.reservation.system;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import net.proteanit.sql.DbUtils;

/**
 * Room Search and Availability Filter Screen.
 * Allows filtering by Bed Type and instantly checking available inventory.
 */
public class SearchRoom extends JFrame implements ActionListener {
    private JTable table;
    private JComboBox<String> bedTypeCombo;
    private JCheckBox availableOnlyCheck;
    private JButton searchButton, showAllButton, backButton;

    public SearchRoom() {
        UIUtils.applyModernTheme();

        setTitle("The Grand Hotel - Room Search & Filter");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 560);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        // Filter Bar (Top)
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 12));
        filterPanel.setBackground(new Color(24, 43, 73));

        JLabel titleLabel = new JLabel("SEARCH ROOMS");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        filterPanel.add(titleLabel);

        JLabel typeLabel = new JLabel("Bed Type:");
        typeLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        typeLabel.setForeground(Color.WHITE);
        filterPanel.add(typeLabel);

        bedTypeCombo = new JComboBox<>(new String[]{"All Types", "Single Bed", "Double Bed"});
        bedTypeCombo.setPreferredSize(new Dimension(140, 26));
        filterPanel.add(bedTypeCombo);

        availableOnlyCheck = new JCheckBox("Available Only");
        availableOnlyCheck.setForeground(Color.WHITE);
        availableOnlyCheck.setFont(new Font("Tahoma", Font.PLAIN, 13));
        availableOnlyCheck.setOpaque(false);
        filterPanel.add(availableOnlyCheck);

        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        searchButton.addActionListener(this);
        filterPanel.add(searchButton);

        showAllButton = new JButton("Reset");
        showAllButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
        showAllButton.addActionListener(this);
        filterPanel.add(showAllButton);

        add(filterPanel, BorderLayout.NORTH);

        // Table in JScrollPane (Center)
        table = new JTable();
        table.setRowHeight(25);
        table.setFont(new Font("Tahoma", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(230, 235, 245));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Footer Actions (Bottom)
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        footerPanel.setBackground(Color.WHITE);

        backButton = new JButton("Back to Reception");
        backButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
        backButton.setBackground(new Color(108, 117, 125));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(this);
        footerPanel.add(backButton);

        add(footerPanel, BorderLayout.SOUTH);

        loadRooms(null, false);
        setVisible(true);
    }

    private void loadRooms(String type, boolean availableOnly) {
        StringBuilder sql = new StringBuilder("SELECT roomNo, available, status, price, type FROM room WHERE 1=1");

        if (type != null && !type.equals("All Types")) {
            sql.append(" AND type = ?");
        }
        if (availableOnly) {
            sql.append(" AND available = 'Available'");
        }
        sql.append(" ORDER BY roomNo ASC");

        try (Conn conn = new Conn();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            if (type != null && !type.equals("All Types")) {
                ps.setString(1, type);
            }

            try (ResultSet rs = ps.executeQuery()) {
                table.setModel(DbUtils.resultSetToTableModel(rs));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to search rooms: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == searchButton) {
            String selectedType = (String) bedTypeCombo.getSelectedItem();
            boolean availOnly = availableOnlyCheck.isSelected();
            loadRooms(selectedType, availOnly);
        } else if (ae.getSource() == showAllButton) {
            bedTypeCombo.setSelectedIndex(0);
            availableOnlyCheck.setSelected(false);
            loadRooms(null, false);
        } else if (ae.getSource() == backButton) {
            setVisible(false);
            dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SearchRoom());
    }
}
