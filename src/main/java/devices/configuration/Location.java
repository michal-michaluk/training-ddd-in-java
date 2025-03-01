package devices.configuration;

import devices.configuration.Coordinates;

public record Location(
        String street,
        String houseNumber,
        String city,
        String postalCode,
        String country,
        Coordinates coordinates
) {

    public static Location unassigned() {
        return new Location(null, null, null, null, null, null);
    }

}
