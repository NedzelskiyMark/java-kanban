package Manager;

import Model.Task;
import Model.Node;

import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Node<Task> head;
    private Node<Task> tail;
    private int size;
    private List<Task> historyList = new LinkedList<>();
    private Map<Integer, Integer> idIndexInList = new HashMap<>();

    private Map<Integer, Node> nodesMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (idIndexInList.containsKey(task.getId())) {
            historyList.remove((int) idIndexInList.get(task.getId()));
            idIndexInList.remove(task.getId());
        }
        historyList.add(task);
        idIndexInList.put(task.getId(), historyList.size() - 1);
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }

    @Override
    public void clearHistoryList() {
        historyList.clear();
    }

    @Override
    public void remove(int id) {
        if (idIndexInList.containsKey(id)) {
            historyList.remove((int) idIndexInList.get(id));
        }
    }
}
