package br.com.egs.task.control.web.rest.client.user;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.egs.task.control.web.model.SessionUser;
import br.com.egs.task.control.web.model.repository.UserRepository;
import br.com.egs.task.control.web.rest.client.JsonClient;
import br.com.egs.task.control.web.rest.client.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

@Component
@RequestScoped
public class UserClient implements UserRepository {

    private static final Logger log = LoggerFactory.getLogger(UserClient.class);
    public static final int SUCCESS_CODE = 200;

    private JsonClient jsonClient;
    private SessionUser user;

	public UserClient(JsonClient jsonClient, SessionUser user) {
        this.jsonClient = jsonClient;
        this.user = user;
    }

    @Override
	public boolean authenticate(String login, String pass){
        String json = String.format("{username:'%s', password:'%s'}", login, pass);
        Response response = jsonClient.at("authentication").postAsJson(json);

        if(response.getCode().equals(SUCCESS_CODE)){
            CoreUser coreUser = CoreUser.fromJson(response.getContent());
            user.login(coreUser.getName(), coreUser.getLogin(), coreUser.getEmail(), coreUser.getApplications());
            return true;
        }

        return false;
	}

}
