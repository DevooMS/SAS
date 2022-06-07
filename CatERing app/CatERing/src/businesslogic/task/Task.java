package businesslogic.task;

import businesslogic.menu.Menu;
import businesslogic.user.User;
import businesslogic.workShift.WorkShift;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Task {
    private int id, recipe_id, work_shift_id, cook_id;
    private boolean completed;
    private String description, quantity, portions, estimated_time;


    public Task(int recipe_id, String description){
        this.recipe_id = recipe_id;
        this.description = description;
        this.work_shift_id = 0;
        this.cook_id = 0;
        this.quantity = "";
        this.portions = "";
        this.estimated_time = "";
        this.completed = false;
    }

    public int getId(){
        return id;
    }

    /* assign a task */
    public void assign(WorkShift work_shift, User cook, String estimated_time, String quantity, String portions){
        this.work_shift_id = work_shift.getId();

        if(cook != null)
            this.cook_id = cook.getId();

        if(estimated_time!=null)
            this.estimated_time = estimated_time;

        if(quantity != null)
            this.quantity = quantity;

        if(portions != null)
            this.portions = portions;
    }

    // STATIC METHODS FOR PERSISTENCE

    /* save the businesslogic.task in the db */
    public static void saveNewTask(int summary_sheet_id, Task t, int position) {
        String summarySheetInsert = "INSERT INTO tasks (summary_sheet_id, recipe_id, work_shift_id, cook_id, " +
                                                       "description, quantity, portions, estimatedTime, completed, position) " +
                                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PersistenceManager.executeBatchUpdate(summarySheetInsert, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1,summary_sheet_id);
                ps.setInt(2,t.recipe_id);
                ps.setInt(3, t.work_shift_id);
                ps.setInt(4, t.cook_id);
                ps.setString(5, PersistenceManager.escapeString(t.description));
                ps.setString(6, PersistenceManager.escapeString(t.quantity));
                ps.setString(7, PersistenceManager.escapeString(t.portions));
                ps.setString(8, PersistenceManager.escapeString(t.estimated_time));
                ps.setBoolean(9, t.completed);
                ps.setInt(10, position);
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // should be only one
                if (count == 0) {
                    t.id = rs.getInt(1);
                }
            }
        });
    }

    /* update the information of the task in the db */
    public static void updateTaskInformation(Task task){
        String upd = "UPDATE tasks SET work_shift_id = " + task.work_shift_id +
                     "                 cook_id = " + task.cook_id + " quantity = " + task.quantity +
                     "                 portions = " + task.portions + " estimated_time = " + task.estimated_time +
                     "WHERE id = " + task.id;

        PersistenceManager.executeUpdate(upd);
    }
}
