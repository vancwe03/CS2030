import java.util.function.Supplier;

public class Simulator {
    private final int numOfServers;
    private final ImList<Double> arrivalTimes;
    private final Supplier<Double> serviceTime;
    private final int qmax;
    private final Supplier<Double> restTimes;
    private final int numSelfCheckout;

    Simulator(int numOfServers, int numSelfCheckout, int qmax,
            ImList<Double> arrivalTimes, Supplier<Double> serviceTime, Supplier<Double> restTimes) {
        this.numOfServers = numOfServers;
        this.arrivalTimes = arrivalTimes;
        this.serviceTime = serviceTime;
        this.qmax = qmax;
        this.restTimes = restTimes;
        this.numSelfCheckout = numSelfCheckout;
    }
    
    /**
     simulate a shop whereby if there are available servers, customer is served. 
     * else customer leaves
     * after serving done event is created
     */
    public String simulate() {
        //Create list of servers
        ImList<Server> listOfServers = new ImList<Server>();
        for (int j = 1; j < this.numOfServers + 1; j++) {
            listOfServers = listOfServers.add(new Server(j, 0, 0));
        }
        //Create list of selfcheckouts
        ImList<SelfCheckOut> selfCheckOutList = new ImList<SelfCheckOut>();
        for (int j = 1; j < this.numSelfCheckout + 1; j++) {
            selfCheckOutList = selfCheckOutList.add(new SelfCheckOut(numOfServers + j, 0, 0));
        }
        //Creat ServeManager
        ServeManager serveManager = new ServeManager(listOfServers, selfCheckOutList, 0);
        //Create list of customers
        ImList<Customer> listOfCustomers = new ImList<Customer>();
        for (int i = 0; i < this.arrivalTimes.size(); i++) {
            listOfCustomers = listOfCustomers.add(new Customer(
            this.arrivalTimes.get(i), this.serviceTime, i + 1));
        }
        //Add arrival events into priority queue
        PQ<Event> pq = new PQ<>(new EventComparator());
        for (int i = 0; i < this.arrivalTimes.size(); i++) {
            pq = pq.add(new Arrival(this.arrivalTimes.get(i), 
                        listOfCustomers.get(i), 
                        new Server(0, 0, 0), false, false));    
        }
        String output = "";
        Statistics statistics = new Statistics(0, 0, 0);
        while (!pq.isEmpty()) {
            Pair<Event, PQ<Event>> pr = pq.poll();
            Event topEvent = pr.first();
            pq = pr.second();
            if (topEvent.continuedDone()) {
                continue;
            }
            Pair<Event, ServeManager> nextEvent = 
                topEvent.nextEvent(serveManager, this.qmax, this.restTimes);
            Event newEvent = nextEvent.first();
            statistics = topEvent.updateStatistics(statistics);
            serveManager = nextEvent.second();
            if (!topEvent.continuedWait() && !topEvent.continuedDone()) { 
                if (output == "") {
                    output += topEvent;
                } else {
                    output += "\n" + topEvent;
                }
            }
            if (newEvent.continuedWait() || newEvent.continuedDone()) {
                pq = pq.add(newEvent);
                continue;
            }
            if (topEvent.sameEvent(newEvent)) {
                continue;
            } else {
                pq = pq.add(newEvent);
            }
        }
        output += "\n" + statistics;
        return output;
    }
}
