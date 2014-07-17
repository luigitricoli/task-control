package br.com.egs.task.control.core.reports;

/**
 *
 */
public interface TaskReportGenerator {

    public SimpleMonthlyReportResult generateSimpleMonthlyReport(int year, int month);


    public UserTypeSummaryResult generateUserTypeSummaryReport(int year, int month);
}
