package br.com.egs.task.control.web.rest.client.user;

import br.com.egs.task.control.web.model.SessionUser;
import br.com.egs.task.control.web.model.User;
import br.com.egs.task.control.web.model.repository.UserRepository;
import br.com.egs.task.control.web.rest.client.JsonClient;
import br.com.egs.task.control.web.rest.client.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
public class UserClient implements UserRepository {

    private static final Logger log = LoggerFactory.getLogger(UserClient.class);
    public static final int SUCCESS_CODE = 200;

    @Inject
    private JsonClient jsonClient;
    @Inject
    private SessionUser user;

    @Override
	public boolean authenticate(String login, String pass){
        String json = String.format("{username:'%s', password:'%s'}", login, pass);
        Response response = jsonClient.at("authentication").postAsJson(json);
        if(response.getCode().equals(SUCCESS_CODE)){
            CoreUser coreUser = CoreUser.unmarshal(response.getContent());
            user.login(coreUser.getName(), coreUser.getLogin(), coreUser.getEmail(), coreUser.getApplications());
            return true;
        }

        return false;
	}

    @Override
	public boolean changePassword(String login, String newPass){
        String json = String.format("{password:\"%s\"}",newPass);
        Response response = jsonClient.at("users/"+login).putAsJson(json);
        if(response.getCode().equals(SUCCESS_CODE)){
            CoreUser coreUser = CoreUser.unmarshal(response.getContent());
            user.login(coreUser.getName(), coreUser.getLogin(), coreUser.getEmail(), coreUser.getApplications());
            return true;
        }

        return false;
	}
    
    @Override
  	public boolean newUser(User cadUser){
    	
    	String apps="";
        for ( String s : cadUser.getSystems())
        {
      	  apps += "{name:'"+s+"'},";
      	  
        }
        apps = apps.substring (0, apps.length() - 1);  
        
          String json = String.format("{login:'%s', name: '%s', email: '%s', type: '%s', password: '%s',applications: [%s]}",
        		  cadUser.getNickname(),cadUser.getName(),cadUser.getEmail(),cadUser.getType(),cadUser.getpass(),apps);

          log.debug("UserCliente JSON [{}]", json);
          Response response = jsonClient.at("users/").postAsJson(json);
          
          if(response.getCode().equals(SUCCESS_CODE)){
             // CoreUser coreUser = CoreUser.unmarshal(response.getContent());
             // user.login(coreUser.getName(), coreUser.getLogin(), coreUser.getEmail(), coreUser.getApplications());
              return true;
          }

          return false;
  	}
    
    @Override
    public List<User> getAll(){
        Response response = jsonClient.at("users").getAsJson();
        List<User> users = new ArrayList<>();
        if(response.getCode().equals(SUCCESS_CODE)){
            for(CoreUser user : CoreUser.unmarshalList(response.getContent())){
                users.add(new User(user.getName(), user.getLogin(), user.getEmail(), user.getApplications()));
            }
        }

        return users;
    }

}
