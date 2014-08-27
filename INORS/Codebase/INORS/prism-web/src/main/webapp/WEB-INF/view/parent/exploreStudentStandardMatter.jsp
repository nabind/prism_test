<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<noscript class="message black-gradient simpler"><spring:message code="error.noscript" /></noscript>
<div class="margin-bottom-medium std-matter-parent-div thin">
	<h1><spring:message code="h1.exploreStudentStandardMatter" /></h1>
	<div class="standard-tabs margin-bottom reportTabContainer" id="add-tabs">
		<ul class="tabs reporttabs">
			<li class="active"><a href="#nogo">
				<spring:message code="label.whyStandardsMatter" />
			</a></li>
		</ul>
		<div class="tabs-content">
			<div class="relative with-padding" style="padding: 20px !important">
				<p><spring:message code="p.exploreStudentStandardMatter.1" /></p>
				<p><spring:message code="p.exploreStudentStandardMatter.2" /></p>
				<div class="content-panel accordion-div">
					<dl class="accordion">
						<dt class="closed">
							<span class="icon-info tracked"></span>
							<spring:message code="label.whatAreStandards" />
						</dt>
						<dd style="height: auto; display: none;"
							class="accordion-body with-padding">
							<p><spring:message code="p.exploreStudentStandardMatter.3" /></p>
							<p><spring:message code="p.exploreStudentStandardMatter.4" /></p>
							<p><spring:message code="p.exploreStudentStandardMatter.5" /></p>
							<p><spring:message code="p.exploreStudentStandardMatter.6" /></p>
						</dd>
						<dt class="closed">
							<span class="icon-info tracked"></span>
							<spring:message code="label.howIsTestingUseful" />
						</dt>
						<dd style="height: auto;" class="accordion-body with-padding">
							<p><spring:message code="p.exploreStudentStandardMatter.7" /></p>
							<p><spring:message code="p.exploreStudentStandardMatter.8" /></p>
						</dd>
						<dt class="closed">
							<span class="icon-info tracked"></span> 
							<spring:message code="label.whatIsNoChildLeftBehind" />
						</dt>
						<dd style="height: auto;" class="accordion-body with-padding">
							<p><spring:message code="p.exploreStudentStandardMatter.9" /></p>
							<p><spring:message code="p.exploreStudentStandardMatter.10" /></p>
							<p><spring:message code="p.exploreStudentStandardMatter.11" /></p>
						</dd>
					</dl>
				</div>
			</div>
		</div>
	</div>
</div>
