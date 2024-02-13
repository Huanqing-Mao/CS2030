class Event {
    private final double startingTime;
    private final Customer customer;

    Event(double startingTime, Customer customer) {
        this.startingTime = startingTime;
        this.customer = customer;
    }

    // basic getters:

    double getStartingTime() {
        return this.startingTime;
    }

    double getEndTime() {
        return this.startingTime + 0;
    }

    Customer getCustomer() {
        return this.customer;
    }

    
    // Counters:

    
    int countServe(int numOfServe) {
        return numOfServe;
    }


    /*boolean isLeaving() {
        return false;
    }*/
    
    double countWaitingTime(double totalWaitingTime, Customer c) {
        return totalWaitingTime;
    }

    int countLeave(int numOfLeave) {
        return numOfLeave;
    }

    boolean toPrint() {
        return true;
    }

    // modifiers:
    // will be overriden:

    Pair<Event, ImList<Server>> nextEvent(double currentTime, 
            ImList<Server> servers, int numOfHuman) {
        return new Pair<Event, ImList<Server>>(this, servers);
    }

    

    // update servers in the list

    ImList<Server> updatingList(ImList<Server> originalList, int index, Server newServer) {
        ImList<Server> newList = originalList.set(index, newServer);
        return newList;
    }


    @Override 
    public String toString() {
        return String.format("%.3f %d", this.startingTime, this.customer.getID());
    }
    
}
