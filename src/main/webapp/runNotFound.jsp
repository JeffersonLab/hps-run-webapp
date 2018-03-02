<%@page contentType="text/html" pageEncoding="UTF-8"%>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
</head>
<body>
    <%@include file="header.jspf"%>    
    <div class="content">
        <%
            if (request.getParameter("run") != null) {
        %>
        <p>No data found for run ${run} in database.</p>
        <%
            } else {
        %>
        <p>No run number provided in URL parameters.</p>
        <%
            }
        %>    
    </div>
</body>
</html>