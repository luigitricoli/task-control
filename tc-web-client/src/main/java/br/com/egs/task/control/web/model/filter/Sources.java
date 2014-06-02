package br.com.egs.task.control.web.model.filter;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@SessionScoped
public class Sources {

    private Map<String, String> labels;

    public Sources() {
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
