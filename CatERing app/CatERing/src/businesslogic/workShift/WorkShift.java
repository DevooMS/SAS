package businesslogic.workShift;

import businesslogic.recipe.Recipe;
import businesslogic.task.Task;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;
import persistence.ResultHandler;
import persistence.WorkShiftPersistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.*;

public class WorkShift {
    private static Map<Integer, WorkShift> all = new HashMap<>();

    public int id;
    private Date date;
    private Time hourStart, hourEnd;
    private boolean assignable;
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

    public String toString(){
        String workShift = "id turno: " + this.id + " - data: " + this.date + " - ora inizio: " + hourStart +
                           " - ora fine: " + hourEnd + " - assegnabile: " + this.assignable;
         workShift += "\n cuochi: ";
        for(User cook: cooks){
            workShift += cook.getUserName() + " ";
        }

        return workShift;
    }

    /* indicate not full */
    public void indicateNotFull(){
        this.assignable = true;
    }

    // STATIC METHODS FOR PERSISTENCE

    /* load all work shifts */
    public static ObservableList<WorkShift> loadAllWorkShifts() {
        ArrayList<WorkShift> newWorkShifts = new ArrayList<>();

        String query = "SELECT * FROM workshifts";
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int id = rs.getInt("id");
                if (all.containsKey(id)) {
                    WorkShift ws = all.get(id);
                    ws.date = rs.getDate("date");
                    ws.hourStart = rs.getTime("hour_start");
                    ws.hourEnd = rs.getTime("hour_end");
                    ws.assignable = rs.getBoolean("assignable");
                } else {
                    WorkShift ws = new WorkShift();
                    ws.id = id;
                    ws.date = rs.getDate("date");
                    ws.hourStart = rs.getTime("hour_start");
                    ws.hourEnd = rs.getTime("hour_end");
                    ws.assignable = rs.getBoolean("assignable");
                    all.put(ws.id, ws);
                    newWorkShifts.add(ws);
                }
            }
        });

        for(WorkShift workShift: newWorkShifts){
            String wsCook_id = "SELECT cook_id FROM workshiftcooks WHERE workshift_id = " + workShift.id;
            PersistenceManager.executeQuery(wsCook_id, new ResultHandler() {
                @Override
                public void handle(ResultSet rs) throws SQLException {
                    int cook_id = rs.getInt("cook_id");
                    User user = User.loadUserById(cook_id);
                    workShift.cooks.add(user);
                }
            });
        }

        return FXCollections.observableArrayList(all.values());
    }

    /* update the information of the work shift in the db */
    public static void updateWorkShiftInformation(WorkShift workShift){
        String upd = "UPDATE workshifts SET assignable = ? WHERE id = ?";

        PersistenceManager.executeBatchUpdate(upd, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setBoolean(1, workShift.assignable);
                ps.setInt(2, workShift.id);
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // no generated ids to handle
            }
        });
    }

    /* get all the work shifts */
    public static ObservableList<WorkShift> getAllWorkShifts() {
        return FXCollections.observableArrayList(all.values());
    }

    /* check if has work shift */
    public static boolean hasWorkShift(WorkShift workShift){
        return all.containsKey(workShift.id);
    }
}
