package persistence;

import businesslogic.task.SummarySheet;
import businesslogic.task.Task;
import businesslogic.task.TaskEventReceiver;

public class TaskPersistence implements TaskEventReceiver {

    @Override
    public void updateSummarySheetCreated(SummarySheet s){
        SummarySheet.saveNewSummarySheet(s);
    }

    @Override
    public void updateTaskAdded(SummarySheet s, Task t, int task_position){
        Task.saveNewTask(s.getId(), t, task_position);
    }
}
