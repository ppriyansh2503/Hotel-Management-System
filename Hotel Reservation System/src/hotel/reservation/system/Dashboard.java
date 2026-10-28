package hotel.reservation.system;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Main Application Dashboard.
 * Integrates role-based navigation, active session display, and system menus.
 */
public class Dashboard extends JFrame implements ActionListener {
    private String username = "admin";
    private String userRole = "Admin";
    private String fullName = "Administrator";

    public Dashboard() {
        this("admin", "Admin", "Administrator");
    }

    public Dashboard(String username, String role, String fullName) {
        if (username != null) this.username = username;
        if (role != null) this.userRole = role;
        if (fullName != null) this.fullName = fullName;

        UIUtils.applyModernTheme();

        setTitle("The Grand Hotel & Resort - Management Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1450, 720);
        setLocationRelativeTo(null);
        setLayout(null);

        // Menu Bar (Standard Swing Menu Bar)
        JMenuBar mb = new JMenuBar();
        mb.setFont(new Font("Tahoma", Font.PLAIN, 14));

        // 1. HOTEL MANAGEMENT Menu
        JMenu hotelMenu = new JMenu("HOTEL MANAGEMENT");
        hotelMenu.setForeground(new Color(24, 43, 73));
        hotelMenu.setFont(new Font("Tahoma", Font.BOLD, 14));

        JMenuItem receptionItem = new JMenuItem("Reception Portal");
        receptionItem.addActionListener(this);
        hotelMenu.add(receptionItem);

        mb.add(hotelMenu);

        // 2. ADMIN Menu
        JMenu adminMenu = new JMenu("ADMINISTRATION");
        adminMenu.setForeground(new Color(180, 40, 40));
        adminMenu.setFont(new Font("Tahoma", Font.BOLD, 14));

        JMenuItem addEmployeeItem = new JMenuItem("Add Employee");
        addEmployeeItem.addActionListener(this);
        adminMenu.add(addEmployeeItem);

        JMenuItem addRoomsItem = new JMenuItem("Add Rooms");
        addRoomsItem.addActionListener(this);
        adminMenu.add(addRoomsItem);

        JMenuItem addDriversItem = new JMenuItem("Add Drivers");
        addDriversItem.addActionListener(this);
        adminMenu.add(addDriversItem);

        mb.add(adminMenu);

        // 3. ACCOUNT / LOGOUT Menu
        JMenu sessionMenu = new JMenu("SESSION (" + userRole.toUpperCase() + ")");
        sessionMenu.setForeground(new Color(40, 116, 166));
        sessionMenu.setFont(new Font("Tahoma", Font.BOLD, 14));

        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(this);
        sessionMenu.add(logoutItem);

        JMenuItem exitItem = new JMenuItem("Exit Application");
        exitItem.addActionListener(this);
        sessionMenu.add(exitItem);

        mb.add(sessionMenu);

        setJMenuBar(mb);

        // Background Image
        Image bgImg = UIUtils.getScaledImage("icons/third.jpg", 1450, 680);
        JLabel bgLabel = new JLabel(new ImageIcon(bgImg));
        bgLabel.setBounds(0, 0, 1450, 680);
        add(bgLabel);

        // Welcome Header
        JLabel welcomeLabel = new JLabel("THE GRAND HOTEL WELCOMES YOU");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Tahoma", Font.BOLD, 42));
        welcomeLabel.setBounds(300, 50, 950, 60);
        bgLabel.add(welcomeLabel);

        // User badge card
        JPanel userBadge = new JPanel();
        userBadge.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 8));
        userBadge.setBackground(new Color(0, 0, 0, 180));
        userBadge.setBounds(40, 550, 460, 45);

        JLabel userText = new JLabel("Logged in: " + fullName + " (" + userRole + ")");
        userText.setForeground(Color.WHITE);
        userText.setFont(new Font("Tahoma", Font.BOLD, 14));
        userBadge.add(userText);

        bgLabel.add(userBadge);

        // Quick Launch Reception Button
        JButton openReceptionBtn = new JButton("Open Reception Desk →");
        openReceptionBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
        openReceptionBtn.setBackground(new Color(24, 43, 73));
        openReceptionBtn.setForeground(Color.WHITE);
        openReceptionBtn.setBounds(1100, 550, 260, 45);
        openReceptionBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        openReceptionBtn.addActionListener(e -> {
            new Reception(username, userRole).setVisible(true);
        });
        bgLabel.add(openReceptionBtn);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();

        if ("Reception Portal".equals(cmd) || "RECEPTION".equalsIgnoreCase(cmd)) {
            new Reception(username, userRole).setVisible(true);
        } else if ("Add Employee".equalsIgnoreCase(cmd)) {
            if (checkAdminPrivileges()) {
                new AddEmployee().setVisible(true);
            }
        } else if ("Add Rooms".equalsIgnoreCase(cmd)) {
            if (checkAdminPrivileges()) {
                new AddRooms().setVisible(true);
            }
        } else if ("Add Drivers".equalsIgnoreCase(cmd)) {
            if (checkAdminPrivileges()) {
                new AddDriver().setVisible(true);
            }
        } else if ("Logout".equalsIgnoreCase(cmd)) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                setVisible(false);
                dispose();
                new Login().setVisible(true);
            }
        } else if ("Exit Application".equalsIgnoreCase(cmd)) {
            System.exit(0);
        }
    }

    private boolean checkAdminPrivileges() {
        if (!"Admin".equalsIgnoreCase(userRole)) {
            JOptionPane.showMessageDialog(this,
                "Access Denied: You are logged in as " + userRole + ".\nAdministrative privileges are required for this action.",
                "Permission Restricted", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard());
    }
}
