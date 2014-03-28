package br.com.egs.task.control.web.controller;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.egs.task.control.web.model.SessionUser;
import br.com.egs.task.control.web.model.repository.UserRepository;

@Resource
public class AuthController {

    private Result result;
    private UserRepository user;
    private SessionUser sessionUser;

    public AuthController(Result result, UserRepository user, SessionUser sessionUser) {
        this.result = result;
        this.user = user;
        this.sessionUser = sessionUser;
    }

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
