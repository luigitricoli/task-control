package br.com.egs.task.control.web.rest.client.task.split;

import br.com.egs.task.control.web.model.Hashtag;
import br.com.egs.task.control.web.model.OneWeekTask;
import br.com.egs.task.control.web.model.Stage;
import br.com.egs.task.control.web.rest.client.task.CorePost;
import br.com.egs.task.control.web.rest.client.task.CoreTask;
import br.com.egs.task.control.web.rest.client.task.TaskDate;

import java.util.Calendar;

class FinishedSpliter extends TaskSpliter {

    protected FinishedSpliter(TaskDate referenceMonth) {
        super(referenceMonth);
    }

    @Override
    protected void run(CoreTask coreTask, OneWeekTask.Builder task) throws Exception {
        task.as(Stage.FINISHED);

        if (isInSameMonth(coreTask.getEndDate()) && isInSameWeek(coreTask.getEndDate())) {
            if(wasFinishedLate(coreTask)){
                task.foreseenEndDay(coreTask.getEndDate().getDayOfWeek());
            }

            task.runUntil(coreTask.getEndDate().getDayOfWeek());
            setKeepInNextWeek(false);
        } else if (keepInNextWeek()) {
            task.runUntil(task.LAST_UTIL_DAY_OF_WEEK);
            task.foreseenEndDay(task.LAST_UTIL_DAY_OF_WEEK);
            task.continueNextWeek();
        }

    }

    @Override
    protected Integer lastWeekIndex(CoreTask coreTask) {
        if (wasFinishedLate(coreTask)) {
            if (referenceDateMonth.toCalendar().get(Calendar.MONTH) < coreTask.getEndDate().toCalendar().get(Calendar.MONTH)) {
                return 6;
            } else {
                return coreTask.getEndDate().getWeekOfMonth();
            }
        } else {
            return super.lastWeekIndex(coreTask);
        }
    }
}
