package br.com.egs.task.control.web.controller;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.egs.task.control.web.interceptor.AuthRequired;
import br.com.egs.task.control.web.model.calendar.ReferenceMonth;

import java.util.Calendar;

@Resource
@AuthRequired
public class CalendarsController {
	
	private Result result;
	
	public CalendarsController(Result result) {
        this.result = result;
	}
	
	@Get("/calendario/mes/{month}")
	public void monthDays(Integer month){
        int year = Calendar.getInstance().get(Calendar.YEAR);
        ReferenceMonth structure = new ReferenceMonth(month,year);
		result.use(Results.http()).body(structure.getDaysAsJson());
	}

}
