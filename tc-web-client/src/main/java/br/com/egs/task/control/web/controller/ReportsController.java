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

import java.util.List;

@Controller
@AuthRequired
public class ReportsController {
    
    private Result result;
    private TaskRepository tasks;
    private SessionUser session;

    public ReportsController(Result result, TaskRepository tasks, SessionUser session) {
        this.result = result;
        this.tasks = tasks;
        this.session = session;
    }
    
    @Get("/relatorios")
    public void index() {
        
    }
    
    @Get("/relatorios/listaTarefas")
    public void simpleTaskList(Integer month, Integer year) {
        List<SimpleTaskData> taskList = tasks.listTasks(month, year);
        result.include("tasks", taskList);
    }
}
