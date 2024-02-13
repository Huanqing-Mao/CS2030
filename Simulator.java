import java.util.function.Supplier;

class Simulator {
    private final int numOfServers;
    // number of self-checkouts = 0
    private final int numOfSelfCheckouts;
    private final int qmax;
    private final ImList<Double> arrivalTimes;
    private final Supplier<Double> serviceTime;
    // resttime
    private final Supplier<Double> restTimes;

    Simulator(int numOfServers, int numOfSelfCheckouts, int qmax, ImList<Double> arrivalTimes, 
            Supplier<Double> serviceTime, Supplier<Double> restTimes) {
        this.numOfServers = numOfServers;
        this.numOfSelfCheckouts = numOfSelfCheckouts;
        this.arrivalTimes = arrivalTimes;
        this.qmax = qmax;
        this.serviceTime = serviceTime;
        this.restTimes = restTimes;
    }


    ImList<Server> makingServerList() {
        ImList<Server> createServers = new ImList<Server>();
        for (int i = 1; i <= this.numOfServers; i += 1) {
            createServers = createServers.add(new Server(i, 0.0, 0, this.qmax, this.restTimes));
        }

        //System.out.println("Self checkouts = " + this.numOfSelfCheckouts);

        for (int i = this.numOfServers + 1; i <= this.numOfServers + this.numOfSelfCheckouts; i++) {
            createServers = createServers.add(new SelfCheck(i, 0.0, 0, this.qmax, this.restTimes));
        }
        return createServers;
    }

    PQ<Event> makingArrivalEvents() {
        PQ<Event> events = new PQ<Event>(new EventComparator());
        for (int i = 0; i < this.arrivalTimes.size(); i++) {
            double arrivalTime = this.arrivalTimes.get(i);
            int customerNo = i + 1;
            Event e = new Arrival(arrivalTime, 
                    new Customer(customerNo, arrivalTime, this.serviceTime, 0.0));
            events = events.add(e);
        }

        return events;

    }



    String simulate() {
        // initialise the lists:
        ImList<Server> servers = this.makingServerList();
        //System.out.println(servers);
        PQ<Event> events = this.makingArrivalEvents();
        // may need another list for printing?
        ImList<Event> printing = new ImList<Event>();

        // waiting Time counter:
        // another variable that counts no of waiting customers
        // int numOfWaitingCustomers = 0;
        // another variable of waiting time, cal their total waiting time by division.
        double totalWaitingTime = 0;

        // serve counter:
        int numOfServe = 0;

        // leave counter:
        int numOfLeave = 0;


        while (!events.isEmpty()) {
            // Each loop:
            // poll first
            //System.out.println("\nPQ: " + events);
            //System.out.println(servers);
            Event e = events.poll().first();
            PQ<Event> restEvents = events.poll().second();

            //System.out.println("Target Event: " + e);

            Customer c = e.getCustomer();

            

            // change this part to polymorphism
            /*if (e.isServing()) {
                totalWaitingTime += c.getWaitingTime();
                numOfServe += 1;
            } else if (e.isLeaving()) {
                numOfLeave += 1;
            }*/

            totalWaitingTime = e.countWaitingTime(totalWaitingTime, c);
            numOfServe = e.countServe(numOfServe);
            numOfLeave = e.countLeave(numOfLeave);


            // printing:
            printing = printing.add(e);

            // get next event + resulting PQ + resulting servers
            Pair<Event, ImList<Server>> resultPair = e.nextEvent(e.getStartingTime(), 
                    servers, numOfServers);
            Event nextE = resultPair.first();
            //System.out.println("Next event: " + nextE);

            ImList<Server> updatedServers = resultPair.second();
            //System.out.println("Updated servers: " + updatedServers);


            // pump next event to PQ, if same object dont pump
            if (e.equals(nextE)) {
                events = restEvents;
                

            } else {
                //update events
                events = restEvents.add(nextE);


            }

            // counters:

            // servers = updated servers
            servers = updatedServers;






            // update waiting time, retrieve waiting time
            


        }
        String s = "";
        // print messages + counters
        for (Event e : printing) {
            if (e.toPrint()) {
                s += e.toString() + "\n";
            }      
        }


        double averageWaitingTime = 1.0 * totalWaitingTime / numOfServe;
        if (numOfServe == 0) {
            averageWaitingTime = 0.0;
        }

        s += String.format("[%.3f %d %d]", averageWaitingTime, numOfServe, numOfLeave);
        return s;

        
    }




    @Override
    public String toString() {
        return "Simulator :D";
    }
}
