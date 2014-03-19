package br.com.egs.task.control.web.controller;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.egs.task.control.web.model.User;

@Resource
public class AuthController {

    private Result result;
    private User user;

    public AuthController(Result result, User user) {
        this.result = result;
        this.user = user;
    }

    @Get("/login")
    public void index(){
    }

    @Post("/login")
    public void login(String nickname, String pass){
        this.user.login(nickname, pass);
        result.redirectTo(TasksController.class).index(null);
    }

    @Get("/logout")
    public void logout(){
        this.user.logout();
        result.redirectTo(this).index();
    }

}
