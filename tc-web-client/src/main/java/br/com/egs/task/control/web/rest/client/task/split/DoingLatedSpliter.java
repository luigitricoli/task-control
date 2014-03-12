package br.com.egs.task.control.web.rest.client.task.split;

import br.com.egs.task.control.web.model.OneWeekTask;
import br.com.egs.task.control.web.model.Stage;
import br.com.egs.task.control.web.rest.client.task.CoreTask;
import br.com.egs.task.control.web.rest.client.task.TaskDate;

class DoingLatedSpliter extends TaskSpliter {

    private TaskDate current;

    DoingLatedSpliter(TaskDate referenceMonth, TaskDate current) {
        super(referenceMonth);
        this.current = current;
    }

    @Override
    protected void run(CoreTask coreTask, OneWeekTask.Builder task) throws Exception {
        task.as(Stage.LATE);

        if (isInSameWeek(coreTask.getForeseenEndDate())){
            task.runUntil(coreTask.getForeseenEndDate().getDayOfWeek());
            setKeepInNextWeek(false);
        } else if (keepInNextWeek()) {
            task.runUntil(task.LAST_UTIL_DAY_OF_WEEK);
        }

        if (isInSameWeek(current)) {
            task.foreseenEndDay(current.getDayOfWeek());
            setKeepInNextWeek(false);
        } else {
            task.foreseenEndDay(task.LAST_UTIL_DAY_OF_WEEK);
        }
    }

    @Override
    protected Integer lastWeekIndex(CoreTask coreTask) {
        return current.getWeekOfMonth();
    }
}
