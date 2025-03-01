import devices.configuration.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WorkOrderTest {

    @Test
    public void testWorkOrderEquals() {
        Ownership ownership = new Ownership("operator1", "provider1");
        WorkOrder workOrder1 = new WorkOrder("order1", ownership, false);
        WorkOrder workOrder2 = new WorkOrder("order1", ownership, false);

        assertEquals(workOrder1, workOrder2, "Work orders with the same orderId and ownership should be equal");
    }

    @Test
    public void testWorkOrderStatus() {
        Ownership ownership = new Ownership("operator2", "provider2");
        WorkOrder workOrder = new WorkOrder("order2", ownership, true);

        assertTrue(workOrder.isFinished(), "Work order should be marked as finished");
    }

    @Test
    public void testWorkOrderStatusFalse() {
        Ownership ownership = new Ownership("operator3", "provider3");
        WorkOrder workOrder = new WorkOrder("order3", ownership, false);

        assertFalse(workOrder.isFinished(), "Work order should not be finished");
    }

    @Test
    public void testWorkOrderStatusException() {
        Ownership ownership = new Ownership("operator", "provider");
        Location location = Location.unassigned();
        Settings settings = Settings.defaultSettings();

        Violations violations = Violations.builder()
                .operatorNotAssigned(false)
                .providerNotAssigned(false)
                .locationMissing(true)
                .showOnMapButMissingLocation(true)
                .showOnMapButNoPublicAccess(false)
                .build();

        Visibility visibility = Visibility.calculate(settings, violations);

        DeviceConfiguration deviceConfig = new DeviceConfiguration(
                "device1", ownership, location, OpeningHours.defaultHours(), settings, violations, visibility
        );

        WorkOrder workOrder = new WorkOrder("order1", ownership, true);
        DeviceConfigurationEditor editor = new DeviceConfigurationEditor(deviceConfig, workOrder);

        assertThrows(IllegalStateException.class, editor::checkWorkOrderStatus, "Cannot process a finished work order.");
    }
}