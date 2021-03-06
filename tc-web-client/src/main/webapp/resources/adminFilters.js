var appFilter="task_control";
var ADMIN = true;

function getUsers(){
	var url = DOMAIN + "filtros/usuarios";
	$.get(url,function(data){
		$("#user-filter-container").empty();
		$("#user-filter-container").append(data);

        $("#user-filter-container input:checkbox").click(function(event){
            toogleUserFilterTasks($(this));
        });
	});
}

function filterRadio(filter){
    appFilter = filter.data("filter");
    loadMonth();
}

function toogleUserFilterTasks(filter){
    var name = filter.data("filter");
    if(users.indexOf(name) < 0){
        if(users === "" || users === undefined){
            users = name;
        } else {
            users = users + "," + name;
        }
    } else {
        users = users.replace(","+name, "");
        users = users.replace(name+",", "");
        users = users.replace(name, "");
    }
    loadMonth();
}

function handleActiveItemMenu(){
    $("#top-menu li").removeClass("active");
    if(/tarefas/.test(window.location.pathname)){
        $($("#top-menu li")[0]).find("a").addClass("active");
    }
    if(/usuarios/.test(window.location.pathname)){
        $($("#top-menu li")[2]).find("a").addClass("active");
    }
    if(/relatorios/.test(window.location.pathname)){
        $($("#top-menu li")[4]).find("a").addClass("active");
    }
}

$(document).ready(function(){
    $(".filter-group input.app").click(function(event){
        filterRadio($(this));
    });

    $(".filter-group input.app").each(function(element){
        if($(this).data("filter") === "task_control"){
            $(this).prop( "checked", true );
        }
    });

    handleActiveItemMenu();
});
