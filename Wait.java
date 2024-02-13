class Wait extends Event {
    private final Server server;

    Wait(double startingTime, Customer customer, 
            Server server) {
        // default waiting Duration = 0
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
        int id = this.server.getID();
        // System.out.println("id = " + id);
        // double quickestTime = s.getAvailTime();
        if (id > numOfHuman) { // it's a self check out
            ImList<Server> selfChecks = new ImList<Server>();
            // generate a list of self-check outs
            for (int j = numOfHuman; j < servers.size(); j++) {
                selfChecks = selfChecks.add(servers.get(j));
            }

            // find quickest server
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
                // System.out.println("New self check server: " + newServer);
                
                servers = this.updatingList(servers, i - 1, newServer);

                return new Pair<Event, ImList<Server>>(serveEvent, servers);
            } else {
                double nextFreeTime = quickestServer.getAvailTime(); // get the quickest server
                Event waitEvent = new StillWaiting(nextFreeTime, 
                        this.getCustomer(), quickestServer);
                return new Pair<Event, ImList<Server>>(waitEvent, servers);
            }
        }
            
        // it's human check out
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


        } else {
            // time == server's end service time
            double nextFreeTime = s.getAvailTime();
            Event waitEvent = new StillWaiting(nextFreeTime, this.getCustomer(), this.server);
            return new Pair<Event, ImList<Server>>(waitEvent, servers);
        }


    }
    

    @Override
    public String toString() {
        return super.toString() + " waits at " + this.server.toString();
    }


}
