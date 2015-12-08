/**
 * This section is to add new js file for APT table report
 * This new imported js file will work after the tabular report is fully loaded
 */
var imported = document.createElement("script");
imported.src = "./scripts/js/report/contextMenu.js";
document.getElementsByTagName("head")[0].appendChild(imported);

/**
 * requirejs plugin to put css link tags inside the head element
 */
define({
    load: function (name, require, onload, config) {
        var link = document.createElement("link");
        link.type = "text/css";
        link.rel = "stylesheet";
        link.href = require.toUrl(name);

        // FIXME: probably webfonts could be better identified
        if (link.href.indexOf('font') != 0) {
            link.className = 'jrWebFont';
        }

        document.getElementsByTagName("head")[0].appendChild(link);

        onload();
        return;
    }
});