package alterego.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import alterego.utils.DateUtils;

public class DeadlineTest {
    private Deadline deadline = new Deadline("a_task_name", DateUtils.parseDateFromInput("20-04-2026"));

    @Test
    public void toFileFormatTest() {
        assertEquals("D | 0 | a_task_name | Apr 20 2026", deadline.toFileFormat());
        deadline.setDone();
        assertEquals("D | 1 | a_task_name | Apr 20 2026", deadline.toFileFormat());
    }
}
