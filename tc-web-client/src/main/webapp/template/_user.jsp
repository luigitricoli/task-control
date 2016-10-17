<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${sessionUser.logged}">
    <div id="user">
        <select id="user-info" class="dropdown">
            <option value="" id="nickName" class="label">${sessionUser.user.nickname}</option>
            <!--
            <option value="my data">Meus Dados</option>
            <option value="help">Ajuda</option>
            -->
            <option id="change-pass" value="changePass" data-login="${sessionUser.user.nickname}">Change Password</option>
            <option value="logout">Logout</option>
        </select>
    </div>
</c:if>
