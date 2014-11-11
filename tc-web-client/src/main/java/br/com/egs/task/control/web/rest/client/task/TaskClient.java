package br.com.egs.task.control.web.rest.client.task;

import br.com.egs.task.control.web.model.*;
import br.com.egs.task.control.web.model.exception.InvalidDateException;
import br.com.egs.task.control.web.model.exception.TaskControlWebClientException;
import br.com.egs.task.control.web.model.exception.UpdateException;
import br.com.egs.task.control.web.model.repository.TaskRepository;
import br.com.egs.task.control.web.model.task.BasicTask;
import br.com.egs.task.control.web.rest.client.JsonClient;
import br.com.egs.task.control.web.rest.client.Response;
import br.com.egs.task.control.web.rest.client.task.split.TaskSpliter;
import br.com.egs.task.control.web.rest.client.task.split.TaskSpliterFactory;
import br.com.egs.task.control.web.rest.client.user.CoreUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.*;
import java.util.Map.Entry;

@RequestScoped
public class TaskClient implements TaskRepository {

    private static final Logger log = LoggerFactory.getLogger(TaskClient.class);
    public static final String EMPTY = "";
    private static final String BRAZILIAN_FORMAT = "dd/MM/yy";

    private JsonClient jsonClient;
    private FilterFormat fomatter;
    private SessionUser session;

    /**
     * @deprecated needed for CDI
     */
    public TaskClient() {
    }

    @Inject
    public TaskClient(final FilterFormat fomatter, JsonClient jsonClient, SessionUser user) {
        this.fomatter = fomatter;
        this.jsonClient = jsonClient;
        this.session = user;
    }

    @Override
    public List<Week> weeksBy(Integer month) {
        return weeksBy(month, new ArrayList<String>(), null);
    }

    @Override
    public List<Week> weeksBy(Integer month, List<String> filters, String users) {
        jsonClient.at("tasks").addUrlParam("year", "2014").addUrlParam("month", month.toString());

        if (users != null && !users.equals(EMPTY)) {
            jsonClient.addUrlParam("owner", users);
        } else if (!session.isAdmin()) {
            jsonClient.addUrlParam("owner", session.getUser().getNickname());
        }

        if (filters != null) {
            Map<String, String> filterParam = fomatter.formatParams(filters);
            for (Entry<String, String> filter : filterParam.entrySet()) {
                jsonClient.addUrlParam(filter.getKey(), filter.getValue());
            }
        }

        List<CoreTask> tasks = CoreTask.unmarshalList(jsonClient.getAsJson().getContent());
        List<Week> weeks = loadWeeks();

        Calendar referenceMonth = Calendar.getInstance();
        referenceMonth.set(Calendar.YEAR, 2014);
        referenceMonth.set(Calendar.MONTH, month - 1);

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
    public Task get(String taskId) {
        Response response = jsonClient.at(String.format("tasks/%s", taskId)).getAsJson();
        CoreTask task = CoreTask.unmarshal(response.getContent());

        List<Post> posts = new LinkedList<>();
        for (CorePost post : task.getPosts()) {
            posts.add(new Post(post.getTimestamp(), post.getLogin(), post.getName(), post.getText()));
        }

        List<User> owners = new LinkedList<>();
        for (CoreUser user : task.getOwners()) {
            owners.add(new User(user.getName(), user.getLogin(), user.getEmail(), user.getApplications()));
        }

        return new BasicTask(task.getId(),task.getDescription(),task.getStartDate().toCalendar(),task.getForeseenEndDate().toCalendar(),task.getSource(),task.getApplication(),posts, owners);
    }

    @Override
    public List<Post> postsBy(String taskId) {
        return get(taskId).getPosts();
    }

    @Override
    public boolean add(String start, String foreseen, String type, String system, String description, List<String> users) {
        List<CoreUser> owners = new LinkedList<>();
        for (String login : users) {
            owners.add(new CoreUser(login));
        }

        CoreTask task = null;
        try {
            task = new CoreTask(new TaskDate(start, BRAZILIAN_FORMAT), new TaskDate(foreseen, BRAZILIAN_FORMAT), null, description, type, system, owners);
        } catch (InvalidDateException e) {
            log.error(e.getMessage());
            return false;
        }

        Response response = jsonClient.at("tasks").postAsJson(task.toJson());
        if (response.isSuccess()) {
            return true;
        }

        return false;
    }

    @Override
    public boolean add(Post p, String taskId) {
        CorePost post = new CorePost(p.getTime(), p.getLogin(), p.getText(), p.getText());

        Response response = jsonClient.at(String.format("tasks/%s", taskId)).postAsJson(post.toJson());
        if (response.isSuccess()) {
            return true;
        }
        return false;
    }

    @Override
    public void finish(String taskId, String date) throws InvalidDateException, UpdateException {
        CoreTask task = new CoreTask(taskId, new TaskDate(date));

        Response response = jsonClient.at(String.format("tasks/%s", taskId)).putAsJson(task.toJson());
        if (!response.isSuccess()) {
            throw new UpdateException(response.getContent());
        }
    }

    @Override
    public void update(Task task) throws InvalidDateException, UpdateException {
        String path = String.format("tasks/%s", task.getId());
        Response response = jsonClient.at(path).putAsJson(new CoreTask(task).toJson());
        if (!response.isSuccess()) {
            throw new UpdateException(response.getContent());
        }
    }

    @Override
    public void replan(String taskId, String dateFormat, String start, String foreseen) throws InvalidDateException, UpdateException {
        TaskDate startDate = start != null ? new TaskDate(start, dateFormat) : null;
        TaskDate foreseenDate = foreseen != null ? new TaskDate(foreseen, dateFormat) : null;

        CoreTask task = new CoreTask(taskId, startDate, foreseenDate);

        Response response = jsonClient.at(String.format("tasks/%s", taskId)).putAsJson(task.toJson());
        if (!response.isSuccess()) {
            throw new UpdateException(response.getContent());
        }
    }

    @Override
    public List<SimpleTaskData> listTasks(Integer month, Integer year) {
        jsonClient.at("tasks").addUrlParam("year", "2014").addUrlParam("month", month.toString());
        List<CoreTask> tasks = CoreTask.unmarshalList(jsonClient.getAsJson().getContent());

        List<SimpleTaskData> result = convertCoreTasksToSimpleTaskData(tasks);
        return result;
    }

    @Override
    public void delete(String taskId) throws TaskControlWebClientException {
        Response response = jsonClient.at(String.format("tasks/%s", taskId)).delete();
        if (!response.isSuccess()) {
            throw new UpdateException(response.getContent());
        }
    }

    List<SimpleTaskData> convertCoreTasksToSimpleTaskData(List<CoreTask> tasks) {
        List<SimpleTaskData> result = new ArrayList<>();
        for (CoreTask coreTask : tasks) {
            StringBuilder ownersDescription = new StringBuilder();
            for (CoreUser coreUser : coreTask.getOwners()) {
                if (ownersDescription.length() > 0) {
                    ownersDescription.append(", ");
                }
                ownersDescription.append(coreUser.getName());
            }
            
            String startDate = coreTask.getStartDate().toString();
            String foreseenEndDate = coreTask.getForeseenEndDate().toString();
            String endDate = coreTask.getEndDate() != null ? coreTask.getEndDate().toString() : null;
            
            result.add(new SimpleTaskData(
                    coreTask.getDescription(),
                    startDate,
                    foreseenEndDate,
                    endDate,
                    coreTask.getForeseenWorkHours(),
                    coreTask.getSource(),
                    coreTask.getApplication(), 
                    ownersDescription.toString()));
        }
        return result;
    }
}
