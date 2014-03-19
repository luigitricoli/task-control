package br.com.egs.task.control.web.rest.client;

import java.util.List;
import java.util.Map;

public interface Response {

        Integer getCode();

        String getContent();

        List<String> getHeader(String s);

        Map<String, String> getHeaders();

        String getStatusLine();
}
