import java.util.function.Supplier;

class Done extends Event {

    Done(double timestamp, Customer customer, Server server, 
            boolean continuedWait, boolean continuedDone) {
        super(timestamp, customer, server, continuedWait, continuedDone);
    }

    public Pair<Event, ServeManager> nextEvent(ServeManager serveManager, 
            int qmax, Supplier<Double> restTimes) {
        ImList<Server> listOfServers = serveManager.getServerList();
        ImList<SelfCheckOut> selfCheckOutList = serveManager.getSelfCheckOutList();
        int queueSize = serveManager.getQueueSize();
        if (this.server.isHuman()) {
            Server updatedServer = listOfServers.get(this.server.getServerNum() - 1);
            //gen rest time for server
            double newDoneTime = this.timestamp + restTimes.get();
            Server modifiedServer =  new Server(this.server.getServerNum(),
                    newDoneTime, updatedServer.getQueueNum());
            //if (!this.continuedDone()) {
            listOfServers = listOfServers.set(this.server.getServerNum() - 1, modifiedServer);
            serveManager = new ServeManager(listOfServers, selfCheckOutList, queueSize); 
            Pair<Event, ServeManager> eventNManager = new Pair<>(
                    new Done(this.timestamp, this.customer, this.server, this.continuedWait, true),
                    serveManager);
            return eventNManager;
        } else {
            Pair<Event, ServeManager> eventNManager = new Pair<>(
                    new Done(this.timestamp, this.customer, 
                        this.server, this.continuedWait, this.continuedDone), serveManager);
            return eventNManager;
        }
    }

    public Statistics updateStatistics(Statistics statistics) {
        return statistics;
    }

    public String toString() {
        if (this.server.isHuman()) {
            return (String.format("%.3f",this.timestamp) +
                    " " + this.customer.getCustomerNum() + " done serving by " +
                    this.server.getServerNum());
        } else {
            return (String.format("%.3f",this.timestamp) +  
                    " " + this.customer.getCustomerNum() + " done serving by self-check " +
                    this.server.getServerNum());
        }
    }
}
