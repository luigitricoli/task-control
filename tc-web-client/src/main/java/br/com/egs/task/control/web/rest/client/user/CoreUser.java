package br.com.egs.task.control.web.rest.client.user;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class CoreUser {

    private String name;
    private String login;
    private String email;

    public CoreUser(String name, String login, String email) {
        this.name = name;
        this.login = login;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public static CoreUser fromJson(String json){
        return new Gson().fromJson(json, CoreUser.class);
    }
}
