package businesslogic.task;

import businesslogic.event.ServiceInfo;
import businesslogic.menu.MenuItem;
import businesslogic.recipe.Recipe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SummarySheet {
    private int id;

    private int service_id;

    private ObservableList<Task> tasks;

    public SummarySheet(ServiceInfo service){
        this.service_id = service.getId();

        this.tasks = FXCollections.observableArrayList();

        ArrayList<Integer> menu_items_id = service.getServiceMenuItems();

        for(int item_id: menu_items_id){
            ArrayList<String> menu_item_recipe = MenuItem.getItemRecipe(item_id);

            Task t = new Task(Integer.parseInt(menu_item_recipe.get(0)), menu_item_recipe.get(1));

            tasks.add(t);
        }
    }

    public int getId() {
        return id;
    }

    /* add a new task in the summary sheet */
    public Task addTask(Recipe recipe){
        Task t = new Task(recipe.getId(), recipe.getName());
        tasks.add(t);

        return t;
    }

    /* return the position of a task */
    public int getTaskPosition(Task t){
        return tasks.indexOf(t);
    }

    /* return if summary sheet has the task */
    public boolean hasTask(Task t){
        for(Task task: tasks){
            if(task.getId() == t.getId())
                return true;
        }

        return false;
    }

    /* return the number of the task */
    public int tasksSize(){
        return tasks.size();
    }

    /* sort the task list */
    public void sortTaskList(Tas)
    // STATIC METHODS FOR PERSISTENCE

    /* save the summary sheet in the db */
    public static void saveNewSummarySheet(SummarySheet s) {
        String summarySheetInsert = "INSERT INTO catering.summarysheet (service_id) VALUES (?);";
        PersistenceManager.executeBatchUpdate(summarySheetInsert, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1,s.service_id);
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // should be only one
                if (count == 0) {
                    s.id = rs.getInt(1);
                }
            }
        });

        for(Task task: s.tasks){
            task.saveNewTask(s.id, task, s.tasks.indexOf(task));
        }
    }
}
