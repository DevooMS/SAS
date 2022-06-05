package businesslogic.task;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.EventException;
import businesslogic.event.EventInfo;
import businesslogic.event.ServiceInfo;
import businesslogic.menu.MenuEventReceiver;
import businesslogic.user.User;

import java.util.ArrayList;

public class TaskManager {

    private SummarySheet currentSummarySheet;
    private ArrayList<TaskEventReceiver> eventReceivers;

    public TaskManager(){
        this.eventReceivers = new ArrayList<>();
    }

    /* generate a summary sheet for a specif service of an event */
    public SummarySheet generateSummarySheet(EventInfo event, ServiceInfo service) throws UseCaseLogicException, EventException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if(!user.isChef()){
            throw new UseCaseLogicException();
        }

        if(!event.isAssignedUser(user) || !event.hasService(service)){
            throw new EventException();
        }

        SummarySheet s = new SummarySheet(service);

        this.setCurrentSummarySheet(s);
        this.notifySummarySheetCreated(s);

        return s;
    }

    /* set the current summary sheet */
    public void setCurrentSummarySheet(SummarySheet summarySheet) {
        this.currentSummarySheet = summarySheet;
    }

    /* notify the creation of the summary sheet for a specific service*/
    private void notifySummarySheetCreated(SummarySheet s) {
        for (TaskEventReceiver er : this.eventReceivers) {
            er.updateSummarySheetCreated(s);
        }
    }

    public void addEventReceiver(TaskEventReceiver rec) {
        this.eventReceivers.add(rec);
    }
}
