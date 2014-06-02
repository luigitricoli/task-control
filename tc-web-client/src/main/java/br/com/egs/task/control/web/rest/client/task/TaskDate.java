package br.com.egs.task.control.web.rest.client.task;

import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TaskDate implements Comparable<TaskDate> {

    private static final Logger log = LoggerFactory.getLogger(TaskDate.class);

	private Calendar date;
	
	public TaskDate() {
		this.date = Calendar.getInstance();
	}

    public TaskDate(Calendar date) {
        this.date = (Calendar) date.clone();
    }

	public TaskDate(String date) throws ParseException {
		this(date, new SimpleDateFormat("yyyy-MM-dd"));
	}
	
	public TaskDate(String date, SimpleDateFormat format) throws ParseException {
        this.date = Calendar.getInstance();
        this.date.setTime(format.parse(date));
	}

    public Integer getMonth(){
        return date.get(Calendar.MONTH);
    }

	public Integer getWeekOfMonth(){
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
		if(obj instanceof TaskDate){
			TaskDate other = (TaskDate) obj;
			if (!date.equals(other.date)){				
				return false;
			} 
			return true;
		}
		return false;
	}

	@Override
	public int compareTo(TaskDate o) {
		return date.compareTo(o.date);
	}
	
	@Override
	public String toString() {
		return date.toString();
	}
	
	public static class JsonUnmarshaller implements JsonDeserializer<TaskDate> {

        @Override
        public TaskDate deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            try {
                return new TaskDate(jsonElement.getAsString());
            } catch (ParseException e) {
                throw new JsonParseException(e);
            }
        }
    }

    public static class JsonMarshaller implements JsonSerializer<TaskDate> {

        @Override
        public JsonElement serialize(TaskDate src, Type typeOfSrc, JsonSerializationContext context) {
            String value = String.format("%s-%s-%s", src.date.get(Calendar.YEAR), src.date.get(Calendar.MONTH) + 1, src.date.get(Calendar.DAY_OF_MONTH));
            return new JsonPrimitive(value);
        }
    }

}
