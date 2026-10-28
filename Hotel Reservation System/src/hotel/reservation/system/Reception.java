package hotel.reservation.system;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Central Reception Navigation Hub.
 * Provides unified access to all guest, room, staff, billing, and transport operations.
 */
public class Reception extends JFrame implements ActionListener {
    private String username = "admin";
    private String userRole = "Admin";

    private JButton b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13;

    public Reception() {
        this("admin", "Admin");
    }

    public Reception(String username, String role) {
        if (username != null) this.username = username;
        if (role != null) this.userRole = role;

        UIUtils.applyModernTheme();

        setTitle("The Grand Hotel - Front Desk Reception");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(920, 620);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Header
        JLabel l2 = new JLabel("FRONT DESK RECEPTION");
        l2.setFont(new Font("Tahoma", Font.BOLD, 22));
        l2.setForeground(new Color(24, 43, 73));
        l2.setBounds(30, 15, 350, 30);
        add(l2);

        // Side Image
        Image img = UIUtils.getScaledImage("icons/fourth.jpg", 500, 480);
        JLabel l1 = new JLabel(new ImageIcon(img));
        l1.setBounds(390, 45, 490, 480);
        add(l1);

        // Navigation Buttons - Left Panel
        int btnX = 30;
        int btnWidth = 230;
        int btnHeight = 32;
        int startY = 60;
        int gap = 38;

        b1 = createNavButton("1. New Customer Form", btnX, startY, btnWidth, btnHeight);
        b2 = createNavButton("2. Room Inventory", btnX, startY + gap, btnWidth, btnHeight);
        b3 = createNavButton("3. Department Budgets", btnX, startY + gap * 2, btnWidth, btnHeight);
        b4 = createNavButton("4. All Employees", btnX, startY + gap * 3, btnWidth, btnHeight);
        b5 = createNavButton("5. Active Customer Info", btnX, startY + gap * 4, btnWidth, btnHeight);
        b6 = createNavButton("6. Manager Directory", btnX, startY + gap * 5, btnWidth, btnHeight);
        b7 = createNavButton("7. Guest Checkout & Bill", btnX, startY + gap * 6, btnWidth, btnHeight);
        b8 = createNavButton("8. Update Check-in Status", btnX, startY + gap * 7, btnWidth, btnHeight);
        b9 = createNavButton("9. Update Room Status", btnX, startY + gap * 8, btnWidth, btnHeight);
        b10 = createNavButton("10. Pickup Services", btnX, startY + gap * 9, btnWidth, btnHeight);
        b11 = createNavButton("11. Search Room", btnX, startY + gap * 10, btnWidth, btnHeight);
        b12 = createNavButton("12. Billing History", btnX, startY + gap * 11, btnWidth, btnHeight);

        // Close/Back to Dashboard Button
        b13 = new JButton("← Back to Dashboard");
        b13.setBounds(btnX, startY + gap * 12 + 5, btnWidth, 34);
        b13.setFont(new Font("Tahoma", Font.BOLD, 13));
        b13.setBackground(new Color(180, 40, 40));
        b13.setForeground(Color.WHITE);
        b13.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b13.addActionListener(this);
        add(b13);

        setVisible(true);
    }

    private JButton createNavButton(String text, int x, int y, int w, int h) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, w, h);
        btn.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btn.setBackground(new Color(24, 43, 73));
        btn.setForeground(Color.WHITE);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(this);
        add(btn);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object src = ae.getSource();

        if (src == b1) {
            new AddCustomer().setVisible(true);
        } else if (src == b2) {
            new Room().setVisible(true);
        } else if (src == b3) {
            new Department().setVisible(true);
        } else if (src == b4) {
            new EmployeeInfo().setVisible(true);
        } else if (src == b5) {
            new CustomerInfo().setVisible(true);
        } else if (src == b6) {
            new ManagerInfo().setVisible(true);
        } else if (src == b7) {
            new Checkout().setVisible(true);
        } else if (src == b8) {
            new UpdateCheck().setVisible(true);
        } else if (src == b9) {
            new UpdateRoom().setVisible(true);
        } else if (src == b10) {
            new PickUp().setVisible(true);
        } else if (src == b11) {
            new SearchRoom().setVisible(true);
        } else if (src == b12) {
            new BillingHistory().setVisible(true);
        } else if (src == b13) {
            setVisible(false);
            dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Reception());
    }
}
