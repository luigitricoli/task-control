package br.com.egs.task.control.core.entities;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class UserTest {

    @Test
    public void generatePasswordHash() {
        User u = new User("user");
        u.setPasswordAsText("secret");
        assertEquals("335AA6A62C6C0C1C628AEF13A97E1E63BA3A1612", u.getPasswordHash());

        // Ensure that a different user with same password gets a different hash
        u = new User("user2");
        u.setPasswordAsText("secret");
        assertEquals("1299DCB1C274905CD58C37A1ABF17B080A06C6C5", u.getPasswordHash());
    }

    @Test
    public void checkPassword_failure() {
        User u = new User("user");
        u.setPasswordAsText("secret");

        boolean check = u.checkPassword("nonono");
        assertFalse(check);
    }

    @Test
    public void checkPassword_ok() {
        User u = new User("user");
        u.setPasswordAsText("secret");

        boolean check = u.checkPassword("secret");
        assertTrue(check);
    }
}
