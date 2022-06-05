package businesslogic.task;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.EventException;
import businesslogic.event.EventInfo;
import businesslogic.event.ServiceInfo;
import businesslogic.recipe.Recipe;
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

    /* define new task for a summary sheet */
    public Task defineTask(Recipe recipe) throws UseCaseLogicException {

        if(currentSummarySheet == null){
            throw new UseCaseLogicException();
        }

        Task t = currentSummarySheet.addTask(recipe);
        int task_position = currentSummarySheet.getTaskPosition(t);

        this.notifyTaskAdded(currentSummarySheet, t, task_position);

        return t;
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

    /* notify the add of a new task in the summary sheet for a specific service*/
    private void notifyTaskAdded(SummarySheet s, Task t, int task_position) {
        for (TaskEventReceiver er : this.eventReceivers) {
            er.updateTaskAdded(s, t, task_position);
        }
    }

    /* sort the task list */
    public void sortTaskList(Task t, int position) throws UseCaseLogicException {
        if(currentSummarySheet == null || !currentSummarySheet.hasTask(t) || position < 0 || position >= currentSummarySheet.tasksSize()){
            throw  new UseCaseLogicException();
        }

        currentSummarySheet.sortTaskList(t, position);
    }

    public void addEventReceiver(TaskEventReceiver rec) {
        this.eventReceivers.add(rec);
    }
}
