package br.com.egs.task.control.web.rest.client;

public interface JsonClient {

    public JsonClient at(String resource);

    public JsonClient addUrlParam(String key, String value);

    public JsonClient addUrlParam(String key, String... values);

    public String getUrl();

    public String getAsJson();

    public String postAsJson(String body);

}
