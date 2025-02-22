package devices.configuration.installation;

import lombok.Builder;

@Builder(toBuilder = true)
public record BootNotification(
        String deviceId, Protocols protocol,
        String vendor, String model,
        String serial, String firmware) {

    public BootNotification(String deviceId) {
        this(deviceId, null, null, null, null, null);
    }

    public enum Protocols {
        IoT20, IoT16
    }
}
