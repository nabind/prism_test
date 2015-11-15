JRDefaultHighchartsSettingService = {

	setProperty: function(highchartsOptions, propertyPath, propertyValue) {
		var tokens = propertyPath.split('\.');
		var obj = highchartsOptions;
		var idx;
		for (idx = 0; idx < tokens.length - 1; ++idx) {
			var subobj = obj[tokens[idx]];
			if (typeof(subobj) == 'undefined' || subobj == null) {
				subobj = obj[tokens[idx]] = {};
			}
			obj = subobj;
		}
		obj[tokens[idx]] = propertyValue;
	}

}