<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="filter-group">
    <h4>Application</h4>
    <div class="constraint">
        <input type="radio" class="all"
         data-filter="all"
         id="chb-all"
         checked
         name="applications">
        <label for="chb-all" data-filter="all">All</label>
    </div>
    <c:forEach var="app" items="${applications.getValues()}">
    <!--
        <div class="constraint">
            <input type="radio" class="app"
             data-filter="${app}"
             id="chb-${app}"
             name="applications">
            <label for="chb-${app}" data-filter="${app}">${applications.getLabel(app)}</label>
        </div>
     -->
    </c:forEach>
</div>