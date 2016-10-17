<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="filter-group">
    <h4>Application</h4>
    <c:if test="${sessionUser.admin}">
        <c:forEach var="app" items="${applications.getValues()}">
            <div class="constraint">
                <input type="radio" class="app"
                 data-filter="${app}"
                 id="chb-${app}"
                 <c:if test="${applications.selected eq app}">
                    checked
                 </c:if>
                 name="applications">
                <label for="chb-${app}" data-filter="${app}">${applications.getLabel(app)}</label>
            </div>
        </c:forEach>
    </c:if>

    <c:if test="${not sessionUser.admin}">
        <c:forEach var="app" items="${sessionUser.user.systems}">
            <div class="constraint">
                <input type="checkbox" data-filter="${app}" id="chb-${app}">
                <label for="chb-${app}" data-filter="${app}">${applications.getLabel(app)}</label>
            </div>
        </c:forEach>
    </c:if>
</div>
<c:if test="${sessionUser.admin}">
    <div class="filter-group" id="user-filter-container">
        <script type="text/javascript">
            getUsers();
        </script>
    </div>
</c:if>
<div class="filter-group">
    <h4>Status</h4>
    <div class="constraint">
        <input type="checkbox" data-filter="finished" id="chb-finished">
        <label for="chb-finished" data-filter="finished">Finished</label>
    </div>
    <div class="constraint">
        <input type="checkbox" data-filter="doing" id="chb-doing">
        <label for="chb-doing" data-filter="doing">In Progress</label>
    </div>
    <div class="constraint">
        <input type="checkbox" data-filter="late" id="chb-late">
        <label for="chb-late" data-filter="late">Late</label>
    </div>
</div>
<div class="filter-group">
    <h4>Demand type</h4>
    <c:forEach var="source" items="${sources.getValues()}">
        <div class="constraint">
            <input type="checkbox" data-filter="${source}" id="chb-${source}">
            <label for="chb-${source}" data-filter="${source}">${sources.getLabel(source)}</label>
        </div>
    </c:forEach>
</div>
<!--
<div class="filter-group">
    <h4>Respons&aacute;vel</h4>
    <div class="constraint">
        <input type="radio" name="user" checked>
        <label>Todos</label>
    </div>
    <div class="constraint">
        <input type="radio" name="user">
        <label>William</label>
    </div>
    <div class="constraint">
        <input type="radio" name="user">
        <label>Rodrigo</label>
    </div>
    <div class="constraint">
        <input type="radio" name="user">
        <label>Kaue</label>
    </div>
</div>
-->