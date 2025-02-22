package devices.configuration.device;

import lombok.Builder;

@Builder
public record Settings(
        Boolean autoStart,
        Boolean remoteControl,
        Boolean billing,
        Boolean reimbursement,
        Boolean showOnMap,
        Boolean publicAccess) {

    public static Settings defaultSettings() {
        return Settings.builder()
                .autoStart(false)
                .remoteControl(false)
                .billing(false)
                .reimbursement(false)
                .showOnMap(false)
                .publicAccess(false).build();
    }

    public Settings merge(Settings given) {
        return Settings.builder()
                .autoStart(coalesce(given.autoStart, this.autoStart))
                .remoteControl(coalesce(given.remoteControl, this.remoteControl))
                .billing(coalesce(given.billing, this.billing))
                .reimbursement(coalesce(given.reimbursement, this.reimbursement))
                .showOnMap(coalesce(given.showOnMap, this.showOnMap))
                .publicAccess(coalesce(given.publicAccess, this.publicAccess))
                .build();
    }

    private static Boolean coalesce(Boolean given, Boolean that) {
        return given == null ? that : given;
    }
}
