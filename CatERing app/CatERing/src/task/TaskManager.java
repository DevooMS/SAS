package task;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.EventInfo;
import businesslogic.event.ServiceInfo;
import businesslogic.user.User;

public class TaskManager {

    public SummarySheet generateSummarySheet(EventInfo event, ServiceInfo service) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if(!user.isChef() || !event.isAssignedUser(user) || !event.hasService(service)){
            throw new UseCaseLogicException();
        }

        SummarySheet s = new SummarySheet(service);

        return s;
    }
}
