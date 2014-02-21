package br.com.egs.task.control.web.rest.client.restfulie;

import br.com.egs.task.control.web.rest.client.JsonClient;
import junit.framework.Assert;
import org.junit.Test;

public class RestfulieCoreClientTest {

    @Test
    public void getBasicUrl(){
        String url = "http://localhost:8080/task-control-core/v1/";

        JsonClient client = new RestfulieCoreClient();
        Assert.assertEquals(url, client.getUrl());
    }

    @Test
    public void getUrlNoDefaultVersion(){
        String url = "http://localhost:8080/task-control-core/v2/";

        JsonClient client = new RestfulieCoreClient("v2");
        Assert.assertEquals(url, client.getUrl());
    }

    @Test
    public void versionParamWithSlash(){
        String url = "http://localhost:8080/task-control-core/v3/";
        Assert.assertEquals(url, new RestfulieCoreClient("v3/").getUrl());
        Assert.assertEquals(url, new RestfulieCoreClient("/v3").getUrl());
        Assert.assertEquals(url, new RestfulieCoreClient("/v3/").getUrl());
    }

    @Test
    public void at(){
        String url = "http://localhost:8080/task-control-core/v1/tasks";

        JsonClient client = new RestfulieCoreClient().at("tasks");
        Assert.assertEquals(url, client.getUrl());
    };

    @Test
    public void resourceParamWithSlash(){
        String url = "http://localhost:8080/task-control-core/v1/tasks/1/posts";
        JsonClient client = new RestfulieCoreClient();

        Assert.assertEquals(url, client.at("tasks/1/posts/").getUrl());
        Assert.assertEquals(url, client.at("/tasks/1/posts").getUrl());
        Assert.assertEquals(url, client.at("/tasks/1/posts/").getUrl());
    }

    @Test
    public void addOneUrlParam(){
        String url = "http://localhost:8080/task-control-core/v1/tasks?year=2014";

        JsonClient client = new RestfulieCoreClient().at("tasks").addUrlParam("year", "2014");
        Assert.assertEquals(url, client.getUrl());
    };

    @Test
    public void addOneUrlParamWithMultipleValues(){
        String url = "http://localhost:8080/task-control-core/v1/tasks?owners=foo,bar";

        JsonClient client = new RestfulieCoreClient().at("tasks").addUrlParam("owners", "foo", "bar");
        Assert.assertEquals(url, client.getUrl());
    };

    @Test
    public void addMulipleUrlParam(){
        String url = "http://localhost:8080/task-control-core/v1/tasks?year=2014&month=1";

        JsonClient client = new RestfulieCoreClient().at("tasks").addUrlParam("year", "2014").addUrlParam("month", "1");
        Assert.assertEquals(url, client.getUrl());
    };

    //TODO
    @Test
    public void getAsJson(){};

    @Test
    public void postAsJson(){};

    //TODO
    @Test
    public void updateAsJson(){};

    //TODO
    @Test
    public void deleteAsJson(){};
}
