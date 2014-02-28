package br.com.egs.task.control.web.rest.client.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.egs.task.control.web.model.OneWeekTask;
import br.com.egs.task.control.web.model.Post;
import br.com.egs.task.control.web.model.Week;
import br.com.egs.task.control.web.model.repository.TaskRepository;
import br.com.egs.task.control.web.model.stage.Stage;
import br.com.egs.task.control.web.rest.client.JsonClient;

@Component
@RequestScoped
public class TaskClient implements TaskRepository {

	private Logger log = LoggerFactory.getLogger(TaskCalendar.class);
	private JsonClient jsonClient;
	private TaskCalendar today;
	private FilterFormat fomatter;

	public TaskClient(FilterFormat fomatter, JsonClient jsonClient) {
		this(fomatter, jsonClient, Calendar.getInstance());
	}

	public TaskClient(FilterFormat fomatter, JsonClient jsonClient, Calendar today) {
		this.fomatter = fomatter;
		this.jsonClient = jsonClient;
		this.today = new TaskCalendar(today);
	}

	@Override
	public List<Week> weeksBy(Integer month) {
		return weeksBy(month, new ArrayList<String>());
	}
	
	@Override
	public List<Week> weeksBy(Integer month, List<String> filters) {
		jsonClient.at("tasks").addUrlParam("year", "2014").addUrlParam("month", month.toString());
		Map<String, String> filterParam = fomatter.formatParams(filters);
		for(Entry<String, String> filter : filterParam.entrySet()){
			jsonClient.addUrlParam(filter.getKey(), filter.getValue());
		}

		List<CoreTask> tasks = CoreTask.unmarshalList(jsonClient.getAsJson());
		List<Week> weeks = loadWeeks();

		for (CoreTask coreTask : tasks) {
			boolean keepInNextWeek = true;

			for (int weekIndex = coreTask.startDate.getWeekOfYear(); weekIndex <= coreTask.foreseenEndDate.getWeekOfYear(); weekIndex++) {

				OneWeekTask.Builder task = new OneWeekTask.Builder(coreTask.id, coreTask.description);

				// TODO improve it
				try {
					if (isFirstWeek(coreTask, weekIndex)) {
						task.starDay(coreTask.startDate.getDayOfWeek());
					}

					if (isLastWeek(coreTask, weekIndex)) {
						task.foreseenEndDay(coreTask.foreseenEndDate.getDayOfWeek());
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					continue;
				}

				if (isFinished(coreTask)) {
					task.as(Stage.FINISHED);

					if (isInSameWeek(coreTask.endDate, weekIndex)) {
						task.runUntil(coreTask.endDate.getDayOfWeek());
						keepInNextWeek = false;
					} else if (keepInNextWeek) {
						task.runAtTheEnd();
					}
				} else if (isBeyondTheForeseen(coreTask)) {
					task.as(Stage.LATE);

					if (isInSameWeek(today, weekIndex)) {
						task.runUntil(today.getDayOfWeek());
					} else {
						task.runAtTheEnd();
					}
				} else if (isStarted(coreTask)) {
					task.as(Stage.DOING);

					if (isInSameWeek(today, weekIndex)) {
						task.runUntil(today.getDayOfWeek());
					} else {
						task.runAtTheEnd();
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
	public List<Post> postsBy(String taskId) {
		String response = jsonClient.at(String.format("tasks/%s", taskId)).getAsJson();
		CoreTask task = CoreTask.unmarshal(response);

		List<Post> posts = new LinkedList<>();
		for (CorePost post : task.posts) {
			posts.add(new Post(post.timestamp, post.user, post.text));
		}

		return posts;
	}
	
	@Override
	public boolean add(Post p, String taskId){
		CorePost post = new CorePost(p.getTime(), p.getUser(), p.getText());
		
		String response = jsonClient.at(String.format("tasks/%s", taskId)).postAsJson(post.toJson());
		if(response != ""){
			return false;
		}
		
		return true;
		
	}

}
