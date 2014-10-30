<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<div id="cancel-task-container" class="float-window">
    <form id="cancel-task-form">
        <input id="cancel-id" type="hidden" name="id" value="${task.id}">
        <h4>Remover</h4>
        <div class="info">
            <p>Confirmar remo&ccedil;&atilde;o da tarefa: ${task.description}?</p>
        </div>
        <div class="alert begin">
            <p></p>
        </div>
        <div class="field">
            <button type="reset" id="cancel-cancel-btn" class="btn red"><span class="icon add">X</span>N&atilde;o</button>
            <button type="button" id="salve-cancel-btn" class="btn green"><span class="icon add">S</span>Sim</button>
        </div>
    </form>
</div>
