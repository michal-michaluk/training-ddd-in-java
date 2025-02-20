package device;
import devices.configuration.device.Location;
import devices.configuration.device.OpeningHours;
import devices.configuration.device.Ownership;
import devices.configuration.device.Settings;
import lombok.Builder;
import java.math.BigDecimal;
public class DeviceTestFixture {
    public static Ownership someOwnership() {
        return new Ownership("Devicex.nl", "public-devices");
    }
    public static Location someLocationInCity() {
        return new Location(
                "Rakietowa",
                "1A",
                "Wrocław",
                "54-621",
                "POL",
                new Location.Coordinates(
                        new BigDecimal("16.931752852309156"),
                        new BigDecimal("51.09836221719513")
                )
        );
    }
    public static Settings.SettingsBuilder someSettings() {
        return Settings.builder()
                .publicAccess(true)
                .showOnMap(true);
    }
}
