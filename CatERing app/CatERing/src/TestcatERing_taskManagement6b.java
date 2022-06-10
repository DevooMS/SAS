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
import businesslogic.workShift.WorkShift;
import businesslogic.workShift.WorkShiftException;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class TestcatERing_taskManagement6b {
    public static void main(String[] args) {
        try {
            System.out.println("TEST GET WORK SHIF BOARD");
            ObservableList<WorkShift> workShifts = CatERing.getInstance().getTaskManager().getWorkShiftBoard();
            System.out.println("Turni: ");
            for(WorkShift ws: workShifts)
                System.out.println(ws);

            WorkShift workShift = workShifts.get(0);

            System.out.println("TEST INDICATE FULL WORK SHIFT");
            System.out.println("Turno da indicare completo");
            System.out.println(workShift);
            CatERing.getInstance().getTaskManager().indicateFullWorkShift(workShift);
            System.out.println("Turno aggiornato");
            System.out.println(workShift);

            System.out.println("TEST INDICATE NOT FULL WORK SHIFT");
            System.out.println("Turno da indicare non completo");
            System.out.println(workShift);
            CatERing.getInstance().getTaskManager().indicateNotFullWorkShift(workShift);
            System.out.println("Turno aggiornato");
            System.out.println(workShift);
        } catch (WorkShiftException e) {
            e.printStackTrace();
        }
    }
}
