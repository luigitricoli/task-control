package br.com.egs.task.control.web.rest.client;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import br.com.caelum.vraptor.ioc.Component;
import br.com.egs.task.control.web.model.Hashtag;
import br.com.egs.task.control.web.model.HashtagDay;
import br.com.egs.task.control.web.model.Stage;
import br.com.egs.task.control.web.model.Task;
import br.com.egs.task.control.web.model.Week;
import br.com.egs.task.control.web.model.repository.TaskRepository;

@Component
public class TaskClient implements TaskRepository {

	@Override
	public List<Task> byMonth() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Week> weekByMonth() {
		List<Week> weeks = new LinkedList<>();
		
		List<Task> tasks = new LinkedList<>();
		
		Set<Hashtag> tags = new HashSet<Hashtag>(Arrays.asList(new Hashtag[] { Hashtag.late, Hashtag.overtime }));
		Set<HashtagDay> hashtagDays = new HashSet<HashtagDay>();
		hashtagDays.add(new HashtagDay(1, tags));
		
		tasks.add(new Task(6, 2, Stage.finished, "SR555 - Update.", hashtagDays));
		tasks.add(new Task(6, 2, Stage.finished, "SR555 - Update.", hashtagDays));
		
		weeks.add(new Week(1, tasks));
		weeks.add(new Week(2, tasks));
		weeks.add(new Week(3, tasks));
		weeks.add(new Week(4, tasks));
		weeks.add(new Week(5, tasks));
		weeks.add(new Week(6, tasks));
		
		return weeks;
	}

}
