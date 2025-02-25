package devices.configuration.installation;

import devices.configuration.device.Location;

public interface DomainEvent {
    record ProcessCreated(WorkOrder workOrder) implements DomainEvent {}

    record InstallerAssigned(String orderId, String installerId) implements DomainEvent {}

    record DeviceAssigned(String orderId, String deviceId) implements DomainEvent {}

    record BootNotificationReceived(String orderId, BootNotification pendingBootNotification) implements DomainEvent {}

    record BootNotificationConfirmed(String orderId, BootNotification confirmedBootNotification)implements DomainEvent {}

    record LocationChanged(String orderId, Location location) implements DomainEvent {}

    record InstallationFinished(String orderId) implements DomainEvent {}
}
