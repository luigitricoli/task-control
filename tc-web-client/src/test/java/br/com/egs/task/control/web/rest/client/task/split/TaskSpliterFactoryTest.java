package br.com.egs.task.control.web.rest.client.task.split;

import br.com.egs.task.control.web.model.exception.InvalidDateException;
import br.com.egs.task.control.web.rest.client.task.CoreTask;
import br.com.egs.task.control.web.rest.client.task.TaskDate;
import org.joda.time.DateTime;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.GregorianChronology;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class TaskSpliterFactoryTest {

    @Test
    public void taskOfOneDay() throws InvalidDateException {
        TaskDate today = new TaskDate();
        String json = String.format("{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"%s\",\"foreseenEndDate\":\"%s\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #overtime to finish it sooner\"}]}", today.toString(), today.toString());
        TaskSpliter spliter = TaskSpliterFactory.getInstance(CoreTask.unmarshal(json), new TaskDate("2014-01-01").toCalendar());

        assertEquals(DoingSpliter.class, spliter.getClass());
    }

}
