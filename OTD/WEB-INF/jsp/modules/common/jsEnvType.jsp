<%--
~ Copyright (C) 2005 - 2011 Jaspersoft Corporation. All rights reserved.
~ http://www.jaspersoft.com.
~ Licensed under commercial Jaspersoft Subscription License Agreement
--%>

<%@ page import="com.jaspersoft.ji.license.LicenseManager" %>

<%
    request.setAttribute("isDevelopmentEnvironmentType", LicenseManager.isDevelopmentEnvironmentType());
%>