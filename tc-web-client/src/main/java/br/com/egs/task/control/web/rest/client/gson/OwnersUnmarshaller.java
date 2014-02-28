package br.com.egs.task.control.web.rest.client.gson;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class OwnersUnmarshaller implements JsonDeserializer<List<String>> {

    @Override
    public List<String> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        List<String> values = new LinkedList<>();
        for (JsonElement element : jsonElement.getAsJsonArray()) {
            values.add(element.getAsJsonObject().get("login").getAsString());
        }
        return values;
    }
}
