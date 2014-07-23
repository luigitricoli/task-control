<%@ page pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<display:table name="tasks" export="true" requestURI="/relatorios/listaTarefas"
               pagesize="10">
    <%@include file="_displayTag-setup.jsp" %>
    
    <display:column title="Descrição" property="description" sortable="true" />
    <display:column title="Sistema" property="application" sortable="true" />
    <display:column title="Responsáveis" property="owners" sortable="true" />
    <display:column title="Origem" property="source" sortable="true" />
    <display:column title="Data Início" property="startDate" sortable="true" />
    <display:column title="Data Prevista" property="foreseenEndDate" sortable="true" />
    <display:column title="Horas Previstas" property="foreseenWorkHours" sortable="true" />
    <display:column title="Data Fim" property="endDate" sortable="true" />
</display:table>

<script lang="javascript">
    replaceDisplayTagLinksWithAjax();
</script>