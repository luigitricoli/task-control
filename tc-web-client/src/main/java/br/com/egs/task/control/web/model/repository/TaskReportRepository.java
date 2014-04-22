package br.com.egs.task.control.web.model.repository;

import br.com.egs.task.control.web.rest.client.reports.CoreSimpleMonthlyReport;

/**
 *
 */
public interface TaskReportRepository {

    public CoreSimpleMonthlyReport simpleMonthlyReport(Integer year, Integer month);
}
