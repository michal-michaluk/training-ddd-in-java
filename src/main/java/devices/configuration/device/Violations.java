package devices.configuration.device;

import lombok.Builder;

@Builder
public record Violations(
        Boolean operatorNotAssigned,
        Boolean providerNotAssigned,
        Boolean locationMissing,
        Boolean showOnMapButMissingLocation,
        Boolean showOnMapButNoPublicAccess
) {}
