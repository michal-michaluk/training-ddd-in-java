package installation;

import devices.configuration.device.Location;
import devices.configuration.device.Ownership;
import devices.configuration.installation.BootNotification;
import devices.configuration.installation.InstallationProcess;
import devices.configuration.installation.WorkOrder;

import java.math.BigDecimal;

public class InstallationProcessFixture {

    private static final String orderId = "K56F";
    private static final String deviceId = "ALF-83266831";
    private static final String installerId = "INSTALLER-456";

    public static BootNotification defaultNotification() {
        return BootNotification.builder()
                .deviceId(deviceId)
                .protocol(BootNotification.Protocols.IoT20)
                .vendor("EVSE")
                .model("UltraCharge")
                .serial("123-ABC")
                .firmware("v2.0")
                .build();
    }

    public static Location someLocation() {
        return Location.builder()
                .street("Rakietowa")
                .houseNumber("1A")
                .city("Wrocław")
                .postalCode("54-621")
                .country("POL")
                .coordinates(new Location.Coordinates(new BigDecimal("16.931752852309156"), new BigDecimal("51.09836221719513")))
                .build();
    }

    public static Ownership someOwnership() {
        return Ownership.builder()
                .operator("Devicex.nl")
                .provider("public-devices")
                .build();
    }

    public static WorkOrder defaultOrder() {
        return WorkOrder.builder()
                .orderId(orderId)
                .ownership(someOwnership())
                .build();
    }

    public static InstallationProcess processInProgress() {
        InstallationProcess process = new InstallationProcess(defaultOrder());
        process.assignDevice(deviceId);
        process.assignInstaller(installerId);
        process.receiveBootNotification(defaultNotification());
        process.confirmBoot();
        process.setLocation(someLocation());

        return process;
    }
}
