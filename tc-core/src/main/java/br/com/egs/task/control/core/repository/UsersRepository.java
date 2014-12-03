package br.com.egs.task.control.core.repository;

import br.com.egs.task.control.core.entities.User;

import java.util.List;

/**
 *
 */
public interface UsersRepository {
    public User get(String login);

    void remove(User user);

    public void add(User user);
    public List<User> getAll();
    public List<User> getByApplication(String application);
    public void update(User user);
}
