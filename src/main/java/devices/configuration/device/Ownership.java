package devices.configuration.device;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

@Builder
public record Ownership(String operator, String provider) {
    public Ownership {
        if ((operator == null && provider != null)
                || (operator != null && provider == null)) {
            throw new IllegalArgumentException("Either both operator and provider must be null or both must be non-null");
        }
    }

    public static Ownership unowned() {
        return new Ownership(null, null);
    }

    @JsonIgnore
    public Boolean isUnowned() {
        return operator == null && provider == null;
    }
}