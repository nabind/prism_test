/*
 * Copyright (C) 2005 - 2011 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased  a commercial license agreement from Jaspersoft,
 * the following license terms  apply:
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License  as
 * published by the Free Software Foundation, either version 3 of  the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero  General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public  License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

JRS.ViewQueryDialog = (function($) {
    var Dialog = function(options) {
        this.template = $(options.id);
        this.selectionContainer = options.selectionContainer;
        this.updateContent(options.content);
        this.registerEvents();
    };

    Dialog.fn = Dialog.prototype;

    Dialog.fn.close = function() {
        disableSelectionWithoutCursorStyle(this.selectionContainer);
        dialogs.popup.hide(this.template.get(0));
    };

    Dialog.fn.show = function() {
        enableSelection(this.selectionContainer);
        dialogs.popup.show(this.template.get(0));
    };

    Dialog.fn.updateContent = function(content) {
        $('.content .body textarea', this.template).html(content);
    };

    Dialog.fn.registerEvents = function() {
        $('.button', this.template).on('click', $.proxy(this.close, this));
    };
    return Dialog;
})(jQuery);

JRS.SaveAsDialog = (function(jQuery, dynamicTree) {
    return function(inputOptions) {
        var defaultOptions = {
            templateMatcher: "#saveAs",
            insertAfterMatcher: "#frame",
            cloneTemplate: false,
            elementId: null,
            okButtonMatcher: ".saveButton",
            cancelButtonMatcher: ".cancelButton",
            inputMatchers: {
                name: ".resourceName",
                description: ".resourceDescription"
            },
            foldersTreeMatcher: "ul.folders",
            organizationId: "",
            publicFolderUri: "/public",
            grabKeydownEvents: true,
            validator: function(placeToSave) { return true;}, //default validator
            saveHandler: function(placeToSave) { //default save handler
                var deferred = jQuery.Deferred(); //use this to delay dialog close until server confirms successful save
                deferred.resolve();
                return deferred;
            }
        }
        var opt = jQuery.extend({}, defaultOptions, inputOptions);
        var thisSaveAsDialog = this;
        var dialogElement = getDialogElement(opt);
        this.dialogElement = dialogElement;
        var inputElements = findInputElements(dialogElement, opt);
        this.inputElements = inputElements;
        var placeToSave = {folder: null};
        var saveAsTree = null;
        var foldersTree = dialogElement.find(opt.foldersTreeMatcher);
        this.foldersTree = foldersTree;

        function getDialogElement(options) {
            var dialogElement = jQuery(options.templateMatcher);
            if(opt.cloneTemplate) {
                dialogElement = dialogElement.clone();
            }
            if(opt.cloneTemplate && opt.elementId) {
                dialogElement.attr("id", options.elementId);
            }
            jQuery(opt.insertAfterMatcher).append(dialogElement);
            dialogElement.on("keydown", function(event) {
                if(opt.grabKeydownEvents) {
                    event.stopPropagation();
                }
                if(event.keyCode == Event.KEY_ESC) {
                    thisSaveAsDialog.close();
                }
            });
            return dialogElement
        }

        function findInputElements(dialogElement, opt) {
            var ret = {};
            for(inputName in opt.inputMatchers) {
                if(!opt.inputMatchers.hasOwnProperty(inputName)) {
                    continue;
                }
                ret[inputName] = dialogElement.find(opt.inputMatchers[inputName]);
            }
            return ret;
        }

        function updatePlaceToSave(place) {
            for(inputName in inputElements) {
                if(!inputElements.hasOwnProperty(inputName)) {
                    continue;
                }
                place[inputName] = inputElements[inputName].val();
            }
            var selNode = saveAsTree.getSelectedNode();
            if(selNode) {
                place.folder = selNode.param.uri;
                place.isWritable = selNode.param.extra ? selNode.param.extra.isWritable : true
            } else {
                place.folder = null;
            }
        }

        function okButtonHandler(event) {
            event.stopPropagation();
            updatePlaceToSave(placeToSave);
            if(!opt.validator(placeToSave)) {
                return;
            }
            opt.saveHandler(placeToSave).then(function() {
                thisSaveAsDialog.close();
            });
        };

        function cancelButtonHandler(event) {
            event.stopPropagation();
            thisSaveAsDialog.close();
        };

        function getSaveAsTree() {
            var foldersTreeId = foldersTree.attr("id");
            var saveAsTree = dynamicTree.createRepositoryTree(foldersTreeId, {
                providerId: 'adhocRepositoryTreeFoldersProvider',
                rootUri: '/',
                organizationId: opt.organizationId,
                publicFolderUri: opt.publicFolderUri,
                urlGetNode: 'flow.html?_flowId=adhocTreeFlow&method=getNode',
                urlGetChildren: 'flow.html?_flowId=adhocTreeFlow&method=getChildren',
                treeErrorHandlerFn: function() {}
            });

            return saveAsTree;
        }

        this.open = function(initialPlaceToSave) {
            placeToSave = initialPlaceToSave;
            if(!placeToSave.folder) {
                placeToSave.folder = "/";
            }

            dialogs.popup.show(dialogElement.get(0));

            for(inputName in inputElements) {
                if(!inputElements.hasOwnProperty(inputName)) {
                    continue;
                }
                inputElements[inputName].val(placeToSave[inputName]);
            }

            dialogElement.find(opt.okButtonMatcher).click(okButtonHandler);
            dialogElement.find(opt.cancelButtonMatcher).click(cancelButtonHandler);

            saveAsTree = getSaveAsTree();

            /*
             saveAsTree.observe("node:selected", function (event) {
             try {
             dialogElement.find(opt.okButtonMatcher)[0].disabled = !event.memo.node.param.extra.isWritable;
             }
             catch (e) {
             dialogElement.find(opt.okButtonMatcher)[0].disabled = true;
             console && console.log("report.view.pro[this.open] - " + e);
             }
             });
             */

            var deferred = jQuery.Deferred();
            saveAsTree.showTreePrefetchNodes(placeToSave.folder, function() {
                saveAsTree.openAndSelectNode(placeToSave.folder);
                deferred.resolve();
            });
            return deferred;
        }

        this.close = function() {
            dialogElement.find(opt.okButtonMatcher).unbind("click", okButtonHandler);
            dialogElement.find(opt.cancelButtonMatcher).unbind("click", cancelButtonHandler);
            dialogs.popup.hide(dialogElement.get(0));
        }
    }
})(jQuery, dynamicTree);



JRS.RepositorySelectionDialog = (function(jQuery, dynamicTree) {
    var defaultOptions = {
        idsPrefix: "repSelDialog_" ,
        templateMatcher: "#repositorySelectionDialog",
        insertAfterMatcher: "#frame",
        okButtonMatcher: ".okButton",
        cancelButtonMatcher: ".cancelButton",
        repositoryTreeMatcher: "ul.repositoryTree",
        organizationId: "",
        publicFolderUri: "/public",
        treeFlowId: "treeFlow",
        treeProviderId: "repositoryExplorerTreeFoldersProvider",
        uriOnCancel: null,
        selectionValidationMessage: "Item not selected",
        acceptOnlyLeaf: true,
        okHandler: function(selectedUri) { //default ok handler
            var deferred = jQuery.Deferred(); //use this to delay dialog close until server confirms successful save
            deferred.resolve();
            return deferred;
        }
    };

    return function(inputOptions) {
        var opt = jQuery.extend({}, defaultOptions, inputOptions);
        var thisDialog = this;

        function getDialogElement(options) {
            var dialogElement = jQuery(options.templateMatcher).clone();
            if(opt.elementId) {
                dialogElement.attr("id", options.elementId);
            }
            jQuery(opt.insertAfterMatcher).append(dialogElement);
            return dialogElement
        }

        this._createRepositoryTree = function() {
            var treeId = opt.idsPrefix+"repTree";
            this.$repositoryTree.attr("id", treeId);
            var tree = dynamicTree.createRepositoryTree(treeId, {
                providerId: opt.treeProviderId,
                rootUri: '/',
                organizationId: opt.organizationId,
                publicFolderUri: opt.publicFolderUri,
                urlGetNode: "flow.html?_flowId="+opt.treeFlowId+"&method=getNode",
                urlGetChildren: "flow.html?_flowId="+opt.treeFlowId+"&method=getChildren",
                treeErrorHandlerFn: function() {}
            });

            return tree;
        }

        function okButtonHandler(event) {
            event.stopPropagation();

            var selNode = thisDialog._repositoryTree.getSelectedNode();

            if(!selNode || opt.acceptOnlyLeaf && selNode.isParent()){
                alert(opt.selectionValidationMessage);
                return;
            }

            var okDeferred = opt.okHandler(selNode.param.uri);
            if(okDeferred) {
                okDeferred.then(function() {
                    thisDialog.close();
                });
            } else {
                thisDialog.close();
            }
        };

        function cancelButtonHandler(event) {
            event.stopPropagation();
            thisDialog.close();
            if(opt.uriOnCancel) {
                document.location=opt.uriOnCancel;
            }
        };

        this.init = function() {
            this.$dialogElement = getDialogElement(opt);
            this.$repositoryTree = this.$dialogElement.find(opt.repositoryTreeMatcher);
            this.$dialogElement.find(opt.okButtonMatcher).click(okButtonHandler);
            this.$dialogElement.find(opt.cancelButtonMatcher).click(cancelButtonHandler);
        }

        this.open = function() {
            dialogs.popup.show(this.$dialogElement.get(0), true);
            this._repositoryTree = this._createRepositoryTree();

            var deferred = jQuery.Deferred();
            this._repositoryTree.showTreePrefetchNodes("/", function() {
                thisDialog._repositoryTree.openAndSelectNode("/");
                deferred.resolve();
            });
            this._repositoryTree.observe("leaf:dblclick", okButtonHandler);
            return deferred;
        }

        this.close = function() {
            dialogs.popup.hide(this.$dialogElement.get(0));
        }

        this.init(); //TODO lazily call init when user opens the dialog for the first time

    }
    })(jQuery, dynamicTree);

JRS.RepositorySelectionDialog = (function(jQuery, dynamicTree) {
    var defaultOptions = {
        idsPrefix: "repSelDialog_" ,
        templateMatcher: "#repositorySelectionDialog",
        insertAfterMatcher: "#frame",
        okButtonMatcher: ".okButton",
        cancelButtonMatcher: ".cancelButton",
        repositoryTreeMatcher: "ul.repositoryTree",
        organizationId: "",
        publicFolderUri: "/public",
        treeFlowId: "treeFlow",
        treeProviderId: "repositoryExplorerTreeFoldersProvider",
        uriOnCancel: null,
        selectionValidationMessage: "Item not selected",
        acceptOnlyLeaf: true,
        okHandler: function(selectedUri) { //default ok handler
            var deferred = jQuery.Deferred(); //use this to delay dialog close until server confirms successful save
            deferred.resolve();
            return deferred;
        }
    };

    return function(inputOptions) {
        var opt = jQuery.extend({}, defaultOptions, inputOptions);
        var thisDialog = this;

        function getDialogElement(options) {
            var dialogElement = jQuery(options.templateMatcher).clone();
            if(opt.elementId) {
                dialogElement.attr("id", options.elementId);
            }
            jQuery(opt.insertAfterMatcher).append(dialogElement);
            return dialogElement
        }

        this._createRepositoryTree = function() {
            var treeId = opt.idsPrefix+"repTree";
            this.$repositoryTree.attr("id", treeId);
            var tree = dynamicTree.createRepositoryTree(treeId, {
                providerId: opt.treeProviderId,
                rootUri: '/',
                organizationId: opt.organizationId,
                publicFolderUri: opt.publicFolderUri,
                urlGetNode: "flow.html?_flowId="+opt.treeFlowId+"&method=getNode",
                urlGetChildren: "flow.html?_flowId="+opt.treeFlowId+"&method=getChildren",
                treeErrorHandlerFn: function() {}
            });

            return tree;
        }

        function okButtonHandler(event) {
            event.stopPropagation();

            var selNode = thisDialog._repositoryTree.getSelectedNode();

            if(!selNode || opt.acceptOnlyLeaf && selNode.isParent()){
                alert(opt.selectionValidationMessage);
                return;
            }

            var okDeferred = opt.okHandler(selNode.param.uri);
            if(okDeferred) {
                okDeferred.then(function() {
                    thisDialog.close();
                });
            } else {
                thisDialog.close();
            }
        };

        function cancelButtonHandler(event) {
            event.stopPropagation();
            thisDialog.close();
            if(opt.uriOnCancel) {
                document.location=opt.uriOnCancel;
            }
        };

        this.init = function() {
            this.$dialogElement = getDialogElement(opt);
            this.$repositoryTree = this.$dialogElement.find(opt.repositoryTreeMatcher);
            this.$dialogElement.find(opt.okButtonMatcher).click(okButtonHandler);
            this.$dialogElement.find(opt.cancelButtonMatcher).click(cancelButtonHandler);
        }

        this.open = function() {
            dialogs.popup.show(this.$dialogElement.get(0), true);
            this._repositoryTree = this._createRepositoryTree();

            function processTreeNodeIcon(node) {//TODO most code copypasted from adhoc.start.js. Think of reusing
                if(node.isParent()){
                    for(var i = 0; i < node.childs.length; i++){
                        processTreeNodeIcon(node.childs[i]);
                    }
                }else{
                    if (node.param.type == 'com.jaspersoft.ji.adhoc.AdhocDataView') {
                        node.param.cssClass = "adhocDataView";
                        node.refreshStyle();
                    }
                }
            }

            var deferred = jQuery.Deferred();
            this._repositoryTree.observe("children:loaded", function(event){
                var nodes = event.memo.nodes;
                nodes.each(function(node){
                    //iterate all nodes and assign css class
                    processTreeNodeIcon(node);
                });
            });

            this._repositoryTree.showTreePrefetchNodes("/", function() {
                thisDialog._repositoryTree.openAndSelectNode("/");
                deferred.resolve();
            });
            this._repositoryTree.observe("leaf:dblclick", okButtonHandler);
            return deferred;
        }

        this.close = function() {
            dialogs.popup.hide(this.$dialogElement.get(0));
        }

        this.init(); //TODO lazily call init when user opens the dialog for the first time

    }
})(jQuery, dynamicTree);
