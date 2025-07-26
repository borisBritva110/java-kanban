package ru.common.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.common.model.Task;

public class InMemoryHistoryManager implements HistoryManager {

    private final HashMap<Integer, Node> historyEntities = new HashMap<>();
    private Node head;
    private Node<Task> tail;

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        if (historyEntities.containsKey(task.getId())) {
            removeNode(historyEntities.get(task.getId()));
        }
        linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(getTasks());
    }

    @Override
    public void remove(int id) {
        if (!historyEntities.containsKey(id)) {
            return;
        }

        Node node = historyEntities.remove(id);

        if (node == null) {
            return;
        }

        Node prev = node.prev;
        Node next = node.next;

        if (prev != null) {
            prev.next = next;
        } else {
            head = next;
        }

        if (next != null) {
            next.prev = prev;
        } else {
            tail = prev;
        }
    }

    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }


    private void linkLast(Task task) {
        Node<Task> newNode = new Node<>(tail, task, null);
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        historyEntities.put(task.getId(), newNode);

    }

    public List<Task> getTasks() {
        List<Task> result = new ArrayList<>();
        Node<Task> current = head;
        while (current != null) {
            result.add(current.data);
            current = current.next;
        }
        return result;
    }
}
