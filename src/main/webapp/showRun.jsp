<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page language="java" session="true"%>

<html>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
<head>
    <title>HPS Run Database - Run <c:out value="${run}"/></title>
</head>
<body>

    <%@include file="header.jspf"%>
    
    <div class="content">
        <iframe class="nav" name="runSummaryFrame" frameBorder="0" src="runSummary?run=<c:out value="${run}"/>" height="100%" width="30%"></iframe>
        <iframe class="data" name="dataFrame" frameBorder="0" src="" height="100%" width="68%"></iframe>
    </div>
    
</body>
</html>
