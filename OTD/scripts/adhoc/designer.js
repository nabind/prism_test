/*
 * Copyright (C) 2005 - 2012 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */

JRS.vars.current_flow = 'adhoc';

var localContext = window;
var theBody = document.body;
var requestLogEnabled = false;
var adhocSessionButton;
var adhocSessionDialog;
var TIMEOUT_INTERVAL = serverTimeoutInterval * 1000; //since intervals are in milli-secs we need to multiply by 1000
var ADHOC_SESSION_TIMEOUT_MESSAGE = adHocSessionExpireCode;
var ADHOC_EXIT_MESSAGE = adHocExitConfirmation;

var adhocDesigner = {
	worksheets: [],
    currentWorksheet: null,

	ui: {
        header_title: null,
        display_manager: null,
        canvas: null,
        dataMode: null
	},

    //member variables
    _leafSelectedFired : false,

    dimensionsTree : null,
    measuresTree : null,

    _availableTreeLastOpened : null,
    _AVAILABLE_TREE_DEPTH : 10,
    _cookieName : "lastNodeUri",
    _cookieTime : 3,
    FOLDER_TYPE : "ItemGroupType",
    multiSelect : false,

    //Name of measures dimension and measures level
    MEASURES : "Measures",

    //For OLAP mode
    DIMENSIONS_TREE_DOM_ID : "dimensionsTree",
    DIMENSIONS_TREE_PROVIDER_ID : "dimensionsTreeDataProvider",
    MEASURES_TREE_DOM_ID : "measuresTree",
    MEASURES_TREE_PROVIDER_ID : "measuresTreeDataProvider",

    TREE_CONTEXT_MENU_PATTERN: [
        'ul#dimensionsTree li.leaf .button',
        'ul#dimensionsTree li.node .button',
        'ul#measuresTree li.leaf .button',
        'ul#measuresTree li.node .button'],
    DIMENSION_TREE_DIMENSION_CONTEXT: "dimensionsTree_dimension",
    DIMENSION_TREE_LEVEL_CONTEXT: "dimensionsTree_level",
    MEASURE_TREE_GROUP_CONTEXT: "measuresTree_group",
    MEASURE_TREE_CONTEXT : "measuresTree",
    TREE_NODE_AND_LEAF_PATTERN:
        ['ul#visibleFieldsTree li.leaf', 'ul#visibleFieldsTree li.node',
         'ul#dimensionsTree li.leaf', 'ul#dimensionsTree li.node', 'ul#measuresTree li.node'],

    CANVAS_ID : "canvasTable",
    CANVAS_PARENT_ID : "mainTableContainer",
    CANVAS_PANEL_ID : "canvas",
    OLAP_MEASURES_TREE: "measuresTree",
    DISPLAY_MANAGER_ID: "displayManagerPanel",
    overlayParent : null,
    overlayDraggedColumn : null,
    initialDragXposition : null,
    NaN : "NaN",
    removeDroppables : null,
    addDroppables : null,
    DEFAULT_SUMMARY_NUM_FUNC : "Sum",
    DEFAULT_SUMMARY_NONNUM_FUNC : "DistinctCount",
    //patterns
    //table patterns
    COLUMN_OVERLAY_PATTERN : "div.overlay.col",
    GROUP_OVERLAY_PATTERN : "div.overlay.group",
    SUMMARY_OVERLAY_PATTERN : "div.overlay.summary",
    GROUP_LABEL_SPAN_PATTERN : "span.labelOverlay.label",
    COLUMN_SIZER_PATTERN : "div.columnSizer",

    ROW_OVERLAY_PATTERN : "div.rowOverlay",
    ROW_GROUP_OVERLAY_PATTERN : "div.rowGroupOverlay",
    COLUMN_GROUP_OVERLAY_PATTERN : "div.columnGroupOverlay",
    MEASURE_OVERLAY_PATTERN : "div.measureOverlay",

    XTAB_GROUP_HEADER_PATTERN : "th.label.group",
    XTAB_GROUP_OVERLAY_PATTERN : "div.overlay.xtab.gr",
    XTAB_GROUP_HEADER_OVERLAY_PATTERN : "div.overlay.xtab.header",

    XTAB_MEASURE_OVERLAY_PATTERN : "div.overlay.xtab.m",
    XTAB_MEASURE_HEADER_OVERLAY_PATTERN : "div.overlay.xtab.measure",

    ROW_GROUP_MEMBER_PATTERN : "tbody#detailRows tr td.label.member",
    COLUMN_GROUP_MEMBER_PATTERN : "thead#headerAxis th.label.member",
    LEGEND_OVERLAY_PATTERN : "div.legend.overlay",

    AVAILABLE_FIELDS_PATTERN : ["ul#visibleFieldsTree", "ul#dimensionsTree", "ul#measuresTree"],
    CANVAS_PATTERN : "table#canvasTable",
    MENU_PATTERN : "div#menu",
    CANVAS_PARENT_PATTERN : "div#mainTableContainer",
    EXPORT_FORM_PATTERN : "#exportActionForm",

    ///////////////////////////////////////////
    //Type conversation constants
    ///////////////////////////////////////////

    INTEGER_JAVA_TYPES: ["java.lang.Byte", "java.lang.Integer", "java.lang.Short", "java.lang.Long", "java.math.BigInteger"],
    DECIMAL_JAVA_TYPES: ["java.lang.Float", "java.lang.Double", "java.math.BigDecimal", "java.lang.Number"],
    DATE_JAVA_TYPES: ["java.sql.Timestamp", "java.sql.Time", "java.sql.Date", "java.util.Date"],
    BOOLEAN_JAVA_TYPES: ["java.lang.Boolean"],

    DATE_TYPE_DISPLAY: "date",
    INTEGER_TYPE_DISPLAY: "int",
    DECIMAL_TYPE_DISPLAY: "dec",
    BOOLEAN_TYPE_DISPLAY: "bool",
    NOT_A_NUMBER_TYPE_DISPLAY: "NaN",

    //action array
    toolbarActionMap : {
        presentation : "adhocDesigner.goToPresentationView",
        explorer : "adhocDesigner.goToDesignView",
        execute : "adhocDesigner.saveAndRun",
        undo : "adhocDesigner.undo",
        redo : "adhocDesigner.redo",
        undoAll : "adhocDesigner.undoAll",
        pivot : "adhocDesigner.pivot",
        sort : "adhocDesigner.sort",
        controls : "adhocDesigner.launchDialogMenu",
        styles : "adhocDesigner.showAdhocThemePane",
        query : "adhocDesigner.showViewQueryDialog"
    },

    dialogESCFunctions : {
        save : "saveAs",
        saveDataViewAndReport : "saveDataViewAndReport",
        sort : "sortDialog",
        reentrant : "selectFields",
        editLabel: "editLabel"
    },

    contextMap: {
        table : AdHocTable,
        crosstab : AdHocCrosstab,
        olap_crosstab : AdHocCrosstab,
        chart : AdHocChart,
        ichart: AdhocIntelligentChart,
        olap_ichart: AdhocIntelligentChart
    },

    ///////////////////////////////////////////////////////////////
    // Type conversation helper functions
    ///////////////////////////////////////////////////////////////
    isIntegerType: function(type) {
        return adhocDesigner.INTEGER_JAVA_TYPES.indexOf(type) >= 0;
    },

    isDecimalType: function(type) {
        return adhocDesigner.DECIMAL_JAVA_TYPES.indexOf(type) >= 0;
    },

    isDateType: function(type) {
        return adhocDesigner.DATE_JAVA_TYPES.indexOf(type) >= 0;
    },

    isBooleanType: function(type) {
        return adhocDesigner.BOOLEAN_JAVA_TYPES.indexOf(type) >= 0;
    },

    getSuperType: function(type) {
        if (adhocDesigner.isIntegerType(type)) {
            return adhocDesigner.INTEGER_TYPE_DISPLAY;
        } else if (adhocDesigner.isDecimalType(type)) {
            return adhocDesigner.DECIMAL_TYPE_DISPLAY;
        } else if (adhocDesigner.isDateType(type)) {
            return adhocDesigner.DATE_TYPE_DISPLAY;
        } else {
            return adhocDesigner.NOT_A_NUMBER_TYPE_DISPLAY;
        }
    },

    /*
    * Todo: Should be refactored inline
    */
    getSelectedColumnOrGroup: function(){
        return selObjects[0];
    },
    generalDesignerCallback: function(){
        localContext.initAll();
        adhocDesigner.updateTrees();
    },
    run: function(mode){
        // Setup Web Help
        webHelpModule.setCurrentContext(mode.indexOf('olap') >= 0 ? "analysis" : "ad_hoc");
        // Init UI elements
        this.ui.dataMode = jQuery('#dataSizeSelector');
        this.ui.canvas = isSupportsTouch() ? jQuery('#mainTableContainer > .scrollWrapper') : jQuery('#mainTableContainer');
        this.ui.header_title = jQuery('#canvas > div.content > div.header > div.title');
        /*
         * Events
         */
        this.observePointerEvents();
        this.observeKeyEvents();
        this.observeCustomEvents();
        this.observeTableContainerEvents();
        /*
         * DnD
         */
        this.initDroppables();
        /*
         * Worksheet
         */
        this.currentWorksheet = this.addWorksheet(mode);
        // Initialize Mode dependent Ad Hoc Designer components
        this.initComponents(mode);
        this.currentWorksheet.load();
        /*
         * UI
         */
        toolbarButtonModule.initialize(this.toolbarActionMap, $("adhocToolbar"));

        _.extend(this, {
            selectFields: function(){
                adhocReentrance.launchDialog();
            },
            launchDialogMenu: function(){
                adhocControls.launchDialog();
            },
            showViewQueryDialog: function() {
                adhocDesigner.viewQueryDialog.show();
            },
            sort: function(){
                adhocSort.launchDialog();
            },
            createCalculatedField: function(){
                adhocCalculatedFields.launchDialog();
            },
            editCalculatedField: function(){
                adhocCalculatedFields.launchDialog(true);
            }
        });

        if(isSupportsTouch()){
            var wrapper = this.ui.canvas.get(0);
            this._touchController = new TouchController(wrapper,wrapper.parentNode,{
                useParent: true,
                absolute: true,
                scrollbars: true
            });
        }

        var mainPanelID = adhocDesigner.CANVAS_PANEL_ID;
        if($('fields')) {
            layoutModule.resizeOnClient('fields', mainPanelID, 'filters');
        } else {
            layoutModule.resizeOnClient('filters', mainPanelID);
        }
        /*
         * TODO: make this UI update happen in CSS using media queries.
         */
        isIPad() && layoutModule.minimize(document.getElementById('filters'), true);

        this.initTitle();
        this.initFieldsPanel(true);
        this.initFiltersPanel(true);
        this.initDialogs();
        typeof window.orientation !== 'undefined' && window.orientation === 0 && this.hideOnePanel();
        /*
         * Error on load?
         */
        $("errorPageContent") ? adhocDesigner.initEnableBrowserSelection($("designer")) : adhocDesigner.initPreventBrowserSelection($("designer"));
    },
    initComponents: function(mode){
        // Init Crosstab mode variables
        this.isCrosstabMode = mode.indexOf('ichart') >= 0 || mode.indexOf('crosstab') >= 0;
        var isIntelligentChart = mode === designerBase.OLAP_ICHART || mode === designerBase.ICHART;

        if (isIntelligentChart) {
            adHocFilterModule.CONTROLLER_PREFIX = "ich";
        } else {
            adHocFilterModule.CONTROLLER_PREFIX = this.isCrosstabMode ? "cr" : "co";
        }

        this.ui.canvas.empty();
        jQuery('#level-container').hide();
        jQuery('#dataModeSelector').val(mode);

        // Set up local context variable
        localContext = this.contextMap[mode];
        localContext.setMode && localContext.setMode(mode);
        localContext.init && localContext.init(mode);
        localContext.reset();
        // Setup Current worksheet
        adhocDesigner.currentWorksheet.setMode(mode);
        // Register Report Template
        if(mode.indexOf('ichart') < 0) adhocDesigner.registerTemplate(localContext, mode + "Template");
        // Init Layout Manager instance
        this.initLayoutManager(mode);
        // Update Data Mode panel appearance
        (mode.indexOf('chart') >= 0 || mode.indexOf('olap') >= 0) ? this.ui.dataMode.hide() : this.ui.dataMode.show();
        // Prepare axes labels
        jQuery('#columns').children().eq(0).html(layoutManagerLabels.column[mode]);
        jQuery('#rows').children().eq(0).html(layoutManagerLabels.row[mode]);
    },
    render: function(state){
        toolbarButtonModule.setActionModel(state.actionmodel);
        adhocDesigner.currentWorksheet.setActionModel(state.actionmodel);

        if(localContext.getMode() != designerBase.ICHART &&
            localContext.getMode() != designerBase.OLAP_ICHART ) {
                adhocDesigner.ui.canvas.empty();
        }
        adhocDesigner.updateCanvasClasses(adhocDesigner.isCrosstabMode);

        adhocDesigner.updateState(state);
        var isDataRendered = localContext.render();

        if (isDesignView) {  //save and undo buttons are disabled in report display view
            adhocDesigner.enableCanUndoRedo();
            adhocDesigner.enableRunAndSave(localContext.canSaveReport());
        }

        if(isDataRendered) {
            editor = null;
            designerBase.initAdhocSpecificDesignerBaseVar();
            designerBase.setState();
            designerBase.updateSessionWarning();
            designerBase.updateFlowKey();
            if (localContext.initAll) {
                localContext.initAll();
            }
            designerBase.unSelectAll();
        }

        if (isDesignView && adhocDesigner.isDisplayManagerVisible()) {
            jQuery("#" + adhocDesigner.DISPLAY_MANAGER_ID).removeClass(layoutModule.HIDDEN_CLASS);
        }

        adhocDesigner.updateModeLabelSelection(localContext.state.viewType);
        adhocDesigner.updateDataMode(localContext.state.isShowingFullData);

        adhocDesigner.ui.display_manager.render(
            state.columns ?    { column : state.columns, group : state.groups } :
            state.chartItems ? { measures: state.chartItems, group: state.group } :
                               { column : state.crosstabState.columnGroups, row : state.crosstabState.rowGroups});

        jQuery('#designer').trigger('layout_update');
        adhocDesigner.updateAllFieldLabels();
        adhocDesigner.resetScroll();

        //save and undo buttons are disabled in report display view
        if (isDesignView) {
            adhocDesigner.enableRunAndSave(localContext.canSaveReport());
        }
        adhocDesigner.enableSort(state.viewType == 'table');
        adhocDesigner.viewQueryDialog.updateContent(localContext.state.query);
    },
    resetState: function() {
        localContext.state = new adhocDesigner.State({});
    },
    updateState: function(state) {
        localContext.state = new adhocDesigner.State(state);
    },
    updateModeLabelSelection: function(mode){
        jQuery('#dataModeSelector').val(mode);
    },
    updateDataMode: function(isFullData){
        jQuery('#dataSizeSelector').val(isFullData ? 'full' : 'sample');
    },
    setNothingToDisplayVisibility : function(visible) {
        if(visible) {
            jQuery('#titleCaption').css('min-width','400px');
            jQuery('#nothingToDisplay').removeClass(layoutModule.HIDDEN_CLASS);
            centerElement($('nothingToDisplay'), {horz: true, vert: true});
            /*
             * TODO: put layout positioning code into layout related code. Should be handled through media CSS queries.
             */
            if (isIPad()) {
                var elem = $('nothingToDisplay');
                var theWidth = parseInt(elem.getStyle('width'));
                var theBufferedWidth = theWidth + getBufferWidth(elem, true);
                var e = jQuery('#displayManager');
                var parentWidth = e ? e.width() : elem.up(1).getWidth();

                elem.style.marginLeft = (theWidth/2) + 'px';
                elem.style.left = '0%';

                elem.style.position = 'relative';
                elem.style.minWidth = '300px';
            }
        } else {
            jQuery('#titleCaption').css('min-width','');
            jQuery('#nothingToDisplay').addClass(layoutModule.HIDDEN_CLASS);
        }
    },
    updateCanvasClasses : function (isCrosstabMode) {
        jQuery('#' + adhocDesigner.CANVAS_PANEL_ID)[(isCrosstabMode ? 'add' : 'remove') + 'Class']('showingSubHeader OLAP');
    },
    resetScroll: function() {
        if (adhocDesigner._touchController) {
            adhocDesigner._touchController.reset();
            adhocDesigner._touchController.addPadding('canvasTable',{right:200});
        }
    }
};

adhocDesigner.State = function(state) {
    this.update(state);
    mixin(this, localContext.State);
};

adhocDesigner.State.prototype.update = function(newState) {
    _.extend(this, newState);
};
