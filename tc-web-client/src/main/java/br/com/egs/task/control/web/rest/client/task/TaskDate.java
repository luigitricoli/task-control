package br.com.egs.task.control.web.rest.client.task;

import br.com.egs.task.control.web.model.exception.InvalidDateException;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TaskDate implements Comparable<TaskDate> {

    private static final Logger log = LoggerFactory.getLogger(TaskDate.class);
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd";

    private Calendar time;
	
	public TaskDate() {
        this.time = populateTimeFrom(Calendar.getInstance());
	}

    public TaskDate(Calendar date) {
        this.time = populateTimeFrom(date);
    }

	public TaskDate(String date) throws InvalidDateException {
        this.time = populateTimeFrom(date, new SimpleDateFormat(DEFAULT_PATTERN));
	}
	
	public TaskDate(String date, SimpleDateFormat formater) throws InvalidDateException {
        this.time = populateTimeFrom(date, formater);
    }

    private Calendar populateTimeFrom(Calendar date) {
        SimpleDateFormat formater = new SimpleDateFormat(DEFAULT_PATTERN);
        String sDate = formater.format(date.getTime());
        try {
            return populateTimeFrom(sDate, formater);
        } catch (InvalidDateException cause) {
            log.error(cause.getMessage(), cause);
        }
        return null;
    }

    private Calendar populateTimeFrom(String date, SimpleDateFormat formater) throws InvalidDateException {
        Calendar tmp = Calendar.getInstance();
        try {
            tmp.setTime(formater.parse(date));
        } catch (ParseException e) {
            throw new InvalidDateException(date, formater.toPattern());
        }

        tmp.set(Calendar.HOUR, 0);
        tmp.set(Calendar.MINUTE, 0);
        tmp.set(Calendar.SECOND, 0);
        tmp.set(Calendar.MILLISECOND, 0);

        return tmp;
    }

    public Integer getMonth(){
        return time.get(Calendar.MONTH);
    }

	public Integer getWeekOfMonth(){
		return time.get(Calendar.WEEK_OF_MONTH) - 1;
	}
	
	public Integer getDayOfWeek(){
		return time.get(Calendar.DAY_OF_WEEK);
	}
	
	public Calendar toCalendar(){
		return (Calendar) time.clone();
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
        String month = twoDigits(time.get(Calendar.MONTH) + 1);
        String day = twoDigits(time.get(Calendar.DAY_OF_MONTH));
		return String.format("%s-%s-%s", time.get(Calendar.YEAR), month, day);
	}

    private String twoDigits(int value){
        if(value < 9){
            return String.format("0%s", value);
        } else {
            return String.valueOf(value);
        }
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
            String value = String.format("%s-%s-%s", src.time.get(Calendar.YEAR), src.time.get(Calendar.MONTH) + 1, src.time.get(Calendar.DAY_OF_MONTH));
            return new JsonPrimitive(value);
        }
    }

}
