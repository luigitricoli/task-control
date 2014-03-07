package br.com.egs.task.control.web.rest.client.task.split;

import br.com.egs.task.control.web.rest.client.task.CoreTask;
import br.com.egs.task.control.web.rest.client.task.TaskDate;

public class TaskSpliterFactory {

    public static TaskSpliter getInstance(final CoreTask coreTask){
        TaskDate today = new TaskDate();
        if (isFinished(coreTask)) {
            return new FinishedSpliter();
        } else if (isBeyondTheForeseen(coreTask, today)) {
            return new DoingLatedSpliter(today);
        } else if (isStarted(coreTask, today)) {
            return new DoingSpliter(today);
        }
        return new WaitingSpliter();
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
