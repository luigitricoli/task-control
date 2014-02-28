package br.com.egs.task.control.web.rest.client.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
class FilterFormat {

	private Logger log = LoggerFactory.getLogger(FilterFormat.class);
	Map<String, String> filters;
	
	public FilterFormat() {
		filters = new HashMap<>();
		
		filters.put("OLM", "application");
		filters.put("EMA", "application");
		filters.put("GOL", "application");
		
		filters.put("doing", "status");
		filters.put("finished", "status");
		filters.put("late", "status");
		filters.put("waiting", "status");
		
		filters.put("CCC", "sources");
		filters.put("internal", "sources");
		filters.put("sup-prod", "sources");
	}
	
	public Map<String, String> formatParams(List<String> selected){
		Map<String, String> formatted = new HashMap<>();
		for(String filter : selected){
			String key = filters.get(filter);
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
	
}

