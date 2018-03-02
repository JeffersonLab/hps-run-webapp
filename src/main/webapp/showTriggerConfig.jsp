<%@page contentType="text/html"%>
<%@page language="java" session="true"%>
<%@page import="java.util.*"%>
<%@page import="org.hps.record.triggerbank.TriggerConfigData"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page isELIgnored="false"%>

<html>
<body>
    <div>
        <sql:query dataSource="jdbc/hps_run_db_v2" var="result">
            SELECT * FROM trigger_configs WHERE run = ?;
            <sql:param value="${run}" />
        </sql:query>
        <c:set var="newline" value="<%= \"\n\" %>" />
        <c:forEach var="row" items="${result.rows}">
            
            <c:set var="config1" value="${fn:replace(row.config1, newline, '<br />')}" />
            <h3>Crate <%=TriggerConfigData.Crate.values()[0].getCrateNumber()%></h3>
            <c:out escapeXml="false" value="${config1}" />
            
            <c:set var="config2" value="${fn:replace(row.config2, newline, '<br />')}" />
            <h3>Crate <%=TriggerConfigData.Crate.values()[1].getCrateNumber()%></h3>
            <c:out escapeXml="false" value="${config2}" />
            
            <c:set var="config3" value="${fn:replace(row.config3, newline, '<br />')}" />
            <h3>Crate <%=TriggerConfigData.Crate.values()[2].getCrateNumber()%></h3>
            <c:out escapeXml="false" value="${config3}" />
            
            <c:set var="config4" value="${fn:replace(row.config4, newline, '<br />')}" />
            <h3>Crate <%=TriggerConfigData.Crate.values()[3].getCrateNumber()%></h3>
            <c:out escapeXml="false" value="${config4}" />                                                         
            
        </c:forEach>
    </div>
</body>
</html>
