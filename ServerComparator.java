import java.util.Comparator;

class ServerComparator implements Comparator<Server> {
    // time >> compare ID
    public int compare(Server s1, Server s2) {
        if (s1.getAvailTime() == s2.getAvailTime()) {
            return s1.getID() - s2.getID();
        } else if (s1.getAvailTime() - s2.getAvailTime() < 0) {
            return -1;
        } else {
            return 1;
        }
    }
}