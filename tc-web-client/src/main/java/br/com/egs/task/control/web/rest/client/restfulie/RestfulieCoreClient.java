package br.com.egs.task.control.web.rest.client.restfulie;

import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.Restfulie;
import br.com.egs.task.control.web.rest.client.JsonClient;
import br.com.egs.task.control.web.rest.client.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@RequestScoped
public class RestfulieCoreClient implements JsonClient {

    private static final Logger log = LoggerFactory.getLogger(RestfulieCoreClient.class);

	private static final String SLASH = "/";
	private static final String EMPTY = "";
	private static final String DEFAULT_VERSION = "v1";
	public static final String PARAM_SEPARATOR = "&";
	public static final String VALUE_SEPARATOR = ",";

    private static String CONFIGURATION_FILE = "/tc-web-client.properties";
    private static String rootUrl;

	private RestClient client;
	private String version;
	private String resource;
	private Map<String, String> params;

	@Inject
	public RestfulieCoreClient() {
		this(DEFAULT_VERSION);
	}

	public RestfulieCoreClient(String version) {
		this(Restfulie.custom(), trimSlash(version), EMPTY, new LinkedHashMap<String, String>());
	}

	private RestfulieCoreClient(RestClient client, String version, String resource, Map<String, String> params) {
		this.client = client;
        this.client.getMediaTypes().register(new JsonUnicode());

		this.version = version;
		this.resource = resource;
		this.params = params;
	}

	private static String trimSlash(String value) {
		if (value.startsWith(SLASH)) {
			value = value.substring(1);
		}
		if (value.endsWith(SLASH)) {
			value = value.substring(0, value.lastIndexOf("/"));
		}
		return value;
	}

	@Override
	public JsonClient at(String resource) {
		this.resource = trimSlash(resource);
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
		for (int index = 0; index < values.length; index++) {
			value.append(values[index]);
			value.append(VALUE_SEPARATOR);
		}
		value.deleteCharAt(value.lastIndexOf(VALUE_SEPARATOR));

		params.put(key, value.toString());
		return this;
	}

	@Override
	public String getUrl() {
        if (rootUrl == null) {
            loadRootUrlFromConfig();
        }

		StringBuilder url = new StringBuilder();
		url.append(rootUrl);
		url.append(version);
		url.append(SLASH);
		url.append(resource);

		if (!params.isEmpty()) {
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

    private void loadRootUrlFromConfig() {
        if(!CONFIGURATION_FILE.startsWith("/")){
            throw new IllegalArgumentException("The configurationFile param needs to begin with slash");
        }
        InputStream propFile = getClass().getResourceAsStream(CONFIGURATION_FILE);
        if (propFile == null) {
            throw new RuntimeException("Configuration file not found in classpath: " + CONFIGURATION_FILE);
        }

        Properties config = new Properties();
        try {
            config.load(propFile);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load configuration file", e);
        }

        rootUrl = config.getProperty("core.services.root-url");
    }

    @Override
	public Response getAsJson() {
        return new RestifulieResponse(client.at(getUrl()).accept(JsonUnicode.TYPE).get());
	}

	@Override
	public Response postAsJson(String body) {
        return new RestifulieResponse(client.at(getUrl()).as(JsonUnicode.TYPE).accept(JsonUnicode.TYPE).post(body.getBytes()));
    }

    @Override
    public Response putAsJson(String body) {

        return new RestifulieResponse(client.at(getUrl()).as(JsonUnicode.TYPE).accept(JsonUnicode.TYPE).put(body.getBytes()));
    }

}
