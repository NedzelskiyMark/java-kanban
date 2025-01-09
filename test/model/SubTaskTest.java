package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
    SubTask subtask1 = new SubTask("SubTask name", "SubTask description", 0, 55);
    SubTask subtask2 = new SubTask("SubTask name", "SubTask description", 1, 15);

    @Test
    void testSubTaskEqualsOnlyIfIdIsEqual() {
        assertNotEquals(subtask1, subtask2);
    }

    @Test
    void subtaskCantAddItselfLikeHisOwnEpic() {
        //попытка добавить другую задачу как Эпик подзадачи
        assertEquals(1, subtask1.setRelationEpicId(subtask2));
        //попытка добавить саму задачу как Эпик подзадачи для самой же себя
        assertEquals(-1, subtask1.setRelationEpicId(subtask1));
    }
}