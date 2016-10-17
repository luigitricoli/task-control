<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/user.css"/>">
</head>
<body>
    <header id="top-main">
        <a href="#" id="new-user-btn" class="btn green"><span class="icon add">+</span>New User</a>
    </header>
    <div id="container-left">
        <jsp:include page="_filters.jsp"/>
    </div>
    <div id="container-right">
        <div id="navigation">
            <!--
            <div id="paginate">
                <a id="previous-page" href="#" class="btn green previous">&lt;</a>
                <a id="next-page" href="#" class="btn green next">&gt;</a>
            </div>
            -->
            <h3>Users</h3>
        </div>
        <div id="registers-list">
            <table class="table-content">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Applications</th>
                        <th colspan="1"></th>
						<th colspan="1"></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="user" items="${users}" varStatus="status">
                        <tr class="${status.count % 2 == 0 ? 'white' : 'gray' }">
                            <td>${user.name}</td>
                            <td>${user.nickname}</td>
                            <td>${user.email}</td>
                            <td>
                                <c:forEach var="system" items="${user.systems}">
                                    ${applications.getLabel(system)},
                                </c:forEach>
                            </td>
                            <!-- <td class="btn"><button class="edit">Editar</button></td> -->
							<td class="btn"><button class="line-btn changePw" data-login="${user.nickname}">Change Password</button></td>
                            <td class="btn"><button class="line-btn delete" data-login="${user.nickname}">Delete</button></td>
                        <tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <div id="block-screen-users" class="block-screen">
        <jsp:include page="_newUser.jsp"/>
    </div>
</body>
</html>