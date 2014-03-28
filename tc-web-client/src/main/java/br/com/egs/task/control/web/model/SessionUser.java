package br.com.egs.task.control.web.model;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;

import java.util.List;

@Component
@SessionScoped
public class SessionUser {

    private String name;
    private String login;
    private String email;
    private List<String> systems;

    public SessionUser() {
        this.name = null;
        this.login = null;
        this.email = null ;
        this.systems = null;
    }

    public Boolean login(String name, String login, String email, List<String> systems){
        this.name = name;
        this.login = login;
        this.email = email;
        this.systems = systems;

        return isLogged();
    }

    public Boolean logout(){
        this.name = null;
        this.login = null;
        this.email = null ;
        this.systems = null;

        return !isLogged();
    }

    public boolean isLogged(){
        return name != null && login != null;
    }

    public String getNickname() {
        return login;
    }

    public String getEmail() {
        return email;
    }
}
