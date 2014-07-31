/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.egs.task.control.web.model;

/**
 *
 * @author t3276780
 */
public class SimpleTaskData {
    private String description;
    private String startDate;
    private String foreseenEndDate;
    private String endDate;
    private Integer foreseenWorkHours;
    private String source;
    private String application;
    private String owners;

    public SimpleTaskData(String description, String startDate, String foreseenEndDate, String endDate, Integer foreseenWorkHours, String source, String application, String owners) {
        this.description = description;
        this.startDate = startDate;
        this.foreseenEndDate = foreseenEndDate;
        this.endDate = endDate;
        this.foreseenWorkHours = foreseenWorkHours;
        this.source = source;
        this.application = application;
        this.owners = owners;
    }

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
