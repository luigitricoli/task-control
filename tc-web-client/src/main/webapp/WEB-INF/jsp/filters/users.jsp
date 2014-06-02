<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h4>Usu&aacute;rios</h4>
<c:forEach var="user" items="${users}">
    <div class="constraint">
        <input type="checkbox" data-filter="${user.nickname}" id="chb-${user.nickname}">
        <label for="chb-${user.nickname}" data-filter="${user.nickname}">${user.name}</label>
    </div>
</c:forEach>