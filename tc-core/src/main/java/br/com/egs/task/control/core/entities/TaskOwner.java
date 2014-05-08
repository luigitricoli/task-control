package br.com.egs.task.control.core.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a task owner. This is a short version of a User.
 */
public class TaskOwner {
    private String login;
    private String name;
    private String type;
    private List<WorkDay> workDays = new ArrayList<>();

    public TaskOwner(String login, String name, String type) {
        this.login = login;
        this.name = name;
        this.type = type;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public TaskOwner addWorkHours(String day, int hours) {
        workDays.add(new WorkDay(day, hours));
        return this;
    }

    public List<WorkDay> getWorkDays() {
        return workDays;
    }

    /**
     *
     */
    public static class WorkDay {
        private String day;
        private int hours;

        private WorkDay(String day, int hours) {
            this.day = day;
            this.hours = hours;
        }

        public String getDay() {
            return day;
        }

        public int getHours() {
            return hours;
        }
    }

}
