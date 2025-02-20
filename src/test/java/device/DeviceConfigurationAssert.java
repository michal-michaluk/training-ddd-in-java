package device;
import devices.configuration.device.*;
import org.assertj.core.api.Assertions;

public class DeviceConfigurationAssert {
    private final DeviceConfiguration actual;
    private DeviceConfigurationAssert(DeviceConfiguration actual) {
        this.actual = actual;
    }
    public static DeviceConfigurationAssert assertThat(DeviceConfigurationEditor editor) {
        return new DeviceConfigurationAssert(editor.toDeviceConfiguration());
    }
    public DeviceConfigurationAssert hasOwnership(Ownership expected) {
        Assertions.assertThat(actual.ownership()).isEqualTo(expected);
        return this;
    }
    public DeviceConfigurationAssert hasLocation(Location expected) {
        Assertions.assertThat(actual.location()).isEqualTo(expected);
        return this;
    }
    public DeviceConfigurationAssert hasOpeningHours(OpeningHours expected) {
        Assertions.assertThat(actual.openingHours()).isEqualTo(expected);
        return this;
    }
    public DeviceConfigurationAssert hasSettings(Settings expected) {
        Assertions.assertThat(actual.settings()).isEqualTo(expected);
        return this;
    }
    public DeviceConfigurationAssert hasViolations(boolean operatorNotAssigned,
                                                   boolean providerNotAssigned,
                                                   boolean locationMissing,
                                                   boolean showOnMapButMissingLocation,
                                                   boolean showOnMapButNoPublicAccess) {
        Assertions.assertThat(actual.violations().operatorNotAssigned()).isEqualTo(operatorNotAssigned);
        Assertions.assertThat(actual.violations().providerNotAssigned()).isEqualTo(providerNotAssigned);
        Assertions.assertThat(actual.violations().locationMissing()).isEqualTo(locationMissing);
        Assertions.assertThat(actual.violations().showOnMapButMissingLocation()).isEqualTo(showOnMapButMissingLocation);
        Assertions.assertThat(actual.violations().showOnMapButNoPublicAccess()).isEqualTo(showOnMapButNoPublicAccess);
        return this;
    }
    public DeviceConfigurationAssert hasNoViolations() {
        return hasViolations(false, false, false, false, false);
    }
}
