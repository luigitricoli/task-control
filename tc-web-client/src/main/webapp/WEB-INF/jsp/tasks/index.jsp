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
            <div id="paginate">
                <a id="previous-month" href="#" class="btn green previous">&lt;</a>
                <a id="next-month" href="#" class="btn green next">&gt;</a>
            </div>
            <h3 id="calendar-month-label"></h3>
        </div>
        <div id="tasks-in-calendar">
            <div id="task-board">
                <div class="day">
                    <h4 class="day-label">1, Qui</h4>
                </div>
                <div class="day">
                    <h4 class="day-label">2, Sex</h4>
                </div>
            </div>

            <!--
            <div id="tasks-layer">

            </div>
            -->
        </div>
        <div id="task-history">
        </div>
    </div>
    <div id="block-screen-tasks" class="block-screen">
        <jsp:include page="_addTask.jsp"/>
    </div>
</body>
</html>