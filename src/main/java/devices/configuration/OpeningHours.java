package devices.configuration;

import lombok.Builder;

@Builder
public record OpeningHours(boolean alwaysOpen) {
    public static OpeningHours defaultHours() {
        return new OpeningHours(true);
    }
}
