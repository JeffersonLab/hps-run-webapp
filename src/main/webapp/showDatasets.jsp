<%@ page contentType="text/html"%>
<%@page import="java.util.*"%>
<%@page import="java.io.File"%>
<%@page import="org.hps.run.database.RunSummary"%>
<%@page import="org.hps.webapps.run.DatacatHelper"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tags/functions.tld" prefix="f"%>

<html>

<link rel="stylesheet" href="css/style.css" />

<!-- include links and scripts for tablesorter jquery plugin -->
<%@include file="html/tablesorter.html"%>

<!-- sort the table after doc loads -->
<script>
    $(document).ready(function() {
        $("#dataset-table").tablesorter({
            widthFixed : true,
            widgets : [ 'zebra' ]
        }).tablesorterPager({
            container : $("#pager"),
            size : 50
        });
    });
</script>

<body>
    <h3><c:out value="${fileFormat}"/> <c:out value="${dataType}"/> for Run <c:out value="${run}"/></h3>
    
    <!-- tablesorter paging container -->
    <%@include file="html/pager.html"%>
    
    <table id="dataset-table" class="tablesorter-blue">
        <thead>
            <tr>
                <th>Name</th>
                <th>Size</th>
                <th>Created</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="dataset" items="${datasets}">
                <c:forEach var="location" items="${dataset.viewInfo.locations}">
                    <c:if test="${location.isMaster().booleanValue()}">
                        <c:set var="master" value="${location}"/>
                    </c:if>
                </c:forEach>
                <tr> 
                    <td>
                        <!-- Link to external datacat site. -->
                        <a href="<%=getServletContext().getInitParameter("datacat")%>/display/datasets/<c:out value="${dataset.path}"/>">
                            <c:out value="${dataset.name}" />
                        </a>
                        <!-- Link to plot display if file is AIDA DQM. -->
                        <c:if test="${dataType == 'DQM' && dataFormat == 'AIDA'}">
                            <a target="_blank" style="font-size: 80%" 
                                href="<%=getServletContext().getInitParameter("dqm")%>/show_plots?file=<c:out value="${master.resource}"/>">Show Plots</a>
                        </c:if>
                    </td>
                    <td>
                        <c:out value="${f:formatBytes(master.size)}" />
                    </td>
                    <td>
                        <c:out value="${dataset.dateCreated}" />
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>        
</body>
</html>
