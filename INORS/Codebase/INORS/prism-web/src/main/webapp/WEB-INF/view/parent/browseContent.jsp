<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>
<div class="margin-bottom-medium thin" style="min-height: 425px;">
	<h1>Browse Content Overview</h1>
        Choose from the links below to search through content for all grades and subjects:
	<h3>Overview</h3>
		<p>
           All children must master challenging academic standards in order to succeed in school and beyond. Being engaged in your child's 
           education is important to his or her continued progress. Understanding these academic standards will enable you to help your 
           child achieve his or her goals.

           Parent NetworkTM gives you the tools and resources to be an informed and active parent or guardian. The following text highlights 
           these tools and resources available to you. We encourage you to take a few minutes to understand them.
		</p>
		<br>
		<p>           
           <a class="browse-content" href="#nogo" 
           		menuId='<spring:message code="menuId.content.stdAct"/>' 
           		menuName='<spring:message code="menuName.content.stdAct"/>'  
           		id=""> -  <spring:message code="menuName.content.stdAct"/></a> <br><br>
             These activities give you the opportunity to help your child improve his or her skills in a fun and engaging way. Your support on 
             these activities can significantly increase your child's appreciation for learning and confidence when tackling new challenges.
       	</p>
		<br>
		<p>
		  <a class="browse-content" href="#nogo" 
				menuId='<spring:message code="menuId.content.stdInd"/>' 
           		menuName='<spring:message code="menuName.content.stdInd"/>'  
         		id=""> -  <spring:message code="menuName.content.stdInd"/> </a><br><br>
			Your state standards and indicators describe what your child is expected to know. Being aware of the indicators will help you help 
			your child acquire skills that show they meet them.
		</p>
		<br>
		<p>
		 	<a class="browse-content" href="#nogo" 
				menuId='<spring:message code="menuId.content.rsc"/>' 
           		menuName='<spring:message code="menuName.content.rsc"/>'
           		id=""> -  <spring:message code="menuName.content.rsc"/></a><br><br>
			The advent of the Internet has greatly increased opportunities to find helpful resources. We have taken time to find additional tools 
			that give you even more ways to help your child.
		</p>
		<br>
		<p>
			<a class="browse-content" href="#nogo" 
				menuId='<spring:message code="menuId.content.eda"/>' 
           		menuName='<spring:message code="menuName.content.eda"/>'
           		id=""> -  <spring:message code="menuName.content.eda"/> </a><br><br>
			Your day-to-day involvement in your child's education makes an important difference in his or her success in school. 
			Everyday Activities shows you practical ways to play an active and supportive role in your child's education.
		</p>
		<br>
		<p>
			<a class="browse-content" href="#nogo"
				menuId='<spring:message code="menuId.content.att"/>' 
           		menuName='<spring:message code="menuName.content.att"/>' 
				id=""> -  <spring:message code="menuName.content.att"/> </a><br><br>
			Your child should know about the test he or she will take. By doing so, stress will be lessened when he or she takes the actual test. 
			This section of Parent Network provides a brief and easy-to-understand overview of your child's test.
		</p>
		
</div>
