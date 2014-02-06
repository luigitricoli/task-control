package br.com.egs.task.control.core.repository;

/**
 *
 */
public class TaskSearchCriteria {
    private int year;
    private int month;

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

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }
}
