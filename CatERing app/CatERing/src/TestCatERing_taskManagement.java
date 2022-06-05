import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.EventException;
import businesslogic.event.EventInfo;
import businesslogic.event.ServiceInfo;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class TestCatERing_taskManagement {
    public static void main(String[] args) {
        try {
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");

            //ObservableList<EventInfo> events = CatERing.getInstance().getEventManager().getAllEventInfo();
            ArrayList<EventInfo> event = CatERing.getInstance().getEventManager().getEventInfo(1);
            for(EventInfo e: event) {
                for (ServiceInfo service : e.getServices()) {
                    CatERing.getInstance().getTaskManager().generateSummarySheet(e, service);
                }
            }
        } catch (UseCaseLogicException | EventException e) {
            e.printStackTrace();
        }
    }
}
