package br.com.egs.task.control.web.rest.client.task.split;

import br.com.egs.task.control.web.rest.client.task.CoreTask;
import br.com.egs.task.control.web.rest.client.task.TaskDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

public class TaskSpliterFactory {

    private static final Logger log = LoggerFactory.getLogger(TaskSpliterFactory.class);
    private static final String DATE_FORMAT = "2014-%s-01";

    public static TaskSpliter getInstance(final CoreTask coreTask, final Calendar referenceMonth) {
        TaskDate today = new TaskDate();
        TaskDate month = getReferenceMonth(referenceMonth);
        if (isFinished(coreTask)) {
            return new FinishedSpliter(month);
        } else if (isBeyondTheForeseen(coreTask, today)) {
            return new DoingLatedSpliter(month, today);
        } else if (isStarted(coreTask, today)) {
            return new DoingSpliter(month, today);
        }
        return new WaitingSpliter(month);
    }

    private static TaskDate getReferenceMonth(Calendar date) {
        Calendar tmp = (Calendar) date.clone();
        tmp.set(Calendar.DAY_OF_MONTH, 1);
        return new TaskDate(tmp);
    }

    private static boolean isStarted(CoreTask coreTask, TaskDate today) {
        return today.compareTo(coreTask.getStartDate()) >= 0;
    }

    private static boolean isBeyondTheForeseen(CoreTask coreTask, TaskDate today) {
        return today.compareTo(coreTask.getForeseenEndDate()) > 0;
    }

    private static boolean isFinished(CoreTask coreTask) {
        return coreTask.getEndDate() != null;
    }

}
