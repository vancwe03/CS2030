import java.util.Comparator;

class EventComparator implements Comparator<Event> {
    
    public int compare(Event a, Event b) {
        if (a.isEarlier(b)) {
            return -1;
        } else if (a.isLater(b)) {
            return 1;
        } else if (a.beforeCustomer(b)) {
            return -1;
        } else if (a.afterCustomer(b)) {
            return 1;
        } else {
            return 0;
        }
    }
}
