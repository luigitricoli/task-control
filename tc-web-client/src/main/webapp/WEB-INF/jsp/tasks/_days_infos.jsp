<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:forEach var="day" items="${task.daysInfo}">
    <div class="hashtags task-day-${day}">
        <c:forEach var="hashtag" items="${task.getHashtagsBy(day)}">
            <span class="${fn:toLowerCase(hashtag)}">&#35;${fn:toLowerCase(hashtag)}</span>
        </c:forEach>
    </div>
</c:forEach>