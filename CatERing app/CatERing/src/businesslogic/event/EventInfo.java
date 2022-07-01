package businesslogic.event;

import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.security.Provider;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EventInfo implements EventItemInfo {
    private int id;
    private String name;
    private Date date_Start;
    private Date date_End;
    private int participants;
    private User organizer;

    private ObservableList<ServiceInfo> services;

    public EventInfo(String name) {
        this.name = name;
        id = 0;
    }

    public ObservableList<ServiceInfo> getServices() {
        return FXCollections.unmodifiableObservableList(this.services);
    }

    /* check if event is assigned to the user */
    public boolean isAssignedUser(User user){
        return user.getId() == organizer.getId();
    }

    /* check if event has a specific service */
    public boolean hasService(ServiceInfo service){
        for(ServiceInfo s: services){
            if(s.getId()==service.getId())
                return true;
        }
        return false;
    }

    public String toString() {
        return name + ": " + date_Start + "-" + date_End + ", " + participants + " pp. (" + organizer.getUserName() + ")";
    }

    // STATIC METHODS FOR PERSISTENCE

    public static ObservableList<EventInfo> loadAllEventInfo() {
        ObservableList<EventInfo> all = FXCollections.observableArrayList();
        String query = "SELECT * FROM Events WHERE true";
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                String n = rs.getString("name");
                EventInfo e = new EventInfo(n);
                e.id = rs.getInt("id");
                e.date_Start = rs.getDate("date_start");
                e.date_End = rs.getDate("date_end");
                e.participants = rs.getInt("expected_participants");
                int org = rs.getInt("organizer_id");
                e.organizer = User.loadUserById(org);
                all.add(e);
            }
        });

        for (EventInfo e : all) {
            e.services = ServiceInfo.loadServiceInfoForEvent(e.id);
        }
        return all;
    }

    /* load the info of an event, start from the id */
    public static ObservableList<EventInfo> loadEventInfo(int event_id) {
        ObservableList<EventInfo> e = FXCollections.observableArrayList();
        String query = "SELECT * FROM Events WHERE id = " + event_id;
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                String n = rs.getString("name");
                EventInfo event = new EventInfo(n);
                event.id = rs.getInt("id");
                event.date_Start = rs.getDate("date_start");
                event.date_End = rs.getDate("date_end");
                event.participants = rs.getInt("expected_participants");
                int org = rs.getInt("organizer_id");
                event.organizer = User.loadUserById(org);
                e.add(event);
            }
        });

        e.get(0).services = ServiceInfo.loadServiceInfoForEvent(e.get(0).id);
        return e;
    }
}
