/*
 * Copyright (C) 2005 - 2011 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */

var home = {
    _locationMap: {},

    initialize: function(options) {
        webHelpModule.setCurrentContext("bi_overview");

        if (options.locationMap) {
            this._locationMap = options.locationMap;
        }

        this._initHandlers();
    },

    _initHandlers: function() {
        var buttons = $(document.body).select('.button.action.jumbo');

        buttons.each(function(button) {
            $(button).observe('click', function(e) {
                var buttonId = button.identify();

                if (this._locationMap[buttonId]) {
                    if(_.isFunction(this._locationMap[buttonId])) {
                        (this._locationMap[buttonId])();
                    } else {
                        document.location = this._locationMap[buttonId];
                    }
                }
            }.bindAsEventListener(home));
        });
    }
};
