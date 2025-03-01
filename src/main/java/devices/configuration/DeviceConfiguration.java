package devices.configuration;

import lombok.Builder;

@Builder(toBuilder = true)
public record DeviceConfiguration(
        String deviceId,
        Ownership ownership,
        Location location,
        OpeningHours openingHours,
        Settings settings,
        Violations violations,
        Visibility visibility
) {

}
