<script src="scripts/js/libs/jquery-1.7.2.min.js"></script>
<div>
	<textarea id="taContent" style="display:none;"></textarea>
	<div id="inorsHome" class="relative with-padding" style="height: auto; text-align: justify">
	</div>
</div>

<script>
//alert("jquery");
//For temporary fix
openInorsHomePageTemp();
function openInorsHomePageTemp() {
	//Fixed for TD 77263 -  By Joy
	if($('#inorsHome').length) {
		$.ajax({
			type : "GET",
			url : "loadHomePageMsg.do",
			data : null,
			dataType : 'json',
			cache: true,
			success : function(data) {
				$('#taContent').val(data.value);
				$('#inorsHome').html($('#taContent').val());
			},
			error : function(data) {
			if (data.status == "200") {
				$('#taContent').val(data.responseText);
				$('#inorsHome').html($('#taContent').val());
			} else {
				$('#inorsHome').html("<p class='big-message icon-warning red-gradient'>Error getting home page content. Please try later.</p>");
			}				
		  }
		});
	}
	
}
</script>

