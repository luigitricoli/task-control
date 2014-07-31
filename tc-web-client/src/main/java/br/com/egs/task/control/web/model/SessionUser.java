package br.com.egs.task.control.web.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;

@Component
@SessionScoped
public class SessionUser {

    private static final Logger log = LoggerFactory.getLogger(SessionUser.class);
    private User user;

    public SessionUser() {}

    public Boolean login(User user) {
        this.user = user;
        log.debug("Loggin as {}, handling the applications {}", user.getNickname(), user.getSystems());

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
