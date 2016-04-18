<%@ page pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<div class="reportTitle">
    <h1>Relatório Mensal ${param.monthName}/${param.year}</h1>
</div>

<display:table name="tasks" export="true" requestURI="/relatorios/listaTarefas"
               pagesize="20" id="task">
    <%@include file="_displayTag-setup.jsp" %>
    
    <display:column title="ID" property="theId" sortable="true" />
    <display:column title="Demanda/Atividade" property="theDescription" sortable="true" value="${description.split('/-(.+)?/')[1]}"/>
    <display:column title="Sistema" property="formattedApplication" sortable="true" />
    <display:column title="Origem" property="source" sortable="true" /> 
    <display:column title="Data Início" sortProperty="startDate" sortable="true">
        <fmt:parseDate pattern="yyyy-MM-dd" type="date" var="startDate" value="${task.startDate}" />
        <fmt:formatDate pattern="dd/MM/yyyy" value="${startDate}" />
    </display:column>
    <display:column title="Data Prevista" sortProperty="foreseenEndDate" sortable="true"> 
        <fmt:parseDate pattern="yyyy-MM-dd" type="date" var="foreseenEndDate" value="${task.foreseenEndDate}" />
        <fmt:formatDate pattern="dd/MM/yyyy" value="${foreseenEndDate}" />
    </display:column>
    <display:column title="Data Fim" sortProperty="endDate" sortable="true">
        <c:if test="${task.endDate ne null}">
            <fmt:parseDate pattern="yyyy-MM-dd" type="date" var="endDate" value="${task.endDate}" />
            <fmt:formatDate pattern="dd/MM/yyyy" value="${endDate}" />
        </c:if>
        <c:if test="${task.endDate eq null}">
            -
        </c:if>
    </display:column>
    <display:column title="Responsáveis" property="owners" sortable="true" />
    <display:column title="Status" property="status" sortable="true" />   
</display:table>
<script lang="javascript">
    customizeTableLinks();
</script>