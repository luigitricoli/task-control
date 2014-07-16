package br.com.egs.task.control.web.rest.client.task.split;

import br.com.egs.task.control.web.model.Hashtag;
import br.com.egs.task.control.web.model.Hashtags;
import br.com.egs.task.control.web.model.OneWeekTask;
import br.com.egs.task.control.web.model.Stage;
import br.com.egs.task.control.web.model.exception.InvalidDateException;
import br.com.egs.task.control.web.rest.client.task.CoreTask;
import br.com.egs.task.control.web.rest.client.task.TaskDate;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class FinishedSpliterTest {

    @Test
    public void moreThaOneWeekTaskFinishedEarlyInSecondWeek() throws InvalidDateException {
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-02-03\",\"endDate\":\"2014-02-10\",\"foreseenEndDate\":\"2014-02-12\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new FinishedSpliter(new TaskDate("2014-02-01"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask first = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 5, Stage.FINISHED, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, false);
        OneWeekTask second = new OneWeekTask("52f518377cf06f3be158a352", 2, 3, 1, Stage.FINISHED, "My First CoreTask", new HashMap<Integer, Hashtags>(), false, true);

        assertThat(spliter.secondWeek(), equalTo(first));
        assertThat(spliter.thirdWeek(), equalTo(second));
    }

    @Test
    public void oneWeekTaskFinishedEarly() throws InvalidDateException {
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-01-06\",\"endDate\":\"2014-01-09\",\"foreseenEndDate\":\"2014-01-10\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new FinishedSpliter(new TaskDate("2014-01-01"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask expected = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 4, Stage.FINISHED, "My First CoreTask", new HashMap<Integer, Hashtags>(), false, false);
        assertThat(spliter.secondWeek(), equalTo(expected));
    }

    @Test
    public void moreThaOneWeekTaskFinishedEarlyInFirstWeek() throws InvalidDateException{
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-02-03\",\"endDate\":\"2014-02-06\",\"foreseenEndDate\":\"2014-02-12\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new FinishedSpliter(new TaskDate("2014-02-01"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask first = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 4, Stage.FINISHED, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, false);
        OneWeekTask second = new OneWeekTask("52f518377cf06f3be158a352", 2, 3, 0, Stage.FINISHED, "My First CoreTask", new HashMap<Integer, Hashtags>(), false, true);

        assertThat(spliter.secondWeek(), equalTo(first));
        assertThat(spliter.thirdWeek(), equalTo(second));
    }

    @Test
    public void oneWeekTaskFinishedLate() throws InvalidDateException {
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-03-10\",\"endDate\":\"2014-03-12\",\"foreseenEndDate\":\"2014-03-11\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new FinishedSpliter(new TaskDate("2014-03-12"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask expected = new OneWeekTask("52f518377cf06f3be158a352", 2, 3, 3, Stage.FINISHED, "My First CoreTask", new HashMap<Integer, Hashtags>(), false, false);
        assertThat(spliter.thirdWeek(), equalTo(expected));
    }

    @Test
    public void moreThaOneWeekTaskFinishedLate() throws InvalidDateException{
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-02-19\",\"endDate\":\"2014-02-25\",\"foreseenEndDate\":\"2014-02-20\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new FinishedSpliter(new TaskDate("2014-02-01"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask first = new OneWeekTask("52f518377cf06f3be158a352", 4, 3, 3, Stage.FINISHED, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, false);
        OneWeekTask second = new OneWeekTask("52f518377cf06f3be158a352", 2, 2, 2, Stage.FINISHED, "My First CoreTask", new HashMap<Integer, Hashtags>(), false, true);

        assertThat(spliter.fourthWeek(), equalTo(first));
        assertThat(spliter.fifthWeek(), equalTo(second));
    }

    //@Test
    public void overTimeHashtagOneWeek() throws InvalidDateException {
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-01-06\",\"endDate\":\"2014-01-10\",\"foreseenEndDate\":\"2014-01-10\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-07 09:15:30\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-07 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #horaextra to finish it sooner\"}]}";

        TaskSpliter spliter = new FinishedSpliter(new TaskDate("2014-01-01"));
        spliter.split(CoreTask.unmarshal(json));

        Hashtags hashtags = new Hashtags(Hashtag.OVERTIME);
        Map<Integer, Hashtags> daysInfo = new HashMap<Integer, Hashtags>();
        daysInfo.put(3, hashtags);

        OneWeekTask expected = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 5, Stage.FINISHED, "My First CoreTask", daysInfo, false, false);
        assertThat(spliter.secondWeek(), equalTo(expected));
    }

    //@Test
    public void lateHashtagOneWeek() throws InvalidDateException {
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-01-06\",\"endDate\":\"2014-01-10\",\"foreseenEndDate\":\"2014-01-10\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-07 09:15:30\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-07 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #atraso to finish it sooner\"}]}";

        TaskSpliter spliter = new FinishedSpliter(new TaskDate("2014-01-01"));
        spliter.split(CoreTask.unmarshal(json));

        Hashtags hashtags = new Hashtags(Hashtag.LATE);
        Map<Integer, Hashtags> daysInfo = new HashMap<Integer, Hashtags>();
        daysInfo.put(3, hashtags);

        OneWeekTask expected = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 5, Stage.FINISHED, "My First CoreTask", daysInfo, false, false);
        assertThat(spliter.secondWeek(), equalTo(expected));
    }

    //@Test
    public void hashtagsTwoWeek() throws InvalidDateException {
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-01-06\",\"endDate\":\"2014-01-14\",\"foreseenEndDate\":\"2014-01-14\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-07 09:15:30\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-07 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #atraso to finish it sooner\"},{\"timestamp\":\"2014-01-13 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #horaextra to finish it sooner\"}]}";

        TaskSpliter spliter = new FinishedSpliter(new TaskDate("2014-01-01"));
        spliter.split(CoreTask.unmarshal(json));

        Map<Integer, Hashtags> firstDaysInfo = new HashMap<Integer, Hashtags>();
        firstDaysInfo.put(3, new Hashtags(Hashtag.LATE));
        OneWeekTask first = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 5, Stage.FINISHED, "My First CoreTask", firstDaysInfo, true, false);

        Map<Integer, Hashtags> secondDaysInfo = new HashMap<Integer, Hashtags>();
        secondDaysInfo.put(2, new Hashtags(Hashtag.OVERTIME));
        OneWeekTask second = new OneWeekTask("52f518377cf06f3be158a352", 2, 2, 2, Stage.FINISHED, "My First CoreTask", secondDaysInfo, false, true);

        assertThat(spliter.secondWeek(), equalTo(first));
        assertThat(spliter.thirdWeek(), equalTo(second));
    }

    //@Test
    public void threeWeeksTaskHashtagInLast() throws InvalidDateException {
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-03-20\",\"endDate\":\"2014-03-31\",\"foreseenEndDate\":\"2014-03-31\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-03-23 09:15:30\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-03-31 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #atraso to finish it sooner\"},{\"timestamp\":\"2014-03-31 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #horaextra to finish it sooner\"}]}";

        TaskSpliter spliter = new FinishedSpliter(new TaskDate("2014-03-31"));
        spliter.split(CoreTask.unmarshal(json));

        Map<Integer, Hashtags> hashtags = new HashMap<Integer, Hashtags>();
        hashtags.put(3, new Hashtags(Hashtag.LATE));
        hashtags.put(2, new Hashtags(Hashtag.OVERTIME));
        OneWeekTask third = new OneWeekTask("52f518377cf06f3be158a352", 2, 1, 1, Stage.FINISHED, "My First CoreTask", hashtags, false, true);

        assertThat(spliter.sixthWeek(), equalTo(third));
    }

    @Test
    public void taskCrossMonthTodayInFirstMonth() throws InvalidDateException{
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-02-03\",\"foreseenEndDate\":\"2014-02-25\",\"endDate\":\"2014-04-14\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new FinishedSpliter(new TaskDate("2014-02-01"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask head = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 5, Stage.FINISHED, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, false);
        OneWeekTask middle = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 5, Stage.FINISHED, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, true);

        Assert.assertThat(spliter.firstWeek(), nullValue());
        Assert.assertThat(spliter.secondWeek(), equalTo(head));
        Assert.assertThat(spliter.thirdWeek(), equalTo(middle));
        Assert.assertThat(spliter.fourthWeek(), equalTo(middle));
        Assert.assertThat(spliter.fifthWeek(), equalTo(middle));
        Assert.assertThat(spliter.sixthWeek(), equalTo(middle));
    }

    @Test
    public void taskCrossMonthTodayInSecondMonth() throws InvalidDateException{
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-02-03\",\"foreseenEndDate\":\"2014-02-25\",\"endDate\":\"2014-04-14\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new FinishedSpliter(new TaskDate("2014-03-01"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask middle = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 5, Stage.FINISHED, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, true);

        Assert.assertThat(spliter.firstWeek(), equalTo(middle));
        Assert.assertThat(spliter.secondWeek(), equalTo(middle));
        Assert.assertThat(spliter.thirdWeek(), equalTo(middle));
        Assert.assertThat(spliter.fourthWeek(), equalTo(middle));
        Assert.assertThat(spliter.fifthWeek(), equalTo(middle));
        Assert.assertThat(spliter.sixthWeek(), equalTo(middle));
    }

    @Test
    public void taskCrossMonthTodayInLastMonth() throws InvalidDateException{
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-02-03\",\"foreseenEndDate\":\"2014-02-25\",\"endDate\":\"2014-04-14\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new FinishedSpliter(new TaskDate("2014-04-01"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask middle = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 5, Stage.FINISHED, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, true);
        OneWeekTask late = new OneWeekTask("52f518377cf06f3be158a352", 2, 1, 1, Stage.FINISHED, "My First CoreTask", new HashMap<Integer, Hashtags>(), false, true);

        Assert.assertThat(spliter.firstWeek(), equalTo(middle));
        Assert.assertThat(spliter.secondWeek(), equalTo(middle));
        Assert.assertThat(spliter.thirdWeek(), equalTo(late));
        Assert.assertThat(spliter.fourthWeek(), nullValue());
        Assert.assertThat(spliter.fifthWeek(), nullValue());
        Assert.assertThat(spliter.sixthWeek(), nullValue());
    }
}
