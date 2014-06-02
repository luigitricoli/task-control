package br.com.egs.task.control.web.rest.client.user;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class CoreUser {

    private String name;
    private String login;
    private String email;
    private List<String> applications;

    public CoreUser(String login) {
        this.login = login;
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

    public List<String> getApplications() {
        return applications;
    }


    private static Gson unmarshaller() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(new TypeToken<List<String>>() {
        }.getType(), new ApplicationsUnmarshaller());
        return gson.create();
    }

    public static CoreUser unmarshal(String json) {
        return unmarshaller().fromJson(json, CoreUser.class);
    }

    public static List<CoreUser> unmarshalList(String json) {
        return unmarshaller().fromJson(json, new TypeToken<List<CoreUser>>() {
        }.getType());
    }

    private static class ApplicationsUnmarshaller implements JsonDeserializer<List<String>> {
        @Override
        public List<String> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            List<String> values = new LinkedList<>();
            for (JsonElement element : jsonElement.getAsJsonArray()) {
                values.add(element.getAsJsonObject().get("name").getAsString());
            }
            return values;
        }
    }
}
