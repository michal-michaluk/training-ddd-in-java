package devices.configuration.device;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class DeviceConfigurationEditor {
    private final String deviceId;
    final List<DomainEvent>events;
    private Ownership ownership;
    private Location location;
    private OpeningHours openingHours;
    private Settings settings;

    public static DeviceConfigurationEditor createNewDevice(String deviceId){
    return new DeviceConfigurationEditor(
            deviceId,
            new ArrayList<>(),
            Ownership.unowned(),
            null,
            OpeningHours.alwaysOpened(),
            Settings.defaultSettings());
    }
    
    public void assignToOwner(Ownership ownership){
        if(!Objects.equals(this.ownership,ownership)){
            this.ownership=ownership;
            events.add(new DomainEvent.OwnershipUpdated(deviceId, ownership));
        }
        if(this.ownership.isUnowned()){
            resetToDefault();
        }
    }

    public void resetToDefault() {
        if (!Objects.equals(this.openingHours, OpeningHours.alwaysOpened())) {
            this.openingHours = OpeningHours.alwaysOpened();
        }
        if (!Objects.equals(this.location, null)) {
            setLocation(null);
        }
        if (!Objects.equals(this.settings, Settings.defaultSettings())) {
            setSettings(Settings.defaultSettings());
        }
    }

    public void setSettings(Settings settings) {
        if (!Objects.equals(this.settings, settings)) {
            this.settings = this.settings.merge(settings);
            events.add(new DomainEvent.SettingsChanged(deviceId, settings));
        }
    }

    public void setLocation(Location location) {
        if (!Objects.equals(this.location, location)) {
            this.location = location;
            events.add(new DomainEvent.LocationChanged(deviceId, location));
        }
    }
    public void changeOpeningHours(OpeningHours openingHours) {
        if (!Objects.equals(this.openingHours, openingHours)) {
            this.openingHours = openingHours;
            events.add(new DomainEvent.OpeningHoursChanged(deviceId, openingHours));
        }
    }

    public void uninstallDevice() {
        this.ownership = Ownership.unowned();
        resetToDefault();
        events.add(new DomainEvent.DeviceRemoved(deviceId));
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
