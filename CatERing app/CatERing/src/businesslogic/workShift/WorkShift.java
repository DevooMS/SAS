package businesslogic.workShift;

import businesslogic.user.User;

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
}
