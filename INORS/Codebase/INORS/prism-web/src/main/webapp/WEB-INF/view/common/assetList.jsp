<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<div class="with-padding main-section" style="background-color:#FFF; padding: 20px 10px !important;min-width:686px;min-height:629px;overflow-x:auto;position: relative">
	<hgroup id="main-title" class="thin" style="padding: 0 0 22px">
		<h1>Asset List</h1>
	</hgroup>
 
	<c:forEach items="${assets}" var="asset">
		<a href="downloadAssest.do?assetPath=ads/<c:out value="${asset}"/>"><c:out value="${asset}"/></a>
		<br />
	</c:forEach>

	<br/><br/>
	<!-- a href="asset">Upload Asset</a-->
</div>
</body>
</html>