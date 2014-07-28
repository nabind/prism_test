<script src="scripts/js/libs/jquery-1.7.2.min.js"></script>
<div>
	<textarea id="taContent" style="display:none;"></textarea>
	<div id="growthHome" class="relative with-padding" style="height: auto; text-align: justify">
	</div>
</div>

<script>
openGrowthHomePageTemp();
function openGrowthHomePageTemp() {
	if($('#growthHome').length) {
		$.ajax({
			type : "GET",
			url : "loadHomePageMsg.do",
			data : 'homeMessage=growth',
			dataType : 'json',
			async : false,
			cache: true,
			success : function(data) {
				$('#taContent').val(data.value);
				$('#growthHome').html($('#taContent').val());
			},
			error : function(data) {
			if (data.status == "200") {
				$('#taContent').val(data.responseText);
				$('#growthHome').html($('#taContent').val());
			} else {
				$('#growthHome').html("<p class='big-message icon-warning red-gradient'>Error getting home page content. Please try later.</p>");
			}				
		  }
		});
	}
	return false;
	
}
</script>