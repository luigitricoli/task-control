<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="add-handleUser-container" class="float-window handleUserWindow">
	<form id="handle-user-form">
		<h4>User Signup</h4>
		<div class="alert begin">
			<p></p>
		</div>
<script type="text/javascript">
listUser();
</script>
<table>
	<thead>
		<tr>
			<th>Name</th>
			<th>Username</th>
			<th>Email</th>
			<th>Application</th>
			<th>Edit</th>
			<th>Delete</th>
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