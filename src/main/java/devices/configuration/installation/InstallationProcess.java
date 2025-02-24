package devices.configuration.installation;

import devices.configuration.device.Location;
import devices.configuration.device.Ownership;
import devices.configuration.installation.DomainEvent.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class InstallationProcess {

    private final String orderId;
    private final Ownership ownership;
    final List<DomainEvent> events;
    private String installerId;
    private String deviceId;
    private BootNotification confirmedBootNotification;
    private BootNotification pendingBootNotification;
    private Location location;
    private boolean isFinished;

    public static InstallationProcess create(WorkOrder workOrder) {
        return new InstallationProcess(
                workOrder.orderId(),
                workOrder.ownership(),
                new ArrayList<>(List.of(new ProcessCreated(workOrder))),
                null,
                null,
                null,
                null,
                null,
                false
        );
    }

    public void assignInstaller(String installerId) {
        validateNotFinished();
        if (!Objects.equals(this.installerId, installerId)) {
            this.installerId = installerId;
            events.add(new InstallerAssigned(workOrder(), installerId));
        }
    }

    public void assignDevice(String deviceId) {
        validateNotFinished();
        if (!Objects.equals(this.deviceId, deviceId)) {
            this.deviceId = deviceId;
            resetBootState();
            events.add(new DeviceAssigned(workOrder(), deviceId));
        }
    }

    public void receiveBootNotification(BootNotification notification) {
        if (isFinished) {
            return;
        }
        if (!notification.deviceId().equals(deviceId)) {
            throw new IllegalStateException("Device id not matching!");
        }
        if (!Objects.equals(notification, confirmedBootNotification)
                && !Objects.equals(notification, pendingBootNotification)) {
            pendingBootNotification = notification;
            events.add(new BootNotificationReceived(workOrder(), notification));
        }
    }

    public void confirmBoot() {
        validateNotFinished();
        if (pendingBootNotification == null) {
            throw new IllegalStateException("No pending BootNotification to confirm");
        }
        confirmedBootNotification = pendingBootNotification;
        pendingBootNotification = null;
        events.add(new BootNotificationConfirmed(workOrder(), confirmedBootNotification));
    }

    public void setLocation(Location someLocation) {
        validateNotFinished();
        if (confirmedBootNotification == null) {
            throw new IllegalStateException("Notification Boot is not confirmed!");
        }
        if (!Objects.equals(location, someLocation)) {
            location = someLocation;
            events.add(new LocationChanged(workOrder(), location));
        }
    }

    public void finish() {
        validateNotFinished();
        validateCompletedInstallation();
        isFinished = true;
        events.add(new InstallationFinished(workOrder()));
    }

    private void validateNotFinished() {
        if (isFinished) {
            throw new IllegalStateException("Process is already finished");
        }
    }

    private void validateCompletedInstallation() {
        if (installerId == null
                || deviceId == null
                || confirmedBootNotification == null
                || location == null) {
            throw new IllegalStateException("Missing required completion parameters");
        }
    }

    private void resetBootState() {
        if (confirmedBootNotification != null || pendingBootNotification != null) {
            events.add(new BootNotificationReceived(workOrder(), null));
            events.add(new BootNotificationConfirmed(workOrder(), null));
            confirmedBootNotification = null;
            pendingBootNotification = null;
        }
        if (location != null) {
            events.add(new LocationChanged(workOrder(), null));
            location = null;
        }
    }

    public WorkOrder workOrder() {
        return WorkOrder.builder()
                .orderId(this.orderId)
                .ownership(this.ownership)
                .build();
    }
}
