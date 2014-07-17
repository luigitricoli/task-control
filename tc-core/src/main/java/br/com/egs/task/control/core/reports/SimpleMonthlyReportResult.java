package br.com.egs.task.control.core.reports;

import java.util.ArrayList;
import java.util.List;

/**
 * Result DTO for the Simple Monthly Report.
 */
public class SimpleMonthlyReportResult {

    private List<Item> records = new ArrayList<>();

    public void addItem(Item item) {
        records.add(item);
    }

    public List<Item> getRecords() {
        return records;
    }

        public static class Item {
            private String user;
            private String application;
            private int hoursInMonth;
            private String taskDescription;

            public Item(String user, String application, int hoursInMonth, String taskDescription) {
                this.user = user;
                this.application = application;
                this.hoursInMonth = hoursInMonth;
                this.taskDescription = taskDescription;
            }

        public String getUser() {
            return user;
        }

        public String getApplication() {
            return application;
        }

        public int getHoursInMonth() {
            return hoursInMonth;
        }

        public String getTaskDescription() {
            return taskDescription;
        }
    }
}
