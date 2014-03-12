package br.com.egs.task.control.web.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OneWeekTask {

    private String id;
    private Integer startDay;
    private Integer daysInterval;
    private Integer daysRun;
    private Stage stage;
    private String description;
    private Map<Integer, Hashtags> hashtagsByDay;
    private Boolean continueNextWeek;
    private Boolean continuationPreviousWeek;

    public OneWeekTask(String id, Integer startDay, Integer daysInterval, Integer daysRun, Stage stage, String description, Map<Integer, Hashtags> hashtagsByDay, Boolean continueNextWeek, Boolean continuationPreviousWeek) {
        this.id = id;
        this.startDay = startDay;
        this.daysInterval = daysInterval;
        this.stage = stage;
        this.description = description;
        this.hashtagsByDay = hashtagsByDay;
        this.daysRun = daysRun;
        this.continueNextWeek = continueNextWeek;
        this.continuationPreviousWeek = continuationPreviousWeek;
    }

    public String getId() {
        return id;
    }

    public Integer getStartDay() {
        return startDay;
    }

    public Integer getDaysInterval() {
        return daysInterval;
    }

    public Stage getStage() {
        return stage;
    }

    public String getDescription() {
        return description;
    }

    public Integer getDaysRun() {
        return daysRun;
    }

    public Set<Integer> getDaysInfo() {
        return hashtagsByDay.keySet();
    }

    public Hashtags getHashtagsBy(Integer day) {
        return hashtagsByDay.get(day);
    }

    public boolean isContinueNextWeek() {
        return continueNextWeek;
    }

    public boolean isContinuationPreviousWeek() {
        return continuationPreviousWeek;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        OneWeekTask task = (OneWeekTask) obj;

        if (!daysInterval.equals(task.daysInterval)) return false;
        if (daysRun != null ? !daysRun.equals(task.daysRun) : task.daysRun != null) return false;
        if (!description.equals(task.description)) return false;
        if (!hashtagsByDay.equals(task.hashtagsByDay)) return false;
        if (!id.equals(task.id)) return false;
        if (stage != task.stage) return false;
        if (!startDay.equals(task.startDay)) return false;
        if (!continueNextWeek.equals(task.continueNextWeek)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + startDay.hashCode();
        result = 31 * result + daysInterval.hashCode();
        result = 31 * result + stage.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + hashtagsByDay.hashCode();
        result = 31 * result + (daysRun != null ? daysRun.hashCode() : 0);
        result = 31 * result + continueNextWeek.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", startDay=" + startDay +
                ", daysInterval=" + daysInterval +
                ", daysRun=" + daysRun +
                ", continueNextWeek=" + continueNextWeek +
                ", stage=" + stage +
                ", description='" + description + '\'' +
                ", hashtagsByDay=" + hashtagsByDay +
                '}';
    }

    public static class Builder {

        public static final int LAST_UTIL_DAY_OF_WEEK = 6;
		private String id;
        private Integer sDay;
        private Integer foreseenEndDay;
        private Stage st;
        private String desc;
        private Map<Integer, Hashtags> htsByDay;
        private Integer rDay;
        private boolean cNextWeek;
        private boolean cPreviousWeek;

        public Builder(String id, String description) {
            this.id = id;
            this.sDay = 2;
            this.foreseenEndDay = LAST_UTIL_DAY_OF_WEEK;
            this.rDay = 0;
            this.desc = description;
            this.htsByDay = new HashMap<>();
            this.cNextWeek = false;
            this.cPreviousWeek = false;

            as(Stage.WAITING);
        }

        public Builder starDay(Integer dayOfWeek) throws Exception{
            if(dayOfWeek>foreseenEndDay){
                throw new Exception("The new start day is greater than the foreseen end day");
            }
            sDay = dayOfWeek;
            return this;
        }

        public Builder foreseenEndDay(Integer dayOfWeek) throws Exception{
            if(dayOfWeek< sDay){
                throw new Exception("The new foreseen end day is less than the start day");
            }
            foreseenEndDay = dayOfWeek;
            return this;
        }
        
        public Builder runUntil(Integer dayOfWeek) throws Exception {
            if(dayOfWeek< sDay){
                throw new Exception("The new run day is less than the start day");
            }
            rDay = dayOfWeek;
            return this;
        }

        public Builder addHashtag(Integer day, Hashtag ht) {
            Hashtags hts = htsByDay.get(day);
            if (hts == null) {
                hts = new Hashtags(ht);
                htsByDay.put(day, hts);
            } else {
                hts.add(ht);
            }
            return this;
        }

        public Builder as(Stage st) {
            this.st = st;
            return this;
        }

        public Builder continueNextWeek(){
            cNextWeek = true;
            return this;
        }

        public Builder continuationPreviousWeek(){
            cPreviousWeek = true;
            return this;
        }

        public OneWeekTask build() {
            return new OneWeekTask(id, sDay, calculateForeseenInterval(), calculateRunInterval(), st, desc, htsByDay, cNextWeek, cPreviousWeek);
        }

        public Integer calculateForeseenInterval(){
            return (foreseenEndDay + 1) - sDay;
        }

        private Integer calculateRunInterval(){
            Integer tmp = (rDay + 1) - sDay;

            if(tmp < 0){
                return 0;
            } else {
                return tmp;
            }
        }

    }

}
