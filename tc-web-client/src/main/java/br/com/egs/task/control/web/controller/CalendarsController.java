package br.com.egs.task.control.web.controller;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.egs.task.control.web.interceptor.AuthRequired;
import br.com.egs.task.control.web.model.calendar.ReferenceMonth;

import javax.inject.Inject;
import java.util.Calendar;

@Controller
@AuthRequired
public class CalendarsController {

    @Inject
	private Result result;

	@Get("/calendario/mes/{date}")
	public void monthDays(String date){
        String[] datePartials = date.split("-");
        int month = Integer.valueOf(datePartials[0]);
        int year = Integer.valueOf(datePartials[1]);
        ReferenceMonth structure = new ReferenceMonth(month,year);
		result.use(Results.http()).body(structure.getDaysAsJson());
	}

}
