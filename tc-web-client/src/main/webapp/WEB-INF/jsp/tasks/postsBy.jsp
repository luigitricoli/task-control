<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<h3>Hist&oacute;rico de Itera&ccedil;&otilde;es</h3>
<p id="task-description"></p>
<div id="iteraction-menu">
    <a href="#" id="finish" class="btn green"><span class="icon add">V</span>Encerrar</a>
    <a href="#" id="replan" class="btn green"><span class="icon add">R</span>Replanejar</a>
    <a href="#" id="cancel" class="btn red"><span class="icon add">X</span>Cancelar</a>
</div>
<div id="iteraction-form">
    <form>
        <label>No que voc&ecirc; est&aacute; pensando?</label>
        <textarea id="comentary" name="text"></textarea>
        <button id="add_comentary" type="button" class="btn green"><span class="icon add">+</span></button>
        <button id="remove_comentary" type="reset" class="btn red"><span class="icon remove">X</span></button>
    </form>
</div>
<div id="timeline">
    <c:forEach var="post" items="${posts}" varStatus="status">
        <div class="post">
            <span class="datetime"><fmt:formatDate value="${post.time.time}" pattern="dd/MM/yyyy HH:mm:ss" /></span>

            <div class="timeline_dot">
                <div class="dot"></div>
                <div class="speak_ballon">
                    <div class="arrow"></div>
                    <p class="speaker">${post.user}:</p>
                    <p class="message">${post.textHtml}</p>
                </div>
            </div>
        </div>
    </c:forEach>
</div>
