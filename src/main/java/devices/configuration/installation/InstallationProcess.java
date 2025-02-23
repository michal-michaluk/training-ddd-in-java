package devices.configuration.installation;

import lombok.Getter;

public class InstallationProcess {

    @Getter
    private final String orderId;
    @Getter
    private boolean finished;

    public InstallationProcess(WorkOrder workOrder) {
        orderId = workOrder.orderId();
        finished = false;
    }

}
