package devices.configuration.device;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ListAssert;

import java.util.List;

public class DeviceConfigurationEventsAssert {

    public static ListAssert<DomainEvent> assertEvents(DeviceConfigurationEditor editor) {
        return Assertions.assertThat(editor.events);
    }

    public static ListAssert<DomainEvent> resetEvents(DeviceConfigurationEditor editor) {
        List<DomainEvent> events = List.copyOf(editor.events);
        editor.events.clear();
        return Assertions.assertThat(events);
    }
}
