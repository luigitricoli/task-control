package br.com.egs.task.control.web.rest.client.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class OwnersUnmarshallerTest {

	@Test
	public void unmarshall(){
		List<String> owners = parser().fromJson("[{\"login\":\"john\"},{\"login\":\"mary\"}]", new TypeToken<List<String>>() {
		}.getType());
		
		assertThat(owners.get(0), equalTo("john"));
		assertThat(owners.get(1), equalTo("mary"));
	}
	
	@Test
	public void OneElementUnmarshall(){
		List<String> owners = parser().fromJson("[{\"login\":\"john\"}]", new TypeToken<List<String>>() {
		}.getType());
		
		assertThat(owners.get(0), equalTo("john"));
		assertThat(owners.size(), is(1));
	}

	private Gson parser() {
		GsonBuilder gson = new GsonBuilder();
		gson.registerTypeAdapter(new TypeToken<List<String>>() {
		}.getType(), new OwnersUnmarshaller());
		Gson parser = gson.create();
		return parser;
	}
}
