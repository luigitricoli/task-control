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

public class DoingLatedSpliterTest {

    @Test
    public void taskOfOneWeeks() throws InvalidDateException {
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-03-10\",\"foreseenEndDate\":\"2014-03-11\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new DoingLatedSpliter(new TaskDate("2014-03-01"), new TaskDate("2014-03-12"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask expected = new OneWeekTask("52f518377cf06f3be158a352", 2, 3, 2, Stage.LATE, "My First CoreTask", new HashMap<Integer, Hashtags>(), false, false);
        assertThat(spliter.thirdWeek(), equalTo(expected));
    }

    @Test
    public void taskOfOneWeekTodayInNextWeek() throws InvalidDateException{
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-03-04\",\"foreseenEndDate\":\"2014-03-07\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new DoingLatedSpliter(new TaskDate("2014-03-01"), new TaskDate("2014-03-12"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask first = new OneWeekTask("52f518377cf06f3be158a352", 3, 4, 4, Stage.LATE, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, false);
        OneWeekTask second = new OneWeekTask("52f518377cf06f3be158a352", 2, 3, 0, Stage.LATE, "My First CoreTask", new HashMap<Integer, Hashtags>(), false, true);

        assertThat(spliter.secondWeek(), equalTo(first));
        assertThat(spliter.thirdWeek(), equalTo(second));
    }

    @Test
    public void taskOfTwoWeeksTodayInSecondWeek() throws InvalidDateException{
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-01-14\",\"foreseenEndDate\":\"2014-01-22\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new DoingLatedSpliter(new TaskDate("2014-01-01"), new TaskDate("2014-01-23"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask first = new OneWeekTask("52f518377cf06f3be158a352", 3, 4, 4, Stage.LATE, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, false);
        OneWeekTask second = new OneWeekTask("52f518377cf06f3be158a352", 2, 4, 3, Stage.LATE, "My First CoreTask", new HashMap<Integer, Hashtags>(), false, true);

        assertThat(spliter.thirdWeek(), equalTo(first));
        assertThat(spliter.fourthWeek(), equalTo(second));
    }

    @Test
    public void taskCrossMonthTodayInFirstMonth() throws InvalidDateException{
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-02-03\",\"foreseenEndDate\":\"2014-02-25\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new DoingLatedSpliter(new TaskDate("2014-02-01"), new TaskDate("2014-04-14"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask head = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 5, Stage.LATE, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, false);
        OneWeekTask done = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 5, Stage.LATE, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, true);
        OneWeekTask last = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 2, Stage.LATE, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, true);
        OneWeekTask late = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 0, Stage.LATE, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, true);

        Assert.assertThat(spliter.firstWeek(), nullValue());
        Assert.assertThat(spliter.secondWeek(), equalTo(head));
        Assert.assertThat(spliter.thirdWeek(), equalTo(done));
        Assert.assertThat(spliter.fourthWeek(), equalTo(done));
        Assert.assertThat(spliter.fifthWeek(), equalTo(last));
        Assert.assertThat(spliter.sixthWeek(), equalTo(late));
    }

    @Test
    public void taskCrossMonthTodayInSecondMonth() throws InvalidDateException{
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-02-03\",\"foreseenEndDate\":\"2014-02-25\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new DoingLatedSpliter(new TaskDate("2014-03-01"), new TaskDate("2014-04-14"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask late = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 0, Stage.LATE, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, true);

        Assert.assertThat(spliter.firstWeek(), equalTo(late));
        Assert.assertThat(spliter.secondWeek(), equalTo(late));
        Assert.assertThat(spliter.thirdWeek(), equalTo(late));
        Assert.assertThat(spliter.fourthWeek(), equalTo(late));
        Assert.assertThat(spliter.fifthWeek(), equalTo(late));
        Assert.assertThat(spliter.sixthWeek(), equalTo(late));
    }

    @Test
    public void taskCrossMonthTodayInLastMonth() throws InvalidDateException{
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-02-03\",\"foreseenEndDate\":\"2014-02-25\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new DoingLatedSpliter(new TaskDate("2014-04-01"), new TaskDate("2014-04-14"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask late = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 0, Stage.LATE, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, true);
        OneWeekTask current = new OneWeekTask("52f518377cf06f3be158a352", 2, 1, 0, Stage.LATE, "My First CoreTask", new HashMap<Integer, Hashtags>(), false, true);

        Assert.assertThat(spliter.firstWeek(), equalTo(late));
        Assert.assertThat(spliter.secondWeek(), equalTo(late));
        Assert.assertThat(spliter.thirdWeek(), equalTo(current));
        Assert.assertThat(spliter.fourthWeek(), nullValue());
        Assert.assertThat(spliter.fifthWeek(), nullValue());
        Assert.assertThat(spliter.sixthWeek(), nullValue());
    }

}
