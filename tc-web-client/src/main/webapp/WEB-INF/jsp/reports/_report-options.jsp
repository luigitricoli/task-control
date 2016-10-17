<%@ page pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<form id="report-options-form">
    <div class="filter-group">
        <h4>Type</h4>

        <div class="constraint">
            <input type="radio" id="optReportType-taskList" name="optReportType" value="taskList" />
            <label for="optReportType-taskList">Monthly Report</label>
        </div>

        <div class="constraint">
            <input type="radio" id="optReportType-dailyActivities" name="optReportType" value="dailyActivities" />
            <label for="optReportType-dailyActivities">Daily Activities</label>
        </div>
    </div>
    
    <div class="filter-group" id="reportParamGroup-yearMonth">
        <label class="reportFilterLabel">Period</label>
        <select id="cboReportMonth" name="month">
            <option value="1">January</option>
            <option value="2">February</option>
            <option value="3">March</option>
            <option value="4">April</option>
            <option value="5">May</option>
            <option value="6">June</option>
            <option value="7">July</option>
            <option value="8">August</option>
            <option value="9">September</option>
            <option value="10">October</option>
            <option value="11">November</option>
            <option value="12">December</option>
        </select>
        <input type="hidden" id="hidReportMonthName" name="monthName"></input>
        <select id="cboReportYear" name="year">
            <c:forEach begin="2000" end="2030" var="yearOption">
                <option value="${yearOption}">${yearOption}</option>
            </c:forEach>
        </select>
    </div>

    <div class="alert"></div>

    <div class="filter-group" id="reportParamGroup-date">
        <label class="reportFilterLabel">Date</label>
        <input id="txtReportDate" name="date" />
    </div>
    
    <div class="filter-group" id="reportSubmit">
        <a href="#" id="btSubmitReport" class="btn-no-icon green">View</a>
    </div>

</form>