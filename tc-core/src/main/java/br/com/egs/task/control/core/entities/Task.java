package br.com.egs.task.control.core.entities;

import java.util.Date;
import java.util.List;

/**
 *
 */
public class Task {

	private String id;
    private String description;

    private Date startDate;
    private Date foreseenEndDate;
    private Date endDate;

    private String source;
    private Application application;

    private List<TaskOwner> owners;

    private List<Post> posts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getForeseenEndDate() {
        return foreseenEndDate;
    }

    public void setForeseenEndDate(Date foreseenEndDate) {
        this.foreseenEndDate = foreseenEndDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public List<TaskOwner> getOwners() {
        return owners;
    }

    public void setOwners(List<TaskOwner> owners) {
        this.owners = owners;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
