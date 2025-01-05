package model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    Task task1 = new Task("Task name", "Task description");
    Task task2 = new Task("Task name", "Task description",1, 15);


    @Test
    void testTaskEqualsOnlyIfIdIsEqual() {
        assertNotEquals(task1, task2);
    }

    @Test
    void testEndTimeCalculating() {
        task2.setStartTime(2025, 1,5,12,00);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
        String endTimeString = task2.getEndTime().format(formatter);

        assertEquals("05.01.2025, 13:15", endTimeString);
    }
}

