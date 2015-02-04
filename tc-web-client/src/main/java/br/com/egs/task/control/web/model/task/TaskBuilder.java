package br.com.egs.task.control.web.model.task;

import br.com.egs.task.control.web.model.ForeseenType;
import br.com.egs.task.control.web.model.Post;
import br.com.egs.task.control.web.model.Task;
import br.com.egs.task.control.web.model.User;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
* Created by T3321939 on 03/02/15.
*/
public class TaskBuilder {

    private static String DESCRIPTION_SEPARATOR = " - ";
    public static final String EMPTY = "";
    private static final String FAIL_RESPONSE_CODE = "fail";

    private String id;
    private String description;
    private String prefix;
    private DateTime startDate;
    private DateTime foreseenEndDate;
    private DateTime endDate;
    private String source;
    private String application;
    private List<Post> posts;
    private List<User> owners;
    private ForeseenType foreseenType;
    private Integer qtd;
    private String type;

    public TaskBuilder(){
        posts = new ArrayList<>();
        owners = new ArrayList<>();
    }

    public TaskBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public TaskBuilder setPrefixDescription(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public TaskBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public TaskBuilder setStartDate(String date) {
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



    public TaskBuilder addOnStartDate(Integer value){
        this.startDate = this.startDate.plusDays(value);
        if(this.startDate.getDayOfWeek() == BasicTask.SATURDAY){
            this.startDate = this.startDate.plusDays(2);
        }
        return this;
    }

    public TaskBuilder addOnForeseenEndDate(Integer value){
        this.foreseenEndDate  = this.foreseenEndDate.plusDays(value);
        if(this.foreseenEndDate.getDayOfWeek() == BasicTask.SATURDAY){
            this.foreseenEndDate = this.foreseenEndDate.plusDays(2);
        }
        return this;
    }

    public TaskBuilder setStartDate(DateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public TaskBuilder setForeseenType(ForeseenType type) {
        this.foreseenType = type;
        return this;
    }

    public TaskBuilder setForeseenQtd(Integer qtd) {
        this.qtd = qtd;
        return this;
    }

    public TaskBuilder setForeseenEndDate(DateTime foreseenEndDate) {
        this.foreseenEndDate = foreseenEndDate;
        return this;
    }

    public TaskBuilder setEndDate(DateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public TaskBuilder setSource(String source) {
        this.source = source;
        return this;
    }

    public TaskBuilder setApplication(String application) {
        this.application = application;
        return this;
    }

    public TaskBuilder setTaskType(String type) {
        this.type = type;
        return this;        }

    public TaskBuilder addPost(Post post) {
        this.posts.add(post);
        return this;
    }

    public TaskBuilder addPosts(List<Post> posts) {
        this.posts.addAll(posts);
        return this;
    }

    public TaskBuilder addOwner(User owner) {
        this.owners.add(owner);
        return this;
    }

    public TaskBuilder addOwnersAsString(List<String> owners) {
        for (String name : owners){
            this.owners.add(new User(name));
        }
        return this;
    }

    public TaskBuilder addOwners(List<User> owners) {
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

        if(foreseenEndDate != null){
            return new BasicTask(id, description, startDate, foreseenEndDate, endDate, source, application, posts, owners, null);
        }

        if (foreseenType == null) {
            throw new InvalidTask("Identificação inválida");
        } else if (foreseenType == ForeseenType.hours && qtd > 8) {
            throw new InvalidTask("Um tarefa em horas não pode durar mais que 8 horas.");
        }

        if(foreseenType.equals(ForeseenType.days)){
            int weekends = Math.round(qtd/5) - 1;
            qtd += weekends*2;
            foreseenEndDate = startDate.plusDays(qtd - 1);
            if(foreseenEndDate.getDayOfWeek() == BasicTask.SUNDAY || foreseenEndDate.getDayOfWeek() == BasicTask.SATURDAY){
                foreseenEndDate = foreseenEndDate.plusDays(2);
            }

            return new BasicTask(id, description, startDate, foreseenEndDate, endDate, source, application, posts, owners, null);
        } else{
            if (qtd == null || qtd <= 0) {
                throw new InvalidTask("Data fim inválida");
            }

            foreseenEndDate = startDate.plusHours(qtd);
            return new BasicTask(id, description, startDate, foreseenEndDate, endDate, source, application, posts, owners, qtd);
        }
    }

    public boolean validTask() throws InvalidTask {
        Pattern pValidDate = Pattern.compile("([0-2][0-9]|3[0-1])\\/(0[1-9]|1[0-2])\\/[1-9][0-9]");
        if (startDate == null || !pValidDate.matcher(startDate.toString("dd/MM/yy")).matches()) {
            throw new InvalidTask("Data de início inválida");
        } else if (description == null || description.equals(EMPTY)) {
            throw new InvalidTask("Descrição inválida");
        } else if (application == null || owners.size() == 0) {
            throw new InvalidTask("Identificação inválida");
        }
        return true;
    }

}
