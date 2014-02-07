package br.com.egs.task.control.core.entities;

/**
 * Represents a task owner. This is a short version of a User.
 */
public class TaskOwner {
    private String login;

    public TaskOwner(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskOwner taskOwner = (TaskOwner) o;

        if (login != null ? !login.equals(taskOwner.login) : taskOwner.login != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return login != null ? login.hashCode() : 0;
    }
}
