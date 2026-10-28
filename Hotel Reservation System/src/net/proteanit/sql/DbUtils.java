package net.proteanit.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * Built-in replacement for rs2xml DbUtils.
 * Converts JDBC ResultSet to a Swing TableModel cleanly and efficiently
 * without external unmaintained JAR dependencies.
 */
public class DbUtils {

    public static TableModel resultSetToTableModel(ResultSet rs) {
        if (rs == null) {
            return new DefaultTableModel();
        }
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int numberOfColumns = metaData.getColumnCount();
            Vector<String> columnNames = new Vector<>();

            // Extract column labels/headers
            for (int column = 1; column <= numberOfColumns; column++) {
                String label = metaData.getColumnLabel(column);
                if (label == null || label.trim().isEmpty()) {
                    label = metaData.getColumnName(column);
                }
                columnNames.add(formatColumnHeader(label));
            }

            // Extract rows
            Vector<Vector<Object>> rows = new Vector<>();
            while (rs.next()) {
                Vector<Object> newRow = new Vector<>();
                for (int i = 1; i <= numberOfColumns; i++) {
                    Object val = rs.getObject(i);
                    newRow.add(val != null ? val : "-");
                }
                rows.add(newRow);
            }

            return new DefaultTableModel(rows, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Prevent accidental editing in view tables
                }
            };
        } catch (Exception e) {
            System.err.println("DbUtils Error: " + e.getMessage());
            return new DefaultTableModel();
        }
    }

    private static String formatColumnHeader(String raw) {
        if (raw == null) return "";
        // Humanize common column names (e.g. roomNo -> Room No)
        switch (raw.toLowerCase()) {
            case "roomno": return "Room No";
            case "available": return "Availability";
            case "status": return "Cleaning Status";
            case "price": return "Price (₹)";
            case "type": return "Bed Type";
            case "checkintime": return "Check-in Time";
            case "deposit": return "Deposit (₹)";
            case "aadhar": return "Aadhar No";
            default:
                if (raw.length() > 1) {
                    return Character.toUpperCase(raw.charAt(0)) + raw.substring(1);
                }
                return raw.toUpperCase();
        }
    }
}
