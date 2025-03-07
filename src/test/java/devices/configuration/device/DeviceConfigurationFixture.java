package devices.configuration.device;

import java.math.BigDecimal;
import java.util.function.Consumer;

public class DeviceConfigurationFixture {

    static DeviceConfigurationEditor given(String deviceId) {
        return given(deviceId, e -> {
        });
    }

    static DeviceConfigurationEditor given(String deviceId, Consumer<DeviceConfigurationEditor> customize) {
        var editor = DeviceConfigurationEditor.createNewDevice(deviceId);
        customize.accept(editor);
        DeviceConfigurationEventsAssert.resetEvents(editor);
        return editor;
    }

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
                .autoStart(false)
                .remoteControl(false)
                .billing(false)
                .reimbursement(false)
                .publicAccess(true)
                .showOnMap(true);
    }
}
