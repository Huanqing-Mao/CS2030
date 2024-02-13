class StillWaiting extends Event {
    private final Server server;

    StillWaiting(double startingTime, Customer customer, 
            Server server) {
        super(startingTime, customer);
        this.server = server;

    }

    Server findQuickestServer(ImList<Server> servers) {
        ImList<Server> newServers = servers.sort(new ServerComparator());
        return newServers.get(0);
    }

    @Override 
    Pair<Event, ImList<Server>> nextEvent(double currentTime,
            ImList<Server> servers, int numOfHuman) {
        Customer c = this.getCustomer();
        Server s = servers.get(this.server.getID() - 1);
        // self-check:
        int id = this.server.getID();
        // double quickestTime = s.getAvailTime();
        if (id > numOfHuman) { // it's a self check out
            ImList<Server> selfChecks = new ImList<Server>();
            for (int j = numOfHuman; j < servers.size(); j++) {
                selfChecks = selfChecks.add(servers.get(j));
            }
            Server quickestServer = this.findQuickestServer(selfChecks);
            Server selfCheck = quickestServer;
            
            if (selfCheck.isNotOccupied(currentTime)) { 
                // can count waiting time here, put waiting time in customer property
                double waitingTime = currentTime - c.getArrivalTime();
                c = new Customer(c.getID(), c.getArrivalTime(), 
                        c.getServiceTimeProperty(), waitingTime);
                
                // update server list and queue list
                int i = selfCheck.getID();
                
                //generate a service time
                double duration = c.getServiceTime();
    
                // generate a serve event.
                Event serveEvent = new Serve(currentTime, c, duration, selfCheck);
    
                // update server list, and update the server's next avail time
                double endingTime = serveEvent.getEndTime();
                Server newServer = selfCheck.toServe(endingTime);
                
                servers = this.updatingList(servers, i - 1, newServer);

                return new Pair<Event, ImList<Server>>(serveEvent, servers);
            } else {
                // time == server's end service time
                double nextFreeTime = quickestServer.getAvailTime(); // get the quickest server
                Event waitEvent = new StillWaiting(nextFreeTime, 
                        this.getCustomer(), quickestServer);
                return new Pair<Event, ImList<Server>>(waitEvent, servers);
            }
        }





        // human:
        if (s.isNotOccupied(currentTime)) { 
            // can count waiting time here, put waiting time in customer property
            double waitingTime = currentTime - c.getArrivalTime();
            c = new Customer(c.getID(), c.getArrivalTime(), 
                    c.getServiceTimeProperty(), waitingTime);
            

            // update server list and queue list
            int i = s.getID();
            // remove the event from the waiting queue, 
            // s = s.removeFromWaitingQueue();
            
            //generate a service time
            double duration = c.getServiceTime();

            // generate a serve event.
            Event serveEvent = new Serve(currentTime, c, duration, s);

            // update server list, and update the server's next avail time
            double endingTime = serveEvent.getEndTime();
            Server newServer = s.toServe(endingTime);
            
            servers = this.updatingList(servers, i - 1, newServer);

            
            return new Pair<Event, ImList<Server>>(serveEvent, servers);

            // can retrieve customer from the event returned.

        } else {
            // time == server's end service time
            double nextFreeTime = s.getAvailTime();
            Event waitEvent = new StillWaiting(nextFreeTime, this.getCustomer(), this.server);
            return new Pair<Event, ImList<Server>>(waitEvent, servers);
        }
        
        
    }

    

    @Override
    boolean toPrint() {
        return false;
    }

    @Override
    public String toString() {
        return "Still waiting event";
    }
}