package devices.configuration.installation;

import devices.configuration.device.Ownership;

import java.util.Objects;

public class InstallationProcess {

    private final String orderId;
    private final Ownership ownership;

    private String installerId;
    private String deviceId;
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
        }
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

    public String getOrderId() { return orderId; }
    public boolean isFinished() { return isFinished; }
}
