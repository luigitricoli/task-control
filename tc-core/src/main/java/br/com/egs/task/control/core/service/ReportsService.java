package br.com.egs.task.control.core.service;

import br.com.egs.task.control.core.reports.SimpleMonthlyReportResult;
import br.com.egs.task.control.core.reports.TaskReportGenerator;
import br.com.egs.task.control.core.utils.HttpResponseUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 */
@Path("reports")
public class ReportsService {

    private TaskReportGenerator reportGenerator;

    @Inject
    public ReportsService(TaskReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    @GET
    @Path("simpleMonthlyReport")
    @Produces("application/json;charset=UTF-8")
    public String simpleMonthlyReport(
            @QueryParam("year") String yearParam,
            @QueryParam("month") String monthParam
    ) {
        if (StringUtils.isBlank(yearParam)
                || StringUtils.isBlank(monthParam)) {
            HttpResponseUtils.throwBadRequestException("year and month parameters are required");
        }

        int year = 0;
        try {
            year = Integer.parseInt(yearParam, 10);
        } catch (NumberFormatException e) {
            HttpResponseUtils.throwBadRequestException("Invalid year: " + yearParam);
        }

        int month = 0;
        try {
            month = Integer.parseInt(monthParam, 10);
        } catch (NumberFormatException e) {
            HttpResponseUtils.throwBadRequestException("Invalid month: " + monthParam);
        }

        SimpleMonthlyReportResult result = reportGenerator.generateSimpleMonthlyReport(year, month);
        return new GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(result);
    }
}
