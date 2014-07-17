/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.egs.task.control.core.builder;

import br.com.egs.task.control.core.entities.Application;
import br.com.egs.task.control.core.entities.Post;
import br.com.egs.task.control.core.entities.Task;
import br.com.egs.task.control.core.entities.TaskOwner;
import br.com.egs.task.control.core.exception.ValidationException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author t3276780
 */
public class TestTaskBuilder {
    
    private String id_;
    private String description_;
    private Date startDate_;
    private Date foreseenEndDate_;
    private Date endDate_;
    private Integer foreseenWorkHours_;
    private String source_;
    private Application application_;
    private List<TaskOwner> owners_;
    private List<Post> posts_;
    
    DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public TestTaskBuilder id(String id) {
        id_ = id;
        return this;
    }

    public TestTaskBuilder description(String description) {
        description_ = description;
        return this;
    }

    public TestTaskBuilder startDate(Date startDate) {
        startDate_ = startDate;
        return this;
    }

    /**
     * Inputs the date using format yyyy-MM-dd HH:mm:ss.SSS
     * @param startDate
     * @return 
     */
    public TestTaskBuilder startDate(String startDate) {
        try {
            startDate_ = timestampFormat.parse(startDate);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
        return this;
    }

    public TestTaskBuilder foreseenEndDate(Date foreseenEndDate) {
        foreseenEndDate_ = foreseenEndDate;
        return this;
    }

    /**
     * Inputs the date using format yyyy-MM-dd HH:mm:ss.SSS
     * @param foreseenEndDate
     * @return 
     */
    public TestTaskBuilder foreseenEndDate(String foreseenEndDate) {
        try {
            foreseenEndDate_ = timestampFormat.parse(foreseenEndDate);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
        return this;
    }

    public TestTaskBuilder endDate(Date endDate) {
        endDate_ = endDate;
        return this;
    }
    
    public TestTaskBuilder nullEndDate() {
        endDate_ = null;
        return this;
    }
    
    /**
     * Inputs the date using format yyyy-MM-dd HH:mm:ss.SSS
     * @param endDate
     * @return 
     */
    public TestTaskBuilder endDate(String endDate) {
        try {
            endDate_ = timestampFormat.parse(endDate);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
        return this;
    }

    public TestTaskBuilder foreseenWorkHours(Integer foreseenWorkHours) {
        foreseenWorkHours_ = foreseenWorkHours;
        return this;
    }

    public TestTaskBuilder source(String source) {
        source_ = source;
        return this;
    }

    public TestTaskBuilder application(Application application) {
        application_ = application;
        return this;
    }

    public TestTaskBuilder application(String applicationName) {
        application_ = new Application(applicationName);
        return this;
    }

    public TestTaskBuilder owners(TaskOwner... owner) {
        owners_ = Arrays.asList(owner);
        return this;
    }

    public TestTaskBuilder addPost(Post p) {
        if (posts_ == null) {
            posts_ = new ArrayList<>();
        }
        posts_.add(p);
        return this;
    }
    
    public Task build() {
        final Task task = new Task(id_,
                description_,
                startDate_,
                foreseenEndDate_,
                endDate_,
                foreseenWorkHours_,
                source_,
                application_,
                owners_);
        if (posts_ != null) {
            for (Post post : posts_) {
                try {
                    task.addPost(post);
                } catch (ValidationException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return task;
    }
   
}
