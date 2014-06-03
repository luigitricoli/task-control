package br.com.egs.task.control.core.reports;

import br.com.egs.task.control.core.entities.Task;
import br.com.egs.task.control.core.entities.TaskOwner;
import br.com.egs.task.control.core.repository.TaskSearchCriteria;
import br.com.egs.task.control.core.repository.TasksRepository;

import javax.inject.Inject;
import java.util.*;

/**
 *
 */
public class TaskReportGeneratorImpl implements TaskReportGenerator {

    private TasksRepository taskRepository;

    @Inject
    public TaskReportGeneratorImpl(TasksRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public SimpleMonthlyReportResult generateSimpleMonthlyReport(int year, int month) {
        TaskSearchCriteria criteria = new TaskSearchCriteria().month(year, month);
        List<Task> tasks = taskRepository.searchTasks(criteria);

        SimpleMonthlyReportResult result = new SimpleMonthlyReportResult();

        for (Task task : tasks) {
            // The report presents one record per owner, not one record per task
            // Project hours are divided among the workers
            int totalHours = calculateWorkHours(task, year, month);
            int numberOfWorkers = task.getOwners().size();

            for (TaskOwner owner : task.getOwners()) {
                result.addItem(new SimpleMonthlyReportResult.Item(
                        owner.getName(),
                        task.getApplication().getName(),
                        totalHours / numberOfWorkers,
                        task.getDescription()));
            }
        }

        return result;
    }

    @Override
    public UserTypeSummaryResult generateUserTypeSummaryReport(int year, int month) {
        TaskSearchCriteria criteria = new TaskSearchCriteria().month(year, month);
        List<Task> tasks = taskRepository.searchTasks(criteria);

        Map<ApplicationUserTypeKey, Integer> hourSummary = new TreeMap<>();

        for (Task task : tasks) {
            // The report presents one record per owner, not one record per task
            // Project hours are divided among the workers
            int totalHours = calculateWorkHours(task, year, month);
            int numberOfWorkers = task.getOwners().size();

            for (TaskOwner owner : task.getOwners()) {
                int hoursPerWorker = totalHours / numberOfWorkers;
                hourSummary.put(
                        new ApplicationUserTypeKey(task.getApplication().getName(), owner.getType()), hoursPerWorker);
            }
        }

        UserTypeSummaryResult result = new UserTypeSummaryResult();
        for (ApplicationUserTypeKey key : hourSummary.keySet()) {
            result.addItem(new UserTypeSummaryResult.Item(key.application, key.userType, hourSummary.get(key)));
        }
        return result;
    }

    private int calculateWorkHours(Task task, int reportYear, int reportMonth) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(task.getStartDate());

        // If the task does not contain an end date, it stretches to the current day
        Date end = task.getEndDate() != null ? task.getEndDate() : new Date();

        int totalHours = 0;
        while (cal.getTime().before(end)) {

            boolean isWorkday = cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                    && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY;

            boolean isInDesiredMonth = cal.get(Calendar.YEAR) == reportYear
                    && (cal.get(Calendar.MONTH) + 1) == reportMonth;

            if (isWorkday && isInDesiredMonth) {
                totalHours += 8;
            }

            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        return totalHours;
    }

    private static class ApplicationUserTypeKey implements Comparable<ApplicationUserTypeKey> {
        String application;
        String userType;

        private ApplicationUserTypeKey(String application, String userType) {
            this.application = application;
            this.userType = userType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ApplicationUserTypeKey that = (ApplicationUserTypeKey) o;

            if (application != null ? !application.equals(that.application) : that.application != null) return false;
            if (userType != null ? !userType.equals(that.userType) : that.userType != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = application != null ? application.hashCode() : 0;
            result = 31 * result + (userType != null ? userType.hashCode() : 0);
            return result;
        }

        @Override
        public int compareTo(ApplicationUserTypeKey o) {
            if (this.application.compareTo(o.application) != 0) {
                return this.application.compareTo(o.application);
            } else {
                return this.userType.compareTo(o.userType);
            }
        }
    }
}
