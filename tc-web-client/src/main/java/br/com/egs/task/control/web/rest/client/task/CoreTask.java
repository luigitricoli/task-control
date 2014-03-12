package br.com.egs.task.control.web.rest.client.task;

import br.com.egs.task.control.web.rest.client.gson.OwnersUnmarshaller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class CoreTask {

	private String id;
    private String description;
    private TaskDate startDate;
    private TaskDate foreseenEndDate;
    private TaskDate endDate;
    private String source;
    private String application;
    private List<String> owners;
    private List<CorePost> posts;

    private CoreTask(){}

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

    public List<String> getOwners() {
        return owners;
    }

    public List<CorePost> getPosts() {
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
