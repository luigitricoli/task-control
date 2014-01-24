package br.com.egs.task.control.core.entities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Representation of a Task Control user.
 */
public class User {
    private String login;
    private String name;
    private String email;
    private String passwordHash;

    private List<Application> applications;

    public User(String login) {
        if (login == null) {
            throw new IllegalArgumentException("Cannot create a user with null login");
        }
        this.login = login;
    }

    public void setPasswordAsText(String pass) {
        pass = login + pass;
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hashedBytes = digest.digest(pass.getBytes());
        String hashedString = bytesToHexString(hashedBytes);
        this.passwordHash = hashedString;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public void setPasswordHash(String hash) {
        this.passwordHash = hash;
    }

    /**
     *
     * @param bytes
     * @return
     */
    private String bytesToHexString(byte[] bytes) {
        final String hexDigits = "0123456789ABCDEF";

        StringBuilder converted = new StringBuilder();
        for (byte b : bytes) {
            converted.append(hexDigits.charAt((b & 0xF0) >> 4))
                    .append(hexDigits.charAt((b & 0x0F)));
        }
        return converted.toString();
    }

}