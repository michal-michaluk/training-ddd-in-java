package devices.configuration.device;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record Ownership(String operator, String provider) {

    public static Ownership unowned() {
        return new Ownership(null, null);
    }

    @JsonIgnore
    public Boolean isUnowned() {
        return operator == null && provider == null;
    }
}
