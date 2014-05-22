package br.com.egs.task.control.web.rest.client.task;

import br.com.egs.task.control.web.rest.client.task.split.CoreUser;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CoreTaskTest {

	@Test
	public void unmarshal() throws ParseException{
		String json = "{\"id\":\"530e76ef7cf056f2dad8fd32\",\"description\":\"SR1393456879915\",\"startDate\":\"2014-02-20\",\"foreseenEndDate\":\"2014-02-25\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"user\":\"john\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"user\":\"john\",\"text\":\"Doing #overtime to finish it sooner\"}]}";
		CoreTask task = CoreTask.unmarshal(json);
		
		assertThat(task.getId(), is("530e76ef7cf056f2dad8fd32"));
		assertThat(task.getDescription(), is("SR1393456879915"));
		
		assertThat(task.getStartDate(), equalTo(new TaskDate("2014-02-20")));
		assertThat(task.getForeseenEndDate(), equalTo(new TaskDate("2014-02-25")));
		
		assertThat(task.getSource(), is("CCC"));
		assertThat(task.getApplication(), is("OLM"));
		
		assertThat(task.getOwners().size(), is(2));
		assertThat(task.getPosts().size(), is(2));
	}

    @Test
    public void marshal() throws ParseException{
        TaskDate date = new TaskDate("2014-02-20");

        CoreTask task = new CoreTask("530e76ef7cf056f2dad8fd32", date);
        assertThat(task.toJson(), is("{\"task\":{\"id\":\"530e76ef7cf056f2dad8fd32\",\"endDate\":\"2014-2-20\"}}"));
    }

    @Test
    public void marshalUnicode() throws ParseException{
        TaskDate date = new TaskDate("2014-02-20");
        List<CoreUser> users = new ArrayList<>();
        users.add(new CoreUser("luigi", null));

        CoreTask task = new CoreTask(date, date, "ção", "CCC", "OLM", users);

        String expected = "{\"task\":{\"description\":\"ção\",\"startDate\":\"2014-2-20\",\"foreseenEndDate\":\"2014-2-20\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"luigi\"}]}}";
        assertThat(task.toJson(), is(expected));
        assertThat(task.toJson().getBytes(), equalTo(expected.getBytes()));
    }
	
}
