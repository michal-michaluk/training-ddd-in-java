package installation;

import devices.configuration.installation.BootNotification;
import devices.configuration.installation.InstallationProcess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InstallationProcessTest {

    private InstallationProcess process;
    private final String orderId = "order-001";

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

    @Test
    void bootNotificationNotMatchingTest() {
        process.assignDevice("correct-device");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> process.receiveBootNotification(new BootNotification("wrong-device")));

        assertEquals("Boot Notification device does not match assigned device!", exception.getMessage());
    }

    @Test
    void shouldIgnoreExtraBootNotificationsAfterCompletion() {
        process.assignDevice("cool-device");
        process.receiveBootNotification(new BootNotification("cool-device"));
        process.finishProcess();

        assertDoesNotThrow(() -> process.receiveBootNotification(new BootNotification("cool-device")));
    }
}
