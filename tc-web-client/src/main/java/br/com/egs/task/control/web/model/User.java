package br.com.egs.task.control.web.model;

import br.com.egs.task.control.web.model.filter.Applications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class User {

    private static final Logger log = LoggerFactory.getLogger(User.class);

    private String name;
    private String login;
    private String email;
    private String type;
    private String pass;
    private List<String> systems;
    
    public User(String name, String login, String email, String type, String pass, List<String> systems) {
        this.name = name;
        this.login = login;
        this.email = email;
        this.type = type;
        this.pass = pass;
        this.systems = systems;
    }
    
    public User(String name, String login, String email, List<String> systems) {
    	this(name, login, email, null, null, systems);
    }

    public boolean isAdmin(){
        return systems.contains(Applications.TASK_CONTROL);
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }
    
    public String getpass() {
        return pass;
    }
    
    public List<String> getSystems() {
        return systems;
    }

}
