package businesslogic.event;

import javafx.collections.ObservableList;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class EventManager {
    public ObservableList<EventInfo> getAllEventInfo() {
        return EventInfo.loadAllEventInfo();
    }

    /* get the info of an event, start from the id */
    public ObservableList<EventInfo> getEventInfo(int event_id){
        return EventInfo.loadEventInfo(event_id);
    }
}
