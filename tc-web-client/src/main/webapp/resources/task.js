function Task(obj){
    this.begin = true;
    this.end = true;
    this.id = obj.id;
    this.status = obj.status;
    this.description = obj.description;

    var d = obj.startDate.split("/");
    this.startDate = new Date(d[2], d[1]-1, d[0], 0, 0, 0, 0);
    if(this.startDate < FIRST_DAY_OF_MONTH){
        this.startDate = FIRST_DAY_OF_MONTH;
        while(this.start_date_on_weekend()){
            this.startDate.setDate(this.startDate.getDate()+1);
        }
        this.begin = false;
    }

    var f = obj.foreseenEndDate.split("/");
    this.foreseenDate = new Date(f[2], f[1]-1, f[0], 0, 0, 0, 0);
    if(this.foreseenDate > LAST_DAY_OF_MONTH){
        this.foreseenDate = LAST_DAY_OF_MONTH;
        this.end = false;
    }

    if(obj.endDate !== undefined){
        var e = obj.endDate.split("/");
        this.endDate = new Date(e[2], e[1]-1, e[0], 0, 0, 0, 0);
        if(this.endDate > LAST_DAY_OF_MONTH){
            this.end = false;
        }
    }


}

Task.prototype.only_at_weekend = function(){
    if(this.start_date_on_weekend() && (this.foreseen_date_on_weekend() || this.end_date_on_weekend())) {
        return true;
    } else {
        return false;
    }
}

Task.prototype.start_date_on_weekend = function(){
    return this.startDate.getDay() === 6 || this.startDate.getDay() === 0;
}

Task.prototype.foreseen_date_on_weekend = function(){
    return this.foreseenDate.getDay() === 6 || this.foreseenDate.getDay() === 0;
}

Task.prototype.end_date_on_weekend = function(){
    return this.endDate !== undefined && (this.endDate.getDay() === 6 || this.endDate.getDay() === 0);
}

Task.prototype.begin_current_month = function(){
    return this.begin;
}

Task.prototype.end_current_month = function(){
    return this.end;
}

Task.prototype.first_day = function(){
    return this.startDate.getDate();
}

Task.prototype.foreseen_days = function(){
    return (this.foreseenDate - this.startDate) / 86400000;
}

Task.prototype.is_late = function(){
    return this.status === "late";
}

Task.prototype.is_finished = function(){
    return this.status === "finished"
}

Task.prototype.total_days = function(){
    var lastDate = this.foreseenDate;
    if (this.is_late()) {
        lastDate = TODAY;
    }
    if(this.endDate !== undefined && this.endDate > this.foreseenDate){
        lastDate = this.endDate;
    }

    return (lastDate - this.startDate) / 86400000;
}

Task.prototype.last_day = function(){
    return this.startDate.getDate() + this.total_days();
}

Task.prototype.progress_days = function(){
    var last_date = TODAY;
    if (this.is_finished()) {
        last_date = this.endDate;
    }

    var days = (last_date - this.startDate) / 86400000;
    return days >= 0 ? days : 0;
}