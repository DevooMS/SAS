package businesslogic.workShift;

import businesslogic.task.Task;
import businesslogic.user.User;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;
import persistence.WorkShiftPersistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WorkShift {
    private boolean assignable;
    public int id;
    private ArrayList<User> cooks;

    public WorkShift(){
        assignable = true;
        cooks = new ArrayList<>();
    }

    public int getId(){
        return id;
    }

    public boolean getAssignable() {
        return assignable;
    }

    /* check if work shift has cook */
    public boolean hasCook(User cook){
        for(User c: cooks){
            if(cook.getId() == c.getId()){
                return true;
            }
        }

        return false;
    }

    /* indicate full */
    public void indicateFull(){
        this.assignable = false;
    }

    // STATIC METHODS FOR PERSISTENCE

    /* save the businesslogic.workShift in the db */
    public static void saveNewWorkShift(WorkShift ws) {
        String workShiftInsert = "INSERT INTO workshifts (assignable " +
                                    "VALUES (?);";
        PersistenceManager.executeBatchUpdate(workShiftInsert, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setBoolean(1, ws.assignable);
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // should be only one
                if (count == 0) {
                    ws.id = rs.getInt(1);
                }
            }
        });
    }

    /* update the information of the work shift in the db */
    public static void updateWorkShiftInformation(WorkShift ws){
        String upd = "UPDATE workshifts SET assignable = " + ws.assignable +
                "WHERE id = " + ws.id;

        PersistenceManager.executeUpdate(upd);
    }
}
