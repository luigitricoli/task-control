package br.com.egs.task.control.web.rest.client.task.split;

import br.com.egs.task.control.web.model.exception.InvalidDateException;
import br.com.egs.task.control.web.rest.client.task.CoreTask;
import br.com.egs.task.control.web.rest.client.task.TaskDate;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @Test
    public void taskCalendar() throws ParseException {
        Calendar tmp1 = Calendar.getInstance();
        tmp1.set(Calendar.HOUR, 0);
        tmp1.set(Calendar.MINUTE, 0);
        tmp1.set(Calendar.SECOND, 0);
        tmp1.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = formater.format(tmp1.getTime());

        Calendar tmp2 = Calendar.getInstance();
        tmp2.setTime(formater.parse(sDate));
        tmp2.set(Calendar.HOUR, 0);
        tmp2.set(Calendar.MINUTE, 0);
        tmp2.set(Calendar.SECOND, 0);
        tmp2.set(Calendar.MILLISECOND, 0);

        assertEquals(tmp1, tmp2);
    }

}
