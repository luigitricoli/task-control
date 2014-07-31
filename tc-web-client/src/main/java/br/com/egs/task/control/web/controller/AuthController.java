package br.com.egs.task.control.web.controller;

import java.io.Serializable;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.egs.task.control.web.model.SessionUser;
import br.com.egs.task.control.web.model.User;
import br.com.egs.task.control.web.model.exception.AuthenticateException;
import br.com.egs.task.control.web.model.filter.Applications;
import br.com.egs.task.control.web.model.filter.Sources;
import br.com.egs.task.control.web.model.repository.UserRepository;


@Controller
public class AuthController implements Serializable {

    private static final long serialVersionUID = -3709586215500930152L;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    
    @Inject
    private Result result;
    @Inject
    private UserRepository users;
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
