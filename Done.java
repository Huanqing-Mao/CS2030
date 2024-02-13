class Done extends Event {
    private final Server server;
    // private final int sequence; // 4

    Done(double startingTime, Customer customer, Server server) {
        super(startingTime, customer);
        this.server = server;
    }


    @Override
    Pair<Event, ImList<Server>> nextEvent(double currentTime, 
            ImList<Server> servers, int numOfHuman) {
        // update servers
        
        Customer c = this.getCustomer();
        Server s = servers.get(this.server.getID() - 1);
        // System.out.println("server = " + s);
        int i = s.getID();
        // s = s.removeFromWaitingQueue();
        s = s.serverAfterRest();
        // System.out.println("now server after rest = " + s);
        servers = this.updatingList(servers, i - 1, s);
        return new Pair<Event, ImList<Server>>(this, servers);
    }

    @Override
    public String toString() {
        return super.toString() + " done serving by " + this.server.toString();
    }
}
