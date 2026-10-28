package hotel.reservation.system;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Splash Screen for the Hotel Management System.
 * Features an animated welcome banner and clean navigation to the Login screen.
 */
public class HotelReservationSystem extends JFrame implements ActionListener {
    private JLabel textLabel;
    private JButton nextButton;
    private Timer blinkTimer;

    public HotelReservationSystem() {
        UIUtils.applyModernTheme();

        setTitle("The Grand Hotel & Resort - Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 600);
        setLocationRelativeTo(null); // Center on screen
        setLayout(null);

        // Background Image
        Image scaledBg = UIUtils.getScaledImage("icons/first.jpg", 1300, 600);
        JLabel bgLabel = new JLabel(new ImageIcon(scaledBg));
        bgLabel.setBounds(0, 0, 1300, 600);
        add(bgLabel);

        // Animated Welcome Text Banner
        textLabel = new JLabel("HOTEL MANAGEMENT SYSTEM");
        textLabel.setBounds(40, 470, 950, 70);
        textLabel.setFont(new Font("Serif", Font.BOLD, 46));
        textLabel.setForeground(new Color(220, 20, 60)); // Crimson Red
        bgLabel.add(textLabel);

        // Next Button
        nextButton = new JButton("Next →");
        nextButton.setFont(new Font("Tahoma", Font.BOLD, 16));
        nextButton.setBackground(Color.WHITE);
        nextButton.setForeground(Color.BLACK);
        nextButton.setBounds(1080, 480, 150, 45);
        nextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextButton.addActionListener(this);
        bgLabel.add(nextButton);

        // Non-blocking Swing Timer for blinking text animation
        blinkTimer = new Timer(600, new ActionListener() {
            private boolean visible = true;
            @Override
            public void actionPerformed(ActionEvent e) {
                visible = !visible;
                textLabel.setVisible(visible);
            }
        });
        blinkTimer.start();

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (blinkTimer != null && blinkTimer.isRunning()) {
            blinkTimer.stop();
        }
        setVisible(false);
        dispose();
        new Login().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelReservationSystem());
    }
}
