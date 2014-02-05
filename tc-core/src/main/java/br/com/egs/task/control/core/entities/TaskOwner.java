package br.com.egs.task.control.core.entities;

/**
 * Represents a task owner. This is a short version of a User.
 */
public class TaskOwner {
    private String user;

    public TaskOwner(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskOwner taskOwner = (TaskOwner) o;

        if (user != null ? !user.equals(taskOwner.user) : taskOwner.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return user != null ? user.hashCode() : 0;
    }
}
