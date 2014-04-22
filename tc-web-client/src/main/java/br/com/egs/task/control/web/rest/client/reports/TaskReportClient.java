package br.com.egs.task.control.web.rest.client.reports;


import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.egs.task.control.web.model.SessionUser;
import br.com.egs.task.control.web.model.repository.TaskReportRepository;
import br.com.egs.task.control.web.rest.client.JsonClient;
import br.com.egs.task.control.web.rest.client.Response;
import br.com.egs.task.control.web.rest.client.task.FilterFormat;

@Component
@RequestScoped
public class TaskReportClient implements TaskReportRepository {
    public static final int SUCCESS_CODE = 200;

    private JsonClient jsonClient;
    private FilterFormat fomatter;
    private SessionUser user;

    public TaskReportClient(JsonClient jsonClient, FilterFormat fomatter, SessionUser user) {
        this.jsonClient = jsonClient;
        this.fomatter = fomatter;
        this.user = user;
    }

    public CoreSimpleMonthlyReport simpleMonthlyReport(Integer year, Integer month) {
        jsonClient.at("reports/simpleMonthlyReport")
                .addUrlParam("year", year.toString())
                .addUrlParam("month", month.toString());
        Response reportJson = jsonClient.getAsJson();

        if (reportJson.getCode() != SUCCESS_CODE) {
            // TODO Handle this gracefully
            throw new RuntimeException("HTTP Error: " + reportJson.getCode());
        }

        String content = reportJson.getContent();
        System.out.println("++++++++++++++++++++++++++++++++++\n" + content);
        return CoreSimpleMonthlyReport.unmarshal(content);
    }

}
