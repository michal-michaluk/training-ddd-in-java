package devices.configuration.communication.protocols.iot20;

record BootNotificationRequest(
        Device device,
        Reason reason) {

    record Device(
            String serialNumber,
            String model,
            Modem modem,
            String vendorName,
            String firmwareVersion) {
    }

    record Modem(String iccid, String imsi) {
    }

    enum Reason {
        ApplicationReset,
        FirmwareUpdate,
        LocalReset,
        PowerUp,
        RemoteReset,
        ScheduledReset,
        Triggered,
        Unknown,
        Watchdog
    }
}
