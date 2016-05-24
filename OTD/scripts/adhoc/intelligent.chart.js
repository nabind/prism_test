/**
 * Holds all core functionality for Ad Hoc charts.
 *
 * @type {Object}
 */
var AdhocIntelligentChart = {};

/**
 * This is short alias for AdhocIntelligentChart object.
 *
 * @type {Object}
 */
var AIC = AdhocIntelligentChart;

_.extend(AIC, {
    debug: false,
    mode: null,
    controller: "ich",
    state: {},
    cache: [],
    initd: false,
    hasAllData: false,
    groupLevel: {
        column: 0,
        row: 0
    },

    reset: function() {
        AIC.isDesignerInitialized = false
    },

    setMode: function(arg) {
        this.mode = arg;
    },

    getMode: function() {
        return this.mode;
    },

    flush: function() {
        this.hasAllData = false;
        this.cache = [];
    },

    setUp: function() {
        HighchartsDataMapper.debug = AIC.debug;
        AdhocDataProcessor.debug = AIC.debug;
    },

    isOLAP: function() {
        return this.getMode() === designerBase.OLAP_ICHART;
    },

    isOlapMode: function() {
        return this.getMode() === designerBase.OLAP_ICHART;
    },

    isNonOlapMode: function() {
        return this.getMode() === designerBase.ICHART;
    },

    update: function(state) {
        try {
            AIC.setUp();

            _.extend(this.state, state);
        } catch (error) {
            if (AIC.debug) {
                console.log("AIC.update:   caught error " + error + " restore state to previous");
            }
            adhocDesigner.undo();

            // TODO: i18n required.
            dialogs.systemConfirm.show("The previous request encountered an error '" + error +
                "' restoring chart to previous state");
        }

        return this;
    },

    render: function() {
        //var test = new HighChartUnitTests();
        //test.run();
        var hasData = AIC.state.queryData.data.length > 0;

        jQuery("#nothingToDisplayMessage").html(adhocDesigner.getMessage("ADH_1214_ICHARTS_NO_DATA"));
        adhocDesigner.setNothingToDisplayVisibility(!hasData);
        if (AIC.chart && AIC.state.isRedrawRequired === true) {
            AIC.chart.destroy();
            AIC.chart = undefined;
        }

        // Update UI data.
        AIC.ui.update(AIC.state);

        // Render UI data.
        AIC.ui.render();

        //try {
            if (AIC.state.queryData.data.length) {
                if (AIC.debug) {
                    console.info(AIC.state);
                }
                var highchartsOptions = AdhocHighchartsAdapter.generateOptions(deepClone(AIC.state.queryData),
                    deepClone(AIC.state.chartState));

                if (AIC._checkHasData(highchartsOptions)) {
                    if (AIC.state.isRedrawRequired === true) {
                        AIC.chart = new Highcharts.Chart(highchartsOptions);
                    }

                    hasData = true;
                } else {
                    //
                    // http://bugzilla.jaspersoft.com/show_bug.cgi?id=29278
                    //
                    // 2012-10-09 thorick
                    //
                    // the current state:  hasData AND !highchartsOptions.hasData
                    //   means that the query has returned some results
                    //   but the chart level rendering does NOT have results
                    //   this can happen if a Data Level selector is set at a level
                    //   which does not contain any results
                    //      (note:  a measure value of zero counts as a result)
                    //
                    hasData = false;
                    jQuery("#nothingToDisplayMessage").html(adhocDesigner.getMessage("ADH_1214_ICHARTS_NO_CHART_DATA"));
                    adhocDesigner.setNothingToDisplayVisibility(!hasData);
                }

                if (AIC.isOLAP()) {
                    AIC.state.olapDimensionInfo = AdhocDataProcessor.fn.getOLAPDimensionInfo();
                }

                isDesignView && AIC._renderDataLevelSelector();
            }
        /*
        } catch (error) {
            if (AIC.debug) {
                console.log("AIC.update:   caught error " + error + " restore state to previous");
            }
            adhocDesigner.undo();

            // TODO: i18n required.
            dialogs.systemConfirm.show("The previous request encountered an error '" + error +
                "' restoring chart to previous state");
        }
        */
        AIC.isDesignerInitialized = true;

        return hasData;
    },

    cleanupOnSwitch: function() {
        AIC.ui.controls.chartTypeSelector.hide();
        AIC.ui.dialogs.groupSelector.hide();
    },

    //
    //  check to see if the highcharts Object has any data
    //  It is possible to have a query that returned data
    //  but a grouping level that contains NO data
    //
    _checkHasData: function (highchartsOptions) {
        if (!highchartsOptions)  return false;
        if (!highchartsOptions.series)  return false;

        if (highchartsOptions.series.length <= 0) return false;
        var hasSeriesData = false;
        for (var i=0; i<highchartsOptions.series.length; i++) {
            if (highchartsOptions.series[i].data != null) {
                if (highchartsOptions.series[i].data.length > 0) {
                    hasSeriesData = true;
                    break;
                }
            }
        }
        return hasSeriesData;
    },

    _isPivotAllowed: function() {
        var me = AIC;

        return me.getDimensions('row').length > 0 ||
            (me.isOLAP() && me.getDimensions('column').length > 0);
    },

    _renderDataLevelSelector: function() {
        AIC.ui.controls.dataLevelSelector.render();
    }
});

/**
 * Service bus.
 */
_.extend(AIC, {
    serviceBus: {
        _listenersMap: {
        },

        _allEventsListeners: [],

        _incorrectEventTypeMsg: "Service bus supports only string events. Received event is not a string.",

        /**
         * Should be used by services to fire event.
         *
         * @param event the event which is fired.
         */
        fireEvent: function(event) {
            var serviceBus = AIC.serviceBus;
            if (typeof event !== "string") {
                throw serviceBus._incorrectEventTypeMsg;
            }

            var listenersMap = serviceBus._listenersMap;
            var eventListeners = listenersMap[event];

            if (eventListeners) {
                _.each(eventListeners, function(listener) {
                    listener(event);
                });
            }

            _.each(serviceBus._allEventsListeners, function(listener) {
                listener(event);
            });
        },

        /**
         * Registers event listener.
         *
         * @param event the event.
         * @param listener the listener.
         */
        registerListener: function(event, listener) {
            var serviceBus = AIC.serviceBus;
            if (typeof event !== "string") {
                throw serviceBus._incorrectEventTypeMsg;
            }

            var listenersMap = serviceBus._listenersMap;
            var eventListeners = listenersMap[event];

            if (!eventListeners) {
                eventListeners = [];

                listenersMap[event] = eventListeners;
            }

            eventListeners.push(listener);
        },

        registerAllEventsListener: function(listener) {
            AIC.serviceBus._allEventsListeners.push(listener);
        }
    }
});

/**
 * Mediator. Knows all about components on the page. Handles custom events and performs the actions.
 */
_.extend(AIC, {
    mediator: {
        _isListenerRegistered: false,

        eventToAction: {
            "chart:typeChanged": function() {
                // Update client state.
                AIC.state.chartState.chartType = AIC.ui.controls.chartTypeSelector.getType();

                // Update server state.
                AIC.updateChartState(false);
            },

            "chart:levelChanged": function() {
                // Update client state.
                AIC.state.chartState.columnsSelectedLevels =
                    AIC.ui.controls.dataLevelSelector.getColumnSelectedLevels();
                AIC.state.chartState.rowsSelectedLevels = AIC.ui.controls.dataLevelSelector.getRowsSelectedLevels();

                // Update server state.
                AIC.updateChartState(true);
            }
        },

        listener: function(event) {
            if (typeof event !== "string") {
                throw "Mediator supports only string events. Received event is not a string.";
            }

            var action = AIC.mediator.eventToAction[event];

            // Execute the action if we have if defined for the event.
            if (action) {
                action();
            }
        },

        /**
         * Registers itself in service bus to listen events.
         */
        register: function() {
            if (!AIC.serviceBus._isListenerRegistered) {
                AIC.serviceBus.registerAllEventsListener(AIC.mediator.listener);
                AIC.serviceBus._isListenerRegistered = true;
            }
        }
    }
});

/*
 * Initialization
 */
_.extend(AIC,{
    ui: {
        controls: {
            dataLevelSelector: {
                _column: null,
                _row: null,

                // TODO: pass the state to the render method instead of external referencing.
                render: function() {
                    var me = AIC.ui.controls.dataLevelSelector;

                    AIC.ui.selectorTables.column.show().find('td.select-header').parent().siblings().remove();
                    AIC.ui.selectorTables.row.show().find('td.select-header').parent().siblings().remove();

                    if (AIC.isOLAP()) {
                        me._renderOlapSlider();
                    } else {
                        me._renderNonOlapSliders();
                    }
                },

                _renderOlapSlider: function() {
                    var me = AIC.ui.controls.dataLevelSelector;
                    me._column = [];
                    me._row = [];

                    jQuery.each(['row', 'column'], function(index, type) {
                        if (AIC.state.olapDimensionInfo[index].length) {
                            jQuery.each(AIC.state.olapDimensionInfo[index], function(i, dimension) {
                                var level = AIC.state.chartState[type + "sSelectedLevels"][i];
                                me['_'+type].push(AIC.ui.create.slider(AIC.ui.selectorTables[type], dimension.levels, dimension.dimension, {
                                    max: dimension.levels.length - 1,
                                    value:  me._levelToLevelPosition(level, dimension),
                                    stop: function(ev, ui) {
                                        AIC.serviceBus.fireEvent("chart:levelChanged");
                                    }
                                }));
                            });
                            AIC.ui.selectorTables[type].show();
                        } else {
                            AIC.ui.selectorTables[type].hide();
                        }
                    });
                },

                _renderNonOlapSliders: function() {
                    var me = AIC.ui.controls.dataLevelSelector;

                    me._row = me._renderNonOlapSlider(0);
                    me._column = me._renderNonOlapSlider(1);
                },

                _renderNonOlapSlider: function(axisIndex) {
                    var me = AIC.ui.controls.dataLevelSelector;
                    var levels = [];

                    jQuery.each(AIC.state.queryData.metadata.axes[axisIndex],function(i,v){
                        if(v.name != 'Measures') {
                            levels.push(v.label);
                        }
                    });

                    var selectorTable = axisIndex == 0 ? AIC.ui.selectorTables.row : AIC.ui.selectorTables.column;
                    var sliderLevelCount = axisIndex == 0 ? AdhocDataProcessor.rowSliderLevelCount :
                        AdhocDataProcessor.colSliderLevelCount;
                    var selectedLevels = axisIndex == 0 ? AIC.state.chartState.rowsSelectedLevels :
                        AIC.state.chartState.columnsSelectedLevels;

                    selectorTable.find('td.select-header').parent().siblings().remove();

                    sliderLevelCount == 1 ? selectorTable.hide() : selectorTable.show();

                    return AIC.ui.create.slider(selectorTable, levels, '', {
                        max: sliderLevelCount - 1,
                        value: me._getSliderPositionForNonOlap(selectedLevels, axisIndex),
                        stop: function(ev, ui) {
                            AIC.serviceBus.fireEvent("chart:levelChanged");
                        }
                    });
                },

                hide: function() {
                    AIC.ui.selectorTables.column.hide();
                    AIC.ui.selectorTables.row.hide();
                },

                _levelToLevelPosition: function(level, dimension) {
                    for (var i = 0; i < dimension.levels.length; i++) {
                        if (dimension.levels[i].levelName === level.name) {
                            return i;
                        }
                    }

                    throw "No OLAP level info found for specified level = " + level;
                },

                getRowsSelectedLevels: function() {
                    var me = AIC.ui.controls.dataLevelSelector;
                    return me._getSelectedLevels(me._row, me._row, 0);
                },

                getColumnSelectedLevels: function() {
                    var me = AIC.ui.controls.dataLevelSelector;
                    return me._getSelectedLevels(me._column, me._column, 1);
                },

                _getSelectedLevels: function(nonOlapSlider, olapSliders, axisIndex) {
                    var me = AIC.ui.controls.dataLevelSelector;

                    if (AIC.isOLAP()) {
                        return me._extractOlapSelectedLevels(olapSliders, axisIndex);
                    } else {
                        return me._extractNonOlapSelectedLevels(nonOlapSlider, axisIndex);
                    }
                },

                _extractOlapSelectedLevels: function(sliders, axisIndex) {
                    var me = AIC.ui.controls.dataLevelSelector;
                    var selectedLevels = [];

                    jQuery(sliders).each(function(i) {
                        var selectedLevelPosition = me._extractSliderPosition(
                            jQuery(this));
                        var dimensionInfo = AIC.state.olapDimensionInfo[axisIndex][i];
                        var selectedLevel = dimensionInfo.levels[selectedLevelPosition];

                        selectedLevels.push({
                            name: selectedLevel.levelName,
                            label: selectedLevel.label,
                            dimension: dimensionInfo.dimension
                        });
                    });

                    return selectedLevels;
                },

                _extractNonOlapSelectedLevels: function(dimSlider, axisIndex) {
                    var me = AIC.ui.controls.dataLevelSelector;

                    var sliderPos = me._extractSliderPosition(dimSlider);

                    if (sliderPos === 0) {
                        return [];
                    } else {
                        var levels =
                            AdhocDataProcessor.fn.getLevelsWithoutMeasures(AIC.state.queryData.metadata.axes[axisIndex]);

                        return [levels[sliderPos - 1]];
                    }
                },

                _getSliderPositionForNonOlap: function(selectedLevels, axisIndex) {
                    if (selectedLevels == 0) {
                        return 0;
                    }

                    var levels =
                        AdhocDataProcessor.fn.getLevelsWithoutMeasures(AIC.state.queryData.metadata.axes[axisIndex]);

                    // The slider position will be level index + 1 because of grand total on the position 0.
                    return AdhocDataProcessor.fn.getLevelIndex(levels, selectedLevels[0]) + 1;
                },

                _extractSliderPosition: function(dimSlider) {
                    return dimSlider.slider('option', 'value');
                },

                dock: function() {
                    AIC.ui.dialogs.groupSelector.find('div.pod-body').eq(0).appendTo(jQuery('#level-container'));
                    AIC.ui.dialogs.groupSelector.hide();
                },

                undock: function() {
                    jQuery('#level-container').children('div.pod-body').eq(0).appendTo(AIC.ui.dialogs.groupSelector.find('div.body').eq(0));
                    AIC.ui.dialogs.groupSelector.show().position({
                        of: '#frame',
                        my: 'right top',
                        at: 'right top',
                        offset: '-2 4'
                    });
                }
            },

            chartTypeSelector: {
                _type: null,
                _container: null,

                init: function() {
                    var me = AIC.ui.controls.chartTypeSelector;

                    me._container = jQuery('#chartTypeSelector');
                    me._container.on('click', 'div.cell', me._typeSelectedHandler);
                },

                update: function(type) {
                    var me = AIC.ui.controls.chartTypeSelector;

                    me._type = type;
                },

                render: function() {
                    var me = AIC.ui.controls.chartTypeSelector;

                    me._container.find('div.cell').removeClass("selected");
                    me._container.find('div.cell[name="' + me._type + '"]').eq(0).addClass("selected");
                },

                hide: function() {
                    var me = AIC.ui.controls.chartTypeSelector;

                    me._container.hide();
                },

                _typeSelectedHandler: function() {
                    var me = AIC.ui.controls.chartTypeSelector;

                    me._type = this.getAttribute('name');
                    me.render();

                    AIC.serviceBus.fireEvent("chart:typeChanged");
                },

                getType: function() {
                    var me = AIC.ui.controls.chartTypeSelector;

                    return me._type;
                }
            }
        },

        update: function(state) {
            AIC.ui.controls.chartTypeSelector.update(state.chartState.chartType);
        },

        render: function() {
            var titleCaption = jQuery('#titleCaption');
            AIC.state.titleBarShowing ? titleCaption.show() : titleCaption.hide();
            titleCaption.html(AIC.state.title);

            adhocDesigner.enableXtabPivot(AIC._isPivotAllowed());

            AIC.ui.controls.chartTypeSelector.render();

            // Hide them by default. If required they will be rendered later in the flow.
            AIC.ui.controls.dataLevelSelector.hide();
        },

        create: {
            sliderTable: function(isRowsAxis){
                var body = jQuery('#level-container').show().children('.pod-body').eq(0);
                var levelSelectorHtml = Mustache.to_html(jQuery('#levelSelectorTemplate').html(), {
                    id: (isRowsAxis ? "row" : "column") + "LevelSelector",
                    colspan: (AIC.isOLAP() ? 2 : 1),
                    name: isRowsAxis ? layoutManagerLabels.row[AIC.mode] : layoutManagerLabels.column[AIC.mode]
                });
                var table = jQuery(levelSelectorHtml).appendTo(body);

                table.on('mouseover','div.tickOverlay',function(evt){
                    var tick = jQuery(this).parent();
                    AIC.dataLevelTooltip.html(tick.attr('level-name'));
                    AIC.dataLevelTooltip.show().position({
                        of: tick,
                        at: 'center top',
                        my: 'center bottom',
                        offset: '0 -6',
                        collision: 'fit'
                    });
                    AIC.ui.selectorTables.column.css('display') == 'block' && AIC.ui.selectorTables.column.hide().show();
                    AIC.ui.selectorTables.row.css('display') == 'block' && AIC.ui.selectorTables.row.hide().show();
                });

                table.on('mouseover','a.ui-slider-handle',function(evt){
                    var slider = jQuery(this).parent();
                    var tick = slider.children('div.sliderTick').eq(slider.slider('option','value'));
                    AIC.dataLevelTooltip.html(tick.attr('level-name'));

                    AIC.dataLevelTooltip.show().position({
                        of: tick,
                        at: 'center top',
                        my: 'center bottom',
                        offset: '0 -6',
                        collision: 'fit'
                    });
                    AIC.ui.selectorTables.column.css('display') == 'block' && AIC.ui.selectorTables.column.hide().show();
                    AIC.ui.selectorTables.row.css('display') == 'block' && AIC.ui.selectorTables.row.hide().show();
                });

                table.on('mouseout','a.ui-slider-handle, div.tickOverlay',function(evt){
                    AIC.dataLevelTooltip.hide();
                    AIC.ui.selectorTables.column.css('display') == 'block' && AIC.ui.selectorTables.column.hide().show();
                    AIC.ui.selectorTables.row.css('display') == 'block' && AIC.ui.selectorTables.row.hide().show();
                });

                return table;
            },
            slider: function(table, levels, name, options) {
                /*
                 * Create <tr>. Add label in OLAP mode
                 */
                var parms = {
                    label: AIC.isOLAP() ? {name: name} : false,
                    marginLeft: AIC.isOLAP() ? '24px' : 0
                };
                table.find('tbody').eq(0).append(Mustache.to_html(jQuery('#dataLevelSelectorTemplate').html(), parms));
                /*
                 * Init jqueryui slider
                 */
                var slider = table.find('div.jrs-slider').last();
                slider.slider({
                    value: options.value,
                    min: 0,
                    max: options.max,
                    step: 1,
                    range: 'min',
                    stop: options.stop,
                    slide: function(ev,ui) {
                        var tick = jQuery(ui.handle).parent().children('div.sliderTick').eq(ui.value);
                        AIC.dataLevelTooltip.html(tick.attr('level-name'));
                        AIC.dataLevelTooltip.show().position({
                            of: tick,
                            at: 'center top',
                            my: 'center bottom',
                            offset: '0 -6',
                            collision: 'fit'
                        });
                        AIC.ui.selectorTables.column.css('display') == 'block' && AIC.ui.selectorTables.column.hide().show();
                        AIC.ui.selectorTables.row.css('display') == 'block' && AIC.ui.selectorTables.row.hide().show();
                    }
                });
                /*
                 * Add tick marks
                 */
                var spacing = options.max > 0 ? 100 / (options.max) : 0;
                if(!AIC.isOLAP()) {
                    levels.unshift('All');
                    options.max++;
                }

                for(var i=0;i < (options.max + 1);i++) {
                    if(levels[i]){
                        var parms = {
                            label: levels[i].label || levels[i],
                            width: i*spacing
                        };
                        parms.label == 'All' && (parms.label = 'Total');
                        var htm = Mustache.to_html(jQuery('#sliderTickTemplate').html(), parms);
                        slider.append(htm);
                    }
                }
                return slider;
            }
        }
    },
    init: function(mode) {
        AIC.mediator.register();
        AIC.dataLevelTooltip = jQuery('#dataLevelTooltip');

        jQuery('#chartOptions').appendTo(adhocDesigner.ui.canvas).show();
        jQuery('#chartMenu').appendTo(adhocDesigner.ui.canvas);
        jQuery(Mustache.to_html(jQuery('#titleCaptionTemplate').html()),{}).appendTo(adhocDesigner.ui.canvas).show();
        jQuery('#chartContainer').appendTo(adhocDesigner.ui.canvas).show();

        if(!AIC.initd) {
            jQuery('#chartOptions').on('mouseenter',function() {
                jQuery(this).addClass('over');
                jQuery('#chartMenu').show().position({
                    of: jQuery('#chartOptions'),
                    at: 'left bottom',
                    my: 'left top',
                    offset: '0 -1'
                })
            });
            jQuery('#chartMenu').on('mouseleave',function() {
                jQuery('#chartOptions').removeClass('over');
                jQuery('#chartMenu').hide();
            });
            jQuery('#chartMenu').on('mouseenter','p.wrap',function(){
                jQuery(this).addClass('over');
            });
            jQuery('#chartMenu').on('mouseleave','p.wrap',function(){
                jQuery(this).removeClass('over');
            });
            jQuery('#chartMenu').on('click','li',function(){
                AIC.ui.dialogs.typeSelector.show().position({
                    of: 'body',
                    at: 'center center',
                    my: 'center center'
                });

                AIC.ui.controls.chartTypeSelector.render();
            });
            jQuery('#chartTypeSelector .closeIcon').on('click',function(){
                AIC.ui.dialogs.typeSelector.hide();
            });
            jQuery('#dataLevelSelector .closeIcon').on('click',function(){
                AIC.ui.dialogs.groupSelector.hide();
            });

            AIC.ui.dialogs = {
                typeSelector: jQuery('#chartTypeSelector').draggable(),
                groupSelector: jQuery('#dataLevelSelector').draggable()
            };

            AIC.ui.selectorTables = {
                column : AIC.ui.create.sliderTable(false),
                row: AIC.ui.create.sliderTable(true)
            };

            AIC.ui.controls.chartTypeSelector.init();
        }
        AIC.ui.dialogs.typeSelector.hide();
        AIC.ui.dialogs.groupSelector.hide();
        jQuery('#level-container').show();

        AIC.initd = true;
    },
    hide: function(){
        jQuery('#chartOptions').appendTo('#highChartsRepo').hide();
        jQuery('#chartMenu').appendTo('#highChartsRepo').hide();
        jQuery('#titleCaption').remove();
        jQuery('#chartContainer').appendTo('#highChartsRepo').hide();
    }
});
/*
 * Event Handling
 */
_.extend(AIC,{
    mouseUpHandler: function(evt) {

    },
    mouseDownHandler: function(evt) {

    },
    mouseOverHandler: function(evt) {

    },
    mouseOutHandler: function(evt) {

    },
    mouseClickHandler: function(evt) {

    },
    crosstabTreeMenuHandler: function(event) {
        var matched = null;
        var node = event.memo.node;
        var evt = event.memo.targetEvent;

        matched = matchAny(node, adhocDesigner.TREE_CONTEXT_MENU_PATTERN, true);
        if (matched) {
            node = adhocDesigner.dimensionsTree.getTreeNodeByEvent(event.memo.targetEvent);
            var tree = adhocDesigner.dimensionsTree;
            var contextName = adhocDesigner.DIMENSION_TREE_LEVEL_CONTEXT;
            if (!node) {
                node = adhocDesigner.measuresTree.getTreeNodeByEvent(event.memo.targetEvent);
                tree = adhocDesigner.measuresTree;
                contextName = node.isParent() ? adhocDesigner.MEASURE_TREE_GROUP_CONTEXT : adhocDesigner.MEASURE_TREE_CONTEXT;
            } else {
                node.isParent() && (contextName = adhocDesigner.DIMENSION_TREE_DIMENSION_CONTEXT);
            }

            if (!adhocDesigner.isSelectedNode(node)) {
                adhocDesigner.selectFromAvailableFields(evt, node);
            }
            adhocDesigner.showDynamicMenu(evt, contextName, null);
        }
    },
    lmHandlersMap: {
        // Common methods for both axes
        addItems : function(nodes, pos, axis) {
            var dims = nodes[0].extra.isMeasure ? nodes[0].extra.dimensionId :
                _.map(nodes, function(n) {return n.extra.dimensionId;}).join(',');
            AIC.insertDimensionInAxisWithChild(dims, axis, pos, _.pluck(nodes, 'id').join(','));
        },
        measureReorder : function(measure, to) {
            AIC.moveMeasure(measure, to);
        },
        column : {
            addItem : function(dim, pos, level, levelPos, isMeasure, uri) {
                AIC.insertDimensionInAxisWithChild(dim, 'column', pos, level, levelPos, isMeasure, uri);
            },
            removeItem : function(item, index) {
                if (item.isMeasure) {
                    AIC.removeMeasure(index);
                } else {
                    AIC.hideColumnLevel(item.dimension, item.level);
                }
            },
            moveItem : function(dim, from, to) {
                AIC.moveDimension(dim, 'column', to);
            },
            switchItem : function(dim, from, to) {
                AIC.moveDimension(dim, 'column', to);
            },
            contextMenu : function(event, options) {
                AIC.selectFromDisplayManager(options.targetEvent, options.extra, designerBase.COLUMN_GROUP_MENU_LEVEL);
                AIC.showMenu(options.targetEvent, 'displayManagerColumn');
            }
        },
        row : {
            addItem : function(dim, pos, level, levelPos, isMeasure, uri) {
                AIC.insertDimensionInAxisWithChild(dim, 'row', pos, level, levelPos, isMeasure, uri);
            },
            removeItem : function(item, index) {
                if (item.isMeasure) {
                    AIC.removeMeasure(index);
                } else {
                    AIC.hideRowLevel(item.dimension, item.level);
                }
            },
            moveItem : function(dim, from, to) {
                AIC.moveDimension(dim, 'row', to);
            },
            switchItem : function(dim, from, to) {
                AIC.moveDimension(dim, 'row', to);
            },
            contextMenu : function(event, options) {
                AIC.selectFromDisplayManager(options.targetEvent, options.extra, designerBase.ROW_GROUP_MENU_LEVEL);
                AIC.showMenu(options.targetEvent, 'displayManagerRow');
            }
        }
    }
});
/*
 * Ajax Controller
 */
_.extend(AIC,{
    insertDimensionInAxisWithChild: function(dimName, axis, position, level, measurePosition, isMeasure, uri) {
        var params = ['dim=' + encodeText(dimName), 'axis=' + axis, 'pos=' + position];
        if (isNotNullORUndefined(measurePosition)) {
            params.push('measure_pos=' + measurePosition);
        }
        if (isNotNullORUndefined(level)) {
            params.push('child=' + encodeText(level));
        }
        if (isNotNullORUndefined(uri)) {
            params.push('uri=' + encodeText(uri));
        }
        if (isNotNullORUndefined(isMeasure)) {
            params.push('isMeasure=' + encodeText(isMeasure));
        }
        AIC.flush();
        designerBase.sendRequest('ich_insertDimensionInAxisWithChild', params,  adhocDesigner.render, {bPost: true});
    },
    removeMeasure: function(index) {
        designerBase.sendRequest('ich_removeMeasure', new Array('i='+index), adhocDesigner.render, null);
    },
    moveDimension: function(dimName, axis, position) {
        designerBase.sendRequest('ich_moveDimension', new Array('dim=' + encodeText(dimName), 'axis=' + axis, 'pos=' + position), adhocDesigner.render, null);
    },
    hideRowLevel: function(dimName, levelName) {
        designerBase.sendRequest('ich_hideRowLevel', new Array('f=' + encodeText(dimName), 'level=' + encodeText(levelName)), adhocDesigner.render, null);
    },
    hideColumnLevel: function(dimName, levelName) {
        var callback = function(state){
            adhocDesigner.render(state);

            var level = AIC.getLevelObject(levelName, dimName, "column");
            if(level && level.propertyMap && level.propertyMap.lastFilteredLevel == "true") {
                dialogs.systemConfirm.show(adhocDesigner.getMessage("ADH_CROSSTAB_LAST_FILTERED_LEVEL"), 5000);
            }
        };

        designerBase.sendRequest('ich_hideColumnLevel', new Array('f=' + encodeText(dimName), 'level=' + encodeText(levelName)), callback, null);
    },
    expandAllLevels:function(){
        if (AIC.debug) {
            console.log("AIC.expandAllLevels:  sending expandAll query, this may take some time");
        }
        AIC.cache = [];
        AIC.hasAllData = true;
        designerBase.sendRequest('ich_expandAllLevels', null, adhocDesigner.render, null);
    },
    switchToColumnGroup: function(ind) {
        var object = adhocDesigner.getSelectedColumnOrGroup();
        // TODO: adapt/normalize data from tree and from custom generated event
        var index = _.isNumber(ind) ? ind : object.groupIndex;
        designerBase.sendRequest('ich_switchToColumnGroup', new Array('i=' + index), adhocDesigner.render, null);
    },
    switchToRowGroup: function(ind) {
        var object = adhocDesigner.getSelectedColumnOrGroup();
        var index = _.isNumber(ind) ? ind : object.groupIndex;
        designerBase.sendRequest('ich_switchToRowGroup', new Array('i=' + index), adhocDesigner.render, null);
    },
    moveRowGroup: function(from, to, customCallback) {
        var callback = function(state){
            adhocDesigner.render(state);
            customCallback && customCallback();
        };
        designerBase.sendRequest('ich_moveRowGroup', new Array('f='+from,'t='+to), callback, null);
    },
    moveColumnGroup: function(from, to, customCallback) {
        var callback = function(state){
            adhocDesigner.render(state);
            customCallback && customCallback();
        };
        designerBase.sendRequest('ich_moveColumnGroup', new Array('f='+from,'t='+to), callback, null);
    },
    moveMeasure: function(measure, to) {
        designerBase.sendRequest('ich_moveMeasureByName', ['measure='+measure,'to='+to], adhocDesigner.render, null);
    },
    updateChartState: function(doDataRequest) {
        var callback = function(response) {
            if (response) {
                adhocDesigner.render(response);
            } else {
                throw "No response received on save chart state request";
            }
        };

        designerBase.sendRequest('ich_updateChartState', [
            'chartState=' + encodeURI(Object.toJSON(AIC.state.chartState))], callback, null);
    },
    setCategoryForRowGroup: function(catName, index) {
        var callback = function(state) {
            adhocDesigner.render(state);
        };
        designerBase.sendRequest('ich_setRowGroupCategorizer', new Array('cat=' + encodeText(catName), 'i=' + index),
            callback, null);
    },
    setCategoryForColumnGroup: function(catName, index) {
        var callback = function(state) {
            adhocDesigner.render(state);
        };
        designerBase.sendRequest('ich_setColumnGroupCategorizer', new Array('cat=' + encodeText(catName), 'i=' + index),
            callback, null);
    },
    setSummaryFunction: function(thisFunction, index) {
        designerBase.sendRequest('ich_setSummaryFunction', new Array('f=' + thisFunction, 'i=' + index),
            AIC.standardIChartOpCallback, null);
    },
    setSummaryFunctionAndMask: function(thisFunction, thisMask, index) {
        designerBase.sendRequest('cr_setSummaryFunctionAndDataMask',
            new Array('f=' + thisFunction, 'm=' + encodeText(thisMask), 'i=' + index),
            AIC.standardIChartOpCallback, null);
    },
    setHideEmptyRows: function() {
        designerBase.sendRequest('ich_setProperty', new Array("name=hideEmptyRows", "value=true"),
            AIC.standardIChartOpCallback, null);
    },
    setUnhideEmptyRows: function() {
        designerBase.sendRequest('ich_setProperty', new Array("name=hideEmptyRows", "value=false"),
            AIC.standardIChartOpCallback, null);
    }
});

/*
 * Actionmodel Tests
 */
_.extend(AIC,{
    canSaveReport: function() {
        return AIC.state.canSave;
    },
    hideEmptyRowsEquals: function(val) {
        return AIC.state.crosstabState.hideEmptyRows === val;
    },
    canAddFilter: function(object, errorMessages) {
        var isMeasure = localContext.isOlapMode() && (isNotNullORUndefined(object.isMeasure)
            ? object.isMeasure
            : (object.param.extra && object.param.extra.isMeasure));
        var isAllLevel = isNotNullORUndefined(object.param)
            ? (object.param.extra && object.param.extra.id == localContext.ALL_LEVEL_NAME)
            : object.level == localContext.ALL_LEVEL_NAME;

        var isDuplicate = localContext.isOlapMode() && localContext._isAddingFilterDuplicate(object);

        //We do not support filters for measures in OLAP-mode.
        if (isMeasure) {
            errorMessages && errorMessages.push(addFilterErrorMessageMeasureAdd);
            return false;
        }
        //We do not support filters for (All) level in 1'st iteration.
        if (isAllLevel) {
            errorMessages && errorMessages.push(addFilterErrorMessageAllLevelAdd);
            return false;
        }
        // Cannot add group of fields as filter.
        if (object.isParent && object.isParent()) {
            errorMessages && errorMessages.push(addFilterErrorMessageGroupAdd);
            return false;
        }
        if (adhocDesigner.isSpacerSelected(object)) {
            errorMessages && errorMessages.push(addFilterErrorMessageSpacerAdd);
            return false;
        }
        if (adhocDesigner.isPercentOfParentCalcSelected(object)) {
            errorMessages && errorMessages.push(addFilterErrorMessagePercentOfParentCalcFieldAdd);
            return false;
        }
        if (adhocDesigner.isConstantSelected(object)) {
            errorMessages && errorMessages.push(addFilterErrorMessageConstantAdd);
            return false;
        }
        if (isDuplicate) {
            errorMessages && errorMessages.push(addFilterErrorMessage);
            return false;
        }

        return true;
    },
    canAddSliceFilter: function() {
        var doesSelectionContainNotSliceableObject = selObjects.find(function(obj) {
            return !obj.isSliceable;
        });

        return selObjects.first() && !doesSelectionContainNotSliceableObject;
    },
    _isAddingFilterDuplicate: function (filterCandidate) {
        var isDuplicate = false;
        var filterCandidateName;

        if (filterCandidate.param) {
            filterCandidateName = "[" + filterCandidate.param.extra.dimensionId + "].[" + filterCandidate.param.extra.id + "]";
        } else {
            filterCandidateName = "[" + filterCandidate.dimension + "].[" + filterCandidate.level + "]";
        }
        var filterPods = adHocFilterModule._getFilterPods();
        if (filterPods) {
            filterPods.each(function(object){
                var pod = object.down(FILTER_PANE_PATTERN);
                if (pod && (pod.readAttribute("data-fieldName") == filterCandidateName)) {
                    isDuplicate = true;
                    throw $break;
                }
            });
        }
        return isDuplicate;
    },
    isDateType: function(){
        var object = adhocDesigner.getSelectedColumnOrGroup();
        if(object){
            var isGroup = AIC.isGroupSelected(object);
            var group = AdHocCrosstab.getSelectedGroup(object);
            if (group) {
                var canReBucket = group.canReBucket === true;
                var dateDataType = group.type === "date";
                return (isGroup && canReBucket && dateDataType);
            }
        }
        return false;
    },
    isGroupSelected: function(selectedObject) {
        return !selectedObject.isMeasure;
    },
    isCurrentDateType: function(thisType) {
        var group = AdHocCrosstab.getSelectedGroup(adhocDesigner.getSelectedColumnOrGroup());
        if(group){
            return group.categorizerName == thisType;
        }
        return false;
    },
    canMoveUpOrLeft: function() {
        var object = adhocDesigner.getSelectedColumnOrGroup();
        var index = AIC.getSelectedDimensionIndex(object);
        return (index > 0);
    },
    canMoveDownOrRight: function() {
        var object = adhocDesigner.getSelectedColumnOrGroup();
        var index = AIC.getSelectedDimensionIndex(object);
        return index < AIC.getDimensions(object.axis).length - 1;
    },
    canAddSiblingLevels: function() {
        //TODO: if all sibling levels already added to crosstab or
        //or no siblings present - return false
        return true;
    },
    canSwitchToRow: function() {
        var object = adhocDesigner.getSelectedColumnOrGroup();
        return (!AIC.isOLAP() || AIC.getDimensions(object.axis).length > 1);
    },
    canAddDimensionAsRowGroup: function() {
        var node = selObjects.first();
        if (!node.hasChilds()) {
            node = node.parent;
        }

        if (AIC.isOLAP()) {
            if (AIC.getDimensions('column').length === 0) {
                return false;
            }
            var dimension = node.param.extra.id,
                levelsAtColumns = AIC.getLevelsFromDimension(dimension, 'column'),
                levelsAtRows = AIC.getLevelsFromDimension(dimension, 'row');

            return levelsAtColumns.length === 0 && (levelsAtRows.length < node.getChildCount());
        } else {
            var tree = dynamicTree.trees[node.getTreeId()];
            var leaves = adhocDesigner.getAllLeaves(node, tree);
            var leavesStringArray = adhocDesigner.getAllLeaves(node, tree).collect(function(node) {
                return node.param.extra.id;
            });

            if (leaves[0].param.extra.isMeasure) {
                var measuresInColumns = AIC.getFilteredMeasureList('column');
                return measuresInColumns.length === 0;
            } else {
                var allUsedFields = _.pluck(AIC.getFilteredList(), 'name');
                return _.difference(leavesStringArray, allUsedFields).length > 0;
            }
        }
    },
    canAddDimensionAsColumnGroup: function() {
        var node = selObjects.first();
        if (!node.hasChilds()){
            node = node.parent;
        }

        if(AIC.isOLAP()) {
            var dimension = node.param.extra.id;
            var levelsAtColumns = AIC.getLevelsFromDimension(dimension, 'column');
            var levelsAtRows = AIC.getLevelsFromDimension(dimension, 'row');

            return levelsAtRows.length === 0 && (levelsAtColumns.length < node.getChildCount());
        } else {
            var tree = dynamicTree.trees[node.getTreeId()];
            var leaves = adhocDesigner.getAllLeaves(node, tree);
            var leavesStringArray = adhocDesigner.getAllLeaves(node, tree).collect(function(node) {
                return node.param.extra.id;
            });

            if (leaves[0].param.extra.isMeasure) {
                var measuresInRows = AIC.getFilteredMeasureList('row');
                return measuresInRows.length === 0;

            } else {
                var allUsedFields = _.pluck(AIC.getFilteredList(), 'name');
                return _.difference(leavesStringArray, allUsedFields).length > 0;
            }
        }
    },
    canAddLevelAsColumnGroup: function() {
        var node = selObjects.first();
        var dimensionIds =  AIC.isOLAP() ? [node.param.extra.dimensionId] :
            adhocDesigner.getAllLeaves(node).map(function(n) {return n.param.extra.dimensionId});

        var levelsAtColumns = dimensionIds.inject([], function(levels, d) {
            return levels.concat(AIC.getLevelsFromDimension(d, 'column') || []);
        });
        var levelsAtRows = dimensionIds.inject([], function(levels, d) {
            return levels.concat(AIC.getLevelsFromDimension(d, 'row') || []);
        });
        return levelsAtRows.length === 0 &&
            (levelsAtColumns.length === 0 ||
                (!AIC.isOLAP() && node.param.extra.isMeasure) ||
                !levelsAtColumns.find(function(name) {return node.param.extra.id === name}));
    },
    canAddLevelAsRowGroup: function() {
        if (AIC.getDimensions('column').length === 0 && AIC.isOLAP()) {
            return false;
        }
        var node = selObjects.first();
        var dimensionIds = AIC.isOLAP() ? [node.param.extra.dimensionId] :
            adhocDesigner.getAllLeaves(node).map(function(n) {return n.param.extra.dimensionId});

        var levelsAtColumns = dimensionIds.inject([], function(levels, d) {
            return levels.concat(AIC.getLevelsFromDimension(d, 'column') || []);
        });
        var levelsAtRows = dimensionIds.inject([], function(levels, d) {
            return levels.concat(AIC.getLevelsFromDimension(d, 'row') || []);
        });

        return levelsAtColumns.length === 0 &&
            (levelsAtRows.length === 0 ||
                (!AIC.isOLAP() && node.param.extra.isMeasure) ||
                !levelsAtRows.find(function(name) {return node.param.extra.id === name}));
    },
    isRowGroupSelected: function(selectedObject) {
        return selectedObject.axis === "row" && !selectedObject.isMeasure;
    },
    isColumnGroupSelected: function(selectedObject) {
        return selectedObject.axis === "column" && !selectedObject.isMeasure;
    }
});
/*
 * Actionmodel Actions
 */
_.extend(AIC,{
    removeLevelFromColumn: function() {
        var meta = selObjects.first();
        jQuery('#' + adhocDesigner.DISPLAY_MANAGER_ID).trigger('lm:removeItem',{
            axis : 'column',
            index : meta.index,
            item : {
                level : meta.level,
                dimension : meta.dimension,
                isMeasure : meta.isMeasure
            }
        });
    },
    removeLevelFromRow: function() {
        var meta = selObjects.first();
        jQuery('#' + adhocDesigner.DISPLAY_MANAGER_ID).trigger('lm:removeItem',
            {axis : 'row', index : meta.index, item : {level : meta.level, dimension : meta.dimension, isMeasure : meta.isMeasure}});
    },
    moveRowGroupLeft: function(customCallback) {
        var object = adhocDesigner.getSelectedColumnOrGroup();
        var fromGroup = AIC.getSelectedDimensionIndex(object);
        var toGroup = fromGroup-1;
        AIC.moveRowGroup(fromGroup, toGroup, customCallback);
    },
    moveRowGroupRight: function(customCallback) {
        var object = adhocDesigner.getSelectedColumnOrGroup();
        var fromGroup = AIC.getSelectedDimensionIndex(object);
        var toGroup=fromGroup + 1;
        AIC.moveRowGroup(fromGroup, toGroup, customCallback);
    },
    moveColumnGroupLeft: function(customCallback) {
        var object = adhocDesigner.getSelectedColumnOrGroup();
        var fromGroup = AIC.getSelectedDimensionIndex(object);
        var toGroup = fromGroup-1;
        AIC.moveColumnGroup(fromGroup, toGroup, customCallback);
    },
    moveColumnGroupRight: function(customCallback) {
        var object = adhocDesigner.getSelectedColumnOrGroup();
        var fromGroup = AIC.getSelectedDimensionIndex(object);
        var toGroup=fromGroup + 1;
        AIC.moveColumnGroup(fromGroup, toGroup, customCallback);
    },
    appendDimensionToRowAxisWithLevel: function(level){
        var meta = level ?
            AIC.getAvailableFieldsNodeBySelection(level.id, level.groupId) :
            AIC.getAvailableFieldsNodeBySelection();
        jQuery('#' + adhocDesigner.DISPLAY_MANAGER_ID).trigger('lm:addItem',
            {axis : 'row', dimension : meta.dimension, level : meta.level, index : meta.index, uri : meta.uri, isMeasure : meta.isMeasure});
    },
    appendDimensionToColumnAxisWithLevel: function(level){
        var meta = level ?
            AIC.getAvailableFieldsNodeBySelection(level.id, level.groupId) :
            AIC.getAvailableFieldsNodeBySelection();
        jQuery('#' + adhocDesigner.DISPLAY_MANAGER_ID).trigger('lm:addItem',
            {axis : 'column', dimension : meta.dimension, level : meta.level, index : meta.index, uri : meta.uri, isMeasure : meta.isMeasure});
    },
    appendMeasureToRow: function(name) {
        var meta = name ?
            AIC.getAvailableFieldsNodesBySelection(name) :
            AIC.getAvailableFieldsNodesBySelection();
        jQuery('#' + adhocDesigner.DISPLAY_MANAGER_ID).trigger('lm:addItems',
            {axis : 'row', levels : meta, index : meta[0].index});
    },
    appendMeasureToColumn: function(name) {
        var meta = name ?
            AIC.getAvailableFieldsNodesBySelection(name) :
            AIC.getAvailableFieldsNodesBySelection();
        jQuery('#' + adhocDesigner.DISPLAY_MANAGER_ID).trigger('lm:addItems',
            {axis : 'column', levels : meta, index : meta[0].index});
    },
    showMenu: function(evt,type){
        var context;
        switch(type){
            case 'displayManagerRow':
                context = 'displayManagerRow';
                break;
            case 'displayManagerColumn':
                context = 'displayManagerColumn';
                break;
            case 'groupMember':
                context = designerBase.MEMBER_GROUP_MENU_LEVEL;
                break;
            case 'measuresDimensionRow':
                context = 'measuresDimensionInRows';
                break;
            case 'measuresDimensionColumn':
                context = 'measuresDimensionInColumns';
                break;
            case 'rowGroup':
                context = designerBase.ROW_GROUP_MENU_LEVEL;
                break;
            case 'columnGroup':
                context = designerBase.COLUMN_GROUP_MENU_LEVEL;
                break;
            case 'measureRow':
                context = 'measureRow';
                break;
            case 'measureColumn':
                context = 'measureColumn';
        }
        adhocDesigner.showDynamicMenu(evt, context, null, type == 'groupMember' ? null : AIC.updateContextMenuWithSiblingLevels);
    },
    setCatForColumnGroup: function(catName) {
        var object = adhocDesigner.getSelectedColumnOrGroup();
        if (object && AIC.isColumnGroupSelected(object)) {
            AIC.setCategoryForColumnGroup(catName, object.groupIndex);
        }
    },
    setCatForRowGroup: function(catName) {
        var object = adhocDesigner.getSelectedColumnOrGroup();
        if (object && AIC.isRowGroupSelected(object)) {
            AIC.setCategoryForRowGroup(catName, object.groupIndex);
        }
    },
    selectFunction: function(newFunction) {
        var object = AdHocCrosstab.getSelectedMeasure();
        if (object) {
            var index = selObjects.first().index;
            var type = adhocDesigner.getSuperType(object.type);
            var newType = AdHocCrosstab.getMeasureTypeByFunction(newFunction);
            if (type !== newType) {
                AIC.setSummaryFunctionAndMask(newFunction, defaultMasks[newType], index);
            } else {
                AIC.setSummaryFunction(newFunction, index);
            }
        }
    }
});
/*
 * Helpers
 */
_.extend(AIC,{
    getDimensions: function(axis) {
        var xtabState = AIC.state.crosstabState;
        return axis ? xtabState[axis + 'Groups'] : _.chain(xtabState).filter(function(val, key) {
            return key.search(/Groups$/) >= 0;
        }).flatten().value();
    },
    getLevelsFromDimension : function(dim, axis) {
        var axisModel = AIC.getDimensions(axis);
        dim = _.find(axisModel, function(d) {
            return d.name === dim;
        });

        return dim ?
            _.chain(dim.levels).filter(function(level) { // return only visible levels
                return level.visible;
            }).map(function(level) {                    // retrieve all members from levels
                    return !_.isEmpty(level.members) ? level.members : level
                }).flatten().filter(function(member) {      // filter out spacers
                    return !member.isSpacer;
                }).pluck('name').value()     :
            [];
    },
    getLevelObject: function(levelName, dimName, axisName) {
        var axisModel = this.getDimensions(axisName);
        function findByName(array, name) {
            return _.find(array, function(item) {
                return item.name == name;
            })
        };
        var dim = findByName(axisModel, dimName);
        if(!dim) {
            return null;
        }
        var level = findByName(dim.levels, levelName);
        return level;
    },
    getFilteredList : function(a, p) {
        var axis = a || null, props = p || {};
        if (arguments.length === 1 && _.isObject(a)) {
            props = a;
            axis = null;
        }
        // add mandatory values to property map
        props.visible = true;
        return _.chain(AIC.getDimensions(axis)).pluck('levels').flatten().
            map(function(level) {
                return !_.isEmpty(level.members) ? level.members : level;
            }).flatten().filter(function(level) {
                return _.all(props, function(prop, key) {
                    return level[key] === undefined ||  level[key] === prop;
                });
            }).value();
    },
    getFilteredMeasureList : function(a, p) {
        var axis = a || null, props = p || {};
        if (arguments.length === 1 && _.isObject(a)) {
            props = a;
            axis = null;
        }
        return AIC.getFilteredList(axis, _.extend(props, {measure : true, measuresLevel : true}));
    },
    getAvailableFieldsNodeBySelection: function (level, dimension, item) {
        var meta = {};
        item = selObjects.first();
        if (selectionCategory.area == designerBase.AVAILABLE_FIELDS_AREA) {
            if (item.hasChilds()) {
                meta.isMeasure = item.treeId === "measuresTree";
                meta.dimension = meta.isMeasure ? item.treeId : item.param.id;
                meta.uri = localContext.isNonOlapMode() ? item.param.uri : undefined;
                meta.level = null;
            } else {
                meta.isMeasure = !!item.param.extra.isMeasure;
                meta.level = item.param.extra.dimensionId && item.param.id;
                meta.dimension = item.param.extra.dimensionId || item.param.extra.id;
            }
            meta.index = -1;
        } else if (selectionCategory.area == designerBase.ROW_GROUP_MENU_LEVEL
            || selectionCategory.area == designerBase.COLUMN_GROUP_MENU_LEVEL) {

            if (level || dimension) {
                meta.isMeasure = !dimension;
                meta.level = level;
                meta.dimension = meta.isMeasure ? adhocDesigner.MEASURES : dimension;
                meta.index = -1;
            } else if (item.axis) {
                //Display Manager object in selection
                meta = item;
            } else {
                //xtab object in selection
                //TODO get dim and lev from crosstab
                alert("Need a way to get dimension name for clicked level from crosstab");
                return;
            }
        }
        return meta;
    },
    getAvailableFieldsNodesBySelection: function(level, dimension) {
        var metas = [];
        for (var i = 0; i<selObjects.length; i++){
            metas.push(AIC.getAvailableFieldsNodeBySelection(level, dimension,selObjects[i]));
            metas[i].extra = metas[i];
            metas[i].dimensionId = metas[i].dimension;
            metas[i].id = metas[i].level;
        }
        return metas;
    },
    updateContextMenuWithSiblingLevels: function(contextName, contextActionModel) {
        if (!adhocDesigner.ui.display_manager) {
            return contextActionModel;
        }

        var menuToUpdate = contextActionModel.find(function(item) {
            return item.clientTest === "AdHocCrosstab.canAddSiblingLevels";
        });

        if (!menuToUpdate) {
            return contextActionModel;
        }

        var siblingLevels = null;
        var rootNode = null;
        var action = null;

        if (!selObjects.first().isMeasure) {
            if (AIC.isOLAP()) {
                rootNode = adhocDesigner.dimensionsTree.getRootNode().childs.find(function(node) {
                    return node.param.extra.id === selObjects.first().dimension;
                });

                action = selObjects.first().axis === "row" ? "AdHocCrosstab.appendDimensionToRowAxisWithLevel" : "AdHocCrosstab.appendDimensionToColumnAxisWithLevel";
            } else {
                //In nonOLAP mode there is only one level for any dimension
                selObjects.first().index = 0;
                return contextActionModel;
            }
        } else {
            rootNode = adhocDesigner.measuresTree.getRootNode();
            action = selObjects.first().axis === "row" ? "AdHocCrosstab.appendMeasureToRow" : "AdHocCrosstab.appendMeasureToColumn";
        }

        if (selObjects.first().allLevels === undefined) {
            var metadata = selObjects.first();
            metadata.allLevels = AdHocCrosstab.state.getLevelsFromDimension(selObjects.first().dimension, metadata.axis);
            metadata.index = metadata.allLevels.indexOf(metadata.level);
        }

        if (selObjects.first().allLevels) {
            if (AIC.isOLAP()) {
                siblingLevels = rootNode.childs.findAll(function(node) {
                    return !selObjects.first().allLevels.include(node.param.extra.id);
                });
            } else {
                siblingLevels = adhocDesigner.getAllLeaves(rootNode, adhocDesigner.measuresTree)
                    .findAll(function(node) {
                        return !selObjects.first().allLevels.include(node.param.extra.id);
                    });
            }
        }

        if (!siblingLevels || siblingLevels.length == 0) {
            return contextActionModel;
        }

        menuToUpdate.text = adhocDesigner.getMessage(selObjects.first().isMeasure ? 'addMeasures' : 'addLevels');
        menuToUpdate.children = siblingLevels.collect(function(node) {
            return actionModel.createMenuElement("optionAction", {
                text: node.name,
                action: action,
                actionArgs: selObjects.first().isMeasure ? [node.param.extra.id] : [{id: node.param.extra.id, groupId: node.param.extra.dimensionId, isMeasure: false}]
            });
        });

        return contextActionModel;
    },
    getSelectedDimensionIndex: function(selectedObject){
        if(selectedObject){
            var dimension = selectedObject.isMeasure ? adhocDesigner.MEASURES : selectedObject.dimension;
            var index = -1;

            _.find(AIC.getDimensions(selectedObject.axis), function(elem, ind) {
                if (elem.name === dimension) {
                    index = ind;
                    return true;
                }
            });
        }
        return index;
    }
});
/*
 * Select
 */
_.extend(AIC,{
    selectFromDisplayManager: function(event, node, area) {
        designerBase.unSelectAll();
        var isMultiSelect = adhocDesigner.isMultiSelect(event, area);
        selectionCategory.area = area;
        var isSelected = adhocDesigner.isAlreadySelected(node);
        adhocDesigner.addSelectedObject(event, node, isMultiSelect, isSelected);
        Event.stop(event);
    },
    deselectAllSelectedOverlays: function(){}
});

///////////////////////////////////////////////////////////////
// Ajax callbacks
///////////////////////////////////////////////////////////////
_.extend(AIC,{
    standardOpCallback: function(state) {
        if (state) {
            localContext.standardIChartOpCallback(state);
        } else {
            throw "standardOpCallback called without a state!";
        }
    },
    standardIChartOpCallback: function(state) {
        adhocDesigner.render(state);
    }
});
