
if(typeof $!='undefined'){var dynSocialBookmarks={params:{delay:1000,hideClass:'dynSBM-hide',holder:'#share-this-page',toggler:'a.share',parentDiv:'#pageTools'},target:false,timer:false,left:0,offLeft:'-999999',parseArgs:function(args){if(!args){return false;}
if(args['delay']){this.params.delay=args['delay'];}
if(args['hideClass']){this.params.hideClass=args['hideClass'];}
if(args['holder']){this.params.holder=args['holder'];}
if(args['toggler']){this.params.toggler=args['toggler'];}
if(args['parentDiv']){this.params.parentDiv=args['parentDiv'];}},init:function(args){this.parseArgs(args);this.target=$(this.params.holder);if($.browser.msie){this.target.css({'marginTop':'1.5em'});}
$toggler=$(this.params.parentDiv+' '+this.params.toggler);var position=$toggler.position();this.left=position.left;$toggler.click(function(){dynSocialBookmarks.toggleIt();dynSocialBookmarks.timer=setTimeout(function(){dynSocialBookmarks.toggleIt();},dynSocialBookmarks.params.delay);return false;});this.target.bind('mouseout',function(){dynSocialBookmarks.timer=setTimeout(function(){dynSocialBookmarks.toggleIt();},dynSocialBookmarks.params.delay);});this.target.bind('mouseover',function(){clearTimeout(dynSocialBookmarks.timer);});},toggleIt:function(){var pos=this.target.position();pos=pos.left;if(pos==this.offLeft||this.target.hasClass(this.params.hideClass)){this.target.removeClass(this.params.hideClass);this.target.css({'left':this.left+'px'});}else{this.target.css({'left':this.offLeft+'px'});}}};$(document).ready(function(){dynSocialBookmarks.init();});};
