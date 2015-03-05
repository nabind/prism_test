<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<noscript class="message black-gradient simpler"><spring:message code="error.noscript" /></noscript>
<div class="margin-bottom-medium thin" style="min-height: 425px;">
	<h1><spring:message code="h1.browseContent" /></h1>
	<div class="standard-tabs margin-bottom reportTabContainer" id="add-tabs">
		<ul class="tabs reporttabs">
			<li class="active"><a href="#nogo">
				<spring:message code="menuName.content.overview" />
			</a></li>
		</ul>
		<div class="tabs-content">
			<div class="relative with-padding" style="padding: 20px !important">
					<b><spring:message code="p.browseContent.1" /></b>
					<br/><br/>
					<p>
					   <spring:message code="p.browseContent.2" />
					   <spring:message code="p.browseContent.3" />
					</p>
					<br>
					<p>           
					   <a class="browse-content browsecontent" href="#nogo" 
							menuId='<spring:message code="menuId.content.stdAct"/>' 
							menuName='<spring:message code="menuName.content.stdAct"/>'  
							id=""> -  <spring:message code="menuName.content.stdAct"/></a> <br><br>
						 <spring:message code="p.browseContent.4" />
					</p>
					<br>
					<p>
					  <a class="browse-content browsecontent" href="#nogo" 
							menuId='<spring:message code="menuId.content.stdInd"/>' 
							menuName='<spring:message code="menuName.content.stdInd"/>'  
							id=""> -  <spring:message code="menuName.content.stdInd"/> </a><br><br>
						<spring:message code="p.browseContent.5" />
					</p>
					<br>
					<p>
						<a class="browse-content browsecontent" href="#nogo" 
							menuId='<spring:message code="menuId.content.rsc"/>' 
							menuName='<spring:message code="menuName.content.rsc"/>'
							id=""> -  <spring:message code="menuName.content.rsc"/></a><br><br>
						<spring:message code="p.browseContent.6" />
					</p>
					<br>
					<p>
						<a class="browse-content browsecontent" href="#nogo" 
							menuId='<spring:message code="menuId.content.eda"/>' 
							menuName='<spring:message code="menuName.content.eda"/>'
							id=""> -  <spring:message code="menuName.content.eda"/> </a><br><br>
						<spring:message code="p.browseContent.7" />
					</p>
					<br>
					<p>
						<a class="browse-content browsecontent" href="#nogo"
							menuId='<spring:message code="menuId.content.att"/>' 
							menuName='<spring:message code="menuName.content.att"/>' 
							id=""> -  <spring:message code="menuName.content.att"/> </a><br><br>
						<spring:message code="p.browseContent.8" />
					</p>
				</div>
			</div>
		</div>
</div>
