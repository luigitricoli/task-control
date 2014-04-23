package br.com.egs.task.control.web.controller;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.egs.task.control.web.model.repository.TaskReportRepository;
import br.com.egs.task.control.web.rest.client.reports.CoreSimpleMonthlyReport;
import br.com.egs.task.control.web.rest.client.reports.TaskReportClient;

/**
 *
 */
@Resource
public class ReportsController {

    private Result result;
    private TaskReportRepository reportRepository;

    public ReportsController(Result result, TaskReportRepository reportRepository) {
        this.result = result;
        this.reportRepository = reportRepository;
    }

    @Get("/relatorios")
    public void index() {

    }

    @Get("/relatorios/mensalprojetos")
    public void tasksMonthlyReport(String year, String month) {

        Integer yearInt = Integer.valueOf(year, 10);
        Integer monthInt = Integer.valueOf(month, 10);

        CoreSimpleMonthlyReport report = reportRepository.simpleMonthlyReport(yearInt, monthInt);
        result.include("items", report.getRecords());
    }
}
