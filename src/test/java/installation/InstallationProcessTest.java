package installation;

import devices.configuration.device.Ownership;
import devices.configuration.installation.InstallationProcess;
import devices.configuration.installation.WorkOrder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InstallationProcessTest {

    private static final String orderId = "K56F";
    private static final String deviceId = "ALF-83266831";

    private WorkOrder workOrder;

    @BeforeEach
    void setUp() {
        Ownership ownership = Ownership.builder()
                .operator("Devicex.nl")
                .provider("public-devices")
                .build();

        workOrder = WorkOrder.builder()
                .orderId(ORDER_ID)
                .ownership(ownership)
                .build();
    }

    @Test
    void shouldCreateInstallationProcess() {
        InstallationProcess process = new InstallationProcess(workOrder);

        assertNotNull(process);
        assertEquals(orderId, process.getOrderId());
        assertFalse(process.isFinished());
    }

    /*@Test
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

    @Test
    void bootNotificationUpdated() {
        process.assignDevice("cool-device");
        process.receiveBootNotification(new BootNotification("cool-device"));
        process.confirmBootNotification();

        process.updateBootNotification(new BootNotification("cool-device"));

        assertFalse(process.isBootConfirmed());
    }

    @Test
    void shouldAllowNewDeviceAssignmentButRequireBootNotificationAgain() {
        process.assignDevice("cool-device");
        process.receiveBootNotification(new BootNotification("cool-device"));
        process.confirmBootNotification();

        process.assignDevice("device-456");

        assertFalse(process.isBootConfirmed());
    }*/
}
