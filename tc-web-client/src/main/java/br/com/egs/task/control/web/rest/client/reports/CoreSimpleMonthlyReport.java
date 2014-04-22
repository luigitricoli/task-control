package br.com.egs.task.control.web.rest.client.reports;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Result DTO for the Simple Monthly Report.
 */
public class CoreSimpleMonthlyReport {

    private List<Item> records = new ArrayList<>();

    public static CoreSimpleMonthlyReport unmarshal(String json) {
        return new Gson()
                .fromJson(json, CoreSimpleMonthlyReport.class);
    }

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
