package br.com.egs.task.control.web.controller;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
public class CalendarsController {
	
	private Result result;
	
	public CalendarsController(Result result) {
        this.result = result;
	}
	
	@Get("/calendario/mes/{month}")
	public void monthDays(Integer month){
		if(month.equals(1)){			
			result.use(Results.http()).body("{\"days\":[\"27\",\"28\",\"29\",\"30\",\"31\",\"Nov 1\",\"2\","
					+ "\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\",\"10\",\"11\",\"12\",\"13\",\"14\",\"15\",\"16\","
					+ "\"17\",\"18\",\"Hoje 19\",\"20\",\"21\",\"22\",\"23\",\"24\",\"25\",\"26\",\"27\",\"28\",\"29\",\"30\","
					+ "\"Dez 1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\"]}");
		} else {
			result.use(Results.http()).body("{\"days\":[\"0\",\"0\",\"0\",\"0\",\"0\",\"Nov 1\",\"2\","
					+ "\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\",\"10\",\"11\",\"12\",\"13\",\"14\",\"15\",\"16\","
					+ "\"17\",\"18\",\"Hoje 19\",\"20\",\"21\",\"22\",\"23\",\"24\",\"25\",\"26\",\"27\",\"28\",\"29\",\"30\","
					+ "\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\"]}");
			
		}
	} 

}
