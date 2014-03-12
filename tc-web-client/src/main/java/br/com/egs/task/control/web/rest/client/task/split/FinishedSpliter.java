package br.com.egs.task.control.web.rest.client.task.split;

import br.com.egs.task.control.web.model.OneWeekTask;
import br.com.egs.task.control.web.model.Stage;
import br.com.egs.task.control.web.rest.client.task.CoreTask;
import br.com.egs.task.control.web.rest.client.task.TaskDate;

class FinishedSpliter extends TaskSpliter {

    protected FinishedSpliter(TaskDate referenceMonth) {
        super(referenceMonth);
    }

    @Override
    protected void run(CoreTask coreTask, OneWeekTask.Builder task) throws Exception {
        task.as(Stage.FINISHED);

        if (isInSameWeek(coreTask.getEndDate())) {
            if(wasFinishedLate(coreTask)){
                task.foreseenEndDay(coreTask.getEndDate().getDayOfWeek());
            }

            task.runUntil(coreTask.getEndDate().getDayOfWeek());
            setKeepInNextWeek(false);
        } else if (keepInNextWeek()) {
            task.runUntil(task.LAST_UTIL_DAY_OF_WEEK);
            task.foreseenEndDay(task.LAST_UTIL_DAY_OF_WEEK);
        }
    }

    @Override
    protected Integer lastWeekIndex(CoreTask coreTask) {
        return wasFinishedLate(coreTask) ? coreTask.getEndDate().getWeekOfMonth() : coreTask.getForeseenEndDate().getWeekOfMonth();
    }
}
