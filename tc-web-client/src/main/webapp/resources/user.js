function userInfoActions(selected){
    if("logout" === selected.value){
        window.location = DOMAIN + "logout";
    }
    if("changePass" == selected.value){
    	    $("#block-screen").show();
    	    $("#add-changePass-container").show();
    }
    if("newUser" == selected.value){
	    $("#block-screen").show();
	    $("#add-newUser-container").show();
    }
    
    resetUserNick();
    event.preventDefault();
}

function resetUserNick(){
	$("#user .dropdown .selected").text($("#nickName").text());
}