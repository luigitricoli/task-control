<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${user.logged}">
    <div id="user">
        <select id="user-info" class="dropdown" onchange="userInfoActions(this)">
            <option value="" class="label">${user.nickname}</option>
            <!--
            <option value="my data">Meus Dados</option>
            <option value="help">Ajuda</option>
            -->
            <option value="logout">Logout</option>
        </select>
    </div>
</c:if>