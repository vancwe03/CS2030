import java.util.function.Supplier;

public class Wait extends Event {
    //private final int count;

    Wait(double timestamp, Customer customer, 
            Server server, boolean continuedWait, boolean continuedDone) {
        super(timestamp, customer, server, continuedWait, continuedDone);
        //this.count = count;
    }    

    /** next event is a serve event if customer is first in queue.
     * else next event is another wait event.
     * update of server list.
     */
    public Pair<Event, ServeManager> nextEvent(ServeManager serveManager, 
            int qmax, Supplier<Double> restTimes) {
        ImList<Server> listOfServers = serveManager.getServerList();
        ImList<SelfCheckOut> selfCheckOutList = serveManager.getSelfCheckOutList();
        int queueSize = serveManager.getQueueSize();
        // if its first wait, increase num of cus in queue.
        if (this.server.isHuman()) {
            Server updatedServer = listOfServers.get(this.server.getServerNum() - 1);
            double servingTime = updatedServer.doneTime();
            if (!this.continuedWait) {
                Server modifiedServer = new Server(updatedServer.getServerNum(),
                        updatedServer.doneTime(), updatedServer.getQueueNum() + 1);
                listOfServers = listOfServers.set(this.server.getServerNum() - 1, modifiedServer);
                ServeManager newServeManager = 
                    new ServeManager(listOfServers, selfCheckOutList, queueSize);
                if (this.timestamp < servingTime) {
                    Pair<Event, ServeManager> eventNManager = new Pair<>(new Wait(servingTime,
                                this.customer, updatedServer, true,
                                this.continuedDone), newServeManager);
                    return eventNManager;
                } else {
                    Pair<Event, ServeManager> eventNManager = new Pair<>(new Serve(servingTime,
                                this.customer, updatedServer, false,
                                this.continuedDone), newServeManager);
                    return eventNManager;
                }
            } else {
                ServeManager newServeManager = serveManager;
                if (this.timestamp < servingTime) {
                    Pair<Event, ServeManager> eventNManager = new Pair<>(new Wait(servingTime,
                                this.customer, updatedServer, true,
                                this.continuedDone), newServeManager);
                    return eventNManager;
                } else {
                    Pair<Event, ServeManager> eventNManager = new Pair<>(new Serve(servingTime,
                                this.customer, updatedServer, false,
                                this.continuedDone), newServeManager);
                    return eventNManager; 
                }
            }
        } else {
            SelfCheckOut updatedServer = serveManager.nextFreeSelfCheck();
            double servingTime = updatedServer.doneTime();
            if (!this.continuedWait) {
                ServeManager newServeManager = 
                    new ServeManager(listOfServers, selfCheckOutList, queueSize + 1);
                if (this.timestamp < servingTime) {
                    Pair<Event, ServeManager> eventNManager = new Pair<>(new Wait(servingTime,
                                this.customer, updatedServer, true,
                                this.continuedDone), newServeManager);
                    return eventNManager;
                } else {
                    Pair<Event, ServeManager> eventNManager = new Pair<>(new Serve(servingTime,
                                this.customer, updatedServer, false,
                                this.continuedDone), newServeManager);
                    return eventNManager;
                }
            } else {
                ServeManager newServeManager = serveManager;
                if (this.timestamp < servingTime) {
                    Pair<Event, ServeManager> eventNManager = new Pair<>(new Wait(servingTime,
                                this.customer, updatedServer, true,
                                this.continuedDone), newServeManager);
                    return eventNManager;
                } else {
                    Pair<Event, ServeManager> eventNManager = new Pair<>(new Serve(servingTime, 
                                this.customer, updatedServer, false, 
                                this.continuedDone), newServeManager);
                    return eventNManager;
                }
            }
        } 
    }
    
    public Statistics updateStatistics(Statistics statistics) {
        return statistics;
    }

    /** toString method.*/
    public String toString() {
        if (this.server.isHuman()) {
            return String.format("%.3f", this.timestamp) + " " + this.customer.getCustomerNum() + 
                " waits at " + this.server.getServerNum();
        } else {
            return String.format("%.3f", this.timestamp) + " " + this.customer.getCustomerNum() +
                " waits at self-check " + this.server.getServerNum();
        }
    }
}
