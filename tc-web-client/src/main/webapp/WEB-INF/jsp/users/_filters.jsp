<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="filter-group">
    <h4>Sistema</h4>
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
</div>