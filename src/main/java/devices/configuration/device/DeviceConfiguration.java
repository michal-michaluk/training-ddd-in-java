package devices.configuration.device;
import com.fasterxml.jackson.databind.ObjectMapper;
public record DeviceConfiguration(
        String deviceId,
        Ownership ownership,
        Location location,
        OpeningHours openingHours,
        Settings settings,
        Violations violations
) { }
