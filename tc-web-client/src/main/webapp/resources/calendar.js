var CURRENT_MONTH="currentMonth";
var activeFilters="";

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
        ajustHashtagLineHeight(task);
    });
    
    $(".week").each(function(){
    	var tasksCount = $(this).children(".task").length;
    	var extraTasks = tasksCount-3;
    	if(extraTasks>0){
    	    var weekCalendarId = "#"+$(this).attr("id").replace("task", "calendar");
    	    var weekCalendarColumn = $(weekCalendarId+" td");
    	    
    	    var extraHeight = extraTasks*19.35;
    	    weekCalendarColumn.height($(weekCalendarId).height()+extraHeight);
    	    $(this).height($(this).height()+extraHeight);
    	}
    });    
}
function ajustHashtagLineHeight(task){
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
		$(this).removeClass("today");
		if(/hoje/i.test(value)){
			$(this).addClass("today");	
		}
    	
		$(this).text(value);
	});
}
function getMonth(month){
	var url = DOMAIN + "calendario/mes/" + month;
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
	var url = DOMAIN + "tarefas/mes/" + month;
    if(activeFilters !== "" && activeFilters !== undefined){
        url = url + "?filters=" + activeFilters;
    }
	$.get(url,function(data){
		$("#tasks-layer").empty();
		$("#tasks-layer").append(data);
		
		ajustMonthLines();
        addClickActionToTasks();
        openTask();
	});

}

function openTask(){
    if(OPEN_TASK !== undefined){
        var id = "#task-"+OPEN_TASK;
        OPEN_TASK = undefined;
        $(id)[0].click();
    }
}

function addClickActionToTasks(){
    $(".task").click(function(){
        populateTimeline($(this));
    });
}

function populateTimeline(task){
    var url = DOMAIN + "tarefas/" + task.data("id") + "/historico";
    $.get(url,function(data){
        $("#task-history").empty();
        $("#task-history").show();

        var html = $($.parseHTML(data));
        $("#task-history").append(html);
        $("#iteraction-form").data("task-id", task.attr("id"));
        $("#task-description").text(task.find(".task-description").text());

        $( ".post:contains('#atraso')" ).addClass("late");
        $( ".post:contains('#horaextra')" ).addClass("overtime");
        
    	$("#add_comentary").click(function() {
    		addPost(task, url);
    	});	
        
        $("#btn-task-history")[0].click();
    });
}

function toogleFilterTasks(filter){
    var name = filter.data("filter");
    if(activeFilters.indexOf(name) < 0){
        if(activeFilters === "" || activeFilters === undefined){
            activeFilters = name;
        } else {
            activeFilters = activeFilters + "," + name;
        }
    } else {
        activeFilters = activeFilters.replace(","+name, "");
        activeFilters = activeFilters.replace(name+",", "");
        activeFilters = activeFilters.replace(name, "");
    }
    loadMonth();
}

function addPost(task, url){
	$.post(url, { "text": $("#iteraction-form").find("#comentary").val() }, function(data){
		populateTimeline(task);
	});

}

$(document).ready(function(){
    $(".filter-group label").click(function(){
            toogleFilterTasks($(this));
        }
    );
//TODO error duplicated click
//    $(".filter-group input:checkbox").click(function(event){
//            toogleFilterTasks($(this));
//        }
//    );
	$("#next-month").click(function() {
		nextMonth();
	});
	$("#previous-month").click(function() {
		prevMonth();
	});
	loadMonth();
});