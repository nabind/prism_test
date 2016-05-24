/*
 * Copyright (C) 2005 - 2012 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */

AdHocTable.initOverlays = function(){
    AdHocTable.initColumnOverlays();
    AdHocTable.initColumnResizers();
    AdHocTable.initSummaryOverlay();
}

AdHocTable.initRowDependentDimensions = function(){
    AdHocTable.lastRow = AdHocTable.theRows.length - 1;
    if (AdHocTable.theRows[AdHocTable.lastRow].id == "NoDataRow") {
        AdHocTable.lastRow = AdHocTable.lastRow - 1;
    }
    AdHocTable.columnHeaderRow = $("columnHeaderRow");
}

AdHocTable.initNewRows = function(){
    AdHocTable.state.endOfFile = $("endOfFileRow") || (AdHocTable.existingRowCount == AdHocTable.theRows.length);
    AdHocTable.theRows = $('canvasTable').rows;
    window.status = "total rows = " + AdHocTable.theRows.length;

    if (this.shouldFetchMoreRows()) {
        setTimeout("localContext.fetchMoreRows()", 100);
        return;
    }

    AdHocTable.initRowDependentDimensions();
    AdHocTable.initOverlays();

    AdHocTable.existingRowCount = AdHocTable.theRows.length;
    AdHocTable.fetchingRows = false;
}

AdHocTable.initColumnResizers = function(){
    var miniHack = 3;
    var columnHeaders = AdHocTable.columnHeaderRow.cells;
    designerBase.clearOverlaySet(AdHocTable.columnResizers);
    var tableTop = this.getTableTop();
    var tableHeight = this.getTableHeight();

    for(var index = 0; index < columnHeaders.length; index++){
        var columnHeader = columnHeaders[index];
        var columnOverlaySizer = designerBase.createDomObject("DIV", "columnSizer");
        columnOverlaySizer.writeAttribute("id", "columnSizer_" + index);
        var tempLeft = ($(columnHeader).cumulativeOffset()[0] - $("mainTableContainer").cumulativeOffset()[0] - miniHack);
        var columnLeft = tempLeft + columnHeader.offsetWidth + "px";

        columnOverlaySizer.setStyle({
            'left': columnLeft,
            'top' : tableTop,
            'height' : tableHeight
        });
        AdHocTable.columnResizers[index] = columnOverlaySizer;
        adhocDesigner.overlayParent.appendChild(columnOverlaySizer);
    }
}

AdHocTable.initColumnOverlays = function(){
    var columnHeaders = AdHocTable.columnHeaderRow.cells;
    var count = columnHeaders.length;
    designerBase.clearOverlaySet(AdHocTable.columnOverlays);
    var tableTop = this.getTableTop();
    var tableHeight = this.getTableHeight();

    for(var index = 0, realIndex = 0; index < count; index++){
        var columnHeader = columnHeaders[index];
        if (!$(columnHeader).hasClassName('label')) {
            continue;
        }
        var columnOverlay = designerBase.createDomObject("DIV", "overlay col");
        columnOverlay.writeAttribute("id", "columnOverlay_" + realIndex);
        var columnLeft = ($(columnHeader).cumulativeOffset()[0] - $("mainTableContainer").cumulativeOffset()[0]) + "px";
        var columnWidth = columnHeader.offsetWidth + "px";

        columnOverlay.setStyle({
            'left': columnLeft,
            'width': columnWidth,
            'top' : tableTop,
            'height' : tableHeight
        });
        AdHocTable.columnOverlays[realIndex] = columnOverlay;
        adhocDesigner.overlayParent.appendChild(columnOverlay);
        realIndex = realIndex + 1;
    }
}

AdHocTable.initSummaryOverlay = function(){
    designerBase.clearOverlaySet(AdHocTable.summaryOverlays);
    if ($("grandSummaryRow")) {
        var summaryCells = $("grandSummaryRow").cells;
        var count = summaryCells.length;

        for(var index = 0; index < count; index++){
            var summaryCell = summaryCells[index];
            var summaryCellIndex = $(summaryCell).cellIndex;

            var summaryOverlay = designerBase.createDomObject("DIV", "overlay summary button");
            summaryOverlay.writeAttribute("id", "grandSummaryOverlay_" + summaryCellIndex);
            summaryOverlay.writeAttribute("data-summaryIndex", summaryCellIndex);
            summaryOverlay.setStyle({
                'left': ($(summaryCell).cumulativeOffset()[0] - $("mainTableContainer").cumulativeOffset()[0])  + "px",
                'width': ($(summaryCell).offsetWidth - 2) + "px",
                'top' :  ($(summaryCell).offsetTop + $("canvasTable").offsetTop )+ "px",
                'height' : $(summaryCell).offsetHeight + "px"
            });
            AdHocTable.summaryOverlays[index] = summaryOverlay;
            adhocDesigner.overlayParent.appendChild(summaryOverlay);
        }
    }
}

