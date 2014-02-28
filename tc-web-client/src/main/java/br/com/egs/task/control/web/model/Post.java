package br.com.egs.task.control.web.model;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Post {

    private static final String EMPTY = "";
	public static final String OPEN_TAG = "<span>";
    public static final String CLOSE_TAG = "</span>";
    private Calendar time;
    private String user;
    private String text;
    private transient Set<String> hashtags;
    private transient String html;

    public Post(Calendar time, String user, String text) {
        this.time = time;
        this.user = user;
        this.hashtags = new HashSet<>();

        if(text != null){
            this.text = text;
            extractHashtags(text);
            extractHtml(text);
        } else {
        	this.text = EMPTY;
        	this.html = EMPTY;
        }
    }

    private void extractHashtags(String text) {
        Pattern hashtag = Pattern.compile("(#\\w+)");
        Matcher matcher = hashtag.matcher(text);
        if (matcher.find()) {
            for (int index = 0; index < matcher.groupCount(); index++) {
                this.hashtags.add(matcher.group());
            }
        }
    }

    private void extractHtml(String text) {
        this.html = text;
        for (String hashtag : hashtags) {
            this.html = this.html.replaceAll(hashtag, spanTag(hashtag));
        }
    }

    private String spanTag(String text) {
        return OPEN_TAG.concat(text).concat(CLOSE_TAG);
    }

    public Calendar getTime() {
        return time;
    }

    public String getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public String getTextHtml() {
        return html;
    }
}
