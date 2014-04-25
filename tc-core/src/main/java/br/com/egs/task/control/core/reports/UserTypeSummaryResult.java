package br.com.egs.task.control.core.reports;

import java.util.ArrayList;
import java.util.List;

/**
 * Result DTO for the User type Summary Report.
 */
public class UserTypeSummaryResult {

    private List<Item> records = new ArrayList<>();

    public void addItem(Item item) {
        records.add(item);
    }

    public List<Item> getRecords() {
        return records;
    }

        public static class Item {
            private String application;
            private String userType;
            private int hoursInMonth;

            public Item(String application, String userType, int hoursInMonth) {
                this.application = application;
                this.userType = userType;
                this.hoursInMonth = hoursInMonth;
            }

            public String getApplication() {
                return application;
            }

            public String getUserType() {
                return userType;
            }

            public int getHoursInMonth() {
                return hoursInMonth;
            }
        }
}