<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<div id="replan-task-container" class="float-window">
    <form id="replan-task-form">
        <input id="replan-id" type="hidden" name="id" value="${task.id}">
        <h4>Replan</h4>
        <div class="alert begin">
            <p></p>
        </div>
        <div class="field half-left">
            <label>Start</label>
            <input id="replan-start" type="text" name="start" class="startDay" value="${task.startDateAsString}" disabled>
            <span class="format">dd/mm/aa</span>
        </div>
        <div class="field half-right">
            <label>Forecast</label>
            <input id="replan-foreseen" type="text" name="foreseen" class="foreseenDay" required>
            <span class="format">dd/mm/aa</span>
        </div>
        <div class="field">
            <button type="reset" id="cancel-replan-btn" class="btn red"><span class="icon add">X</span>Cancel</button>
            <button type="button" id="salve-replan-btn" class="btn green"><span class="icon add">V</span>Save</button>
        </div>
    </form>
</div>
