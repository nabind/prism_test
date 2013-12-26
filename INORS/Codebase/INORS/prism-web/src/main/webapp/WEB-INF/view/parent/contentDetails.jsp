<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>

<div class="margin-bottom-medium">
	
		<div id="standardHeader" class="wrapped relative white-gradient"
			style="height: auto; text-align: justify">	
			<h2>Name : ${articleTypeDescription.contentName}</h2>		
		</div>
		
		<div>
		<textarea id="taContent" style="display:none;">
			Description : ${articleTypeDescription.contentDescription}
		</textarea>
		</div>
		
</div>
