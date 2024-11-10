package Manager;

import Model.Task;

import java.util.Collection;

public interface HistoryManager {
    void add(Task task);

    Collection<Task> getHistory();

    public void clearHistoryList();
}
