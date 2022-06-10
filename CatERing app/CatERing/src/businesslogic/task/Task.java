package businesslogic.task;

import businesslogic.menu.Menu;
import businesslogic.recipe.Recipe;
import businesslogic.user.User;
import businesslogic.workShift.WorkShift;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Task {
    private int id;
    private boolean completed;
    private String description, quantity, portions, estimatedTime;
    private Recipe recipe;
    private WorkShift workShift;
    private User cook;


    public Task(Recipe recipe, String description){
        this.recipe = recipe;
        this.description = description;
        this.quantity = "--";
        this.portions = "--";
        this.estimatedTime = "--";
    }

    public int getId(){
        return id;
    }

    /* assign a task */
    public void assign(WorkShift workShift, User cook, String estimated_time, String quantity, String portions){
        this.workShift = workShift;

        if(cook != null)
            this.cook = cook;

        if(estimated_time!=null)
            this.estimatedTime = estimated_time;

        if(quantity != null)
            this.quantity = quantity;

        if(portions != null)
            this.portions = portions;
    }

    /* indicate completed */
    public void indicateCompleted(){
        this.completed = true;
    }

    public String toString(){
        String task = "Id compito " + this.id + " id ricetta: " + recipe.getId() + " - descrizione: " + this.description;

        if(this.workShift != null)
            task += " - id turno: " + this.workShift.getId();
        else
            task += " - id turno: --";

        if(this.cook != null)
            task += " - id cuoco: " + this.cook.getId();
        else
            task += " - id cuoco: --";

        task += " - quantità: " + this.quantity + " - porzioni: " + this.portions + " - tempo stimato: " + this.estimatedTime +
                " - completato: " + this.completed;

        return task;
    }

    // STATIC METHODS FOR PERSISTENCE

    /* save the businesslogic.task in the db */
    public static void saveNewTask(int summary_sheet_id, Task t, int position) {
        String summarySheetInsert = "INSERT INTO tasks (summary_sheet_id, recipe_id, description, completed, position) " +
                                    "VALUES (?, ?, ?, ?, ?);";
        PersistenceManager.executeBatchUpdate(summarySheetInsert, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1,summary_sheet_id);
                ps.setInt(2,t.recipe.getId());
                ps.setString(3, PersistenceManager.escapeString(t.description));
                ps.setBoolean(4, t.completed);
                ps.setInt(5, position);
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
        String upd = "UPDATE tasks SET work_shift_id = ?, quantity = ?, portions = ?, estimated_time = ?" +
                     "WHERE id = ?";

        PersistenceManager.executeBatchUpdate(upd, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, task.workShift.getId());
                ps.setString(2, task.quantity);
                ps.setString(3, task.portions);
                ps.setString(4, task.estimatedTime);
                ps.setInt(5, task.id);
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // no generated ids to handle
            }
        });

        if(task.cook != null){
            String updCook = "UPDATE tasks SET cook_id = ? WHERE id = ?";

            PersistenceManager.executeBatchUpdate(updCook, 1, new BatchUpdateHandler() {
                @Override
                public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                    ps.setInt(1, task.cook.getId());
                    ps.setInt(2, task.id);
                }

                @Override
                public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                    // no generated ids to handle
                }
            });
        }
    }

    /* delete a task from the db */
    public static void deleteTask(Task task, SummarySheet s){
        String delTask = "DELETE FROM tasks WHERE id = " + task.id;
        PersistenceManager.executeUpdate(delTask);

        ObservableList<Task> sTasks = FXCollections.unmodifiableObservableList(s.getTasks());
    }

    /* indicate task completed in the db */
    public static void completeTask(Task task){
        String completeTask = "UPDATE tasks SET completed = ? WHERE id = ?";

        PersistenceManager.executeBatchUpdate(completeTask, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setBoolean(1, task.completed);
                ps.setInt(2, task.id);
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // no generated ids to handle
            }
        });
    }
}
