package br.com.egs.task.control.web.model.stage;

public class Late extends AbstractStage {

    Integer daysLate;

    protected Late(Integer daysInterval, Integer daysRun) {
        super(daysInterval, daysRun);
        daysLate = this.daysRun-daysInterval;
    }

    @Override
    public Integer calculateDaysInterval() {
        return daysLate < 0 ? daysInterval : daysInterval+daysLate;
    }

    @Override
    public Integer calculateDaysRun() {
        return daysLate > 0 ? daysInterval : daysRun;
    }
}
