package br.com.egs.task.control.web.controller;

//import org.eclipse.jetty.util.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.egs.task.control.web.model.SessionUser;
import br.com.egs.task.control.web.model.User;
import br.com.egs.task.control.web.model.exception.AuthenticateException;
import br.com.egs.task.control.web.model.repository.UserRepository;


@Resource
public class AuthController {
	
	private static final Logger log = LoggerFactory.getLogger(TasksController.class);
    private Result result;
    private UserRepository users;
    private SessionUser sessionUser;

    public AuthController(Result result, UserRepository users, SessionUser sessionUser) {
        this.result = result;
        this.users = users;
        this.sessionUser = sessionUser;
    }

    @Get("/login")
    public void index(){
    }

    @Post("/login")
    public void login(String nickname, String pass){
    	try{
    		User user = users.authenticate(nickname, pass);
    			sessionUser.login(user);
    			result.redirectTo(TasksController.class).index(null);    		
    	}catch (AuthenticateException cause) {
    		
    		result.redirectTo(this).index();
    		log.error(cause.getMessage(), cause);
		}
    }

    @Get("/logout")
    public void logout(){
        sessionUser.logout();
        result.redirectTo(this).index();
    }

}
