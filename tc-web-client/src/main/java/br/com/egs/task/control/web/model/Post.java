package br.com.egs.task.control.web.model;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Post {

    private Calendar time;
    private String user;
    private String content;

    public Post(Calendar time, String user, String content) {
        this.time = time;
        this.user = user;
        this.content = content;
    }

    public Calendar getTime() {
        return time;
    }

    public String getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    public String getContentHtml(){
        Pattern hashSymbol = Pattern.compile("( (#)(\\w+) )");
        Matcher matcher = hashSymbol.matcher(content);
        int hashtagCount = Math.round(matcher.groupCount()/3);
        for(int index = 0; index < hashtagCount; index++){

        }

        return null;
    }
}
