<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<html xml:lang="en" dir="ltr" xmlns="http://www.w3.org/1999/xhtml"
	lang="en">
<head>

<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" href="favicon.ico" mce_href="favicon.ico"/>
<title>Process Tracking</title>

	<link rel="stylesheet" type="text/css" href="css/css_004.css">
	<style type="text/css" title="currentStyle">
		@import "css/media/css/demo_page.css";
		@import "css/media/css/demo_table_jui.css";
		@import "css/themes/ui-lightness/jquery-ui-1.8.17.custom.css";
	</style>
	<link rel="stylesheet" href="css/themes/base/jquery.ui.all.css">
	
	<script src="js/jquery/jquery-1.11.1.min.js"></script>
	<script src="js/jquery/jquery-ui.min.js"></script>
	<script type="text/javascript" language="javascript" src="js/dataTable/1.10.7/jquery.dataTables.min.js"></script>
	<script src="css/jquery.prettyPhoto.js" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" href="css/prettyPhoto.css" type="text/css" media="screen" title="prettyPhoto main stylesheet" charset="utf-8" />

<style>
.buttonLink {
	border: 1px solid #CCC;
	padding: 3px 10px 3px 10px;	
	color: #FFF;
	font-weight:bold;
	background:
		url(css/themes/ui-lightness/images/ui-bg_gloss-wave_35_f6a828_500x100.png)
		top left no-repeat;
}
.regularLink {
	color: #00329B !important;
	font-weight: bold;	
}
.ui-dialog .ui-dialog-titlebar-close span{
	margin: -8px;
}

</style>
</head>
    <body>
    	<tiles:insertAttribute name="header" />
               
        <tiles:insertAttribute name="body" />
               
        <tiles:insertAttribute name="footer" />
    </body>
</html>
