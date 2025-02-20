package device;
import devices.configuration.device.*;
import org.junit.jupiter.api.Test;
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
                .hasViolations(true, true, true, false, false);
    }

    @Test
    void assignOwnership() {
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);
        Ownership ownership = someOwnership();
        editor.assignToOwner(ownership);
        DeviceConfigurationAssert.assertThat(editor)
                .hasOwnership(ownership)
                .hasLocation(null)
                .hasOpeningHours(OpeningHours.alwaysOpened())
                .hasSettings(Settings.defaultSettings())
                .hasViolations(false, false, true, false, false);
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
                .hasViolations(true, true, true, false, false);
    }

    @Test
    void setLocation() {
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);
        Location location = someLocationInCity();
        editor.setLocation(location);
        DeviceConfigurationAssert.assertThat(editor)
                .hasOwnership(Ownership.unowned())
                .hasLocation(location)
                .hasOpeningHours(OpeningHours.alwaysOpened())
                .hasSettings(Settings.defaultSettings())
                .hasViolations(true, true, false, false, false);
    }

    @Test
    void setSettings() {
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);
        Settings customSettings = someSettings().build();
        editor.setSettings(customSettings);
        DeviceConfigurationAssert.assertThat(editor)
                .hasOwnership(Ownership.unowned())
                .hasLocation(null)
                .hasOpeningHours(OpeningHours.alwaysOpened())
                .hasSettings(customSettings)
                .hasViolations(true, true, true, true, false);
    }

    @Test
    void changeOpeningHours() {
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);
        OpeningHours openingHours = OpeningHours.alwaysOpened();
        editor.changeOpeningHours(openingHours);
        DeviceConfigurationAssert.assertThat(editor)
                .hasOwnership(Ownership.unowned())
                .hasLocation(null)
                .hasOpeningHours(openingHours)
                .hasSettings(Settings.defaultSettings())
                .hasViolations(true, true, true, false, false);
    }

    @Test
    void uninstallDevice() {
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);
        editor.assignToOwner(someOwnership());
        editor.uninstallDevice();
        DeviceConfigurationAssert.assertThat(editor)
                .hasOwnership(Ownership.unowned())
                .hasLocation(null)
                .hasOpeningHours(OpeningHours.alwaysOpened())
                .hasSettings(Settings.defaultSettings())
                .hasViolations(true, true, true, false, false);
    }

    @Test
    void checkViolations() {
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);
        editor.assignToOwner(someOwnership());
        editor.setLocation(someLocationInCity());
        editor.setSettings(someSettings().build());
        DeviceConfigurationAssert.assertThat(editor)
                .hasNoViolations();
    }
}
