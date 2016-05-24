<%--
  ~ Copyright (C) 2005 - 2012 Jaspersoft Corporation. All rights reserved.
  ~ http://www.jaspersoft.com.
  ~
  ~ Unless you have purchased  a commercial license agreement from Jaspersoft,
  ~ the following license terms  apply:
  ~
  ~ This program is free software: you can redistribute it and/or  modify
  ~ it under the terms of the GNU Affero General Public License  as
  ~ published by the Free Software Foundation, either version 3 of  the
  ~ License, or (at your option) any later version.
  ~
  ~ This program is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  ~ GNU Affero  General Public License for more details.
  ~
  ~ You should have received a copy of the GNU Affero General Public  License
  ~ along with this program. If not, see <http://www.gnu.org/licenses/>.
  --%>

<%@ taglib prefix="t" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page import="com.jaspersoft.jasperserver.war.dto.StringOption"%>


<t:insertTemplate template="/WEB-INF/jsp/modules/addResource/dataSource/addDataSourceTemplate.jsp">
    <t:putAttribute name="pageTitle">
        <c:choose>
            <c:when test="${dataResource.editMode}"><spring:message code="resource.datasource.jdbc.page.title.edit"/></c:when>
            <c:otherwise><spring:message code="resource.datasource.jdbc.page.title.add"/></c:otherwise>
        </c:choose>
    </t:putAttribute>
    <t:putAttribute name="bodyID" value="addResource_dataSource_JDBC"/>
    <t:putAttribute name="dataSourceType" value="jdbc"/>
    <t:putAttribute name="testAvailable" value="${true}"/>
    <t:putAttribute name="typeSpecificContent">
        <fieldset  class="group">
            <legend class="offLeft"><span><spring:message code='resource.dataSource.accessProp'/></span></legend>

            <spring:bind path="dataResource.reportDataSource.driverClass">
                <label class="control input text <c:if test="${status.error}"> error </c:if>" class="required" for="driverID"
                       title="<spring:message code='resource.analysisConnection.driver'/>">
                    <span class="wrap"><spring:message code='resource.dataSource.jdbc.driver'/> (<spring:message code='required.field'/>):</span>
                    <input class="" name="${status.expression}" id="driverID" type="text" value="${status.value}"/>
                <span class="message warning">
                    <c:if test="${status.error}">${status.errorMessage}</c:if>
                </span>
                    <span class="message hint"><spring:message code='resource.dataSource.jdbc.hint1'/></span>
                </label>
            </spring:bind>

            <spring:bind path="dataResource.reportDataSource.connectionUrl">
                <label class="control input text <c:if test="${status.error}"> error </c:if>" class="required" for="urlID" title="<spring:message code='resource.analysisConnection.requiredURL'/>">
                    <span class="wrap"><spring:message code='resource.dataSource.jdbc.url'/> (<spring:message code='required.field'/>):</span>
                    <input class="" id="urlID" type="text" name="${status.expression}" value="${status.value}"/>
                <span class="message warning">
                    <c:if test="${status.error}">${status.errorMessage}</c:if>
                </span>
                    <span class="message hint"><spring:message code='resource.dataSource.jdbc.hint2'/></span>
                </label>
            </spring:bind>

            <spring:bind path="dataResource.reportDataSource.username">
                <label class="control input text <c:if test="${status.error}"> error </c:if>" class="required" for="userNameID" title="<spring:message code='resource.analysisConnection.userName'/>">
                    <span class="wrap"><spring:message code='resource.dataSource.jdbc.username'/> (<spring:message code='required.field'/>):</span>
                    <input class="" id="userNameID" type="text" name="${status.expression}" value="${status.value}"/>
                <span class="message warning">
                    <c:if test="${status.error}">${status.errorMessage}</c:if>
                </span>
                    <span class="message hint"></span>
                </label>
            </spring:bind>

            <spring:bind path="dataResource.reportDataSource.password">
                <label class="control input password <c:if test="${status.error}"> error </c:if>" class="required" for="passwordID"
                       title="<spring:message code='resource.analysisConnection.password'/>">
                    <span class="wrap"><spring:message code='resource.dataSource.jdbc.password'/>:</span>
                    <input class="" id="passwordID" type="password"  name="${status.expression}" value="${status.value}" />
                <span class="message warning">
                    <c:if test="${status.error}">${status.errorMessage}</c:if>
                </span>
                    <span class="message hint"></span>
                </label>
            </spring:bind>

            <spring:bind path="dataResource.reportDataSource.timezone">
                <label class="control select" for="timeZone" title="<spring:message code='resource.analysisConnection.timeZone'/>">
                    <span class="wrap"><spring:message code='resource.dataSource.timezone'/></span>
                    <select name="${status.expression}" class="fnormal" id="timeZone">
                        <option value="" <c:if test="${selectedTimezone == null}">selected</c:if>><spring:message code="jsp.jdbcPropsForm.timezone.default.option"/></option>
                        <c:forEach var="timezone" items="${timezones}">
                            <option value="${timezone.code}"
                                    <c:if test="${selectedTimezone == timezone.code}">selected</c:if>>
                                <spring:message code="timezone.option"
                                                arguments='<%= new String[]{((StringOption) pageContext.getAttribute("timezone")).getCode(), ((StringOption) pageContext.getAttribute("timezone")).getDescription()} %>'/>
                            </option>
                        </c:forEach>
                    </select>
                    <span class="message warning">${status.errorMessage}</span>
                    <span class="message hint"><spring:message code='resource.dataSource.hint3'/></span>
                </label>
            </spring:bind>
        </fieldset>
    </t:putAttribute>
</t:insertTemplate>