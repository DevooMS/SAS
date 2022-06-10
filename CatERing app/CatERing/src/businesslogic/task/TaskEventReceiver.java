package businesslogic.task;

import businesslogic.workShift.WorkShift;

public interface TaskEventReceiver {
    public void updateSummarySheetCreated(SummarySheet s);

    public void updateTaskAdded(SummarySheet s, Task t, int task_position);

    public void updateTaskListRearranged(SummarySheet s);

    public void updateTaskAssigned(Task t);

    public void updateIndicatedFullWorkShift(WorkShift ws);

    public void updateTaskDeleted(Task t, SummarySheet s);

    public void updateTaskCompleted(Task t);

    public void updateIndicatedNotFullWorkShift(WorkShift ws);
}
