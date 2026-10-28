package hotel.reservation.system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database connection wrapper.
 * Integrates with DatabaseManager for automatic SQLite/MySQL connection handling,
 * prepared statement support, and safe resource management.
 */
public class Conn implements AutoCloseable {
    public Connection c;
    public Statement s;

    public Conn() {
        try {
            c = DatabaseManager.getInstance().getConnection();
            s = c.createStatement();
        } catch (Exception e) {
            System.err.println("Conn Error: " + e.getMessage());
        }
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        if (c == null || c.isClosed()) {
            c = DatabaseManager.getInstance().getConnection();
        }
        return c.prepareStatement(sql);
    }

    @Override
    public void close() {
        try {
            if (s != null && !s.isClosed()) s.close();
            if (c != null && !c.isClosed()) c.close();
        } catch (Exception ignored) {}
    }
}
