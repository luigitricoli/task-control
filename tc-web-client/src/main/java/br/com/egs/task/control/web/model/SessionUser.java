package br.com.egs.task.control.web.model;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SessionScoped
public class SessionUser implements Serializable{

    private static final long serialVersionUID = 4544357036779454022L;
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user.getNickname() == null) ? 0 : user.getNickname().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		User other = (User) obj;
		if (user.getNickname() == null) {
			if (other.getNickname() != null)
				return false;
		} else if (!user.getNickname().equals(other.getNickname()))
			return false;
		return true;
	} 

    
}
