/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.egs.task.control.web.controller;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.egs.task.control.web.interceptor.AuthRequired;
import br.com.egs.task.control.web.model.SessionUser;
import br.com.egs.task.control.web.model.SimpleTaskData;
import br.com.egs.task.control.web.model.repository.TaskRepository;

import javax.inject.Inject;
import java.util.List;

@Controller
@AuthRequired
public class ReportsController {

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
}
