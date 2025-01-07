package model;

import java.time.LocalDateTime;
import java.util.Comparator;

public class TaskComparatorByStartTime implements Comparator<Task> {
    @Override
    public int compare(Task t1, Task t2) {
        LocalDateTime startTime1 = t1.getStartTime();
        LocalDateTime startTime2 = t2.getStartTime();

        if (startTime1.isBefore(startTime2)) {
            return -1;
        } else if (startTime1.isAfter(startTime2)) {
            return 1;
        } else {
            return 0;
        }
    }
}
