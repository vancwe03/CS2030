public class Server {
    protected final double totalTime;
    protected final int serverNum;
    protected final int queueNum;
   
    Server(int serverNum, double totalTime, int queueNum) {
        this.serverNum = serverNum;
        this.totalTime = totalTime;
        this.queueNum = queueNum;
    }
   
    public int getServerNum() {
        return this.serverNum;
    }

    public int getQueueNum() {
        return this.queueNum;
    }

    // method to check if server can serve customer
    
    public boolean canServe(Customer customer) {
        return this.totalTime <= customer.getArrivalTime(); 
    }

    /** checks if server queue has space.
     */
    public boolean queueHasSpace(int qmax) {
        if (qmax > this.queueNum) {
            return true;
        }
        return false;
    }

    public boolean isHuman() {
        return true;
    }

    public double doneTime() {
        return this.totalTime;
    }

}
