package br.com.egs.task.control.web.rest.client.task;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.egs.task.control.web.rest.client.task.filter.Applications;
import br.com.egs.task.control.web.rest.client.task.filter.Sources;
import br.com.egs.task.control.web.rest.client.task.filter.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@ApplicationScoped
public class FilterFormat {

	private Logger log = LoggerFactory.getLogger(FilterFormat.class);
	
	public FilterFormat() {
	}
	
	public Map<String, String> formatParams(List<String> selected){
		Map<String, String> formatted = new HashMap<>();
		for(String filter : selected){
            filter = filter.toLowerCase();

            String key = getFilterKey(filter);
            if(key == null){
				log.error("There is no mapped filter for {}", filter);
				continue;
			}

			if(formatted.get(key) == null){
				formatted.put(key, filter);	
			} else {
				String value = formatted.get(key);
				formatted.put(key, value.concat(",").concat(filter));
			}
		}
		return formatted;
	}

    public String getFilterKey(String filter){
        try{
            Applications.valueOf(filter);
            return "application";
        } catch(IllegalArgumentException e){}
        try{
            Status.valueOf(filter);
            return "status";
        } catch(IllegalArgumentException e){}
        try{
            Sources.valueOf(filter);
            return "sources";
        } catch(IllegalArgumentException e){}
        return null;
    }

}

