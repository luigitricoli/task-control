<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="add-newUser-container" class="float-window newUserWindow">
	<form id="new-user-form">
		<h4>Cadastro de Usu�rios</h4>
		<div class="alert begin">
			<p></p>
		</div>

		<div class="field half-left">
			<label>Nome</label> <input type="text" name="name"
				class="newUser-input" id="name" required>
		</div>
		<div class="field half-left">
			<label>Email</label> <input type="email" name="email"
				class="newUser-input" id="email" required>
		</div>
		<div class="field half-left">
			<label>Login</label> <input type="text" name="login"
				class="newUser-input" id="login" required>
		</div>
		<div class="field half-left">
			<label>Senha</label> <input type="password" name="pass"
				class="newUser-input" id="pass" required>
		</div>
		<div class="field">
			<label>Tipo</label>
			<select name="type">
				<option value="N1">N1 - Pe&atilde;o</option>
				<option value="N2">N2 - Arquitetos</option>
				<option value="MGR">MGR - Gerente Cordenador</option>
			</select>
		</div>
		<div class="field" id="userApplications">
		    <label>Sistemas</label>
			<c:forEach var="app" items="${applications.getValues()}">
			
				<div class="userbox-inside">
					<input type="checkbox" name="applications" value="${app}" id="chb-user-${app}" ><label class="checkbox-label" for="chb-user-${app}">${applications.getLabel(app)}</label>
				</div>
			</c:forEach>
		</div>
		
		<div class="userBottom">
			<button type="reset" id="cancel-register-user" class="btn red">
				<span class="icon add">X</span>Cancelar
			</button>
			<button type="button" id="salve-register-user" class="btn green">
				<span class="icon add">V</span>Salvar
			</button>
		</div>
			</div>	
	</form>
</div>