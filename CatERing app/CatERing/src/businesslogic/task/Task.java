package businesslogic.task;

import businesslogic.menu.Menu;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Task {
    private int id;
    private boolean completed;
    private String description, quantity, portions, estimated_time;

    private int recipe_id;

    public Task(int recipe_id, String description){
        this.recipe_id = recipe_id;
        this.description = description;
        this.quantity = "";
        this.portions = "";
        this.estimated_time = "";
        this.completed = false;
    }

    public int getId(){
        return id;
    }

    // STATIC METHODS FOR PERSISTENCE

    /* save the businesslogic.task in the db */
    public static void saveNewTask(int summary_sheet_id, Task t, int position) {
        String summarySheetInsert = "INSERT INTO tasks (summary_sheet_id, recipe_id, description, quantity, portions, estimatedTime, completed, position) " +
                                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        PersistenceManager.executeBatchUpdate(summarySheetInsert, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1,summary_sheet_id);
                ps.setInt(2,t.recipe_id);
                ps.setString(3, PersistenceManager.escapeString(t.description));
                ps.setString(4, PersistenceManager.escapeString(t.quantity));
                ps.setString(5, PersistenceManager.escapeString(t.portions));
                ps.setString(6, PersistenceManager.escapeString(t.estimated_time));
                ps.setBoolean(7, t.completed);
                ps.setInt(8, position);
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
}
