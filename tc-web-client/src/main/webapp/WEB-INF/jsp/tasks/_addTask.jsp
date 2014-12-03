<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="add-task-container" class="float-window addTaskWindow">
    <form id="add-task-form">
        <h4>Nova Tarefa</h4>
        <div class="alert begin">
            <p></p>
        </div>
        <div class="field half-left">
            <label>In&iacute;cio</label>
            <input type="text" name="start" id="startDay" class="startDay" required>
        </div>
        <div class="field half-right">
            <label>Fim</label>
            <input type="text" name="foreseen" id="foreseenDay" class="foreseenDay" required>
        </div>
        <div class="field half-left">
            <label>Tipo</label>
            <select name="type">
                <c:forEach var="source" items="${sources.getValues()}">
                    <option value="${source}">${sources.getLabel(source)}</option>
                </c:forEach>
            </select>
        </div>
        <div class="field half-right">
            <label>Sistema</label>
            <select name="system">
                <c:forEach var="system" items="${sessionUser.user.systems}">
                    <option value="${system}">${applications.getLabel(system)}</option>
                </c:forEach>
            </select>
        </div>
        <div class="field">
            <label>Identificador</label>
            <select name="idType" id="id-type">
                <option value="DM">DM</option>
                <option value="SDN">SDN</option>
                <option value="TIR">TIR</option>
                <option value="INT">INT</option>
            </select>
            <input type="text" name="idValue" id="id-value" required>
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
            <span>${sessionUser.user.nickname}</span>
            <input type="text" name="owners[]" value="${sessionUser.user.nickname}" style="display:none" >
        </div>
        <div class="field btn-container">
            <button type="reset" id="cancel-register-btn" class="btn red"><span class="icon add">X</span>Cancelar</button>
            <button type="button" id="salve-register-btn" class="btn green"><span class="icon add">V</span>Salvar</button>
        </div>
    </form>
</div>
