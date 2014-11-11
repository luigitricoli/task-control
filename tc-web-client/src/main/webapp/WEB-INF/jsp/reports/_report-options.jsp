<%@ page pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<form id="report-options-form">
    <div class="filter-group">
        <h4>Tipo</h4>

        <div class="constraint">
            <input type="radio" id="optReportType-taskList" name="optReportType" value="taskList" />
            <label for="optReportType:taskList">Relatório Mensal</label>
        </div>

        <div class="constraint">
            <input type="radio" id="optReportType-dailyActivities" name="optReportType" value="dailyActivities" />
            <label for="optReportType:dailyActivities">Atividades Diárias</label>
        </div>
    </div>
    
    <div class="filter-group" id="reportParamGroup-yearMonth">
        <label class="reportFilterLabel">Período</label>
        <select id="cboReportMonth" name="month">
            <option value="1">Janeiro</option>
            <option value="2">Fevereiro</option>
            <option value="3">Marco</option>
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
        <input type="hidden" id="hidReportMonthName" name="monthName"></input>
        <select id="cboReportYear" name="year">
            <c:forEach begin="2000" end="2030" var="yearOption">
                <option value="${yearOption}">${yearOption}</option>
            </c:forEach>
        </select>
    </div>

    <div class="filter-group" id="reportParamGroup-date">
        <label class="reportFilterLabel">Data</label>
        <input id="txtReportDate" name="date" />
    </div>
    
    <div class="filter-group" id="reportSubmit">
        <a href="#" id="btSubmitReport" class="btn-no-icon green">Visualizar</a>
    </div>

    <div class="alert"></div>
</form>