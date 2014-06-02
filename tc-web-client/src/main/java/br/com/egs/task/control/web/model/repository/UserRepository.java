package br.com.egs.task.control.web.model.repository;

import br.com.egs.task.control.web.model.User;

import java.util.List;

public interface UserRepository {
    public boolean authenticate(String login, String pass);

    public List<User> getAll();
}
