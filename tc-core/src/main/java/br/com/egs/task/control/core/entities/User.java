package br.com.egs.task.control.core.entities;

import br.com.egs.task.control.core.exception.ValidationException;
import com.google.gson.*;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a Task Control user.
 */
public class User {

    private String login;
    private String name;
    private String email;
    private String type;
    String passwordHash;

    private List<Application> applications;

    public User(String login) {
        if (login == null) {
            throw new IllegalArgumentException("Cannot create a user with null login");
        }
        this.login = login;
    }

    /** Sets the password as clear text. It is converted and stored as hash */
    public void setPassword(String pass) {
        String hashedString = extractHash(pass);
        this.passwordHash = hashedString;
    }

    public void validate() throws ValidationException {
        if (StringUtils.isBlank(this.getLogin())) {
            throw new ValidationException("Login is empty");
        }
        if (StringUtils.isBlank(this.getName())) {
            throw new ValidationException("Name is empty");
        }
        if (StringUtils.isBlank(this.getEmail())) {
            throw new ValidationException("E-mail is empty");
        }
        if (StringUtils.isBlank(this.getType())) {
            throw new ValidationException("User type is empty");
        }
        if (StringUtils.isBlank(this.getPasswordHash())) {
            throw new ValidationException("Password is empty");
        }
        if (applications == null || applications.size() == 0) {
            throw new ValidationException("Application list is empty");
        }
    }

    /**
     * Verifies if the given password matches this users' defined password.
     * @param pass
     * @return
     */
    public boolean checkPassword(String pass) {
        String hash = extractHash(pass);
        return hash.equals(this.passwordHash);
    }

    /**
     * Convert the user to a JSON representation, removing the password-related attributes
     * @return
     */
    public String toJson() {
        return new GsonBuilder()
                .registerTypeAdapter(this.getClass(), new UserSerializer(true))
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }

    /**
     * Converts to the MongoDB Object representation
     *
     * @return
     */
    public BasicDBObject toDbObject() {
        BasicDBObject result = new BasicDBObject();
        result.put("_id", this.getLogin());
        result.put("name", this.getName());
        result.put("email", this.getEmail());
        result.put("type", this.getType());
        result.put("passwordHash", this.getPasswordHash());

        BasicDBList resultApplications = new BasicDBList();
        for (Application app : this.getApplications()) {
            resultApplications.add(new BasicDBObject()
                    .append("name", app.getName())
            );
        }

        result.put("applications", resultApplications);

        return result;
    }

    /**
     * Converts a MongoDB object representation to a User instance
     *
     * @param dbUser
     * @return
     */
    public static User fromDbObject(BasicDBObject dbUser) {
        String login = dbUser.getString("_id");

        User u = new User(login);
        u.setName(dbUser.getString("name"));
        u.setEmail(dbUser.getString("email"));
        u.setType(dbUser.getString("type"));
        u.passwordHash = dbUser.getString("passwordHash");

        @SuppressWarnings("unchecked")
        List<BasicDBObject> dbApplications = (List<BasicDBObject>) dbUser.get("applications");

        List<Application> applications = new ArrayList<Application>();
        for (BasicDBObject dbApp : dbApplications) {
            applications.add(new Application(dbApp.getString("name")));
        }
        u.setApplications(applications);

        return u;
    }

    public void copyPasswordFrom(User other) {
        this.passwordHash = other.passwordHash;
    }

    /**
     *
     * @param json
     * @return
     */
    public static User fromJson(String json) throws JsonSyntaxException {
        return new GsonBuilder()
                .registerTypeAdapter(User.class, new UserDeserializer())
                .create()
                .fromJson(json, User.class);
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String extractHash(String pass) {
        pass = login + pass;
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hashedBytes = digest.digest(pass.getBytes());
        return bytesToHexString(hashedBytes);
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

    /**
     * A custom JSON serializer for a User instance, that can (optionally) exclude the
     * password attribute from the resulting object.
     */
    public static class UserSerializer implements JsonSerializer<User> {
        private boolean excludePassword;

        public UserSerializer(boolean excludePassword) {
            this.excludePassword = excludePassword;
        }

        @Override
        public JsonElement serialize(User user, Type type, JsonSerializationContext jsonSerializationContext) {
            Gson gson = new Gson();
            JsonObject jObj = (JsonObject)gson.toJsonTree(user);
            if (excludePassword) {
                jObj.remove("passwordHash");
            }
            return jObj;
        }
    }

    private static class UserDeserializer implements JsonDeserializer<User> {
        @Override
        public User deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            // Performs the default parsing, and then add the Password
            Gson gson = new Gson();
            User user = gson.fromJson(jsonElement, User.class);

            if (((JsonObject)jsonElement).has("password")) {
                String passwordText = ((JsonObject)jsonElement).get("password").getAsString();
                user.setPassword(passwordText);
            }

            return user;
        }
    }
}