package devices.configuration.installation;

import java.util.HashSet;
import java.util.Set;

public class InstallationProcess {

    public static final Set<String> existingOrders = new HashSet<>();

    private final String orderId;

    public InstallationProcess(String orderId) {
        if (existingOrders.contains(orderId)) {
            throw new IllegalStateException("Work order already exists!");
        }

        this.orderId = orderId;
        existingOrders.add(orderId);
    }
}
