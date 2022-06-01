package task;

import businesslogic.event.ServiceInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SummarySheet {
    private ServiceInfo service;
    private ObservableList<Task> tasks;

    public SummarySheet(ServiceInfo service){
        this.service = service;

        this.tasks = FXCollections.observableArrayList();
    }
}
