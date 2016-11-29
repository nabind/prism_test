	<!--[if gt IE 8]>
    <style>
	    .icon-leaf {
		    font-size: 30% !important;
		}
		.icon-star {
			font-size: 30% !important;
		}
		.icon-users {
			font-size: 40% !important;
		}
		.icon-pages {
			font-size: 50% !important;
		}
    </style>
	<![endif]-->

	
	<script>
		// Call template init (optional, but faster if called manually)
		// Call template init (optional, but faster if called manually)
		$.template.init();
		
		if(!window.slider) var slider={};
	    slider.data=[
	                 {"id":"slide-img-1","client":"","desc":"welcome to ACSI Data Online for Parents"},
	                 {"id":"slide-img-2","client":"","desc":"explore the links below for additional information"},
	                 {"id":"slide-img-3","client":"","desc":"add all your child information into single login"}];
	    
	    // ========================== CODE FOR SHOWING Windows H-scrollbar in non desktop width ==========================
		var element = $('html');
		$(document).on('init-queries',					function() { if ($.template.mediaQuery.isSmallerThan('desktop')) {
																		element.css('overflow-x', 'auto');
																	} })
				   .on('quit-query-mobile-portrait',	function() { element.css('overflow-x', 'auto'); })
				   .on('quit-query-mobile-landscape',	function() { element.css('overflow-x', 'auto'); })
				   .on('quit-query-mobile',				function() { element.css('overflow-x', 'auto'); })
				   .on('quit-query-tablet-portrait',	function() { element.css('overflow-x', 'auto'); })
				   .on('quit-query-tablet-landscape',	function() { element.css('overflow-x', 'auto'); })
				   .on('quit-query-tablet',				function() { element.css('overflow-x', 'auto'); })
				   .on('quit-query-desktop',			function() { element.css('overflow-x', 'auto'); })
				   .on('enter-query-mobile',			function() { element.css('overflow-x', 'auto'); })
				   .on('enter-query-mobile-portrait',	function() { element.css('overflow-x', 'auto'); })
				   .on('enter-query-mobile-landscape',	function() { element.css('overflow-x', 'auto'); })
				   .on('enter-query-tablet',			function() { element.css('overflow-x', 'auto'); })
				   .on('enter-query-tablet-portrait',	function() { element.css('overflow-x', 'auto'); })
				   .on('enter-query-tablet-landscape',	function() { element.css('overflow-x', 'hidden'); })
				   .on('enter-query-desktop',			function() { element.css('overflow-x', 'hidden'); });
		
		$(document).ready(function() {
			unblockUI()
			// ========================== HIDE SCROLLBAR FROM MENU ==========================
			$("#menu").removeClass("custom-scroll");
			
			// ========================== CODE FOR SHOW/HIDE ORG HIERARCHY ==========================
			$('#hideHierarchy').live("click", function(e) {
				// patch for safari
				$("#searchUser").focus();
				
				$("#showHierarchy").show();
				$('.content-panel').css('padding-left', '0px');
				$(this).trackElement($('.content-panel'));
				//$(".panel-navigation").css("width", "0px");
				$(".panel-navigation").hide();
				/*$('.content-panel').animate({
					'padding-left': '0px'
				}, 500, function() {
					$(".panel-navigation").css("width", "0px");
				});*/
				$("#showHierarchy").removeClass("display-none");
				$(document).click();
				
				// patch for safari
				$("#searchUser").blur();
			});
			$('#showHierarchy').live("click", function(e) {
				$("#showHierarchy").hide();
				$("#hideHierarchy").css("top", "5px").css("left", "5px");
				$('.content-panel').css('padding-left', '250px');
				//$(".panel-navigation").css("width", "250px");
				$(".panel-navigation").show();
				
				$(document).click();
				/*$('.content-panel').animate({
					'padding-left': '250px'
				}, 500, function() {
					$(".panel-navigation").css("width", "250px");
				});*/
			});
			
			//if(!$.browser.msie) $('#menu').height($(".main-section").height()+42);
			
			
			/*$(window).bind("scroll", function(){
				$(".with-right-arrow").removeTooltip(true, false);
			});*/
			
			// ================ PATCH FOR INTERNET EXPLORER (IE) ============================
			if($.browser.msie) {
				// patch for report export buttons in ie7
				if($.template.ie7) {
					$(".report-btn").css("margin-right", "10px");
					$(".manage-btn").css("margin-right", "15px");
					$("#addUser").css("margin-right", "10px");
				}
				
				// Patch for modal
				$('.label').addClass('label-ie');
				// Patch for dashboard modal
				if ($("#addDashboard").size()>0){
					$('.label').css('width', '150px');
				}
				// Patch for my account 
				if ($(".manageAccount").size()>0){
					$('.label').css('width', '200px');
				}
				// Patch for user registration
				if ($("#registrationForm").size()>0){
					$("#registrationForm").css('width', '800px');
					$("#registrationForm").css('text-align', 'left');
					$("#registrationFormContainer").attr('align', 'center');
					$('.label').css('width', '200px');
					$('ul.wizard-steps > li').css('float', 'left');
				}
				
				// Patch for changePassword
				if ($("#changePasswordFrom").size()>0){
					$("#changePasswordFrom").css('width', '800px');
					$("#changePasswordFrom").css('text-align', 'left');
					$("#registrationFormContainer").attr('align', 'center');
					$('.label').css('width', '200px');
					$('ul.wizard-steps > li').css('float', 'left');
				}
				
				//patch for table header
				$(".simple-table th").addClass("align-left");
				
				// patch for role modal
				if($("#report-list").size()>0){
					$('.label').css('width', '150px');
					//$('.search-input').css('padding', '8px');
				}
				// patch for all search auto-complete
				//$('.search-input').css('padding', '7px');
				
				// patch for h-scrollbar for IE7 and IE8
				if($.template.ie8 || $.template.ie7) {
					$(window).resize(function() {
						handleWindowScroll();
					});
				}
				
				// patch for drilldown page
				$(".drilldown-report-container").find("span").css("vertical-align","middle !important");
				
			}
			
			//=========================== My-Profile TAB FORM VALIDATION ==================================
			$('.myaccount > li > a').on('click', function(event) {
				$(document).click();
				if($("#manageProfile").validationEngine('validate')) {
					return true;
				} else {
					event.preventDefault();
					event.stopPropagation();
					event.stopImmediatePropagation();
					return false;
				}
			});
			
			//=========================== PATCH for removing validation message from modal after close ==================================
			$(".red-hover").live('click', function(event) {
				$('#addNewUser').validationEngine('hide');
				$('#editUser').validationEngine('hide');
				$('#editRoleForm').validationEngine('hide');
				$('#editReportForm').validationEngine('hide');
				$('#addNewReport').validationEngine('hide');
			});
			
			//=========================== PATCH for removing 2nd level menu (some time the menu keeps open) =====================
			$("#open-menu").on('click', function(event) {
				var secondLevelMenu = $("li.with-right-arrow");
				$.each(secondLevelMenu, function(index, value) { 
					$('#select-tooltip-'+index).removeTooltip(true, true);
				});
			});
			
		});
		
		// ==========================PATCH FOR IE FOR SHOWING Windows H-scrollbar in non desktop width ==========================
		function handleWindowScroll() {
			var windowWidth = $(window).width();
			var element = $('html');
			if(windowWidth < 550) {
				element.css('overflow-x', 'auto');
				$('.clearfix').addClass('menu-hidden');
			} else {
				element.css('overflow-x', 'hidden');
				$('.clearfix').removeClass('menu-hidden');
			}
		}
		
		//=========================== Download resources ==================================
		function downloadResources(obj) {
			url = $(obj).attr('param');
			window.open(url);
		}
		
		//=========================== PREPARING SECOND LEVEL MENU ==================================
		// this method is called again from report.js file after menu is loaded with ajax for dashboards
		callbackMenu();
		var timeout = false,
			button = $('li.with-right-arrow'); // The menu trigger
		// Functions
		function menuEnter() {
			 //if (timeout) {
				 clearTimeout(timeout);
				 timeout = false;
			// }
		}
		function secondMenuEnter() {
			 //if (timeout) {
				 clearTimeout(timeout);
				 timeout = false;
			// }
		}
		function menuOut() {
			var buttonid = $(this).attr('id');
			 //if (!timeout) {
				 timeout = setTimeout(function() {
					// Remove tooltip
					//button.removeTooltip();
					$('#'+buttonid+'').removeTooltip();
				 }, 1000);
			 //}
		}
		function secondMenuOut() {
			var buttonid = $(this).attr('id');
			$.each(button, function(index, value) { 
				$('#select-tooltip-'+index).removeTooltip();
			});
		}
		function callbackMenu() {
			var secondLevelMenu = $("li.with-right-arrow");
			$.each(secondLevelMenu, function(index, value) { 
				// Tooltip menu hover
				$('#select-tooltip-'+index).menuTooltip($('#select-context-'+index).hide(), {
					onShow: function() {
						// Watch
						//$(this).add(button).on('mouseenter', menuEnter).on('mouseleave', menuOut);
						$(this).add(button).on('mouseenter', secondMenuEnter);
						//$(this).add(button).on('mouseleave', secondMenuOut);
						$('#select-tooltip-'+index).on('mouseenter', menuEnter).on('mouseleave', menuOut);
						$("li.with-right-arrow").on('mouseenter', menuEnter);
					},
					onRemove: function() {
						// Stop watching
						//$(this).add(button).off('mouseenter', menuEnter).off('mouseleave', menuOut);
						$(this).add(button).off('mouseenter', secondMenuEnter);
						//$(this).add(button).off('mouseleave', secondMenuOut);
						$('#select-tooltip-'+index).off('mouseenter', menuEnter).off('mouseleave', menuOut);
						$("li.with-right-arrow").off('mouseenter', menuEnter);
					},
					classes: ['blue-gradient', 'padding-very-small'],
					position: 'left',
					removeOnClick : true,
					noPointerEvents: false,
					removeOnBlur: true,
					delay: 250
				}, 'mouseenter');
				
				// Tooltip menu click
				$('#select-tooltip-'+index).menuTooltip($('#select-context-'+index).hide(), {
					onShow: function() {
						// Watch
						$(this).add(button).on('mouseenter', menuEnter).on('mouseleave', menuOut);
					},
					onRemove: function() {
						// Stop watching
						$(this).add(button).off('mouseenter', menuEnter).off('mouseleave', menuOut);
					},
					classes: ['blue-gradient', 'padding-very-small'],
					position: 'left',
					removeOnClick : true,
					noPointerEvents: false,
					removeOnBlur: true,
					delay: 250
				}, 'click');
			});
		}
		
		
		
		// ================ Remove duplicate icons in IE ============================
		function manageIconIE(icon) {
			if($.browser.msie) {
				$('.'+icon).html('');
				$('.'+icon+' > .empty').hide();
			}
		}
	</script>