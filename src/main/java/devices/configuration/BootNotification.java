package devices.configuration;

import lombok.Builder;

@Builder(toBuilder = true)
public record BootNotification(
        String deviceId,
        BootNotification.Protocol protocol,
        String vendor,
        String model,
        String serial,
        String firmware
) {
    public enum Protocol { IoT20, IoT16 }
}
