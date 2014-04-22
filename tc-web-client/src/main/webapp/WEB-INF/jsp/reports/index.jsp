<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/main.css"/>">
</head>
<body>
    <header id="header">
        <h1>TaskControl<span class="sub1">beta</span><span class="sub2">1</span></h1>
        <jsp:include page="../_user.jsp"/>
    </header>
    <section id="main">
        <h1>Relatório Mensal de Projetos</h1>
        <form method="get" action='<c:url value="/relatorios/mensalprojetos"/>'>
            <input type="text" name="month" size="3" maxlength="2" />
            <span>/</span>
            <input type="text" name="year" size="5" maxlength="4" />
            <span>&nbsp;&nbsp;</span>
            <input type="submit" value="Visualizar" />
        </form>
    </section>
</body>
</html>