package devices.configuration.device;

public interface DomainEvent {
    public record OwnershipUpdated(String deviceId, Ownership ownership) implements DomainEvent {}
    public record SettingsChanged(String deviceId, Settings settings) implements DomainEvent {}
    public record LocationChanged(String deviceId, Location location) implements DomainEvent {}
    public record OpeningHoursChanged(String deviceId, OpeningHours openingHours) implements DomainEvent {}
    public record DeviceRemoved(String deviceId) implements DomainEvent {}
}
