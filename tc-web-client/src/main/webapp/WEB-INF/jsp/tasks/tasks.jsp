<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
                    <a id="btn-task-history" href="#task-history"></a>
                    <c:forEach var="week" items="${weeks}" varStatus="status">
	                    <div id="task-week-${status.count}" class="week">
	  						<c:forEach var="_task" items="${week}">
                                    <c:set var="task" value="${_task}" scope="request"/>
                                    <c:choose>
                                        <c:when test="${task.stage eq 'LATE'}">
                                            <jsp:include page="_late.jsp"/>
                                        </c:when>
                                        <c:otherwise>
                                            <jsp:include page="_default.jsp"/>
                                        </c:otherwise>
                                    </c:choose>
							</c:forEach>
	                    </div>
	                </c:forEach>