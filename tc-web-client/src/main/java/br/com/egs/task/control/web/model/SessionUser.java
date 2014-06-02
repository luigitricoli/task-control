package br.com.egs.task.control.web.model;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;
import br.com.egs.task.control.web.model.filter.Applications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Component
@SessionScoped
public class SessionUser {

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
