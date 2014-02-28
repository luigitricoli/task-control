package br.com.egs.task.control.web.rest.client.task;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

class TaskCalendar implements Comparable<TaskCalendar> {
	
	private Calendar date;
	
	public TaskCalendar(Calendar date) {
		this.date = (Calendar) date.clone();
	}
	
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
	
	public Calendar toCalendar(){
		return (Calendar) date.clone();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){			
			return true;
		}
		if(obj instanceof TaskCalendar){			
			TaskCalendar other = (TaskCalendar) obj;
			if (!date.equals(other.date)){				
				return false;
			} 
			return true;
		}
		return false;
	}

	@Override
	public int compareTo(TaskCalendar o) {
		return date.compareTo(o.date);
	}
	
	@Override
	public String toString() {
		return date.toString();
	}
	
	public static class JsonUnmarshaller implements JsonDeserializer<TaskCalendar> {

		@Override
		public TaskCalendar deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
			try {
				return new TaskCalendar(jsonElement.getAsString());
			} catch (ParseException e) {
				throw new JsonParseException(e);
			}
		}
	}

}
