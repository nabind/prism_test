<%@page import="com.vaannila.TO.TASCProcessTO"%>
<%@page import="javax.servlet.http.HttpServletRequest" %>
<%@page import="com.vaannila.TO.OrgProcess" %>
<%@page import="com.vaannila.TO.AdminTO" %>
<%@page import="java.util.ArrayList" %>
<%@page import="com.vaannila.web.UserController" %>
<%@page import="java.util.Properties" %>
<%@page import="com.vaannila.util.PropertyFile" %>

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<script type="text/javascript">
$(function () {
        $('#container').highcharts({
            chart: {
                zoomType: 'x',
                spacingRight: 20
            },
            title: {
                text: 'Request count for data load at TASC PRISM'
            },
            subtitle: {
                text: document.ontouchstart === undefined ?
                    'Click and drag in the plot area to zoom in' :
                    'Pinch the chart to zoom in'
            },
            xAxis: {
                type: 'datetime',
                maxZoom: 3 * 24 * 3600000, // fourteen days
                title: {
                    text: null
                }
            },
            yAxis: {
                title: {
                    text: 'Request Count'
                }
            },
            credits: {
                enabled: false
            },
            tooltip: {
                shared: true
            },
            legend: {
                enabled: true
            },
            plotOptions: {
                area: {
                    fillColor: {
                        linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1},
                        stops: [
                            [0, Highcharts.getOptions().colors[0]],
                            [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                        ]
                    },
                    lineWidth: 1,
                    marker: {
                        enabled: false
                    },
                    shadow: false,
                    states: {
                        hover: {
                            lineWidth: 1
                        }
                    },
                    threshold: null
                }
            },
    
            series: [{
                type: 'column',
                name: 'Paper Pencil',
                pointInterval: 24 * 3600 * 1000,
                pointStart: Date.UTC(<%= (String) request.getSession().getAttribute("tascStartDate") %>),
                data: [
                    <%= (String) request.getSession().getAttribute("tascPPCountList") %>
                ]
            },
            {
                type: 'column',
                name: 'Web Service',
                pointInterval: 24 * 3600 * 1000,
                pointStart: Date.UTC(<%= (String) request.getSession().getAttribute("tascStartDate") %>),
                data: [
					<%= (String) request.getSession().getAttribute("tascOLCountList") %>
                ],
                color: '#8bbc21'
            }
            ]
        });
        // ============================================== GRAPH 2 ==============================================
        $('#container1').highcharts({
            chart: {
                zoomType: 'x',
                spacingRight: 20
            },
            title: {
                text: 'Status count for data load at TASC PRISM'
            },
            subtitle: {
                text: document.ontouchstart === undefined ?
                    'Click and drag in the plot area to zoom in' :
                    'Pinch the chart to zoom in'
            },
            xAxis: {
                type: 'datetime',
                maxZoom: 3 * 24 * 3600000, // fourteen days
                title: {
                    text: null
                }
            },
            yAxis: {
                title: {
                    text: 'Count'
                }
            },
            credits: {
                enabled: false
            },
            tooltip: {
                shared: true
            },
            legend: {
                enabled: true
            },
            plotOptions: {
                
            },
    
            series: [{
                type: 'column',
                name: 'Success',
                pointInterval: 24 * 3600 * 1000,
                pointStart: Date.UTC(<%= (String) request.getSession().getAttribute("tascStartDate") %>),
                data: [
                    <%= (String) request.getSession().getAttribute("tascCOCountList") %>
                ],
                color: '#36A502'
            },
            {
                type: 'column',
                name: 'Error',
                pointInterval: 24 * 3600 * 1000,
                pointStart: Date.UTC(<%= (String) request.getSession().getAttribute("tascStartDate") %>),
                data: [
					<%= (String) request.getSession().getAttribute("tascERCountList") %>
                ],
                color: '#BD0606'
            }, {
                type: 'pie',
                name: 'Total count',
                data: [{
                    name: 'Success',
                    y: <%= (String) request.getSession().getAttribute("successCount") %>,
                    color: '#36A502'
                }, {
                    name: 'Error',
                    y: <%= (String) request.getSession().getAttribute("errorCount") %>,
                    color: '#BD0606'
                }],
                center: [100, 80],
                size: 100,
                showInLegend: false,
                dataLabels: {
                    enabled: true
                }
            }
            ]
        });
    });
    

</script>
<script src="js/highcharts.js"></script>
<script src="js/modules/exporting.js"></script>
	
<div id="heromaskarticle" style="min-height: 531px;">
	<div id="articlecontent">
	 
		<div id="accordion">
				<div id="container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
		</div>

	</div>
</div>
<div id="heromaskarticle">
	<div id="articlecontent">
	 
		<div id="accordion">
				<div id="container1" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
		</div>

	</div>
</div>
</div>
