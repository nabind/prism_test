/**
 * This js file is to manage Rescore Request - Start
 * Author: Joy
 * Version: 1
 * Don't include it in custom.all.js
 */
var ANIMATION_TIME = 200;

$(document).ready(function() {
	$("#studentTableRRF").dataTable({
		'aoColumnDefs' : [ {
			'bSortable' : false,
			'aTargets' : [ 1, 3, 4, 6 ]
		} ],
		'sPaginationType' : 'full_numbers',
		'fnDrawCallback': function( oSettings ) {
			filteredRow = this.$('tr', {"filter": "applied"} );
		}
	});
	$( "#studentTableRRF_length > label" ).css( "cursor", "default" );
	$( "#studentTableRRF_filter > label" ).css( "cursor", "default" );
	$( ".sorting_disabled" ).css( "cursor", "default" );
	var filteredRow_2;
	$("#studentTableRRF_2").dataTable({
		'aoColumnDefs' : [ {
			'bSortable' : false,
			'aTargets' : [ 0, 3, 4, 6 ]
		} ],
		'sPaginationType' : 'full_numbers',
		'fnDrawCallback': function( oSettings ) {
			filteredRow_2 = this.$('tr', {"filter": "applied"} );
		}
	});
	$( "#studentTableRRF_2_length > label" ).css( "cursor", "default" );
	$( "#studentTableRRF_2_filter > label" ).css( "cursor", "default" );
	
});
//=====document.ready End=========================================

/**
 * This js file is to manage Rescore Request - End
 * Author: Joy
 * Version: 1
 * Don't include it in custom.all.js
 */