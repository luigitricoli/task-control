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
		var domain='<c:url value="/"/>';
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
                <h4>Sistema</h4>
                <div class="constraint">
                    <input type="checkbox">
                    <label>GOL</label>
                </div>
                <div class="constraint">
                    <input type="checkbox">
                    <label>EMA</label>
                </div>
                <div class="constraint">
                    <input type="checkbox">
                    <label>OLM</label>
                </div>
            </div>              
            <div class="filter-group">
                <h4>Status</h4>
                <div class="constraint">
                    <input type="checkbox">
                    <label>Andiantado</label>
                </div>
                <div class="constraint">
                    <input type="checkbox">
                    <label>No Prazo</label>
                </div>
                <div class="constraint">
                    <input type="checkbox">
                    <label>Atrasado</label>
                </div>
            </div>
            <div class="filter-group">
                <h4>Tipo da Demanda</h4>
                <div class="constraint">
                    <input type="checkbox">
                    <label>CCC</label>
                </div>
                <div class="constraint">
                    <input type="checkbox">
                    <label>Interna</label>
                </div>
                <div class="constraint">
                    <input type="checkbox">
                    <label>Suporte a produ&ccedil;&atilde;o</label>
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
                <h3>Hist&oacute;rico de Itera&ccedil;&otilde;es</h3>
                <p id="task-description">SR123456789 - Beef ribs chicken tail boudin.</p>
                <div id="iteraction-menu">
                    <a href="" id="finish" class="btn green"><span class="icon add">V</span>Encerrar</a>
                    <a href="" id="replan" class="btn green"><span class="icon add">R</span>Replanejar</a>
                    <a href="" id="cancel" class="btn red"><span class="icon add">X</span>Cancelar</a>
                </div>
                <div id="iteraction-form">
                    <label>No que voc&ecirc; est&aacute; pensando?</label>
                    <textarea id="comantary"></textarea>
                    <button id="add_comentary" class="btn green"><span class="icon add">+</span></button>
                    <button id="remove_comentary" class="btn red"><span class="icon remove">X</span></button>
                </div>
                <div id="timeline">
                    <div class="post">
                        <span class="datetime">08/12/2013 12:34:50</span>
                        <div class="timeline_dot">
                            <div class="dot"></div>
                            <div class="speak_ballon">
                                <div class="arrow"></div>
                                <p class="speaker">Luigi:</p>
                                <p class="message">In&iacute;cio esta tarefa com Rodrigo e Kaue</p>
                            </div>
                        </div>
                    </div>
                    <div class="post">
                        <span class="datetime">08/12/2013 12:34:50</span>
                        <div class="timeline_dot">
                            <div class="dot"></div>
                            <div class="speak_ballon">
                                <div class="arrow"></div>
                                <p class="speaker">Luigi:</p>
                                <p class="message">Beef ribs chicken tail boudin pork chop filet mignon kevin chuck.Beef ribs chicken tail boudin pork chop filet mignon kevin chuck.Beef ribs chicken tail boudin pork chop filet mignon kevin chuck.Beef ribs chicken tail boudin pork chop filet mignon kevin chuck.Beef ribs chicken tail boudin pork chop filet mignon kevin chuck.Beef ribs chicken tail boudin pork chop filet mignon kevin chuck.Beef ribs chicken tail boudin pork chop filet mignon kevin chuck.Beef ribs chicken tail boudin pork chop filet mignon kevin chuck.Beef ribs chicken tail boudin pork chop filet mignon kevin chuck.Beef ribs chicken tail boudin pork chop filet mignon kevin chuck.</p>
                            </div>
                        </div>
                    </div>
                    <div class="post">
                        <span class="datetime">08/12/2013 12:34:50</span>
                        <div class="timeline_dot">
                            <div class="dot"></div>
                            <div class="speak_ballon">
                                <div class="arrow"></div>
                                <p class="speaker">Luigi:</p>
                                <p class="message">Beef ribs chicken tail boudin pork chop filet mignon kevin chuck.</p>
                            </div>
                        </div>
                    </div>
                    <div class="post late">
                        <span class="datetime">08/12/2013 12:34:50</span>
                        <div class="timeline_dot">
                            <div class="dot"></div>
                            <div class="speak_ballon">
                                <div class="arrow"></div>
                                <p class="speaker">Luigi:</p>
                                <p class="message">Altera&ccedil;&atilde;o de escopo. <span>#atraso</span></p>
                            </div>
                        </div>
                    </div>
                    <div class="post">
                        <span class="datetime">08/12/2013 12:34:50</span>
                        <div class="timeline_dot">
                            <div class="dot"></div>
                            <div class="speak_ballon">
                                <div class="arrow"></div>
                                <p class="speaker">Luigi:</p>
                                <p class="message">Beef ribs chicken tail boudin pork chop filet mignon kevin chuck.</p>
                            </div>
                        </div>
                    </div>
                    <div class="post overtime">
                        <span class="datetime">08/12/2013 12:34:50</span>
                        <div class="timeline_dot">
                            <div class="dot"></div>
                            <div class="speak_ballon">
                                <div class="arrow"></div>
                                <p class="speaker">Luigi:</p>
                                <p class="message">2 <span>#horasextra</span></p>
                            </div>
                        </div>
                    </div>
                    <div class="post">
                        <span class="datetime">08/12/2013 12:34:50</span>
                        <div class="timeline_dot">
                            <div class="dot"></div>
                            <div class="speak_ballon">
                                <div class="arrow"></div>
                                <p class="speaker">Luigi:</p>
                                <p class="message">Beef ribs chicken tail boudin pork chop filet mignon kevin chuck.</p>
                            </div>
                        </div>
                    </div>
                    <div class="post">
                        <span class="datetime">08/12/2013 12:34:50</span>
                        <div class="timeline_dot">
                            <div class="dot"></div>
                            <div class="speak_ballon">
                                <div class="arrow"></div>
                                <p class="speaker">Luigi:</p>
                                <p class="message">Beef ribs chicken tail boudin pork chop filet mignon kevin chuck.</p>
                            </div>
                        </div>
                    </div>                    
                </div>
            </div>
        </div>
        <script type="text/javascript">
            $("#menu").menu();
        </script>
    </section>        
</body>
</html>