public class ServeManager {
    private final ImList<Server> listOfServers;
    private final ImList<SelfCheckOut> selfCheckOutList;
    private final int queueSize;

    ServeManager(ImList<Server> listOfServers, 
            ImList<SelfCheckOut> selfCheckOutList, int queueSize) {
        this.listOfServers = listOfServers;
        this.selfCheckOutList = selfCheckOutList;
        this.queueSize = queueSize;
    }

    public ImList<Server> getServerList() {
        return this.listOfServers;
    }

    public ImList<SelfCheckOut> getSelfCheckOutList() {
        return this.selfCheckOutList;
    }

    public boolean availSelfCheckQueue(int qmax) {
        return this.queueSize < qmax;
    }

    public int getQueueSize() {
        return this.queueSize;
    }

    /** finds next free selfcheckout.*/
    public SelfCheckOut nextFreeSelfCheck() {
        SelfCheckOut nextFree = selfCheckOutList.get(0);
        for (int i = 1; i < selfCheckOutList.size(); i++) {
            if (selfCheckOutList.get(i).doneTime() < nextFree.doneTime()) {
                nextFree = selfCheckOutList.get(i);
            }
        }
        return nextFree;
    }
}

// has a list of self checkouts and one queue
