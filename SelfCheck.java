import java.util.function.Supplier;

class SelfCheck extends Server {

    
    SelfCheck(int n, double leavingTime, int queueLength, int qmax, Supplier<Double> restTime) {
        super(n, leavingTime, queueLength, qmax, restTime);

    }


    @Override
    double getRestTime() {
        return 0.0;
    }

    @Override
    SelfCheck serverAfterRest() {
        return this;
    }

    @Override
    SelfCheck toServe(double leavingTime) {
        return new SelfCheck(this.getID(), leavingTime, 
                this.getQueueLength(), this.getQueueMax(), this.getRestTimeProperty());
    }

    SelfCheck addToWQ() {
        return new SelfCheck(this.getID(), this.getAvailTime(), 
                this.getQueueLength() + 1, this.getQueueMax(), this.getRestTimeProperty());
    }

    SelfCheck removeFromWaitingQueue() {
        if (this.getQueueLength() >= 1) {
            return new SelfCheck(this.getID(), this.getAvailTime(), 
                    this.getQueueLength() - 1, this.getQueueMax(), this.getRestTimeProperty());
        } else {
            return new SelfCheck(this.getID(), this.getAvailTime(), 
                    0, this.getQueueMax(), this.getRestTimeProperty());
        }
        
    }

    /*Server toServe(Event serve) {
        double leavingTime = serve.getEndTime();
        return new Server(this.n, leavingTime, this.queueLength, this.qmax);
    }*/

    @Override
    public String toString() {
        return String.format("self-check %d", this.getID());
        //return "self-check " + super.toString();

    }
    
}
