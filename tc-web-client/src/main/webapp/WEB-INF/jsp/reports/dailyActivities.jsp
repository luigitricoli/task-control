<%@ page pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<div class="reportTitle">
    <h1>Daily Activities Report - ${param.date}</h1>
</div>

<display:table name="tasks" export="true" requestURI="/relatorios/atividadesDiarias"
               pagesize="20" id="task">
    <%@include file="_displayTag-setup.jsp" %>
    
    <display:column title="ID" property="theId" sortable="true" />
    <display:column title="Demand/Activity" property="theDescription" sortable="true" />
    <display:column title="Application" property="formattedApplication" sortable="true" />
    <display:column title="Source" property="source" sortable="true" />
    <display:column title="Start Date" sortProperty="startDate" sortable="true">
        <fmt:parseDate pattern="yyyy-MM-dd" type="date" var="startDate" value="${task.startDate}" />
        <fmt:formatDate pattern="dd/MM/yyyy" value="${startDate}" />
    </display:column>
    <display:column title="Foreseen End Date" sortProperty="foreseenEndDate" sortable="true"> 
        <fmt:parseDate pattern="yyyy-MM-dd" type="date" var="foreseenEndDate" value="${task.foreseenEndDate}" />
        <fmt:formatDate pattern="dd/MM/yyyy" value="${foreseenEndDate}" />
    </display:column>
    <display:column title="End Date" sortProperty="endDate" sortable="true">
        <c:if test="${task.endDate ne null}">
            <fmt:parseDate pattern="yyyy-MM-dd" type="date" var="endDate" value="${task.endDate}" />
            <fmt:formatDate pattern="dd/MM/yyyy" value="${endDate}" />
        </c:if>
        <c:if test="${task.endDate eq null}">
            -
        </c:if>
    </display:column>
    <display:column title="Foreseen Work Hours" property="foreseenWorkHours" sortable="true" />
    <display:column title="Owners" property="owners" sortable="true" />  
    <display:column title="Status" property="status" sortable="true" />  
</display:table>

<script lang="javascript">
    customizeTableLinks();
</script>