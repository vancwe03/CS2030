import java.util.function.Supplier;

public class Customer {
    protected final Supplier<Double> serviceTime;
    protected final double arrivalTime;
    protected final int customerNum;

    Customer(double arrivalTime, Supplier<Double> serviceTime, 
            int customerNum) {
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.customerNum = customerNum;
    }

    public double getArrivalTime() {
        return this.arrivalTime;
    }

    public double getServiceTime() {
        return this.serviceTime.get();
    }

    public int getCustomerNum() {
        return this.customerNum;
    } 

}
