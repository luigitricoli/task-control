package br.com.egs.task.control.web.rest.client.task;

import br.com.egs.task.control.web.rest.client.gson.OwnersUnmarshaller;
import br.com.egs.task.control.web.rest.client.user.CoreUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CoreTask {

	private String id;
    private String description;
    private TaskDate startDate;
    private TaskDate foreseenEndDate;
    private TaskDate endDate;
    private String source;
    private String application;
    private List<CoreUser> owners;
    private List<CorePost> posts;

    private CoreTask(){}

    public CoreTask(TaskDate startDate, TaskDate foreseenEndDate, String description, String source, String application, List<CoreUser> owners) {
        this.startDate = startDate;
        this.foreseenEndDate = foreseenEndDate;
        this.description = description;
        this.source = source;
        this.application = application;

        this.owners = new LinkedList<>(owners);
    }

    public CoreTask(String id, TaskDate endDate){
        this.id = id;
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public TaskDate getStartDate() {
        return startDate;
    }

    public TaskDate getForeseenEndDate() {
        return foreseenEndDate;
    }

    public TaskDate getEndDate() {
        return endDate;
    }

    public String getSource() {
        return source;
    }

    public String getApplication() {
        return application;
    }

    public List<CoreUser> getOwners() {
        return owners;
    }

    public List<CorePost> getPosts() {
        if(posts == null){
            return new ArrayList<>();
        }
        return posts;

    }

    private static Gson unmarshaller() {
		GsonBuilder gson = new GsonBuilder();
		gson.registerTypeAdapter(TaskDate.class, new TaskDate.JsonUnmarshaller());
		gson.registerTypeAdapter(new TypeToken<List<String>>() {
		}.getType(), new OwnersUnmarshaller());
		gson.registerTypeAdapter(CorePost.class, new CorePost.JsonUnmarshaller());
		return gson.create();
	}

	public static CoreTask unmarshal(String json) {
		return unmarshaller().fromJson(json, CoreTask.class);
	}

	public static List<CoreTask> unmarshalList(String json) {
		return unmarshaller().fromJson(json, new TypeToken<List<CoreTask>>() {
        }.getType());
	}

    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(TaskDate.class, new TaskDate.JsonMarshaller());
        Gson marhaller = gson.create();
        return String.format("{\"task\":%s}", marhaller.toJson(this));
    }

}
