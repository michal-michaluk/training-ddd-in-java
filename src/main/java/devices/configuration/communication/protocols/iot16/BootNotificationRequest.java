package devices.configuration.communication.protocols.iot16;

record BootNotificationRequest(
        String chargePointVendor,
        String chargePointModel,
        String chargePointSerialNumber,
        String chargeBoxSerialNumber,
        String firmwareVersion,
        String iccid,
        String imsi,
        String meterType,
        String meterSerialNumber) {
}
