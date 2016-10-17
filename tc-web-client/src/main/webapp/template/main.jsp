<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html>
<head>
    <script src="<c:url value="/resources/jquery-1.10.2.min.js"/>"></script>
    <script src="<c:url value="/resources/jquery.cookie.js"/>"></script>
    <script src="<c:url value="/resources/jquery-ui-1.10.4.custom.min.js"/>"></script>
    <script src="<c:url value="/resources/jquery.easydropdown.min.js"/>"></script>
    <script src="<c:url value="/resources/jquery.noty.packaged.min.js"/>"></script>
    <script src="<c:url value="/resources/jquery.mask.min.js"/>"></script>
    <script src="<c:url value="/resources/stringUtils.js"/>"></script>
    <script src="<c:url value="/resources/dateUtils.js"/>"></script>
    <script src="<c:url value="/resources/user.js"/>"></script>
    <script src="<c:url value="/resources/calendar.js"/>"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/easydropdown.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/calendar.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/main.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/noty_theme.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/jquery-ui.css"/>">
    <c:if test="${sessionUser.admin}">
        <script src="<c:url value="/resources/adminFilters.js"/>"></script>
    </c:if>
    <script type="text/javascript">
		var DOMAIN='<c:url value="/"/>';
	</script>

    <sitemesh:write property='head'/>
    <title>TaskControl</title>
</head>
<body>
    <header id="header">
        <h1>TaskControl<span class="sub1">RC</span><span class="sub2">1</span></h1>
        <c:if test="${sessionUser.admin}">
            <ul id="top-menu">
                <li><a href="<c:url value="/tarefas"/>">Calendar</a></li>
                <li class="separator">|</li>
                <li><a href="<c:url value="/usuarios"/>">Users</a></li>
                <li class="separator">|</li>
                <li><a href="<c:url value="/relatorios"/>">Reports</a></li>
            </ul>
        </c:if>
        <jsp:include page="_user.jsp"/>
    </header>
    <section id="main">
        <sitemesh:write property='body'/>
        <div id="block-screen-main" class="block-screen">
            <jsp:include page="_changePass.jsp"/>
        </div>
    </section>        
</body>
</html>