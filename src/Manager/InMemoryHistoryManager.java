package Manager;

import Model.Task;

import java.util.LinkedHashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Map<Integer, Task> historyList = new LinkedHashMap<>();

    @Override
    public void add(Task task) {
        if (historyList.containsKey(task.getId())) {
            historyList.remove(task.getId());
        }
        historyList.put(task.getId(), task);
    }

    @Override
    public Map<Integer, Task> getHistory() {
        return historyList;
    }

    @Override
    public void clearHistoryList() {
        historyList.clear();
    }

    @Override
    public void remove(int id) {
        historyList.remove(id);
    }
}
