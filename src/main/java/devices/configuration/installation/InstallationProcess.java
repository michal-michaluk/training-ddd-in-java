package devices.configuration.installation;

import devices.configuration.device.Ownership;

import java.util.Objects;

public class InstallationProcess {

    private final String orderId;
    private final Ownership ownership;

    private String installerId;
    private String deviceId;

    private BootNotification confirmedBootNotification;
    private BootNotification pendingBootNotification;

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
            resetBootNotification();
        }
    }

    public void receiveBootNotification(BootNotification notification) {
        if (isFinished) {
            return;
        } else if (!notification.deviceId().equals(deviceId)) {
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

    public void finish() {
        validateNotFinished();
        isFinished = true;
    }

    private void validateNotFinished() {
        if (isFinished) {
            throw new IllegalStateException("Process is already finished");
        }
    }

    private void resetBootNotification() {
        confirmedBootNotification = null;
        pendingBootNotification = null;
    }

    public String getOrderId() {
        return orderId;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public BootNotification getPendingBootNotification() {
        return pendingBootNotification;
    }

    public BootNotification getConfirmedBootNotification() {
        return confirmedBootNotification;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
