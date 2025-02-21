package installation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InstallationProcessTest {

    private InstallationProcess process;
    private String orderId = "order-001";

    @BeforeEach
    void setUp() {
        process = new InstallationProcess(orderId);
    }

    @Test
    void createNewInstallationProcessTest() {
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> new InstallationProcess(orderId));
        assertEquals("Work order already exists!", exception.getMessage());
    }
}
