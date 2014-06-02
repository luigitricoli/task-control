package br.com.egs.task.control.web.rest.client.task.split;

import br.com.egs.task.control.web.model.Hashtags;
import br.com.egs.task.control.web.model.OneWeekTask;
import br.com.egs.task.control.web.model.Stage;
import br.com.egs.task.control.web.rest.client.task.CoreTask;
import br.com.egs.task.control.web.rest.client.task.TaskDate;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class DoingSpliterTest {

    @Test
    public void taskOfOneWeek() throws ParseException {
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-01-06\",\"foreseenEndDate\":\"2014-01-10\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"user\":\"john\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"user\":\"john\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new DoingSpliter(new TaskDate("2014-01-01"), new TaskDate("2014-01-08"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask expected =  new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 3, Stage.DOING, "My First CoreTask", new HashMap<Integer, Hashtags>(), false, false);
        assertThat(spliter.secondWeek(), equalTo(expected));
    }

    @Test
    public void taskOfTwoWeeksTodayInFirstWeek() throws ParseException{
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-01-06\",\"foreseenEndDate\":\"2014-01-16\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"user\":\"john\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"user\":\"john\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new DoingSpliter(new TaskDate("2014-01-01"), new TaskDate("2014-01-09"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask first = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 4, Stage.DOING, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, false);
        OneWeekTask second = new OneWeekTask("52f518377cf06f3be158a352", 2, 4, 0, Stage.DOING, "My First CoreTask", new HashMap<Integer, Hashtags>(), false, true);

        assertThat(spliter.secondWeek(), equalTo(first));
        assertThat(spliter.thirdWeek(), equalTo(second));
    }

    @Test
    public void taskOfTwoWeeksTodayInSecondWeek() throws ParseException{
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-01-06\",\"foreseenEndDate\":\"2014-01-16\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"user\":\"john\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"user\":\"john\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new DoingSpliter(new TaskDate("2014-01-01"), new TaskDate("2014-01-14"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask first = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 5, Stage.DOING, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, false);
        OneWeekTask second = new OneWeekTask("52f518377cf06f3be158a352", 2, 4, 2, Stage.DOING, "My First CoreTask", new HashMap<Integer, Hashtags>(), false, true);

        assertThat(spliter.secondWeek(), equalTo(first));
        assertThat(spliter.thirdWeek(), equalTo(second));
    }


    @Test
    public void taskCrossMonthTodayInFirstMonth() throws ParseException{
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-02-03\",\"foreseenEndDate\":\"2014-04-25\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"user\":\"john\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"user\":\"john\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new DoingSpliter(new TaskDate("2014-02-01"), new TaskDate("2014-04-14"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask head = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 5, Stage.DOING, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, false);
        OneWeekTask middle = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 5, Stage.DOING, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, true);

        Assert.assertThat(spliter.firstWeek(), nullValue());
        Assert.assertThat(spliter.secondWeek(), equalTo(head));
        Assert.assertThat(spliter.thirdWeek(), equalTo(middle));
        Assert.assertThat(spliter.fourthWeek(), equalTo(middle));
        Assert.assertThat(spliter.fifthWeek(), equalTo(middle));
        Assert.assertThat(spliter.sixthWeek(), equalTo(middle));
    }

    @Test
    public void taskCrossMonthTodayInSecondMonth() throws ParseException{
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-02-03\",\"foreseenEndDate\":\"2014-04-25\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"user\":\"john\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"user\":\"john\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new DoingSpliter(new TaskDate("2014-03-01"), new TaskDate("2014-04-14"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask middle = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 5, Stage.DOING, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, true);

        Assert.assertThat(spliter.firstWeek(), equalTo(middle));
        Assert.assertThat(spliter.secondWeek(), equalTo(middle));
        Assert.assertThat(spliter.thirdWeek(), equalTo(middle));
        Assert.assertThat(spliter.fourthWeek(), equalTo(middle));
        Assert.assertThat(spliter.fifthWeek(), equalTo(middle));
        Assert.assertThat(spliter.sixthWeek(), equalTo(middle));
    }

    @Test
    public void taskCrossMonthTodayInLastMonth() throws ParseException{
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-02-03\",\"foreseenEndDate\":\"2014-04-25\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"user\":\"john\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"user\":\"john\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new DoingSpliter(new TaskDate("2014-04-01"), new TaskDate("2014-04-14"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask middle = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 5, Stage.DOING, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, true);
        OneWeekTask last = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 1, Stage.DOING, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, true);
        OneWeekTask waitting = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 0, Stage.DOING, "My First CoreTask", new HashMap<Integer, Hashtags>(), false, true);

        Assert.assertThat(spliter.firstWeek(), equalTo(middle));
        Assert.assertThat(spliter.secondWeek(), equalTo(middle));
        Assert.assertThat(spliter.thirdWeek(), equalTo(last));
        Assert.assertThat(spliter.fourthWeek(), equalTo(waitting));
        Assert.assertThat(spliter.fifthWeek(), nullValue());
        Assert.assertThat(spliter.sixthWeek(), nullValue());
    }

}
