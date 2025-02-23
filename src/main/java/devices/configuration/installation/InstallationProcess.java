package devices.configuration.installation;

import devices.configuration.device.Location;
import devices.configuration.device.Ownership;
import lombok.Getter;

import java.util.Objects;

@Getter
public class InstallationProcess {

    private final String orderId;
    private final Ownership ownership;

    private String installerId;
    private String deviceId;

    private BootNotification confirmedBootNotification;
    private BootNotification pendingBootNotification;

    private Location location;

    private boolean isFinished;

    public InstallationProcess(WorkOrder workOrder) {
        this.orderId = workOrder.orderId();
        this.ownership = workOrder.ownership();
    }

    public void assignInstaller(String installerId) {
        validateNotFinished();
        if (!Objects.equals(this.installerId, installerId)) {
            this.installerId = installerId;
        }
    }

    public void assignDevice(String deviceId) {
        validateNotFinished();
        if (!Objects.equals(this.deviceId, deviceId)) {
            this.deviceId = deviceId;
            resetBootState();
        }
    }

    public void receiveBootNotification(BootNotification notification) {
        if (isFinished) {
            return;
        }
        if (!notification.deviceId().equals(deviceId)) {
            throw new IllegalStateException("Device id not matching!");
        }

        if (!notification.equals(confirmedBootNotification)) {
            pendingBootNotification = notification;
        }
    }

    public void confirmBoot() {
        validateNotFinished();
        if (pendingBootNotification == null) {
            throw new IllegalStateException("No pending BootNotification to confirm");
        }
        confirmedBootNotification = pendingBootNotification;
        pendingBootNotification = null;
    }

    public void setLocation(Location someLocation) {
        validateNotFinished();
        if (confirmedBootNotification == null) {
            throw new IllegalStateException("Notification Boot is not confirmed!");
        }
        if (!Objects.equals(location, someLocation)) {
            location = someLocation;
        }
    }

    public void finish() {
        validateNotFinished();
        validateCompletedInstallation();
        isFinished = true;
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
        confirmedBootNotification = null;
        pendingBootNotification = null;
        location = null;
    }
}
