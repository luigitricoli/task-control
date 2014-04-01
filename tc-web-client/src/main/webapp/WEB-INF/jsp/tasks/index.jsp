<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html>
<head>
    <script src="<c:url value="/resources/jquery-1.10.2.min.js"/>"></script>
    <script src="<c:url value="/resources/jquery.cookie.js"/>"></script>
    <script src="<c:url value="/resources/jquery-ui-1.10.4.custom.min.js"/>"></script>
    <script src="<c:url value="/resources/jquery.easydropdown.min.js"/>"></script>
    <script src="<c:url value="/resources/jquery.mask.min.js"/>"></script>
    <script src="<c:url value="/resources/user.js"/>"></script>
    <script src="<c:url value="/resources/calendar.js"/>"></script>
    <script type="text/javascript">
		var DOMAIN='<c:url value="/"/>';
        var OPEN_TASK = undefined;
        <c:if test="${openTask ne null}">
            OPEN_TASK="<c:out value="${openTask}" />";
        </c:if>
	</script>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/easydropdown.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/calendar.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/main.css"/>">
</head>
<body>
    <header id="header">
        <h1>TaskControl<span class="sub1">beta</span><span class="sub2">1</span></h1>
        <jsp:include page="../_user.jsp"/>
    </header>
    <section id="main">
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
                <h3 id="calendar-month-label">Atual</h3>
            </div>
            <div id="tasks-in-calendar">
                <table id="calendar-layer">
                    <thead>
                        <tr class="subline">
                            <th>Dom</th>
                            <th>Seg</th>
                            <th>Ter</th>
                            <th>Qua</th>
                            <th>Qui</th>
                            <th>Sex</th>
                            <th>S&aacute;b</th>
                        </tr>
                    </thead>        
                    <tr id="calendar-week-1" class="subline days">
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td></td>
                    </tr>
                    <tr id="calendar-week-2" class="subline days border-top">
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td>29</td>
                    </tr>
                    <tr id="calendar-week-3" class="subline days border-top">
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td>16</td>
                    </tr>
                    <tr id="calendar-week-4" class="subline days border-top">
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td>23</td>
                    </tr>
                    <tr id="calendar-week-5" class="subline days border-top">
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td>30</td>
                    </tr>
                    <tr id="calendar-week-6" class="subline days border-top">
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td class="border-right"></td>
                        <td>7</td>
                    </tr>
                </table>

                <div id="tasks-layer">

                </div>
            </div>
            <div id="task-history">
            </div>
        </div>
        <div id="block-screen">
            <jsp:include page="_addTask.jsp"/>
        </div>
        <script type="text/javascript">
            $("#menu").menu();
        </script>
    </section>        
</body>
</html>