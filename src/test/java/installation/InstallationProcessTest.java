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
        // given - already defined process
        // then - verify that process is created with correct order id
        assertNotNull(process);
        assertEquals(orderId, process.getOrderId());
        assertFalse(process.isFinished());
    }

    @Test
    void whenAssignInstallerAfterFinish_thenThrowException() {
        // given - a complete process
        process = processInProgress();

        // when - process finishes
        process.finish();

        // then - any commands after finish of process should be not allowed
        assertThrows(IllegalStateException.class, () -> process.assignInstaller(installerId));
    }

    @Test
    void whenAssignDeviceAfterFinish_thenThrowException() {
        // given - a complete process
        process = processInProgress();

        // when - process finishes
        process.finish();

        // then - any commands after finish of process should be not allowed
        assertThrows(IllegalStateException.class, () -> process.assignDevice(deviceId));
    }

    @Test
    void ignoreBootNotification_ifProcessFinished() {
        // given - a complete process
        process = processInProgress();

        // when - process finishes
        process.finish();

        // process should ignore new notification after finishing
        BootNotification newBoot = defaultBoot.toBuilder().firmware("v2.1").build();
        process.receiveBootNotification(newBoot);

        // then - verify no pending notification is stored
        assertNull(process.getPendingBootNotification());
        assertEquals(defaultBoot, process.getConfirmedBootNotification());
    }

    @Test
    void whenReceivingNewBootNotification_thenStoreAsPending() {
        // given
        // when - receiving notification should be stored as pending before confirming
        process.assignDevice(deviceId);
        process.receiveBootNotification(defaultBoot);

        // then verify pending notification was stored
        assertNotNull(process.getPendingBootNotification());
        assertEquals(defaultBoot, process.getPendingBootNotification());
    }

    @Test
    void whenDeviceNotMatchBootNotification_thenThrowException() {
        // when no device assigned
        // then exception should be thrown if we send notification
        assertThrows(IllegalStateException.class, () -> process.receiveBootNotification(defaultBoot));

        // when devices not matching
        // then exception should be thrown if we send notification
        process.assignDevice("wrong-device");

        assertThrows(IllegalStateException.class, () -> process.receiveBootNotification(defaultBoot));
    }

    @Test
    void whenConfirmBootWithoutReceiving_thenThrowException() {
        // given
        process.assignDevice(deviceId);

        // then exception should be thrown - cannot confirm boot without receiving notification first
        assertThrows(IllegalStateException.class, () -> process.confirmBoot());
    }

    @Test
    void whenConfirmBoot_thenUpdateConfirmedAndClearPending() {
        // given
        process.assignDevice(deviceId);
        process.receiveBootNotification(defaultBoot);
        process.confirmBoot();

        // then verify that after confirming, pending is set to null and confirmed is set correctly
        assertNull(process.getPendingBootNotification());
        assertEquals(defaultBoot, process.getConfirmedBootNotification());
    }

    @Test
    void whenUpdatingAnyBootNotificationField_thenRequireNewConfirmation() {
        // given
        process.assignDevice(deviceId);
        process.receiveBootNotification(defaultBoot);
        process.confirmBoot();

        // when receiving updated boot notification
        BootNotification updatedBoot = defaultBoot.toBuilder().firmware("v2.1").build();
        process.receiveBootNotification(updatedBoot);

        // verify that the new notification is stored as pending
        // requires confirming again to store the new notification as confirmed
        assertNotNull(process.getPendingBootNotification());
        assertEquals(updatedBoot, process.getPendingBootNotification());
        assertNotEquals(defaultBoot, process.getPendingBootNotification());
        assertEquals(defaultBoot, process.getConfirmedBootNotification());
    }

    @Test
    void whenResendingSameBootNotificationData_thenNoPending() {
        // given
        process.assignDevice(deviceId);
        process.receiveBootNotification(defaultBoot);
        process.confirmBoot();

        // when resending the same boot notification
        BootNotification sameBoot = defaultBoot.toBuilder().build();
        process.receiveBootNotification(sameBoot);

        // then verify that it will be ignored
        assertNull(process.getPendingBootNotification());
        assertEquals(defaultBoot, process.getConfirmedBootNotification());
    }

    @Test
    void whenAssigningNewDeviceAfterBoot_thenRequireNewNotification() {
        // given
        process.assignDevice(deviceId);
        process.receiveBootNotification(defaultBoot);
        process.confirmBoot();

        // when reassigning new device after confirming
        String newDeviceId = "new-device";
        process.assignDevice(newDeviceId);

        // then verify new device is assigned correctly
        assertEquals(newDeviceId, process.getDeviceId());

        // then verify boot notification & boot notification confirming are needed
        assertNull(process.getPendingBootNotification());
        assertNull(process.getConfirmedBootNotification());
    }

    @Test
    void whenSettingLocationWithoutConfirmedBoot_thenThrowException() {
        // given
        process.assignDevice(deviceId);

        // when receiving boot notification
        process.receiveBootNotification(defaultBoot);

        // then setting location if boot notification is not confirmed
        // should throw exception
        assertThrows(IllegalStateException.class, () -> process.setLocation(someLocation));
    }

    @Test
    void whenAssignNewDevice_thenResetBootAndLocation() {
        // given
        process.assignDevice(deviceId);
        process.receiveBootNotification(defaultBoot);
        process.confirmBoot();
        process.setLocation(someLocation);

        // when reassigning new device
        String newDeviceId = "new-device";
        process.assignDevice(newDeviceId);

        // then boot notification, confirmation & location are needed again
        assertNull(process.getConfirmedBootNotification());
        assertNull(process.getPendingBootNotification());
        assertNull(process.getLocation());
        assertEquals(newDeviceId, process.getDeviceId());
    }

    @Test
    void whenFinishingWithoutCompleteTerms_thenThrowException() {
        // cannot finish process when missing:
        // installer, device, boot confirmation, location
        assertThrows(IllegalStateException.class, () -> process.finish());
    }

    @Test
    void whenFinishingWithAllCompletedTerms_thenMarkAsFinished() {
        // given a completed process
        process = processInProgress();

        // when it finishes
        process.finish();

        // then verify it has finished
        assertTrue(process.isFinished());
    }
}

