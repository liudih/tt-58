url2000 = "http://img.tomtop-cdn.com/product/xy/2000/2000/";
url560 = "http://img.tomtop-cdn.com/product/xy/560/560/";
url500 = "http://img.tomtop-cdn.com/product/xy/500/500/";
url60 = "http://img.tomtop-cdn.com/product/xy/60/60/";
webUrl = "http://static.tomtop-cdn.com/tomtop/";
var winUrl = window.location.href;
var winArr =  new Array();
winArr = winUrl.split(".");
domain = winArr[0]+".tomtop.com/";
//domain = "http://192.168.220.55/";
//domain = "http://localhost/";

/*
 功能：给登陆后的 url 加参数
 */
$(function(){
	if($("#logout").length==1){
		var aid = $("#logout").attr("aid");
		if(aid != "" && winUrl.indexOf("?aid")== -1 && winUrl.indexOf("&aid")== -1){
			if(winUrl.indexOf("?")== -1){
				window.history.replaceState(null, null, winUrl+"?aid="+aid);
			}else{
				window.history.replaceState(null, null, winUrl+"&aid="+aid);
			}
		}
	}
})
/*
获取url中的参数
 */
function request(paras) {  
   var url = location.href;  
   var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");  
   var paraObj = {}  
   for (i = 0; j = paraString[i]; i++) {  
       paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length);  
   }  
   var returnValue = paraObj[paras.toLowerCase()];  
   if (typeof (returnValue) == "undefined") {  
       return "";  
       } else {  
           return returnValue;  
       }  
   } 
/*
 功能：方法
 参数：循环数组 删除重复数值
 */
function unique(arr){// 遍历arr，把元素分别放入tmp数组(不存在才放)
	var tmp = new Array();
	for(var i in arr){//该元素在tmp内部不存在才允许追加
		if(tmp.indexOf(arr[i])==-1){
			tmp.push(arr[i]);
		}
	}
	return tmp;
}

/*
 功能：保存cookies函数 
 参数：name，cookie名字；value，值
 */
function SetCookie(name, value) {  
   var exp = new Date();  
   exp.setTime(exp.getTime() + 365 * 24 * 60 * 60 * 1000); //3天过期  
   document.cookie = name + "=" + encodeURIComponent(value) + ";domain=.tomtop.com;expires=" + exp.toGMTString()+";path=/";  
   return true;  
};
/*
 功能：设置临时cookie
 参数：name，cookie名字；value，值
 */
function temCookie(name, value) {  
   document.cookie = name + "=" + encodeURIComponent("["+value+"]") + ";domain=.tomtop.com;path=/";  
   return true;  
};
 /*
 功能：获取cookies函数 
 参数：name，cookie名字
 */
 function getCookie(name){
     var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
     if(arr != null){
    	 return unescape(arr[2]); 
     }else{
    	 return null;
     }
 } 
 /*
 功能：删除cookies函数 
 参数：name，cookie名字
 */

 function delCookie(name){
     var exp = new Date();  //当前时间
     exp.setTime(exp.getTime() - 1);
     var cval=getCookie(name);
     if(cval!=null) document.cookie= name + "="+cval+";domain=.tomtop.com;expires="+exp.toGMTString();
 }
 /**
 *功能：计算价格
 *参数：
 * usAmount:价格
 * rate    :汇率
 */
function switchPrice(usAmount,rate){
	var curr = getCookie("TT_CURR");
	if(curr=="JPY"){
		var price = Math.round(usAmount * rate);
	}else{
		var price = (Math.round(((Math.floor((usAmount * rate)*1000))/1000)*100)/100).toFixed(2);
	}
	return price;
}
 /**
 * @desc 方法
 */
//解析cookie值
 function trim(str){     //删除左右两端的空格           
    return str.replace(/(^\s*)|(\s*$)/g, "");  
}    
function parseJSON(data){  
    if ( typeof data !== "string" || !data ) {  
        return null;  
    }  
    data = jQuery.trim(data);  
    if ( window.JSON && window.JSON.parse ) {  
        return window.JSON.parse(data);  
    }  
    if ( rvalidchars.test( data.replace( rvalidescape, "@" )  
        .replace( rvalidtokens, "]" )  
        .replace( rvalidbraces, "")) ) {  
        return ( new Function( "return " + data ) )();  
    }  
    OpPopAlert( "Invalid JSON: " + data );  
    return;  
}  

 /**
 * @desc 方法
 */
//拆分cookie值
function writeCookie(name){//获取指定名称的cookie的值
    var arrLang = document.cookie.split("; ");
    for(var i = 0;i < arrLang.length;i ++){
        var temps = arrLang[i].split("=");
        if(temps[0] == name){
        	return unescape(temps[1]);
        }
   }
}

 /*
 功能：scrollTop
 */
function scTop(){
	var scTop;
	if(document.body.scrollTop){ //非标准写法,chrome能识别
		scTop=document.body.scrollTop;
	}
	else{ //标准写法
		scTop=document.documentElement.scrollTop;
	}
	return scTop;
}
$scTop = scTop();

/*
 *ajax 语言
 * */
function ajaxLang(rUrl){
	if(request('lang') == '' && request('currency') == ''){
		var urls = domain+rUrl
	}else{
		var lang = request('lang');
		var currency = request('currency');
		var urls = domain+rUrl+"&lang="+lang+"&currency="+currency;
	}
	return urls;
}

/////======================只能输入数字================================
function IsNum(e) {
    var k = window.event ? e.keyCode : e.which;
    if (((k >= 48) && (k <= 57)) || k == 8 || k == 0) {
    } else {
        if (window.event) {
            window.event.returnValue = false;
        }
        else {
            e.preventDefault(); //for firefox 
        }
    }
} 

/*
 功能：将字符串转换为代码执行 替代eval方法
 */
var playLang = ['en','es','ru','de','fr','it','jp','pt'];
var jsLanguage='en';
$.each(playLang,function(key,val){
	if(val == getCookie("PLAY_LANG")){
		jsLanguage = getCookie("PLAY_LANG");
	}
})

function strFun(fn) {
    var Fn = Function;  //一个变量指向Function，防止有些前端编译工具报错
    return new Fn('return ' + fn)();
}

/**
 * @desc 通用JS
 */
//切换货币

function switchCurrency(currency){
	var usAmount = 0;
	var rate = currencyRate[currency];
	var label = currencyLabel[currency];
	
	//保存cookie保存cookie
	delCookie("TT_CURR");
	SetCookie("TT_CURR",currency);
	
	$('.pricelab').each(function(){
		usAmount = $(this).attr('usvalue');
		var curr = getCookie("TT_CURR");
		if($(this).hasClass("price")){
			if(curr=="JPY"){
				$(this).html(Math.round(usAmount * rate));
			}else{
				$(this).html((Math.round(((Math.floor((usAmount * rate)*1000))/1000)*100)/100).toFixed(2));
			}
		}else{
			if(curr=="JPY"){
				$(this).html(label+Math.round(usAmount * rate));
			}else{
				$(this).html(label+(Math.round(((Math.floor((usAmount * rate)*1000))/1000)*100)/100).toFixed(2));
			}
		}
	});
	$(".symbolLab").html(label);
}

// 
// function getCookies(currency){//获取指定名称的cookie的值
    // var arrStr = document.cookie.split("; ");
    // var cookStart = document.cookie.indexOf("currency=");
    // for(var i = 0;i < arrStr.length;i ++){
        // var temp = arrStr[i].split("=");
        // if(temp[0] == "currency"){
        	// switchCurrency(unescape(temp[1]));
        // }
   // }
// }

$(function(){
	$(".pu_navHover").parent(".lineBlock,.rightHover").hover(function(){
		$(this).find(".pu_blockWarp").show();
	},function(){$(this).find(".pu_blockWarp").hide();})
	if(getCookie("TT_CURR")){
		var cookies = getCookie("TT_CURR");
		switchCurrency(cookies);
	    // var arrStr = document.cookie.split("; ");
	    // var cookStart = document.cookie.indexOf("currency=");
	    // for(var i = 0;i < arrStr.length;i ++){
	        // var temp = arrStr[i].split("=");
	        // if(temp[0] == "currency"){
	        	// switchCurrency(unescape(temp[1]));
	        // }
	    // }
		var tt_cur_txts = currencyLabel[cookies];
		$(".pu_notranslate").siblings(".pu_navHover").html(tt_cur_txts+"<i class='icon-arr'> </i>")
    }
	//getCookies();
	//读取cookie的值 
	$(".pu_notranslate a").bind("click",function(){
		var currency = $(this).attr("data-currency");
		var txts = $(this).children("span").text();
		switchCurrency(currency);
		$(this).parents(".pu_notranslate").siblings(".pu_navHover").html(txts+"<i class='icon-arr'> </i>");
		$(this).parents(".pu_notranslate").hide();
		$(".symbolLab").html(txts);
	})
})


//切换语言，国家
function switchSiteInfo(country, currency, language){
	var url = window.location.href;
	$.ajax({
		type : 'post',
		url : domain+'index.php?r=site/switchinfo',
		data : {country:country, currency:currency, language:language},
		success : function(data){
			window.location.reload();
		},
		dataType : 'json'
	});
}

/* 改变url参数
* url 目标url 
* arg 需要替换的参数名称 
* arg_val 替换后的参数的值 
* return url 参数替换后的url 
*/ 
function changeURLArg(url,arg,arg_val){ 
    var pattern=arg+'=([^&]*)'; 
    var replaceText=arg+'='+arg_val; 
    if(url.match(pattern)){ 
        var tmp='/('+ arg+'=)([^&]*)/gi'; 
        tmp=url.replace(eval(tmp),replaceText); 
        return tmp;
    }else{ 
        if(url.match('[\?]')){ 
            return url+'&'+replaceText; 
        }else{ 
            return url+'?'+replaceText; 
        } 
    } 
    return url+'\n'+arg+'\n'+arg_val; 
} 
/**
 * @desc 通用JS
 */
//切换语言
function getLanguageId(code){
	switch (code){
		case 'es':
		  landId=2;
		  break;
		case 'ru':
		  landId=3;
		  break;
		case 'de':
		  landId=4;
		  break;
		case 'fr':
		  landId=5;
		  break;
		case 'it':
		  landId=6;
		  break;
		case 'jp':
		  landId=7;
		  break;
		case 'pt':
		  landId=8;
		  break;
		default:
		  landId=1;
	}
	return landId;
}

$(function(){
	//读取cookie的值 
	function switchLanguage(language){
		delCookie("PLAY_LANG");
		SetCookie("PLAY_LANG",language);
		delCookie("TT_LANG");
		SetCookie("TT_LANG",getLanguageId(language));
	}
	
	function getLanguage(language){//获取指定名称的cookie的值
	    var arrLang = document.cookie.split("; ");
	    for(var i = 0;i < arrLang.length;i ++){
	        var temps = arrLang[i].split("=");
	        if(temps[0] == "PLAY_LANG"){
	        	switchLanguage(unescape(temps[1]));
	        }
	   }
	}
	getLanguage();
	var langC = $(".pu_langWarp").siblings(".pu_blockWarp").children("a");
	langC.bind("click",function(){
		var url = window.location.href;
		var urlPath = window.location.pathname;
		var urlId = $(this).attr("data-path");
		var lang = $(this).attr("lang");
		var langTxt = $(this).text();
		switchLanguage(lang);
		
		if(lang == "en"){
			switchCurrency("USD");
		}else if(lang == "ru"){
			switchCurrency("RUB");
		}else if(lang == "jp"){
			switchCurrency("JPY");
		}else{
			switchCurrency("EUR");
		}
		
		$(this).parents(".pu_blockWarp").siblings(".pu_navHover").html(langTxt+"<i class='icon-arr'> </i>");
		$(this).parents(".pu_blockWarp").hide();
		if(url.indexOf("?")== -1){
			window.location = url+"?lang="+lang;
		}else if(url.indexOf("?lang=") == -1 && url.indexOf("&lang=") == -1 && url.indexOf("?") != -1){
			window.location = url+"&lang="+lang;
		}else{
			window.location = changeURLArg(url,'lang',lang);
		}
		if(urlId=="en.tomtop.com"){
			window.location = "http://www.tomtop.com"+urlPath;
		}else{
			window.location = "http://"+urlId+urlPath;
		}
	})
})

/**
 * @desc 通用JS
 */
//切换国家 搜索国家
function Country() {}
Country.prototype = {
	// 得到所有国家
	search : function(q) {
		var regstr = $(q).val() + ".*";
		var reg = new RegExp(regstr, "i");
		$(".country_list li.country_item").each(function(i) {
			$(this).hide();
		});
		$(".country_list li.country_item").each(function(i) {
			if ($.trim($(this).html()) != "") {
				if (reg.test($(this).children("span").html().toLowerCase())) {
					$(this).show();
				} else {
					$(this).hide();
				}
			}
		});
	}
}
$(function(){
var country = new Country();
	$(".country_list li.country_item").click(function() {
		var countryText = $(this).find("span").text();
		var countryCode = $(this).find("span").attr("data");
		var texts = $(this).parentsUntil(".selectFlag", "div.country_all").prev(".pu_navHover");
		texts.children("#current_country_flage").removeClass();
		texts.children("#current_country_flage").addClass("flag_" + countryCode);
		texts.children(".flag_Txt").html(countryText);
		$(this).parents(".country_all").hide();
		//delCookie("country");
		SetCookie("TT_COUN",countryCode);
	});
	$("input[name=country_filter]").keyup(function() {
		country.search(this);
	});
})
/**
 * @desc 通用JS
 */
//返回顶部
$(window).scroll(function() {
	if ($(window).scrollTop() > 300) {
		$(".toTopButton").slideDown(200);
	} else {
		$(".toTopButton").slideUp(200);
	}
});

$(function(){
	$(".toTopButton").click(function() {
		$("html,body").animate({
			scrollTop : 0
		});
	});
})

/**
 * @desc 通用JS
 */
//搜索 传值

$(function(){
	//////关键字点击跳转
	// var searchKey = $('.searchHot').find("a");
	// searchKey.click(function(){
		// var searchK = $(this).text();
		// window.location.href="index.php?r=search/default/index&keyword="+searchK;
	// })
	////////but 跳转
	var searchC = $(".searchWarp").children("input[type='button']");
	searchC.click(function(){
		var searchVal = $(".searchWarp").children("input[type='text']").val();
		if(searchVal!=""){
			window.location.href=domain+"product?q="+searchVal;
		}else{
			$(".searchWarp").children("input[type='text']").focus();
		}
	})
	////////enter键跳转
	var keyEnter = $(".searchWarp").children("input[type='text']");
	var keyword_Input;
	function check_search_keyword() {
		$(document).on("input propertychange", ".searchWarp input[type='text']", function() {
			var keyword = $(this).val();
			clearTimeout(keyword_Input);
			keyword_Input = setTimeout(function() {
				$.ajax({
					url : domain + 'index.php?r=search/default/ajaxkeyauto&keyword='+ keyword,
					timeout : 10000,
					type : 'get',
					dataType : 'html',
					success : function(html) {
						$("#searchThink").html(html);
						$("#searchThink li").click(function(){
							var key = $(this).text();
							keyEnter.val(key);
							window.location.href=domain+"product?q="+key;
						})
					}
				});
			}, 300);
		});
	} 
	check_search_keyword();
	
	keyEnter.focus(function(){
		$(".searchWarp").append("<ul id='searchThink'></ul>");
		var keyword = keyEnter.val();
		////////////////================关键词 异步加载=====================
		$.ajax({
			url : domain + 'index.php?r=search/default/ajaxkeyauto&keyword='+ keyword,
			timeout : 10000,
			type : 'get',
			dataType : 'html',
			success : function(html) {
				$("#searchThink").html(html);
				$("#searchThink li").click(function(){
					var key = $(this).text();
					keyEnter.val(key);
					window.location.href=domain+"product?q="+key;
				})
			}
		});
	})
	keyEnter.click(function(e){
		e.stopPropagation(); 
	})
	$("body").click(function(){
		$("#searchThink").hide();
		$("#searchThink").remove();
	})
	keyEnter.keyup(function(event){
		$this = $("#searchThink");
		if(!$this.is(":hidden")){
			var keys = {
				UP : 38,
				DOWN : 40,
				PAGEUP : 33,
				PAGEDOWN : 34
			};
			var kc = event.keyCode;
			if (kc == keys.UP || kc == keys.PAGEUP) {
				if ($this.find("li:first.active").length == 1 || $this.find("li.active").length == 0) {
					$this.children().removeClass("active").last().addClass("active");
				} else {
					$this.find("li.active").removeClass("active").prev().addClass("active");
				}
				var keywords = $this.find("li.active").html().replace(/<u>(.*)<\/u>/ig, '');
				keywords = keywords.replace(/(<[a-z0-9\/]+>)+/ig, '');
				$(this).val(keywords);
			}
			if (kc == keys.DOWN || kc == keys.PAGEDOWN) {
				if ($this.find("li:last.active").length == 1 || $this.find("li.active").length == 0) {
					$this.children().removeClass("active").first().addClass("active");
				} else {
					$this.find("li.active").removeClass("active").next().addClass("active");
				}
				var keywords = $this.find("li.active").html().replace(/<u>(.*)<\/u>/ig, '');
				keywords = keywords.replace(/(<[a-z0-9\/]+>)+/ig, '');
				$(this).val(keywords);
			}
		}
	})
	
	keyEnter.keypress(function(e){
		var key = e.which;
		if (key == 13) {
			var searchVal = keyEnter.val();
	        window.location.href=domain+"product?q="+searchVal;
	        return false;
	    }
	})
	//限制输入下划线 英文 和数字
	// keyEnter.keyup(function(){
		// this.value=this.value.replace(/[^-_.,#\s\$\/&a-zA-Z0-9]+$/g,'');
	// })
})




/**
 * @desc 方法
 */
//改变图片显示方式
function eachImg(boxClass){
	boxClass.find("img.lazy").each(function(){
	    $(this).attr("src",$(this).attr("data-original"));
	    $(this).removeAttr("data-original");
	});
}



/**
 * @desc 历史记录 返回相关产品
 * @desc 公用
 */

$(function(){

/////======================滚到底部加载产品==================================
	if(getCookie("WEB-history") && $("#viewedFeatured").length==1){
		function Recently(){
			 if($("#viewedFeatured").children().length==0){
				 $contentLoadRecently = true;
			 	 $("#viewedFeatured").append("<div class='loading-ajax' />");
			 	 var urls = ajaxLang("index.php?r=site/ajaxvf");
				 $.ajax({
				    type: "GET",
		    		cache : false,
				    url: urls,
				    dataType:'html',
					 success: function (html)
					 {
					   	 $("#viewedFeatured").html(html)
				   		 $("img.lazy").lazyload();
						 $(".loading-ajax").remove();
						 $contentLoadRecently = true;
						 try{moveBox($(".viewedWarp"));}catch(e){}  //历史记录左边
						 try{moveBox($(".alsoLike"));}catch(e){}  //历史记录左边
						 histC();
					 }
				 });
			 }
		 }
		 $contentLoadRecently = false;
		 $(window).scroll(function(){
			 var scrRecently = $("#viewedFeatured").offset().top;
			 if(scrRecently >= $(window).scrollTop() && scrRecently <($(window).scrollTop()+$(window).height()) && $contentLoadRecently == false)
			 {
				 Recently()
			 }
		 });
		if($("#viewedFeatured").offset().top-$scTop<$(window).height() && $contentLoadRecently == false){
			Recently()
		}
	}
/////======================点击加载产品==================================
	function histC(){
		var lC = $(".viewedWarp").children(".leftArr");
		var rC = $(".viewedWarp").children(".rightArr");
		var listing = 0;
		var list = $(".viewedWarp").find(".moveList");
		
		for(var i=0;i<list.length-1;i++){
			$(".histRightBox").append('<div class="listMoveWarp alsoLike"></div>')
		}
		rC.click(function(){
			listing++;
			if(listing>=list.length){listing=list.length-1}
			var listingID = list.eq(listing).attr("rel-data");
			if($(".alsoLike").eq(listing).children().length==0){
				$(".histRightBox").append("<div class='loading-ajax' />")
				$.ajax({
				   type: "GET",
				   url: domain+"index.php?r=site/ajaxyoumightlike&position=foot",
				   timeout : 10000,
				   data: "listing_id="+listingID,
				   dataType:'html',
				   success: function(html){
				   	 	$(".alsoLike").eq(listing).html(html);
				   	 	//eachImg($(".alsoLike"));
				   	 	$(".alsoLike").find('img').trigger('sporty')
				   	 	$(".alsoLike").find('.lazy').lazyload({event:'sporty'})
					   	$(".alsoLike").eq(listing).siblings(".alsoLike").hide();
					   	$(".alsoLike").eq(listing).show();
					 	try{moveBox($(".alsoLike").eq(listing));}catch(e){}
					 $(".loading-ajax").remove();
				   }
				});
			}else{
			   	$(".alsoLike").eq(listing).siblings(".alsoLike").hide();
			   	$(".alsoLike").eq(listing).show();
			}
		})
		lC.click(function(){
			listing--;
			if(listing<=0){listing=0}
		   	 $(".alsoLike").eq(listing).siblings(".alsoLike").hide();
		   	 $(".alsoLike").eq(listing).show();
		})
	}
})

$(function(){
//显示购物车
	$("#cartNumber").hover(function(){
		var plistL=getCookie("plist");
		//异步取购物车数量
		$.post(domain+"index.php?r=cart/default/cartnum",function(result){
			if(result.status==1){
	   			var num = $("#cartNumber").children(".icon-cart");
	   			num.text(result.cartNumber);
			}else{
		   		$("#cartNumber").children(".icon-cart").text(0);
		   	}
		},"json");
		
		if(plistL!=null){
			showCart();
		}else{
			if($(".noneProduct").length<1){
				$(".cart_content").html("");
				$(".cart_content").append('<div class="noneProduct">'+strFun("TT_language_"+jsLanguage)["tomtop.message.shoppingCartIsEmpty"]+'</div><div class="viewCart"><a href="'+domain+'" class="btn btn-orange">'+strFun("TT_language_"+jsLanguage)["tomtop.common.continueShopping"]+'</a></div>');
			}
		}
	},function(){
		carReady = false;
	})
//导航deals margin-left
	$(".dealsNav").prev("a").mouseover(function(){
		var dealsW = $(".dealsNav").width();
		$(".dealsNav").css({"margin-left":-dealsW/2+70})
	})
})
function dlCart(){
	$(".dlCart").click(function(){
		$(this).unbind("click");
		var dels = $(this).parents("li");
		var listingID = $(this).attr("listing_id");
		var TT_warehouse = $(this).attr("TT_warehouse");
		$.ajax({
		   type: "GET",
		   url: domain+"index.php?r=cart/default/ajaxdeletecart",
		   timeout : 10000,
		   data: {listing_id:listingID,TT_warehouse:TT_warehouse},
		   cache : false,
		   dataType:'json',
		   success: function(data){
		   		if(data.status == 1){
		   			//$("#"+listingID).remove();
		   			var num = $("#cartNumber").children(".icon-cart")
		   			var nums = parseInt(num.text())-1;
		   			if(nums<=0){nums=0}
		   			num.text(nums)
		   			$("#item").text(nums);
		   			if($('.miniScroll').find("li").length==0){
		   				$(".viewCart").remove();
		   				$(".cart_content").hide();
		   			}
		   		}
		   }
		});
		if(dels.siblings('li').length == 0){
			$(this).parents('.cartWarp').prev('.miniCarWare').remove();
			dels.remove();
		}else{
			dels.remove();
		}
	})
}
function showCart(){
	var carReady = false;
	if(carReady==false){
		if($(".cart_content").children().length==0){
			$(".cart_content").append("<div class='loading-ajax' />")
		}
		$.ajax({
			type : 'get',
			url : domain+'index.php?r=cart/default/ajaxshowcart',
			cache : false,
			success : function(result){
				var html = '';
				if(result.status == 1){
					html += '<div class="miniScroll">';
					$.each(result.result,function(i,items){
						html += '<p class="miniCarWare">'+strFun("TT_language_"+jsLanguage)["tomtop.product.shippingFrom"]+' '+ i +' '+strFun("TT_language_"+jsLanguage)["tomtop.product.warehouse"]+'<span></span></p><ul class="cartWarp">';
						$.each(items,function(keys,item){
							var discountDsiplay = '';
							if(item.origprice > item.nowprice){
								var off = 100-Math.round(item.nowprice/item.origprice*100);
								var discountDsiplay = '<span class="saleOff">'+off+'% OFF</span><span class="seleNum">'+item.symbol+item.origprice+'</span>';
							}
							html += '<li class="lbBox" id="'+item.listingId+'">'
										+ '<a class="cartHeader_IMG lineBlock" href="'+domain+item.url+'.html">'
											+ '<img src="'+url60+item.imageUrl+'" />'
										+ '</a>'
										+ '<div class="lineBlock cartHeader_TXT">'
											+ '<a class="cartTxt" href="'+domain+item.url+'.html">'+item.title+'</a>'
											+ '<p class="cartSale">'
												+ ''+discountDsiplay+''
											+ '</p>'
											+ '<p>'
												+ '<span class="cartPrice">'+item.symbol+item.nowprice+'</span>'
												+ '<span>x'+item.num+'</span>'
											+ '</p>'
										+ '</div>'
										+ '<a class="icon-small-close dlCart" TT_warehouse="'+ i +'" listing_id="'+item.listingId+'" href="javascript:;"> </a>'
									+ '</li>'
						})
						html += '</ul>';
					});	
					html += '</div><div class="viewCart"><a href="http://cart.tomtop.com" class="btn btn-orange">'+strFun("TT_language_"+jsLanguage)["tomtop.common.viewCartCheckOut"]+'</a></div>'	
				}else{
					html += '<div class="noneProduct">'+strFun("TT_language_"+jsLanguage)["tomtop.message.shoppingCartIsEmpty"]+'</div><div class="viewCart"><a href="'+domain+'" class="btn btn-orange">'+strFun("TT_language_"+jsLanguage)["tomtop.common.continueShopping"]+'</a></div>';
				}
				$('.cart_content').html(html);
				//改变购物车产品计数 TODO
				carReady=true;
				dlCart();
				$(".loading-ajax").remove();
			},
			error :function(XMLHttpRequest, textStatus, errorThrown){
				html = '<div class="noneProduct">'+errorThrown+'</div>';
				$('.cart_content').html(html);
			},
			dataType : 'json'
		});
	}
}

//ajax登录
function ajaxSig(logins){
	var email = logins.find('#sign_email').val();
	var pw = logins.find('#sign_password').val();
	var eErrHtml = logins.find("#sign_email").next(".help-block");
	var eErrCss = logins.find("#sign_email").parents(".controls");
	var pErrHtml = logins.find("#sign_password").next(".help-block");
	var pErrCss = logins.find("#sign_password").parents(".controls");
	var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
	$.ajax({
		type : 'post',
		url : domain+'index.php?r=member/default/login',
		data : {email:email,pw:pw},
		success : function(result){
			if(result.status==1){
				var url = window.location.href;
				$('.signJoin .controls').addClass('success');
				location.href = url;
				pErrHtml.html("");
				eErrHtml.html("");
			}else{
				if (email != "") {
					isok= reg.test(email );
					if (!isok) {
						eErrHtml.html(strFun("TT_language_"+jsLanguage)["tomtop.message.wrongEmail"]);//您输入的邮箱格式不正确
						eErrCss.addClass("error");
					}else{
						eErrCss.removeClass("error");
						eErrHtml.html("");
					}
				}else{
					eErrHtml.html(strFun("TT_language_"+jsLanguage)["tomtop.message.enterYourEmail"]);//请输入您的邮箱
					eErrCss.addClass("error");
				}
				if(pw == ""){
					pErrHtml.html(strFun("TT_language_"+jsLanguage)["tomtop.message.enterYourPassword"]);//请输入您的密码 
					pErrCss.addClass("error");
				}else{
					pErrCss.removeClass("error");
					pErrHtml.html("");
				}
				if(reg.test(email )&&pw != ""){
					eErrCss.addClass("error");
					pErrCss.addClass("error");
					pErrHtml.html(strFun("TT_language_"+jsLanguage)["tomtop.message.usernameOrPassword"]);//您输入的用户名或密码不正确
				}
				logins.find("#sign_email,#sign_password").focus(function(){
					$(this).siblings(".help-block").text("");
					$(this).parents(".controls").removeClass("error")
				})
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			logins.find('.signJoin .controls').addClass('error');
			logins.find('#sign_password').next('.help-block').html(errorThrown);
		},
		dataType : 'json'
	});
}
$(function(){
	$('#signin').click(function(){
		ajaxSig($(".pu_blockWarp"));
	});
	var keyEnter = $("#sign_password");
	keyEnter.keypress(function(e){
		var key = e.which;
		if (key == 13) {
			ajaxSig($(".pu_blockWarp"));
	        return false;
	    }
	})
})


/////======================公用弹框================================

function successful(){
	var params ={content:strFun("TT_language_"+jsLanguage)["tomtop.common.successfully"]+'!'};	
	params = $.extend({'position':{'zone':'center'},'overlay':true}, params);
	var markup = [
        '<div class="successfulPop_box">',
			'<div class="checkbox"></div>',
			'<p>',params.content,'</p></div>'
    ].join('');
    $(markup).hide().appendTo('body').fadeIn();
    setTimeout(function () { 
        $('.successfulPop_box').fadeOut(function(){$('.successfulPop_box').remove()});
    }, 1000);
}
function errorPop(){
	var params ={content:strFun("TT_language_"+jsLanguage)["tomtop.common.error"]+'!'};	
	params = $.extend({'position':{'zone':'center'},'overlay':true}, params);
	var markup = [
        '<div class="errorPop_Box">',
			'<div class="checkbox"></div>',
			'<p>',params.content,'</p></div>'
    ].join('');
    $(markup).hide().appendTo('body').fadeIn();
    setTimeout(function () { 
        $('.errorPop_Box').fadeOut(function(){$('.errorPop_Box').remove()});
    }, 1000);
}


/////======================登陆弹出公用框==================================
function loginPop(){
	var markup = [
        '<div id="loginPopWarp">',
			'<div class="signJoin lbBox">',
				'<h5>'+strFun("TT_language_"+jsLanguage)["tomtop.common.signIn"]+'</h5>',
				'<i class="icon-Close"> </i>',
				'<div class="lineBlock">',
					'<p class="marB5">'+strFun("TT_language_"+jsLanguage)["tomtop.common.signInWithTomTopAccount"]+'</p>',
				      '<div class="controls">',
				      	'<i class="icon-email"> </i>',
				        '<input id="sign_email" class="span8" type="text" placeholder="'+strFun("TT_language_"+jsLanguage)["tomtop.common.enterYourEmail"]+'">',
				        '<span class="help-block"></span>',
				      '</div>',
				      '<div class="controls">',
				      	'<i class="icon-lock"> </i>',
				        '<input id="sign_password" class="span8" type="password" placeholder="'+strFun("TT_language_"+jsLanguage)["tomtop.common.enterYourPassword"]+'">',
				        '<span class="help-block"></span>',
				      '</div>',
				      '<input id="signins" type="button" value="SIGN IN" class="btn btn-primary">',
				      '<a class="forgetPs" href="http://www.tomtop.com/member/findpass">'+strFun("TT_language_"+jsLanguage)["tomtop.common.forgotPassword"]+'?</a>',
				      '<p class="marT15">'+strFun("TT_language_"+jsLanguage)["tomtop.common.signInWithAnExistingAccount"]+'</p>',
				      '<div class="iconSign marT15">',
				      	'<a class="icon-f" href="https://www.facebook.com/dialog/oauth?client_id=284737934897635&redirect_uri=http://www.tomtop.com/ttfb&response_type=code&scope=email"> </a>',
				      	// '<a class="icon-p" href="javascript:;"> </a>',
				      	'<a class="icon-g" href="https://accounts.google.com/o/oauth2/auth?client_id=1031779384204-umd79kdejl4q5sdul1hjs32c93437jop.apps.googleusercontent.com&redirect_uri=http://www.tomtop.com/ttgoogle&response_type=code&state=state&scope=https://www.googleapis.com/auth/plus.login email"> </a>',
				      '</div>',
				'</div>',
				'<div class="lineBlock rJoinToday">',
					'<p>New to TOMTOP?</p>',
					'<a class="btn btn-primary joinToday" href="http://www.tomtop.com/member/login">',
						strFun("TT_language_"+jsLanguage)["tomtop.common.joinToday"]+'!',
						'<i class="icon-free"> </i>',
					'</a>',
					'<p>'+strFun("TT_language_"+jsLanguage)["tomtop.common.membersOnlyService"]+'</p>',
					'<ol class="onlyService">',
						'<li>'+strFun("TT_language_"+jsLanguage)["tomtop.common.membersOnlyService1"]+'</li>',
						'<li>'+strFun("TT_language_"+jsLanguage)["tomtop.common.membersOnlyService2"]+'</li>',
						'<li>'+strFun("TT_language_"+jsLanguage)["tomtop.common.membersOnlyService3"]+'</li>',
						'<li>'+strFun("TT_language_"+jsLanguage)["tomtop.common.membersOnlyService4"]+'</li>',
						'<li>'+strFun("TT_language_"+jsLanguage)["tomtop.common.membersOnlyService5"]+'</li>',
					'</ol>',
				'</div>',
			'</div>',
			'<div class="pu_popBlack"> </div>',
		'</div>'
    ].join('');
    $(markup).hide().appendTo('body').fadeIn();
    $(".icon-Close,.pu_popBlack").click(function(){
		$("#loginPopWarp").fadeOut(function(){$(this).remove();})
	})
	$('#signins').click(function(){
		ajaxSig($("#loginPopWarp"));
	});
}


/////======================头部关键词展开==================================
// $(function(){
	// var hotH = $(".searchHot").height();
	// if(hotH>20){
		// $(".searchHot").after("<div class='keyHotArr'>+</div>")
	// }
	// $(".keyHotArr").click(function(){
		// if($(".keyHotArr").hasClass("keys")){
			// $(".searchHotWarp").animate({
				// height:15
			// })
			// $(".keyHotArr").text("+")
		// }else{
			// $(".searchHotWarp").animate({
				// height:30
			// })
			// $(".keyHotArr").text("--")
		// }
		// $(this).toggleClass("keys")
	// })
// })

/////======================bottom right historical==================================
$(function(){
	 function rightHist(){
		 $.ajax({
		    type: "GET",
		    cache : false,
		    url: domain+"index.php?r=site/rview",
		    dataType:'html',
			 success: function (html) 
			 {
			   	 $(".fixedRecently").html(html)
				 $("img.lazy").lazyload();
				 rightHistF();
			 }
		 });
	 }
	 rightHist()
})

////=====================右边浏览历史 上下点击==================================

	function rightHistF(){
		var warp = $(".fixedRecently");
		var hidH = warp.find(".moveHidden").outerHeight(true);
		var Mbox = warp.find(".moveBox");
		var list = Mbox.children("li");
		var listH =list.outerHeight(true);
		var MboxH = list.length*listH;
		Mbox.css({"height":MboxH})
		if(list.length<=3){
			warp.find(".moveHidden").css({"height":list.length*listH})
			warp.css({"height":list.length*listH})
		}else{
			warp.find(".moveHidden").css({"height":"168px"})
		}
		if(list.length==0){
			warp.hide();
			$(".fixedBTxt").hide();
		}else{
			warp.show();
			$(".fixedBTxt").show();
		}
	}
/////======================cart number==================================
$(function(){
	 function cartNumber(){
		 $.ajax({
		    type: "GET",
			cache : false,
		    url: domain+"index.php?r=cart/default/cartnum",
		    dataType:'json',
			 success: function (result) 
			 {
			 	if(result.status == 1){
			   	 	$("#cartNumber").children(".icon-cart").text(result.cartNumber);
			   	}else{
			   		$("#cartNumber").children(".icon-cart").text(0);
			   	}
			 }
		 });
	 }
	 cartNumber();
})
/////======================getuser 登陆状态==================================
$(function(){
	 function getuser(){
		 $.ajax({
		    type: "GET",
			cache : false,
		    url: domain+"index.php?r=member/default/getuser",
		    dataType:'json',
			 success: function (data) 
			 {
			 	if(data.status == 1){
			 		$("#loginBox").html(data.result);
			 	}
			 }
		 });
	 }
	 getuser();
})
/////======================浏览记录==================================
/*$(function(){
	 function visit(){
		 $.ajax({
		    type: "GET",
			cache : false,
		    url: domain+"index.php?r=site/visit",
		    dataType:'json',
			 success: function (data){}
		 });
	 }
	 visit();
})*/
/////======================选择国家 Ship to==================================

$(function(){
	 function shipTo(){
		 $.ajax({
		    type: "GET",
			cache : false,
		    url: domain+"index.php?r=site/shipto",
		    dataType:'json',
			 success: function (data){
			 	if(data.status == 1){
			 		var countryName = $(".selectFlag").find('span[data$="'+data.code+'"]').html();
			 		$("#current_country_flage").removeClass("flag_US").addClass("flag_"+data.code);
			 		if(data.code=="CN"){
			 			$("#current_country_flage").next(".flag_Txt").html("China");
			 		}else{
			 			$("#current_country_flage").next(".flag_Txt").html(countryName);
			 		}
			 	}
			 }
		 });
	 }
	 shipTo();
})
function visitWeb(){
	if(request('aid') == ''){
		var aid = getCookie("AID");
	}else{
		var aid = request('aid');
	}
	var url = window.location.href;
	 $.ajax({
	    type: "GET",
	    url: domain+"index.php?r=site/visit",
	    data:{aid:aid,url:url},
	    dataType:'json',
		 success: function (json) 
		 {
		 }
	 });
}

$(function(){
	if(getCookie("AID")||request('aid') != ''){
		visitWeb();
	}
})
/////======================Deals==================================
/////======================Deals==================================
/////======================Deals==================================
// $(function(){
	 // function dayDeals(){
		 // $.ajax({
		    // type: "GET",
		    // url: domain+"index.php?r=site/hot",
		    // dataType:'html',
			 // success: function (html) 
			 // {
			   	 // $(".dealsWarp").html(html)
			   	 // $("img.lazy").lazyload();
			   	 // moveBanner($(".dealsWarp"));
			 // }
		 // });
	 // }
	 // dayDeals()
// })

//================================================================================对话框黑底显示/隐藏
function fnDialogsBg(custom){
	//显示/隐藏对话框黑底:fnDialogs(1.不设置值为自动切换,2.'hide'为隐藏,3.'show'为显示)
	if(custom){
		if(custom=='hide'){
			fnfnDialogBgsHide();
		} else if(custom=='show'){
			fnDialogsBgShow();
		}
	} else{
		if($('#bm_dialogs_bg').length==0 || $('#bm_dialogs_bg').css('display')=='none'){
			fnDialogsBgShow();
		} else{
			fnDialogsBgHide();
		};
	};
};
function fnDialogsBgHide(){
	//隐藏对话框黑底
	$('#bm_dialogs_bg').css('display','none');
	//$('html').css('overflow-y','auto');
};
function fnDialogsBgShow(){
	//显示对话框黑底
	if($('#bm_dialogs_bg').length==0){
		var newElems=$('<div id="bm_dialogs_bg"></div>');
		$('body').append(newElems);
	} else{
		$('#bm_dialogs_bg').css('display','block');
	}
	//$('html').css('overflow-y','hidden');
	$('#bm_dialogs_bg').click(fnDialogsBgHide);
};
//================================================================================点击黑底关闭对话框
$(document).on('click','#bm_dialogs_b',function(){
	fnDialogsBgHide();
});
$(document).on('click','.dialogs',function(){
	fnCloseDialogs($(this));
});
$(document).on('click','.close_dialogs',function(){
	var obj=$(this).parents('.dialogs');
	fnCloseDialogs(obj);
});
$(document).on('click','.dialogs_c',function(e){
	e.stopPropagation();
});
function fnCloseDialogs(obj){
	fnDialogsBg();
	obj.removeClass('dialogs_show');
};
//===============================================================================IE低版本提示
$(function(){
	fnBrowserV(function(v){
		if(v){
			var newElems=$('<div class="dialogs dialogs_show">' +
								'<i></i>' +
								'<span class="dialogs_c iev">' +
									'<p>Your browser is an expired version, please update it  to view this page.</p>' +
									'<i class="close_dialogs"></i>' +
								'</span>' +
							'</div>');
			$('body').append(newElems);
			fnDialogsBg();
		};
	});
});

function fnBrowserV(fn){
	if(window.navigator.userAgent.indexOf("IE") !== -1){
		var browser=navigator.appName;
		var b_version=navigator.appVersion;
		var version=b_version.split(";");
		var trim_Version=version[1].replace(/[ ]/g,"");
		if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE4.0")
		{
			fn(true);
		}
		else if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE5.0")
		{
			fn(true);
		}
		else if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE6.0")
		{
			fn(true);
		}
		else if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE7.0")
		{
			fn(true);
		}
		else if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE8.0")
		{
			fn(false);
		}
		else if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE9.0")
		{
			fn(false);
		}
		else if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE10.0")
		{
			fn(false);
		}
	}
}
