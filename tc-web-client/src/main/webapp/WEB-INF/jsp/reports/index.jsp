<%@ page pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html>
<head>    
    <script src="<c:url value="/resources/report.js"/>"></script> 
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/report.css"/>">
</head>
<body>
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
</body>
</html>