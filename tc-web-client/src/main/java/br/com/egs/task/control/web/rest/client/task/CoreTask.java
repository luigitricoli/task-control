package br.com.egs.task.control.web.rest.client.task;

import java.util.List;

import br.com.egs.task.control.web.rest.client.gson.StringListDeserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

class CoreTask {

	public String id;
	public String description;
	public TaskCalendar startDate;
	public TaskCalendar foreseenEndDate;
	public TaskCalendar endDate;
	public String source;
	public String application;
	public List<String> owners;

	public static class JsonList {

		private static Gson parser() {
			GsonBuilder gson = new GsonBuilder();
			gson.registerTypeAdapter(TaskCalendar.class, new TaskCalendar.JsonUnmarshaller());
			gson.registerTypeAdapter(new TypeToken<List<String>>() {
			}.getType(), new StringListDeserializer());
			return gson.create();
		}

		public static List<CoreTask> parse(String json) {
			return parser().fromJson(json, new TypeToken<List<CoreTask>>() {
			}.getType());
		}

	}

}
