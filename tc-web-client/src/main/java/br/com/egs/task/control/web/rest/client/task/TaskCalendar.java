package br.com.egs.task.control.web.rest.client.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TaskCalendar {
	
	private Calendar date;
	
	public TaskCalendar(String date) throws ParseException {
		this(date, new SimpleDateFormat("yyyy-MM-dd"));
	}
	
	public TaskCalendar(String date, SimpleDateFormat format) throws ParseException {
        this.date = Calendar.getInstance();
        this.date.setTime(format.parse(date));
	}
	
	public Integer getWeekOfYear(){
		return date.get(Calendar.WEEK_OF_MONTH) - 1;
	}
	
	public Integer getDayOfWeek(){
		return date.get(Calendar.DAY_OF_WEEK);	
	}
	
	@Override
	public String toString() {
		return date.toString();
	}

}
