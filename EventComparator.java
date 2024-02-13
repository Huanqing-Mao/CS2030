import java.util.Comparator;

class EventComparator implements Comparator<Event> {
    // time >> compare ID
    public int compare(Event e1, Event e2) {
        if (e1.getStartingTime() == e2.getStartingTime()) {
            return e1.getCustomer().getID() - e2.getCustomer().getID();
        } else if (e1.getStartingTime() - e2.getStartingTime() < 0) {
            return -1;
        } else {
            return 1;
        }
    }
}

