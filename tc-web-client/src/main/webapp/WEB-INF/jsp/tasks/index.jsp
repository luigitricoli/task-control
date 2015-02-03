<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html>
<head>
    <script type="text/javascript">
        var OPEN_TASK = undefined;
        <c:if test="${openTask ne null}">
            OPEN_TASK="<c:out value="${openTask}" />";
        </c:if>
	</script>
    <script src="/tc-web-client/resources/task.js"></script>
    <script src="/tc-web-client/resources/new_calendar.js"></script>
</head>
<body>
    <header id="top-main">
        <a href="#" id="btn_new" class="btn green"><span class="icon add">+</span>Nova Tarefa</a>
    </header>
    <div id="container-left">
        <jsp:include page="_filters.jsp"/>
    </div>
    <div id="container-right">
        <div id="navigation">
            <h5 id="task-amount"></h5>
            <div id="paginate">
                <a id="previous-month" href="#btn_new" class="btn green previous">&lt;</a>
                <a id="next-month" href="#btn_new" class="btn green next">&gt;</a>
            </div>
            <h3 id="calendar-month-label"></h3>
        </div>
        <div id="tasks-in-calendar">
            <div id="task-board-scroll">
                <div id="task-board">
                </div>
            </div>
        </div>
        <div id="task-history">
        </div>
    </div>
    <div id="block-screen-tasks" class="block-screen">
        <jsp:include page="_addTask.jsp"/>
    </div>
    <a id="btn-task-history" href="#task-history" ></a>
</body>
</html>