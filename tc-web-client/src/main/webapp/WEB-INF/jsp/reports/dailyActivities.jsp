<%@ page pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<div class="reportTitle">
    <h1>Relatório Diário de Atividades - ${param.date}</h1>
</div>

<display:table name="tasks" export="true" requestURI="/relatorios/atividadesDiarias"
               pagesize="20" id="task">
    <%@include file="_displayTag-setup.jsp" %>
    
    <display:column title="Reponsável" property="owners" sortable="true" />
    <display:column title="Descrição" property="description" sortable="true" />
    <display:column title="Sistema" property="application" sortable="true" />
    <display:column title="Origem" property="source" sortable="true" />
    <display:column title="Data Início" sortProperty="startDate" sortable="true">
        <fmt:parseDate pattern="yyyy-MM-dd" type="date" var="startDate" value="${task.startDate}" />
        <fmt:formatDate pattern="dd/MM/yyyy" value="${startDate}" />
    </display:column>
    <display:column title="Data Prevista" sortProperty="foreseenEndDate" sortable="true"> 
        <fmt:parseDate pattern="yyyy-MM-dd" type="date" var="foreseenEndDate" value="${task.foreseenEndDate}" />
        <fmt:formatDate pattern="dd/MM/yyyy" value="${foreseenEndDate}" />
    </display:column>
    <display:column title="Horas Previstas" property="foreseenWorkHours" sortable="true" />
    <display:column title="Data Fim" sortProperty="endDate" sortable="true">
        <c:if test="${task.endDate ne null}">
            <fmt:parseDate pattern="yyyy-MM-dd" type="date" var="endDate" value="${task.endDate}" />
            <fmt:formatDate pattern="dd/MM/yyyy" value="${endDate}" />
        </c:if>
        <c:if test="${task.endDate eq null}">
            -
        </c:if>
    </display:column>    
</display:table>

<script lang="javascript">
    customizeTableLinks();
</script>