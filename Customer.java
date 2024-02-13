import java.util.function.Supplier;

class Customer {
    private final int number;
    private final double arrivalTime;
    private final Supplier<Double> serviceTime;
    private final double waitingTime;

    Customer(int number, double arrivalTime, Supplier<Double> serviceTime, double waitingTime) {
        this.number = number;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.waitingTime = waitingTime;
    }

    int getID() {
        return this.number;
    }

    Supplier<Double> getServiceTimeProperty() {
        return this.serviceTime;
    }

    double getServiceTime() {
        return this.serviceTime.get();
    }

    double getArrivalTime() {
        return this.arrivalTime;
    }

    double getWaitingTime() {
        return this.waitingTime;
    }

    public String toString() {
        return "customer" + this.number;
    }

}
