JRItemHyperlinkHighchartsSettingService = {

	configureHyperlinks: function(highchartsOptions, seriesId, hyperlinkProperty) {
		var series = null;
		for (var idx = 0; idx < highchartsOptions.series.length; ++idx) {
			if (highchartsOptions.series[idx]._jrid == seriesId) {
				series = highchartsOptions.series[idx];
				break;
			}
		}
		
		if (!series) {
			return;
		}
		
		series.cursor = 'pointer';
		series.point = series.point || {};
		series.point.events = series.point.events || {};
		//FIXME check if click already exists?
		series.point.events.click = function() {
			JRItemHyperlinkHighchartsSettingService.openHyperlink(this.options[hyperlinkProperty]);
		}
	},
	
	openHyperlink: function(hyperlink) {
		if (!hyperlink || !hyperlink.url) {
			return;
		}
		
		if (!hyperlink.target || hyperlink.target == 'None' || hyperlink.target == 'Self') {
			location.href = hyperlink.url;
		} else if (hyperlink.target == 'Blank') {
			window.open(hyperlink.url, '_blank');
		} else if (hyperlink.target == 'Parent') {
			window.open(hyperlink.url, '_parent');
		} else if (hyperlink.target == 'Top') {
			window.open(hyperlink.url, '_top');
		} else {
			// TODO lucianc frame support
		}
	}

}