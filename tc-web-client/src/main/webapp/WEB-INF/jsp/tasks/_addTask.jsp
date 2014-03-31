<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="add-task-container" class="float-window">
    <form id="add-task-form">
        <h4>Nova Tarefa</h4>
        <div class="alert begin">
            <p></p>
        </div>
        <div class="field half-left">
            <label>In&iacute;cio</label>
            <input type="text" name="start" id="startDay" required>
            <span class="format">dd/mm/aa</span>
        </div>
        <div class="field half-right">
            <label>Fim</label>
            <input type="text" name="foreseen" id="foreseenDay" required>
            <span class="format">dd/mm/aa</span>
        </div>
        <div class="field half-left">
            <label>Tipo</label>
            <select name="type">
                <option value="CCC">CCC</option>
                <option value="interna">Interna</option>
                <option value="sup-prod">Sup. Produ&ccedil;&atilde;o</option>
            </select>
        </div>
        <div class="field half-right">
            <label>Sistema</label>
            <select name="system">
                <c:forEach var="system" items="${sessionUser.systems}">
                    <option value="${system}">${system}</option>
                </c:forEach>
            </select>
        </div>
        <div class="field">
            <label>Descri&ccedil;&atilde;o</label>
            <textarea name="description" required></textarea>
        </div>
        <div class="field" style="display:none">
            <label>Respons&aacute;vel</label>
            <input type="text" required>
            <button id="add_comentary" type="button" class="btn green"><span class="icon add">+</span></button>
        </div>
        <div id="users" class="field">
            <label>Respons&aacute;vel</label>
            <span>${sessionUser.nickname}</span>
            <input type="text" name="owners[]" value="${sessionUser.nickname}" style="display:none" >
        </div>
        <div class="field">
            <button type="reset" id="cancel-register-btn" class="btn red"><span class="icon add">X</span>Cancelar</button>
            <button type="button" id="salve-register-btn" class="btn green"><span class="icon add">V</span>Salvar</button>
        </div>
    </form>
</div>
