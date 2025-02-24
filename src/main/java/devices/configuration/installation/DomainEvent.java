package devices.configuration.installation;

import devices.configuration.device.Location;

public interface DomainEvent {
    record ProcessCreated(WorkOrder workOrder) implements DomainEvent {}

    record InstallerAssigned(WorkOrder workOrder, String installerId) implements DomainEvent {}

    record DeviceAssigned(WorkOrder workOrder, String deviceId) implements DomainEvent {}

    record BootNotificationReceived(WorkOrder workOrder, BootNotification pendingBootNotification) implements DomainEvent {}

    record BootNotificationConfirmed(WorkOrder workOrder, BootNotification confirmedBootNotification)implements DomainEvent {}

    record LocationChanged(WorkOrder workOrder, Location location) implements DomainEvent {}

    record InstallationFinished(WorkOrder workOrder) implements DomainEvent {}
}
