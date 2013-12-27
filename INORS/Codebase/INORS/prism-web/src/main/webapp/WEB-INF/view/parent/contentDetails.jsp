<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>

<div class="margin-bottom-medium" style="min-height: 425px;">
	
		<div id="contentDetailsHeader" class="wrapped relative white-gradient"
			style="height: auto; text-align: justify">	
			<h2>${articleTypeDescription.contentName}</h2>		
		</div>
		<textarea id="taContent" style="display:none;">
			${articleTypeDescription.contentDescription}
		</textarea>
		<div id="contentDescription" class="wrapped relative white-gradient"
			style="height: auto; text-align: justify">			
		</div>
</div>
