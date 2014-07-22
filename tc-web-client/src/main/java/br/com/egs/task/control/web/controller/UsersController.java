package br.com.egs.task.control.web.controller;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.egs.task.control.web.interceptor.AuthRequired;
import br.com.egs.task.control.web.model.SessionUser;
import br.com.egs.task.control.web.model.User;
import br.com.egs.task.control.web.model.repository.UserRepository;
import br.com.egs.task.control.web.rest.client.user.UserClient;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Resource
@AuthRequired
public class UsersController {
	
	private static final Logger log = LoggerFactory.getLogger(UserClient.class);
	
	private Result result;
	private UserRepository users;
	private SessionUser sessionUser;

	public UsersController(Result result, UserRepository user,
			SessionUser sessionUser) {
		super();
		this.result = result;
		this.users = user;
		this.sessionUser = sessionUser;
	}

    @Get("/usuarios")
    public void index(){}

	@Post("/changePass")
	public void changePass(String oldPass, String newPass, String newcPass) {
		String nickName;
		nickName = sessionUser.getUser().getNickname();

		// Verifica senha nova e antiga

		if (!newPass.equals(newcPass)) {

			result.use(Results.http()).body("Confirmação de senha incorreta!");

		} else if (users.authenticate(nickName, oldPass)) {

			users.changePassword(nickName, newPass);
			result.use(Results.http()).body("sucess");

		} else {

			result.use(Results.http()).body("Senha antiga incorreta!");

		}

	}

	
	@Post("/saveUser")
	public void saveUser(String name, String email, String login,String type, String pass, List<String> applications) {

		/*boolean vemail;
		vemail = validEmail(email);
*/
		User cadUser;
		cadUser = new User(name, login, email, type, pass, applications);

			if(users.newUser(cadUser)){
				result.use(Results.http()).body("sucess");	
			}else{
				result.use(Results.http()).body("fail");
			}
			
	}


/*	//Valida Email
	public boolean validEmail(String email) {
		Pattern p = Pattern
				.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$");
		Matcher m = p.matcher(email);
		if (m.find()) {
			log.debug("O Email [{}] é valido", email);
			//return "O email " + email + " e valido";
			return true;
		} else {
			log.debug("O Email [{}] é inválido", email);
			//return "O E-mail " + email + " é inválido";
			return false;
		}
	}*/

}
