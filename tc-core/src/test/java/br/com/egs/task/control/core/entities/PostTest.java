package br.com.egs.task.control.core.entities;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class PostTest {

    private final DateFormat timestampParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    public void fromJson() throws ParseException {
        String json = "{post: " +
                "{login: 'testusr', " +
                "name: 'A Test User', " +
                "text: 'An history event', " +
                "timestamp: '2013-03-15 09:30:15'}" +
                "}";

        Post p = Post.fromJson(json);

        assertEquals("testusr", p.getLogin());
        assertEquals("A Test User", p.getName());
        assertEquals("An history event", p.getText());
        assertEquals(timestampParser.parse("2013-03-15 09:30:15"), p.getTimestamp());

    }
}
