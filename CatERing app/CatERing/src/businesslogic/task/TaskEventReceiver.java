package businesslogic.task;

public interface TaskEventReceiver {
    public void updateSummarySheetCreated(SummarySheet s);

    public void updateTaskAdded(SummarySheet s, Task t, int task_position);
}
