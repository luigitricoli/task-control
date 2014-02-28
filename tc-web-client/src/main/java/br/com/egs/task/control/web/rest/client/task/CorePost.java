package br.com.egs.task.control.web.rest.client.task;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

class CorePost {

	public Calendar timestamp;
	public String user;
	public String text;

	public CorePost(Calendar timestamp, String user, String text) {
		this.timestamp = timestamp;
		this.user = user;
		this.text = text;
	}

	public String toJson() {
		GsonBuilder gson = new GsonBuilder();
		gson.registerTypeAdapter(timestamp.getClass(), new JsonCalendarMarshaller());

		return gson.create().toJson(this);
	}

	private class JsonCalendarMarshaller implements JsonSerializer<Calendar> {

		@Override
		public JsonElement serialize(Calendar src, Type typeOfSrc, JsonSerializationContext context) {
			String value = String.format("%s-%s-%s %s:%s:%s", src.get(Calendar.YEAR), src.get(Calendar.MONTH) + 1, src.get(Calendar.DAY_OF_MONTH),
					src.get(Calendar.HOUR_OF_DAY), src.get(Calendar.MINUTE), src.get(Calendar.SECOND));
			return new JsonPrimitive(value);
		}
	}

	public static class JsonUnmarshaller implements JsonDeserializer<CorePost> {

		@Override
		public CorePost deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
			String date = jsonElement.getAsJsonObject().get("timestamp").getAsString();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar timestamp = Calendar.getInstance();
			try {
				timestamp.setTime(format.parse(date));
			} catch (ParseException e) {
				throw new JsonParseException(e);
			}

			String user = jsonElement.getAsJsonObject().get("user").getAsString();
			String text = jsonElement.getAsJsonObject().get("text").getAsString();

			return new CorePost(timestamp, user, text);
		}
	}

}
