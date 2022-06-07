package businesslogic.task;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.EventException;
import businesslogic.event.EventInfo;
import businesslogic.event.ServiceInfo;
import businesslogic.recipe.Recipe;
import businesslogic.user.User;
import businesslogic.workShift.WorkShift;
import businesslogic.workShift.WorkShiftException;

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

    /* sort the task list */
    public void sortTaskList(Task t, int position) throws UseCaseLogicException {
        if(currentSummarySheet == null || !currentSummarySheet.hasTask(t) || position < 0 || position >= currentSummarySheet.tasksSize()){
            throw  new UseCaseLogicException();
        }

        currentSummarySheet.sortTaskList(t, position);

        this.notifyTaskListRearranged(currentSummarySheet);
    }

    /* get the work shif board */
    public ArrayList<WorkShift> getWorkShiftBoard(){
        return CatERing.getInstance().getWorkShiftManager().getWorkShifts();
    }

    /* assign a task */
    public void assignTask(Task task, WorkShift workShift, User cook, String estimatedTime, String quantity, String portions) throws UseCaseLogicException, WorkShiftException {
        if(this.currentSummarySheet == null || !currentSummarySheet.hasTask(task)){
            throw new UseCaseLogicException();
        }

        if((cook != null && !workShift.hasCook(cook)) || workShift.getAssignable()){
            throw new WorkShiftException();
        }

        currentSummarySheet.assignTask(task, workShift, cook, estimatedTime, quantity, portions);

        this.notifyTaskAssigned(task);
    }

    /* indicate full work shift */
    public void indicateFullWorkShift(WorkShift workShift) throws WorkShiftException {
        CatERing.getInstance().getWorkShiftManager().indicateFullWorkShift(workShift);

        this.notifyIndicatedFullWorkShift(workShift);
    }

    /* select a summary sheet */
    public SummarySheet selectSummarySheet(EventInfo event, ServiceInfo service, SummarySheet summarySheet) throws UseCaseLogicException, EventException, TaskException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if(!user.isChef())
            throw new UseCaseLogicException();

        if(!event.isAssignedUser(user) || !event.hasService(service))
            throw new EventException();

        if(!summarySheet.hasService(service))
            throw new TaskException();

        this.currentSummarySheet = summarySheet;

        return summarySheet;
    }

    /* notify the creation of the summary sheet for a specific service */
    private void notifySummarySheetCreated(SummarySheet s) {
        for (TaskEventReceiver er : this.eventReceivers) {
            er.updateSummarySheetCreated(s);
        }
    }

    /* notify the add of a new task in the summary sheet for a specific service */
    private void notifyTaskAdded(SummarySheet s, Task t, int task_position) {
        for (TaskEventReceiver er : this.eventReceivers) {
            er.updateTaskAdded(s, t, task_position);
        }
    }

    /* notify the rearranged of the task list */
    private void notifyTaskListRearranged(SummarySheet s) {
        for (TaskEventReceiver er : this.eventReceivers) {
            er.updateTaskListRearranged(s);
        }
    }

    /* notify the assigned of the task */
    private void notifyTaskAssigned(Task t){
        for (TaskEventReceiver er : this.eventReceivers) {
            er.updateTaskAssigned(t);
        }
    }

    /* notify the indicated full work shift*/
    private void notifyIndicatedFullWorkShift(WorkShift ws){
        for (TaskEventReceiver er : this.eventReceivers) {
            er.updateIndicatedFullWorkShift(ws);
        }
    }

    public void addEventReceiver(TaskEventReceiver rec) {
        this.eventReceivers.add(rec);
    }
}
