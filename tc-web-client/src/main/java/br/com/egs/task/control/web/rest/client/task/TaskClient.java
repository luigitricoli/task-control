package br.com.egs.task.control.web.rest.client.task;

import br.com.egs.task.control.web.model.*;
import br.com.egs.task.control.web.model.exception.InvalidDateException;
import br.com.egs.task.control.web.model.exception.TaskControlWebClientException;
import br.com.egs.task.control.web.model.exception.UpdateException;
import br.com.egs.task.control.web.model.repository.TaskRepository;
import br.com.egs.task.control.web.model.task.BasicTask;
import br.com.egs.task.control.web.model.task.InvalidTask;
import br.com.egs.task.control.web.model.task.TaskBuilder;
import br.com.egs.task.control.web.rest.client.JsonClient;
import br.com.egs.task.control.web.rest.client.Response;
import br.com.egs.task.control.web.rest.client.user.CoreUser;
import org.joda.time.DateTime;
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
    public List<Task> get(Integer month, Integer year) {
        return get(month, year, new ArrayList<String>(), null);
    }

    @Override
    public List<Task> get(Integer month, Integer year, List<String> filters, String users) {
        jsonClient.at("tasks").addUrlParam("year", year.toString()).addUrlParam("month", month.toString());

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
        List<Task> results = new ArrayList<>();
        for (CoreTask task : tasks) {
            TaskBuilder builder = new TaskBuilder();
            builder.setId(task.getId());
            builder.setDescription(task.getDescription());
            builder.setStartDate(task.getStartDate().toDateTime());
            builder.setForeseenEndDate(task.getForeseenEndDate().toDateTime());
            builder.setEndDate(task.getEndDate() != null ? task.getEndDate().toDateTime() : null);
            builder.setSource(task.getSource());
            builder.setApplication(task.getApplication());

            List<User> owners = new LinkedList<>();
            for (CoreUser user : task.getOwners()) {
                owners.add(new User(user.getName(), user.getLogin(), user.getEmail(), user.getApplications()));
            }

            builder.addOwners(owners);

            try {
                results.add(builder.build());
            } catch (InvalidTask invalidTask) {
                log.error("Invalid task found in core [{}]", task);
            }
        }



        return results;
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

        DateTime end = task.getEndDate() != null ? task.getEndDate().toDateTime() : null;
        return new BasicTask(task.getId(),task.getDescription(),task.getStartDate().toDateTime(),task.getForeseenEndDate().toDateTime(),end,task.getSource(),task.getApplication(),posts, owners, task.getForeseenWorkHours());
    }

    @Override
    public List<Post> postsBy(String taskId) {
        return get(taskId).getPosts();
    }

    @Override
    public boolean add(Task task) {
        Response response = jsonClient.at("tasks").postAsJson(new CoreTask(task).toJson());
        if (response.isSuccess()) {
            CoreTask coreTask = CoreTask.unmarshal(response.getContent());
            if(task.getWorkHours() != null){
                Post post = new Post(coreTask.getStartDate().toCalendar(), session.getUser().getNickname(), session.getUser().getName(), String.format("%s #horasutilizadas", task.getWorkHours()));
                add(post, coreTask.getId());
            }
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
    public List<SimpleTask> listTasks(Integer month, Integer year) {
        jsonClient.at("tasks").addUrlParam("year", year.toString()).addUrlParam("month", month.toString());
        List<CoreTask> tasks = CoreTask.unmarshalList(jsonClient.getAsJson().getContent());

        List<SimpleTask> result = convertCoreTasksToSimpleTaskData(tasks, false);
        return result;
    }

    @Override
    public void delete(String taskId) throws TaskControlWebClientException {
        Response response = jsonClient.at(String.format("tasks/%s", taskId)).delete();
        if (!response.isSuccess()) {
            throw new UpdateException(response.getContent());
        }
    }

    public List<SimpleTask> listActiveTasks(String date, String dateFormat) throws InvalidDateException {
        TaskDate td = new TaskDate(date, dateFormat);

        jsonClient.at("tasks")
                .addUrlParam("dayIntervalStart", td.toString())
                .addUrlParam("dayIntervalEnd", td.toString())
                .addUrlParam("excludeForeseenTasks", "true");
        List<CoreTask> tasks = CoreTask.unmarshalList(jsonClient.getAsJson().getContent());

        List<SimpleTask> result = convertCoreTasksToSimpleTaskData(tasks, true);
        return result;
    }

    /**
     *
     * @param tasks Tasks to be converted
     * @param replicateTasksWithMultipleOwners If true, a task is replicated for each owner. Otherwise a single
     *                                         output is generated, and the owner name is a composition of all
     *                                         the owners.
     */
    List<SimpleTask> convertCoreTasksToSimpleTaskData(List<CoreTask> tasks, boolean replicateTasksWithMultipleOwners) {
        List<SimpleTask> result = new ArrayList<>();
        for (CoreTask coreTask : tasks) {

            if (replicateTasksWithMultipleOwners) {
                for (CoreUser coreUser : coreTask.getOwners()) {
                    SimpleTask std = toSimpleTaskData(coreTask, coreUser.getName());
                    result.add(std);
                }
            } else {
                StringBuilder ownersDescription = new StringBuilder();
                for (CoreUser coreUser : coreTask.getOwners()) {
                    if (ownersDescription.length() > 0) {
                        ownersDescription.append(", ");
                    }
                    ownersDescription.append(coreUser.getName());
                }
                SimpleTask std = toSimpleTaskData(coreTask, ownersDescription.toString());
                result.add(std);
            }
        }
        return result;
    }

    private SimpleTask toSimpleTaskData(CoreTask coreTask, String ownerDescription) {
        String startDate = coreTask.getStartDate().toString();
        String foreseenEndDate = coreTask.getForeseenEndDate().toString();
        String endDate = coreTask.getEndDate() != null ? coreTask.getEndDate().toString() : null;

        return new SimpleTask(
                coreTask.getId(),
                coreTask.getDescription(),
                startDate,
                foreseenEndDate,
                endDate,
                coreTask.getForeseenWorkHours(),
                coreTask.getSource(),
                coreTask.getApplication(),
                ownerDescription);
    }
}
