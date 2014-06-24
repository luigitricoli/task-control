function userInfoActions(selected){
    if("logout" === selected.value){
        window.location = DOMAIN + "logout";
    }
    if("changePass" == selected.value){
    	    $("#block-screen").show();
    	    $("#add-changePass-container").show();
    	    event.preventDefault();
    }
}