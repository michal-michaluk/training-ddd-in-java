package devices.configuration.device;

import devices.configuration.device.DomainEvent.*;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class DeviceConfigurationEditor {
    private final String deviceId;
    List<DomainEvent> events;
    private Ownership ownership;
    private Location location;
    private OpeningHours openingHours;
    private Settings settings;

    public static DeviceConfigurationEditor createNewDevice(String deviceId) {
        return new DeviceConfigurationEditor(
                deviceId,
                new ArrayList<>(List.of(new DeviceCreated(deviceId))),
                Ownership.unowned(),
                null,
                OpeningHours.alwaysOpened(),
                Settings.defaultSettings());
    }

    public void assignToOwner(Ownership ownership) {
        if (!Objects.equals(this.ownership, ownership)) {
            this.ownership = ownership;
            events.add(new OwnershipUpdated(deviceId, ownership));
        }
        if (this.ownership.isUnowned()) {
            resetToDefault();
        }
    }

    public void resetToDefault() {
        changeOpeningHours(OpeningHours.alwaysOpened());
        setLocation(null);
        setSettings(Settings.defaultSettings());
    }

    public void setSettings(Settings settings) {
        Settings set = this.settings.merge(settings);
        if (!Objects.equals(this.settings, set)) {
            this.settings = set;
            events.add(new SettingsChanged(deviceId, set));
        }
    }

    public void setLocation(Location location) {
        if (!Objects.equals(this.location, location)) {
            this.location = location;
            events.add(new LocationChanged(deviceId, location));
        }
    }

    public void changeOpeningHours(OpeningHours openingHours) {
        if (!Objects.equals(this.openingHours, openingHours)) {
            this.openingHours = openingHours;
            events.add(new OpeningHoursChanged(deviceId, openingHours));
        }
    }

    public void uninstallDevice() {
        assignToOwner(Ownership.unowned());
        events.add(new DeviceRemoved(deviceId));
    }

    public Violations checkViolations() {
        return Violations.builder()
                .operatorNotAssigned(ownership.operator() == null)
                .providerNotAssigned(ownership.provider() == null)
                .locationMissing(location == null)
                .showOnMapButMissingLocation(settings.showOnMap() && location == null)
                .showOnMapButNoPublicAccess(settings.showOnMap() && !settings.publicAccess())
                .build();
    }

    public DeviceConfiguration toDeviceConfiguration() {
        Violations violations = checkViolations();
        return new DeviceConfiguration(deviceId, ownership, location, openingHours, settings, violations);
    }
}
