import devices.configuration.Violations;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ViolationsTest {

    @Test
    void testNoneReturnsTrueWhenNoViolations() {
        Violations violations = new Violations(false, false, false, false, false);
        assertTrue(violations.none());
    }

    @Test
    void testNoneReturnsFalseWhenViolationsExist() {
        Violations violations = new Violations(true, false, false, false, false);
        assertFalse(violations.none());
    }
}
