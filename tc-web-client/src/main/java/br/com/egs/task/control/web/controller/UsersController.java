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
		System.out.println("UsersController passou");
		  String usuario; usuario = sessionUser.getUser().getNickname();
		  System.out.println(usuario);
	//Verifica senha nova
		
		if (!newPass.equals(newcPass)){
			result.use(Results.http()).body("fail");
			System.out.println("Senha nova incorreta");
		}else if(user.authenticate(usuario, oldPass)){
			System.out.println("Senha antiga correta");
			result.use(Results.http()).body("sucess");
		}else{
			result.use(Results.http()).body("passFail");
			System.out.println("Senha anatiga incorret");
		}

		//CRIAR VERIFICAÇÃO DE SENHA ANTIGA
		

		 
	/*	if(user.authenticate(usuario, oldPass)){
			System.out.println("Senha correta");
			result.use(Results.http()).body("passSucess");
		}else{
			System.out.println("Senha incorreta");
			result.use(Results.http()).body("passFail");
		}
		*/
			
		System.out.println(oldPass);
		System.out.println(newPass);
		System.out.println(newcPass);
    }
	
	
	
    
}
