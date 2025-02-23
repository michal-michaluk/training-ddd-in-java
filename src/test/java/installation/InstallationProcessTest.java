package installation;

import devices.configuration.device.Ownership;
import devices.configuration.installation.InstallationProcess;
import devices.configuration.installation.WorkOrder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InstallationProcessTest {

    private static final String orderId = "K56F";
    private static final String deviceId = "ALF-83266831";
    private static final String installerId = "INSTALLER-456";

    private WorkOrder workOrder;
    private InstallationProcess process;

    @BeforeEach
    void setUp() {
        Ownership ownership = Ownership.builder()
                .operator("Devicex.nl")
                .provider("public-devices")
                .build();

        workOrder = WorkOrder.builder()
                .orderId(orderId)
                .ownership(ownership)
                .build();

        process = new InstallationProcess(workOrder);
    }

    @Test
    void shouldCreateInstallationProcess() {
        assertNotNull(process);
        assertEquals(orderId, process.getOrderId());
        assertFalse(process.isFinished());
    }

    @Test
    void whenAssignInstallerAfterFinish_thenThrowException() {
        process.finish();

        assertThrows(IllegalStateException.class, () ->
                process.assignInstaller(installerId)
        );
    }

    @Test
    void whenAssignDeviceAfterFinish_thenThrowException() {
        process.finish();

        assertThrows(IllegalStateException.class, () ->
                process.assignDevice(deviceId)
        );
    }
}
