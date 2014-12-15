package br.com.egs.task.control.web.rest.client.task;

import br.com.egs.task.control.web.model.exception.InvalidDateException;
import com.google.gson.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Calendar;

public class TaskDate implements Comparable<TaskDate> {

    private static final Logger log = LoggerFactory.getLogger(TaskDate.class);
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd";

    private DateTime time;
	
	public TaskDate() {
        this.time = new DateTime().withMillisOfDay(0);
	}

    public TaskDate(Calendar date) {
        this.time = new DateTime(date).withMillisOfDay(0);
    }

	public TaskDate(String date) throws InvalidDateException {
        this(date, DEFAULT_PATTERN);
	}
	
	public TaskDate(String date, String format) throws InvalidDateException {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(format);
        this.time = DateTime.parse(date, fmt);
    }

    public Integer getMonth(){
        return time.getMonthOfYear();
    }

	public Integer getWeekOfMonth(){
		return time.getWeekOfWeekyear() - time.withDayOfMonth(1).getWeekOfWeekyear();
	}
	
	public Integer getDayOfWeek(){
		return time.getDayOfWeek() % 7 != 0 ? time.getDayOfWeek() + 1 : 1;
	}
	
	public Calendar toCalendar(){
		return time.toGregorianCalendar();
	}

    public DateTime toDateTime(){
        return time;
    }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){			
			return true;
		}
		if(obj instanceof TaskDate){
			TaskDate other = (TaskDate) obj;
			if (!time.equals(other.time)){
				return false;
			} 
			return true;
		}
		return false;
	}

	@Override
	public int compareTo(TaskDate o) {
		return time.compareTo(o.time);
	}
	
	@Override
	public String toString() {
        return time.toString(DEFAULT_PATTERN);
	}
	
	public static class JsonUnmarshaller implements JsonDeserializer<TaskDate> {

        @Override
        public TaskDate deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            try {
                return new TaskDate(jsonElement.getAsString());
            } catch (InvalidDateException e) {
                throw new JsonParseException(e);
            }
        }
    }

    public static class JsonMarshaller implements JsonSerializer<TaskDate> {

        @Override
        public JsonElement serialize(TaskDate src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

}
