/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.egs.task.control.web.model;

import org.joda.time.DateTime;

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
    private String theId;
	private String theDescription;
	private String formattedApplication;

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
        this.theId = formatId();
        this.theDescription = formatDescription();
        this.status = generateStatus();
        this.formattedApplication = formatApplication();
    }

    private String formatApplication() {
    	if(application.equals("roaming")) {
    		return "CapSettle";
    	}
    	if(application.equals("bill_presentment")) {
    		return "Bill Presentment";
    	}
		return application.toUpperCase();
	}

	private String formatId() {
		if(description.contains("-")) {
			return description.split("-")[0];
		}
		else {
			return description;
		}
	}

	private String formatDescription() {
		if(description.contains("-")) {
			return description.split("-", 2)[1];
		}
		else {
			return description;
		}
	}
	
	private String generateStatus() {
        DateTime today = new DateTime().withMillisOfDay(0);
        if (isFinished()) {
            return "Finalizada";
        } else if (isBeyondTheForeseen(today)) {
            return "Atrasada";
        } else if (isStarted(today)) {
            return "Em andamento";
        }
        return "Não iniciada";
    }
	
	   private boolean isStarted(DateTime today) {
	        return today.compareTo(new DateTime(this.startDate)) >= 0;
	    }

	    private boolean isBeyondTheForeseen(DateTime today) {
	        return today.compareTo(new DateTime(this.foreseenEndDate)) > 0;
	    }

	    private boolean isFinished() {
	        return this.endDate != null;
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
    
    public String getTheId() {
    	return theId;
    }
    
    public String getTheDescription() {
    	return theDescription;
    }
    
    public String getStatus() {
    	return status;
    }
    
    public String getFormattedApplication() {
    	return formattedApplication;
    }
    
}