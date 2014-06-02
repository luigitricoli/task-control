package br.com.egs.task.control.web.rest.client.restfulie;

import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.client.DefaultLinkConverter;
import br.com.caelum.restfulie.mediatype.MediaType;
import br.com.caelum.restfulie.mediatype.XStreamHelper;
import br.com.caelum.restfulie.relation.CachedEnhancer;
import br.com.caelum.restfulie.relation.DefaultEnhancer;
import br.com.caelum.restfulie.relation.Enhancer;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class JsonUnicode implements MediaType {

    private static final Logger log = LoggerFactory.getLogger(JsonUnicode.class);

    public static final String TYPE = "application/json; charset=UTF-8";

    private final List<String> types = Arrays.asList("application/json; charset=utf-8", "application/json;charset=utf-8", "text/json;charset=utf-8", "text/json; charset=utf-8", "json; charset=utf-8", "json;charset=utf-8");

    private final XStreamHelper helper;

    private final XStream xstream;

    public JsonUnicode(Enhancer enhancer) {
        helper = new XStreamHelper(new JettisonMappedXmlDriver(), enhancer);
        this.xstream = helper.getXStream(getTypesToEnhance(), getCollectionNames());
    }

    public JsonUnicode() {
        this(new CachedEnhancer(new DefaultEnhancer()));
    }

    public boolean answersTo(String type) {
        return types.contains(type.toLowerCase());
    }

    public <T> void marshal(T payload, Writer writer, RestClient client) throws IOException {
        if(payload.getClass().equals(String.class)) {
            log.debug("String Payload: {}", payload);
            writer.append(String.class.cast(payload));
            return;
        }

        if(payload instanceof byte[]) {
            log.debug("ByteArray Payload: {}", payload);
            for(byte b : (byte[]) payload) {
                writer.write(b & 0xff);
            }
            return;
        }
        xstream.toXML(payload, writer);
        writer.flush();
    }

    public <T> T unmarshal(String content, RestClient client) {
        xstream.registerConverter(new DefaultLinkConverter(client));
        return (T) xstream.fromXML(content);
    }

    protected List<Class> getTypesToEnhance() {
        return Collections.emptyList();
    }
    protected List<String> getCollectionNames() {
        return Collections.emptyList();
    }
}
