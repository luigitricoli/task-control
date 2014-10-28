package br.com.egs.task.control.web.rest.client.restfulie;

import br.com.egs.task.control.web.rest.client.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestifulieResponse implements Response {

    public static final int SUCCESS_CODE = 200;
    public static final int SUCCESS_CODE_NO_CONTENT = 204;

    private br.com.caelum.restfulie.Response resp;


    public RestifulieResponse(br.com.caelum.restfulie.Response resp) {
        this.resp = resp;
    }

    @Override
    public boolean isSuccess() {
        return (resp.getCode() == SUCCESS_CODE) || (resp.getCode() == SUCCESS_CODE_NO_CONTENT);
    }

    @Override
    public Integer getCode() {
        return resp.getCode();
    }

    @Override
    public String getContent() {
        return resp.getContent();
    }

    @Override
    public List<String> getHeader(String s) {
        return new ArrayList<>(resp.getHeader(s));
    }

    @Override
    public Map<String, String> getHeaders() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String getStatusLine() {
        return resp.getStatusLine();
    }
}
