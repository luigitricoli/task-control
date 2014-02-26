package br.com.egs.task.control.web.rest.client.task;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import br.com.caelum.vraptor.ioc.Component;
import br.com.egs.task.control.web.model.OneWeekTask;
import br.com.egs.task.control.web.model.Post;
import br.com.egs.task.control.web.model.Week;
import br.com.egs.task.control.web.model.repository.TaskRepository;
import br.com.egs.task.control.web.model.stage.Stage;
import br.com.egs.task.control.web.rest.client.JsonClient;

@Component
public class TaskClient implements TaskRepository {

	private JsonClient jsonClient;
	private TaskCalendar today;

	public TaskClient(JsonClient jsonClient) {
		this(jsonClient, Calendar.getInstance());
	}

	public TaskClient(JsonClient jsonClient, Calendar today) {
		this.jsonClient = jsonClient;
		this.today = new TaskCalendar(today);
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

				if (isFinished(coreTask)) {
					task.as(Stage.FINISHED);

					if (isInSameWeek(coreTask.endDate, weekIndex)) {
						task.runUntil(coreTask.endDate.getDayOfWeek());
					}
				} else {
					if (isBeyondTheForeseen(coreTask)) {
						task.as(Stage.LATE);
					} else if (isStarted(coreTask)) {
						task.as(Stage.DOING);
					}

					if (isInSameWeek(today, weekIndex)) {
						task.runUntil(today.getDayOfWeek());
					}
	
				}
				
				weeks.get(weekIndex).add(task.build());
			}
		}
		return weeks;
	}

	private boolean isStarted(CoreTask coreTask) {
		return today.compareTo(coreTask.startDate) >= 0;
	}

	private boolean isBeyondTheForeseen(CoreTask coreTask) {
		return today.compareTo(coreTask.foreseenEndDate) > 0;
	}

	private boolean isFinished(CoreTask coreTask) {
		return coreTask.endDate != null;
	}

	private boolean isFirstWeek(CoreTask coreTask, int weekIndex) {
		return weekIndex == coreTask.startDate.getWeekOfYear();
	}

	private boolean isLastWeek(CoreTask coreTask, int weekIndex) {
		return weekIndex == coreTask.foreseenEndDate.getWeekOfYear();
	}

	private boolean isInSameWeek(TaskCalendar today, int weekIndex) {
		return today.getWeekOfYear().equals(weekIndex);
	}

	private List<Week> loadWeeks() {
		List<Week> weeks = new LinkedList<>();
		for (int count = 0; count < 6; count++) {
			weeks.add(new Week());
		}
		return weeks;
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

}
