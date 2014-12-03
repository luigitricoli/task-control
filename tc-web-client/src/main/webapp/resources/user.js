function userInfoActions(selected, evt){
    if("logout" === selected.value){
        window.location = DOMAIN + "logout";
    }
    if("changePass" == selected.value){
        $("#block-screen-main").show();
        $("#add-changePass-container").show();
    }

    resetUserNick();
    evt.preventDefault();
}


function defaultBallonAlert(msg, type){
	new noty({
		text: msg,
		type: type,
		theme:'task_control',
		timeout: 5000,
		animation: {
	        open: {height: 'toggle'},
	        close: {height: 'toggle'},
	        easing: 'swing',
	        speed: 250
	    },
		layout:'topLeft',
		template: '<div class="noty_message"><span class="noty_text" style="font-family: arial, sans-serif; color: #ffffff; font-size: 14px;"></span></div>'
			});
}

function successBallon(msg){
	defaultBallonAlert(msg, 'success');
}

//Inicio change Pass

function changePass() {
	
	if($("#oldPass").val() == "" || $("#newPass").val() == "" || $("#newcPass").val() == ""){
		closeAddAlertPass();
		showAddAlertPass("O preenchimento e obrigatorio para todos os campos!");
		return;
	}
	
	var url = DOMAIN + "senha";
	var formData = $("#change-pass-form").serialize();
	var successFunction = function(data) {
		if ("sucess" == data) {
			$("#cancel-register-pass")[0].click();
			
			successBallon("Senha atualizada com sucesso!");
			
		} else{
			closeAddAlertPass();
			showAddAlertPass(data);
		} 
	};
	$.post(url, formData, successFunction);
}

function showAddAlertPass(text) {
	$("#add-changePass-container").height("280px");
	$("#change-pass-form .alert p").text(text);
	$("#change-pass-form .alert").show();
	$("#change-pass-form .alert").switchClass("begin", "end", 1500);
}

function closeAddAlertPass() {
	$("#add-changePass-container").height("245px");
	$("#change-pass-form .alert").hide();
	$("#change-pass-form .alert").switchClass("end", "begin", 0);
}


function closeFloatWindowPass() {
	$(".block-screen").hide();
	$("#add-changePass-container").hide();
	closeAddAlertPass();
}


//Inicio Cad de usuario
function validateEmail(email) { 
    var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
} 

function saveUser() {
	
	if($("#name").val() == "" || $("#email").val() == "" || $("#login").val() == "" || $("#pass").val() == ""){
		
		closeAddAlertUser();
		showAddAlertUser("O preenchimento e obrigatorio para todos os campos!");
		
	}else if(!validateEmail($("#email").val())){
		
		closeAddAlertUser();
		showAddAlertUser("Email invalido!");
		
	}else if(!($("#userApplications input").filter(":checked").size() > 0)){
		
		closeAddAlertUser();
		showAddAlertUser("Selecione pelo menos um dos Sistemas!");
		
	}else{

	//var isApplicationChecked = $("#userApplications input").filter(":checked").size() > 0;	
	
	var url = DOMAIN + "usuarios";
	var formData = $("#new-user-form").serialize();
	var successFunction = function(data) {
		if ("sucess" == data) {
			getUsers();
			$("#cancel-register-user")[0].click();
			successBallon("Usuario criado com sucesso!");
            closeFloatWindowUser();
            window.location = DOMAIN + "usuarios";
		} 
	};
	$.post(url, formData, successFunction);
	}
}

function removeUser(id){
    var url = DOMAIN + "usuarios/" + id;

    $.ajax({
        "url": url,
        "type": "DELETE",
        "success": function(data) {
                    if("success" === data) {
                        closeFloatWindowAlert("#cancel-task-container");
                        window.location = DOMAIN + "usuarios"
                    } else if (data.message) {
                        showFloatWindowAlert("#cancel-task-container", data.message);
                    } else {
                        showFloatWindowAlert("#cancel-task-container", "Não foi possível remover este usuário, entre em contato com o Gestor.");
                    }

        }
    });

}

function showAddAlertUser(text) {
	$("#add-newUser-container").height("400px");
	$("#new-user-form .alert p").text(text);
	$("#new-user-form .alert").show();
	$("#new-user-form .alert").switchClass("begin", "end", 1500);
}

function closeFloatWindowUser() {
	$(".block-screen").hide();
	$("#add-newUser-container").hide();
	closeAddAlertUser();
}

function closeAddAlertUser() {
	$("#add-newUser-container").height("380px");
	$("#new-user-form .alert").hide();
	$("#new-user-form .alert").switchClass("end", "begin", 0);
}


function resetUserNick(){
	$("#user .dropdown .selected").text($("#nickName").text());
}


$(document).ready(function() {
	$("#cancel-register-user").click(function(event) {
		closeFloatWindowUser();
	});

	$("#salve-register-user").click(function(event) {
		saveUser();
	});
	

	$("#cancel-register-pass").click(function(event) {
		closeFloatWindowPass();
	});
	
	$("#salve-register-pass").click(function(event) {
		changePass();
	});
	
	$("#cancel-register-btn").click(function(event) {
		closeFloatWindow();
	});
	
    $("#user-info").bind("change", function(event) {
        userInfoActions(this, event)
    });

    $(".line-btn.delete").click(function(event) {
        removeUser($(this).data("login"));
    });

    $("#new-user-btn").click(function(event) {
        $("#block-screen-users").show();
        $("#add-newUser-container").show();
    });
});
