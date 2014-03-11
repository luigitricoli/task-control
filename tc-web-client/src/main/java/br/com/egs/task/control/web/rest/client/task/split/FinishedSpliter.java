package br.com.egs.task.control.web.rest.client.task.split;

import br.com.egs.task.control.web.model.OneWeekTask;
import br.com.egs.task.control.web.model.Stage;
import br.com.egs.task.control.web.rest.client.task.CoreTask;

class FinishedSpliter extends TaskSpliter {

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
            task.runAtTheEnd();
        }
    }
}
