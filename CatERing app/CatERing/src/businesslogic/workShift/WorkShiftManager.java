package businesslogic.workShift;

import businesslogic.task.Task;
import businesslogic.task.TaskEventReceiver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class WorkShiftManager {
    private ArrayList<WorkShiftEventReceiver> eventReceivers;

    public WorkShiftManager() {
        WorkShift.loadAllWorkShifts();
        eventReceivers = new ArrayList<>();
    }

    public ObservableList<WorkShift> getWorkShiftBoard() {
        return FXCollections.unmodifiableObservableList(WorkShift.getAllWorkShifts());
    }

    /* indicate full work shift */
    public void indicateFullWorkShift(WorkShift workShift) throws WorkShiftException {
        if(!this.hasWorkShift(workShift)){
            throw new WorkShiftException();
        }

        workShift.indicateFull();
    }

    public boolean hasWorkShift(WorkShift workShift){
        return WorkShift.hasWorkShift(workShift);
    }

    /* indicate not full work shift */
    public void indicateNotFullWorkShift(WorkShift workShift) throws WorkShiftException {
        if(!this.hasWorkShift(workShift)){
            throw new WorkShiftException();
        }

        workShift.indicateNotFull();
    }

    public void addEventReceiver(WorkShiftEventReceiver rec) {
        this.eventReceivers.add(rec);
    }
}
