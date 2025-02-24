package installation;

import devices.configuration.device.Location;
import devices.configuration.device.Ownership;
import devices.configuration.installation.BootNotification;
import devices.configuration.installation.InstallationProcess;
import devices.configuration.installation.WorkOrder;

import java.math.BigDecimal;
import java.util.function.Consumer;

public class InstallationProcessFixture {

    static InstallationProcess given(WorkOrder workOrder, Consumer<InstallationProcess> customize) {
        var process = InstallationProcess.create(workOrder);
        customize.accept(process);
        return process;
    }


    public static BootNotification ultraChargeBootNotification(String deviceId) {
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
                .coordinates(
                        new Location.Coordinates(
                                new BigDecimal("16.931752852309156"),
                                new BigDecimal("51.09836221719513")))
                .build();
    }

    public static Ownership someOwnership() {
        return Ownership.builder()
                .operator("Devicex.nl")
                .provider("public-devices")
                .build();
    }
}
