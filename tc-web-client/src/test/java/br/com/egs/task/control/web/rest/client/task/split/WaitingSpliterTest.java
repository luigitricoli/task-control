package br.com.egs.task.control.web.rest.client.task.split;

import br.com.egs.task.control.web.model.Hashtags;
import br.com.egs.task.control.web.model.OneWeekTask;
import br.com.egs.task.control.web.model.Stage;
import br.com.egs.task.control.web.model.exception.InvalidDateException;
import br.com.egs.task.control.web.rest.client.task.CoreTask;
import br.com.egs.task.control.web.rest.client.task.TaskDate;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class WaitingSpliterTest {

    @Test
    public void oneWeekTaskWaiting() throws InvalidDateException {
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-01-14\",\"foreseenEndDate\":\"2014-01-16\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new WaitingSpliter(new TaskDate("2014-01-01"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask expected = new OneWeekTask("52f518377cf06f3be158a352", 3, 3, 0, Stage.WAITING, "My First CoreTask", new HashMap<Integer, Hashtags>(), false, false);
        assertThat(spliter.thirdWeek(), equalTo(expected));
    }

    @Test
    public void moreThaOneWeekTaskWaiting() throws InvalidDateException {
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-01-14\",\"foreseenEndDate\":\"2014-01-22\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        OneWeekTask first = new OneWeekTask("52f518377cf06f3be158a352", 3, 4, 0, Stage.WAITING, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, false);
        OneWeekTask second = new OneWeekTask("52f518377cf06f3be158a352", 2, 3, 0, Stage.WAITING, "My First CoreTask", new HashMap<Integer, Hashtags>(), false, true);

        TaskSpliter spliter = new WaitingSpliter(new TaskDate("2014-01-01"));
        spliter.split(CoreTask.unmarshal(json));

        assertThat(spliter.thirdWeek(), equalTo(first));
        assertThat(spliter.fourthWeek(), equalTo(second));
    }

    @Test
    public void taskCrossMonthTodayInFirstMonth() throws InvalidDateException{
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-05-05\",\"foreseenEndDate\":\"2014-07-23\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new WaitingSpliter(new TaskDate("2014-05-01"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask task = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 0, Stage.WAITING, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, false);
        OneWeekTask middle = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 0, Stage.WAITING, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, true);

        Assert.assertThat(spliter.firstWeek(), nullValue());
        Assert.assertThat(spliter.secondWeek(), equalTo(task));
        Assert.assertThat(spliter.thirdWeek(), equalTo(middle));
        Assert.assertThat(spliter.fourthWeek(), equalTo(middle));
        Assert.assertThat(spliter.fifthWeek(), equalTo(middle));
        Assert.assertThat(spliter.sixthWeek(), equalTo(middle));
    }

    @Test
    public void taskCrossMonthTodayInSecondMonth() throws InvalidDateException{
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-05-05\",\"foreseenEndDate\":\"2014-07-23\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new WaitingSpliter(new TaskDate("2014-06-01"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask middle = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 0, Stage.WAITING, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, true);

        Assert.assertThat(spliter.firstWeek(), equalTo(middle));
        Assert.assertThat(spliter.secondWeek(), equalTo(middle));
        Assert.assertThat(spliter.thirdWeek(), equalTo(middle));
        Assert.assertThat(spliter.fourthWeek(), equalTo(middle));
        Assert.assertThat(spliter.fifthWeek(), equalTo(middle));
        Assert.assertThat(spliter.sixthWeek(), equalTo(middle));
    }

    @Test
    public void taskCrossMonthTodayInLastMonth() throws InvalidDateException {
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-05-05\",\"foreseenEndDate\":\"2014-07-23\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new WaitingSpliter(new TaskDate("2014-07-01"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask middle = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 0, Stage.WAITING, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, true);
        OneWeekTask last = new OneWeekTask("52f518377cf06f3be158a352", 2, 3, 0, Stage.WAITING, "My First CoreTask", new HashMap<Integer, Hashtags>(), false, true);

        Assert.assertThat(spliter.firstWeek(), equalTo(middle));
        Assert.assertThat(spliter.secondWeek(), equalTo(middle));
        Assert.assertThat(spliter.thirdWeek(), equalTo(middle));
        Assert.assertThat(spliter.fourthWeek(), equalTo(last));
        Assert.assertThat(spliter.fifthWeek(), nullValue());
        Assert.assertThat(spliter.sixthWeek(), nullValue());
    }

}
