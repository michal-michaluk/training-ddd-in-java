package devices.configuration.device;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ListAssert;

public class DeviceConfigurationAssert {
    private final DeviceConfiguration actual;

    private DeviceConfigurationAssert(DeviceConfiguration actual) {
        this.actual = actual;
    }

    public static DeviceConfigurationAssert assertThat(DeviceConfigurationEditor editor) {
        return new DeviceConfigurationAssert(editor.toDeviceConfiguration());
    }

    public static ListAssert<DomainEvent> assertEvents(DeviceConfigurationEditor editor) {
        return Assertions.assertThat(editor.events);
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

    public DeviceConfigurationAssert hasViolations(Violations expected) {
        Assertions.assertThat(actual.violations()).isEqualTo(expected);
        return this;
    }
}
