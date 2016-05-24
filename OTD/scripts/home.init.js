/*
 * Copyright (C) 2005 - 2011 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */

document.observe('dom:loaded', function() {
    var options = {
        //Map button IDs to some location. If location is string then browser is redirected to that URL,
        //if location is a function then this function is executed on button click.
        locationMap: {
           'viewReports':'flow.html?_flowId=searchFlow&mode=search&filterId=resourceTypeFilter&filterOption=resourceTypeFilter-reports&searchText=',
           'createView':'flow.html?_flowId=adhocFlow',
           'createReport': JRS.CreateReport.selectADV,
           'createDashboard':'flow.html?_flowId=dashboardDesignerFlow&createNew=true',
           'analyzeResults':'flow.html?_flowId=searchFlow&mode=search&filterId=resourceTypeFilter&filterOption=resourceTypeFilter-view&searchText=',
           'manageServer':'flow.html?_flowId=adminHomeFlow'
        }
    };
    home.initialize(options);
});
