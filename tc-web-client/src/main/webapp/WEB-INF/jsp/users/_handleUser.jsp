<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="add-handleUser-container" class="float-window handleUserWindow">
	<form id="handle-user-form">
		<h4>Cadastro de Usuário</h4>
		<div class="alert begin">
			<p></p>
		</div>
<script type="text/javascript">
listUser();
</script>
<table>
	<thead>
		<tr>
			<th>Nome</th>
			<th>Login</th>
			<th>Email</th>
			<th>Sistema</th>
			<th>Editar</th>
			<th>Remover</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${userList}" var="user">
			<tr>
				<td>${user.nome }</td>
				<td>${user.login }</td>
				<td>${user.email }</td>
				<td>${user.Sistema }<td>
			</tr>
		</c:forEach>
	</tbody>
</table>


	</form>
</div>