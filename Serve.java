class Serve extends Event {
    private final double duration;
    private final Server server;

    Serve(double startingTime, Customer customer, double duration, Server server) {
        super(startingTime, customer);
        this.duration = duration;
        this.server = server;
    }

    @Override
    double getEndTime() {
        return this.getStartingTime() + this.duration;
    }

    @Override
    double countWaitingTime(double totalWaitingTime, Customer c) {
        return totalWaitingTime + c.getWaitingTime();
    }

    @Override
    int countServe(int numOfServe) {
        return numOfServe + 1;
    }


    
    @Override
    Pair<Event, ImList<Server>> nextEvent(double currentTime, 
            ImList<Server> servers, int numOfHuman) {
        Customer c = this.getCustomer();
        Server s = servers.get(this.server.getID() - 1);
        int i = s.getID();

        if (i > numOfHuman) { // self-check
            // update itself
            servers = this.updatingList(servers, i - 1, s);

            // update first self check
            Server firstSelf = servers.get(numOfHuman).removeFromWaitingQueue();
            servers = this.updatingList(servers, numOfHuman, firstSelf);

        } else { // human
            s = s.removeFromWaitingQueue();
            servers = this.updatingList(servers, i - 1, s);
            
        }

        Done doneEvent = new Done(this.getEndTime(), super.getCustomer(), this.server); 
        return new Pair<Event, ImList<Server>>(doneEvent, servers);
        
    }


    @Override
    public String toString() {
        return super.toString() + " serves by " + this.server.toString();
    }
}
