package br.com.egs.task.control.web.model;

import br.com.egs.task.control.web.model.stage.AbstractStage;
import br.com.egs.task.control.web.model.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Task {

    private String id;
    private Integer startDay;
    private Integer daysInterval;
    private Stage stage;
    private String description;
    private Map<Integer, Hashtags> hashtagsByDay;
    private Integer daysRun;

    private Task(String id, Integer startDay, Integer daysInterval, Integer daysRun, Stage stage, String description, Map<Integer, Hashtags> hashtagsByDay) {
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

    public static class Builder {

        private String id;
        private Integer start;
        private Integer interval;
        private Stage st;
        private String desc;
        private Map<Integer, Hashtags> htsByDay;
        private Integer run;

        public Builder(String id, Integer startDay, Integer intervalDay, String description) {
            this.id = id;
            this.start = startDay;
            this.interval = intervalDay;
            this.run = intervalDay;
            this.desc = description;
            this.htsByDay = new HashMap<>();

            as(Stage.WAITING);
        }

/*        public Builder setState(Stage st) {
            this.st = st;
            return this;
        }*/

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

        public Builder setRun(Integer days) {
            this.run = days;
            return this;
        }

        public Builder as(Stage st) {
            this.st = st;
            return this;
        }

        public Builder daysRun(Integer days) {
            this.run = days;
            return this;
        }

        public Task build() {
            AbstractStage stage = st.getInstance(interval, run);
            return new Task(id, start, stage.calculateDaysInterval(), stage.calculateDaysRun(), st, desc, htsByDay);
        }

    }

}
