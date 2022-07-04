import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.EventException;
import businesslogic.event.EventInfo;
import businesslogic.event.ServiceInfo;
import businesslogic.menu.Menu;
import businesslogic.recipe.Recipe;
import businesslogic.task.SummarySheet;
import businesslogic.task.Task;
import businesslogic.task.TaskException;
import businesslogic.user.User;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class TestCatERing_taskManagement2a {
    public static void main(String[] args) {
        try {
            System.out.println("TEST FAKE LOGIN");
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            User user = CatERing.getInstance().getUserManager().getCurrentUser();
            System.out.println(user.getUserName());

            System.out.println("TEST GENERATE SUMMARY SHEET");
            CatERing.getInstance().getMenuManager().getAllMenus();
            ObservableList<EventInfo> event = CatERing.getInstance().getEventManager().getEventInfo(1);
            System.out.println("Generazione fogli riepilogativi per i servizi dell'evento: ");
            System.out.println(event);

            ArrayList<SummarySheet> summarySheets = new ArrayList<>();
            ObservableList<ServiceInfo> services = null;

            for (EventInfo e : event) {
                services = e.getServices();
                for (ServiceInfo service : services) {
                    SummarySheet s = CatERing.getInstance().getTaskManager().generateSummarySheet(e, service);
                    summarySheets.add(s);
                    System.out.println("Foglio riepilogativo del servizio: ");
                    System.out.println(service);
                    System.out.println(s);
                }
            }
            System.out.println("TEST SELECT SUMMARY SHEET");
            SummarySheet summarySheetSelect = CatERing.getInstance().getTaskManager().selectSummarySheet(event.get(0), services.get(0), summarySheets.get(0));
            System.out.println(summarySheetSelect);

            System.out.println("TEST ADD TASK");
            Recipe recipe = CatERing.getInstance().getRecipeManager().getLoadedRecipe(9);
            Task insertTask = CatERing.getInstance().getTaskManager().defineTask(recipe, "Cornetti al cioccolato");
            System.out.println("Foglio riepilogativo aggiornato");
            System.out.println(summarySheetSelect);

            System.out.println("TEST SORT TASK");
            CatERing.getInstance().getTaskManager().sortTaskList(insertTask, 2);
            System.out.println("Foglio riepilogativo aggiornato");
            System.out.println(summarySheetSelect);

            System.out.println("TEST DELETE TASK");
            CatERing.getInstance().getTaskManager().deleteTask(insertTask);
            System.out.println("Foglio riepilogativo aggiornato");
            System.out.println(summarySheetSelect);
        } catch (UseCaseLogicException | EventException | TaskException e) {
            e.printStackTrace();
        }
    }
}
