package br.com.egs.task.control.web.model.task;

import br.com.egs.task.control.web.model.ForeseenType;
import br.com.egs.task.control.web.model.Post;
import br.com.egs.task.control.web.model.Task;
import br.com.egs.task.control.web.model.User;
import br.com.egs.task.control.web.model.exception.InvalidDateException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class BasicTask implements Task {

    private static final int SUNDAY = 7;
    private static final int SATURDAY = 6;
    private String id;
    private String description;
    private DateTime startDate;
    private DateTime foreseenEndDate;
    private String source;
    private String application;
    private List<Post> posts;
    private List<User> owners;
    private Integer workHours;

    public static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy";

    public BasicTask(String id, String description, DateTime startDate, DateTime foreseenEndDate, String source, String application, List<Post> posts, List<User> owners, Integer workHours) {
        this.id = id;
        this.description = description;
        this.startDate = startDate;
        this.foreseenEndDate = foreseenEndDate;
        this.source = source;
        this.application = application;
        this.posts = posts;
        this.owners = owners;
        this.workHours = workHours;
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

    public static class Builder{

        private static String DESCRIPTION_SEPARATOR = " - ";
        public static final String EMPTY = "";
        private static final String FAIL_RESPONSE_CODE = "fail";

        private String id;
        private String description;
        private String prefix;
        private DateTime startDate;
        private DateTime foreseenEndDate;
        private String source;
        private String application;
        private List<Post> posts;
        private List<User> owners;
        private ForeseenType foreseenType;
        private Integer qtd;
        private String type;

        public Builder(){
            posts = new ArrayList<>();
            owners = new ArrayList<>();
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setPrefixDescription(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setStartDate(String date) {
            DateTimeFormatter defaultFmt = DateTimeFormat.forPattern("dd/MM/yyyy");
            if(date.length() == 8){
                date = date.replaceAll("([0-9]+/[0-9]+)/([0-9]+)", "$1/20$2");
                this.setStartDate(defaultFmt.parseDateTime(date));
            }
            if(date.length() == 10){
                this.setStartDate(defaultFmt.parseDateTime(date));
            }

            return this;
        }



        public Builder addOnStartDate(Integer value){
            this.startDate = this.startDate.plusDays(value);
            if(this.startDate.getDayOfWeek() == SATURDAY){
                this.startDate = this.startDate.plusDays(2);
            }
            return this;
        }

        public Builder addOnForeseenEndDate(Integer value){
            this.foreseenEndDate  = this.foreseenEndDate.plusDays(value);
            if(this.foreseenEndDate.getDayOfWeek() == SATURDAY){
                this.foreseenEndDate = this.foreseenEndDate.plusDays(2);
            }
            return this;
        }

        public Builder setStartDate(DateTime startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder setForeseenType(ForeseenType type) {
            this.foreseenType = type;
            return this;
        }

        public Builder setForeseenQtd(Integer qtd) {
            this.qtd = qtd;
            return this;
        }

        public Builder setForeseenEndDate(DateTime foreseenEndDate) {
            this.foreseenEndDate = foreseenEndDate;
            return this;
        }

        public Builder setSource(String source) {
            this.source = source;
            return this;
        }

        public Builder setApplication(String application) {
            this.application = application;
            return this;
        }

        public Builder setTaskType(String type) {
            this.type = type;
            return this;        }

        public Builder addPost(Post post) {
            this.posts.add(post);
            return this;
        }

        public Builder addPosts(List<Post> posts) {
            this.posts.addAll(posts);
            return this;
        }

        public Builder addOwner(User owner) {
            this.owners.add(owner);
            return this;
        }

        public Builder addOwnersAsString(List<String> owners) {
            for (String name : owners){
                this.owners.add(new User(name));
            }
            return this;
        }

        public Builder addOwners(List<User> owners) {
            this.owners.addAll(owners);
            return this;
        }

        public Task build() throws InvalidTask {
            validTask();

            if(prefix != null){
                description = prefix.concat(" ").concat(description);
            }
            if(type != null){
                description = type.concat(DESCRIPTION_SEPARATOR).concat(description);
                type = null;
            }

            if(foreseenEndDate == null && foreseenType.equals(ForeseenType.days)){
                int weekends = qtd/5;
                qtd += weekends*2;
                foreseenEndDate = startDate.plusDays(qtd - 1);
                if(foreseenEndDate.getDayOfWeek() == SUNDAY || foreseenEndDate.getDayOfWeek() == SATURDAY){
                    foreseenEndDate = foreseenEndDate.plusDays(2);
                }

                return new BasicTask(id, description, startDate, foreseenEndDate, source, application, posts, owners, null);
            } else {
                foreseenEndDate = startDate.plusHours(qtd);
                return new BasicTask(id, description, startDate, foreseenEndDate, source, application, posts, owners, qtd);
            }
        }

        public boolean validTask() throws InvalidTask {
            Pattern pValidDate = Pattern.compile("([0-2][0-9]|3[0-1])\\/(0[1-9]|1[0-2])\\/[1-9][0-9]");
            if (startDate == null || !pValidDate.matcher(startDate.toString("dd/MM/yy")).matches()) {
                throw new InvalidTask("Data de início inválida");
            } else if (qtd == null || qtd <= 0) {
                throw new InvalidTask("Data fim inválida");
            } else if (description == null || description.equals(EMPTY)) {
                throw new InvalidTask("Descrição inválida");
            } else if (foreseenType == null || application == null || owners.size() == 0) {
                throw new InvalidTask("Identificação inválida");
            } else if (foreseenType == ForeseenType.hours && qtd > 8) {
                throw new InvalidTask("Um tarefa em horas não pode durar mais que 8 horas.");
            }
            return true;
        }

    }
}
