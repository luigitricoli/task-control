package br.com.egs.task.control.web.model.filter;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.servlet.ServletContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class Sources implements Serializable{

    private static final long serialVersionUID = 5237469042102689591L;

    private Map<String, String> labels;

    public void whenApplicationStarts(@Observes ServletContext context) {
        labels = new HashMap<>();

        labels.put("ccc", "CCC");
        labels.put("interna", "Interna");
        labels.put("sup_prod", "Sup. Produção");
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
}
