/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.egs.task.control.web.controller;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.egs.task.control.web.interceptor.AuthRequired;
import br.com.egs.task.control.web.model.SessionUser;
import br.com.egs.task.control.web.model.SimpleTaskData;
import br.com.egs.task.control.web.model.exception.InvalidDateException;
import br.com.egs.task.control.web.model.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@AuthRequired
public class ReportsController {

    private static final Logger log = LoggerFactory.getLogger(ReportsController.class);

    private static final String BRAZILIAN_DATE_FORMAT = "dd/MM/yy";
    private static final int BAD_REQUEST_STATUS = 400;

    @Inject
    private Result result;
    @Inject
    private TaskRepository tasks;
    @Inject
    private SessionUser session;
    
    @Get("/relatorios")
    public void index() {
        
    }
    
    @Get("/relatorios/listaTarefas")
    public void simpleTaskList(Integer month, Integer year) {
        List<SimpleTaskData> taskList = tasks.listTasks(month, year);
        result.include("tasks", taskList);
    }

    @Get("/relatorios/atividadesDiarias")
    public void dailyActivities(String date) {
        try {
            List<SimpleTaskData> taskList = tasks.listActiveTasks(date, BRAZILIAN_DATE_FORMAT);

            // Sort by owner, to a better visualization
            Collections.sort(taskList, new Comparator<SimpleTaskData>() {
                @Override
                public int compare(SimpleTaskData task1, SimpleTaskData task2) {
                     return task1.getOwners().compareTo(task2.getOwners());
                }
            });

            result.include("tasks", taskList);
        } catch (InvalidDateException e) {
            log.error(e.getMessage());
            result.use(Results.http()).body("Invalid date").setStatusCode(BAD_REQUEST_STATUS);
        }
    }
}
