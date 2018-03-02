<%@ page contentType="text/html" import="java.util.*,java.text.SimpleDateFormat,org.hps.record.scalers.*"%>
<!DOCTYPE html>
<html>
<link rel="stylesheet" href="css/style.css" />

<!-- include links and scripts for tablesorter jquery plugin -->
<%@include file="html/tablesorter.html"%>

<!-- sort the table after doc loads -->
<script>
    $(document).ready(function() {
        $("#scalers-table").tablesorter({
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
        // Get the run number.
        int run = Integer.parseInt(request.getParameterValues("run")[0]);

        // Get the list of EPICS data for this run.
        List<ScalerData> scalerDataList = (List<ScalerData>) request.getAttribute("ScalerDataList");
    %>
    <h2>Scaler Data</h2>
    <hr/>
    
    <h3>Summary</h3>
    
    <% if (!scalerDataList.isEmpty()) { %>
        <table class="summary-table">
            <tr>
                <td class="label">Scaler Events</td>
                <td><%=scalerDataList.size()%></td>
            </tr>
            <tr>
                <td class="label">Min Event</td>
                <td><%=scalerDataList.get(0).getEventId()%>
            </tr>
            <tr>
                <td class="label">Max Event</td>
                <td><%=scalerDataList.get(scalerDataList.size() - 1).getEventId()%>
            </tr>
        </table>
    <%
        } else {
    %>
        <p>NO DATA</p>
    <% } %>

    <h3>Raw Data</h3>

    <!-- tablesorter paging container -->
    <%@include file="html/pager.html"%>
    
    <!-- scaler data table -->
    <table id="scalers-table" class="tablesorter-blue">
        <thead>
            <tr>
                <th>Event ID</th>
                <%
                    for (ScalerDataIndex index : ScalerDataIndex.values()) {
                %>
                <th><%= index.toString().replace("_", " ") %></th>
                <%
                    }
                %>
            </tr>
        </thead>
        <tbody>
            <% for (ScalerData scalerData : scalerDataList) { %>
            <tr>
                <td><%= scalerData.getEventId() %></td>
                <% for (ScalerDataIndex index : ScalerDataIndex.values()) { %>
                <td><%= scalerData.getValue(index.index()) %></td>
                <% } %>
            </tr>
            <% } %>
        <tbody>
    </table>

</body>
</html>
