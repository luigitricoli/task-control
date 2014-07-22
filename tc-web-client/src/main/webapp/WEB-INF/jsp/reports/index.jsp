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
    
    <script type="text/javascript">
		var DOMAIN='<c:url value="/"/>';
    </script>

    <script src="<c:url value="/resources/stringUtils.js"/>"></script>
    <script src="<c:url value="/resources/dateUtils.js"/>"></script>
    <script src="<c:url value="/resources/user.js"/>"></script>
    <script src="<c:url value="/resources/reports.js"/>"></script>

    <%--
    <c:if test="${sessionUser.admin}">
        <!-- 
        Load Custom scripts
        <script src="<c:url value="/resources/"/>"></script>
        -->
    </c:if>
--%>
        
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/easydropdown.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/calendar.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/main.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/noty_theme.css"/>">
    
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/report.css"/>">
</head>
<body>
    <header id="header">
        <h1>TaskControl<span class="sub1">beta</span><span class="sub2">2</span></h1>
        <div id="main-navigation-panel">
            <a href="<c:url value="/tarefas"/>" class="main-navigation-item">Calendário</a>
            <span class="main-navigation-item-current">Relatórios</span>
            <a href="<c:url value="/usuarios"/>" class="main-navigation-item">Usuários</a>
        </div>        
        <jsp:include page="../_user.jsp"/>
    </header>
    <section id="main">
        <header id="top-main">
            
        </header>
        <div id="container-left">
            <jsp:include page="_report-options.jsp"/>
        </div>        
        <div id="container-right">
            <div id="report-area">
                <span>Utilize o menu ao lado para solicitar o relatório</span>
            </div>
        </div>
    </section>        
        
    <div id="block-screen" class="block-screen">
        <jsp:include page="../_changePass.jsp"/>
    </div>
</body>
</html>