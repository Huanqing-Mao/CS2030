class Leave extends Event {
    
    Leave(double startingTime, Customer customer) {
        super(startingTime, customer);
    }


    @Override
    Pair<Event, ImList<Server>> nextEvent(double currentTime, 
            ImList<Server> servers, int numOfHuman) {
        return new Pair<Event, ImList<Server>>(this, servers);
    }

    @Override
    int countLeave(int numOfLeave) {
        return numOfLeave + 1;
    }

    

    @Override
    public String toString() {
        return super.toString() + " leaves";
    }
}
