package br.com.egs.task.control.web.model.calendar;

import java.util.Calendar;

public class ReferenceMonth {

    public static final int FIRST_DAY = 1;
    public static final String OPEN_ELEMENT = "\"";
    public static final String CLOSE_ELEMENT = "\",";

    private Label month;
    private int year;
    private int maxCurrent;
    private int dayLimitOfPreviousMonth;
    private int lastDayOfPreviousMonth;
    private int dayLimitNextMonth;

    public ReferenceMonth(int month, int year) {
        this.month = Label.getByNumber(month);
        this.year = year;

        Calendar reference = Calendar.getInstance();
        reference.set(Calendar.YEAR, year);
        reference.set(Calendar.MONTH, this.month.getId());
        reference.set(Calendar.DAY_OF_MONTH, FIRST_DAY);
        maxCurrent = reference.getActualMaximum(Calendar.DAY_OF_MONTH);
        int dayOfWeekFirstDay = reference.get(Calendar.DAY_OF_WEEK);

        Calendar previous = (Calendar) reference.clone();
        previous.add(Calendar.MONTH, -1);
        lastDayOfPreviousMonth = previous.getActualMaximum(Calendar.DAY_OF_MONTH);
        dayLimitOfPreviousMonth = Math.abs(lastDayOfPreviousMonth - (dayOfWeekFirstDay - 2));

        int previousDays = lastDayOfPreviousMonth - dayLimitOfPreviousMonth;
        dayLimitNextMonth = 41 - (maxCurrent + previousDays);
    }

    public String getDaysAsJson() {
        StringBuilder json = new StringBuilder();
        json.append("{\"label\":\"");

        if(month.getId() == Calendar.getInstance().get(Calendar.MONTH)){
            json.append("Atual");
        } else {
            json.append(month.getFullName());
            json.append("/");
            json.append(year);
        }


        json.append("\", \"days\":[");
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
        for (int value = 2; value <= dayLimitNextMonth; value++) {
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
            return days.toString().replaceFirst(String.format("\"(\\w{3} )?%s\"", day.toString()), String.format("\"Hoje %s\"", day.toString()));
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
        for (int value = dayLimitOfPreviousMonth; value <= lastDayOfPreviousMonth; value++) {
            days.append(OPEN_ELEMENT);
            days.append(value);
            days.append(CLOSE_ELEMENT);
        }
        return days.toString();
    }

    public enum Label {
        Jan(0, "Janeiro"),
        Fev(1, "Fevereiro"),
        Mar(2, "Março"),
        Abr(3, "Abril"),
        Mai(4, "Maio"),
        Jun(5, "Junho"),
        Jul(6, "Julho"),
        Ago(7, "Agosto"),
        Set(8, "Setembro"),
        Out(9, "Outubro"),
        Nov(10, "Novembro"),
        Dez(11, "Dezembro");

        private int id;
        private String fullName;

        Label(int id, String fullName) {
            this.id = id;
            this.fullName = fullName;
        }

        public static Label getByNumber(int number) {
            int id = number - 1;
            return Label.values()[id];
        }

        public static Label getById(int id) {
            return Label.values()[id];
        }

        public Label getNext() {
            int next = id + 1;
            if(next > 11){
                next = 0;
            }
            return Label.values()[next];
        }

        public int getId() {
            return id;
        }

        public String getFullName() {
            return fullName;
        }
    }

}
