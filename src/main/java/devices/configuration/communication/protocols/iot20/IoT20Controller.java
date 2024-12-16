package devices.configuration.communication.protocols.iot20;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.Instant;

import static devices.configuration.communication.protocols.iot20.BootNotificationResponse.Status.Accepted;

@RestController
@RequiredArgsConstructor
class IoT20Controller {

    private final Clock clock;

    @PostMapping(path = "/protocols/iot20/bootnotification/{deviceId}",
            consumes = "application/json", produces = "application/json")
    BootNotificationResponse handleBootNotification(@PathVariable String deviceId,
                                                    @RequestBody BootNotificationRequest request) {
        return BootNotificationResponse.builder()
                .currentTime(Instant.now(clock).toString())
                .interval(1800)
                .status(Accepted)
                .build();
    }
}
