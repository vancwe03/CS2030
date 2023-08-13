import java.util.function.Supplier;

public class Arrival extends Event {

    Arrival(double timestamp, Customer customer, Server server, 
            boolean continuedWait, boolean continuedDone) {
        super(timestamp, customer, server, continuedWait, continuedDone);
    }

    /** if there is a free server, serve event is generated.
     * else if there is a space in a queue, wait is generated.
     * else customer leaves.
     */
    public Pair<Event, ServeManager> nextEvent(ServeManager serveManager, 
            int qmax, Supplier<Double> restTimes) {
        ImList<Server> listOfServers = serveManager.getServerList();
        // check for free server
        for (int i = 0; i < listOfServers.size(); i++) {
            Server newServer = listOfServers.get(i);
            if (newServer.canServe(this.customer)) {
                Pair<Event, ServeManager> eventNManager = new Pair<>(
                        new Serve(this.timestamp, this.customer, 
                            newServer, this.continuedWait, this.continuedDone), serveManager);
                return eventNManager;
            }
        }    
        // check for free selfcheckout
        ImList<SelfCheckOut> selfCheckOutList = serveManager.getSelfCheckOutList();
        if (selfCheckOutList.size() > 0) {
            for (int i = 0; i < selfCheckOutList.size(); i++) {
                SelfCheckOut newServer = selfCheckOutList.get(i);
                if (newServer.canServe(this.customer)) {
                    Pair<Event, ServeManager> eventNManager = new Pair<>(
                            new Serve(this.timestamp, this.customer,
                                newServer, this.continuedWait, this.continuedDone), serveManager);
                    return eventNManager;
                }
            }
        }
        // check for space in human servers' queue
        for (int i = 0; i < listOfServers.size(); i++) {
            Server newServer = listOfServers.get(i);
            if (newServer.queueHasSpace(qmax)) {
                Pair<Event, ServeManager> eventNManager =
                    new Pair<>(new Wait(this.timestamp, this.customer,
                                newServer, this.continuedWait,
                                this.continuedDone), serveManager);
                return eventNManager;
            }
        }
        // check for space in selfcheckout's queue
        if (selfCheckOutList.size() > 0) {
            if (serveManager.availSelfCheckQueue(qmax)) {
                SelfCheckOut newServer = selfCheckOutList.get(0);
                Pair<Event, ServeManager> eventNManager =
                    new Pair<>(new Wait(this.timestamp, this.customer,
                                newServer, this.continuedWait,
                                this.continuedDone), serveManager);
                return eventNManager;
            }
        }
        Pair<Event, ServeManager> eventNManager = 
            new Pair<>(new Leave(this.timestamp, this.customer, 
                        new Server(1, 0, 0), 
                        this.continuedWait, this.continuedDone), serveManager);
        return eventNManager;
    }

    public Statistics updateStatistics(Statistics statistics) {
        return statistics;
    }
    
    public String toString() {
        return (String.format("%.3f",this.timestamp) + 
                " " + this.customer.getCustomerNum() + " arrives");
    }
}
