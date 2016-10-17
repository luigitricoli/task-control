<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="add-changePass-container" class="float-window changePassWindow">
	<form id="change-pass-form">
		<h4>Troca de senha</h4>
		<div class="alert begin">
			<p></p>
		</div>

		<input type="hidden" name="login" id="login">

		<div class="field">
			<label>Old Password</label> <input type="password" name="oldPass" 
				class="changePass-input" id="oldPass" required>
		</div>
		<div class="field">
			<label>New Password</label> <input type="password" name="newPass"
				class="changePass-input" id="newPass" required>
		</div>
		<div class="field">
			<label>Confirm New Password</label> <input type="password"
				name="newcPass" class="changePass-input" id="newcPass" required>
		</div>
		<div class="field">
			<button type="reset" id="cancel-register-pass" class="btn red">
				<span class="icon add">X</span>Cancel
			</button>
			<button type="button" id="salve-register-pass" class="btn green">
				<span class="icon add">V</span>Save
			</button>
		</div>
	</form>
</div>