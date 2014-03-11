package br.com.egs.task.control.web.rest.client.task.split;


import br.com.egs.task.control.web.model.OneWeekTask;
import br.com.egs.task.control.web.rest.client.task.CoreTask;
import br.com.egs.task.control.web.rest.client.task.TaskDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class TaskSpliter {

    private List<OneWeekTask> tasks;
    private Logger log = LoggerFactory.getLogger(TaskSpliter.class);
    private int weekIndex;
    private boolean keepInNextWeek;

    public boolean keepInNextWeek() {
        return keepInNextWeek;
    }

    public void setKeepInNextWeek(boolean keepInNextWeek) {
        this.keepInNextWeek = keepInNextWeek;
    }

    public final void split(CoreTask coreTask) {
        loadTasks();
        setKeepInNextWeek(true);

        Integer weekIndexEnd = wasFinishedLate(coreTask) ? coreTask.getEndDate().getWeekOfYear() : coreTask.getForeseenEndDate().getWeekOfYear() ;
        for (weekIndex = coreTask.getStartDate().getWeekOfYear(); weekIndex <= weekIndexEnd; weekIndex++) {

            OneWeekTask.Builder builder = new OneWeekTask.Builder(coreTask.getId(), coreTask.getDescription());

            // TODO improve it
            try {
                if (isFirstWeek(coreTask)) {
                    builder.starDay(coreTask.getStartDate().getDayOfWeek());
                }

                if (isLastWeek(coreTask)) {
                    builder.foreseenEndDay(coreTask.getForeseenEndDate().getDayOfWeek());
                }

                run(coreTask, builder);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                continue;
            }

            tasks.add(weekIndex, builder.build());
        }
    }

    private void loadTasks() {
        tasks = new ArrayList<>();
        for (int count = 0; count < 6; count++) {
            tasks.add(null);
        }
    }

    protected boolean wasFinishedLate(CoreTask coreTask) {
          return coreTask.getEndDate() != null && coreTask.getEndDate().compareTo(coreTask.getForeseenEndDate()) > 0;
    }

    private boolean isFirstWeek(CoreTask coreTask) {
        return weekIndex == coreTask.getStartDate().getWeekOfYear();
    }

    private boolean isLastWeek(CoreTask coreTask) {
        return weekIndex == coreTask.getForeseenEndDate().getWeekOfYear();
    }

    protected abstract void run(CoreTask coreTask, OneWeekTask.Builder task) throws Exception;

    protected boolean isInSameWeek(TaskDate date) {
        return date.getWeekOfYear().equals(weekIndex);
    }

    public final OneWeekTask firstWeek() {
        return tasks.get(0);
    }

    public final OneWeekTask secondWeek() {
        return tasks.get(1);
    }

    public final OneWeekTask thirdWeek() {
        return tasks.get(2);
    }

    public final OneWeekTask fourthWeek() {
        return tasks.get(3);
    }

    public final OneWeekTask fifthWeek() {
        return tasks.get(4);
    }

    public final OneWeekTask sixthWeek() {
        return tasks.get(5);
    }

}
