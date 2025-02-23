package devices.configuration.device;

import lombok.Builder;

import java.math.BigDecimal;

@Builder(toBuilder = true)
public record Location(
        String street,
        String houseNumber,
        String city,
        String postalCode,
        String country,
        Coordinates coordinates) {

    public record Coordinates(
            BigDecimal longitude,
            BigDecimal latitude) {}
}