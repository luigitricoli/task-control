var CURRENT_MONTH = "currentMonth";
var activeFilters = "";
var appFilter = "";
var users = "";

function getIntCookie(name) {
	return parseInt($.cookie(name));
}
function setMonthCookie(value) {
	$.cookie(CURRENT_MONTH, value, {
		path : '/'
	});
}
function ajustMonthLines() {
	$(".subline.days td").height(70);
	var tasks = $(".hashtags");
	tasks.each(function() {
		var task = $(this).parent();
		ajustHashtagLineHeight(task);
	});

	$(".week").each(
			function() {
				var tasksCount = $(this).children(".task").length;
				var extraTasks = tasksCount - 3;
				if (extraTasks > 0) {
					var weekCalendarId = "#"
							+ $(this).attr("id").replace("task", "calendar");
					var weekCalendarColumn = $(weekCalendarId + " td");

					var extraHeight = extraTasks * 19.35;
					weekCalendarColumn.height($(weekCalendarId).height()
							+ extraHeight);
					$(this).height($(this).height() + extraHeight);
				}
			});
}
function ajustHashtagLineHeight(task) {
	if (task.data("added-hashtags") === undefined) {
		task.css("margin-bottom", 15);

		var weekTask = task.parent();
		weekTask.height(weekTask.height() + 10);

		var weekCalendarId = "#"
				+ weekTask.attr("id").replace("task", "calendar");
		var weekCalendarColumn = $(weekCalendarId + " td");
		weekCalendarColumn.height($(weekCalendarId).height() + 8);

		task.data("added-hashtags", true);
	}
}
function populateCalendar(days) {
	$(".subline.days").children().each(function(index) {
		var value = days[index];

		// highlight today
		$(this).removeClass("today");
		if (/hoje/i.test(value)) {
			$(this).addClass("today");
		}

		$(this).text(value);
	});
}
function getMonth(month) {
	var url = DOMAIN + "calendario/mes/" + month;
	$.getJSON(url, function(data) {
		$("#calendar-month-label").text(data.label);
		populateCalendar(data.days);
	});
}

function nextMonth() {
	var month = getIntCookie(CURRENT_MONTH) + 1;
	if (month >= 13) {
		month = 1;
	}
	setMonthCookie(month);
	getMonth(month);
	getTasks(month);
}
function prevMonth() {
	var month = getIntCookie(CURRENT_MONTH) - 1;
	if (month <= 0) {
		month = 12;
	}
	setMonthCookie(month);
	getMonth(month);
	getTasks(month);
}
function loadMonth() {
	var month = $.cookie(CURRENT_MONTH);
	if (month === undefined) {
		var date = new Date();
		month = date.getMonth() + 1;
		setMonthCookie(month);
	}
	getMonth(month);
	cleanTimeline();
	getTasks(month);
}

function formatUrlFilters(params) {
	var params = {};
	var filters = []
	if (appFilter !== "" && appFilter !== undefined) {
		filters.push(appFilter);
	}
	if (activeFilters !== "" && activeFilters !== undefined) {
		filters.push(activeFilters);
	}
	if (filters.length > 0) {
		params["filters"] = filters.join();
	}

	if (users !== "" && users !== undefined) {
		params["users"] = users;
	}
	return params;
}

function getTasks(month) {
	var url = DOMAIN + "tarefas/mes/" + month;

	var params = formatUrlFilters(params);
	if (!jQuery.isEmptyObject(params)) {
		url = url + "?" + jQuery.param(params);
	}
	$.get(url, function(data) {
		$("#tasks-layer").empty();
		$("#tasks-layer").append(data);

		ajustMonthLines();
		addClickActionToTasks();
		openTask();
	});

}

function openTask() {
	if (OPEN_TASK !== undefined) {
		var id = "#task-" + OPEN_TASK;
		OPEN_TASK = undefined;
		$(id)[0].click();
	}
}

function addClickActionToTasks() {
	$(".task").click(function() {
		populateTimeline($(this));
	});
}

function cleanTimeline() {
	$("#task-history").empty();
	$("#task-history").hide();
}

function populateTimeline(task) {
	cleanTimeline();

	var url = DOMAIN + "tarefas/" + task.data("id") + "/historico";
	$.get(url, function(data) {
		$("#task-history").show();

		var html = $($.parseHTML(data));
		$("#task-history").append(html);

		$(".post:contains('#atraso')").addClass("late");
		$(".post:contains('#atrasado')").addClass("late");
		$(".post:contains('#atrasada')").addClass("late");

		$(".post:contains('#horaextra')").addClass("overtime");
		$(".post:contains('#horasextras')").addClass("overtime");
		$(".post:contains('#horasextra')").addClass("overtime");
		$(".post:contains('#horaextras')").addClass("overtime");

		$("#task-description").text(task.find(".task-description").text());

		if (task.find(".stage").hasClass("doing")) {
			$("#iteraction-form").show();

			$("#iteraction-form").data("task-id", task.attr("id"));

			$("#add_comentary").click(function() {
				addPost(task, url);
			});

			$("#finish").show();
			$("#finish").click(function() {
				finish(task);
			});
		}

		$("#btn-task-history")[0].click();
	});
}

function toogleFilterTasks(filter) {
	var name = filter.data("filter");
	if (activeFilters.indexOf(name) < 0) {
		if (activeFilters === "" || activeFilters === undefined) {
			activeFilters = name;
		} else {
			activeFilters = activeFilters + "," + name;
		}
	} else {
		activeFilters = activeFilters.replace("," + name, "");
		activeFilters = activeFilters.replace(name + ",", "");
		activeFilters = activeFilters.replace(name, "");
	}
	loadMonth();
}

function finish(task) {
	var url = DOMAIN + "tarefas/" + task.data("id") + "/finalizacao";

	var today = new Date();
	var month = today.getMonth() + 1;
	var dateValue = today.getFullYear() + "-" + month + "-" + today.getDate();

	$
			.ajax({
				"url" : url,
				"type" : "PUT",
				"data" : {
					"date" : dateValue
				},
				"success" : function(data) {
					if ("success" === data) {
						closePostAlert();
						loadMonth();
					} else if (task.hasClass("late")) {
						closePostAlert();
						showPostAlert("É necessário justificar o atraso da tarefa com uma das seguintes hashtag: #atraso ou #atrasado ou #atrasada.")
					} else {
						closePostAlert();
						showPostAlert("Não foi possível finalizar a tarefa.")
					}

				}
			});

}

function showPostAlert(text) {
	$("#iteraction-form .alert p").text(text);
	$("#iteraction-form .alert").show();
	$("#iteraction-form .alert").switchClass("begin", "end", 1500);
}

function closePostAlert() {
	$("#iteraction-form .alert").hide();
	$("#iteraction-form .alert").switchClass("end", "begin", 0);
}

function addPost(task, url) {
	$.post(url, {
		"text" : $("#iteraction-form").find("#comentary").val()
	}, function(data) {
		populateTimeline(task);
	});
}

function addTask() {
	var url = DOMAIN + "tarefas";
	$.post(url, $("#add-task-form").serialize(), function(data) {
		if ("success" === data) {
			loadMonth();
			$("#cancel-register-btn")[0].click();
		} else if ("fail" !== data) {
			closeAddAlert();
			showAddAlert(data);
		} else {
			closeAddAlert();
			showAddAlert("Campos preenchidos incorretametne.");
		}
	});
}

function showAddAlert(text) {
	$("#add-task-container").height("355px");
	$("#add-task-form .alert p").text(text);
	$("#add-task-form .alert").show();
	$("#add-task-form .alert").switchClass("begin", "end", 1500);
}

function closeAddAlert() {
	$("#add-task-container").height("320px");
	$("#add-task-form .alert").hide();
	$("#add-task-form .alert").switchClass("end", "begin", 0);
}

function closeFloatWindow() {
	$("#block-screen").hide();
	$("#add-task-container").hide();
	closeAddAlert();
}


$(document).ready(function() {
	$(".filter-group input:checkbox").click(function(event) {
		toogleFilterTasks($(this));
	});
	$("#next-month").click(function() {
		nextMonth();
	});
	$("#previous-month").click(function() {
		prevMonth();
	});
	$("#btn_new").click(function(event) {
		$("#block-screen").show();
		$("#add-task-container").show();
		event.preventDefault();
	});
	$("#cancel-register-btn").click(function(event) {
		closeFloatWindow();
	});


	$("#salve-register-btn").click(function(event) {
		addTask();
	});

	$("#startDay").mask('00/00/00');
	$("#foreseenDay").mask('00/00/00');

	loadMonth();
});