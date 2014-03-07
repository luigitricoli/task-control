package br.com.egs.task.control.web.rest.client.task;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.text.ParseException;

import org.junit.Test;

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
	
}
