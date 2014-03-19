package br.com.egs.task.control.web.rest.client.task.split;

import br.com.egs.task.control.web.model.Hashtag;
import br.com.egs.task.control.web.model.Hashtags;
import br.com.egs.task.control.web.model.OneWeekTask;
import br.com.egs.task.control.web.model.Stage;
import br.com.egs.task.control.web.rest.client.task.CoreTask;
import br.com.egs.task.control.web.rest.client.task.TaskDate;
import org.junit.Test;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class FinishedSpliterTest {

    @Test
    public void moreThaOneWeekTaskFinishedEarlyInSecondWeek() throws ParseException {
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-02-03\",\"endDate\":\"2014-02-10\",\"foreseenEndDate\":\"2014-02-12\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"user\":\"john\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"user\":\"john\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new FinishedSpliter(new TaskDate("2014-02-01"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask first = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 5, Stage.FINISHED, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, false);
        OneWeekTask second = new OneWeekTask("52f518377cf06f3be158a352", 2, 3, 1, Stage.FINISHED, "My First CoreTask", new HashMap<Integer, Hashtags>(), false, true);

        assertThat(spliter.secondWeek(), equalTo(first));
        assertThat(spliter.thirdWeek(), equalTo(second));
    }

    @Test
    public void oneWeekTaskFinishedEarly() throws ParseException {
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-01-06\",\"endDate\":\"2014-01-09\",\"foreseenEndDate\":\"2014-01-10\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"user\":\"john\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"user\":\"john\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new FinishedSpliter(new TaskDate("2014-01-01"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask expected = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 4, Stage.FINISHED, "My First CoreTask", new HashMap<Integer, Hashtags>(), false, false);
        assertThat(spliter.secondWeek(), equalTo(expected));
    }

    @Test
    public void moreThaOneWeekTaskFinishedEarlyInFirstWeek() throws ParseException{
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-02-03\",\"endDate\":\"2014-02-06\",\"foreseenEndDate\":\"2014-02-12\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"user\":\"john\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"user\":\"john\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new FinishedSpliter(new TaskDate("2014-02-01"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask first = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 4, Stage.FINISHED, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, false);
        OneWeekTask second = new OneWeekTask("52f518377cf06f3be158a352", 2, 3, 0, Stage.FINISHED, "My First CoreTask", new HashMap<Integer, Hashtags>(), false, true);

        assertThat(spliter.secondWeek(), equalTo(first));
        assertThat(spliter.thirdWeek(), equalTo(second));
    }

    @Test
    public void oneWeekTaskFinishedLate() throws ParseException {
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-03-10\",\"endDate\":\"2014-03-12\",\"foreseenEndDate\":\"2014-03-11\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"user\":\"john\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"user\":\"john\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new FinishedSpliter(new TaskDate("2014-03-12"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask expected = new OneWeekTask("52f518377cf06f3be158a352", 2, 3, 3, Stage.FINISHED, "My First CoreTask", new HashMap<Integer, Hashtags>(), false, false);
        assertThat(spliter.thirdWeek(), equalTo(expected));
    }

    @Test
    public void moreThaOneWeekTaskFinishedLate() throws ParseException{
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-02-19\",\"endDate\":\"2014-02-25\",\"foreseenEndDate\":\"2014-02-20\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"user\":\"john\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"user\":\"john\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new FinishedSpliter(new TaskDate("2014-02-01"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask first = new OneWeekTask("52f518377cf06f3be158a352", 4, 3, 3, Stage.FINISHED, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, false);
        OneWeekTask second = new OneWeekTask("52f518377cf06f3be158a352", 2, 2, 2, Stage.FINISHED, "My First CoreTask", new HashMap<Integer, Hashtags>(), false, true);

        assertThat(spliter.fourthWeek(), equalTo(first));
        assertThat(spliter.fifthWeek(), equalTo(second));
    }

    @Test
    public void overTimeHashtagOneWeek() throws ParseException {
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-01-06\",\"endDate\":\"2014-01-10\",\"foreseenEndDate\":\"2014-01-10\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-07 09:15:30\",\"user\":\"john\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-07 18:20:49\",\"user\":\"john\",\"text\":\"Doing #horaextra to finish it sooner\"}]}";

        TaskSpliter spliter = new FinishedSpliter(new TaskDate("2014-01-01"));
        spliter.split(CoreTask.unmarshal(json));

        Hashtags hashtags = new Hashtags(Hashtag.OVERTIME);
        Map<Integer, Hashtags> daysInfo = new HashMap<Integer, Hashtags>();
        daysInfo.put(3, hashtags);

        OneWeekTask expected = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 5, Stage.FINISHED, "My First CoreTask", daysInfo, false, false);
        assertThat(spliter.secondWeek(), equalTo(expected));
    }

    @Test
    public void lateHashtagOneWeek() throws ParseException {
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-01-06\",\"endDate\":\"2014-01-10\",\"foreseenEndDate\":\"2014-01-10\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-07 09:15:30\",\"user\":\"john\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-07 18:20:49\",\"user\":\"john\",\"text\":\"Doing #atraso to finish it sooner\"}]}";

        TaskSpliter spliter = new FinishedSpliter(new TaskDate("2014-01-01"));
        spliter.split(CoreTask.unmarshal(json));

        Hashtags hashtags = new Hashtags(Hashtag.LATE);
        Map<Integer, Hashtags> daysInfo = new HashMap<Integer, Hashtags>();
        daysInfo.put(3, hashtags);

        OneWeekTask expected = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 5, Stage.FINISHED, "My First CoreTask", daysInfo, false, false);
        assertThat(spliter.secondWeek(), equalTo(expected));
    }

    @Test
    public void hashtagsTwoWeek() throws ParseException {
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-01-06\",\"endDate\":\"2014-01-14\",\"foreseenEndDate\":\"2014-01-14\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-07 09:15:30\",\"user\":\"john\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-07 18:20:49\",\"user\":\"john\",\"text\":\"Doing #atraso to finish it sooner\"},{\"timestamp\":\"2014-01-13 18:20:49\",\"user\":\"john\",\"text\":\"Doing #horaextra to finish it sooner\"}]}";

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
}
