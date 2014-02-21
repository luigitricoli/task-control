package br.com.egs.task.control.web.model;

import br.com.egs.task.control.web.model.stage.AbstractStage;
import br.com.egs.task.control.web.model.stage.Stage;

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

    public OneWeekTask(String id, Integer startDay, Integer daysInterval, Integer daysRun, Stage stage, String description, Map<Integer, Hashtags> hashtagsByDay) {
        this.id = id;
        this.startDay = startDay;
        this.daysInterval = daysInterval;
        this.stage = stage;
        this.description = description;
        this.hashtagsByDay = hashtagsByDay;
        this.daysRun = daysRun;
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
        return result;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", startDay=" + startDay +
                ", daysInterval=" + daysInterval +
                ", stage=" + stage +
                ", description='" + description + '\'' +
                ", hashtagsByDay=" + hashtagsByDay +
                ", daysRun=" + daysRun +
                '}';
    }

    public static class Builder {

        private String id;
        private Integer sday;
        private Integer fday;
        private Stage st;
        private String desc;
        private Map<Integer, Hashtags> htsByDay;
        private Integer runUntil;

        public Builder(String id, String description) {
            this.id = id;
            this.sday = 2;
            this.fday = 5;
            this.runUntil = 5;
            this.desc = description;
            this.htsByDay = new HashMap<>();

            as(Stage.WAITING);
        }

        public Builder starDay(Integer startDayOfWeek){
            this.sday = startDayOfWeek;
            return this;
        }

        public Builder foreseenEnd(Integer dayOfWeek){
            this.fday = (dayOfWeek - sday)+1;
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

        public Builder runUntil(Integer dayOfWeek) {
            dayOfWeek = dayOfWeek + 1;
            this.runUntil = dayOfWeek-sday;
            return this;
        }

        public OneWeekTask build() {
            AbstractStage stage = st.getInstance(fday, runUntil);
            return new OneWeekTask(id, sday, stage.calculateDaysInterval(), stage.calculateDaysRun(), st, desc, htsByDay);
        }

    }

}
