<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="filter-group">
    <h4>Sistema</h4>
    <c:forEach var="system" items="${sessionUser.systems}">
        <div class="constraint">
            <input type="checkbox" data-filter="${system}" id="chb-${system}">
            <label for="chb-${system}" data-filter="${system}">${system}</label>
        </div>
    </c:forEach>
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
        <input type="checkbox" data-filter="CCC" id="chb-CCC">
        <label for="chb-CCC" data-filter="CCC">CCC</label>
    </div>
    <div class="constraint">
        <input type="checkbox" data-filter="interna" id="chb-interna">
        <label for="chb-interna" data-filter="interna">Interna</label>
    </div>
    <div class="constraint">
        <input type="checkbox" data-filter="sup-prod" id="chb-sup-prod">
        <label for="chb-sup-prod" data-filter="sup-prod">Suporte a produ&ccedil;&atilde;o</label>
    </div>
</div>
<!--
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
-->