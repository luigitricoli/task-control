<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="add-changePass-container" class="float-window changePassWindow">
	<form id="change-pass-form">
		<h4>Troca de senha</h4>
		<div class="alert begin">
			<p></p>
		</div>

		<div class="field half-left">
			<label>Senha Antiga</label> <input type="password" name="oldPass" 
				class="changePass-input" id="oldPass" value="maca" required>
		</div>
		<div class="field half-left">
			<label>Senha Nova</label> <input type="password" name="newPass"
				class="changePass-input" id="newPass" value="maca" required>
		</div>
		<div class="field half-left">
			<label>Confirme Senha Nova</label> <input type="password"
				name="newcPass" class="changePass-input" value="maca" id="newcPass" required>
		</div>
		<div class="field half-left">
			<button type="reset" id="cancel-register-pass" class="btn red">
				<span class="icon add">X</span>Cancelar
			</button>
			<button type="button" id="salve-register-pass" class="btn green">
				<span class="icon add">V</span>Salvar
			</button>
		</div>
	</form>
</div>