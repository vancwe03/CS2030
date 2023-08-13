public class Statistics {
    private final int numOfCustomersServed;
    private final int numOfCustomersLeave;
    private final double totalWaitingTime;

    Statistics(int numOfCustomersServed, int numOfCustomersLeave, double totalWaitingTime) {
        this.numOfCustomersServed = numOfCustomersServed;
        this.numOfCustomersLeave = numOfCustomersLeave;
        this.totalWaitingTime = totalWaitingTime;
    }

    public Statistics addToServed() {
        return new Statistics(this.numOfCustomersServed + 1, 
                this.numOfCustomersLeave, this.totalWaitingTime);
    }

    public Statistics addToLeave() {
        return new Statistics(this.numOfCustomersServed, 
                this.numOfCustomersLeave + 1, this.totalWaitingTime);
    }

    public Statistics increaseWaitingTime(double time) {
        return new Statistics(this.numOfCustomersServed, 
                this.numOfCustomersLeave, this.totalWaitingTime + time);
    }

    /** prints the average waiting time.
     * prints num customers served.
     * prints num customers who left.
     */
    public String toString() {
        if (totalWaitingTime == 0) {
            return "[" + String.format("%.3f", totalWaitingTime) + " " +
                numOfCustomersServed + " " + numOfCustomersLeave + "]";
        }
        double averageWaitingTime = totalWaitingTime / numOfCustomersServed;
        return "[" + String.format("%.3f", averageWaitingTime) + " " +
            numOfCustomersServed + " " + numOfCustomersLeave + "]";
    } 
}
