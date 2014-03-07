package br.com.egs.task.control.web.rest.client.task.split;

import br.com.egs.task.control.web.model.Hashtags;
import br.com.egs.task.control.web.model.OneWeekTask;
import br.com.egs.task.control.web.model.Stage;
import br.com.egs.task.control.web.rest.client.task.CoreTask;
import org.junit.Test;

import java.text.ParseException;
import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class WaitingSpliterTest {

    @Test
    public void oneWeekTaskWaiting() throws ParseException {
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-01-14\",\"foreseenEndDate\":\"2014-01-16\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"user\":\"john\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"user\":\"john\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        TaskSpliter spliter = new WaitingSpliter();
        spliter.split(CoreTask.unmarshal(json));

        OneWeekTask expected = new OneWeekTask("52f518377cf06f3be158a352", 3, 3, 0, Stage.WAITING, "My First CoreTask", new HashMap<Integer, Hashtags>());
        assertThat(spliter.thirdWeek(), equalTo(expected));
    }

    @Test
    public void moreThaOneWeekTaskWaiting() throws ParseException {
        String json = "{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-01-14\",\"foreseenEndDate\":\"2014-01-22\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"user\":\"john\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"user\":\"john\",\"text\":\"Doing #overtime to finish it sooner\"}]}";

        OneWeekTask first = new OneWeekTask("52f518377cf06f3be158a352", 3, 4, 0, Stage.WAITING, "My First CoreTask", new HashMap<Integer, Hashtags>());
        OneWeekTask second = new OneWeekTask("52f518377cf06f3be158a352", 2, 3, 0, Stage.WAITING, "My First CoreTask", new HashMap<Integer, Hashtags>());

        TaskSpliter spliter = new WaitingSpliter();
        spliter.split(CoreTask.unmarshal(json));

        assertThat(spliter.thirdWeek(), equalTo(first));
        assertThat(spliter.fourthWeek(), equalTo(second));
    }

}
