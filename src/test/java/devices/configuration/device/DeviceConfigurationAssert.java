package devices.configuration.device;

import org.assertj.core.api.Assertions;

import java.util.List;

public class DeviceConfigurationAssert {
    private final DeviceConfiguration actual;
    private final List<DomainEvent> events;

    private DeviceConfigurationAssert(DeviceConfiguration actual, List<DomainEvent> events) {
        this.actual = actual;
        this.events = events;
    }

    public static DeviceConfigurationAssert assertThat(DeviceConfigurationEditor editor) {
        return new DeviceConfigurationAssert(editor.toDeviceConfiguration(), editor.getEvents());
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

    public DeviceConfigurationAssert hasEvents(List<DomainEvent> expectedEvents) {
        Assertions.assertThat(events).isEqualTo(expectedEvents);
        return this;
    }
}
