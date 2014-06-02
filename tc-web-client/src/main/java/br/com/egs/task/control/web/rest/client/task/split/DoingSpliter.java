package br.com.egs.task.control.web.rest.client.task.split;

import br.com.egs.task.control.web.model.OneWeekTask;
import br.com.egs.task.control.web.model.Stage;
import br.com.egs.task.control.web.rest.client.task.CoreTask;
import br.com.egs.task.control.web.rest.client.task.TaskDate;

class DoingSpliter extends TaskSpliter {

    private TaskDate current;

    DoingSpliter(TaskDate referenceMonth,TaskDate current) {
        super(referenceMonth);
        this.current = current;
    }

    @Override
    protected void run(CoreTask coreTask, OneWeekTask.Builder task) throws Exception {
        task.as(Stage.DOING);

        if (isInSameMonth(current) && isInSameWeek(current)) {
            task.runUntil(current.getDayOfWeek());
            setKeepInNextWeek(false);
        } else if (keepInNextWeek()) {
            task.runUntil(task.LAST_UTIL_DAY_OF_WEEK);
            task.foreseenEndDay(task.LAST_UTIL_DAY_OF_WEEK);
        }
    }

}
