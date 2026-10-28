package hotel.reservation.system;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import javax.swing.*;

/**
 * UI Utilities for robust image asset loading, modern theme application,
 * and high-DPI scaling on macOS Retina displays.
 */
public class UIUtils {

    private static boolean themeApplied = false;

    public static void applyModernTheme() {
        if (!themeApplied) {
            try {
                // Attempt to load FlatLaf for modern cross-platform styling
                UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
                themeApplied = true;
            } catch (Exception e) {
                try {
                    // Fall back to system native Look and Feel (e.g. Aqua on macOS)
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ignored) {}
            }
        }
    }

    /**
     * Infallible image icon loader.
     * Searches classloader resources, file system fallbacks, and produces
     * a clean placeholder graphic if the file is unavailable so UI never crashes.
     */
    public static ImageIcon getImageIcon(String path) {
        if (path == null) {
            return createPlaceholderIcon(100, 100);
        }

        // Clean path
        String cleanPath = path.startsWith("/") ? path.substring(1) : path;

        // 1. Try system classloader
        URL url = ClassLoader.getSystemResource(cleanPath);
        if (url != null) {
            return new ImageIcon(url);
        }

        // 2. Try contextual classloader
        url = UIUtils.class.getClassLoader().getResource(cleanPath);
        if (url != null) {
            return new ImageIcon(url);
        }

        // 3. Try resource relative to root
        url = UIUtils.class.getResource("/" + cleanPath);
        if (url != null) {
            return new ImageIcon(url);
        }

        // 4. Try direct file paths on disk
        String[] probePaths = {
            cleanPath,
            "Hotel Reservation System/src/" + cleanPath,
            "src/" + cleanPath,
            "Hotel Reservation System/build/classes/" + cleanPath
        };

        for (String p : probePaths) {
            File f = new File(p);
            if (f.exists() && f.isFile()) {
                return new ImageIcon(f.getAbsolutePath());
            }
        }

        // 5. Fallback placeholder
        return createPlaceholderIcon(120, 120);
    }

    public static Image getScaledImage(String path, int width, int height) {
        ImageIcon icon = getImageIcon(path);
        Image img = icon.getImage();
        if (img == null || width <= 0 || height <= 0) {
            return createPlaceholderIcon(width > 0 ? width : 100, height > 0 ? height : 100).getImage();
        }
        return img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    private static ImageIcon createPlaceholderIcon(int w, int h) {
        int width = Math.max(w, 20);
        int height = Math.max(h, 20);
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(new Color(240, 243, 246));
        g2.fillRect(0, 0, width, height);
        g2.setColor(new Color(180, 190, 200));
        g2.drawRect(0, 0, width - 1, height - 1);
        g2.setColor(new Color(100, 110, 120));
        g2.drawString("Hotel Asset", 10, height / 2);
        g2.dispose();
        return new ImageIcon(img);
    }
}
