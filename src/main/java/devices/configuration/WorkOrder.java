package devices.configuration;

import java.util.Objects;

public class WorkOrder {
    private final String orderId;
    private final Ownership ownership;
    private final boolean isFinished;

    public WorkOrder(String orderId, Ownership ownership, boolean isFinished) {
        this.orderId = orderId;
        this.ownership = ownership;
        this.isFinished = isFinished;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public boolean processNewOrder(WorkOrder newOrder) {
        return !this.equals(newOrder);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkOrder that = (WorkOrder) o;
        return isFinished == that.isFinished &&
                Objects.equals(orderId, that.orderId) &&
                Objects.equals(ownership, that.ownership);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, ownership, isFinished);
    }
}
