var DAY_LABELS = ["Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab"];
var FIRST_DAY_OF_MONTH = new Date();
var LAST_DAY_OF_MONTH = new Date();

/* TODO improve it */
var TODAY = new Date();
TODAY.setHours(0);
TODAY.setMinutes(0);
TODAY.setSeconds(0);
TODAY.setMilliseconds(0);

var CURRENT_MONTH = "currentMonth";
var CURRENT_YEAR = "currentYear";
var activeFilters = "";
var appFilter = "";
var users = "";

function isAdmin(){
    try {
        return ADMIN;
    } catch(error) {
        return false;
    }
}
function getIntCookie(name){
	return parseInt($.cookie(name));
}
function setYearCookie(value) {
	$.cookie(CURRENT_YEAR, value, {
		path : '/'
	});
}
function setMonthCookie(value) {
	$.cookie(CURRENT_MONTH, value, {
		path : '/'
	});
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
function addClickActionToTasks() {
	$(".task").click(function() {
		populateTimeline($(this));
	});
}
function addHoverActionToTasks(){
    $(".task").hover(
        function(){
            var parent_day = $(this).parent().data('number');
            var position = $(this).data('position');
            var tasks = $(".task")
                .filter("[data-position={0}]".format([position]))
                .filter(function(index){
                    if($(this).parent().data('number') >= parent_day ){
                        return true;
                    }
                    return false;
                })
                .not(this);
            tasks.find(".task-label").css('opacity', '0');
            var element = $(this);
            setTimeout(function(){
                $(".task").css('overflow', 'hidden');
                element.css('overflow', 'visible');
            }, 100);
        },
        function(){
            $(".task[data-position=" + $(this).data('position') + "]").find(".task-label").css('opacity', '1');
            $(".task").css('overflow', 'hidden');
        }
    );
}
function cleanTimeline() {
	$("#task-history").empty();
	$("#task-history").hide();
}

function populateTimeline(task) {
	cleanTimeline();

	var url = DOMAIN + "tarefas/" + task.attr("id") + "/historico";
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

		if (task.hasClass("doing") || task.hasClass("late")) {
			$("#iteraction-form").show();

			$("#iteraction-form").data("task-id", task.attr("id"));

			$("#add_comentary").click(function() {
				var form = new FormData();
				form.append("text", $("#iteraction-form").find("#comentary").val());
				
				var file = $("#iteraction-form").find("#upload")[0].files[0];
				if(undefined !== file){	
					form.append("upload", file, file.name);					
				}

				$.ajax({
				    url: url,
				    type: "POST",
				    data: form,
				    contentType: false,
					processData: false,
				    success: function(data) {
				    	populateTimeline(task);
				    },
				    error: function() {
				    }
				  });
			});

            $("#finish").click(function() {
                finish(task);
            });
            $("#finish").show();
        }

        activeDelete(task);

        activeReplan(task);

        $("#btn-task-history")[0].click();
    });
}

function activeDelete(task){
        if(!isAdmin()){
            return;
        }

        $("#cancel").click(function() {
            $("#cancel-block-screen").show();
            $("#cancel-task-container").show();
        });

        $("#cancel-cancel-btn").click(function(event){
            closeFloatWindow("#replan-task-container");
        });
        $("#salve-cancel-btn").click(function(event){
            removeTask();
            event.preventDefault();
        });

        $("#cancel").show();

}

function removeTask(){
    var url = DOMAIN + "tarefas/" + $("#cancel-id").val();

    $.ajax({
        "url": url,
        "type": "DELETE",
        
        "success": function(data) {
                    if("success" === data) {
                        closeFloatWindowAlert("#cancel-task-container");
                        loadMonth();
                    } else if (data.message) {
                        showFloatWindowAlert("#cancel-task-container", data.message);
                    } else {
                        showFloatWindowAlert("#cancel-task-container", "NÃ£o foi possÃ­vel remover esta tarefa, entre em contato com o Gestor.");
                    }

        }
    });

}

function activeReplan(task){
        if(task.hasClass("finished")){
            return;
        }

        if(!isAdmin() && task.hasClass("late")){
            return;
        }

        if(task.hasClass("waiting")){
            //$("#replan-task-form .startDay").val("");
            $("#replan-task-form .startDay").removeAttr("disabled");
        }

        if(isAdmin()){
            $("#replan-task-form .startDay").removeAttr("disabled");
        }

        $("#replan").click(function() {
            $("#replan-block-screen").show();
            $("#replan-task-container").show();
        });
        $("#cancel-replan-btn").click(function(event){
            closeFloatWindow("#replan-task-container");
        });
        $("#salve-replan-btn").click(function(event){
            replan();
            event.preventDefault();
        });

        var startParts = $(".startDay").val().split("/");
        startParts[2] = startParts[2].replace("20","");
        $(".startDay").val("{0}/{1}/{2}".format(startParts));

        $(".startDay").mask('00/00/00');
        $(".foreseenDay").mask('00/00/00');
        $("#replan").show();

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

function replan(){
    var url = DOMAIN + "tarefas/" + $("#replan-id").val() + "/planejamento";

    var startValue = $("#replan-start").val();
    var foreseenValue = $("#replan-foreseen").val();

    if("" === startValue){
        showFloatWindowAlert("#replan-task-container", "Data de InÃ­cio nÃ£o pode estar vazia.");
        return;
    }

    if("" === foreseenValue){
        showFloatWindowAlert("#replan-task-container", "Data de Fim nÃ£o pode estar vazia.");
        return;
    }

    var start = new BrazilianDate(startValue);
    var foreseen = new BrazilianDate(foreseenValue);

    if(foreseen.compare(start) < 0){
        showFloatWindowAlert("#replan-task-container", "A data Fim nÃ£o pode ser menor que a data de InÃ­cio.");
        return;
    }

    var data = {"foreseen" : foreseen.sDate}
    if(!$("#replan-start").is(":disabled")){
        data.start = start.sDate;
    }

    $.ajax({
        "url": url,
        "type": "PUT",
        "data": data,
        "success": function(data) {
                    if("success" === data) {
                        closeFloatWindowAlert("#replan-task-container");
                        loadMonth();
                    } else if (data.message) {
                        showFloatWindowAlert("#replan-task-container", data.message);
                    } else {
                        showFloatWindowAlert("#replan-task-container", "NÃ£o foi possÃ­vel replanejar esta tarefa, entre em contato com o Gestor.");
                    }

        }
    });

}

function showPostAlert(text){
    showAlert("#iteraction-form", text);
}

function closePostAlert(){
    closeAlert("#iteraction-form");
}

function showAlert(idElement, text) {
        $(idElement + " .alert p").text(text);
        $(idElement + " .alert").show();
        $(idElement + " .alert").switchClass( "begin", "end", 1500 );
}

function closeAlert(){
        $(".alert").hide();
        $(".alert").switchClass( "end", "begin", 0 );
}

function showFloatWindowAlert(idElement, text){
        closeFloatWindowAlert(idElement);

        var root = $(idElement);
        var height = root.height();
        root.height(height+35);
        root.find(".alert p").text(text);
        root.find(".alert").show();
        root.find(".alert").switchClass( "begin", "end", 1500 );
}

function closeFloatWindowAlert(idElement){
        var root = $(idElement);
        var alert = root.find(".alert");
        if(alert.is(":visible")){
            root.height(root.height()-35);
            alert.hide();
            alert.switchClass( "end", "begin", 0 );
        }
}

function finish(task){
    var url = DOMAIN + "tarefas/" + task.attr("id") + "/finalizacao";

    var today = new Date();
    var month = today.getMonth() + 1;
    var dateValue = today.getFullYear() + "-" + month + "-" + today.getDate();

    $.ajax({
        "url": url,
        "type": "PUT",
        "data": { "date" : dateValue },
        "success": function(data) {
                    if("success" === data) {
                        closePostAlert();
                        loadMonth();
                    } else if (task.hasClass("late")) {
                       closePostAlert();
                       showPostAlert("Ã‰ necessÃ¡rio justificar o atraso da tarefa com uma das seguintes hashtag: #atraso ou #atrasado ou #atrasada.")
                    } else {
                       closePostAlert();
                       showPostAlert("NÃ£o foi possÃ­vel finalizar a tarefa.")
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
	$.post(url, {"text" : $("#iteraction-form").find("#comentary").val()}, function(data) {
		populateTimeline(task);
	});
}

function addTask(){
    var url = DOMAIN + "tarefas";
	$.post(url, $("#add-task-form").serialize(), function(data){
	    if("success" === data) {
            loadMonth();
            $("#cancel-register-btn")[0].click();
	    }else if("fail" !== data && "" !== data) {
            showFloatWindowAlert("#add-task-container", data);
        } else {
            showFloatWindowAlert("#add-task-container", "Campos preenchidos incorretamente.");
	    }
	});
}

function closeFloatWindow(){
    closeFloatWindowAlert("#add-task-container");
    $("#add-task-container").hide();

    closeFloatWindowAlert("#replan-task-container");
    $("#replan-task-container").hide();

    $(".block-screen").hide();
}


function disableRepeat() {
    $("#repeat").prop("disabled", true);
    $("#repeat").prop("checked", false);
    $("#repeat-value").val("");
    $("#repeat-value").prop("disabled", true);
}

$(document).ready(function() {
    $.ajaxSetup({
        cache: false
    });

	$(".filter-group input:checkbox").click(function(event) {
		toogleFilterTasks($(this));
	});
	$("#next-month").click(function() {
		nextMonth();
	});
	$("#previous-month").click(function() {
		prevMonth();
	});
	$("#btn_new").click(function(event){
	    $("#block-screen-tasks").show();
	    $("#add-task-container").show();
	    event.preventDefault();
	});
	$("#cancel-register-btn").click(function(event) {
		closeFloatWindow();
		disableRepeat();
	});
	$("#salve-register-btn").click(function(event){
        addTask();
        event.preventDefault();
    });

    $("#id-type").click(function(event){
        if($(this).val() == "INT"){
            $("#id-value").val(new Date().getTime());
        }
    });

    $("#foreseen-type").change(function(event){
        if($(this).val() == "hours"){
            $("#repeat").prop("disabled", false);
        } else {
            disableRepeat();
        }
    });

    $("#repeat").change(function(event){
        if($(this).prop("checked")){
            $("#repeat-value").prop("disabled", false);
        } else {
            $("#repeat-value").prop("disabled", true);
        }
    });

    $("#startDay").datepicker({
                        "dateFormat": "dd/mm/y",
                        "showOn": "button",
    });

    $(".startDay").mask('00/00/00');
    $(".foreseenDay").mask('00/00/00');
});