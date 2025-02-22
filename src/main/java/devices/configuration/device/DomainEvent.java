package devices.configuration.device;

public interface DomainEvent {
    record OwnershipUpdated(String deviceId, Ownership ownership) implements DomainEvent {}

    record SettingsChanged(String deviceId, Settings settings) implements DomainEvent {}

    record LocationChanged(String deviceId, Location location) implements DomainEvent {}

    record OpeningHoursChanged(String deviceId, OpeningHours openingHours) implements DomainEvent {}

    record DeviceRemoved(String deviceId) implements DomainEvent {}
}
