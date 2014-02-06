<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html>
<head>
    <script src="<c:url value="/resources/jquery-1.10.2.min.js"/>"></script>
    <script src="<c:url value="/resources/jquery.cookie.js"/>"></script>
    <script src="<c:url value="/resources/jquery-ui.js"/>"></script>
    <script src="<c:url value="/resources/jquery.easydropdown.min.js"/>"></script>
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
        <h1>TaskControl</h1>
        <div id="user">
            <select class="dropdown">
                <option value="" class="label">luigitricoli</option>
                <option value="">Meus Dados</option>
                <option value="">Ajuda</option>
                <option value="">Logout</option>
            </select>
        </div>
    </header>
    <section id="main">
        <header id="top-main">
            <a href="" id="btn_new" class="btn green"><span class="icon add">+</span>Nova Tarefa</a>
        </header>
        <div id="container-left">
            <div class="filter-group">
                <form>
                    <h4>Sistema</h4>
                    <div class="constraint">
                        <input type="checkbox" data-filter="GOL" id="chb-GOL">
                        <label for="chb-GOL" data-filter="GOL">GOL</label>
                    </div>
                    <div class="constraint">
                        <input type="checkbox" data-filter="EMA" id="chb-EMA">
                        <label for="chb-EMA" data-filter="EMA">EMA</label>
                    </div>
                    <div class="constraint">
                        <input type="checkbox" data-filter="OLM" id="chb-OLM">
                        <label for="chb-OLM" data-filter="OLM">OLM</label>
                    </div>
                </form>
            </div>
            <div class="filter-group">
                <h4>Status</h4>
                <div class="constraint">
                    <input type="checkbox" data-filter="finished" id="chb-finished">
                    <label for="chb-finished" data-filter="finished">Finalizado</label>
                </div>
                <div class="constraint">
                    <input type="checkbox" data-filter="doing" id="chb-doing">
                    <label for="chb-doing" data-filter="doing">Em andamento</label>
                </div>
                <div class="constraint">
                    <input type="checkbox" data-filter="late" id="chb-late">
                    <label for="chb-late" data-filter="late">Atrasado</label>
                </div>
            </div>
            <div class="filter-group">
                <h4>Tipo da Demanda</h4>
                <div class="constraint">
                    <input type="checkbox" data-filter="ccc" id="chb-ccc">
                    <label for="chb-ccc" data-filter="ccc">CCC</label>
                </div>
                <div class="constraint">
                    <input type="checkbox" data-filter="internal" id="chb-internal">
                    <label for="chb-internal" data-filter="internal">Interna</label>
                </div>
                <div class="constraint">
                    <input type="checkbox" data-filter="sup-prod" id="chb--sup-prod">
                    <label for="chb--sup-prod" data-filter="sup-prod">Suporte a produ&ccedil;&atilde;o</label>
                </div>
            </div>
            <div class="filter-group">
                <h4>Respons&aacute;vel</h4>
                <div class="constraint">
                    <input type="radio" name="user" checked>
                    <label>Todos</label>
                </div>                
                <div class="constraint">
                    <input type="radio" name="user">
                    <label>William</label>
                </div>
                <div class="constraint">
                    <input type="radio" name="user">
                    <label>Rodrigo</label>
                </div>
                <div class="constraint">
                    <input type="radio" name="user">
                    <label>Kaue</label>
                </div>
            </div>                     
        </div>        
        <div id="container-right">
            <div id="navigation">
                <div id="paginate">
                    <a id="previous-month" href="#" class="btn green previous">&lt;</a>
                    <a id="next-month" href="#" class="btn green next">&gt;</a>
                </div>
                <h3>Atual</h3>
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
        <script type="text/javascript">
            $("#menu").menu();
        </script>
    </section>        
</body>
</html>