<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<div class="task start-day-${task.startDay}">
    <div class="foreseen interval-day-${task.daysInterval}">
        <div class="stage ${fn:toLowerCase(task.stage)} interval-day-${task.daysRun}"></div>
        <span class="task-description">${task.description}</span>
    </div>
    <jsp:include page="_days_infos.jsp" />
</div>