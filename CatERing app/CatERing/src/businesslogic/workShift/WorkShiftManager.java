package businesslogic.workShift;

import java.util.ArrayList;

public class WorkShiftManager {
    private ArrayList<WorkShift> work_shifts;

    public WorkShiftManager() {
        work_shifts = new ArrayList<>();
    }

    public ArrayList<WorkShift> getWorkShifts() {
        return work_shifts;
    }



}
