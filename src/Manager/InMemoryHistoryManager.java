package Manager;

import Model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> historyList = new ArrayList<>();

    @Override
    public void add(Task task) {
            if (historyList.size() == 10) {
                historyList.removeFirst();
            }
            historyList.add(task);

    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyList;
    }
}
