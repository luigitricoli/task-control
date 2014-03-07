package br.com.egs.task.control.web.rest.client.task.split;

import br.com.egs.task.control.web.model.OneWeekTask;
import br.com.egs.task.control.web.model.Stage;
import br.com.egs.task.control.web.rest.client.task.CoreTask;
import br.com.egs.task.control.web.rest.client.task.TaskDate;

class DoingLatedSpliter extends TaskSpliter {

    private TaskDate current;

    DoingLatedSpliter(TaskDate current) {
        this.current = current;
    }

    @Override
    protected void run(CoreTask coreTask, OneWeekTask.Builder task) throws Exception {
        task.as(Stage.LATE);

        if (isInSameWeek(current)) {
            task.foreseenEndDay(current.getDayOfWeek());
            task.runUntil(coreTask.getForeseenEndDate().getDayOfWeek());
        } else {
            task.runAtTheEnd();
        }



    }
}
