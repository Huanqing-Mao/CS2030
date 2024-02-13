import java.util.function.Supplier;

class Server {
    private final int n;
    private final double endService;
    private final int queueLength;
    private final int qmax;
    private final Supplier<Double> restTime;

    Server(int n, double leavingTime, int queueLength, int qmax, 
            Supplier<Double> restTime) {
        this.n = n;
        this.endService = leavingTime;
        this.queueLength = queueLength;
        this.qmax = qmax;
        this.restTime = restTime;
    }

    // basic getters:

    int getID() {
        return this.n;
    }

    double getAvailTime() {
        return this.endService;
    }

    boolean isNotOccupied(double currentTime) {
        return (currentTime >= this.endService);
    }

    int getQueueLength() {
        return this.queueLength;
    }
    
    int getQueueMax() {
        return this.qmax;
    }

    Supplier<Double> getRestTimeProperty() {
        return this.restTime;
    }

    double getRestTime() {
        return this.restTime.get();
    }

    boolean queueFull() {
        return this.queueLength >= this.qmax;
    }

    // updating Waiting Queues:

    Server addToWQ() {
        return new Server(this.n, this.endService, this.queueLength + 1, this.qmax, this.restTime);
    }

    Server removeFromWaitingQueue() {
        if (this.queueLength >= 1) {
            return new Server(this.n, this.endService, this.queueLength - 1, 
                    this.qmax, this.restTime);
        } else {
            return new Server(this.n, this.endService, 0, this.qmax, this.restTime);
        }
        
    }

    Server serverAfterRest() {
        return new Server(this.n, this.endService + this.getRestTime(),
                this.queueLength, this.qmax, this.restTime);
    }

    /*Server toServe(Event serve) {
        double leavingTime = serve.getEndTime();
        return new Server(this.n, leavingTime, this.queueLength, this.qmax);
    }*/

    Server toServe(double leavingTime) {
        return new Server(this.n, leavingTime, this.queueLength, this.qmax, this.restTime);
    }



    public String toString() {
        return String.format("%d", this.n);
        // return String.format("Server (%d, %.3f, %d, %d)", 
        // this.n, this.endService, this.queueLength, this.qmax);
    }

 
}
