package br.com.egs.task.control.web.controller;

import java.util.List;

import javax.inject.Inject;

import br.com.caelum.vraptor.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.view.Results;
import br.com.egs.task.control.web.interceptor.AuthRequired;
import br.com.egs.task.control.web.model.SessionUser;
import br.com.egs.task.control.web.model.User;
import br.com.egs.task.control.web.model.exception.AuthenticateException;
import br.com.egs.task.control.web.model.exception.UpdateException;
import br.com.egs.task.control.web.model.repository.UserRepository;
import br.com.egs.task.control.web.rest.client.user.UserClient;

@Controller
@AuthRequired
public class UsersController {
	
	private static final Logger log = LoggerFactory.getLogger(UserClient.class);

    @Inject
	private Result result;
    @Inject
	private UserRepository users;
    @Inject
	private SessionUser sessionUser;

    @Get("/usuarios")
    public void index(){
        result.include("users", users.getAll());
    }

    @Delete("/usuarios/{login}")
    public void changePass(String login) {
        User user = new User(login);
        if (users.remove(user)){
            result.use(Results.http()).body("success");
        } else {
            result.use(Results.http()).body("error");
        }
    }

	@Post("/senha")
	public void changePass(String oldPass, String newPass, String newcPass) {
		try{
			
			User user = users.authenticate(sessionUser.getUser().getNickname(), oldPass);
			user = user.changePassword(newPass, newcPass);
			users.updatePassword(user);
			result.use(Results.http()).body("sucess");
			
		} catch (UpdateException cause) {
			result.use(Results.http()).body(cause.getMessage());
			
		}catch (AuthenticateException cause) {
			result.use(Results.http()).body("Senha antiga incorreta!");
		}
	}

	
	@Post("/usuarios")
	public void saveUser(String name, String email, String login,String type, String pass, List<String> applications) {
		
		User cadUser;
		cadUser = new User(name, login, email, type, pass, applications);

			if(users.newUser(cadUser)){
				result.use(Results.http()).body("sucess");	
			}else{
				result.use(Results.http()).body("fail");
			}
	}

}
