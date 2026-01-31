package alterego.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class DeadlineTest {
    private Deadline deadline = new Deadline("a_task_name", LocalDate.parse("2026-04-20"));

    @Test
    public void toFileFormatTest() {
        assertEquals("D | 0 | a_task_name | Apr 20 2026", deadline.toFileFormat());
        deadline.setDone();
        assertEquals("D | 1 | a_task_name | Apr 20 2026", deadline.toFileFormat());
    }
}
