package br.com.egs.task.control.web.model.calendar;

import java.util.Calendar;

public class MonthStructure {

    public static final int FIRST_DAY = 1;
    public static final String OPEN_ELEMENT = "\"";
    public static final String CLOSE_ELEMENT = "\",";

    private Month month;
    private int year;
    private int maxCurrent;
    private int minPrevious;
    private int maxPreviousMonth;
    private int maxNextMonth;

    public MonthStructure(Month m, int year) {
        this.month = m;
        this.year = year;

        Calendar reference = Calendar.getInstance();
        reference.set(Calendar.YEAR, year);
        reference.set(Calendar.MONTH, this.month.getId());
        reference.set(Calendar.DAY_OF_MONTH, FIRST_DAY);
        maxCurrent = reference.getActualMaximum(Calendar.DAY_OF_MONTH);
        int dayOfWeekFirstDay = reference.get(Calendar.DAY_OF_WEEK);

        Calendar prevous = Calendar.getInstance();
        prevous.set(Calendar.YEAR, year);
        prevous.set(Calendar.MONTH, month.getId());
        prevous.add(Calendar.MONTH, -1);
        prevous.set(Calendar.DAY_OF_MONTH, FIRST_DAY);
        maxPreviousMonth = prevous.getActualMaximum(Calendar.DAY_OF_MONTH);
        minPrevious = Math.abs(maxPreviousMonth - (dayOfWeekFirstDay - 2));

        int previousDays = maxPreviousMonth - minPrevious;
        maxNextMonth = 42 - (maxCurrent + previousDays);
    }

    public String getDaysAsJson() {
        StringBuilder json = new StringBuilder();
        json.append("{\"days\":[");

        json.append(previousMonthDays());

        json.append(referenceMonthDays());

        json.append(nextMonthDays());

        json.deleteCharAt(json.lastIndexOf(","));
        json.append("]}");

        return json.toString();
    }

    private String nextMonthDays() {
        StringBuilder days = new StringBuilder();
        days.append(OPEN_ELEMENT);
        days.append(month.getNext());
        days.append(" 1\",");
        for (int value = 2; value < maxNextMonth; value++) {
            days.append(OPEN_ELEMENT);
            days.append(value);
            days.append(CLOSE_ELEMENT);
        }
        return days.toString();
    }

    private String referenceMonthDays() {
        StringBuilder days = new StringBuilder();
        days.append(OPEN_ELEMENT);
        days.append(month);
        days.append(" 1\",");
        for (int value = 2; value <= maxCurrent; value++) {
            days.append(OPEN_ELEMENT);
            days.append(value);
            days.append(CLOSE_ELEMENT);
        }

        if (isItCurrentMonth() && isItCurrentYear()) {
            Integer day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            return days.toString().replace(String.format("\"%s\"", day.toString()), String.format("\"Hoje %s\"", day.toString()));
        } else {
            return days.toString();
        }
    }

    private boolean isItCurrentMonth() {
        Integer currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        return currentMonth.equals(month.getId());
    }

    private boolean isItCurrentYear() {
        Integer currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return currentYear.equals(year);
    }

    private String previousMonthDays() {
        StringBuilder days = new StringBuilder();
        for (int value = minPrevious; value <= maxPreviousMonth; value++) {
            days.append(OPEN_ELEMENT);
            days.append(value);
            days.append(CLOSE_ELEMENT);
        }
        return days.toString();
    }

}
