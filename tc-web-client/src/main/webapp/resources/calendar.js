var CURRENT_MONTH='currentMonth';
function getIntCookie(name){
	return parseInt($.cookie(name));
}
function setMonthCookie(value){
	$.cookie(CURRENT_MONTH, value, {path:'/'});
}
function ajustMonthLines(){
	$(".subline.days td").height(70);
    var tasks = $(".hashtags");
    tasks.each(function(){
        var task = $(this).parent();
        ajustMonthLine(task);
    });
}
function ajustMonthLine(task){
	if(task.data("added-hashtags") === undefined){
		task.css("margin-bottom", 15);
	    
		var weekTask = task.parent();
	    weekTask.height(weekTask.height()+10);
	
	    var weekCalendarId = "#"+weekTask.attr("id").replace("task", "calendar");
	    var weekCalendarColumn = $(weekCalendarId+" td");
	    weekCalendarColumn.height($(weekCalendarId).height()+8);
	
	    task.data("added-hashtags", true);
	}
}


function populateCalendar(days){
	$(".subline.days").children().each(function(index){
		var value = days[index];
		
		//highlight today
		var pattern = /hoje/i;
		$(this).removeClass("today");
		if(pattern.test(value)){
			$(this).addClass("today");	
		}
    	
		$(this).text(value);
	});
}
function getMonth(month){
	var url = domain + "calendario/mes/" + month;
	$.getJSON(url,function(data){
		populateCalendar(data.days);
	});
}

function nextMonth(){
	var month=getIntCookie(CURRENT_MONTH)+1;
	if(month >= 13){
		month=1;
	}
	setMonthCookie(month);
	getMonth(month);
	getTasks(month);
}
function prevMonth(){
	var month=getIntCookie(CURRENT_MONTH)-1;
	if(month <= 0){
		month=12;
	}
	setMonthCookie(month);
	getMonth(month);
	getTasks(month);
}
function loadMonth(){
	var month = $.cookie(CURRENT_MONTH);
	if(month === undefined){
		var date=new Date();
		month=date.getMonth()+1;
		setMonthCookie(month);
	}
	getMonth(month);
	getTasks(month);
}

function getTasks(month){
	var url = domain + "tarefas/mes/" + month;
	$.get(url,function(data){
		$("#tasks-layer").empty();
		$("#tasks-layer").append(data);
		
		ajustMonthLines();
        addClickActionToTasks();
	});
}

function addClickActionToTasks(callback){
    $(".task").click(function(){
        $("#btn-task-history")[0].click();
        callback();
    });
}

$(document).ready(function(){
	$( "#next-month" ).click(function() {
		nextMonth();
	});
	$( "#previous-month" ).click(function() {
		prevMonth();
	});
	loadMonth();
});