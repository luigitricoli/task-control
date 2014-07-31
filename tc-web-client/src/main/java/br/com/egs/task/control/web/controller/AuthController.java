package br.com.egs.task.control.web.controller;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.egs.task.control.web.model.SessionUser;
import br.com.egs.task.control.web.model.filter.Applications;
import br.com.egs.task.control.web.model.filter.Sources;
import br.com.egs.task.control.web.model.repository.UserRepository;

import javax.inject.Inject;
import java.io.Serializable;

@Controller
public class AuthController implements Serializable {

    private static final long serialVersionUID = -3709586215500930152L;

    @Inject
    private Result result;
    @Inject
    private UserRepository user;
    @Inject
    private SessionUser sessionUser;
    @Inject
    private Applications app;
    @Inject
    private Sources source;

    @Get("/login")
    public void index(){
    }

    @Post("/login")
    public void login(String nickname, String pass){
        if(user.authenticate(nickname, pass)){
            result.redirectTo(TasksController.class).index(null);
        } else {
            result.redirectTo(this).index();
        }
    }

    @Get("/logout")
    public void logout(){
        sessionUser.logout();
        result.redirectTo(this).index();
    }

}
