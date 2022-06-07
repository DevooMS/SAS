package businesslogic.workShift;

import businesslogic.task.Task;
import businesslogic.task.TaskEventReceiver;

import java.util.ArrayList;

public class WorkShiftManager {
    private ArrayList<WorkShift> workShifts;
    private ArrayList<WorkShiftEventReceiver> eventReceivers;

    public WorkShiftManager() {
        workShifts = new ArrayList<>();
        eventReceivers = new ArrayList<>();
    }

    public ArrayList<WorkShift> getWorkShifts() {
        return workShifts;
    }

    /* create work shift */
    public WorkShift createWorkShift(){
        WorkShift ws = new WorkShift();
        workShifts.add(ws);

        this.notifyWorkShiftCreated(ws);

        return ws;
    }
    /* indicate full work shift */
    public void indicateFullWorkShift(WorkShift workShift) throws WorkShiftException {
        if(!this.hasWorkShift(workShift)){
            throw new WorkShiftException();
        }

        workShift.indicateFull();
    }

    public boolean hasWorkShift(WorkShift workShift){
        for(WorkShift ws: workShifts){
            if(ws.getId() == workShift.getId()){
                return true;
            }
        }

        return false;
    }

    /* notify the created work shift */
    private void notifyWorkShiftCreated(WorkShift ws){
        for (WorkShiftEventReceiver er : this.eventReceivers) {
            er.updateWorkShiftCreated(ws);
        }
    }

    public void addEventReceiver(WorkShiftEventReceiver rec) {
        this.eventReceivers.add(rec);
    }
}
