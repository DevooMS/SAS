package businesslogic.event;

import businesslogic.menu.Menu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

public class ServiceInfo implements EventItemInfo {

    private int id;
    private int menu_id;
    private String name;
    private Date date;
    private Time time_Start;
    private Time time_End;
    private int participants;

    public ServiceInfo(String name) {
        this.name = name;
    }

    /* get the id of the items of a menu */
    public ArrayList<Integer> getServiceMenuItems() {
        ArrayList<Integer> menu_items = new ArrayList<>();

        for(int item_id: Menu.getMenuItems(this.menu_id)) {
            menu_items.add(item_id);
        }

        return menu_items;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return name + ": " + date + " (" + time_Start + "-" + time_End + "), " + participants + " pp.";
    }

    // STATIC METHODS FOR PERSISTENCE

    public static ObservableList<ServiceInfo> loadServiceInfoForEvent(int event_id) {
        ObservableList<ServiceInfo> result = FXCollections.observableArrayList();
        String query = "SELECT id, menu_id, name, service_date, time_start, time_end, expected_participants " +
                "FROM Services WHERE event_id = " + event_id;
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                String s = rs.getString("name");
                ServiceInfo serv = new ServiceInfo(s);
                serv.id = rs.getInt("id");
                serv.menu_id = rs.getInt("menu_id");
                serv.date = rs.getDate("service_date");
                serv.time_Start = rs.getTime("time_start");
                serv.time_End = rs.getTime("time_end");
                serv.participants = rs.getInt("expected_participants");
                result.add(serv);
            }
        });

        return result;
    }
}
