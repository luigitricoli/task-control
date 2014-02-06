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

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

@Component
public class TaskClient implements TaskRepository {

    @Override
    public List<Week> weekByMonth(Integer month) {
        List<Week> weeks = new LinkedList<>();

        List<Task> tasks = new LinkedList<>();

        tasks.add(new Task.Builder("12ac34ac56ac78ac90fc12", 2, 4, "SR555 - Update.").as(Stage.FINISHED).daysRun(2).addHashtag(1, Hashtag.LATE).addHashtag(1, Hashtag.OVERTIME).build());
        tasks.add(new Task.Builder("12ac34ac56ac78ac90be12", 2, 2, "SR333 - Change.").as(Stage.DOING).daysRun(2).addHashtag(2, Hashtag.OVERTIME).build());
        tasks.add(new Task.Builder("12ac34ac56ac78ac90fa12", 2, 1, "SR444 - Create.").as(Stage.LATE).daysRun(2).addHashtag(2, Hashtag.LATE).build());

        weeks.add(new Week(tasks));
        weeks.add(new Week(tasks));
        weeks.add(new Week(tasks));
        weeks.add(new Week(tasks));
        weeks.add(new Week(tasks));
        weeks.add(new Week(tasks));

        return weeks;
    }

    @Override
    public List<Post> postBy(String id) {
        List<Post> posts = new LinkedList<>();

        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DAY_OF_MONTH, -5);
        posts.add(new Post(c1, "Luigi", "Beef ribs chicken tail boudin pork chop filet mignon #hashtag kevin chuck."));

        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.DAY_OF_MONTH, -4);
        posts.add(new Post(c2, "Luigi", "Beef ribs chicken #horaextra tail boudin pork chop filet #hashtag mignon kevin chuck."));

        Calendar c3 = Calendar.getInstance();
        c3.add(Calendar.DAY_OF_MONTH, -3);
        posts.add(new Post(c3, "Luigi", "Beef ribs chicken #hashtag tail boudin pork chop filet mignon kevin chuck."));

        Calendar c4 = Calendar.getInstance();
        c4.add(Calendar.DAY_OF_MONTH, -2);
        posts.add(new Post(c4, "Luigi", "Beef ribs chicken tail boudin pork chop filet mignon kevin chuck. #atraso #hashtag"));

        return posts;
    }

}
