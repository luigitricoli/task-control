package br.com.egs.task.control.web.rest.client.task;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.egs.task.control.web.model.Post;
import br.com.egs.task.control.web.model.User;
import br.com.egs.task.control.web.model.Week;
import br.com.egs.task.control.web.model.repository.TaskRepository;
import br.com.egs.task.control.web.rest.client.JsonClient;
import br.com.egs.task.control.web.rest.client.Response;
import br.com.egs.task.control.web.rest.client.task.split.CoreUser;
import br.com.egs.task.control.web.rest.client.task.split.TaskSpliter;
import br.com.egs.task.control.web.rest.client.task.split.TaskSpliterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.SimpleFormatter;

@Component
@RequestScoped
public class TaskClient implements TaskRepository {

    private static final Logger log = LoggerFactory.getLogger(TaskClient.class);

	private JsonClient jsonClient;
	private FilterFormat fomatter;
    private User user;

	public TaskClient(final FilterFormat fomatter, JsonClient jsonClient, User user) {
        this.fomatter = fomatter;
        this.jsonClient = jsonClient;
        this.user = user;
    }

	@Override
	public List<Week> weeksBy(Integer month) {
		return weeksBy(month, new ArrayList<String>());
	}
	
	@Override
	public List<Week> weeksBy(Integer month, List<String> filters) {
		jsonClient.at("tasks").addUrlParam("year", "2014").addUrlParam("month", month.toString()).addUrlParam("owner", user.getNickname());
		Map<String, String> filterParam = fomatter.formatParams(filters);
		for(Entry<String, String> filter : filterParam.entrySet()){
			jsonClient.addUrlParam(filter.getKey(), filter.getValue());
		}

		List<CoreTask> tasks = CoreTask.unmarshalList(jsonClient.getAsJson().getContent());
		List<Week> weeks = loadWeeks();

        Calendar referenceMonth = Calendar.getInstance();
        referenceMonth.set(Calendar.YEAR, 2014);
        referenceMonth.set(Calendar.MONTH, month-1);

		for (CoreTask coreTask : tasks) {
            TaskSpliter spliter = TaskSpliterFactory.getInstance(coreTask, referenceMonth);
            spliter.split(coreTask);
            weeks.get(0).add(spliter.firstWeek());
            weeks.get(1).add(spliter.secondWeek());
            weeks.get(2).add(spliter.thirdWeek());
            weeks.get(3).add(spliter.fourthWeek());
            weeks.get(4).add(spliter.fifthWeek());
            weeks.get(5).add(spliter.sixthWeek());
		}
		return weeks;
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
		Response response = jsonClient.at(String.format("tasks/%s", taskId)).getAsJson();
		CoreTask task = CoreTask.unmarshal(response.getContent());

		List<Post> posts = new LinkedList<>();
		for (CorePost post : task.getPosts()) {
			posts.add(new Post(post.getTimestamp(), post.getUser(), post.getText()));
		}

		return posts;
	}

    @Override
    public boolean add(String start, String foreseen, String type, String system, String description, List<String> users) {
        List<CoreUser> owners = new LinkedList<>();
        for (String login : users) {
            owners.add(new CoreUser(login, null));
        }

        CoreTask task = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
            task = new CoreTask(new TaskDate(start, formatter), new TaskDate(foreseen, formatter), description, type, system, owners);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }

        Response response = jsonClient.at("tasks").postAsJson(task.toJson());
        if(response.getCode().equals(201)){
            return true;
        }
        return false;
    }

    @Override
	public boolean add(Post p, String taskId){
		CorePost post = new CorePost(p.getTime(), p.getUser(), p.getText());

        Response response = jsonClient.at(String.format("tasks/%s", taskId)).postAsJson(post.toJson());
        if(response.getCode().equals(201)){
            return true;
        }
        return false;
	}

    @Override
    public boolean finish(String taskId, TaskDate date){
        CoreTask task = new CoreTask(taskId, date);

        Response response = jsonClient.at(String.format("tasks/%s", taskId)).putAsJson(task.toJson());
        if(response.getCode().equals(201)){
            return true;
        }
        return false;
    }
}
