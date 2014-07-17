function validateEmail(email) { 
    var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
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

function saveUser() {
	
	if($("#name").val() == "" || $("#email").val() == "" || $("#login").val() == "" || $("#pass").val() == ""){
		
		closeAddAlertUser();
		showAddAlertUser("O preenchimento e obrigatorio para todos os campos!");
		
	}else if(!validateEmail($("#email").val())){
		
		closeAddAlertUser();
		showAddAlertUser("Email invalido!");
		
	}else if(!($("#userApplications input").filter(":checked").size() > 0)){
		
		closeAddAlertUser();
		showAddAlertUser("Selecione um dos Sistemas!");
		
	}else{

	//var isApplicationChecked = $("#userApplications input").filter(":checked").size() > 0;	
	
	var url = DOMAIN + "saveUser";
	var formData = $("#new-user-form").serialize();
	var successFunction = function(data) {
		if ("sucess" == data) {
			$("#cancel-register-user")[0].click();
			successBallon("Usuario criado com sucesso!");
		} 
	};
	$.post(url, formData, successFunction);
	}
}

function showAddAlertUser(text) {
	$("#add-newUser-container").height("400px");
	$("#new-user-form .alert p").text(text);
	$("#new-user-form .alert").show();
	$("#new-user-form .alert").switchClass("begin", "end", 1500);
}

function closeFloatWindowUser() {
	$("#block-screen").hide();
	$("#add-newUser-container").hide();
	closeAddAlertUser();
}

function closeAddAlertUser() {
	$("#add-newUser-container").height("380px");
	$("#new-user-form .alert").hide();
	$("#new-user-form .alert").switchClass("end", "begin", 0);
}
$(document).ready(function() {
	
	$("#cancel-register-user").click(function(event) {
		closeFloatWindowUser();
	});

	$("#salve-register-user").click(function(event) {
		saveUser();
	});
});