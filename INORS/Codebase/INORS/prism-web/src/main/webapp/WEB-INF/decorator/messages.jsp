<!-- ActionMap will hold all the action available for the logged in user, based on org level and role -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="actionMap" value="${actionMap}" scope="session"/>

<script type="text/javascript">
	var moreCount = "<spring:message code='count.results.button.more' javaScriptEscape='true' />";
	var strings = new Array();
	strings['script.common.error'] = "<spring:message code='script.common.error' javaScriptEscape='true' />";
	strings['script.common.error1'] = "<spring:message code='script.common.error1' javaScriptEscape='true' />";
	strings['script.common.sessionExpired'] = "<spring:message code='script.common.sessionExpired' javaScriptEscape='true' />";
	
	strings['script.org.error'] = "<spring:message code='script.org.error' javaScriptEscape='true' />";
	strings['script.org.searchError'] = "<spring:message code='script.org.searchError' javaScriptEscape='true' />";
	
	strings['script.user.adduser'] = "<spring:message code='script.user.adduser' javaScriptEscape='true' />";
	strings['script.user.adduserSuccess'] = "<spring:message code='script.user.adduserSuccess' javaScriptEscape='true' />";
	strings['script.user.regionuser'] = "<spring:message code='script.user.regionuser' javaScriptEscape='true' />";
	strings['script.user.passwordMismatch'] = "<spring:message code='script.user.passwordMismatch' javaScriptEscape='true' />";
	strings['script.user.passwordLikeUsername'] = "<spring:message code='script.user.passwordLikeUsername' javaScriptEscape='true' />";
	strings['script.user.passwordPartUsername'] = "<spring:message code='script.user.passwordPartUsername' javaScriptEscape='true' />";
	strings['script.user.passwordPolicy'] = "<spring:message code='script.user.passwordPolicy' javaScriptEscape='true' />";
	strings['script.user.passwordPolicyHistory'] = "<spring:message code='script.user.passwordPolicyHistory' javaScriptEscape='true' />";
	strings['script.user.updateSuccess'] = "<spring:message code='script.user.updateSuccess' javaScriptEscape='true' />";
	strings['script.user.saveError'] = "<spring:message code='script.user.saveError' javaScriptEscape='true' />";
	strings['script.user.deleteSuccess'] = "<spring:message code='script.user.deleteSuccess' javaScriptEscape='true' />";
	strings['script.user.deleteError'] = "<spring:message code='script.user.deleteError' javaScriptEscape='true' />";
	strings['script.user.search'] = "<spring:message code='script.user.search' javaScriptEscape='true' />";
	strings['script.user.useridStartNumber'] = "<spring:message code='script.user.useridStartNumber' javaScriptEscape='true' />";
	strings['script.user.examinerUser'] = "<spring:message code='script.user.examinerUser' javaScriptEscape='true' />";	
	strings['user.not.added'] = "<spring:message code='user.not.added' javaScriptEscape='true' />";	
	
	strings['script.role.error'] = "<spring:message code='script.role.error' javaScriptEscape='true' />";
	strings['script.role.update'] = "<spring:message code='script.role.update' javaScriptEscape='true' />";
	strings['script.role.userNotFound'] = "<spring:message code='script.role.userNotFound' javaScriptEscape='true' />";
	strings['script.role.userAlreadyTagged'] = "<spring:message code='script.role.userAlreadyTagged' javaScriptEscape='true' />";
	
	strings['script.report.minScore'] = "<spring:message code='script.report.minScore' javaScriptEscape='true' />";
	strings['script.report.maxScore'] = "<spring:message code='script.report.maxScore' javaScriptEscape='true' />";
	strings['script.report.maxScoreSubtest'] = "<spring:message code='script.report.maxScoreSubtest' javaScriptEscape='true' />";
	strings['script.report.minScoreSubtest'] = "<spring:message code='script.report.minScoreSubtest' javaScriptEscape='true' />";
	strings['script.report.maxSubtest'] = "<spring:message code='script.report.maxSubtest' javaScriptEscape='true' />";
	strings['script.report.menu'] = "<spring:message code='script.report.menu' javaScriptEscape='true' />";
	strings['script.report.maxTab'] = "<spring:message code='script.report.maxTab' javaScriptEscape='true' />";
	strings['script.report.pagination'] = "<spring:message code='script.report.pagination' javaScriptEscape='true' />";
	strings['script.report.noActionFound'] = "<spring:message code='script.report.noActionFound' javaScriptEscape='true' />";
	strings['script.report.actionsSavedSuccessfully'] = "<spring:message code='script.report.actionsSavedSuccessfully' javaScriptEscape='true' />";
	
	strings['script.manageReport.update'] = "<spring:message code='script.manageReport.update' javaScriptEscape='true' />";
	strings['script.manageReport.exists'] = "<spring:message code='script.manageReport.exists' javaScriptEscape='true' />";
	strings['script.manageReport.addSuccess'] = "<spring:message code='script.manageReport.addSuccess' javaScriptEscape='true' />";
	
	strings['script.parent.noChildren'] = "<spring:message code='script.parent.noChildren' javaScriptEscape='true' />";
	strings['script.parent.passwordResetError'] = "<spring:message code='script.parent.passwordResetError' javaScriptEscape='true' />";
	strings['script.parent.question'] = "<spring:message code='script.parent.question' javaScriptEscape='true' />";
	strings['script.parent.getChildrenError'] = "<spring:message code='script.parent.getChildrenError' javaScriptEscape='true' />";
	strings['script.parent.invitationCode'] = "<spring:message code='script.parent.invitationCode' javaScriptEscape='true' />";
	strings['script.parent.alreadyClaimed'] = "<spring:message code='script.parent.alreadyClaimed' javaScriptEscape='true' />";
	
	strings['script.student.blankAssessment'] = "<spring:message code='script.student.blankAssessment' javaScriptEscape='true' />";
	strings['script.student.expirationDate'] = "<spring:message code='script.student.expirationDate' javaScriptEscape='true' />";
	strings['script.student.updateSuccess'] = "<spring:message code='script.student.updateSuccess' javaScriptEscape='true' />";
	strings['script.common.empty'] = "<spring:message code='script.common.empty' javaScriptEscape='true' />";
	strings['script.content.addContent'] = "<spring:message code='script.content.addContent' javaScriptEscape='true' />";
	strings['script.content.addSuccess'] = "<spring:message code='script.content.addSuccess' javaScriptEscape='true' />";
	strings['script.content.editSuccess'] = "<spring:message code='script.content.editSuccess' javaScriptEscape='true' />";
	strings['script.content.deleteSuccess'] = "<spring:message code='script.content.deleteSuccess' javaScriptEscape='true' />";
	
	strings['script.noUserFound'] = "<spring:message code='script.noUserFound' javaScriptEscape='true' />";
	strings['script.trySearch'] = "<spring:message code='script.trySearch' javaScriptEscape='true' />";
	strings['msg.rp.success'] = "<spring:message code='msg.rp.success' javaScriptEscape='true' />";
	strings['msg.rp.email.success'] = "<spring:message code='msg.rp.email.success' javaScriptEscape='true' />";
	strings['msg.rp.email.failure'] = "<spring:message code='msg.rp.email.failure' javaScriptEscape='true' />";
	strings['msg.rp.confirm'] = "<spring:message code='msg.rp.confirm' javaScriptEscape='true' />";
	
	strings['msg.confirm.delete'] = "<spring:message code='msg.confirm.delete' javaScriptEscape='true' />";
	strings['msg.fnf'] = "<spring:message code='msg.fnf' javaScriptEscape='true' />";
	strings['msg.jobDeleted'] = "<spring:message code='msg.jobDeleted' javaScriptEscape='true' />";
	strings['msg.fileDeleteError'] = "<spring:message code='msg.fileDeleteError' javaScriptEscape='true' />";
	strings['msg.studentNotFound'] = "<spring:message code='msg.studentNotFound' javaScriptEscape='true' />";
	strings['msg.duplexPrintConfirm'] = "<spring:message code='msg.duplexPrintConfirm' javaScriptEscape='true' />";
	strings['msg.isr'] = "<spring:message code='msg.isr' javaScriptEscape='true' />";
	strings['msg.urt'] = "<spring:message code='msg.urt' javaScriptEscape='true' />";
	strings['msg.selectStudent'] = "<spring:message code='msg.selectStudent' javaScriptEscape='true' />";
	strings['msg.nff'] = "<spring:message code='msg.nff' javaScriptEscape='true' />";
	strings['msg.validFileName'] = "<spring:message code='msg.validFileName' javaScriptEscape='true' />";
	strings['msg.validEmail'] = "<spring:message code='msg.validEmail' javaScriptEscape='true' />";
	strings['msg.oneStudent'] = "<spring:message code='msg.oneStudent' javaScriptEscape='true' />";

	strings['option.statusCode.0'] = "<spring:message code='option.statusCode.0' javaScriptEscape='true' />";
	strings['option.statusCode.3'] = "<spring:message code='option.statusCode.3' javaScriptEscape='true' />";
	strings['option.statusCode.5'] = "<spring:message code='option.statusCode.5' javaScriptEscape='true' />";
	strings['option.statusCode.7'] = "<spring:message code='option.statusCode.7' javaScriptEscape='true' />";
	strings['option.statusCode.8'] = "<spring:message code='option.statusCode.8' javaScriptEscape='true' />";
	strings['option.statusCode.9'] = "<spring:message code='option.statusCode.9' javaScriptEscape='true' />";
	strings['msg.pleaseSelect'] = "<spring:message code='msg.pleaseSelect' javaScriptEscape='true' />";
	strings['msg.err.homePageContent'] = "<spring:message code='msg.err.homePageContent' javaScriptEscape='true' />";
	strings['msg.confirm.deleteContent'] = "<spring:message code='msg.confirm.deleteContent' javaScriptEscape='true' />";
	
	strings['msg.studentAssessmentDetails'] = "<spring:message code='msg.studentAssessmentDetails' javaScriptEscape='true' />";
	strings['msg.confirm.resetIc'] = "<spring:message code='msg.confirm.resetIc' javaScriptEscape='true' />";
	strings['thead.assessment.availableAssessments'] = "<spring:message code='thead.assessment.availableAssessments' javaScriptEscape='true' />";
	strings['thead.assessment.invitationCode'] = "<spring:message code='thead.assessment.invitationCode' javaScriptEscape='true' />";
	strings['thead.assessment.availableICClaims'] = "<spring:message code='thead.assessment.availableICClaims' javaScriptEscape='true' />";
	strings['thead.groupDownloadFiles.expDate'] = "<spring:message code='thead.groupDownloadFiles.expDate' javaScriptEscape='true' />";
	strings['thead.action'] = "<spring:message code='thead.action' javaScriptEscape='true' />";
	
	strings['label.AdminSeason'] = "<spring:message code='label.AdminSeason' javaScriptEscape='true' />";
	strings['label.Grade'] = "<spring:message code='label.Grade' javaScriptEscape='true' />";
	strings['th.studentTable.studentName'] = "<spring:message code='th.studentTable.studentName' javaScriptEscape='true' />";
	strings['th.studentTable.adminSeason'] = "<spring:message code='th.studentTable.adminSeason' javaScriptEscape='true' />";
	strings['th.studentTable.grade'] = "<spring:message code='th.studentTable.grade' javaScriptEscape='true' />";
	strings['msg.studentNotAvailable'] = "<spring:message code='msg.studentNotAvailable' javaScriptEscape='true' />";
	strings['msg.clickStudent'] = "<spring:message code='msg.clickStudent' javaScriptEscape='true' />";
	strings['msg.newParentPassword'] = "<spring:message code='msg.newParentPassword' javaScriptEscape='true' />";
	strings['msg.passwordReset'] = "<spring:message code='msg.passwordReset' javaScriptEscape='true' />";
	strings['msg.passwordSent'] = "<spring:message code='msg.passwordSent' javaScriptEscape='true' />";
	strings['msg.clickingSubmit'] = "<spring:message code='msg.clickingSubmit' javaScriptEscape='true' />";
	strings['msg.confirmIdentity'] = "<spring:message code='msg.confirmIdentity' javaScriptEscape='true' />";
	strings['msg.userName'] = "<spring:message code='msg.userName' javaScriptEscape='true' />";
	strings['msg.fullName'] = "<spring:message code='msg.fullName' javaScriptEscape='true' />";

	strings['label.overallResults'] = "<spring:message code='label.overallResults' javaScriptEscape='true' />";
	strings['label.resultsByStandard'] = "<spring:message code='label.resultsByStandard' javaScriptEscape='true' />";
	strings['title.noImagePrintAvailable'] = "<spring:message code='title.noImagePrintAvailable' javaScriptEscape='true' />";
	strings['title.appliedSkillsImagePDF'] = "<spring:message code='title.appliedSkillsImagePDF' javaScriptEscape='true' />";
	strings['title.noStudentReportAvailable'] = "<spring:message code='title.noStudentReportAvailable' javaScriptEscape='true' />";
	strings['title.individualStudentReport'] = "<spring:message code='title.individualStudentReport' javaScriptEscape='true' />";
	strings['msg.studentReport'] = "<spring:message code='msg.studentReport' javaScriptEscape='true' />";
	strings['msg.imagePDF'] = "<spring:message code='msg.imagePDF' javaScriptEscape='true' />";
	
	strings['confirm.deleteUser'] = "<spring:message code='confirm.deleteUser' javaScriptEscape='true' />";
	strings['label.delete'] = "<spring:message code='label.delete' javaScriptEscape='true' />";
	strings['title.removeFromAssociation'] = "<spring:message code='title.removeFromAssociation' javaScriptEscape='true' />";
	
	strings['msg.err.userCount'] = "<spring:message code='msg.err.userCount' javaScriptEscape='true' />";
	strings['title.viewUserNumber'] = "<spring:message code='title.viewUserNumber' javaScriptEscape='true' />";
	strings['label.userCount'] = "<spring:message code='label.userCount' javaScriptEscape='true' />";
	strings['msg.clickRedirectManageUsers'] = "<spring:message code='msg.clickRedirectManageUsers' javaScriptEscape='true' />";
	
	strings['msg.savedSuccessfully'] = "<spring:message code='msg.savedSuccessfully' javaScriptEscape='true' />";
	
	strings['msg.editReport'] = "<spring:message code='msg.editReport' javaScriptEscape='true' />";
	strings['msg.reportDeletedSuccessfully'] = "<spring:message code='msg.reportDeletedSuccessfully' javaScriptEscape='true' />";
	strings['msg.addReport'] = "<spring:message code='msg.addReport' javaScriptEscape='true' />";
	strings['msg.configureMassage'] = "<spring:message code='msg.configureMassage' javaScriptEscape='true' />";
	
	strings['msg.editUser'] = "<spring:message code='msg.editUser' javaScriptEscape='true' />";
	
	strings['option.p_Roster_Score_List.0'] = "<spring:message code='option.p_Roster_Score_List.0' javaScriptEscape='true' />";
	strings['msg.selectSubtest'] = "<spring:message code='msg.selectSubtest' javaScriptEscape='true' />";
	
	strings['msg.keepAlive'] = "<spring:message code='msg.keepAlive' javaScriptEscape='true' />";
	
	strings['manage.orgs.usercount'] = "<spring:message code='manage.orgs.usercount' javaScriptEscape='true' />";
	
	strings['msg.editActions'] = "<spring:message code='msg.editActions' javaScriptEscape='true' />";
	
</script>
