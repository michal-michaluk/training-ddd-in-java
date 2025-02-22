package devices.configuration.installation;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class InstallationProcess {

    public static final Set<String> existingOrders = new HashSet<>();

    private final String orderId;
    private boolean isFinished;
    private String deviceId;
    private String installerId;

    public InstallationProcess(String orderId) {
        if (existingOrders.contains(orderId)) {
            throw new IllegalStateException("Work order already exists!");
        }

        this.orderId = orderId;
        existingOrders.add(orderId);
    }

    public void finishProcess() {
        if (!isFinished) {
            isFinished = true;
            existingOrders.remove(orderId);
        }
    }

    public void assignDevice(String deviceId) {
        if (isFinished) {
            throw new IllegalStateException("Device cannot be assigned. Process Finished!");
        }
        if (!Objects.equals(this.deviceId, deviceId)) {
            this.deviceId = deviceId;
        }
    }

    public void assignInstaller(String installerId) {
        if (isFinished) {
            throw new IllegalStateException("Installer cannot be assigned. Process Finished!");
        }
        if (!Objects.equals(this.installerId, installerId)) {
            this.installerId = installerId;
        }
    }
}
