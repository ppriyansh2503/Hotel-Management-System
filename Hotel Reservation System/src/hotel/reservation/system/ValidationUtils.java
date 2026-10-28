package hotel.reservation.system;

import java.util.regex.Pattern;

/**
 * Validation utilities for phone numbers, emails, Aadhar, names, and monetary values.
 */
public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^(\\+91[\\-\\s]?)?[6-9]\\d{9}$"
    );

    private static final Pattern AADHAR_PATTERN = Pattern.compile(
            "^\\d{4}[\\s\\-]?\\d{4}[\\s\\-]?\\d{4}$"
    );

    public static boolean isNotEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }

    public static boolean isValidPhone(String phone) {
        if (!isNotEmpty(phone)) return false;
        String cleaned = phone.replaceAll("[\\s\\-]", "");
        return PHONE_PATTERN.matcher(cleaned).matches();
    }

    public static boolean isValidEmail(String email) {
        if (!isNotEmpty(email)) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidAadhar(String aadhar) {
        if (!isNotEmpty(aadhar)) return false;
        String cleaned = aadhar.replaceAll("[\\s\\-]", "");
        return cleaned.matches("^\\d{12}$");
    }

    public static boolean isPositiveInteger(String value) {
        if (!isNotEmpty(value)) return false;
        try {
            int val = Integer.parseInt(value.trim());
            return val > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isNonNegativeDouble(String value) {
        if (!isNotEmpty(value)) return false;
        try {
            double val = Double.parseDouble(value.trim());
            return val >= 0.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isPositiveDouble(String value) {
        if (!isNotEmpty(value)) return false;
        try {
            double val = Double.parseDouble(value.trim());
            return val > 0.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
