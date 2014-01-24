package br.com.egs.task.control.web.rest.client;

import br.com.caelum.vraptor.ioc.Component;
import br.com.egs.task.control.web.model.Hashtag;
import br.com.egs.task.control.web.model.Post;
import br.com.egs.task.control.web.model.Task;
import br.com.egs.task.control.web.model.Week;
import br.com.egs.task.control.web.model.repository.TaskRepository;
import br.com.egs.task.control.web.model.stage.Doing;
import br.com.egs.task.control.web.model.stage.Finished;
import br.com.egs.task.control.web.model.stage.Late;
import br.com.egs.task.control.web.model.stage.Stage;

import java.util.LinkedList;
import java.util.List;

@Component
public class TaskClient implements TaskRepository {

    @Override
    public List<Week> weekByMonth(Integer month) {
        List<Week> weeks = new LinkedList<>();

        List<Task> tasks = new LinkedList<>();

        tasks.add(new Task.Builder(2, 4, "SR555 - Update.").as(Stage.FINISHED).daysRun(2).addHashtag(1, Hashtag.LATE).addHashtag(1, Hashtag.OVERTIME).build());
        tasks.add(new Task.Builder(2, 2, "SR555 - Update.").as(Stage.DOING).daysRun(2).addHashtag(2, Hashtag.OVERTIME).build());
        tasks.add(new Task.Builder(2, 1, "SR555 - Update.").as(Stage.LATE).daysRun(2).addHashtag(2, Hashtag.LATE).build());

        weeks.add(new Week(tasks));
        weeks.add(new Week(tasks));
        weeks.add(new Week(tasks));
        weeks.add(new Week(tasks));
        weeks.add(new Week(tasks));
        weeks.add(new Week(tasks));

        return weeks;
    }

    @Override
    public List<Post> postBy(Integer id) {
        return null;
    }

}
