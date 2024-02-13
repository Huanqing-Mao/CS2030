class Arrival extends Event {

    Arrival(double startingTime, Customer customer) {
        super(startingTime, customer);
    }


    @Override
    Pair<Event, ImList<Server>> nextEvent(double currentTime, 
            ImList<Server> servers, int numOfHuman) {
        // then add logic from avail
        Customer c = this.getCustomer();
        double st = currentTime;

        // scan for available human servers
        for (int i = 0; i < numOfHuman; i++) { 
            Server s = servers.get(i);
            
            if (s.isNotOccupied(st) && s.getQueueLength() == 0) {
                // duration generated here!
                double duration = c.getServiceTime();
                                                     
                // generate a serve event.
                Event serveEvent = new Serve(st, c, duration, s);

                // update server list.
                double endingTime = serveEvent.getEndTime();
                Server newServer = s.toServe(endingTime); // may need to swap order with prv line

                servers = this.updatingList(servers, i, newServer);
                // System.out.println(servers);

                return new Pair<Event, ImList<Server>>(serveEvent, servers);
            } 
        }

        // scan for avail self check out:
        if (numOfHuman < servers.size()) {
            for (int i = numOfHuman; i < servers.size(); i++) {
                //System.out.println("Yes!");
                Server ss = servers.get(i);
                Server firstSelfCheck = servers.get(numOfHuman);
                if (ss.isNotOccupied(st) && firstSelfCheck.getQueueLength() == 0) {
                    //System.out.println("YES A!");
                    double duration = c.getServiceTime();
                    //System.out.println("Service time = " + duration);
                    // generate a serve event.
                    Event serveEvent = new Serve(st, c, duration, ss);
                    //System.out.println(serveEvent);
    
                    // update server list.
                    double endingTime = serveEvent.getEndTime();
                    //System.out.println(endingTime);
                    Server newServer = ss.toServe(endingTime);
                    
                    servers = this.updatingList(servers, i, newServer);
                    // System.out.println(servers);
    
                    return new Pair<Event, ImList<Server>>(serveEvent, servers);
    
                } 
    
            }

            for (int i = 0; i < numOfHuman + 1; i++) {
                Server s = servers.get(i);
    
                if (!s.queueFull()) {
                    // waiting duration
                    Event waitEvent = new Wait(st, c, s);
    
                    //update queue and server list
                    s = s.addToWQ();
                    servers = this.updatingList(servers, i, s);
                    return new Pair<Event, ImList<Server>>(waitEvent, servers);
                } 
            }

            return new Pair<Event, ImList<Server>>(new Leave(st, c), servers);
        
        }
        

        // scan for waiting servers until the first self-checkout queue

        for (int i = 0; i < servers.size(); i++) {
            Server s = servers.get(i);

            if (!s.queueFull()) {
                // waiting duration
                Event waitEvent = new Wait(st, c, s);

                //update queue and server list
                s = s.addToWQ();
                servers = this.updatingList(servers, i, s);
                return new Pair<Event, ImList<Server>>(waitEvent, servers);
            } 
        }



        // if not, leave
        return new Pair<Event, ImList<Server>>(new Leave(st, c), servers);
        
    }


    @Override
    public String toString() {
        return super.toString() + " arrives";
    }

}

