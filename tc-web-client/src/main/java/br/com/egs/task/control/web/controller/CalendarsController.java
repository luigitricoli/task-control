package br.com.egs.task.control.web.controller;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.egs.task.control.web.model.calendar.MonthStructure;

import java.util.Calendar;

@Resource
public class CalendarsController {
	
	private Result result;
	
	public CalendarsController(Result result) {
        this.result = result;
	}
	
	@Get("/calendario/mes/{month}")
	public void monthDays(Integer month){
        int year = Calendar.getInstance().get(Calendar.YEAR);
		result.use(Results.http()).body(new MonthStructure(month,year).getDaysAsJson());
	}

}
