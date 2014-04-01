package br.com.egs.task.control.web.model.repository;

public interface UserRepository {
    boolean authenticate(String login, String pass);
}
