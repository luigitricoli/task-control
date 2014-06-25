package br.com.egs.task.control.web.controller;

import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.ResultException;
import br.com.caelum.vraptor.view.Results;
import br.com.egs.task.control.web.interceptor.AuthRequired;
import br.com.egs.task.control.web.model.SessionUser;
import br.com.egs.task.control.web.model.User;
import br.com.egs.task.control.web.model.repository.UserRepository;

@Resource
@AuthRequired
public class UsersController {

    private Result result;
    private UserRepository user;
    private SessionUser sessionUser;
	
    public UsersController(Result result, UserRepository user, SessionUser sessionUser) {
		super();
		this.result = result;
		this.user = user;
		this.sessionUser = sessionUser;
	}

	@Post("/changePass")
    public void changePass(String oldPass, String newPass, String newcPass) {
		  String usuario; usuario = sessionUser.getUser().getNickname();
		  
	//Verifica senha nova e antiga
		
		if (!newPass.equals(newcPass)){
			
			result.use(Results.http()).body("fail");
			
		}else if(user.authenticate(usuario, oldPass)){

			user.changePassword(usuario, newPass);
			result.use(Results.http()).body("sucess");
			
		}else{
			
			result.use(Results.http()).body("passFail");

		}

    }
	
	
    public void saveNewPass(String usuario, String newPass) {
		user.changePassword(usuario, newPass);
	}
	
	
    
}
