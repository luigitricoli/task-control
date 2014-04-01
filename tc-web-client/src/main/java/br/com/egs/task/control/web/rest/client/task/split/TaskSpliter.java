package br.com.egs.task.control.web.rest.client.task.split;


import br.com.egs.task.control.web.model.Hashtag;
import br.com.egs.task.control.web.model.OneWeekTask;
import br.com.egs.task.control.web.rest.client.task.CorePost;
import br.com.egs.task.control.web.rest.client.task.CoreTask;
import br.com.egs.task.control.web.rest.client.task.TaskDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public abstract class TaskSpliter {

    private Logger log = LoggerFactory.getLogger(TaskSpliter.class);

    private List<OneWeekTask> tasks;
    private int weekIndex;
    private boolean keepInNextWeek;
    private TaskDate referenceDateMonth;

    protected TaskSpliter(TaskDate referenceDateMonth){
        this.referenceDateMonth = referenceDateMonth;
    }

    public boolean keepInNextWeek() {
        return keepInNextWeek;
    }

    public void setKeepInNextWeek(boolean keepInNextWeek) {
        this.keepInNextWeek = keepInNextWeek;
    }

    public final void split(CoreTask coreTask) {
        loadTasks();
        setKeepInNextWeek(true);

        for (weekIndex = firstWeekIndex(coreTask); weekIndex <= lastWeekIndex(coreTask); weekIndex++) {

            OneWeekTask.Builder builder = new OneWeekTask.Builder(coreTask.getId(), coreTask.getDescription());

            // TODO improve it
            try {
                if (isFirstWeek(coreTask)) {
                    builder.starDay(coreTask.getStartDate().getDayOfWeek());
                } else {
                    builder.continuationPreviousWeek();
                }

                if (isLastWeek(coreTask)) {
                    builder.foreseenEndDay(coreTask.getForeseenEndDate().getDayOfWeek());
                } else {
                    builder.continueNextWeek();
                }

                run(coreTask, builder);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                continue;
            }

            addDayInfos(coreTask, builder);

            tasks.add(weekIndex, builder.build());
        }
    }

    private void addDayInfos(CoreTask coreTask, OneWeekTask.Builder builder) {
        for (CorePost corePost : coreTask.getPosts()) {
            Calendar end = coreTask.getStartDate().toCalendar();

            int weeks =  weekIndex - firstWeekIndex(coreTask);
            int daysCount = weeks*7 + builder.getDaysRun();
            end.add(Calendar.DAY_OF_MONTH, daysCount);
            end.set(Calendar.HOUR, 23);
            end.set(Calendar.MINUTE, 59);
            end.set(Calendar.SECOND, 59);
            end.set(Calendar.MILLISECOND, 0);

            if(!corePost.wasAdded() && corePost.isBefore(end)){
                corePost.added();
                if(corePost.hasOvetimeHashtag()){
                    builder.addHashtag(corePost.getDayOfWeek(), Hashtag.OVERTIME);
                }
                if(corePost.hasLateHashtag()){
                    builder.addHashtag(corePost.getDayOfWeek(), Hashtag.LATE);
                }
            }
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
        Integer previousWeeks = referenceDateMonth.toCalendar().get(Calendar.WEEK_OF_YEAR) - coreTask.getStartDate().toCalendar().get(Calendar.WEEK_OF_YEAR);

        if(weekIndex == 0 && previousWeeks <= 1){
            return true;
        } else {
            return weekIndex == coreTask.getStartDate().getWeekOfMonth();
        }
    }

    private boolean isLastWeek(CoreTask coreTask) {
        return weekIndex == lastWeekIndex(coreTask);
    }

    protected Integer firstWeekIndex(CoreTask coreTask) {
        if(referenceDateMonth.toCalendar().get(Calendar.MONTH) > coreTask.getStartDate().toCalendar().get(Calendar.MONTH)){
            return 0;
        }
        return coreTask.getStartDate().getWeekOfMonth();
    }

    protected Integer lastWeekIndex(CoreTask coreTask){
        if(referenceDateMonth.toCalendar().get(Calendar.MONTH) < coreTask.getForeseenEndDate().toCalendar().get(Calendar.MONTH)){
            return 6;
        }
        return coreTask.getForeseenEndDate().getWeekOfMonth();
    }

    protected abstract void run(CoreTask coreTask, OneWeekTask.Builder task) throws Exception;

    protected boolean isInSameWeek(TaskDate date) {
        return date.getWeekOfMonth().equals(weekIndex);
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
