/*
 This file contains validations that are too specific to be part of the core
 Please reference the file AFTER the translation file or the rules will be overwritten
 Use at your own risk. We can't provide support for most of the validations
*/
(function($){
	if($.validationEngineLanguage == undefined || $.validationEngineLanguage.allRules == undefined )
		alert("Please include other-validations.js AFTER the translation file");
	else {
		$.validationEngineLanguage.allRules["postcodeUK"] = {
		        // UK zip codes
		        "regex": /^([A-PR-UWYZ0-9][A-HK-Y0-9][AEHMNPRTVXY0-9]?[ABEHMNPRVWXY0-9]? {1,2}[0-9][ABD-HJLN-UW-Z]{2}|GIR 0AA)$/,
				"alertText": "* Invalid postcode"
		};
		$.validationEngineLanguage.allRules["onlyLetNumSpec"] = {
				// Good for database fields
				"regex": /^[0-9a-zA-Z_-]+$/,
				"alertText": "* Only Letters, Numbers, hyphen(-) and underscore(_) allowed"
		};
		$.validationEngineLanguage.allRules["fileNameGD"] = {
				// Group download file name
				"regex": /^[0-9a-zA-Z\ \_\.\-]+$/,
			    "alertText": "* File name is not valid"
		};
		$.validationEngineLanguage.allRules["onlyNumber"] = {
				"regex": /^[0-9]+$/,
				"alertText": "* Only Numbers are allowed"
		};
		$.validationEngineLanguage.allRules["nineDigitNumber"] = {
				"regex": /^[0-9]{9}$/,
				"alertText": "* Only Numbers are allowed<br/> * 9 digits are required"
		};
		$.validationEngineLanguage.allRules["fourDigitNumber"] = {
				"regex": /^[0-9]{4}$/,
				"alertText": "* Only Numbers are allowed"
		};
	//	# more validations may be added after this point
	}
})(jQuery);
