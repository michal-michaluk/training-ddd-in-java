import devices.configuration.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeviceConfigurationEditorTest {

    @Test
    public void testCheckViolations() {
        Ownership ownership = new Ownership("operator", "provider");
        Location location = null;
        Settings settings = Settings.builder()
                .showOnMap(true)
                .publicAccess(false)
                .build();

        DeviceConfiguration deviceConfig = DeviceConfiguration.builder()
                .deviceId("device1")
                .ownership(ownership)
                .location(null)
                .openingHours(OpeningHours.defaultHours())
                .settings(settings)
                .violations(null)
                .visibility(null)
                .build();

        WorkOrder workOrder = new WorkOrder("order1", ownership, false);
        DeviceConfigurationEditor editor = new DeviceConfigurationEditor(deviceConfig, workOrder);

        Violations resultViolations = editor.checkViolations();

        assertTrue(resultViolations.locationMissing(), "The location should be missing.");
        assertTrue(resultViolations.showOnMapButMissingLocation(), "Device is set to show on map but location is missing.");
        assertTrue(resultViolations.showOnMapButNoPublicAccess(), "Device is visible on the map but has no public access.");
        assertFalse(resultViolations.operatorNotAssigned(), "Operator is assigned, so this should be false.");
        assertFalse(resultViolations.providerNotAssigned(), "Provider is assigned, so this should be false.");
    }
}