dialogs.dependentResources = {
    dependenciesPanel: null,

    show: function(resources, actionsMap, options) {
        this.dependenciesPanel = jQuery("#dependencies");
        this._$title = this.dependenciesPanel.find(".content .header .title");
        if(options.dialogTitle) {
            this._titleBackup = this._$title.html();
            this._$title.html(options.dialogTitle);
        }
        dialogs.popup.show(this.dependenciesPanel[0]);

        this._changeMessage(options);
        this._switchButtons(options);
        var list =
            this._initList(resources);

        ///////////////////////////////////////////////////////////
        // Observe buttons
        //////////////////////////////////////////////////////////
        this.dependenciesPanel.on("click", function(event){
            var buttonId = jQuery(event.target).closest('button').attr('id');
            var action = actionsMap && actionsMap[buttonId];
            if (_.include(["dependenciesBtnSave", "dependenciesBtnSaveAs", "dependenciesBtnOk", "dependenciesBtnCancel"], buttonId)) {
                dialogs.dependentResources.hide();
                list.setItems([]);

                event.stopPropagation();

                action && action();
            }
        });
//        designerBase.enableSelection();
    },

    hide: function () {
        if (this.dependenciesPanel) {
            this.dependenciesPanel.off("click");
            dialogs.popup.hide(this.dependenciesPanel[0]);
            this.dependenciesPanel = null;
        }
        if (this._titleBackup && this._$title) {
            this._$title.html(this._titleBackup);
            this._titleBackup = null;
        }
    },

    /**
     * Show message
     *
     * @param canSave
     * @private
     */
    _changeMessage: function(options) {
        jQuery("#topMessage").html(options.topMessage);
        jQuery("#bottomMessage").html(options.bottomMessage);
    },

    _initList: function(resources) {
        var list = new dynamicList.List("dependenciesList", {
            listTemplateDomId: "tabular_oneColumn",
            itemTemplateDomId: "tabular_oneColumn:leaf"
        });

        var items = [];
        if(resources) {
            items = resources.collect(function(resource) {
                var resourceItem = new dynamicList.ListItem({
                    cssClassName: layoutModule.LEAF_CLASS,
                    value: resource
                });

                resourceItem.processTemplate = function(element) {
                    var uriElement = element.select(".uri")[0];
                    var uri;
                    if(typeof this.getValue() == "string") {
                        uri = this.getValue();
                    } else if(this.getValue().uristring) {
                        uri = this.getValue().uristring;
                    } else {
                        uri = this.getValue().URIString;
                    }

                    uriElement.update(uri.escapeHTML());
                    return element;
                };
                return resourceItem;
            });
        }

        list.setItems(items);
        list.show();

        return list;
    },

    _switchButtons: function(options) {
        var $buttonElements = {
            save:  jQuery("#dependenciesBtnSave"),
            saveAs: jQuery("#dependenciesBtnSaveAs"),
            ok: jQuery("#dependenciesBtnOk"),
            cancel: jQuery("#dependenciesBtnCancel")
        };

        var buttons;
        if(options.buttons) {
            buttons = options.buttons;
        } else {
            if(options.okOnly) {
                buttons = ["ok"];
            } else if (options.canSave) {
                buttons = ["save", "saveAs", "cancel"];
            } else {
                buttons = ["ok", "cancel"];
            }
        }

        _.each($buttonElements, function($button, key) {
            if(buttons.indexOf(key) < 0 ) {
                $button.addClass("hidden");
            } else {
                $button.removeClass("hidden");
            }
        });

    }
};