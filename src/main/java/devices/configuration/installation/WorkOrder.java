package devices.configuration.installation;

import devices.configuration.device.Ownership;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record WorkOrder(
        @NotNull String orderId,
        @NotNull Ownership ownership) {
}
