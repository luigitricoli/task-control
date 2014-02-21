package br.com.egs.task.control.web.rest.client.gson;

import java.lang.reflect.Type;
import java.text.ParseException;

import br.com.egs.task.control.web.rest.client.task.TaskCalendar;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class DateTaskDeserializer implements JsonDeserializer<TaskCalendar> {

	@Override
	public TaskCalendar deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		try {
			return new TaskCalendar(jsonElement.getAsString());
		} catch (ParseException e) {
			throw new JsonParseException(e);
		}
	}
}
