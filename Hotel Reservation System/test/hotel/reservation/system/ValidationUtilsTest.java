package hotel.reservation.system;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationUtilsTest {

    @Test
    void testValidPhoneNumbers() {
        assertTrue(ValidationUtils.isValidPhone("9876543210"));
        assertTrue(ValidationUtils.isValidPhone("8123456789"));
        assertTrue(ValidationUtils.isValidPhone("+91 9876543210"));
        assertTrue(ValidationUtils.isValidPhone("98765-43210"));

        assertFalse(ValidationUtils.isValidPhone(""));
        assertFalse(ValidationUtils.isValidPhone(null));
        assertFalse(ValidationUtils.isValidPhone("12345"));
        assertFalse(ValidationUtils.isValidPhone("987654321000")); // too long
        assertFalse(ValidationUtils.isValidPhone("abcdefghij"));
    }

    @Test
    void testValidEmails() {
        assertTrue(ValidationUtils.isValidEmail("guest@gmail.com"));
        assertTrue(ValidationUtils.isValidEmail("vikram.malhotra@tajhotels.in"));
        assertTrue(ValidationUtils.isValidEmail("sunita_rao@hotel.co.in"));

        assertFalse(ValidationUtils.isValidEmail(""));
        assertFalse(ValidationUtils.isValidEmail(null));
        assertFalse(ValidationUtils.isValidEmail("not-an-email"));
        assertFalse(ValidationUtils.isValidEmail("user@.com"));
    }

    @Test
    void testValidAadhar() {
        assertTrue(ValidationUtils.isValidAadhar("453210987654"));
        assertTrue(ValidationUtils.isValidAadhar("4532 1098 7654"));
        assertTrue(ValidationUtils.isValidAadhar("4532-1098-7654"));

        assertFalse(ValidationUtils.isValidAadhar("123456"));
        assertFalse(ValidationUtils.isValidAadhar("abcdefghijkl"));
        assertFalse(ValidationUtils.isValidAadhar(""));
        assertFalse(ValidationUtils.isValidAadhar(null));
    }

    @Test
    void testNumericValidation() {
        assertTrue(ValidationUtils.isPositiveInteger("25"));
        assertFalse(ValidationUtils.isPositiveInteger("0"));
        assertFalse(ValidationUtils.isPositiveInteger("-5"));
        assertFalse(ValidationUtils.isPositiveInteger("abc"));

        assertTrue(ValidationUtils.isNonNegativeDouble("0"));
        assertTrue(ValidationUtils.isNonNegativeDouble("2500.50"));
        assertFalse(ValidationUtils.isNonNegativeDouble("-100"));

        assertTrue(ValidationUtils.isPositiveDouble("1500.00"));
        assertFalse(ValidationUtils.isPositiveDouble("0"));
    }
}
