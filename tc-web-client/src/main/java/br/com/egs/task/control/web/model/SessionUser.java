package br.com.egs.task.control.web.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;

@SessionScoped
public class SessionUser implements Serializable{

    private static final long serialVersionUID = 4544357036779454022L;
    private static final Logger log = LoggerFactory.getLogger(SessionUser.class);

    private User user;

    public SessionUser() {}

    public Boolean login(String name, String login, String email, List<String> systems) {
        user = new User(name, login, email, systems);

        log.debug("Loggin as {}, handling the applications {}", login, systems);

        return isLogged();
    }

    public Boolean logout() {
        user = null;
        return !isLogged();
    }

    public boolean isLogged() {
        return user != null;
    }

    public boolean isAdmin(){
        return user.isAdmin();
    }

    public User getUser(){
        return user;
    }
}
