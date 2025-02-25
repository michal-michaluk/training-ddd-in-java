package devices.configuration.installation;

import devices.configuration.installation.DomainEvent.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static devices.configuration.installation.InstallationProcessFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class InstallationProcessTest {

    private final String orderId = "K56F";
    private final String deviceId = "ALF-83266831";
    private final String installerId = "INSTALLER-456";

    private BootNotification someBootNotification;
    private WorkOrder workOrder;

    @BeforeEach
    void setUp() {
        someBootNotification = ultraChargeBootNotification(deviceId);
        workOrder = WorkOrder.builder()
                .orderId(orderId)
                .ownership(someOwnership())
                .build();
    }

    @Test
    void shouldCreateInstallationProcess() {
        // given - a new process created
        var process = InstallationProcess.create(workOrder);

        // then - verify that process is created with correct order id
        var installationState  = process.getCurrentState();

        assertNotNull(installationState );
        assertEquals(orderId, installationState .orderId());
        assertFalse(installationState .isFinished());

        assertThat(process.events)
                .contains(new ProcessCreated(workOrder));
    }

    @Test
    void assignDevice() {
        // given - a new process created
        var process = InstallationProcess.create(workOrder);

        // when assigning device
        process.assignDevice(deviceId);

        // then verify it was stored correctly
        var installationState  = process.getCurrentState();
        
        assertEquals(deviceId, installationState .deviceId());

        assertThat(process.events)
                .contains(
                        new ProcessCreated(workOrder),
                        new DeviceAssigned(orderId, deviceId));
    }

    @Test
    void assignInstaller() {
        // given - a new process created
        var process = InstallationProcess.create(workOrder);

        // when assigning installer
        process.assignInstaller(installerId);

        // then verify it was stored correctly
        var installationState  = process.getCurrentState();

        assertEquals(installerId, installationState .installerId());

        assertThat(process.events)
                .contains(
                        new ProcessCreated(workOrder),
                        new InstallerAssigned(orderId, installerId));
    }

    @Test
    void whenAssignInstallerAfterFinish_thenThrowException() {
        // given - a completed process
        var process = given(workOrder, p -> {
            p.assignDevice(deviceId);
            p.assignInstaller(installerId);
            p.receiveBootNotification(someBootNotification);
            p.confirmBoot();
            p.setLocation(someLocation());
        });

        // when - process finishes
        process.finish();

        // then - any commands after finish of process should be not allowed
        assertThrows(IllegalStateException.class, () -> process.assignInstaller(installerId));

        assertThat(process.events)
                .contains(new InstallationFinished(orderId));
    }

    @Test
    void whenAssignDeviceAfterFinish_thenThrowException() {
        // given - a completed process
        var process = given(workOrder, p -> {
            p.assignDevice(deviceId);
            p.assignInstaller(installerId);
            p.receiveBootNotification(someBootNotification);
            p.confirmBoot();
            p.setLocation(someLocation());
        });

        // when - process finishes
        process.finish();

        // then - any commands after finish of process should be not allowed
        assertThrows(IllegalStateException.class, () -> process.assignDevice(deviceId));

        assertThat(process.events)
                .contains(new InstallationFinished(orderId));
    }

    @Test
    void ignoreBootNotification_ifProcessFinished() {
        // given - a completed process
        var process = given(workOrder, p -> {
            p.assignDevice(deviceId);
            p.assignInstaller(installerId);
            p.receiveBootNotification(someBootNotification);
            p.confirmBoot();
            p.setLocation(someLocation());
        });

        // when - process finishes
        process.finish();

        // process should ignore new notification after finishing
        BootNotification newBoot = someBootNotification.toBuilder().firmware("v2.1").build();
        process.receiveBootNotification(newBoot);

        // then - verify no pending notification is stored
        var installationState  = process.getCurrentState();

        assertNull(installationState .pendingBootNotification());
        assertEquals(someBootNotification, installationState .confirmedBootNotification());

        assertThat(process.events)
                .contains(new InstallationFinished(orderId));
        assertThat(process.events)
                .doesNotContain(
                        new BootNotificationReceived(orderId, newBoot),
                        new BootNotificationConfirmed(orderId, newBoot));
    }

    @Test
    void whenReceivingNewBootNotification_thenStoreAsPending() {
        // given - a new process
        var process = InstallationProcess.create(workOrder);

        // when - receiving notification should be stored as pending before confirming
        process.assignDevice(deviceId);
        process.receiveBootNotification(someBootNotification);

        // then verify pending notification was stored
        var installationState  = process.getCurrentState();

        assertNotNull(installationState .pendingBootNotification());
        assertEquals(someBootNotification, installationState .pendingBootNotification());

        assertThat(process.events)
                .contains(
                        new ProcessCreated(workOrder),
                        new DeviceAssigned(orderId, deviceId),
                        new BootNotificationReceived(orderId, someBootNotification));
    }

    @Test
    void whenNoDeviceAssignedAndReceivingNotification_thenThrowException() {
        // given - a new process
        var process = InstallationProcess.create(workOrder);

        // when no device assigned
        // then exception should be thrown if we send notification
        assertThrows(IllegalStateException.class, () -> process.receiveBootNotification(someBootNotification));

        assertThat(process.events)
                .contains(new ProcessCreated(workOrder));
        assertThat(process.events)
                .doesNotContain(new BootNotificationReceived(orderId, someBootNotification));
    }

    @Test
    void whenDeviceNotMatchBootNotification_thenThrowException() {
        // given - a new process
        var process = InstallationProcess.create(workOrder);

        // when devices not matching
        String newDeviceId = "wrong-device";
        process.assignDevice(newDeviceId);

        // then exception should be thrown if we send notification
        assertThrows(IllegalStateException.class, () -> process.receiveBootNotification(someBootNotification));

        assertThat(process.events)
                .contains(
                        new ProcessCreated(workOrder),
                        new DeviceAssigned(orderId, newDeviceId));
    }

    @Test
    void whenConfirmBootWithoutReceiving_thenThrowException() {
        // given
        var process = InstallationProcess.create(workOrder);

        // when assigning a device
        process.assignDevice(deviceId);

        // then exception should be thrown - cannot confirm boot without receiving notification first
        assertThrows(IllegalStateException.class, process::confirmBoot);

        assertThat(process.events)
                .contains(
                        new ProcessCreated(workOrder),
                        new DeviceAssigned(orderId, deviceId));
    }

    @Test
    void whenConfirmBoot_thenUpdateConfirmedAndClearPending() {
        // given a fresh process
        var process = InstallationProcess.create(workOrder);

        // when confirming correctly boot notification
        process.assignDevice(deviceId);
        process.receiveBootNotification(someBootNotification);
        process.confirmBoot();

        // then verify that after confirming, pending is set to null and confirmed is set correctly
        var installationState  = process.getCurrentState();

        assertNull(installationState .pendingBootNotification());
        assertEquals(someBootNotification, installationState .confirmedBootNotification());

        assertThat(process.events)
                .contains(
                        new ProcessCreated(workOrder),
                        new DeviceAssigned(orderId, deviceId),
                        new BootNotificationReceived(orderId, someBootNotification),
                        new BootNotificationConfirmed(orderId, someBootNotification));
    }

    @Test
    void whenUpdatingAnyBootNotificationField_thenRequireNewConfirmation() {
        // given - process with assigned and confirmed boot
        var process = given(workOrder, p -> {
            p.assignDevice(deviceId);
            p.receiveBootNotification(someBootNotification);
            p.confirmBoot();
        });

        // when receiving updated boot notification
        BootNotification updatedBoot = someBootNotification.toBuilder().firmware("v2.1").build();
        process.receiveBootNotification(updatedBoot);

        // verify that the new notification is stored as pending
        // requires confirming again to store the new notification as confirmed
        var installationState  = process.getCurrentState();

        assertNotNull(installationState .pendingBootNotification());
        assertEquals(updatedBoot, installationState .pendingBootNotification());
        assertNotEquals(someBootNotification, installationState .pendingBootNotification());
        assertEquals(someBootNotification, installationState .confirmedBootNotification());

        assertThat(process.events)
                .contains(new BootNotificationReceived(orderId, updatedBoot));
    }

    @Test
    void whenResendingSameBootNotificationData_thenNoPending() {
        // given - process with assigned and confirmed boot
        var process = given(workOrder, p -> {
            p.assignDevice(deviceId);
            p.receiveBootNotification(someBootNotification);
            p.confirmBoot();
        });

        // when resending the same boot notification
        BootNotification sameBoot = someBootNotification.toBuilder().build();
        process.receiveBootNotification(sameBoot);

        // then verify that it will be ignored
        var installationState  = process.getCurrentState();

        assertNull(installationState .pendingBootNotification());
        assertEquals(someBootNotification, installationState .confirmedBootNotification());

        // event should be ignored
        assertThat(process.events)
                .isEmpty();
    }

    @Test
    void whenAssigningNewDeviceAfterBoot_thenRequireNewNotification() {
        // given - process with assigned and confirmed boot
        var process = given(workOrder, p -> {
            p.assignDevice(deviceId);
            p.receiveBootNotification(someBootNotification);
            p.confirmBoot();
        });

        // when reassigning new device after confirming
        String newDeviceId = "new-device";
        process.assignDevice(newDeviceId);

        // then verify new device is assigned correctly
        var installationState  = process.getCurrentState();

        assertEquals(newDeviceId, installationState .deviceId());

        // then verify boot notification & boot notification confirming are needed
        assertNull(installationState .pendingBootNotification());
        assertNull(installationState .confirmedBootNotification());

        assertThat(process.events)
                .contains(
                        new DeviceAssigned(orderId, newDeviceId),
                        new BootNotificationReceived(orderId, null),
                        new BootNotificationConfirmed(orderId, null));
    }

    @Test
    void whenSettingLocationWithoutConfirmedBoot_thenThrowException() {
        // given - process with assigned device
        var process = given(workOrder, p -> p.assignDevice(deviceId));

        // when receiving boot notification
        process.receiveBootNotification(someBootNotification);

        // then setting location if boot notification is not confirmed
        // should throw exception
        assertThrows(IllegalStateException.class, () -> process.setLocation(someLocation()));

        assertThat(process.events)
                .contains(new BootNotificationReceived(orderId, someBootNotification));
    }

    @Test
    void whenAssignNewDevice_thenResetBootAndLocation() {
        // given - confirmed boot and set location
        var process = given(workOrder, p -> {
            p.assignDevice(deviceId);
            p.receiveBootNotification(someBootNotification);
            p.confirmBoot();
            p.setLocation(someLocation());
        });

        // when reassigning new device
        String newDeviceId = "new-device";
        process.assignDevice(newDeviceId);

        // then boot notification, confirmation & location are needed again
        var installationState  = process.getCurrentState();

        assertNull(installationState .confirmedBootNotification());
        assertNull(installationState .pendingBootNotification());
        assertNull(installationState .location());
        assertEquals(newDeviceId, installationState .deviceId());

        assertThat(process.events)
                .contains(
                        new DeviceAssigned(orderId, newDeviceId),
                        new BootNotificationReceived(orderId, null),
                        new BootNotificationConfirmed(orderId, null),
                        new LocationChanged(orderId, null));
    }

    @Test
    void whenFinishingWithoutCompleteTerms_thenThrowException() {
        // given - new process
        var process = InstallationProcess.create(workOrder);

        // cannot finish process when missing:
        // installer, device, boot confirmation, location
        assertThrows(IllegalStateException.class, process::finish);

        assertThat(process.events)
                .contains(new ProcessCreated(workOrder));
    }

    @Test
    void whenFinishingWithAllCompletedTerms_thenMarkAsFinished() {
        // given a completed process
        var process = given(workOrder, p -> {
            p.assignDevice(deviceId);
            p.assignInstaller(installerId);
            p.receiveBootNotification(someBootNotification);
            p.confirmBoot();
            p.setLocation(someLocation());
        });

        // when it finishes
        process.finish();

        // then verify it has finished
        var installationState  = process.getCurrentState();

        assertTrue(installationState .isFinished());

        assertThat(process.events)
                .contains(new InstallationFinished(orderId));
    }

    @Test
    void noEventsEmittedWhenValuesAreTheSame() {
        // given - an almost completed process
        var process = given(workOrder, p -> {
            p.assignDevice(deviceId);
            p.assignInstaller(installerId);
            p.receiveBootNotification(someBootNotification);
        });

        // when we reassign the same values
        process.assignDevice(deviceId);
        process.assignInstaller(installerId);
        process.receiveBootNotification(someBootNotification);

        // then verify that no event is emitted
        assertThat(process.events)
                .isEmpty();
    }
}

