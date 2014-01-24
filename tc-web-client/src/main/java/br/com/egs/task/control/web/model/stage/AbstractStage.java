package br.com.egs.task.control.web.model.stage;

public abstract class AbstractStage {

    protected Integer daysInterval;
    protected Integer daysRun;

    protected AbstractStage(Integer daysInterval, Integer daysRun) {
        this.daysInterval = daysInterval;
        this.daysRun = daysRun;
    }

    public Integer calculateDaysInterval(){
        return daysInterval;
    };

    public Integer calculateDaysRun() {
        return daysRun;
    }

    public String toString(){
        return this.getClass().getSimpleName().toString();
    };

}
