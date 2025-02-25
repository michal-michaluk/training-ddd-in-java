package devices.configuration.installation;

import devices.configuration.device.Location;
import devices.configuration.device.Ownership;

public record InstallationState(
        String orderId,
        Ownership ownership,
        String installerId,
        String deviceId,
        BootNotification pendingBootNotification,
        BootNotification confirmedBootNotification,
        Location location,
        boolean isFinished
) {}
