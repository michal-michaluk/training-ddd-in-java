package device;
import devices.configuration.device.DeviceConfigurationEditor;
import devices.configuration.device.Ownership;
import devices.configuration.device.Location;
import devices.configuration.device.OpeningHours;
import devices.configuration.device.Settings;
import devices.configuration.device.Violations;
import org.junit.jupiter.api.Test;
import static device.DeviceTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
public class DeviceConfigurationEditorTest {

    private final String deviceId = "ALF-98262561";

    @Test
    void createNewDevice() {
        // given
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);
        // when
        String expectedJson = """
            {
              "deviceId": "ALF-98262561",
              "ownership": {
                "operator": null,
                "provider": null
              },
              "location": null,
              "openingHours": {
                "alwaysOpen": true
              },
              "settings": {
                "autoStart": false,
                "remoteControl": false,
                "billing": false,
                "reimbursement": false,
                "showOnMap": false,
                "publicAccess": false
              },
              "violations": {
                "operatorNotAssigned": true,
                "providerNotAssigned": true,
                "locationMissing": true,
                "showOnMapButMissingLocation": false,
                "showOnMapButNoPublicAccess": false
              }
            }
            """;
        // then
        assertThat(editor.toDeviceConfiguration().toJson().replaceAll("\\s", ""))
                .isEqualTo(expectedJson.replaceAll("\\s", ""));
    }

    @Test
    void assignOwnership() {
        // given
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);
        // when
        Ownership ownership = someOwnership();
        editor.assignToOwner(ownership);
        // then
        String expectedJson = """
        {
          "deviceId": "ALF-98262561",
          "ownership": {
            "operator": "Devicex.nl",
            "provider": "public-devices"
          },
          "location": null,
          "openingHours": {
            "alwaysOpen": true
          },
          "settings": {
            "autoStart": false,
            "remoteControl": false,
            "billing": false,
            "reimbursement": false,
            "showOnMap": false,
            "publicAccess": false
          },
          "violations": {
            "operatorNotAssigned": false,
            "providerNotAssigned": false,
            "locationMissing": true,
            "showOnMapButMissingLocation": false,
            "showOnMapButNoPublicAccess": false
          }
        }
        """;
        assertThat(editor.toDeviceConfiguration().toJson().replaceAll("\\s", ""))
                .isEqualTo(expectedJson.replaceAll("\\s", ""));
    }

    @Test
    void resetToDefault() {
        // given
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);
        editor.assignToOwner(someOwnership());
        editor.setLocation(someLocationInCity());
        // when
        editor.assignToOwner(Ownership.unowned());
        // then
        String expectedJson = """
        {
          "deviceId": "ALF-98262561",
          "ownership": {
            "operator": null,
            "provider": null
          },
          "location": null,
          "openingHours": {
            "alwaysOpen": true
          },
          "settings": {
            "autoStart": false,
            "remoteControl": false,
            "billing": false,
            "reimbursement": false,
            "showOnMap": false,
            "publicAccess": false
          },
          "violations": {
            "operatorNotAssigned": true,
            "providerNotAssigned": true,
            "locationMissing": true,
            "showOnMapButMissingLocation": false,
            "showOnMapButNoPublicAccess": false
          }
        }
        """;
        assertThat(editor.toDeviceConfiguration().toJson().replaceAll("\\s", ""))
                .isEqualTo(expectedJson.replaceAll("\\s", ""));
    }

    @Test
    void setLocation() {
        // given
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);
        // when
        Location location = someLocationInCity();
        editor.setLocation(location);
        // then
        String expectedJson = """
        {
          "deviceId": "ALF-98262561",
          "ownership": {
            "operator": null,
            "provider": null
          },
          "location": {
            "street": "Rakietowa",
            "houseNumber": "1A",
            "city": "Wrocław",
            "postalCode": "54-621",
            "country": "POL",
            "coordinates": {
              "longitude": 16.931752852309156,
              "latitude": 51.09836221719513
            }
          },
          "openingHours": {
            "alwaysOpen": true
          },
          "settings": {
            "autoStart": false,
            "remoteControl": false,
            "billing": false,
            "reimbursement": false,
            "showOnMap": false,
            "publicAccess": false
          },
          "violations": {
            "operatorNotAssigned": true,
            "providerNotAssigned": true,
            "locationMissing": false,
            "showOnMapButMissingLocation": false,
            "showOnMapButNoPublicAccess": false
          }
        }
        """;
        assertThat(editor.toDeviceConfiguration().toJson().replaceAll("\\s", ""))
                .isEqualTo(expectedJson.replaceAll("\\s", ""));
    }

    @Test
    void setSettings() {
        // given
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);
        // when
        Settings customSettings = someSettings().build();
        editor.setSettings(customSettings);
        // then
        String expectedJson = """
        {
          "deviceId": "ALF-98262561",
          "ownership": {
            "operator": null,
            "provider": null
          },
          "location": null,
          "openingHours": {
            "alwaysOpen": true
          },
          "settings": {
            "autoStart": false,
            "remoteControl": false,
            "billing": false,
            "reimbursement": false,
            "showOnMap": true,
            "publicAccess": true
          },
          "violations": {
            "operatorNotAssigned": true,
            "providerNotAssigned": true,
            "locationMissing": true,
            "showOnMapButMissingLocation": true,
            "showOnMapButNoPublicAccess": false
          }
        }
        """;
        assertThat(editor.toDeviceConfiguration().toJson().replaceAll("\\s", ""))
                .isEqualTo(expectedJson.replaceAll("\\s", ""));
    }

    @Test
    void changeOpeningHours() {
        // given
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);
        // when
        OpeningHours openingHours = new OpeningHours(false);
        editor.changeOpeningHours(openingHours);
        // then
        String expectedJson = """
        {
          "deviceId": "ALF-98262561",
          "ownership": {
            "operator": null,
            "provider": null
          },
          "location": null,
          "openingHours": {
            "alwaysOpen": false
          },
          "settings": {
            "autoStart": false,
            "remoteControl": false,
            "billing": false,
            "reimbursement": false,
            "showOnMap": false,
            "publicAccess": false
          },
          "violations": {
            "operatorNotAssigned": true,
            "providerNotAssigned": true,
            "locationMissing": true,
            "showOnMapButMissingLocation": false,
            "showOnMapButNoPublicAccess": false
          }
        }
        """;
        assertThat(editor.toDeviceConfiguration().toJson().replaceAll("\\s", ""))
                .isEqualTo(expectedJson.replaceAll("\\s", ""));
    }

    @Test
    void uninstallDevice() {
        // given
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);
        editor.assignToOwner(someOwnership());
        // when
        editor.uninstallDevice();
        // then
        String expectedJson = """
        {
          "deviceId": "ALF-98262561",
          "ownership": {
            "operator": null,
            "provider": null
          },
          "location": null,
          "openingHours": {
            "alwaysOpen": true
          },
          "settings": {
            "autoStart": false,
            "remoteControl": false,
            "billing": false,
            "reimbursement": false,
            "showOnMap": false,
            "publicAccess": false
          },
          "violations": {
            "operatorNotAssigned": true,
            "providerNotAssigned": true,
            "locationMissing": true,
            "showOnMapButMissingLocation": false,
            "showOnMapButNoPublicAccess": false
          }
        }
        """;
        assertThat(editor.toDeviceConfiguration().toJson().replaceAll("\\s", ""))
                .isEqualTo(expectedJson.replaceAll("\\s", ""));
    }

    @Test
    void checkViolations() {
        // given
        DeviceConfigurationEditor editor = DeviceConfigurationEditor.createNewDevice(deviceId);
        editor.assignToOwner(someOwnership());
        editor.setLocation(someLocationInCity());
        editor.setSettings(someSettings().build());
        // when
        Violations violations = editor.checkViolations();
        // then
        assertThat(violations.operatorNotAssigned()).isFalse();
        assertThat(violations.providerNotAssigned()).isFalse();
        assertThat(violations.locationMissing()).isFalse();
        assertThat(violations.showOnMapButMissingLocation()).isFalse();
        assertThat(violations.showOnMapButNoPublicAccess()).isFalse();
    }
}
