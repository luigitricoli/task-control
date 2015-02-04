var positions = [2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1];
var last_task_lines;
var calendar_ocupaccity;

function cleanCalendarOcupaccity(){
    last_task_lines = 0;
    calendar_ocupaccity = {}
}
function nextMonth() {
    var year = getIntCookie(CURRENT_YEAR);
	var month = getIntCookie(CURRENT_MONTH) + 1;
	if (month >= 13) {
		month = 1;
		year += 1;
	}
	setMonthCookie(month);
	setYearCookie(year);
	getMonth(month, year);
	cleanTimeline();
	cleanCalendarOcupaccity();
	getTasks(month, year);
}
function prevMonth() {
	var year = getIntCookie(CURRENT_YEAR);
	var month = getIntCookie(CURRENT_MONTH) - 1;
	if (month <= 0) {
		month = 12;
        year -= 1;
	}
	setMonthCookie(month);
	setYearCookie(year);
	getMonth(month, year);
	cleanTimeline();
	cleanCalendarOcupaccity();
	getTasks(month, year);
}
function loadMonth() {
    var year = $.cookie(CURRENT_YEAR);
	var month = $.cookie(CURRENT_MONTH);
	if (month === undefined || year ===  undefined) {
		var date = new Date();
		month = date.getMonth() + 1;
		setMonthCookie(month);
		setYearCookie(date.getFullYear());
	}

	getMonth(month, year);
	cleanTimeline();
	cleanCalendarOcupaccity();
	getTasks(month, year);
}
function getMonth(month, year) {
	/* TODO new code */
	FIRST_DAY_OF_MONTH = new Date(year, month - 1, 1,0,0,0,0);
    LAST_DAY_OF_MONTH = new Date(year, month, 0,0,0,0,0);

    $("#calendar-month-label").text(month + "/" + year);
    $("#task-board").empty();
    var date = new Date(FIRST_DAY_OF_MONTH.getTime());
    while(date < LAST_DAY_OF_MONTH){
        addHtmlDay(date);
        date.setDate(date.getDate() + 1);
    }
    addHtmlDay(LAST_DAY_OF_MONTH);
}
function addHtmlDay(date){
    var day = date.getDate();
    var week_day = date.getDay();
    var html_day = $("<div class='day'><h4 class='day-label'>" + day + ", " + DAY_LABELS[week_day] + "</h4></div>");
    html_day.attr("data-number", day);
    if(TODAY.getTime() === date.getTime()){
        html_day.addClass("today")
    }
    if(week_day === 0 || week_day === 6){
        html_day.addClass("weekend")
    }
    $("#task-board").append(html_day)
}
function getTasks(month, year) {
	var url = DOMAIN + "tarefas/mes/" + month + "-" + year;

	var params = formatUrlFilters(params);
	if (!jQuery.isEmptyObject(params)) {
		url = url + "?" + jQuery.param(params);
	}
	$.getJSON(url, function(data) {
        loadTask(data);
		addClickActionToTasks();
		addHoverActionToTasks();
		openTask();
	});

}
function openTask() {
    try{
        if (OPEN_TASK !== undefined) {
            var id = "#task-" + OPEN_TASK;
            OPEN_TASK = undefined;
            $(id)[0].click();
        }
    } catch (error){}
}
function loadTask(tasks){
    if(tasks.length === 0){
        $("#task-amount").text("Sem tarefas");
    }
    if(tasks.length === 1){
        $("#task-amount").text("1 tarefa");
    }
    if(tasks.length > 1){
        $("#task-amount").text(tasks.length + " tarefas");
    }
    $.each(tasks, function(index, json){
        var task = new Task(json);

        var task_html = $("<div class='task'><p class='task-label'></p><div class='progress'></div></div>");
        task_html.hide();
        task_html.attr("id", task.id);
        task_html.width(100 + (task.total_days() * 101));
        task_html.find(".progress").width(88 + (task.progress_days() * 101));
        task_html.addClass(task.status);
        task_html.find(".task-label").text(task.description);
        if(task.only_at_weekend()){
            task_html.css("z-index", "99999");
        }

        if (task.is_late()) {
            var foreseen = $("<div class='foreseen'></div>");
            foreseen.width(100 + (task.foreseen_days() * 101));
            task_html.prepend(foreseen);
        }

        if (!task.end_current_month()) {
            task_html.addClass("no_end");
        }

        if (!task.begin_current_month()) {
            task_html.addClass("no_begin");
        }        

        var calendar_index = task.first_day() - 1;
        var calendar_day = $($(".day")[calendar_index]);
        calendar_day.append(task_html);

        var position;
        for (p = 0; p <= positions.length; p++){
            position = p;
            var found_position = true
            for(d = task.first_day(); d <= task.last_day(); d++){
                var key = "_"+d;
                if((calendar_ocupaccity[key] & positions[p]) !== 0){
                    found_position = false;
                    break;
                }
            }
            if(found_position){
                break;
            }
        }

        task_html.attr('data-position', position);

        for(d = task.first_day(); d <= (task.last_day()+1); d++){
            var key = "_"+d;
            calendar_ocupaccity[key] = calendar_ocupaccity[key] | positions[position];
        }

        var position_top = 50 + (35 * position) + last_task_lines;
        task_html.css("top", position_top + "px");

        if(task_html.height() > 20){
            last_task_lines = task_html.height() - 20;
        } else {
            last_task_lines = 0;
        }
        task_html.show();

    });
}