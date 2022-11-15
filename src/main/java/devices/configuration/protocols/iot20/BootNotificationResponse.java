package devices.configuration.protocols.iot20;

import lombok.Value;

@Value
class BootNotificationResponse {
    String currentTime;
    int interval;
    Status status;

    enum Status {
        Accepted,
        Pending,
        Rejected
    }
}
