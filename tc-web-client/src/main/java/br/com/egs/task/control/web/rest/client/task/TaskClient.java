package br.com.egs.task.control.web.rest.client.task;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import br.com.caelum.vraptor.ioc.Component;
import br.com.egs.task.control.web.model.Post;
import br.com.egs.task.control.web.model.OneWeekTask;
import br.com.egs.task.control.web.model.Week;
import br.com.egs.task.control.web.model.repository.TaskRepository;
import br.com.egs.task.control.web.model.stage.Stage;
import br.com.egs.task.control.web.rest.client.JsonClient;
import br.com.egs.task.control.web.rest.client.gson.DateTaskDeserializer;
import br.com.egs.task.control.web.rest.client.gson.StringListDeserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Component
public class TaskClient implements TaskRepository {

	private JsonClient jsonClient;
	private Calendar today;
	
	public TaskClient(JsonClient jsonClient) {
		this(jsonClient, Calendar.getInstance());
	}
	
	public TaskClient(JsonClient jsonClient, Calendar today) {
		this.jsonClient = jsonClient;
		this.today = (Calendar) today.clone();
	}	
	
	@Override
	public List<Week> weeksByMonth(Integer month) {
		String response = jsonClient.at("tasks").addUrlParam("year", "2014").addUrlParam("month", month.toString()).getAsJson();

		List<CoreTask> tasks = CoreTask.JsonList.parse(response);

		List<Week> weeks = loadWeeks();

		for (CoreTask coreTask : tasks) {
			for (int weekIndex = coreTask.startDate.getWeekOfYear(); weekIndex <= coreTask.foreseenEndDate.getWeekOfYear(); weekIndex++) {

				OneWeekTask.Builder task = new OneWeekTask.Builder(coreTask.id, coreTask.description);

				if (isFirstWeek(coreTask, weekIndex)) {
					task.starDay(coreTask.startDate.getDayOfWeek());
				}

				if (isLastWeek(coreTask, weekIndex)) {
					task.foreseenEnd(coreTask.foreseenEndDate.getDayOfWeek());
				}

				Stage st = getStage(coreTask.foreseenEndDate, coreTask.endDate);
				task.as(st);
				
				if (Stage.DOING.equals(st)) {
					task.runUntil(today.get(Calendar.DAY_OF_WEEK));
				}
				
				if (isFinishedAndInSameWeek(st, coreTask, weekIndex)) {
					task.runUntil(coreTask.endDate.getDayOfWeek());
				}

				weeks.get(weekIndex).add(task.build());
			}
		}
		return weeks;
	}

	private boolean isFirstWeek(CoreTask coreTask, int weekIndex) {
		return weekIndex == coreTask.startDate.getWeekOfYear();
	}

	private boolean isLastWeek(CoreTask coreTask, int weekIndex) {
		return weekIndex == coreTask.foreseenEndDate.getWeekOfYear();
	}

	private boolean isFinishedAndInSameWeek(Stage stage, CoreTask coreTask, int weekIndex) {
		return Stage.FINISHED.equals(stage) && coreTask.endDate.getWeekOfYear().equals(weekIndex);
	}

	private List<Week> loadWeeks() {
		List<Week> weeks = new LinkedList<>();
		for (int count = 0; count < 6; count++) {
			weeks.add(new Week());
		}
		return weeks;
	}

	private Stage getStage(TaskCalendar forseen, TaskCalendar end) {
		if (end == null) {
			return Stage.DOING;
		}
		if (forseen.getDayOfWeek().compareTo(end.getDayOfWeek()) >= 0) {
			return Stage.FINISHED;
		}
		if (forseen.getDayOfWeek().compareTo(end.getDayOfWeek()) < 0) {
			return Stage.LATE;
		}
		return Stage.WAITING;
	}

	@Override
	public List<Post> postBy(String id) {
		List<Post> posts = new LinkedList<>();

		Calendar c1 = Calendar.getInstance();
		c1.add(Calendar.DAY_OF_MONTH, -5);
		posts.add(new Post(c1, "Luigi", "Beef ribs chicken tail boudin pork chop filet mignon #hashtag kevin chuck."));

		Calendar c2 = Calendar.getInstance();
		c2.add(Calendar.DAY_OF_MONTH, -4);
		posts.add(new Post(c2, "Luigi", "Beef ribs chicken #horaextra tail boudin pork chop filet #hashtag mignon kevin chuck."));

		Calendar c3 = Calendar.getInstance();
		c3.add(Calendar.DAY_OF_MONTH, -3);
		posts.add(new Post(c3, "Luigi", "Beef ribs chicken #hashtag tail boudin pork chop filet mignon kevin chuck."));

		Calendar c4 = Calendar.getInstance();
		c4.add(Calendar.DAY_OF_MONTH, -2);
		posts.add(new Post(c4, "Luigi", "Beef ribs chicken tail boudin pork chop filet mignon kevin chuck. #atraso #hashtag"));

		return posts;
	}

	private static class CoreTask {

		private String id;
		private String description;
		private TaskCalendar startDate;
		private TaskCalendar foreseenEndDate;
		private TaskCalendar endDate;
		private String source;
		private String application;
		private List<String> owners;

		public static class JsonList {

			private static Gson parser() {
				GsonBuilder gson = new GsonBuilder();
				gson.registerTypeAdapter(TaskCalendar.class, new DateTaskDeserializer());
				gson.registerTypeAdapter(new TypeToken<List<String>>(){}.getType(), new StringListDeserializer());
				return gson.create();
			}

			public static List<CoreTask> parse(String json) {
				return parser().fromJson(json, new TypeToken<List<CoreTask>>() {
				}.getType());
			}

		}

	}

}
