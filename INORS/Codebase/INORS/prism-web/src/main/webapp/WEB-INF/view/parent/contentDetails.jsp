<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<noscript class="message black-gradient simpler">Your browser
	does not support JavaScript! Some features won't work as expected...</noscript>

<div class="margin-bottom-medium" style="min-height: 425px;">
	<hgroup id="main-title" class="thin" style="padding: 0 0 22px">
		<div id="contentDetailsHeader" class="relative"
			style="height: auto; text-align: justify">	
			<h1>${articleTypeDescription.contentName}</h1>		
		</div>
	</hgroup>
	<textarea id="taContent" style="display:none;">
		${articleTypeDescription.contentDescription}
	</textarea>
	<div id="contentDescription" class="relative"
		style="height: auto; text-align: justify">			
	</div>
</div>


