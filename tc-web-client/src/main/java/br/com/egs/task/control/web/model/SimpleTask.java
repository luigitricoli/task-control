/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.egs.task.control.web.model;

import br.com.egs.task.control.web.rest.client.task.CoreTask;
import br.com.egs.task.control.web.rest.client.task.TaskDate;
import br.com.egs.task.control.web.rest.client.task.split.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.List;

public class SimpleTask {
    public static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy";

    private String id;
    private String description;
    private String startDate;
    private String foreseenEndDate;
    private String endDate;
    private Integer foreseenWorkHours;
    private String source;
    private String application;
    private String owners;
    private String status;

    public SimpleTask(String id, String description, String startDate, String foreseenEndDate, String endDate, Integer foreseenWorkHours, String source, String application, String owners) {
        this.id = id;
        this.description = description;
        this.startDate = startDate;
        this.foreseenEndDate = foreseenEndDate;
        this.endDate = endDate;
        this.foreseenWorkHours = foreseenWorkHours;
        this.source = source;
        this.application = application;
        this.owners = owners;
    }

    public String getId() { return id; }

    public String getDescription() {
        return description;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getForeseenEndDate() {
        return foreseenEndDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Integer getForeseenWorkHours() {
        return foreseenWorkHours;
    }

    public String getSource() {
        return source;
    }

    public String getApplication() {
        return application;
    }

    public String getOwners() {
        return owners;
    }
    
    
}