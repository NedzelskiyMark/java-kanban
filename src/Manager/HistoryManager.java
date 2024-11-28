package Manager;

import Model.Task;

import java.util.Collection;
import java.util.Map;

public interface HistoryManager {
    void add(Task task);

    Map<Integer, Task> getHistory();

    void clearHistoryList();

    void remove(int id);
}
