package installation;

import devices.configuration.installation.InstallationProcess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InstallationProcessTest {

    private InstallationProcess process;
    private String orderId = "order-001";

    @BeforeEach
    void setUp() {
        InstallationProcess.existingOrders.clear();
        process = new InstallationProcess(orderId);
    }

    @Test
    void createSecondInstallationProcessTest() {
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> new InstallationProcess(orderId));

        assertEquals("Work order already exists!", exception.getMessage());
    }

    @Test
    void processFinishedAndAssignedNewDevice() {
        process.finishProcess();

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> process.assignDevice("device-x"));

        assertEquals("Device cannot be assigned. Process Finished!", exception.getMessage());
    }

    @Test
    void processFinishedAndAssignedInstaller() {
        process.finishProcess();

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> process.assignInstaller("installer-007"));

        assertEquals("Installer cannot be assigned. Process Finished!", exception.getMessage());
    }
}
