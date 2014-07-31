package br.com.egs.task.control.web.controller;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.egs.task.control.web.interceptor.AuthRequired;
import br.com.egs.task.control.web.model.SessionUser;
import br.com.egs.task.control.web.model.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@Controller
@AuthRequired
public class FiltersController {

    private static final Logger log = LoggerFactory.getLogger(FiltersController.class);

    @Inject
    private Result result;
    @Inject
    private SessionUser session;
    @Inject
    private UserRepository users;

    @Get("/filtros/usuarios")
    public void users() {
       result.include("users", users.getAll());
    }

}
