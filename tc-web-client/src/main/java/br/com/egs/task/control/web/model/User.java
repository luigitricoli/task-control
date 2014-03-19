package br.com.egs.task.control.web.model;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;

@Component
@SessionScoped
public class User {

    private String nickname;
    private String email;
    private String pass;

    private Boolean logged;

    public User() {
        this.logged = false;
    }

    public Boolean login(String nickname, String pass){
        this.nickname = nickname;
        this.pass = pass;

        this.logged = true;

        return isLogged();
    }

    public Boolean logout(){
        this.nickname = null;
        this.pass = null;
        this.email = null;

        this.logged = false;

        return !isLogged();
    }

    public boolean isLogged(){
        return logged;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }
}
