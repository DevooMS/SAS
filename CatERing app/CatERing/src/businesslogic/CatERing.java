package businesslogic;

import businesslogic.event.EventManager;
import businesslogic.menu.MenuManager;
import businesslogic.recipe.RecipeManager;
import businesslogic.user.UserManager;
import businesslogic.workShift.WorkShiftManager;
import persistence.MenuPersistence;
import persistence.TaskPersistence;
import businesslogic.task.TaskManager;

public class CatERing {
    private static CatERing singleInstance;

    public static CatERing getInstance() {
        if (singleInstance == null) {
            singleInstance = new CatERing();
        }
        return singleInstance;
    }

    private MenuManager menuMgr;
    private RecipeManager recipeMgr;
    private UserManager userMgr;
    private EventManager eventMgr;
    private TaskManager taskMgr;
    private WorkShiftManager workShiftMgr;

    private MenuPersistence menuPersistence;
    private TaskPersistence taskPersistence;

    private CatERing() {
        menuMgr = new MenuManager();
        recipeMgr = new RecipeManager();
        userMgr = new UserManager();
        eventMgr = new EventManager();
        taskMgr = new TaskManager();
        workShiftMgr = new WorkShiftManager();
        menuPersistence = new MenuPersistence();
        taskPersistence = new TaskPersistence();
        menuMgr.addEventReceiver(menuPersistence);
        taskMgr.addEventReceiver(taskPersistence);
    }


    public MenuManager getMenuManager() {
        return menuMgr;
    }

    public RecipeManager getRecipeManager() {
        return recipeMgr;
    }

    public UserManager getUserManager() {
        return userMgr;
    }

    public EventManager getEventManager() { return eventMgr; }

    public TaskManager getTaskManager() { return taskMgr; }

    public WorkShiftManager getWorkShiftManager() { return workShiftMgr; }
}
