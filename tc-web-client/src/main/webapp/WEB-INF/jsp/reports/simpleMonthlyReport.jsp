<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<!DOCTYPE HTML>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/main.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/report.css"/>">
</head>
<body>
    <header id="header">
        <h1>TaskControl<span class="sub1">beta</span><span class="sub2">1</span></h1>
        <jsp:include page="../_user.jsp"/>
    </header>
    <section id="main">
        <h1>Relatório Mensal de Projetos</h1>

        <display:table name="items" export="true">
            <display:column title="Recurso" property="user" />
            <display:column title="Sistema" property="application" />
            <display:column title="Horas/Mês" property="hoursInMonth" />
            <display:column title="Projeto" property="taskDescription" />
        </display:table>

        <a href="<c:url value="/relatorios"/>">Voltar</a>
    </section>
</body>
</html>