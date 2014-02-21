package br.com.egs.task.control.web.rest.client.restfulie;

import java.util.LinkedHashMap;
import java.util.Map;

import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.Restfulie;
import br.com.caelum.vraptor.ioc.Component;
import br.com.egs.task.control.web.rest.client.JsonClient;

@Component
public class RestfulieCoreClient implements JsonClient {

    private static final String SLASH = "/";
    private static final String EMPTY = "";
    //TODO extract this to a external property
    private static final String ROOT_URL = "http://localhost:8080/task-control-core/";
    private static final String DEFAULT_VERSION = "v1";
    public static final String JSON_MEDIA_TYPE = "application/json";
    public static final String PARAM_SEPARATOR = "&";
    public static final String VALUE_SEPARATOR = ",";
    private RestClient client;
    private String version;
    private String resource;
    private Map<String, String> params;

    public RestfulieCoreClient(String version) {
        this.version = trimSlash(version);
        this.params = new LinkedHashMap<>();
        this.resource = EMPTY;
        this.client = Restfulie.custom();
    }

    private String trimSlash(String value){
        if(value.startsWith(SLASH)){
            value = value.substring(1);
        }
        if(value.endsWith(SLASH)){
            value = value.substring(0, value.lastIndexOf("/"));
        }
        return value;
    }

    public RestfulieCoreClient() {
        this(DEFAULT_VERSION);
    }

    @Override
    public JsonClient at(String resource) {
        this.resource = trimSlash(resource);;
        return this;
    }

    @Override
    public JsonClient addUrlParam(String key, String value) {
        params.put(key, value);
        return this;
    }

    @Override
    public JsonClient addUrlParam(String key, String... values) {
        StringBuilder value = new StringBuilder();
        for(int index = 0; index < values.length; index++){
            value.append(values[index]);
            value.append(VALUE_SEPARATOR);
        }
        value.deleteCharAt(value.lastIndexOf(VALUE_SEPARATOR));

        params.put(key, value.toString());
        return this;
    }

    @Override
    public String getUrl() {
        StringBuilder url = new StringBuilder();
        url.append(ROOT_URL);
        url.append(version);
        url.append(SLASH);
        url.append(resource);

        if(!params.isEmpty()){
            url.append("?");
            for (Map.Entry<String, String> param : params.entrySet()) {
                url.append(param.getKey());
                url.append("=");
                url.append(param.getValue());
                url.append(PARAM_SEPARATOR);
            }
            url.deleteCharAt(url.lastIndexOf(PARAM_SEPARATOR));
        }

        return url.toString();
    }

    @Override
    public String getAsJson(){
       return client.at(getUrl()).accept(JSON_MEDIA_TYPE).get().getContent();
    }

    @Override
    public String postAsJson(String body){
        return client.at(getUrl()).accept(JSON_MEDIA_TYPE).post(body).getContent();
    }


}
