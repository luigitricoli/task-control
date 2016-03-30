package br.com.egs.task.control.web.rest.client.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.egs.task.control.web.model.filter.Applications;
import br.com.egs.task.control.web.model.filter.Sources;
import br.com.egs.task.control.web.model.filter.Status;

public class FilterFormat {

	private Logger log = LoggerFactory.getLogger(FilterFormat.class);
	
	@Inject
	private Sources sources;
	
	@Inject
	private Applications applications;

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
        if(applications.contains(filter)){
            return "application";
        }
        try{
            Status.valueOf(filter);
            return "status";
        } catch(IllegalArgumentException e){
        	log.error(e.getMessage());
        }
        
        if(sources.contains(filter)){
            return "sources";
        }
        return null;
    }

}

