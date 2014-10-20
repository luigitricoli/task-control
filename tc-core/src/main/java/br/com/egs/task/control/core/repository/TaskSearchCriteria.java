package br.com.egs.task.control.core.repository;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 *
 */
public class TaskSearchCriteria {
    private int year;
    private int month;
    private String[] applications;
    private Status[] status;
    private String[] sources;
    private String[] ownerLogins;
    private boolean excludePosts;
    private Calendar dayIntervalBegin;
    private Calendar dayIntervalEnd;

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

    public TaskSearchCriteria applications(String ... apps) {
        this.applications = apps;
        return this;
    }

    public TaskSearchCriteria sources(String... src) {
        this.sources = src;
        return this;
    }

    public TaskSearchCriteria ownerLogins(String... logins) {
        this.ownerLogins = logins;
        return this;
    }

    public TaskSearchCriteria status(Status ... st) {
        this.status = st;
        return this;
    }

    public TaskSearchCriteria excludePosts() {
        this.excludePosts = true;
        return this;
    }

    public TaskSearchCriteria dayInterval(Calendar begin, Calendar end) {
        this.dayIntervalBegin = begin;
        this.dayIntervalEnd = end;
        return this;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public String[] getApplications() {
        return applications;
    }

    public Status[] getStatus() {
        return status;
    }

    public String[] getSources() {
        return sources;
    }

    public String[] getOwnerLogins() {
        return ownerLogins;
    }

    public boolean isExcludePosts() {
        return excludePosts;
    }

    public Calendar getDayIntervalEnd() {
        return dayIntervalEnd;
    }

    public Calendar getDayIntervalBegin() {
        return dayIntervalBegin;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskSearchCriteria that = (TaskSearchCriteria) o;

        if (excludePosts != that.excludePosts) return false;
        if (month != that.month) return false;
        if (year != that.year) return false;
        if (!Arrays.equals(applications, that.applications)) return false;
        if (dayIntervalBegin != null ? !dayIntervalBegin.equals(that.dayIntervalBegin) : that.dayIntervalBegin != null)
            return false;
        if (dayIntervalEnd != null ? !dayIntervalEnd.equals(that.dayIntervalEnd) : that.dayIntervalEnd != null)
            return false;
        if (!Arrays.equals(ownerLogins, that.ownerLogins)) return false;
        if (!Arrays.equals(sources, that.sources)) return false;
        if (!Arrays.equals(status, that.status)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + month;
        result = 31 * result + (applications != null ? Arrays.hashCode(applications) : 0);
        result = 31 * result + (status != null ? Arrays.hashCode(status) : 0);
        result = 31 * result + (sources != null ? Arrays.hashCode(sources) : 0);
        result = 31 * result + (ownerLogins != null ? Arrays.hashCode(ownerLogins) : 0);
        result = 31 * result + (excludePosts ? 1 : 0);
        result = 31 * result + (dayIntervalBegin != null ? dayIntervalBegin.hashCode() : 0);
        result = 31 * result + (dayIntervalEnd != null ? dayIntervalEnd.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TaskSearchCriteria{" +
                "year=" + year +
                ", month=" + month +
                ", applications=" + (applications == null ? null : Arrays.asList(applications)) +
                ", status=" + (status == null ? null : Arrays.asList(status)) +
                ", sources=" + (sources == null ? null : Arrays.asList(sources)) +
                ", ownerLogins=" + (ownerLogins == null ? null : Arrays.asList(ownerLogins)) +
                ", excludePosts=" + excludePosts +
                ", dayIntervalBegin=" + dayIntervalBegin +
                ", dayIntervalEnd=" + dayIntervalEnd +
                '}';
    }
}
