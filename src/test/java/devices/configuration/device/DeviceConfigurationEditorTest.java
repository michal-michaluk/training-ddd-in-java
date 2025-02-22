package devices.configuration.device;

import org.junit.jupiter.api.Test;

import static devices.configuration.device.DeviceConfigurationAssert.assertThat;
import static devices.configuration.device.DeviceConfigurationEventsAssert.assertEvents;
import static devices.configuration.device.DeviceConfigurationFixture.*;
import static devices.configuration.device.DomainEvent.*;

public class DeviceConfigurationEditorTest {

    private final String deviceId = "ALF-98262561";

    @Test
    void createNewDevice() {
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);

        assertThat(editor)
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

        assertEvents(editor)
                .containsExactly(new DeviceCreated(deviceId));
    }

    @Test
    void assignOwnership() {
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);

        Ownership ownership = someOwnership();
        editor.assignToOwner(ownership);

        assertThat(editor)
                .hasOwnership(ownership)
                .hasViolations(Violations.builder()
                        .operatorNotAssigned(false)
                        .providerNotAssigned(false)
                        .locationMissing(true)
                        .showOnMapButMissingLocation(false)
                        .showOnMapButNoPublicAccess(false)
                        .build());

        assertEvents(editor)
                .containsExactly(
                        new DeviceCreated(deviceId),
                        new OwnershipUpdated(deviceId, ownership));
    }

    @Test
    void resetToDefault() {
        DeviceConfigurationEditor editor = given(deviceId, e -> {
            e.assignToOwner(someOwnership());
            e.setLocation(someLocationInCity());
            e.setSettings(someSettings().build());
        });

        editor.assignToOwner(Ownership.unowned());

        assertThat(editor)
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

        assertEvents(editor)
                .containsExactly(
                        new OwnershipUpdated(deviceId, Ownership.unowned()),
                        new LocationChanged(deviceId, null),
                        new SettingsChanged(deviceId, Settings.defaultSettings())
                );
    }

    @Test
    void setLocation() {
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);

        Location location = someLocationInCity();
        editor.setLocation(location);

        assertThat(editor)
                .hasLocation(location)
                .hasViolations(Violations.builder()
                        .operatorNotAssigned(true)
                        .providerNotAssigned(true)
                        .locationMissing(false)
                        .showOnMapButMissingLocation(false)
                        .showOnMapButNoPublicAccess(false)
                        .build());

        assertEvents(editor)
                .containsExactly(
                        new DeviceCreated(deviceId),
                        new LocationChanged(deviceId, location));
    }

    @Test
    void setSettings() {
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);

        Settings settings = someSettings().build();
        editor.setSettings(settings);

        assertThat(editor)
                .hasSettings(settings)
                .hasViolations(Violations.builder()
                        .operatorNotAssigned(true)
                        .providerNotAssigned(true)
                        .locationMissing(true)
                        .showOnMapButMissingLocation(true)
                        .showOnMapButNoPublicAccess(false)
                        .build());

        assertEvents(editor)
                .containsExactly(
                        new DeviceCreated(deviceId),
                        new SettingsChanged(deviceId, settings));
    }

    @Test
    void changeOpeningHours() {
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);

        OpeningHours openingHours = OpeningHours.notAlwaysOpened();
        editor.changeOpeningHours(openingHours);

        assertThat(editor)
                .hasOpeningHours(openingHours)
                .hasViolations(Violations.builder()
                        .operatorNotAssigned(true)
                        .providerNotAssigned(true)
                        .locationMissing(true)
                        .showOnMapButMissingLocation(false)
                        .showOnMapButNoPublicAccess(false)
                        .build());

        assertEvents(editor)
                .containsExactly(
                        new DeviceCreated(deviceId),
                        new OpeningHoursChanged(deviceId, openingHours));
    }

    @Test
    void noEventsEmittedWhenValuesAreTheSame() {
        DeviceConfigurationEditor editor = given(deviceId, e -> {
            e.assignToOwner(someOwnership());
            e.setLocation(someLocationInCity());
            e.setSettings(someSettings().build());
        });

        editor.assignToOwner(someOwnership());
        editor.setLocation(someLocationInCity());
        editor.setSettings(someSettings().build());

        assertEvents(editor)
                .isEmpty();
    }

    @Test
    void uninstallDevice() {
        DeviceConfigurationEditor editor = given(deviceId, e -> {
            e.assignToOwner(someOwnership());
            e.setLocation(someLocationInCity());
            e.setSettings(someSettings().build());
        });

        editor.uninstallDevice();

        assertThat(editor)
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

        assertEvents(editor)
                .containsExactly(
                        new OwnershipUpdated(deviceId, Ownership.unowned()),
                        new LocationChanged(deviceId, null),
                        new SettingsChanged(deviceId, Settings.defaultSettings()),
                        new DomainEvent.DeviceRemoved(deviceId)
                );
    }

    @Test
    void checkNoViolations() {
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);

        editor.assignToOwner(someOwnership());
        editor.setLocation(someLocationInCity());
        editor.setSettings(someSettings().build());

        assertThat(editor)
                .hasViolations(Violations.builder()
                        .operatorNotAssigned(false)
                        .providerNotAssigned(false)
                        .locationMissing(false)
                        .showOnMapButMissingLocation(false)
                        .showOnMapButNoPublicAccess(false)
                        .build());
    }

    @Test
    void checkViolationsForInconsistentSettings() {
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);

        editor.assignToOwner(someOwnership());
        editor.setLocation(someLocationInCity());
        editor.setSettings(someSettings()
                .showOnMap(true)
                .publicAccess(false)
                .build()
        );

        assertThat(editor)
                .hasViolations(Violations.builder()
                        .operatorNotAssigned(false)
                        .providerNotAssigned(false)
                        .locationMissing(false)
                        .showOnMapButMissingLocation(false)
                        .showOnMapButNoPublicAccess(true)
                        .build());
    }
}
