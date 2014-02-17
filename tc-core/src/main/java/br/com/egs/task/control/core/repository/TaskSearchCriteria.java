package br.com.egs.task.control.core.repository;

import java.util.Arrays;

/**
 *
 */
public class TaskSearchCriteria {
    private int year;
    private int month;
    private String application;
    private Status[] status;
    private String[] sources;
    private String ownerLogin;

    public TaskSearchCriteria month(int year, int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid month: " + month);
        }
        if (year < 1970) {
            throw new IllegalArgumentException("Invalid year: " + year);
        }

        this.year = year;
        this.month = month;
        return this;
    }

    public TaskSearchCriteria application(String app) {
        this.application = app;
        return this;
    }

    public TaskSearchCriteria sources(String... src) {
        this.sources = src;
        return this;
    }

    public TaskSearchCriteria ownerLogin(String login) {
        this.ownerLogin = login;
        return this;
    }

    public TaskSearchCriteria status(Status ... st) {
        this.status = st;
        return this;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public String getApplication() {
        return application;
    }

    public Status[] getStatus() {
        return status;
    }

    public String[] getSources() {
        return sources;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskSearchCriteria that = (TaskSearchCriteria) o;

        if (month != that.month) return false;
        if (year != that.year) return false;
        if (application != null ? !application.equals(that.application) : that.application != null) return false;
        if (ownerLogin != null ? !ownerLogin.equals(that.ownerLogin) : that.ownerLogin != null) return false;
        if (!Arrays.equals(sources, that.sources)) return false;
        if (!Arrays.equals(status, that.status)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + month;
        result = 31 * result + (application != null ? application.hashCode() : 0);
        result = 31 * result + (status != null ? Arrays.hashCode(status) : 0);
        result = 31 * result + (sources != null ? Arrays.hashCode(sources) : 0);
        result = 31 * result + (ownerLogin != null ? ownerLogin.hashCode() : 0);
        return result;
    }

    /**
     *
     *
     *
     */
    public static enum Status {
        DOING,
        FINISHED,
        WAITING,
        LATE
    }

}
