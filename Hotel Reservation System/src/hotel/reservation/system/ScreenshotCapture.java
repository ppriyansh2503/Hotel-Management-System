package hotel.reservation.system;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Screenshot generator for documentation.
 * Instantiates the actual Swing UI screens and renders genuine screenshots.
 */
public class ScreenshotCapture {

    public static void main(String[] args) {
        UIUtils.applyModernTheme();
        File screenshotsDir = new File("screenshots");
        if (!screenshotsDir.exists()) {
            screenshotsDir.mkdirs();
        }

        System.out.println("Capturing genuine UI screenshots...");

        try {
            // 1. Splash Screen
            HotelReservationSystem splash = new HotelReservationSystem();
            captureFrame(splash, new File(screenshotsDir, "01_splash_screen.png"));
            splash.dispose();

            // 2. Login Screen
            Login login = new Login();
            captureFrame(login, new File(screenshotsDir, "02_login_screen.png"));
            login.dispose();

            // 3. Dashboard Screen
            Dashboard dashboard = new Dashboard("admin", "Admin", "Vikram Malhotra");
            captureFrame(dashboard, new File(screenshotsDir, "03_dashboard_screen.png"));
            dashboard.dispose();

            // 4. Reception Screen
            Reception reception = new Reception("sunita", "Receptionist");
            captureFrame(reception, new File(screenshotsDir, "04_reception_screen.png"));
            reception.dispose();

            // 5. Room Inventory Screen
            Room room = new Room();
            captureFrame(room, new File(screenshotsDir, "05_room_inventory.png"));
            room.dispose();

            // 6. Add Customer Form
            AddCustomer addCustomer = new AddCustomer();
            captureFrame(addCustomer, new File(screenshotsDir, "06_customer_checkin.png"));
            addCustomer.dispose();

            // 7. Checkout & Billing Screen
            Checkout checkout = new Checkout();
            captureFrame(checkout, new File(screenshotsDir, "07_checkout_billing.png"));
            checkout.dispose();

            // 8. Billing History Screen
            BillingHistory billingHistory = new BillingHistory();
            captureFrame(billingHistory, new File(screenshotsDir, "08_billing_history.png"));
            billingHistory.dispose();

            System.out.println("All 8 screenshots captured successfully in " + screenshotsDir.getAbsolutePath());
            System.out.flush();
            System.exit(0);

        } catch (Exception e) {
            System.err.println("Screenshot capture failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void captureFrame(JFrame frame, File outputFile) throws Exception {
        frame.doLayout();
        frame.validate();

        int width = Math.max(frame.getWidth(), 600);
        int height = Math.max(frame.getHeight(), 400);

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();

        // Enable Anti-Aliasing for crisp screenshot output
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        frame.paint(g2);
        g2.dispose();

        ImageIO.write(img, "png", outputFile);
        System.out.println("Saved: " + outputFile.getName() + " (" + width + "x" + height + ")");
    }
}
