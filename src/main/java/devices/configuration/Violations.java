package devices.configuration;

import lombok.Builder;

@Builder
public record Violations(
        boolean operatorNotAssigned,
        boolean providerNotAssigned,
        boolean locationMissing,
        boolean showOnMapButMissingLocation,
        boolean showOnMapButNoPublicAccess
) {
    public boolean none() {
        return !operatorNotAssigned && !providerNotAssigned && !locationMissing &&
                !showOnMapButMissingLocation && !showOnMapButNoPublicAccess;
    }
}