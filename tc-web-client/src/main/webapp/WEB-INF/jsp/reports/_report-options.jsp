<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<form id="report-options-form">
    <div class="filter-group">
        <h4>Relatório</h4>
        <select id="cboReportSelection">
            <option value="">Selecione...</option>
            <option value="taskList">Listagem de Tarefas</option>
        </select>
    </div>
    
    <div class="filter-group" id="reportParamGroup-yearMonth">
        <label>Mês/Ano</label>
        <select id="cboReportMonth" name="month">
            <option value="1">Janeiro</option>
            <option value="2">Fevereiro</option>
            <option value="3">Março</option>
            <option value="4">Abril</option>
            <option value="5">Maio</option>
            <option value="6">Junho</option>
            <option value="7">Julho</option>
            <option value="8">Agosto</option>
            <option value="9">Setembro</option>
            <option value="10">Outubro</option>
            <option value="11">Novembro</option>
            <option value="12">Dezembro</option>
        </select>
        <select id="cboReportYear" name="year">
            <c:forEach begin="2000" end="2030" var="yearOption">
                <option value="${yearOption}">${yearOption}</option>
            </c:forEach>
        </select>
    </div>
    
    <div class="filter-group" id="reportSubmit">
        <a href="#" id="btSubmitReport" class="btn green">Solicitar Relatório</a>
    </div>
</form>