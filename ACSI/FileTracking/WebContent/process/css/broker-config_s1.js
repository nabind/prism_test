/*
Copyright (c) 2010, comScore Inc. All rights reserved.
version: 4.6.3
*/

COMSCORE.SiteRecruit.Broker.config = {
	version: "4.6.3",
	//TODO:Karl extend cookie enhancements to ie userdata
		testMode: false,
	// cookie settings
	cookie:{
		name: 'msresearch',
		path: '/',
		domain:  '.microsoft.com' ,
		duration: 90,
		rapidDuration: 0,
		expireDate: ''
	},
	
	// optional prefix for pagemapping's pageconfig file
	prefixUrl: "",

		mapping:[
	// m=regex match, c=page config file (prefixed with configUrl), f=frequency
	{m: '//[\\w\\.-]+/athome/(default\\.aspx)?$', c: 'inv_c_3331mt3-Static.js', f: 0.0212, p: 1 	}
	,{m: '//[\\w\\.-]+/athome/', c: 'inv_c_3331mt3.js', f: 0.0122, p: 0 	}
	,{m: '//[\\w\\.-]+/atwork', c: 'inv_c_3331mt5.js', f: 0.02, p: 0 	}
	,{m: '//[\\w\\.-]+/australia/business/', c: 'inv_c_p15466742-au-373.js', f: 0.5, p: 1 	}
	,{m: '//[\\w\\.-]+/australia/business/(default\\.aspx)?$', c: 'inv_c_p15466742-au-373-SB-FIXED.js', f: 0.3, p: 2 	}
	,{m: '//[\\w\\.-]+/australia/licensing/howtobuy/', c: 'inv_c_p15466742-AU-977.js', f: 0.5, p: 1 	}
	,{m: '//[\\w\\.-]+/australia/windows/', c: 'inv_c_p15466742-AU-826-p37131508-AU.js', f: 0.0796, p: 1 	}
	,{m: '//[\\w\\.-]+/brasil/windows', c: 'inv_c_p37131508-Brazil.js', f: 0.0188, p: 1 	}
	,{m: '//[\\w\\.-]+/brasil/windows7/($|default\\.aspx$)', c: 'inv_c_p37131508-Brazil_FL-FIX.js', f: 0.0188, p: 2 	}
	,{m: '//[\\w\\.-]+/canada/windows/', c: 'inv_c_p37131508-Canada.js', f: 0.039, p: 1 	}
	,{m: '//[\\w\\.-]+/china/windows/', c: 'inv_c_p37131508-China.js', f: 0.00094, p: 1 	}
	,{m: '//[\\w\\.-]+/de/de/(default\\.aspx)?$', c: 'inv_c_p15466742-Germany-HP.js', f: 0.0236, p: 0 	}
	,{m: '//[\\w\\.-]+/de/de/dynamicit/', c: 'inv_c_p15466742-Germany-AP-1137.js', f: 0.01, p: 1 	}
	,{m: '//[\\w\\.-]+/de-de/cloud/', c: 'inv_c_p73639549-Germany.js', f: 0.25, p: 0 	}
	,{m: '//[\\w\\.-]+/downloads/(en/|.*?displaylang=en)', c: 'inv_c_3331mt13_NEW_751-753.js', f: 0.00068, p: 0 	}
	,{m: '//[\\w\\.-]+/dynamics/asmartmove/default\\.mspx', c: 'inv_c_3331mt14-SL-fix_NEW-750.js', f: 0.5, p: 3 	}
	,{m: '//[\\w\\.-]+/en/au/', c: 'inv_c_p15466742-AU-HP-369.js', f: 0.08738, p: 0 	}
	,{m: '//[\\w\\.-]+/en/us/default\\.aspx', c: 'inv_c_p15394611-US-HP.js', f: 0.01, p: 0 	}
	,{m: '//[\\w\\.-]+/en-us/cloud/', c: 'inv_c_p80033199-p73639549.js', f: 0.25, p: 0 	}
	,{m: '//[\\w\\.-]+/en-us/cloud/(#tab2-small|default\\.aspx.*)?$', c: 'inv_c_p73639549.js', f: 0.25, p: 1 	}
	,{m: '//[\\w\\.-]+/en-us/dynamics/(?!(customersource\\.aspx|partner-login\\.aspx|solution-finder\\.aspx))', c: 'inv_c_3331mt14_NEW-750.js', f: 0.5, p: 0 	}
	,{m: '//[\\w\\.-]+/fr/fr/(default\\.aspx)?$', c: 'inv_c_p15466742-France-HP.js', f: 0.07, p: 0 	}
	,{m: '//[\\w\\.-]+/france/windows/', c: 'inv_c_FR-p15466742_p37131508-Windows.js', f: 0.063, p: 1 	}
	,{m: '//[\\w\\.-]+/germany/branchen/', c: 'inv_c_DE-p15466742-Branchen.js', f: 0.5, p: 1 	}
	,{m: '//[\\w\\.-]+/germany/business/cloudservices/', c: 'inv_c_p15466742-Germany-AP-1135.js', f: 0.01, p: 1 	}
	,{m: '//[\\w\\.-]+/germany/dynamics/', c: 'inv_c_p15466742-Germany-AP-280.js', f: 0.01, p: 1 	}
	,{m: '//[\\w\\.-]+/germany/server(/|$)', c: 'inv_c_DE-wss-p12038685.js', f: 0.5, p: 1 	}
	,{m: '//[\\w\\.-]+/germany/windows(/|$)', c: 'inv_c_DE-p12038685-p37131508-Windows.js', f: 0.0337, p: 1 	}
	,{m: '//[\\w\\.-]+/ja/jp/', c: 'inv_c_p15466742-Japan-HP.js', f: 0.015, p: 0 	}
	,{m: '//[\\w\\.-]+/japan/athome/', c: 'inv_c_JA-p15466742-athome.js', f: 0.003, p: 1 	}
	,{m: '//[\\w\\.-]+/japan/atwork/', c: 'inv_c_JA-p15466742-atwork.js', f: 0.035, p: 1 	}
	,{m: '//[\\w\\.-]+/japan/business/', c: 'inv_c_JA-p15466742-business.js', f: 0.107, p: 1 	}
	,{m: '//[\\w\\.-]+/japan/servers/', c: 'inv_c_JA-p15466742-servers.js', f: 0.04, p: 1 	}
	,{m: '//[\\w\\.-]+/japan/technet/', c: 'inv_c_JA-p12038685-technet.js', f: 0.002, p: 1 	}
	,{m: '//[\\w\\.-]+/japan/windows(/(?!(downloads/ie/au\\.mspx)|(downloads/ie/iedelete\\.mspx))|$)', c: 'inv_c_JA-p15466742-p37131508-windows.js', f: 0.0074, p: 1 	}
	,{m: '//[\\w\\.-]+/korea/windows', c: 'inv_c_p37131508-Korea.js', f: 0.00169, p: 1 	}
]
};
COMSCORE.SiteRecruit.Broker.run();