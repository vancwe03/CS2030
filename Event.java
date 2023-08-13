import java.util.function.Supplier;

public abstract class Event {
    protected final double timestamp;
    protected final Customer customer;
    protected final Server server;
    protected final boolean continuedWait;
    protected final boolean continuedDone;

    Event(double timestamp, Customer customer, Server server, 
            boolean continuedWait, boolean continuedDone) {
        this.timestamp = timestamp;
        this.customer = customer;
        this.server = server;
        this.continuedWait = continuedWait;
        this.continuedDone = continuedDone;
    }

    abstract Pair<Event, ServeManager> nextEvent(ServeManager serveManager, 
            int qmax, Supplier<Double> restTimes);

    abstract Statistics updateStatistics(Statistics statistics);

    //abstract ImList<Server> updateServerList(ImList<Server> listOfServers);

    public boolean isEarlier(Event event) {
        return this.timestamp < event.timestamp;
    }

    public boolean isLater(Event event) {
        return this.timestamp > event.timestamp;
    }

    public boolean beforeCustomer(Event event) {
        return this.customer.getCustomerNum() < event.customer.getCustomerNum();
    }

    public boolean afterCustomer(Event event) {
        return this.customer.getCustomerNum() > event.customer.getCustomerNum();
    }

    public Customer getCustomer() {
        return this.customer;
    }

    /** checks if the previous event is the same as the new event.
     */
    public boolean sameEvent(Event event) {
        if (event.timestamp == this.timestamp && 
                event.customer.equals(this.customer) && 
                event.server.equals(this.server) && 
                event.continuedWait == this.continuedWait) {
            return true;
        }
        return false;
    }

    /**checks if event is a continue waiting event.
     */
    public boolean continuedWait() {
        if (this.continuedWait) {
            return true;
        }
        return false;
    }

    public boolean continuedDone() {
        return this.continuedDone;
    }
    
}

