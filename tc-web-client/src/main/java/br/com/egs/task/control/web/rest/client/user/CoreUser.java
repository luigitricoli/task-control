package br.com.egs.task.control.web.rest.client.user;

import br.com.egs.task.control.web.rest.client.task.TaskDate;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class CoreUser {

    private String name;
    private String login;
    private String email;
    private String type;
    private String password;
    private List<String> applications;

    public CoreUser(String login) {
        this.login = login;
    }

    public CoreUser(String login, String name, String email, String type, String pass, List <String> applications) {
        this.login = login;
        this.name = name;
        this.email = email;
        this.type = type;
        this.password = pass;
        this.applications = applications;
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
    
    public String getType() {
        return type;
    }
    
    public String getPassword() {
        return password;
    }
    
    public List<String> getApplications() {
        return applications;
    }

    public String toJson(){
    	GsonBuilder gson = new GsonBuilder();
    	gson.registerTypeAdapter(new TypeToken<List<String>>() {}.getType(), new ApplicationsMarshaller());
    	
        Gson marhaller = gson.create();
        return marhaller.toJson(this);
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
    
    private static class ApplicationsMarshaller implements JsonSerializer<List<String>> {
		@Override
		public JsonElement serialize(List<String> src, Type typeOfSrc, JsonSerializationContext context) {
			JsonArray array = new JsonArray();
			
			for (String application : src) {
				JsonObject appJson = new JsonObject();
				appJson.add("name", new JsonPrimitive(application));
				array.add(appJson);
			}
			
			return array;
		}   	
    }
    
    
}
