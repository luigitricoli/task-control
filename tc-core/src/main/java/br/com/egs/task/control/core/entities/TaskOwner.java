package br.com.egs.task.control.core.entities;

/**
 * Represents a task owner. This is a short version of a User.
 */
public class TaskOwner {
    private String login;
    private String name;
    private String type;
    private Integer workHours;

    public TaskOwner(String login, String name, String type, Integer workHours) {
        this.login = login;
        this.name = name;
        this.type = type;
        this.workHours = workHours;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Integer getWorkHours() {
        return workHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskOwner owner = (TaskOwner) o;

        if (login != null ? !login.equals(owner.login) : owner.login != null) return false;
        if (name != null ? !name.equals(owner.name) : owner.name != null) return false;
        if (type != null ? !type.equals(owner.type) : owner.type != null) return false;
        if (workHours != null ? !workHours.equals(owner.workHours) : owner.workHours != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (workHours != null ? workHours.hashCode() : 0);
        return result;
    }
}
