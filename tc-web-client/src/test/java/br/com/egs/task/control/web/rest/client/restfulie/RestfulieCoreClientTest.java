package br.com.egs.task.control.web.rest.client.restfulie;

import br.com.egs.task.control.web.rest.client.JsonClient;
import br.com.egs.task.control.web.rest.client.Response;
import junit.framework.Assert;
import org.junit.Test;

import static junit.framework.Assert.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class RestfulieCoreClientTest {

    @Test
    public void getBasicUrl(){
        String url = "http://localhost:8080/task-control-core/v1/";

        JsonClient client = new RestfulieCoreClient();
        assertEquals(url, client.getUrl());
    }

    @Test
    public void getUrlNoDefaultVersion(){
        String url = "http://localhost:8080/task-control-core/v2/";

        JsonClient client = new RestfulieCoreClient("v2");
        assertEquals(url, client.getUrl());
    }

    @Test
    public void versionParamWithSlash(){
        String url = "http://localhost:8080/task-control-core/v3/";
        assertEquals(url, new RestfulieCoreClient("v3/").getUrl());
        assertEquals(url, new RestfulieCoreClient("/v3").getUrl());
        assertEquals(url, new RestfulieCoreClient("/v3/").getUrl());
    }

    @Test
    public void at(){
        String url = "http://localhost:8080/task-control-core/v1/tasks";

        JsonClient client = new RestfulieCoreClient().at("tasks");
        assertEquals(url, client.getUrl());
    };

    @Test
    public void resourceParamWithSlash(){
        String url = "http://localhost:8080/task-control-core/v1/tasks/1/posts";
        JsonClient client = new RestfulieCoreClient();

        assertEquals(url, client.at("tasks/1/posts/").getUrl());
        assertEquals(url, client.at("/tasks/1/posts").getUrl());
        assertEquals(url, client.at("/tasks/1/posts/").getUrl());
    }

    @Test
    public void addOneUrlParam(){
        String url = "http://localhost:8080/task-control-core/v1/tasks?year=2014";

        JsonClient client = new RestfulieCoreClient().at("tasks").addUrlParam("year", "2014");
        assertEquals(url, client.getUrl());
    };

    @Test
    public void addOneUrlParamWithMultipleValues(){
        String url = "http://localhost:8080/task-control-core/v1/tasks?owners=foo,bar";

        JsonClient client = new RestfulieCoreClient().at("tasks").addUrlParam("owners", "foo", "bar");
        assertEquals(url, client.getUrl());
    };

    @Test
    public void addMulipleUrlParam(){
        String url = "http://localhost:8080/task-control-core/v1/tasks?year=2014&month=1";

        JsonClient client = new RestfulieCoreClient().at("tasks").addUrlParam("year", "2014").addUrlParam("month", "1");
        assertEquals(url, client.getUrl());
    };

    //TODO
    @Test
    public void getAsJson(){};

    //@Test
    public void postAsJson(){
        Response response = new RestfulieCoreClient().at("tasks").postAsJson("{  \"description\": \"SR 222233333 Alteração de diretório\",  \"startDate\": \"2014-04-29\",  \"foreseenEndDate\": \"2014-04-29\",  \"source\": \"CCC\",  \"application\": \"EMM\",  \"owners\": [    {      \"login\": \"john\"    }  ]}");
        assertThat(response.getCode(), is(200));
        assertTrue(response.getContent().contains("Alteração de diretório"));
    };

    //TODO
    @Test
    public void updateAsJson(){};

    //TODO
    @Test
    public void deleteAsJson(){};
}
