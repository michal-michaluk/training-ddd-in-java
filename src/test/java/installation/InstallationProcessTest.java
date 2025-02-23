package installation;

import devices.configuration.device.Location;
import devices.configuration.installation.BootNotification;
import devices.configuration.installation.InstallationProcess;
import devices.configuration.installation.WorkOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static installation.InstallationProcessFixture.*;
import static org.junit.jupiter.api.Assertions.*;

public class InstallationProcessTest {

    private final String orderId = "K56F";
    private final String deviceId = "ALF-83266831";
    private final String installerId = "INSTALLER-456";

    private BootNotification defaultBoot;
    private Location someLocation;
    private WorkOrder workOrder;
    private InstallationProcess process;

    @BeforeEach
    void setUp() {
        defaultBoot = defaultNotification();
        someLocation = someLocation();
        workOrder = defaultOrder();

        process = new InstallationProcess(workOrder);
    }

    @Test
    void shouldCreateInstallationProcess() {
        assertNotNull(process);
        assertEquals(orderId, process.getOrderId());
        assertFalse(process.isFinished());
    }

    @Test
    void whenAssignInstallerAfterFinish_thenThrowException() {
        process = processInProgress();
        process.finish();

        assertThrows(IllegalStateException.class, () ->
                process.assignInstaller(installerId)
        );
    }

    @Test
    void whenAssignDeviceAfterFinish_thenThrowException() {
        process = processInProgress();
        process.finish();

        assertThrows(IllegalStateException.class, () ->
                process.assignDevice(deviceId)
        );
    }

    @Test
    void ignoreBootNotification_ifProcessFinished() {
        process = processInProgress();
        process.finish();

        BootNotification newBoot = defaultBoot.toBuilder().firmware("v2.1").build();
        process.receiveBootNotification(newBoot);

        assertNull(process.getPendingBootNotification());
        assertEquals(defaultBoot, process.getConfirmedBootNotification());
    }

    @Test
    void whenReceivingNewBootNotification_thenStoreAsPending() {
        process.assignDevice(deviceId);

        process.receiveBootNotification(defaultBoot);

        assertNotNull(process.getPendingBootNotification());
        assertEquals(defaultBoot, process.getPendingBootNotification());
    }

    @Test
    void whenDeviceNotMatchBootNotification_thenThrowException() {
        // no device assigned
        assertThrows(IllegalStateException.class, () ->
                process.receiveBootNotification(defaultBoot)
        );

        // assign not matching device
        process.assignDevice("wrong-device");

        assertThrows(IllegalStateException.class, () ->
                process.receiveBootNotification(defaultBoot)
        );
    }

    @Test
    void whenConfirmBootWithoutReceiving_thenThrowException() {
        process.assignDevice(deviceId);

        assertThrows(IllegalStateException.class, () ->
                process.confirmBoot()
        );
    }

    @Test
    void whenConfirmBoot_thenUpdateConfirmedAndClearPending() {
        process.assignDevice(deviceId);
        process.receiveBootNotification(defaultBoot);
        process.confirmBoot();

        assertNull(process.getPendingBootNotification());
        assertEquals(defaultBoot, process.getConfirmedBootNotification());
    }

    @Test
    void whenUpdatingAnyBootNotificationField_thenRequireNewConfirmation() {
        process.assignDevice(deviceId);
        process.receiveBootNotification(defaultBoot);
        process.confirmBoot();

        BootNotification updatedBoot = defaultBoot.toBuilder()
                .firmware("v2.1")
                .build();
        process.receiveBootNotification(updatedBoot);

        assertNotNull(process.getPendingBootNotification());
        assertEquals(updatedBoot, process.getPendingBootNotification());
        assertNotEquals(defaultBoot, process.getPendingBootNotification());
        assertEquals(defaultBoot, process.getConfirmedBootNotification());
    }

    @Test
    void whenResendingSameBootNotificationData_thenNoPending() {
        process.assignDevice(deviceId);
        process.receiveBootNotification(defaultBoot);
        process.confirmBoot();

        BootNotification sameBoot = defaultBoot.toBuilder().build();
        process.receiveBootNotification(sameBoot);

        assertNull(process.getPendingBootNotification());
        assertEquals(defaultBoot, process.getConfirmedBootNotification());
    }

    @Test
    void whenAssigningNewDeviceAfterBoot_thenRequireNewNotification() {
        process.assignDevice(deviceId);
        process.receiveBootNotification(defaultBoot);
        process.confirmBoot();

        String newDeviceId = "new-device";
        process.assignDevice(newDeviceId);

        assertEquals(newDeviceId, process.getDeviceId());

        assertNull(process.getPendingBootNotification());
        assertNull(process.getConfirmedBootNotification());
    }

    @Test
    void whenSettingLocationWithoutConfirmedBoot_thenThrowException() {
        process.assignDevice(deviceId);

        assertThrows(IllegalStateException.class, () ->
                process.setLocation(someLocation)
        );
    }

    @Test
    void whenAssignNewDevice_thenResetBootAndLocation() {
        process.assignDevice(deviceId);
        process.receiveBootNotification(defaultBoot);
        process.confirmBoot();
        process.setLocation(someLocation);

        String newDeviceId = "new-device";
        process.assignDevice(newDeviceId);

        assertNull(process.getConfirmedBootNotification());
        assertNull(process.getPendingBootNotification());
        assertNull(process.getLocation());
        assertEquals(newDeviceId, process.getDeviceId());
    }

    @Test
    void whenFinishingWithoutCompleteTerms_thenThrowException() {
        // missing installer, device, boot confirmation, location
        assertThrows(IllegalStateException.class, () ->
                process.finish()
        );
    }

    @Test
    void whenFinishingWithAllCompletedTerms_thenMarkAsFinished() {
        process = processInProgress();

        process.finish();

        assertTrue(process.isFinished());
    }
}

