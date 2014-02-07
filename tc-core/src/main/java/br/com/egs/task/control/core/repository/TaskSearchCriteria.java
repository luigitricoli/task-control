package br.com.egs.task.control.core.repository;

/**
 *
 */
public class TaskSearchCriteria {
    private int year;
    private int month;
    private String application;
    private Status[] status;
    private String source;
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

    public TaskSearchCriteria source(String src) {
        this.source = src;
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

    public String getSource() {
        return source;
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
        if (source != null ? !source.equals(that.source) : that.source != null) return false;
        if (status != that.status) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + month;
        result = 31 * result + (application != null ? application.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
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
        DOING_LATE,
        FINISHED_IN_ADVANCE,
        FINISHED_ON_TIME,
        FINISHED_LATE
    }

}
