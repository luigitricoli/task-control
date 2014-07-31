package br.com.egs.task.control.web.model.filter;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.*;

@SessionScoped
public class Applications implements Serializable{

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

    public Applications() {
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
