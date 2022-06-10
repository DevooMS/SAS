package businesslogic.task;

import businesslogic.event.ServiceInfo;
import businesslogic.menu.Menu;
import businesslogic.menu.MenuItem;
import businesslogic.menu.Section;
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
import java.util.ArrayList;

public class SummarySheet {
    private int id;
    private ServiceInfo service;
    private ObservableList<Task> tasks;
    private Menu menu;

    public SummarySheet(ServiceInfo service){
        this.service = service;
        this.tasks = FXCollections.observableArrayList();
        this.menu = service.getMenu();

        ObservableList<Section> menu_sections = menu.getSections();

        for(Section s: menu_sections){
            for(MenuItem section_item: s.getItems()){
                Recipe r = section_item.getItemRecipe();

                Task t = new Task(r, section_item.getDescription());

                tasks.add(t);
            }
        }

        ObservableList<MenuItem> menu_free_items = menu.getFreeItems();

        for(MenuItem menu_free_item: menu_free_items){
            Recipe r = menu_free_item.getItemRecipe();

            Task t = new Task(r, menu_free_item.getDescription());

            tasks.add(t);
        }
    }

    public int getId() {
        return id;
    }

    public ObservableList<Task> getTasks() {
        return tasks;
    }

    /* add a new task in the summary sheet */
    public Task addTask(Recipe recipe, String description){
        Task t;

        if(description == null)
            t = new Task(recipe, recipe.getName());
        else
            t = new Task(recipe, description);

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
    public void sortTaskList(Task t, int position){
        tasks.remove(t);
        tasks.add(position, t);
    }

    /* assign a task */
    public void assignTask(Task task, WorkShift workShift, User cook, String estimatedTime, String quantity, String portions){
        task.assign(workShift, cook, estimatedTime, quantity, portions);
    }

    /* check if the id of the service is the same of the summary sheet service id */
    public boolean hasService(ServiceInfo service){
        return this.service.getId() == service.getId();
    }

    public String toString(){
        String summarySheet = "Id foglio riepilogativo: " + this.id + "\n";

        for(Task t: tasks)
            summarySheet += t + "\n";

        return summarySheet;
    }

    /* remove a task */
    public void removeTask(Task task){
        tasks.remove(task);
    }

    /* indicate task completed */
    public void indicateTaskCompleted(Task task){
        task.indicateCompleted();
    }


    // STATIC METHODS FOR PERSISTENCE

    /* save the summary sheet in the db */
    public static void saveNewSummarySheet(SummarySheet s) {
        String summarySheetInsert = "INSERT INTO catering.summarysheet (service_id) VALUES (?);";
        PersistenceManager.executeBatchUpdate(summarySheetInsert, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1,s.service.getId());
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

    /* save the new order of the task list */
    public static void saveTaskListRearranged(SummarySheet s) {
        String upd = "UPDATE tasks SET position = ? WHERE id = ?";
        PersistenceManager.executeBatchUpdate(upd, s.tasks.size(), new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, batchCount);
                ps.setInt(2, s.tasks.get(batchCount).getId());
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // no generated ids to handle
            }
        });
    }
}
