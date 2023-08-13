import java.util.function.Supplier;

public class Leave extends Event {

    Leave(double timestamp, Customer customer, Server server, 
            boolean continuedWait, boolean continuedDone) {
        super(timestamp, customer, server, continuedWait, continuedDone);
    }

    /** returns Leave event and list of Servers.
     */
    public Pair<Event, ServeManager> nextEvent(ServeManager serveManager, 
            int qmax, Supplier<Double> restTimes) {
        Pair<Event, ServeManager> eventNManager = new Pair<>(
                new Leave(this.timestamp, this.customer, this.server, 
                    this.continuedWait, this.continuedDone),
                serveManager);
        return eventNManager;
    }

    public Statistics updateStatistics(Statistics statistics) {
        Statistics updatedStatistics = statistics.addToLeave();
        return updatedStatistics;
    }

    public String toString() {
        return (String.format("%.3f",this.timestamp) +
               " " + this.customer.getCustomerNum() + " leaves");
    }
}
