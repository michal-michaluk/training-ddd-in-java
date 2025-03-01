package devices.configuration;

public class DeviceConfigurationEditor {
    private final DeviceConfiguration deviceConfiguration;
    private final WorkOrder workOrder;

    public DeviceConfigurationEditor(DeviceConfiguration deviceConfiguration, WorkOrder workOrder) {
        this.deviceConfiguration = deviceConfiguration;
        this.workOrder = workOrder;
    }

    public Violations checkViolations() {
        return Violations.builder()
                .operatorNotAssigned(deviceConfiguration.ownership().operator() == null)
                .providerNotAssigned(deviceConfiguration.ownership().provider() == null)
                .locationMissing(deviceConfiguration.location() == null)
                .showOnMapButMissingLocation(deviceConfiguration.settings().showOnMap() && deviceConfiguration.location() == null)
                .showOnMapButNoPublicAccess(deviceConfiguration.settings().showOnMap() && !deviceConfiguration.settings().publicAccess())
                .build();
    }

    public void checkWorkOrderStatus() {
        if (workOrder == null) {
            throw new IllegalStateException("WorkOrder is not assigned.");
        }
        if (workOrder.isFinished()) {
            throw new IllegalStateException("Cannot process a finished work order.");
        }
    }

    public DeviceConfiguration toDeviceConfiguration() {
        checkWorkOrderStatus();
        Violations violations = checkViolations();
        Visibility visibility = Visibility.calculate(deviceConfiguration.settings(), violations);
        return deviceConfiguration.toBuilder()
                .violations(violations)
                .visibility(visibility)
                .build();
    }
}
