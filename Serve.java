import java.util.function.Supplier;

public class Serve extends Event {

    Serve(double timestamp, Customer customer, Server server, 
            boolean continuedWait, boolean continuedDone) {
        super(timestamp, customer, server, continuedWait, continuedDone);
    }

    /** returns done event.
     * update of server list.
     */
    public Pair<Event, ServeManager> nextEvent(ServeManager serveManager, 
            int qmax, Supplier<Double> restTimes) {
        ImList<Server> listOfServers = serveManager.getServerList();
        ImList<SelfCheckOut> selfCheckOutList = serveManager.getSelfCheckOutList();
        int queueSize = serveManager.getQueueSize();
        double customerDoneTime = this.timestamp + this.customer.getServiceTime();
        // human server
        if (this.server.isHuman()) {
            Server updatedServer = listOfServers.get(this.server.getServerNum() - 1); 
            if (updatedServer.getQueueNum() > 0) {
                Server modifiedServer = new Server(this.server.getServerNum(),
                        customerDoneTime, updatedServer.getQueueNum() - 1);
                listOfServers = listOfServers.set(this.server.getServerNum() - 1, modifiedServer);
            } else {
                Server modifiedServer = new Server(this.server.getServerNum(),
                        customerDoneTime, updatedServer.getQueueNum());
                listOfServers = listOfServers.set(this.server.getServerNum() - 1, modifiedServer);
            }
            ServeManager updatedServeManager = 
                new ServeManager(listOfServers, selfCheckOutList, queueSize);
            Pair<Event, ServeManager> eventNManager = new Pair<>(
                    new Done(customerDoneTime, this.customer, 
                        this.server, false, this.continuedDone), updatedServeManager);
            return eventNManager;
        } else {
            int position = this.server.getServerNum() - (listOfServers.size() + 1);
            Server updatedServer = selfCheckOutList.get(position);  
            SelfCheckOut modifiedServer = new SelfCheckOut(this.server.getServerNum(),
                    customerDoneTime, 0);
            ImList<SelfCheckOut> newSelfCheckOutList = 
                selfCheckOutList.set(position, modifiedServer);
            if (queueSize > 0) {
                ServeManager updatedServeManager = 
                    new ServeManager(listOfServers, newSelfCheckOutList, queueSize - 1);
                Pair<Event, ServeManager> eventNManager = new Pair<>(
                        new Done(customerDoneTime, this.customer, this.server,
                            false, this.continuedDone),
                        updatedServeManager);
                return eventNManager;
            } else {
                ServeManager updatedServeManager =
                    new ServeManager(listOfServers, newSelfCheckOutList, queueSize);
                Pair<Event, ServeManager> eventNManager = new Pair<>(
                        new Done(customerDoneTime, this.customer, this.server, 
                            false, this.continuedDone),
                        updatedServeManager);
                return eventNManager;
            }
        }
    }

    /** increase number of customers served.
     * increase total waiting time of customers.
     */
    public Statistics updateStatistics(Statistics statistics) {
        //increase customer served num 
        Statistics statistics1 = statistics.addToServed();
        //increase waiting time
        Statistics statistics2 = statistics1.increaseWaitingTime(
                this.timestamp - this.customer.getArrivalTime());
        return statistics2;
    }
    
    /** toString method.*/
    public String toString() {
        if (this.server.isHuman()) {
            return (String.format("%.3f",this.timestamp) + ' ' +
                    this.customer.getCustomerNum() + " serves by " + 
                    this.server.getServerNum());
        } else {
            return (String.format("%.3f",this.timestamp) + ' ' + 
                    this.customer.getCustomerNum() + " serves by self-check " + 
                    this.server.getServerNum());
        }
    }


}
