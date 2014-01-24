package br.com.egs.task.control.core.repository;

import br.com.egs.task.control.core.entities.User;

import java.util.List;

/**
 *
 */
public interface Users {
    public User get(String login);
    public void add(User user);
    public List<User> getAll();
}
