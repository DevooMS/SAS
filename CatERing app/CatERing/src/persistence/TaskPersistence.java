package persistence;

import businesslogic.task.SummarySheet;
import businesslogic.task.Task;
import businesslogic.task.TaskEventReceiver;
import businesslogic.workShift.WorkShift;

public class TaskPersistence implements TaskEventReceiver {

    @Override
    public void updateSummarySheetCreated(SummarySheet s){
        SummarySheet.saveNewSummarySheet(s);
    }

    @Override
    public void updateTaskAdded(SummarySheet s, Task t/*, int task_position*/){
        Task.saveNewTask(s.getId(), t, s.getTaskPosition(t));
    }

    @Override
    public void updateTaskListRearranged(SummarySheet s){
        SummarySheet.saveTaskListRearranged(s);
    }

    @Override
    public void updateTaskAssigned(Task t){
        Task.updateTaskInformation(t);
    }

    @Override
    public void updateIndicatedFullWorkShift(WorkShift ws){
        WorkShift.updateWorkShiftInformation(ws);
    }

    @Override
    public void updateTaskDeleted(SummarySheet s, Task t){
        Task.deleteTask(t,s);
        SummarySheet.saveTaskListRearranged(s);
    }

    @Override
    public void updateTaskCompleted(Task t){
        Task.completeTask(t);
    }

    @Override
    public void updateIndicatedNotFullWorkShift(WorkShift ws){
        WorkShift.updateWorkShiftInformation(ws);
    }
}
