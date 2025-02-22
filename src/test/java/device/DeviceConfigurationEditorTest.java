package device;

import devices.configuration.device.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static device.DeviceTestFixture.*;

public class DeviceConfigurationEditorTest {

    private final String deviceId = "ALF-98262561";

    @Test
    void createNewDevice() {
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);
        DeviceConfigurationAssert.assertThat(editor)
                .hasOwnership(Ownership.unowned())
                .hasLocation(null)
                .hasOpeningHours(OpeningHours.alwaysOpened())
                .hasSettings(Settings.defaultSettings())
                .hasViolations(Violations.builder()
                        .operatorNotAssigned(true)
                        .providerNotAssigned(true)
                        .locationMissing(true)
                        .showOnMapButMissingLocation(false)
                        .showOnMapButNoPublicAccess(false)
                        .build());
    }

    @Test
    void assignOwnership() {
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);
        Ownership ownership = someOwnership();
        editor.assignToOwner(ownership);
        List<DomainEvent> expectedEvents = List.of(new DomainEvent.OwnershipUpdated(deviceId, ownership));
        DeviceConfigurationAssert.assertThat(editor)
                .hasOwnership(ownership)
                .hasEvents(expectedEvents)
                .hasViolations(Violations.builder()
                        .operatorNotAssigned(false)
                        .providerNotAssigned(false)
                        .locationMissing(true)
                        .showOnMapButMissingLocation(false)
                        .showOnMapButNoPublicAccess(false)
                        .build());
    }

    @Test
    void resetToDefault() {
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);
        editor.assignToOwner(someOwnership());
        editor.setLocation(someLocationInCity());
        editor.assignToOwner(Ownership.unowned());
        DeviceConfigurationAssert.assertThat(editor)
                .hasOwnership(Ownership.unowned())
                .hasLocation(null)
                .hasOpeningHours(OpeningHours.alwaysOpened())
                .hasSettings(Settings.defaultSettings())
                .hasViolations(Violations.builder()
                        .operatorNotAssigned(true)
                        .providerNotAssigned(true)
                        .locationMissing(true)
                        .showOnMapButMissingLocation(false)
                        .showOnMapButNoPublicAccess(false)
                        .build());
    }

    @Test
    void setLocation() {
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);
        Location location = someLocationInCity();
        editor.setLocation(location);
        List<DomainEvent> expectedEvents = List.of(new DomainEvent.LocationChanged(deviceId, location));
        DeviceConfigurationAssert.assertThat(editor)
                .hasLocation(location)
                .hasEvents(expectedEvents)
                .hasViolations(Violations.builder()
                        .operatorNotAssigned(true)
                        .providerNotAssigned(true)
                        .locationMissing(false)
                        .showOnMapButMissingLocation(false)
                        .showOnMapButNoPublicAccess(false)
                        .build());
    }

    @Test
    void setSettings() {
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);
        Settings settings = someSettings().build();
        editor.setSettings(settings);
        List<DomainEvent> expectedEvents = List.of(new DomainEvent.SettingsChanged(deviceId, settings));
        DeviceConfigurationAssert.assertThat(editor)
                .hasSettings(settings)
                .hasEvents(expectedEvents)
                .hasViolations(Violations.builder()
                        .operatorNotAssigned(true)
                        .providerNotAssigned(true)
                        .locationMissing(true)
                        .showOnMapButMissingLocation(true)
                        .showOnMapButNoPublicAccess(false)
                        .build());
    }

    @Test
    void changeOpeningHours() {
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);
        OpeningHours openingHours = OpeningHours.notAlwaysOpened();
        editor.changeOpeningHours(openingHours);
        List<DomainEvent> expectedEvents = List.of(new DomainEvent.OpeningHoursChanged(deviceId, openingHours));
        DeviceConfigurationAssert.assertThat(editor)
                .hasOpeningHours(openingHours)
                .hasEvents(expectedEvents)
                .hasViolations(Violations.builder()
                        .operatorNotAssigned(true)
                        .providerNotAssigned(true)
                        .locationMissing(true)
                        .showOnMapButMissingLocation(false)
                        .showOnMapButNoPublicAccess(false)
                        .build());
    }

    @Test
    void uninstallDevice() {
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);
        editor.assignToOwner(someOwnership());
        editor.uninstallDevice();
        List<DomainEvent> expectedEvents = List.of(
                new DomainEvent.OwnershipUpdated(deviceId, someOwnership()),
                new DomainEvent.OwnershipUpdated(deviceId, Ownership.unowned()),
                new DomainEvent.DeviceRemoved(deviceId));
        DeviceConfigurationAssert.assertThat(editor)
                .hasOwnership(Ownership.unowned())
                .hasLocation(null)
                .hasOpeningHours(OpeningHours.alwaysOpened())
                .hasSettings(Settings.defaultSettings())
                .hasEvents(expectedEvents)
                .hasViolations(Violations.builder()
                        .operatorNotAssigned(true)
                        .providerNotAssigned(true)
                        .locationMissing(true)
                        .showOnMapButMissingLocation(false)
                        .showOnMapButNoPublicAccess(false)
                        .build());
    }

    @Test
    void checkViolations() {
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);
        editor.assignToOwner(someOwnership());
        editor.setLocation(someLocationInCity());
        editor.setSettings(someSettings().build());
        DeviceConfigurationAssert.assertThat(editor)
                .hasViolations(Violations.builder()
                        .operatorNotAssigned(false)
                        .providerNotAssigned(false)
                        .locationMissing(false)
                        .showOnMapButMissingLocation(false)
                        .showOnMapButNoPublicAccess(false)
                        .build());
    }
}
