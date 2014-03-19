<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html>
<head>
    <script src="<c:url value="/resources/jquery-1.10.2.min.js"/>"></script>
    <script src="<c:url value="/resources/jquery.cookie.js"/>"></script>
    <script src="<c:url value="/resources/jquery-ui.js"/>"></script>
    <script src="<c:url value="/resources/jquery.easydropdown.min.js"/>"></script>
    <script src="<c:url value="/resources/user.js"/>"></script>
    <script type="text/javascript">
        var DOMAIN='<c:url value="/"/>';
    </script>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/easydropdown.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/main.css"/>">
</head>
<body>
    <header id="header">
        <h1>TaskControl</h1>
        <jsp:include page="../_user.jsp"/>
    </header>
    <section id="main">
        <div id="login-container">
            <form action="<c:url value='/login'/>" method="POST">
                <label>Login</label>
                <input type="text" name="nickname">
                <label>Senha</label>
                <input type="password" name="pass">
                <button id="login-btn" type="submit" class="btn green">Entrar</button>
            </form>
        </div>
    </section>
</body>
</html>