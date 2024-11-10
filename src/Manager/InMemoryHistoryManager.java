package Manager;

import Model.Task;

import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {
    private LinkedList<Task> historyList = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (historyList.size() == 10) {
            historyList.remove(0);
        }
        historyList.add(task);
    }

    @Override
    public LinkedList<Task> getHistory() {
        return historyList;
    }

    @Override
    public void clearHistoryList() {
        historyList.clear();
    }
}
