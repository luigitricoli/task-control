<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${sessionUser.logged}">
    <div id="user">
        <select id="user-info" class="dropdown" onchange="userInfoActions(this)">
            <option value="" id="nickName" class="label">${sessionUser.user.nickname}</option>
            <!--
            <option value="my data">Meus Dados</option>
            <option value="help">Ajuda</option>
            -->
            <option value="changePass">Alterar Senha</option>
            <c:if test="${sessionUser.admin}">
                <option value="newUser">Novo Usu&aacute;rio</option>
            </c:if>
            <option value="logout">Logout</option>
        </select>
    </div>
</c:if>