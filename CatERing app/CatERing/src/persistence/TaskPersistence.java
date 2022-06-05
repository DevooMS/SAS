package persistence;

import businesslogic.task.SummarySheet;
import businesslogic.task.TaskEventReceiver;

public class TaskPersistence implements TaskEventReceiver {

    @Override
    public void updateSummarySheetCreated(SummarySheet s){
        SummarySheet.saveNewSummarySheet(s);
    }
}
