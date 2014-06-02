package br.com.egs.task.control.web.controller;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.egs.task.control.web.interceptor.AuthRequired;
import br.com.egs.task.control.web.model.SessionUser;
import br.com.egs.task.control.web.model.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Resource
@AuthRequired
public class FiltersController {

    private static final Logger log = LoggerFactory.getLogger(FiltersController.class);

    private Result result;
    private SessionUser session;
    private UserRepository users;

    public FiltersController(Result result, SessionUser session, UserRepository users) {
        this.result = result;
        this.session = session;
        this.users = users;
    }

    @Get("/filtros/usuarios")
    public void users() {
       result.include("users", users.getAll());
    }

}
