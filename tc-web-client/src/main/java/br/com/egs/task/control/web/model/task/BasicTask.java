package br.com.egs.task.control.web.model.task;

import br.com.egs.task.control.web.model.Post;
import br.com.egs.task.control.web.model.Task;
import br.com.egs.task.control.web.model.User;
import br.com.egs.task.control.web.model.exception.InvalidDateException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class BasicTask implements Task {

    static final int SUNDAY = 7;
    static final int SATURDAY = 6;
    private String id;
    private String description;
    private DateTime startDate;
    private DateTime foreseenEndDate;
    private DateTime endDate;
    private String source;
    private String application;
    private List<Post> posts;
    private List<User> owners;
    private Integer workHours;
    private String status;

    public static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy";

    public BasicTask(String id, String description, DateTime startDate, DateTime foreseenEndDate, DateTime endDate, String source, String application, List<Post> posts, List<User> owners, Integer workHours) {
        this.id = id;
        this.description = description;
        this.startDate = startDate;
        this.foreseenEndDate = foreseenEndDate;
        this.endDate = endDate;
        this.source = source;
        this.application = application;
        this.posts = posts;
        this.owners = owners;
        this.workHours = workHours;

        this.status = generateStatus();
    }

    private String generateStatus() {
        DateTime today = new DateTime();
        if (isFinished()) {
            return "finished";
        } else if (isBeyondTheForeseen(today)) {
            return "late";
        } else if (isStarted(today)) {
            return "doing";
        }
        return "waiting";
    }

    private boolean isStarted(DateTime today) {
        return today.compareTo(this.startDate) >= 0;
    }

    private boolean isBeyondTheForeseen(DateTime today) {
        return today.compareTo(this.foreseenEndDate) > 0;
    }

    private boolean isFinished() {
        return this.endDate != null;
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Calendar getStartDate() {
        return startDate.toGregorianCalendar();
    }

    @Override
    public String getStartDateAsString() {
        return startDate.toString(DEFAULT_DATE_PATTERN);
    }

    @Override
    public Calendar getForeseenEndDate() {
        return foreseenEndDate.toGregorianCalendar();
    }

    @Override
    public String getForeseenEndDateAsString() {
        return foreseenEndDate.toString(DEFAULT_DATE_PATTERN);
    }

    @Override
    public Calendar getEndDate() {
        return endDate.toGregorianCalendar();
    }

    @Override
    public String getEndDateAsString() {
        return endDate.toString(DEFAULT_DATE_PATTERN);
    }

    @Override
    public Integer getWorkHours() {
        return workHours;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public String getApplication() {
        return application;
    }

    @Override
    public List<Post> getPosts() {
        return Collections.unmodifiableList(posts);
    }

    @Override
    public List<User> getOwners() {
        return Collections.unmodifiableList(owners);
    }

    @Override
    public Task replan(String start, String foreseen) throws InvalidDateException {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yy");
        if(start != null){
            startDate = fmt.parseDateTime(start);
        }
        if(foreseen != null){
            foreseenEndDate = fmt.parseDateTime(foreseen);
        }

        return this;
    }

    @Override
    public void setDefaultDateFormat(String defaultDateFormat) {

    }

    public static Gson marshaller(){
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(DateTime.class, new DateTimeMarshaller());
        return gson.create();
    }

    public String toJson(){
        return String.format("{\"task\":%s}", marshaller().toJson(this));
    }

}
