<%@page contentType="text/html" import="java.util.*,java.text.SimpleDateFormat,org.hps.record.epics.*,org.hps.run.database.*"%>
    
<!DOCTYPE html>
<html>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />

<!-- include links and scripts for tablesorter jquery plugin -->
<%@include file="html/tablesorter.html"%>

<!-- sort the table after doc loads -->
<script>
	$(document).ready(function() {
		$("#epics-table").tablesorter({
			widthFixed : true,
			widgets : [ 'zebra' ]
		}).tablesorterPager({
			container : $("#pager"),
			size : 100
		});
	});
</script>

<body>
    <%
        final EpicsType epicsType = (EpicsType) request.getAttribute("EpicsType");    
        final List<EpicsVariable> epicsVariables = (List<EpicsVariable>) request.getAttribute("EpicsVariables");
        final List<EpicsData> epicsDataList = (List<EpicsData>) request.getAttribute("EpicsDataList");
    %>
    <h2>EPICS <%=epicsType.getTypeCode()%>s</h2>
    <hr />
    
    <h3>Summary</h3>
    
    <% if (!epicsDataList.isEmpty()) { %>
        <table class="summary-table">
            <tr>
                <td class="label">EPICS events</td>
                <td><%=epicsDataList.size()%></td>
            </tr>
            <tr>
                <td class="label">Interval (seconds)</td>
                <td><%=EpicsUtilities.computeTimeInterval(epicsDataList)%></td>
            </tr>
            <tr>
                <td class="label">Min Sequence</td>
                <td><%=epicsDataList.get(0).getEpicsHeader().getSequence()%>
            </tr>
            <tr>
                <td class="label">Max Sequence</td>
                <td><%=epicsDataList.get(epicsDataList.size() - 1).getEpicsHeader().getSequence()%>
            </tr>
        </table>
    <%
        } else {
    %>
        <p>NO DATA</p>
    <% } %>
        
    <h3>Raw Data</h3>

    <!-- tablesorter paging container for data table-->
    <%@include file="html/pager.html"%>

    <!-- EPICS data table -->
    <table id="epics-table" class="tablesorter-blue">
        <thead>
            <tr>
                <th>Sequence</th>
                <th>Timestamp</th>
                <%
                    for (EpicsVariable epicsVariable : epicsVariables) {
                %>
                <th><%=epicsVariable.getVariableName()%></th>
                <%
                    }
                %>
            </tr>
        </thead>
        <tbody>
            <%
                for (EpicsData epicsData : epicsDataList) {
            %>
            <tr>
                <td><%=epicsData.getEpicsHeader().getSequence()%></td>
                <td><%=epicsData.getEpicsHeader().getTimestamp()%></td>
                <%
                    for (EpicsVariable epicsVariable : epicsVariables) {
                %>
                <td><%=epicsData.getValue(epicsVariable.getVariableName())%></td>
                <%
                    }
                %>
            </tr>
            <%
                }
            %>        
        <tbody>
    </table>
    
</body>
</html>
