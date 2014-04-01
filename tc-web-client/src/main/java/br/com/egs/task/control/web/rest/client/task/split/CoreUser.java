package br.com.egs.task.control.web.rest.client.task.split;

public class CoreUser {

    private String login;
    private String fullName;

    public CoreUser(String login, String fullName) {
        this.login = login;
        this.fullName = fullName;
    }

    public String getLogin() {
        return login;
    }

    public String getFullName() {
        return fullName;
    }
}
