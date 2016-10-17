package br.com.egs.task.control.web.rest.client.user;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.egs.task.control.web.model.User;
import br.com.egs.task.control.web.model.exception.AuthenticateException;
import br.com.egs.task.control.web.model.repository.UserRepository;
import br.com.egs.task.control.web.rest.client.JsonClient;
import br.com.egs.task.control.web.rest.client.Response;

@RequestScoped
public class UserClient implements UserRepository {

    private static final Logger log = LoggerFactory.getLogger(UserClient.class);

    private JsonClient jsonClient;

    /**
     * @deprecated only needed because CDI
     */
    public UserClient() {
    }

    @Inject
	public UserClient(JsonClient jsonClient) {
        this.jsonClient = jsonClient;
    }


    @Override
	public User authenticate(String login, String pass) throws AuthenticateException{

        String json = String.format("{username:'%s', password:'%s'}", login, pass);
        Response response = jsonClient.at("authentication").postAsJson(json);
        if(response.isSuccess()){
            CoreUser coreUser = CoreUser.unmarshal(response.getContent());
            return new User(coreUser.getName(), coreUser.getLogin(), coreUser.getEmail(), coreUser.getType(), coreUser.getPassword(), coreUser.getApplications());
        }

        //return null;
        throw new AuthenticateException("Invalid User!");
	}

    @Override
	public boolean updatePassword(User user){
        String json = String.format("{password:\"%s\"}",user.getPass());
        Response response = jsonClient.at("users/"+user.getNickname()).putAsJson(json);
        if(response.isSuccess()){
            return true;
        }

        return false;
	}
    
    @Override
  	public boolean newUser(User cadUser){
          CoreUser  coreUser;
          coreUser = new CoreUser(cadUser.getNickname(),cadUser.getName(),cadUser.getEmail(),cadUser.getType(),cadUser.getPass(),cadUser.getSystems());
          
          String json = coreUser.toJson();
          
          log.debug("UserClient JSON [{}]", json);
          Response response = jsonClient.at("users/").postAsJson(json);
          
          if(response.isSuccess()){

              return true;
          }

          return false;
  	}

    @Override
    public boolean remove(User user) {
        Response response = jsonClient.at("users/".concat(user.getNickname())).delete();
        if(response.isSuccess()){
            return true;
        }

        return false;
    }

    @Override
    public List<User> getAll(){
        Response response = jsonClient.at("users").getAsJson();
        List<User> users = new ArrayList<>();
        if(response.isSuccess()){
            for(CoreUser user : CoreUser.unmarshalList(response.getContent())){
                users.add(new User(user.getName(), user.getLogin(), user.getEmail(), user.getApplications()));
            }
        }

        return users;
    }

}
