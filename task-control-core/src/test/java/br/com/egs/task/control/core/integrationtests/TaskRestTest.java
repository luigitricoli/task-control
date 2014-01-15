package br.com.egs.task.control.core.integrationtests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.Restfulie;

public class TaskRestTest {

	@Test
	public void getTask() {
		RestClient restfulie = Restfulie.custom();
		Response response = restfulie.at("http://localhost:8090/v1/tasks").accept("application/json").get();
		assertEquals("{\"description\":\"Change Fluxo GPRS Ericsson\"}", response.getContent());
	}

}
