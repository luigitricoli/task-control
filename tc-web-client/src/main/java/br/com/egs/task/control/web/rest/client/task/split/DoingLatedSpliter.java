package br.com.egs.task.control.web.rest.client.task.split;

import br.com.egs.task.control.web.model.OneWeekTask;
import br.com.egs.task.control.web.model.Stage;
import br.com.egs.task.control.web.rest.client.task.CoreTask;
import br.com.egs.task.control.web.rest.client.task.TaskDate;

import java.util.Calendar;

class DoingLatedSpliter extends TaskSpliter {

    private TaskDate current;

    DoingLatedSpliter(TaskDate referenceMonth, TaskDate current) {
        super(referenceMonth);
        this.current = current;
    }

    @Override
    protected void run(CoreTask coreTask, OneWeekTask.Builder task) throws Exception {
        task.as(Stage.LATE);

        if(isInSameMonth(coreTask.getForeseenEndDate())){
            if (isInSameWeek(coreTask.getForeseenEndDate())) {
                task.runUntil(coreTask.getForeseenEndDate().getDayOfWeek());
                setKeepInNextWeek(false);
            } else if (keepInNextWeek()){
                task.runUntil(task.LAST_UTIL_DAY_OF_WEEK);
            }
        }

        if (isInSameMonth(current) && isInSameWeek(current)) {
            task.foreseenEndDay(current.getDayOfWeek());
        } else {
            task.foreseenEndDay(task.LAST_UTIL_DAY_OF_WEEK);
            task.continueNextWeek();
        }
    }

    @Override
    protected Integer lastWeekIndex(CoreTask coreTask) {
        if (referenceDateMonth.toCalendar().get(Calendar.MONTH) < current.toCalendar().get(Calendar.MONTH)) {
            return 6;
        } else {
            return current.getWeekOfMonth();
        }
    }
}
