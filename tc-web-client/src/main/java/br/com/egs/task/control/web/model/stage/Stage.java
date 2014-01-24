package br.com.egs.task.control.web.model.stage;

public enum Stage {

    FINISHED {
        @Override
        public AbstractStage getInstance(Integer daysInterval, Integer daysRun) {
            return new Finished(daysInterval, daysRun);
        }
    },
    DOING {
        @Override
        public AbstractStage getInstance(Integer daysInterval, Integer daysRun) {
            return new Doing(daysInterval, daysRun);
        }
    },
    LATE {
        @Override
        public AbstractStage getInstance(Integer daysInterval, Integer daysRun) {
            return new Late(daysInterval, daysRun);
        }
    },
    WAITING {
        @Override
        public AbstractStage getInstance(Integer daysInterval, Integer daysRun) {
            return new Waiting(daysInterval, daysRun);
        }
    };

    public abstract AbstractStage getInstance(Integer daysInterval, Integer daysRun);

}
