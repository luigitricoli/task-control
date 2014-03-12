package br.com.egs.task.control.web.rest.client.task.split;

import br.com.egs.task.control.web.model.OneWeekTask;
import br.com.egs.task.control.web.rest.client.task.CoreTask;
import br.com.egs.task.control.web.rest.client.task.TaskDate;

class WaitingSpliter extends TaskSpliter {

    protected WaitingSpliter(TaskDate referenceMonth) {
        super(referenceMonth);
    }

    @Override
    protected void run(CoreTask coreTask, OneWeekTask.Builder task) throws Exception {

    }
}
