<%@ page contentType="text/html"%>
<%@page import="java.util.*,org.hps.run.database.RunSummary,java.text.SimpleDateFormat"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>

<html>

<head>
    <!-- application stylesheet -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
</head>

<!-- include links and scripts for tablesorter jquery plugin -->
<%@include file="html/tablesorter.html"%>

<!-- sort the table after doc loads -->
<script>
	$(document).ready(function() {
		$("#run-table").tablesorter({
			widthFixed : true,
			widgets : [ 'zebra' ]
		}).tablesorterPager({
			container : $("#pager"),
			size : 100
		});
	});
</script>

<body>
    <%@include file="header.jspf"%>
    <div class="content">
    
        <!-- tablesorter paging container -->
        <%@include file="html/pager.html"%>
        
        <!-- full run table with tablesorter theme, sorting and pagination -->
        <table id="run-table" class="tablesorter-blue">
            <thead>
                <tr>
                    <th>Run</th>
                    <th>Events</th>
                    <th>Files</th>
                    <th>Prestart Timestamp</th>
                    <th>Go Timestamp</th>
                    <th>End Timestamp</th>
                    <th>Trigger Rate</th>
                    <th>Created</th>
                    <th>Updated</th>
                </tr>
            </thead>
            <tbody>
                <sql:query dataSource="jdbc/hps_run_db_v2" var="result">
                    SELECT * FROM run_summaries;
                </sql:query>
                <c:forEach var="row" items="${result.rows}">
                    <tr onclick="document.location.href='run?run=<c:out value="${row.run}"/>';">
                        <td><c:out value="${row.run}" /></td>
                        <td><c:out value="${row.nevents}" /></td>
                        <td><c:out value="${row.nfiles}" /></td>
                        <td><c:out value="${row.prestart_timestamp}" /></td>
                        <td><c:out value="${row.go_timestamp}" /></td>
                        <td><c:out value="${row.end_timestamp}" /></td>
                        <td><c:out value="${row.trigger_rate}" /></td>
                        <td><c:out value="${row.created}" /></td>
                        <td><c:out value="${row.updated}" /></td>
                    </tr>
                </c:forEach>
            <tbody>
        </table>
    </div>
</body>
</html>
