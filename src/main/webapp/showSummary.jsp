<%@page contentType="text/html"%>
<%@page import="java.util.*,java.io.File,java.text.SimpleDateFormat"%>
<%@page import="org.hps.datacat.client.*,org.hps.run.database.*"%>
<%@page language="java" session="true"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery-ui.css" />
    <script src="${pageContext.request.contextPath}/javascript/jquery-1.11.3.js"></script>
    <script src="${pageContext.request.contextPath}/javascript/jquery-ui.js"></script>
    <script>
	$(function() {
		$("#tabs").tabs();
	});
    </script>
</head>
<body>

    <sql:query dataSource="jdbc/hps_run_db_v2" var="result">
        SELECT * FROM run_summaries WHERE run = ?;
        <sql:param value="${run}" />
    </sql:query>

    <div id="tabs">
        <ul>
            <li><a href="#tabs-1">Summary</a></li>
            <li><a href="#tabs-2">Data</a></li>
            <li><a href="#tabs-3">Files</a></li>
            <li><a href="#tabs-4">Config</a></li>
        </ul>    
        <div id="tabs-1">
            <table class="summary-table">
                <c:forEach var="row" items="${result.rows}">
                    <tr>
                        <td class="label">Run</td>
                        <td><c:out value="${row.run}" /></td>
                    </tr>
                    <tr>
                        <td class="label">Events</td>
                        <td><c:out value="${row.nevents}" /></td>
                    </tr>
                    <tr>
                        <td class="label">Files</td>
                        <td><c:out value="${row.nfiles}" /></td>
                    </tr>
                    <tr>
                        <td class="label">Prestart Timestamp</td>
                        <td><c:out value="${row.prestart_timestamp}" /></td>
                    </tr>
                    <tr>
                        <td class="label">Go Timestamp</td>
                        <td><c:out value="${row.go_timestamp}" /></td>
                    </tr>
                    <tr>
                        <td class="label">End Timestamp</td>
                        <td><c:out value="${row.end_timestamp}" /></td>
                    </tr>
                    <tr>
                        <td class="label">Trigger Rate</td>
                        <td><c:out value="${row.trigger_rate}" /></td>
                    </tr>
                    <tr>
                        <td class="label">Trigger Config Name</td>
                        <td><c:out value="${row.trigger_config_name}" /></td>
                    </tr>
                    <tr>
                        <td class="label">TI Time Offset</td>
                        <td><c:out value="${row.ti_time_offset}" /></td>
                    </tr>
                    <tr>
                        <td class="label">Livetime Clock</td>
                        <td><c:out value="${row.livetime_clock}" /></td>
                    </tr>
                    <tr>
                        <td class="label">Livetime TDC</td>
                        <td><c:out value="${row.livetime_fcup_tdc}" /></td>
                    </tr>
                    <tr>
                        <td class="label">Livetime TRG</td>
                        <td><c:out value="${row.livetime_fcup_trg}" /></td>
                    </tr>
                    <tr>
                        <td class="label">Target</td>
                        <td><c:out value="${row.target}" /></td>
                    </tr>
                    <tr>
                        <td class="label">Notes</td>
                        <td><c:out value="${row.notes}" /></td>
                    </tr>
                    <tr>
                        <td class="label">Created</td>
                        <td><c:out value="${row.created}" /></td>
                    </tr>
                    <tr>
                        <td class="label">Updated</td>
                        <td><c:out value="${row.updated}" /></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <div id="tabs-2">
            <p>
                <a target="dataFrame" href="epics?run=<c:out value="${run}"/>&epicsBankType=<%=EpicsType.EPICS_2S%>">EPICS 2s</a>
                &#91;<a class="download" target="_blank" href="download?dataType=EPICS_2S&run=<c:out value="${run}"/>">CSV</a>&#93;
            </p>
            <p>
                <a target="dataFrame" href="epics?run=<c:out value="${run}"/>&epicsBankType=<%=EpicsType.EPICS_20S%>">EPICS 20s</a>
                &#91;<a class="download" target="_blank" href="download?dataType=EPICS_20S&run=<c:out value="${run}"/>">CSV</a>&#93;
            </p>
            <p>
                <a target="dataFrame" href="scalers?run=<c:out value="${run}"/>">Scalers</a>
                &#91;<a class="download" target="_blank" href="download?dataType=SCALERS&run=<c:out value="${run}"/>">CSV</a>&#93;
            </p>
        </div>
        <div id="tabs-3">
            <p>
                <a target="dataFrame" href="datasets?run=<c:out value="${run}"/>&dataType=RAW&fileFormat=EVIO">EVIO Raw</a>
                &#91;<a class="download" target="_blank" href="download?dataType=RAW_FILES&run=<c:out value="${run}"/>">CSV</a>&#93;
            </p>
            <p>
                <a target="dataFrame" href="datasets?run=<c:out value="${run}"/>&dataType=RECON&fileFormat=LCIO">LCIO Recon</a>
                &#91;<a class="download" target="_blank" href="download?dataType=RECON_FILES&run=<c:out value="${run}"/>">CSV</a>&#93;
            </p>
            <p>
                <a target="dataFrame" href="datasets?run=<c:out value="${run}"/>&dataType=DST&fileFormat=ROOT">ROOT DST</a>
                &#91;<a class="download" target="_blank" href="download?dataType=DST_FILES&run=<c:out value="${run}"/>">CSV</a>&#93;
            </p>
            <p>
                <a target="dataFrame" href="datasets?run=<c:out value="${run}"/>&dataType=DQM&fileFormat=AIDA">AIDA DQM</a>
                &#91;<a class="download" target="_blank" href="download?dataType=AIDA_DQM_FILES&run=<c:out value="${run}"/>">CSV</a>&#93;
            </p>
            <p>
                <a target="dataFrame" href="datasets?run=<c:out value="${run}"/>&dataType=DQM&fileFormat=ROOT">ROOT DQM</a>
                &#91;<a class="download" target="_blank" href="download?dataType=ROOT_DQM_FILES&run=<c:out value="${run}"/>">CSV</a>&#93;
            </p>
        </div>
        <div id="tabs-4">
            <p>
                <a target="dataFrame" href="triggerconfig?run=<c:out value="${run}"/>">Trigger Config</a>
            </p>
        </div>
    </div>
</body>
</html>
