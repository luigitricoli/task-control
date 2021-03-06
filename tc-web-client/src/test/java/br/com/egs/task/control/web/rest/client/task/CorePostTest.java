package br.com.egs.task.control.web.rest.client.task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CorePostTest {
	
	@Test
	public void unmarshal(){
		String json = "{\"timestamp\":\"2014-01-08 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #overtime to finish it sooner\"}";
		CorePost post = parse().fromJson(json, CorePost.class);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar timestamp = Calendar.getInstance();
		try {
			timestamp.setTime(format.parse("2014-01-08 18:20:49"));
		} catch (ParseException e) {
			throw new JsonParseException(e);
		}		
		
		assertThat(post.getTimestamp(), is(timestamp));
		assertThat(post.getLogin(), is("john"));
		assertThat(post.getName(), is("John Programmer"));
		assertThat(post.getText(), is("Doing #overtime to finish it sooner"));
	}
	
	@Test
	public void unmarshalAList(){
		String json = "[{\"timestamp\":\"2014-01-03 09:15:30\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"login\":\"john\",\"name\":\"John Programmer\",\"text\":\"Doing #overtime to finish it sooner\"}]";
		
		List<CorePost> posts = parse().fromJson(json, new TypeToken<List<CorePost>>() {
		}.getType());
		
		assertThat(posts.size(), is(2));
	}
	
	private static Gson parse() {
		GsonBuilder gson = new GsonBuilder();
		gson.registerTypeAdapter(CorePost.class, new CorePost.JsonUnmarshaller());
		return gson.create();
	}

}
