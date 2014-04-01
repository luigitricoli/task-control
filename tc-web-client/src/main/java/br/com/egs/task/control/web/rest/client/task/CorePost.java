package br.com.egs.task.control.web.rest.client.task;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CorePost {

	private Calendar timestamp;
    private String user;
    private String text;

    private Boolean added;

	public CorePost(Calendar timestamp, String user, String text) {
		this.timestamp = timestamp;
		this.user = user;
		this.text = text;

        this.added = false;
	}

	public String toJson() {
		GsonBuilder gson = new GsonBuilder();
		gson.registerTypeAdapter(timestamp.getClass(), new JsonCalendarMarshaller());
		Gson marshal = gson.create();
		
		return String.format("{\"post\":%s}", marshal.toJson(this));
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

    public Calendar getTimestamp() {
        return timestamp;
    }

    public String getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public boolean hasOvetimeHashtag(){
        return text.contains("#horaextra");
    }

    public boolean hasLateHashtag(){
        return text.contains("#atraso");
    }

    public boolean isBefore(Calendar date){
        return this.timestamp.compareTo(date) < 0;
    }

    public Integer getDayOfWeek(){
        return timestamp.get(Calendar.DAY_OF_WEEK);
    }

    public boolean wasAdded(){
        return this.added;
    }

    public void added(){
        this.added = true;
    }

}
