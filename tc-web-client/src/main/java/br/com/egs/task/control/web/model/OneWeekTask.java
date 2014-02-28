package br.com.egs.task.control.web.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import br.com.egs.task.control.web.model.stage.AbstractStage;
import br.com.egs.task.control.web.model.stage.Stage;

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

        private static final int LAST_UTIL_DAY_OF_WEEK = 6;
		private String id;
        private Integer sday;
        private Integer iday;
        private Stage st;
        private String desc;
        private Map<Integer, Hashtags> htsByDay;
        private Integer runUntil;

        public Builder(String id, String description) {
            this.id = id;
            this.sday = 2;
            this.iday = 5;
            this.runUntil = 0;
            this.desc = description;
            this.htsByDay = new HashMap<>();

            as(Stage.WAITING);
        }

        public Builder starDay(Integer startDayOfWeek) throws Exception{
            sday = startDayOfWeek;
            recalculateForeseenInterval();
            recalculateRunInterval();
            return this;
        }
        
        public void recalculateForeseenInterval() throws Exception{
        	iday += 2;
            iday = (iday - sday);
            
            //TODO create an specific exception
            if(iday<runUntil){
            	throw new Exception("The new foreseen interval is less than the run interval");
            }
        }

        public Builder foreseenEndDay(Integer dayOfWeek) throws Exception{
            iday = dayOfWeek - 1;

            try {
				recalculateForeseenInterval();
			} catch (Exception e) {
	            //TODO create an specific exception
				throw new Exception(String.format("The day of week [%s] sended as foreseen end day is less than the run end day [%s]", iday+1, runUntil+1));
			}
            return this;
        }
        
        public Builder runUntil(Integer dayOfWeek) {
        	runUntil = dayOfWeek - 1;
        	recalculateRunInterval();
            return this;
        }
        
        private void recalculateRunInterval(){
        	runUntil += 2;
        	runUntil = (runUntil - sday);
        	
        	if(runUntil < 0){
        		runUntil = 0;
        	}
        }
        
        public Builder runAtTheEnd(){
            return runUntil(LAST_UTIL_DAY_OF_WEEK);
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

        public OneWeekTask build() {
            AbstractStage stage = st.getInstance(iday, runUntil);
            return new OneWeekTask(id, sday, stage.calculateDaysInterval(), stage.calculateDaysRun(), st, desc, htsByDay);
        }

    }

}
