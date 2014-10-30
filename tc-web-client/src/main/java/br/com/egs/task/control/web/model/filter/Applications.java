package br.com.egs.task.control.web.model.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.servlet.ServletContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@ApplicationScoped
public class Applications implements Serializable{

    private static final Logger log = LoggerFactory.getLogger(Applications.class);

    public static final String GERAL = "geral";
    public static final String ADMINISTRATIVO = "administrativo";
    public static final String EMA = "ema";
    public static final String EMM = "emm";
    public static final String ESDP = "esdp";
    public static final String GOL = "gol";
    public static final String OLM = "olm";
    public static final String ROAMING = "roaming";
    public static final String TASK_CONTROL = "task_control";
    public static final String SLA = "sla";
    public static final String BILL_PRESENTMENT = "bill_presentment";
    public static final String PORTAL = "portal";

    private Map<String, String> labels;
    private String selected;

    public Applications(){
        whenApplicationStarts(null);
    }

    public void whenApplicationStarts(@Observes ServletContext context) {

        labels = new TreeMap<>();
        labels.put(GERAL, "Geral");
        labels.put(ADMINISTRATIVO, "Administrativo");
        labels.put(EMA, "EMA");
        labels.put(EMM, "EMM");
        labels.put(ESDP, "ESDP");
        labels.put(GOL, "GOL");
        labels.put(OLM, "OLM");
        labels.put(ROAMING, "Roaming");
        labels.put(TASK_CONTROL, "TaskControl");
        labels.put(SLA, "SLA");
        labels.put(PORTAL, "Portal");
        labels.put(BILL_PRESENTMENT, "Bill Presentment");

        selected = GERAL;
    }

    public String getLabel(String key){
        return labels.get(key);
    }

    public boolean contains(String key){
        return labels.containsKey(key);
    }

    public List<String> getValues(){
        return new ArrayList<>(labels.keySet());
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
