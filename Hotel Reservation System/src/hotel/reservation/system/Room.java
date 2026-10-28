package hotel.reservation.system;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import net.proteanit.sql.DbUtils;

/**
 * Room Inventory Screen.
 * Displays all hotel rooms with live status, tariffs, and bed configurations.
 */
public class Room extends JFrame implements ActionListener {
    private JTable table;
    private JButton backButton;

    public Room() {
        UIUtils.applyModernTheme();

        setTitle("The Grand Hotel - Room Inventory Directory");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(880, 560);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel("ROOM INVENTORY DIRECTORY");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        titleLabel.setForeground(new Color(24, 43, 73));
        titleLabel.setBounds(30, 15, 350, 25);
        add(titleLabel);

        // Table in JScrollPane
        table = new JTable();
        table.setRowHeight(25);
        table.setFont(new Font("Tahoma", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(230, 235, 245));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 55, 450, 400);
        add(scrollPane);

        // Side Image
        Image sideImg = UIUtils.getScaledImage("icons/eight.jpg", 340, 400);
        JLabel sideLabel = new JLabel(new ImageIcon(sideImg));
        sideLabel.setBounds(500, 55, 340, 400);
        add(sideLabel);

        // Back Button
        backButton = new JButton("Back to Reception");
        backButton.setBounds(30, 470, 180, 35);
        backButton.setBackground(new Color(24, 43, 73));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(this);
        add(backButton);

        loadRooms();
        setVisible(true);
    }

    private void loadRooms() {
        String sql = "SELECT roomNo, available, status, price, type FROM room ORDER BY roomNo ASC";
        try (Conn conn = new Conn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load room inventory: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        setVisible(false);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Room());
    }
}
