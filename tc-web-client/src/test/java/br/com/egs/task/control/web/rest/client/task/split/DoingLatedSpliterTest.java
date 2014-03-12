package br.com.egs.task.control.web.rest.client.task.split;

import br.com.egs.task.control.web.model.Hashtags;
import br.com.egs.task.control.web.model.OneWeekTask;
import br.com.egs.task.control.web.model.Stage;
import br.com.egs.task.control.web.rest.client.task.CoreTask;
import br.com.egs.task.control.web.rest.client.task.TaskDate;
import org.junit.Test;

import java.text.ParseException;
import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class DoingLatedSpliterTest {

    @Test
    public void taskOfOneWeeks() throws ParseException {
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-03-10\",\"foreseenEndDate\":\"2014-03-11\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"user\":\"john\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"user\":\"john\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new DoingLatedSpliter(new TaskDate("2014-03-01"), new TaskDate("2014-03-12"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask expected = new OneWeekTask("52f518377cf06f3be158a352", 2, 3, 2, Stage.LATE, "My First CoreTask", new HashMap<Integer, Hashtags>(), false, false);
        assertThat(spliter.thirdWeek(), equalTo(expected));
    }

    @Test
    public void taskOfOneWeekTodayInNextWeek() throws ParseException{
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-03-04\",\"foreseenEndDate\":\"2014-03-07\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"user\":\"john\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"user\":\"john\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new DoingLatedSpliter(new TaskDate("2014-03-01"), new TaskDate("2014-03-12"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask first = new OneWeekTask("52f518377cf06f3be158a352", 3, 4, 4, Stage.LATE, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, false);
        OneWeekTask second = new OneWeekTask("52f518377cf06f3be158a352", 2, 3, 0, Stage.LATE, "My First CoreTask", new HashMap<Integer, Hashtags>(), false, true);

        assertThat(spliter.secondWeek(), equalTo(first));
        assertThat(spliter.thirdWeek(), equalTo(second));
    }

    @Test
    public void taskOfTwoWeeksTodayInSecondWeek() throws ParseException{
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-01-14\",\"foreseenEndDate\":\"2014-01-22\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"user\":\"john\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"user\":\"john\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new DoingLatedSpliter(new TaskDate("2014-01-01"), new TaskDate("2014-01-23"));
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask first = new OneWeekTask("52f518377cf06f3be158a352", 3, 4, 4, Stage.LATE, "My First CoreTask", new HashMap<Integer, Hashtags>(), true, false);
        OneWeekTask second = new OneWeekTask("52f518377cf06f3be158a352", 2, 4, 3, Stage.LATE, "My First CoreTask", new HashMap<Integer, Hashtags>(), false, true);

        assertThat(spliter.thirdWeek(), equalTo(first));
        assertThat(spliter.fourthWeek(), equalTo(second));
    }

}
