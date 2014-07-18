<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<h3>Hist&oacute;rico de Itera&ccedil;&otilde;es</h3>
<p id="task-description">${task.description}</p>
<p class="detail-label">Dura&ccedil;&atilde;o prevista de <span>${task.startDateAsString}</span> &agrave; <span>${task.foreseenEndDateAsString}</span></p>
<p class="detail-label">Respons&aacute;veis: <span></span></p>
<div id="iteraction-menu">
    <a id="finish" class="btn green"><span class="icon add">V</span>Encerrar</a>
    <a id="replan" class="btn green"><span class="icon add">R</span>Replanejar</a>
    <a href="#" id="cancel" class="btn red"><span class="icon add">X</span>Cancelar</a>
</div>
<div id="iteraction-form">
    <form>
        <label>No que voc&ecirc; est&aacute; pensando?</label>
        <div class="alert begin">
            <p></p>
        </div>
        <textarea id="comentary" name="text"></textarea>
        <button id="add_comentary" type="button" class="btn green"><span class="icon add">+</span></button>
        <button id="remove_comentary" type="reset" class="btn red"><span class="icon remove">X</span></button>
    </form>
</div>
<div id="timeline">
    <c:forEach var="post" items="${task.posts}" varStatus="status">
        <div class="post">
            <span class="datetime"><fmt:formatDate value="${post.time.time}" pattern="dd/MM/yyyy HH:mm:ss" /></span>

            <div class="timeline_dot">
                <div class="dot"></div>
                <div class="speak_ballon">
                    <div class="arrow"></div>
                    <p class="speaker">${post.name}:</p>
                    <p class="message">${post.textHtml}</p>
                </div>
            </div>
        </div>
    </c:forEach>
</div>
<div id="replan-block-screen" class="block-screen">
    <jsp:include page="_replan.jsp"/>
</div>
