package devices.configuration;

import lombok.Builder;

@Builder(toBuilder = true)
public record Ownership(String operator, String provider) {
    public boolean isUnowned() {
        return operator == null && provider == null;
    }
}
