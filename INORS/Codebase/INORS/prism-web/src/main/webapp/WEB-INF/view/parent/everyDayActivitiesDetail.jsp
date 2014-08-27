<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<noscript class="message black-gradient simpler"><spring:message code="error.noscript" /></noscript>
<div id="" style="height: auto; width: auto">
	<h3><spring:message code="menuName.content.eda" />: ${gradeName}</h3>
	<div>
		<p><spring:message code="p.everyDayActivitiesDetail.1" /></p>
		<p><spring:message code="p.everyDayActivitiesDetail.2" /></p>
		<p><spring:message code="p.everyDayActivitiesDetail.3" /></p>
	</div>
	<br>
	<div id="" height: auto; width: auto">
		<ul class="big-menu blue-gradient collapsible" id="parentMenu">
			<li class="with-right-arrow" id="select-tooltip-1"><span><spring:message code="label.exploreYourChildInterests" /></span>
				<ul class="white-gradient">
					<p><spring:message code="p.everyDayActivitiesDetail.4" /></p>
				</ul>
			</li>
			<li class="with-right-arrow" id="select-tooltip-2"><span><spring:message code="label.integrateLearningEverydayActivities" /></span>
				<ul class="white-gradient">
					<p><spring:message code="p.everyDayActivitiesDetail.5" /></p>
				</ul>
			</li>
			<li class="with-right-arrow" id="select-tooltip-3"><span><spring:message code="label.readYourChild" /></span>
				<ul class="white-gradient">
					<p><spring:message code="p.everyDayActivitiesDetail.6" /></p>
				</ul>
			</li>
			<li class="with-right-arrow" id="select-tooltip-4"><span><spring:message code="label.helpYourChild" /></span>
				<ul class="white-gradient">
					<p><spring:message code="p.everyDayActivitiesDetail.7" /></p>
				</ul>
			</li>
		</ul>
	</div>
	<br>
	<p><spring:message code="p.everyDayActivitiesDetail.8" /></p>
</div>
