package br.com.egs.task.control.web.model.repository;

import br.com.egs.task.control.web.model.User;
import br.com.egs.task.control.web.model.exception.AuthenticateException;

import java.util.List;

public interface UserRepository {
    public User authenticate(String login, String pass) throws AuthenticateException;

    public List<User> getAll();

    public boolean updatePassword(User user);
        
    public boolean newUser(User user) ;

    public boolean remove(User user);
}
