<!DOCTYPE html>

<html>
<head>
	
	<title>Data Point labels</title>

    <link class="include" rel="stylesheet" type="text/css" href="jquery.jqplot.min.css" />
    <link rel="stylesheet" type="text/css" href="examples.min.css" />
    <link type="text/css" rel="stylesheet" href="syntaxhighlighter/styles/shCoreDefault.min.css" />
    <link type="text/css" rel="stylesheet" href="syntaxhighlighter/styles/shThemejqPlot.min.css" />
  
  <!--[if lt IE 9]><script language="javascript" type="text/javascript" src="../excanvas.js"></script><![endif]-->
    <script class="include" type="text/javascript" src="jquery.min.js"></script>
    
	<style>
		.table {border:1px solid #CCC}
		.table tr {border:1px solid #CCC}
		.table tr td {border:1px solid #CCC}
	</style>
   
</head>
<body>
    <div class="logo">
   <div class="nav">
   
   </div>
</div>
    <div class="example-content">

    <div class="example-nav">


<!-- Example scripts go here -->


<div id="chart4" style="height:400px; width:800px;"></div>

<div  style="width:750px"><p>One year scale score growth for Kiara Aman from Grade 4 to Grade 5 is -46. To reach a level of proficiency in
three years, the student must achieve an average scale score growth of 18.7 during each of the following three
years. The actual one year growth rate is lower than the rate that must be maintained to achieve a level of
proficiency.</P></div>
<div>
	<table>
		<tr>
			<td width="20%">Student:</td>
			<td colspan="4">KIARA AMAN - 1065995966</td>
		</tr>
		<tr>
			<td width="20%">Current District:</td>
			<td colspan="4">30-001 Mandan 1 (PK-12)</td>
		</tr>
		<tr>
			<td width="20%">Current School:</td>
			<td colspan="4">30-001-8286 Mary Stark Elem School (0K05)</td>
		</tr>
	</table>
	<table class="table" cellspacing="0">
		<tr>
			<td>Grade</td>
			<td colspan="4">Reading</td>
			<td colspan="2">North Dakota State Assessment Information</td>
		</tr>
		<tr>
			<td></td>
			
			<td>Student Score</td>
			<td>Achievement Level</td>
			<td>Achievement Percentile</td>
			<td>3-Year Path</td>
			
			<td>Date</td>
			<td>School Attended</td>
		</tr>
		<tr>
			<td>3</td>
			
			<td>638</td>
			<td>P</td>
			<td>60</td>
			<td></td>
			
			<td>Fall'08</td>
			<td>Mary Stark Elem School</td>
		</tr>
		<tr>
			<td>4</td>
			
			<td>660</td>
			<td>P</td>
			<td>68</td>
			<td></td>
			
			<td>Fall'09</td>
			<td>Mary Stark Elem School</td>
		</tr>
		<tr>
			<td>5</td>
			
			<td>614</td>
			<td>N</td>
			<td>4</td>
			<td>614</td>
			
			<td>Fall'10</td>
			<td>Mary Stark Elem School</td>
		</tr>
		<tr>
			<td>6</td>
			
			<td></td>
			<td></td>
			<td></td>
			<td>633</td>
			
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>7</td>
			
			<td></td>
			<td></td>
			<td></td>
			<td>651</td>
			
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>8</td>
			
			<td></td>
			<td></td>
			<td></td>
			<td>670</td>
			
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>9</td>
			
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>10</td>
			
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>11</td>
			
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			
			<td></td>
			<td></td>
		</tr>
	</table>
</div>
<div style="width:750px"><p>Growth percentile from fall 2009 to fall 2010 = 0 (low). The Growth Percentile presents the relative level of growth
that a student has made since the previous year as compared to students within the same achievement level
statewide.</p></div>
<br/><br/><br/><br/><br/><br/>



<script class="code" type="text/javascript">

$(document).ready(function(){
  
  var line1 = [[3,650], [4,670], [5,690], [6,696], [7,707], [8,714], [9,722], [10,730], [11,738]];
  var line2 = [[3,610], [4,630], [5,645], [6,655], [7,666], [8,670], [9,680], [10,690], [11,700]];
  var line3 = [[3,579], [4,606], [5,619], [6,632], [7,644], [8,648], [9,658], [10,668], [11,679]];
  
  var line4 = [[3,638], [4,660], [5,614], [6,], [7,], [8,], [9,], [10,], [11,]];
  var line5 = [[3,], [4,], [5,0], [6,633], [7,651], [8,670], [9,], [10,], [11,]];
  
  var plot4 = $.jqplot('chart4', [line1, line2, line3, line4, line5], {
      title: 'Stacked Bar Chart with Cumulative Point Labels', 
	  seriesColors: [ "#9bbb59", "#be4c49", "#4f81bd", "#8064a2", "#46aac5"],
      stackSeries: true, 
	  series:[
		{renderer:$.jqplot.BarRenderer}, 
		{renderer:$.jqplot.BarRenderer}, 
		{renderer:$.jqplot.BarRenderer}, 
		{}, 
		{	
            linePattern: 'dashed',
			markerOptions: { style:"filledSquare", size:10}
		}
	  ],
      seriesDefaults: {
          rendererOptions:{barMargin: 25}, 
          pointLabels:{show:true}
      },
      axes: {
      xaxis: {
        renderer: $.jqplot.CategoryAxisRenderer
      },
      x2axis: {
        renderer: $.jqplot.CategoryAxisRenderer
      },
      yaxis: {
        autoscale:true
      },
      y2axis: {
        autoscale:true
      }
    }
  });
  
  
});

/*

var line1 = [['Cup Holder Pinion Bob', 7], ['Generic Fog Lamp', 9], ['HDTV Receiver', 15], 
  ['8 Track Control Module', 12], [' Sludge Pump Fourier Modulator', 3], 
  ['Transcender/Spice Rack', 6], ['Hair Spray Danger Indicator', 18]];
  var line2 = [['Nickle', 28], ['Aluminum', 13], ['Xenon', 54], ['Silver', 47], 
  ['Sulfer', 16], ['Silicon', 14], ['Vanadium', 23]];
 
  var plot2 = $.jqplot('chart2', [line1, line2], {
    series:[{renderer:$.jqplot.BarRenderer}, {xaxis:'x2axis', yaxis:'y2axis'}],
    axesDefaults: {
        tickRenderer: $.jqplot.CanvasAxisTickRenderer ,
        tickOptions: {
          angle: 30
        }
    },
    axes: {
      xaxis: {
        renderer: $.jqplot.CategoryAxisRenderer
      },
      x2axis: {
        renderer: $.jqplot.CategoryAxisRenderer
      },
      yaxis: {
        autoscale:true
      },
      y2axis: {
        autoscale:true
      }
    }
  });

*/




$(document).ready(function(){
  var line1 = [[3,650], [4,670], [5,690], [6,696], [7,707], [8,714], [9,722], [10,730], [11,738]];
  var line2 = [[3,610], [4,630], [5,645], [6,655], [7,666], [8,670], [9,680], [10,690], [11,700]];
  var line3 = [[3,579], [4,606], [5,619], [6,632], [7,644], [8,648], [9,658], [10,668], [11,679]];
  var line4 = [[3,638], [4,660], [5,614]];
  var line5 = [[3,], [4,], [5,614], [6,633], [7,651], [8,670]];
  var plot1 = $.jqplot('chart1', [line1, line2, line3, line4, line5], {
      title: 'Student Growth Projection<br>KIARA AMAN - 1065995966<br>Reading', 
	  seriesColors: [ "#9bbb59", "#be4c49", "#4f81bd", "#8064a2", "#46aac5"],
      seriesDefaults: { 
        showMarker:true,
        pointLabels: { show:true } 
      },series:[ 
          {
            label: 'Advanced Cut Scores',
			lineWidth:2, 
			pointLabels: { location: 'n' },
            markerOptions: { style:'filledDimaond' }
          }, 
          {
			label: 'Proficient Cut Scores',
            showLine:true, 
			pointLabels: { location: 'ew' },
            markerOptions: { size: 7, style:"filledCircle" }
          },
		  {
		    label: 'Partially Proficient Cut Scores',
            pointLabels: { location: 's' },
            markerOptions: { size: 7, style:"x" }
          },
          { 
		    label: 'Student Score',
			lineWidth:5,
			pointLabels: { location: 'e' },
            markerOptions: { style:"circle" }
          }, 
          {
		    label: '3-Year Path',
            lineWidth:5, 
			pointLabels: { show:false },
			linePattern: 'dashed',
            markerOptions: { style:"filledSquare", size:10 }
          }/*,
		  {
            label: '',
			lineWidth:0, 
			showLine:false,
			pointLabels: { show:false },
			showMarker:false,
            markerOptions: { style:"filledSquare", size:1 }
          }*/
      ],
	  legend: {
        show: true,
        location: 'se',     // compass direction, nw, n, ne, e, se, s, sw, w.
        xoffset: 5,        // pixel offset of the legend box from the x (or x2) axis.
        yoffset: 12,        // pixel offset of the legend box from the y (or y2) axis.
	  },
	  axes: {
        xaxis: {
            // same options as axesDefaults
			min:1
        },
        yaxis: {
            // same options as axesDefaults
        }
	  }
  });
  
  
  var line4_1 = [[3,],[4,599], [5,611]];
  var line5_1 = [[3,], [4,], [5,611], [6,631], [7,650], [8,670]];
  var plot1 = $.jqplot('chart2', [line1, line2, line3, line4_1, line5_1], {
      title: 'Student Growth Projection<br>KIARA AMAN - 1065995966<br>Reading', 
	  seriesColors: [ "#9bbb59", "#be4c49", "#4f81bd", "#8064a2", "#46aac5"],
      seriesDefaults: { 
        showMarker:true,
        pointLabels: { show:true } 
      },series:[ 
          {
            label: 'Advanced Cut Scores',
			lineWidth:2, 
			pointLabels: { location: 'n' },
            markerOptions: { style:'filledDimaond' }
          }, 
          {
			label: 'Proficient Cut Scores',
            showLine:true, 
			pointLabels: { location: 'w' },
            markerOptions: { size: 7, style:"filledCircle" }
          },
		  {
		    label: 'Partially Proficient Cut Scores',
            pointLabels: { location: 's' },
            markerOptions: { size: 7, style:"x" }
          },
          { 
		    label: 'Student Score',
			lineWidth:5,
			pointLabels: { location: 'e' },
            markerOptions: { style:"circle" }
          }, 
          {
		    label: '3-Year Path',
            lineWidth:5, 
			pointLabels: { show:false },
			linePattern: 'dashed',
            markerOptions: { style:"filledSquare", size:10 }
          }/*,
		  {
            label: '',
			lineWidth:0, 
			showLine:false,
			pointLabels: { show:false },
			showMarker:false,
            markerOptions: { style:"filledSquare", size:1 }
          }*/
      ],
	  legend: {
        show: true,
        location: 'se',     // compass direction, nw, n, ne, e, se, s, sw, w.
        xoffset: 5,        // pixel offset of the legend box from the x (or x2) axis.
        yoffset: 12,        // pixel offset of the legend box from the y (or y2) axis.
	  },
	  axes: {
        xaxis: {
            // same options as axesDefaults
			min:1
        },
        yaxis: {
            // same options as axesDefaults
        }
	  }
  });
  
  
  var line4_2 = [[3,633], [4,660], [5,666]];
  var line5_2 = [[3,], [4,], [5,666], [6,667], [7,669], [8,670]];
  var plot1 = $.jqplot('chart3', [line1, line2, line3, line4_2, line5_2], {
      title: 'Student Growth Projection<br>KIARA AMAN - 1065995966<br>Reading', 
	  seriesColors: [ "#9bbb59", "#be4c49", "#4f81bd", "#8064a2", "#46aac5"],
      seriesDefaults: { 
        showMarker:true,
        pointLabels: { show:true } 
      },series:[ 
          {
            label: 'Advanced Cut Scores',
			lineWidth:2, 
			pointLabels: { location: 'n' },
            markerOptions: { style:'filledDimaond' }
          }, 
          {
			label: 'Proficient Cut Scores',
            showLine:true, 
			pointLabels: { location: 'w' },
            markerOptions: { size: 7, style:"filledCircle" }
          },
		  {
		    label: 'Partially Proficient Cut Scores',
            pointLabels: { location: 's' },
            markerOptions: { size: 7, style:"x" }
          },
          { 
		    label: 'Student Score',
			lineWidth:5,
			pointLabels: { location: 'e' },
            markerOptions: { style:"circle" }
          }, 
          {
		    label: '3-Year Path',
            lineWidth:5, 
			pointLabels: { show:false },
			linePattern: 'dashed',
            markerOptions: { style:"filledSquare", size:10 }
          }/*,
		  {
            label: '',
			lineWidth:0, 
			showLine:false,
			pointLabels: { show:false },
			showMarker:false,
            markerOptions: { style:"filledSquare", size:1 }
          }*/
      ],
	  legend: {
        show: true,
        location: 'se',     // compass direction, nw, n, ne, e, se, s, sw, w.
        xoffset: 5,        // pixel offset of the legend box from the x (or x2) axis.
        yoffset: 12,        // pixel offset of the legend box from the y (or y2) axis.
	  },
	  axes: {
        xaxis: {
            // same options as axesDefaults
			min:1
        },
        yaxis: {
            // same options as axesDefaults
        }
	  }
  });
  
});
</script>


</script>

<!-- End example scripts -->

<!-- Don't touch this! -->


    <script class="include" type="text/javascript" src="jquery.jqplot.min.js"></script>
    <script type="text/javascript" src="syntaxhighlighter/scripts/shCore.min.js"></script>
    <script type="text/javascript" src="syntaxhighlighter/scripts/shBrushJScript.min.js"></script>
    <script type="text/javascript" src="syntaxhighlighter/scripts/shBrushXml.min.js"></script>
<!-- End Don't touch this! -->

<!-- Additional plugins go here -->
    <script class="include" language="javascript" type="text/javascript" src="plugins/jqplot.barRenderer.min.js"></script>
    <script class="include" language="javascript" type="text/javascript" src="plugins/jqplot.categoryAxisRenderer.min.js"></script>
    <script class="include" language="javascript" type="text/javascript" src="plugins/jqplot.pointLabels.min.js"></script>

<!-- End additional plugins -->


	</div>	
	<script type="text/javascript" src="example.min.js"></script>

</body>


</html>