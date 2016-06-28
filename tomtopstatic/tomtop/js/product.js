/***************************运费相关***********************/
var TT_NS = TT_NS || {};//tomtop命名空间
TT_NS.cookiePkg = {
	'getCookie': function(name){
		var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
		return arr != null ? unescape(arr[2]) : null;
	},
	'setCookie':function(name, value){  
	   var exp = new Date();  
	   exp.setTime(exp.getTime() + 365 * 24 * 60 * 60 * 1000); //3天过期  
	   document.cookie = name + "=" + encodeURIComponent(value) + ";domain=.tomtop.com;expires=" + exp.toGMTString()+";path=/";  
	   return true;  
	}
};

TT_NS.shipping = {
	'box':null,
	'reqUrl':domain + 'index.php?r=shipping/default/ajaxshipping',
	'countries' : null,
	'lastReqData':null,
	'waiterUrl':'http://tb.53kf.com/webCompany.php?arg=10045862&style=1',
	'country':null,
	'getCountries':function(){
		var countries = {};
		$('.country_list').eq(0).children('li').each(function(index, dom){
			var _code = $(dom).children('span').attr('data');
			if(!countries[_code]){
				var _name = $(dom).children('span').html();
				countries[_code] = _name;
			}
		});
		return countries;
	},
	'init': function(){
		var _this = this;
		var _countryCode = _this.getCountry();
		_this.box = $('.dialogs .dialogs_c');
		_this.country = _this.country || _countryCode;
		_this.countries = _this.countries || _this.getCountries();
		$('.state').html('to<b>' + _this.countries[_countryCode] + '</b>');
		$('.tt_ns_shipping_to .flag_Txt').html(_this.countries[_countryCode]);
		$('.tt_ns_shipping_to span:first-child').each(function(index, dom){
			$(dom).attr('class', 'flag_' + _countryCode);
		});
	},
	'changeCountry':function(countryCode){
		var _this = this;
		_this.country = countryCode;
		$('.logistics .state').html('to<b>' + _this.countries[countryCode] + '</b>');
		$('.tt_ns_shipping_to span:first-child').each(function(index, dom){
			$(dom).attr('class', 'flag_' + countryCode);
		});
		TT_NS.cookiePkg.setCookie('TT_COUN', countryCode);
		$('.tt_ns_shipping_to .flag_Txt').html(_this.countries[_this.getCountry()]);
	},
	'getCountry':function(){
		return TT_NS.cookiePkg.getCookie('TT_COUN');
	},
	'getCurrency':function(){
		return 'USD';
	},
	'getLanguage':function(){
		return getLanguageId(TT_NS.cookiePkg.getCookie('PLAY_LANG'));
	},
	'getRealCurrency':function(){
		return TT_NS.cookiePkg.getCookie('TT_CURR');
	},
	'getStorageId':function(){
		return $('#storageid').val();
	},
	'getWhouse':function(){
		return $('#whouse').val();
	},
	'getListingId':function(){
		return $('#listingId').val();
	},
	'getQty':function(){
		return $('#quantity').val();
	},
	'getTotalPrice':function(){
		return $('#detailPrice').attr('usvalue');
	},
	'getShippingData':function(data){
		/**
		* 参数个数
		* data['country']  data['currency'] data['storageId'] data['totalPrice']
		* data['listingId'] data['qty'] data['language']
		*/
		var _this = this;
		var _reQeq = false;//是否需要重新请求
		data = data || {};
		data['country'] = data['country'] || _this.getCountry();
		data['currency'] = data['currency'] || _this.getCurrency();
		data['storageId'] = data['storageId'] || _this.getStorageId();
		data['totalPrice'] = data['totalPrice'] || _this.getTotalPrice();
		data['listingId'] = data['listingId'] || _this.getListingId();
		data['qty'] = data['qty'] || _this.getQty();
		data['language'] = data['language'] || _this.getLanguage();
		if(data['reRequest']){
			_reQeq = true;
		}else if(_this.lastReqData !== null){
			for(var key in data){
				if(data.hasOwnProperty(key)){
					if(data[key] != _this.lastReqData[key]){ _reQeq = true; break;}
				}
			}
		}
		
		if(_this.lastReqData === null){_reQeq = true;}

		if(_reQeq){
			var _domBox = _this.box.find('.newshopping_address');
			_this.box.find('.newshopping_address').hide();
			_this.lastReqData = data;
			$('.logistics_loading').show();
			$('.loading_defeated').hide();
			$('.logistics_info').hide();
			_this.box.find('.loading').show();
			_this.box.find('.failure').hide();
			$.ajax({
				'url':_this.reqUrl,
				'type':'POST',
				'data':data,
				'timeout':10000,
				'dataType':'JSON',
				'context':_this,
				'success':function(resData){
					var _this = this;
					_this.renderShipping(resData);//回调渲染界面
				},
				'error':function(){
					$('.loading_defeated').show();
					$('.logistics_loading').hide();
				}
			});
		}
	},
	'renderShipping':function(resData){
		var _this = this;
		if(resData.status != 'Y'){//请求失败
			$('.logistics').children('.shipping').html('Shipping:<b>Unavailable</b>');
			$('.logistics').children('.super_saver').html('Unavailable<i></i>');
			_this.box.find('.failure').html('Sorry, it seems that there are no available shipping methods for your location.Please <a target="_blank" href="' + _this.waiterUrl + ';">contact us</a>   for further assistance.').show();
			_this.box.find('.loading').hide();
		}else{//请求成功
			var _html = '';
			var _shippingTips = {'fee':'Unavailable', 'title':'Unavailable'};
			var _className = 'sel_b';
			var _data = resData['data'];
			var _price = '';
			var _default = null;
			var _priceClass = '';
			var _bpriceClass = '';
			var _rate = currencyRate[_this.getRealCurrency()];//汇率
			var _label = currencyLabel[_this.getRealCurrency()];//货币符号
			var _currentLabel = '';
			for(var i in _data){
				if(_data.hasOwnProperty(i)){
					if(_data[i]['isShow'] == false){ 
						_className = 'no_';
						_price = 'Unavailable';
						_priceClass = '';
						_currentLabel = '';
					}else{
						_priceClass = 'class="pricelab" usvalue="' + _data[i]['price'] + '"';
						_className = 'sel_';
						if(_default === null){
							_default = _data[i];
						}
						_currentLabel = _label;
						_price = switchPrice(_data[i]['price'], _rate);//汇率进行转换;
						
					}
					if(_data[i].type == 'SurfaceType'){
						_className += 'a';
					}else{
						_className += 'b';
					}
					_html += '<tr class="' + _className + '"><td>' + _data[i]['title'] + '</td><td>' + _data[i]['description'] + '</td><td><em></em></td><td ' + _priceClass + '>' + _currentLabel + _price + '</td></tr>';
				}
			}


			if(_default !== null){
				_bpriceClass = 'class="pricelab" usvalue="' + _default['price'] + '"';
				_price = switchPrice(_default['price'], _rate);
				if(_default['price'] == 0){
					_bpriceClass = '';
					_shippingTips['fee'] = 'Free';
				}else{
					_shippingTips['fee'] = _label + _price;
				}
				_shippingTips['title'] = _default['title'];
			}
			$('.logistics_info').children('.shipping').html('<b ' + _bpriceClass + '>' + _shippingTips['fee'] + '</b>');
			$('.logistics_info').children('.super_saver').html(_shippingTips['title'] + '<i></i>');
			if(!_html){
				_html = '<tr><td colspan="4">Sorry,this is no shipping available here.</td></tr>';
			}
			_this.box.find('.method_table').children('tbody').html(_html);
			_this.box.find('.newshopping_address').show();
		}
		$('.logistics_loading').hide();
		$('.logistics_info').show();
	},
	'bindEvent':function(){
		var _this = this;
		$('.country_item').click(function(){
			var _countryCode = $(this).children('span').attr('data');
			if(_countryCode != _this.country){//如果选择的国家发生的变化则重新获取数据
				_this.changeCountry(_countryCode);
				_this.getShippingData({});
			}
		});
		$('.super_saver').click(function(){
			fnDialogsBg();
			$('.logistics_c').addClass('dialogs_show');
		});

		$('.loading_defeated').click(function(){
			_this.getShippingData({'reRequest':true});
		});
	},
	'run':function(){
		var _this = this;
		_this.init();
		_this.bindEvent();
	}
};
TT_NS.shipping.run();
/***************************运费相关***********************/


//按钮两种状态
function btnError(){
	$(".addCart").html('<span class="btn btn-error"><i class="icon-prCart"> </i>'+strFun("TT_language_"+jsLanguage)["tomtop.product.addToCart"]+'</span>');
	$(".shippTime").text(strFun("TT_language_"+jsLanguage)["tomtop.common.outStock"]);
}
function btnCar(){
	$(".addCart").html('<a href="javascript:void(0)" id="add_cart" class="btn btn-orange"><i class="icon-prCart"> </i>'+strFun("TT_language_"+jsLanguage)["tomtop.product.addToCart"]+'</a>');
	$(".shippTime").text(strFun("TT_language_"+jsLanguage)["tomtop.common.inStock"]);
}

//产品页面头部左右点击
function selectUurl(){
		var numImg = $("#showCaseSmallPic").find(".productSmallmove");
		var _liIndex = numImg.children(".cpActive").index();
		var pImg = $("#smallClickUrl").children("li");
		pImg.removeClass("cpActive");
		pImg.eq(_liIndex).addClass("cpActive");
		var imgSrc = $("#smallClickUrl").find(".cpActive").children("a").attr('href');
		$("#zoom2").attr("src",imgSrc);
		$("#zoom2").attr("href",imgSrc);
		$("#zoom2").children("img").attr("src",imgSrc);
}

$(function()
{
	now = 0;
	var picLi = $(".browseLeft_clicks").parents(".scrollBox").siblings(".customerSmallmove").children("li");
	var bigPic = $(".browseLeft_clicks").siblings(".customer_popPicBox").find("img");
	$("#smallClickUrl").find("img").bind("click",function()
	{
		now=$(this).parents("li").index();
	})
	
	//点击弹出放大
	$(".hoverBig").children("li").click(function(a){
		selectUurl()
		var zoom2 = $("#zoom2");
		var procuctPOP = $("#procuctPOP");
		var bigBack = $("#bigBacks");
		var bigBackImg = bigBack.find("img")
		var zoomImg = zoom2.find("img");
		var proPOPChi = procuctPOP.children(".customer_popBox");
		zoom2.siblings(".cloud-img-loading").remove();
		procuctPOP.fadeIn();
		$('.cloud-zoom').CloudZoom();
		var W = $(window).width();
		var ws = proPOPChi.width();
		procuctPOP.find(".scrollBox").css({"width":ws-238});
		var scW = procuctPOP.find(".scrollBox").width();
		var scH = procuctPOP.find(".scrollBox").height();
		if(scW/scH>=1){
			bigBackImg.css({"height":"100%","width":"auto"})
		}else{
			bigBackImg.css({"width":"100%","height":"auto"})
		}
		var _thisH = zoomImg.height();
		if(scH>_thisH)
		{
			zoomImg.css({"top":"50%","margin-top":-_thisH/2})
		}else{
			zoomImg.css({"top":"0px","margin-top":"auto"})
		}
		if(W >=1900)
		{
			proPOPChi.css({"margin-left":-500})
		}else{
			proPOPChi.css({"margin-left":-ws/2})
		}
	//判断右边图片多少 高度
	
	var smallL = $("#smallClickUrl").children("li")
	if($("#smallClickUrl").height()<=smallL.length/3*(smallL.eq(0).height()+17)){
		$("#smallClickUrl").css({"overflow-y":"scroll","width":"215px"})
	}else{
		$("#smallClickUrl").css({"overflow-y":"hidden","width":"205px"})
	}
	})
	$(".black").click(function(){
		$(this).parents(".blockPopup_box").fadeOut();
	})
})


$(window).resize(function()
{
	var pu_popCon=$(".pu_popCon").height();
	
	$(".blockPopup_box").each(function()
	{
		if($(this).css("display")=="block"){
			var mH = $(this).find(".scrollWZ").height()+30;
			$(this).find(".scrollWZ").css({"margin-top":-mH/2})
		}
	})
		var W = $(window).width();
		var ws = $("#procuctPOP").children(".customer_popBox").width();
		$("#procuctPOP").find(".scrollBox").css({"width":ws-238});
		var scW = $("#procuctPOP").find(".scrollBox").width();
		var scH = $("#procuctPOP").find(".scrollBox").height();
		if(scW/scH>=1){
			$("#bigBacks").find("img").css({"height":"100%","width":"auto"})
		}else{
			$("#bigBacks").find("img").css({"width":"100%","height":"auto"})
		}
		var _thisH = $("#zoom2").find("img").height();
		if(scH>_thisH)
		{
			$("#zoom2").find("img").css({"top":"50%","margin-top":-_thisH/2})
		}else{
			$("#zoom2").find("img").css({"top":"0px","margin-top":"auto"})
		}
		if(W >=1900)
		{
			$("#procuctPOP").children(".customer_popBox").css({"margin-left":-500})
		}else{
			$("#procuctPOP").children(".customer_popBox").css({"margin-left":-ws/2})
		}
	//判断右边图片多少 高度
	
		var smallL = $("#smallClickUrl").children("li")
		if($("#smallClickUrl").height()<=smallL.length/3*(smallL.eq(0).height()+17)){
			$("#smallClickUrl").css({"overflow-y":"scroll","width":"215px"})
		}else{
			$("#smallClickUrl").css({"overflow-y":"hidden","width":"205px"})
		}
})
$(function(){try{moveBox($(".showCaseSmall_box"));}catch(e){};})
$(function(){try{moveBox($(".comboDealWarp"));}catch(e){};})
$(function(){try{moveBox($(".selectBoughtWarp"));}catch(e){};})
/*$(function(){
	var filter = $(".selectFilter").children("li");
	filter.click(function(){
		if($(this).hasClass("invalids")==false){
			$(this).siblings().removeClass("selectActive");
			$(this).addClass("selectActive");
		}
	})
})*/

//评论图片弹出
$(function(){
	var now = 0;
	$(".writePic li").click(function()
	{
		$(this).parents(".writePic").siblings(".writeAddPic").fadeIn();
		//指定图片显示
		var ind = $(this).index();
		var nextli = $(this).parent().next().find(".customer_bigPic li");
		var lengths= $(this).parents(".writePic").children("li").length;
		nextli.hide();
		nextli.eq(ind).show();
		var nd = $(this).parent().next().find(".nowIndex:eq(0)");
		nd.val(ind);
		now = ind;
		if(now==0){
			$(".AddPicLClick").hide();
			$(".AddPicRClick").show();
		}else if(now==lengths-1){
			$(".AddPicLClick").show();
			$(".AddPicRClick").hide();
		}else{
			$(".AddPicLClick").show();
			$(".AddPicRClick").show();
		}
		if(lengths==1){
			$(".AddPicLClick").hide();
			$(".AddPicRClick").hide();
		}
	})
	$(".AddPicRClick").click(function(){
		var pic = $(this).siblings(".customer_bigPic").children("li");
		now++;
		if(now>=pic.length-1){
			now=pic.length-1;
			$(this).hide();
		}
		pic.hide();
		pic.eq(now).fadeIn();
		$(".AddPicLClick").show();
	})
	$(".AddPicLClick").click(function(){
		var pic = $(this).siblings(".customer_bigPic").children("li");
		now--;
		if(now<=0){
			now=0;
			$(this).hide();
		}
		pic.hide();
		pic.eq(now).fadeIn();
		$(".AddPicRClick").show();
	})
		
})
//顶部滚动浮动
$(window).scroll(function() {
	//document.title=$(window).scrollTop()
	if ($(window).scrollTop() > 720) {
		$(".navFixedTop").slideDown(200);
	} else {
		$(".navFixedTop").slideUp(200);
	}
});
//弹框演示  likes
$(function(){
	$(".favoritesC").click(function(){
		if($("#logout").length==1){
			var Ri = $(this).children("i");
			var hartNum = parseInt($(".heartNumber").text());
			var listingId = $("#listingId").val();
			var TT_UUID = getCookie("TT_UUID");
			var data = {listingId:listingId ,TT_UUID:TT_UUID};
			$.post(domain+"index.php?r=details/default/ajaxcollect",data,function(result){
				if(result.status == 1){
					Ri.removeClass("icon-Favorites").addClass("icon-heartR");
					$(".heartNumber").siblings("i").removeClass("icon-hearts").addClass("icon-heartR");
					$(".heartNumber").html('<i class="iconLArr"> </i>'+(hartNum+1));
				}else{
					Dialog(function(){
						var con = $(this).parents(".pu_pop");
						con.fadeOut(function(){
							con.remove();
						});
					},strFun("TT_language_"+jsLanguage)["tomtop.common.tip"],result.msg);
				}
			},"json");
		}else{
			loginPop();
		}
	})
	$(".heartNumber").parents("a").click(function(){
		if($("#logout").length==1){
			var Ri = $(".favoritesC").children("i");
			var heartNum = parseInt($(".heartNumber").text());
			var listingId = $("#listingId").val();
			var TT_UUID = getCookie("TT_UUID");
			var data = {listingId:listingId ,TT_UUID:TT_UUID};
			$.post(domain+"index.php?r=details/default/ajaxcollect",data,function(result){
				if(result.status == 1){
					$(".heartNumber").html('<i class="iconLArr"> </i>'+(heartNum+1));
					Ri.removeClass("icon-Favorites").addClass("icon-heartR");
					$(".heartNumber").siblings("i").removeClass("icon-hearts").addClass("icon-heartR");
				}else{
					Dialog(function(){
						var con = $(this).parents(".pu_pop");
						con.fadeOut(function(){
							con.remove();
						});
					},strFun("TT_language_"+jsLanguage)["tomtop.common.tip"],result.msg);
				}
			},"json");
		}else{
			loginPop();
		}
		
	})
})
$(function(){
/////======================hart number==================================
	 function hartNumber(){
	 	 var listingId = $("#listingId").val();
		 $.ajax({
		    type: "GET",
			cache : false,
		    url: domain+"index.php?r=details/default/favorites&listingId="+listingId,
		    dataType:'json',
			 success: function (data){
			 	if(data.status == 1){
			 		$(".heartNumber").html('<i class="iconLArr"> </i>'+data.client);
			 	}
			 }
		 });
	 }
	 hartNumber();
/////======================hartR active==================================
	 function hartR_active(){
	 	 var listingId = $("#listingId").val();
		 $.ajax({
		    type: "GET",
			cache : false,
		    url: domain+"index.php?r=details/default/collect&listingId="+listingId,
		    dataType:'json',
			 success: function (data){
			 	if(data.status == 1){
			 		$(".heartNumber").siblings("i").removeClass("icon-hearts").addClass("icon-heartR");
			 		$(".favoritesC").children("i").removeClass("icon-Favorites").addClass("icon-heartR");
			 	}
			 }
		 });
	 }
	 hartR_active();
})
//Add to my dropship 异步请求
$(function(){
	$(".dropshipC").click(function(){
		var Ri = $(this).children("i");
		var sku = $("#productSku").val();
		var TT_UUID = getCookie("TT_UUID");
		var data = {sku:sku ,TT_UUID:TT_UUID};
		$.post(domain+"index.php?r=details/default/ajaxdropship",data,function(result){
			if(result.status == 1){
				$(this).addClass("active");
				successful();
			}else{
				Dialog(function(){
					var con = $(this).parents(".pu_pop");
					con.fadeOut(function(){
						con.remove();
					});
				},strFun("TT_language_"+jsLanguage)["tomtop.common.tip"],result.msg);
			}
		},"json");
	})
})
// Add to wholesale list 异步请求
$(function(){
	$(".wholesaleC").click(function(){
		var Ri = $(this).children("i");
		var sku = $("#productSku").val();
		var TT_UUID = getCookie("TT_UUID");
		var data = {sku:sku ,TT_UUID:TT_UUID};
		$.post(domain+"index.php?r=details/default/ajaxwsp",data,function(result){
			if(result.status == 1){
				successful();
			}else{
				Dialog(function(){
					var con = $(this).parents(".pu_pop");
					con.fadeOut(function(){
						con.remove();
					});
				},strFun("TT_language_"+jsLanguage)["tomtop.common.tip"],result.msg);
			}
		},"json");
	})
})


/**
 * @desc 折扣轮播 
 * @desc 首页
 */
function moveBanner(moveWarp)
{
	var interval=0;
	var now=0;
	var listWarp = moveWarp;
	var leftC = listWarp.find(".moveLeftClick");
	var rightC = listWarp.find(".moveRightClick");
	var banners = moveWarp.find(".moveList")
	var moveW = banners.outerWidth(true);
	var moveBox = moveWarp.find(".moveBox")
	var page_s = listWarp.siblings().find(".pages");       //总共多少页
	var page_number = banners.length;           //总共多少页 向上取整
	var page_n = listWarp.siblings().find(".page");        //当前第几页
	page_s.html(page_number);
	moveBox.css({"width":moveW*banners.length})
	
	if(page_number<=1){
		leftC.hide();
		rightC.hide();
	}
	
	function playBanner()
	{
	$("img.lazy").lazyload({event:'sporty'})
		clearInterval(interval);
		interval=setInterval(function()
		{
			now++
			if(now>=banners.length){now=0}
			moveBox.animate({
				left:-moveW*now
			}).find('img').trigger('sporty');
			page_n.html(now+1);
		},7000)
	};
	playBanner()
	listWarp.hover(function(){
		clearInterval(interval);
		},function(){playBanner()
	})
	rightC.click(function(){
	$("img.lazy").lazyload({event:'sporty'})
		if (!moveBox.is(":animated")) {
			now++
			if(now>=banners.length){
				now=0;
			}
				moveBox.animate({
					left:-moveW*now
				}).find('img').trigger('sporty');
			page_n.html(now+1);
		}
	})
	leftC.click(function(){
	$("img.lazy").lazyload({event:'sporty'})
		if (!moveBox.is(":animated")) {
			now--;
			if(now<0){
				now=banners.length-1;
			}
				moveBox.animate({
					left:-moveW*now
				}).find('img').trigger('sporty');
			page_n.html(now+1);
		}
	})
};

/////======================product Wholesale Inquiry 和 Price Alert 弹出框=================================
var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;

function inputs(){
	$("#Inquiry,#priceAlert").find(".txtInp,.textareas").each(function(){
		$(this).blur(function(){
			var val = $(this).val();
			var email = $("#Inquiry,#priceAlert").find(".email").val();
			if(val == ""){
				$(this).siblings(".help-inline").text(strFun("TT_language_"+jsLanguage)["tomtop.message.cannotBeEmpty"]);
				$(this).parents(".control-group").addClass("error");
			}else{
				$(this).siblings(".help-inline").text("");
				$(this).parents(".control-group").removeClass("error");
				$(this).parents(".control-group").addClass("success");
			}
			if(email != ""){
				isok= reg.test(email );
				if(!isok){
					$("#Inquiry,#priceAlert").find(".email").parents(".control-group").removeClass("success");
					$("#Inquiry,#priceAlert").find(".email").parents(".control-group").addClass("error");
					$("#Inquiry,#priceAlert").find(".email").siblings(".help-inline").text(strFun("TT_language_"+jsLanguage)["tomtop.message.wrongEmail"]);
				}
			}
		})
		$(this).focus(function(){
			$(this).siblings(".help-inline").text("");
			$(this).parents(".control-group").removeClass("error")
		})
		$("#targetPrice").keyup(function(){
			this.value=this.value.replace(/[^\d.]/g,'');
		})
	})
}

//将form转为AJAX提交
function ajaxSubmit(frm, fn) {
    var dataPara = getFormJson(frm);
    $.ajax({
        url: frm.action,
        type: frm.method,
        data: dataPara,
        success: fn
    });
}

//将form中的值转换为键值对。
function getFormJson(frm) {
    var o = {};
    var a = $(frm).serializeArray();
    $.each(a, function () {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });

    return o;
}

$(function(){
	$(".inquiry").click(function(){
		Submit(function(){
			var con = $(this).parents(".pu_pop");
			$("#Inquiry").find(".txtInp,.textareas").each(function(){
				valInq = $(this).val();
				if(valInq == ""){
					$(this).siblings(".help-inline").text(strFun("TT_language_"+jsLanguage)["tomtop.message.cannotBeEmpty"]);
					$(this).parents(".control-group").addClass("error")
				}
			})
			if(valInq != "" && $("#Inquiry").find(".error").length==0){
					jsonData = getFormJson($("#Inquiry"));
					$.ajax({
						type : 'post',
						url : domain+'index.php?r=details/default/ajaxwholesale',
						data : jsonData,
						success : function(result){
							if(result.status==1){
								successful();
								con.fadeOut(function(){
									con.remove();
								});
							}else{
								errorPop();
							}
						},
						dataType : 'json'
					});
				}
		},'Wholesale Inquiry','<form class="form-horizontal" id="Inquiry" method="post"><div class="control-group"><label class="control-label">'+strFun("TT_language_"+jsLanguage)["tomtop.product.name"]+':<span>*</span></label><div class="controls"><input name="name" class="txtInp" type="text"><span class="help-inline"></span></div></div><div class="control-group"><label class="control-label">'+strFun("TT_language_"+jsLanguage)["tomtop.product.mobilePhone"]+':<span>*</span></label><div class="controls"><input name="mobilePhone" class="txtInp" type="text"><span class="help-inline"></span></div></div><div class="control-group"><label class="control-label">'+strFun("TT_language_"+jsLanguage)["tomtop.product.emailAddress"]+':<span>*</span></label><div class="controls"><input name="emailAddress" class="txtInp email" type="text"><span class="help-inline"></span></div></div><div class="control-group"><label class="control-label">'+strFun("TT_language_"+jsLanguage)["tomtop.product.yourTargetPrice"]+':<span>*</span></label><div class="controls"><input id="targetPrice" name="targetPrice" class="txtInp" type="text"><span class="help-inline"></span></div></div><div class="control-group"><label class="control-label">'+strFun("TT_language_"+jsLanguage)["tomtop.product.orderQuantity"]+':<span>*</span></label><div class="controls"><input name="orderQuantity" class="txtInp" type="text"  onpaste="return false;" onkeypress="return IsNum(event)"><span class="help-inline"></span></div></div><div class="control-group"><label class="control-label">'+strFun("TT_language_"+jsLanguage)["tomtop.product.countryState"]+':<span>*</span></label><div class="controls"><input name="countryState" class="txtInp" type="text"><span class="help-inline"></span></div></div><div class="control-group"><label class="control-label">'+strFun("TT_language_"+jsLanguage)["tomtop.product.companyName"]+':<span>*</span></label><div class="controls"><input name="companyName" class="txtInp" type="text"><span class="help-inline"></span></div></div><div class="control-group"><label class="control-label">'+strFun("TT_language_"+jsLanguage)["tomtop.product.writeAnInquiry"]+':<span>*</span></label><div class="controls"><textarea name="writeInquiry" class="writeInquiry textareas"></textarea><span class="help-inline"></span></div></div><div class="control-group"><div class="controls inpMaxW">* '+strFun("TT_language_"+jsLanguage)["tomtop.product.pleaseLetUsKnow"]+' </div></div><input type="hidden" name="listingId" value="'+$("#listingId").val()+'"><input type="hidden" name="sku" value="'+$("#productSku").val()+'"></form>');
		$(".pu_popWarp").addClass("pu_popInf")
		inputs()
	})
})


/////======================产品描述ajax异步==================================
$(function(){
	 function description(){
		 if($("#descriptionImg").children().length==0){
			 $contentLoadTriggered = true;
			 var listId = $("#productSku").attr("value");
		 	 $("#descriptionImg").append("<div class='loading-ajax' />")
			 $.ajax({
			    type: "GET",
				cache : false,
			    url: domain+"index.php?r=details/default/ajaxdescimg",
			    data: "listing_id="+listId,
			    dataType:'html',
				 success: function (html) 
				 {
				   	 $("#descriptionImg").html(html)
					 eachImg($("#descriptionImg"))
					 $(".loading-ajax").remove();
					 $contentLoadTriggered = true;
				 }
			 });
		 }
	 }
	 $contentLoadTriggered = false;
	 if($("#descriptionImg").length == 1){
	 	 $(window).scroll(function(){
			 var scr = $("#descriptionImg").offset().top;
			 if(scr >= $(window).scrollTop()&& scr <($(window).scrollTop()+$(window).height()) && $contentLoadTriggered == false)
			 {
				 description()
			 }
		 });
		if($("#descriptionImg").offset().top-$("html,body").scrollTop()<$(window).height() && $contentLoadTriggered == false){
			description()
		}
	 }
	
})
/////======================Also Bought ajax异步==================================
$(function(){
	 function AlsoBought(){
		 if($("#AlsoBought").children().length==0){
			 $AlsoBoughtTriggered = true;
			 var listingId = $("#listingId").attr("value");
		 	 $("#AlsoBought").append("<div class='loading-ajax' />")
		 	 var urls = ajaxLang("index.php?r=details/default/ajaxalsobought");
			 $.ajax({
			    type: "GET",
				cache : false,
			    url: urls,
			    data: "listingId="+listingId,
			    dataType:'html',
				 success: function (html) 
				 {
				   	 $("#AlsoBought").html(html)
				   	 $("img.lazy").lazyload();
					 $(".loading-ajax").remove();
					 $(function(){try{moveBox($(".AlsoBought"));}catch(e){};})
					 $AlsoBoughtTriggered = true;
				 }
			 });
		 }
	 }
	 $AlsoBoughtTriggered = false;
	 if($("#AlsoBought").length == 1){
		 $(window).scroll(function(){
			 var scr = $("#AlsoBought").offset().top;
			 if(scr >= $(window).scrollTop()&& scr <($(window).scrollTop()+$(window).height()) && $AlsoBoughtTriggered == false)
			 {
				 AlsoBought()
			 }
		 });
		if($("#AlsoBought").offset().top-$("html,body").scrollTop()<$(window).height() && $AlsoBoughtTriggered == false){
			AlsoBought()
		}
	}
})

/////======================AlsoViewed ajax异步==================================
$(function(){
	 function AlsoViewed(){
		 if($("#AlsoViewed").children().length==0){
			 $AlsoViewedTriggered = true;
			 var listingId = $("#listingId").attr("value");
		 	 $("#AlsoViewed").append("<div class='loading-ajax' />")
		 	 var urls = ajaxLang("index.php?r=details/default/ajaxalsoviewed");
			 $.ajax({
			    type: "GET",
				cache : false,
			    url: urls,
			    data: "listingId="+listingId,
			    dataType:'html',
				 success: function (html) 
				 {
				   	 $("#AlsoViewed").html(html)
				   	 $("img.lazy").lazyload();
					 $(".loading-ajax").remove();
					 $(function(){try{moveBox($(".AlsoViewed"));}catch(e){};})
					 $AlsoViewedTriggered = true;
				 }
			 });
		 }
	 }
	 $AlsoViewedTriggered = false;
	 if($("#AlsoViewed").length == 1){
		 $(window).scroll(function(){
			 var scr = $("#AlsoViewed").offset().top;
			 if(scr >= $(window).scrollTop()&& scr <($(window).scrollTop()+$(window).height()) && $AlsoViewedTriggered == false)
			 {
				 AlsoViewed()
			 }
		 });
		if($("#AlsoViewed").offset().top-$("html,body").scrollTop()<$(window).height() && $AlsoViewedTriggered == false){
			AlsoViewed()
		}
	}
})

//////=========================视频加载====================================
window.onload = function () {
    resizeIframe();
}
var resizeIframe=function(){
    var bodyw=945;
    for(var ilength=0;ilength<=document.getElementsByTagName("iframe").length;ilength++){
		try{ document.getElementsByTagName("iframe")[ilength].height = bodyw*9/16;}catch(e){};
    }
}

/////======================实例化倒计时================================
// $(function(){
	// try{
		// var myDate = new Date();
		// var dates = myDate.toLocaleDateString();
		// $('.countdown').downCount({
			// date: dates+' 23:59:59'
		// });
	// }catch(e){}
// })
/////======================Deals==================================
$(function(){
	 function dayDeals(){
		 var urls = ajaxLang("index.php?r=site/daily");
		 $.ajax({
		    type: "GET",
			cache : false,
		    url: urls,
		    dataType:'html',
			 success: function (html) 
			 {
			   	 $(".dailyDeals").html(html)
			   	 $("img.lazy").lazyload();
			   	 moveBanner($(".dealsWarp"));
			   	 try{
					var dates = $("#serverTime").text();
					$('.countdown').downCount({
						date: dates+' 23:59:59'
					});
				}catch(e){}
			 }
		 });
	 }
	 dayDeals();
})
/////======================判断用户 显示相应按钮==================================
$(function(){
	if(getCookie("TT_UUID") && getCookie("TT_TOKEN")){
		$(".dropshipC,.wholesaleC").removeClass("hide");
	}else{
		$(".dropshipC,.wholesaleC").addClass("hide");
	}
})

/////======================presale==================================
$(function(){
	$('.progressPoint li').each(function(){
		var pointLast = $('.point2').data('last');
		var amount = $('.point3').data('now');
		var from=$(this).data('from');
		var to=$(this).data('to');
		$('.presaleT span').text('('+amount+' orders)')
		if(amount>=from&&amount<=to){
		   if($(this).index()==0){
		   		$(this).addClass('active');
				$(".presaleM").animate({
					width:amount/to*128
			  	})
			}else{
		   		$(this).prev('li').andSelf().addClass('active');
			   $(".presaleM").animate({
					width:128*($(this).index())+(amount-from)/(to-from)*128
			 	 })
			}
		}else if(amount>=pointLast)
		{
		   		$(this).prev('li').addClass('active');
			$(".presaleM").animate({
				width:330
			})
		}
	});
})

////////================写评论===========
//星星评分选择

$(function(){
	$(document).on("mouseover",".product_Reviews em",function(){
		var index = $(this).index()+1;
		$(this).parents(".product_Reviews").addClass("startH"+index);
	})
	$(document).on("mouseleave",".product_Reviews em",function(){
		var index = $(this).index()+1;
		$(this).parents(".product_Reviews").removeClass("startH"+index);
	})
	$(document).on("click",".product_Reviews em",function()
	{
		var index = $(this).index()+1;
		if ($(this).parents(".product_Reviews").attr("class").indexOf('start')>0){
			var length = $(this).parents(".product_Reviews").attr("class").indexOf('start');
			var start = $(this).parents(".product_Reviews").attr("class").substr(length,'start'.length+1);
			$(this).parents(".product_Reviews").removeClass(start);
		}
		$(this).parents(".product_Reviews").addClass("start"+index);
		$(this).parents(".product_Reviews").siblings("input").val(index);
		var priW = parseInt($("input[name='ipriceStarWidth']").val());
		var qutW = parseInt($("input[name='iqualityStarWidth']").val());
		var useW = parseInt($("input[name='iusefulness']").val());
		var shipW = parseInt($("input[name='ishippingStarWidth']").val());
		var allStartW = priW+qutW+useW+shipW;
		var percent = (allStartW/4)*20+"%";
		$("#foverallratingStarWidth").css({'width':percent});
	})
	
	/////===================验证email 上传评论======================///////
	$('#subWrite').click(function(){
		var email = $('.email').val();
		$('.emptys').each(function(){
			if($(this).val()==''){
				$(this).parents('.controls').addClass('error');
				$(this).siblings('.help-block').text(strFun("TT_language_"+jsLanguage)["tomtop.message.cannotBeEmpty"]);
			}
		})
		if(email != ""){
			isok= reg.test(email);
			if(!isok){
				$('.email').parents('.controls').addClass('error');
				$('.email').siblings('.help-block').text(strFun("TT_language_"+jsLanguage)["tomtop.message.wrongEmail"]);
			}
		}
	})
	$('.emptys').focus(function(){
		$('.emptys').parents('.controls').removeClass('error');
		$('.emptys').siblings('.help-block').text('');
	})

})

+(function($) {
/////////////////////////////////多属性切换/////////////////////////////////////////////
	filterAttr = {
		curr_list       : null,//过滤之后的plist
		/////////*****当前被点击的属性***///////////
		click_attr_name : 'whouse',
		click_attr_val  : 'CN',
		selected        : null,//当前选中的属性组合
		curr_sku        : null,//当前选中的sku
		attr_content    : null,//当前操作的商品属性容器
		activeClass     : 'selectActive',//选中样式
		forbidClass     : 'invalids',//不能被点击样式
		cpActClass      : 'cpActive',//产品小图选中样式
		init:function(){
			 $(".selectAttribute li").click(function(){
				attr_content = $(this).parents('.showCaseWarp');
				var thisBox = $(this).parents('li.productClass');
				var dataVal = $(this).attr('data-attr-value');
				var dataKey = $(this).parents('.selectAttribute').prev('.proColor').children('span').attr('data_key');
				if(!$(this).hasClass(filterAttr.forbidClass))
				{
					if($(this).hasClass(filterAttr.activeClass))
					{
						$(this).removeClass(filterAttr.activeClass);
					}
					else
					{
						$(this).addClass(filterAttr.activeClass).siblings('li').removeClass(filterAttr.activeClass);
						if(dataKey == 'color'){
							filterAttr.ImageSwitching(dataVal,attr_content);////////******切换图片******/////////
							thisBox.find('.cloud-zoom').CloudZoom();
						}
					}
					filterAttr.click_attr_name = dataKey;
					filterAttr.click_attr_val  = dataVal;
					filterAttr.closeAttr(thisBox);
				}
			})
			///////////*******鼠标经过换图片********//////////
			$(document).on("mouseover",".selectAttribute li",function(){
				if(!$(this).hasClass(filterAttr.forbidClass))
				{
				 	attr_content = $(this).parents('.showCaseWarp');
				 	var hoverImgs = attr_content.find(".hoverBig .cloud-zoom img");
					var thisPrev = $(this).parents('.selectAttribute').prev('.proColor');
					var dataKey  = thisPrev.children('span').attr('data_key');
					var color    = $(this).attr("data-attr-value");
					if(!$(this).hasClass(filterAttr.activeClass))
					{
						if(dataKey == 'color'){
							filterAttr.hoverImg(color,hoverImgs);
						}
					}
					thisPrev.children("b").text(color);
				}
			})
			$(document).on("mouseout",".selectAttribute li",function(){
				if($(this).siblings(".selectActive").length == 1){
			 		attr_content = $(this).parents('.showCaseWarp');
					var thisPrev = $(this).parents('.selectAttribute').prev('.proColor');
				 	var hoverImgs = attr_content.find(".hoverBig .cloud-zoom img");
					var color = $(this).siblings(".selectActive").attr("data-attr-value");
					var active = attr_content.find(".showCaseSmall_box .cpActive a").attr("href");
					thisPrev.children("b").text(color);
					hoverImgs.attr("src",active);
				}
			})
			//购买数量
			$(".qty_wrap .next").click(function(){
				var quantityContainer = $(this).parent().find(".quantity");
				var quantity=quantityContainer.val();
				quantity++;
				if(quantity>=999){
					quantity=999;
				}
				quantityContainer.val(quantity)
			})
			$(".qty_wrap .prev").click(function(){
				var quantityContainer = $(this).parent().find(".quantity");
				var quantity=quantityContainer.val();
				quantity--;
				if(quantity<=1){
					quantity=1;
				}
				quantityContainer.val(quantity)
			})
			$(".quantity").keyup(function(){
				var quantity = $(this).val();
				if(quantity==0){
					quantity=1;
				}
				$(this).val(quantity);
				this.value=this.value.replace(/[^0-9]/g,'');
			})
			//切换货币
			$(".currencyBox li").click(function(){
				$(this).parent(".currencyBox").hide();
				var currencyTxt = $(this).find("span").text();
				var currency = $(this).children("a").attr("data-currency");
				$('.currencyBox').prev().html("<span class='symbolLab'>"+currencyTxt+"</span><i class='icon-arr'> </i>")
				$(".pu_notranslate").siblings(".pu_navHover").html(currencyTxt+"<i class='icon-arr'> </i>");
				switchCurrency(currency)
			})
			$(".currency").hover(function(){
				$(this).find(".currencyBox").show();
			},function(){$(this).find(".currencyBox").hide();})
		},
		hoverImg:function(color,hoverImgs){//根据颜色属性找图片
			var imglist = filterAttr.extractImg(mainContent,color);
			var titleImg = filterAttr.extractTitleImg(imglist);
			$.each(imglist,function(key,value){
				hoverImgs.attr("src","");
				hoverImgs.attr("src",url500+titleImg);
				return false;
			})
		},
		extractImg:function(productMainCoteng,color){
			var mainC = productMainCoteng;
			var imglist = '';
			$.each(mainC,function(key,value){
				$.each(value.attributeMap,function(j,attributeValue){
					if(value.attributeMap.color == color){
						for(var i = 0, _len = value.imgList.length; i < _len; i++){
							if (value.imgList[i]['isSmall'] == true) {
								var _temp = value.imgList[0];
								value.imgList[0] = value.imgList[i];
								value.imgList[i] = _temp;
							}
						}
						imglist = value.imgList;
						return false;
					}
				});
			});
			if(imglist){
				return imglist;
			}
			return false;
		},
		extractTitleImg:function(imglist){
			/*
			 功能：循环提取相关颜色主图
			 参数：imglist为上面循环所得到的变量名
			 */
			var mainC = imglist;
			var imgUrl = '';
			$.each(mainC,function(key,value){
				if(value.isMain == true){
					imgUrl = value.imgUrl;
					return false;
				}
			});
			if(imgUrl){
				return imgUrl;
			}
			return false;
		},
		ImageSwitching:function(dataVal,attr_content){
			var html = '';
			var htmlPop = '';
			var imgHtml = '';
			var listPic = '';
			var hoverBig = attr_content.find(".hoverBig");
			var smallClick = $("#smallClickUrl");
			var smallPic = attr_content.find(".productSmallPic");
			var imglist = filterAttr.extractImg(mainContent,dataVal);
			var titleImg = filterAttr.extractTitleImg(imglist);
			html += '<ul class="productSmallmove lbUl moveBox">';
			$.each(imglist,function(key,value){
				html += '<li class="moveList">'
						+ '<a rel="useZoom: \'zoom1\', smallImage: \''+ url500  + value.imgUrl +'\'" class="cloud-zoom-gallery" href="'+ url2000 +value.imgUrl+'">'    
						+ '<img src="'+ url60 +value.imgUrl+'" class="zoom-tiny-image">'
						+ '</a>'
					 + '</li>';
				htmlPop += '<li class="lineBlock moveList">'
					+ '<a rel="useZoom: \'zoom2\', smallImage: \''+ url500 + value.imgUrl +'\'" class="cloud-zoom-click" href="'+ url2000 +value.imgUrl+'">'    
					+ '<img src="'+ url60 +value.imgUrl+'" class="zoom-tiny-image">'
					+ '</a>'
				 + '</li>';
				listPic += '<li class="moveList">'
					+ '<a rel="useZoom: \'zoom2\', smallImage: \''+ url500 + value.imgUrl +'\'" class="cloud-zoom-gallery" href="'+ url2000 +value.imgUrl+'">'    
					+ '<img src="'+ url60 +value.imgUrl+'" class="zoom-tiny-image">'
					+ '</a>'
				 + '</li>'
			});	
			html += '</ul>';
			
			bigUrl = '<a rel="adjustX:10, adjustY:-4" id="zoom1" class="cloud-zoom" href="'+ url2000 +titleImg+'" style="position: relative; display: inline-block;"><img src="'+ url500 +titleImg+'" style="display: inline-block;"></a><span class="empty"></span>'
			listbigUrl = '<a rel="adjustX:10, adjustY:-4" id="zoom2" class="cloud-zoom" href="'+ url2000 +titleImg+'" style="position: relative; display: inline-block;"><img src="'+ url500 +titleImg+'" style="display: inline-block;"></a><span class="empty"></span>'
			
			if($('.navFixedTop').length == 0){
				smallPic.html('<ul class="productSmallmove lbUl moveBox">'+listPic+'</ul>');
				hoverBig.find(".wrap").html(listbigUrl);
			}else{
				smallPic.html(html);
				hoverBig.find(".wrap").html(bigUrl);
			}
			smallClick.html(htmlPop);
			smallPic.find("li").eq(0).addClass(filterAttr.cpActClass);
			smallClick.find("li").eq(0).addClass(filterAttr.cpActClass);
			$(function(){try{moveBox($(".showCaseSmall_box"));}catch(e){};});
			$('.cloud-zoom-click,.cloud-zoom,.cloud-zoom-gallery').CloudZoom();
		},
		closeAttr:function(thisBox){
			//TT_NS.shipping.getShippingData({});
			filterAttr.filterSku(thisBox);
			var dd = attr_content.find('.selectAttribute');
			//循环每个属性组合
			for (var d = 0; d < dd.length; d++) 
			{
				var attr_name_btn  = dd.eq(d).prev('p.proColor').find('span').attr('data_key');
				var attr_value_btn = dd.eq(d).find('li');
				if(attr_name_btn != filterAttr.click_attr_name)
				{
					//计算出剩余的sku
					var skus = new Array();
					var del = 0;
					for (var u = 0; u < mainContent.length; u++) 
					{
						mainContent[u].isDel = 0;
						skus.push(mainContent[u]);
					}
					for (var i = 0; i < filterAttr.selected.length; i++) 
					{
						var selected_name   = filterAttr.selected[i].attr_name;
						var selected_value  = filterAttr.selected[i].attr_value;
						if (selected_name != attr_name_btn) 
						{
							for (var j in mainContent)
							{
								var is_sku_exist = false;
								var whouse1     = mainContent[j].whouse;
								var maps1   = mainContent[j].attributeMap;
								if (selected_name == 'whouse') 
								{
									for(var w in whouse1)
									{
										if(w == selected_value)
										{
											is_sku_exist = true;
										}
									}
								}
								else
								{
									for(var m in maps1)
									{
										if (selected_name == m && selected_value == maps1[m]) 
										{
											is_sku_exist = true;
										}
									}
								}
								if(!is_sku_exist)
								{
									skus[j].isDel = 1;
								}
							}
						}
					}
					//循环每个按钮
					for (var k = 0; k < attr_value_btn.length; k++) 
					{
						var btn = attr_value_btn.eq(k);
						filterAttr.forbidSelect(btn, skus);//break;
					}
				}
			}
			
			TT_NS.shipping.getShippingData({});
		},
		filterSku:function(thisBox){
			var n = 0;
			curr_list = null;
			curr_list = new Array();
			filterAttr.getAttrValue(thisBox);//alert('被选属性个数: '+selected.length);
			var dd = attr_content.find('.selectAttribute');
			if(dd.length>1){
				for (var j = 0; j < mainContent.length; j++) 
				{
					var flag = false;
					var sum = 0;//符合当前sku条件的属性值个数
					for (var q = 0; q < filterAttr.selected.length; q++) 
					{
						var selected_name  = filterAttr.selected[q].attr_name;
						var selected_value = filterAttr.selected[q].attr_value;//alert(selected_name+' : '+selected_value);
						if (selected_name == 'whouse') 
						{
							var whouse = mainContent[j].whouse;
							for(var i in whouse)
							{
								if (i == selected_value) 
								{
								   sum++;break;
								}
							}
						}
						else
						{
							var attributeMap = mainContent[j].attributeMap;
							for(var i in attributeMap)
							{
								if(i == selected_name && attributeMap[i] == selected_value)//循环每一个产品 看里面是否有选择的全部属性
								{
									sum++;break;
								}
							}
						}
					}
					if (sum == q) //把匹配的留下
					{
						n++;//确定sku 看是否唯一  如果n=1则确定
						curr_list.push(mainContent[j]);
					}
				}
			}else{
				var attr_value = dd.find('li.selectActive').attr('data-attr-value');
				for (var j in mainContent)
				{
					var whouse1 = mainContent[j].whouse;
					for(var w in whouse1)
					{
						if(whouse1[attr_value]){
							curr_list.push(mainContent[j]);
							curr_sku = curr_list[0];
							filterAttr.showCurrSku();
							$('#p_sku_s').text(' ( Item#: '+curr_sku.sku+' )');
							if($("#wearhouseList").find('.selectActive').length == 1){
								$(".addCart a").click(function(){
									var _this = $(this);
									filterAttr.addToCart(_this);
								});
							}else{
								filterAttr.errorPop();
							}
							return;
						}
					}
				}
			}
			//如果sku被确定，获取clistingid
			if(n == 1) 
			{
				curr_sku = curr_list[0];
				filterAttr.showCurrSku();
				$('#p_sku_s').text(' ( Item#: '+curr_sku.sku+' )');
				if($("#wearhouseList").find('.selectActive').length == 1){
					$(".addCart a").click(function(){
						var _this = $(this);
						filterAttr.addToCart(_this);
					});
				}else{
					filterAttr.errorPop();
				}
			}
			else
			{
				var listidDom = attr_content.find('input[name="listingId"]');
				if(!listidDom.hasClass('no-maps-cannot-change'))//有些产品只有仓库没有maps
				{
					$('input[name="listingId"]').val('');
				}
				btnCar();
				$(".addCart a").click(function(){
					filterAttr.errorPop();
				});
			}
		},
		getAttrValue:function(thisBox){
			attr_content = $(document).find('.showCaseWarp');
			if(attr_content.size()>1)
			{
				//attr_content = attr_content.eq(attr_content.size()-1);
				attr_content = thisBox.find('.showCaseWarp');
			}
			var attrList = attr_content.find('.selectAttribute');
			var attr = {};
			var attrValue = new Array();
			for (var i = 0; i < attrList.length; i++) 
			{
				var attr_name  = attrList.eq(i).prev('p.proColor').find('span').attr('data_key');
				var attr_value = attrList.eq(i).find('.selectActive').attr('data-attr-value');
				if(attr_value){
					if(attr_value.indexOf("<")>0){
						attr_value= attr_value.replace(/</ig,"&lt;");
					}
					if(attr_value.indexOf(">")>0){
						attr_value= attr_value.replace(/>/ig,"&gt;");
					}
					if(attr_value.indexOf("\"")>0){
						attr_value= attr_value.replace(/\"/ig,"&quot;");
					}
				}
				if(attr_value != undefined) //至少选中一个
				{
					attr = {'attr_name':attr_name, 'attr_value': attr_value};
					attrValue.push(attr);
				}

				//仓库
				if(attr_name == 'whouse')
				{
					var v = attrList.eq(i).find('li.selectActive');
					var attr_value_id  = v.attr('data-attr-id');
					var attr_value_val = v.attr('data-attr-value');
					$('input[name="storageid"]').val(attr_value_id);
					$('input[name="whouse"]').val(attr_value_val);
				}
			}
			filterAttr.selected = attrValue;
		},
		showCurrSku:function(){
			if (curr_sku == null) 
			{
				for (var j = 0; j < mainContent.length; j++) 
				{
					if(curr_listingId == mainContent[j]['listingId'])
					{
						curr_sku = mainContent[j];break;
					}
				}
			}
			var curr_listingId  = curr_sku.listingId;
			var defaultImg_url  = curr_sku.defaultImg_url;
			var title           = curr_sku.title;
			var pro_url         = curr_sku.pro_url;
			attr_content.find('input[name="listingId"]').val(curr_listingId);
			//根据仓库显示价钱和库存
			var w = attr_content.find('input[name="whouse"]').val();
			if (w)
			{
				var sku_whouse      = curr_sku.whouse[w];
				var nowprice        = sku_whouse.nowprice;
				var origprice       = sku_whouse.origprice;
				var us_nowprices     = sku_whouse.us_nowprice;
				var us_origprices    = sku_whouse.us_origprice;
				var stock           = sku_whouse.qty;
				///////****获取当前币种******/////////
				var currency  = getCookie('TT_CURR');
				var label     = currencyLabel[currency];
				var rate      = currencyRate[currency];
				///////****计算价格******/////////
				var origprices   = switchPrice(us_origprices,rate);
				var detailPrices = switchPrice(us_nowprices,rate);
				attr_content.find('.d_origprice').attr('usvalue', us_origprices);
				attr_content.find('.detailPrice').attr('usvalue', us_nowprices);
				attr_content.find('.d_origprice').text(origprices);
				attr_content.find('.detailPrice').text(detailPrices);
				//////////********是否有折扣********///////////////
				var saleDate = sku_whouse.saleEndDate;
				var saleWarp = attr_content.find('.saleWarp');
				var off = 100-Math.round(us_nowprices/us_origprices*100);
				if(saleDate == ''){
					saleWarp.html('');
				}else{
					var dates = $("#serverTime").text();
					if($('.categoryWarpRight').length == 1){
						saleWarp.html('<p class="lineBlock">Regular Price: <span usvalue="'+us_origprices+'" class="pricelab" id="d_origprice">'+label + origprices+'</span></p><p class="lineBlock saleOff">'+off+'% OFF</p><div class="saleDown"><i class="icon-clock lineBlock"> </i><div class="dealsTime lineBlock countdown"><span class="lineBlock hours">00</span>:<span class="lineBlock minutes">00</span>:<span class="lineBlock seconds">00</span></div></div>');
					}else{
						saleWarp.html('<p class="lineBlock">Regular Price: <span usvalue="'+us_origprices+'" class="pricelab" id="d_origprice">'+label + origprices+'</span></p><p class="lineBlock saleOff">('+off+'% OFF)</p><i class="icon-clock lineBlock"> </i><p class="lineBlock">Sale Ends in</p><div class="dealsTime lineBlock countdown"><span class="lineBlock hours">00</span>:<span class="lineBlock minutes">00</span>:<span class="lineBlock seconds">00</span></div>');
					}
					$('.countdown').downCount({
						date: dates+' 23:59:59'
					});
				}
				////////////*******判断数量 和 状态 是否停售********///////////////
				if(sku_whouse.status !=1 || sku_whouse.qty < 1){
					btnError();
				}else{
					btnCar();
					//filterAttr.addToCart();
				}
				//////////********是否免邮*******///////////////
				/*if(sku_whouse.freeShipping == true){
					attr_content.find('.freeShipping').show();
				}else{
					attr_content.find('.freeShipping').hide();
				}*/
			}
		},
		forbidSelect:function(btn, skus){
			var btn_name  = btn.parents('.selectAttribute').prev('.proColor').find('span').attr('data_key');
			var btn_value = btn.attr('data-attr-value');
			if(btn_value.indexOf("<")>0){
				btn_value= btn_value.replace(/</ig,"&lt;");
			}
			if(btn_value.indexOf(">")>0){
				btn_value= btn_value.replace(/>/ig,"&gt;");
			}
			if(btn_value.indexOf("\"")>0){
				btn_value= btn_value.replace(/\"/ig,"&quot;");
			}
			//循环剩余的sku，看有没有这个按钮
			var flag = false;
			for (var k in skus) 
			{
				if(skus[k].isDel == 0)
				{
					var whouse2 = skus[k].whouse;
					var maps2   = skus[k].attributeMap;
					if (btn_name == 'whouse') 
					{
						for(var w2 in whouse2)
						{
							if(w2 == btn_value)
							{
								flag = true;//break;
							}
						}
					}
					else
					{
						for(var m2 in maps2)
						{
							if(m2 == btn_name && maps2[m2] == btn_value)
							{
								flag = true;//break;
							}
						}
					}                
				}
			}
			if(!flag)
			{
				btn.addClass(filterAttr.forbidClass).removeClass(filterAttr.activeClass);
			}
			else
			{
				if(btn.hasClass(filterAttr.forbidClass))
				{
					btn.removeClass(filterAttr.forbidClass);
				}
			}
		},
		addToCart:function(_this){
			if($('.navFixedTop').length==1){
				var box = $('.showCaseWarp');
			}else{
				var box = _this.parents('.showCaseWarp');
			}
			_this.after('<a href="javascript:void(0)" class="btn btn-orange unBind"><i class="icon-prCart"> </i>'+strFun("TT_language_"+jsLanguage)["tomtop.product.addToCart"]+'</a>');
			_this.hide();
			$(".unBind").unbind("click");
			setTimeout('$(".addCart .btn,.navFixedTop .contentInside .btn").show();$(".unBind").remove()',3000);
			filterAttr.bindClick(box);
		},
		bindClick:function(box){
			var TT_warehouse = box.find("input[name=storageid]").val();
			var listingId = box.find("input[name=listingId]").val();
			var quantity = box.find("input[name=quantity]").val();
			var data = {listingId:listingId,quantity:quantity,TT_warehouse:TT_warehouse};
			var cookList = getCookie("plist");
			$.post(domain+"index.php?r=cart/default/addtocart",data,function(result){
				if(result.status == 1){
					$.post(domain+"index.php?r=cart/default/cartnum",data,function(result){
						if(cookList!=null && result.status==1){
				   			var num = $("#cartNumber").children(".icon-cart");
				   			num.text(result.cartNumber)
						}else{
							var num = $("#cartNumber").children(".icon-cart");
					   		num.text(1)
						}
					},"json");
					window.location.href="http://cart.tomtop.com";
				}
			},"json");
		},
		errorPop:function(){
			Dialog(function(){
				var con = $(this).parents(".pu_pop");
				con.fadeOut(function(){
					con.remove();
				});
			},strFun("TT_language_"+jsLanguage)["tomtop.common.tip"],'Please select a corresponding properties!');
		}
	}
/////////////////////////////////加入购物车/////////////////////////////////////////////
	/*addToCart = {
		init:function(){
			$(".addCart a").click(function(e){
				if($('.navFixedTop').length==1){
					var box = $('.showCaseWarp');
				}else{
					var box = $(this).parents('.showCaseWarp');
				}
				if(box.find('.selectAttribute').length == box.find('.selectActive').length){
					$(this).after('<a href="javascript:void(0)" class="btn btn-orange unBind"><i class="icon-prCart"> </i>'+strFun("TT_language_"+jsLanguage)["tomtop.product.addToCart"]+'</a>');
					$(this).hide();
					$(".unBind").unbind("click");
					setTimeout('$(".addCart .btn,.navFixedTop .contentInside .btn").show();$(".unBind").remove()',3000);
					addToCart.bindClick(box);
					e.stopPropagation();
				}else{
					Dialog(function(){
						var con = $(this).parents(".pu_pop");
						con.fadeOut(function(){
							con.remove();
						});
					},strFun("TT_language_"+jsLanguage)["tomtop.common.tip"],'Please select a corresponding properties!');
				}
			});
		},
		bindClick:function(box){
			var TT_warehouse = box.find("input[name=storageid]").val();
			var listingId = box.find("input[name=listingId]").val();
			var quantity = box.find("input[name=quantity]").val();
			var data = {listingId:listingId,quantity:quantity,TT_warehouse:TT_warehouse};
			var cookList = getCookie("plist");
			$.post(domain+"index.php?r=cart/default/addtocart",data,function(result){
				if(result.status == 1){
					$.post(domain+"index.php?r=cart/default/cartnum",data,function(result){
						if(cookList!=null && result.status==1){
				   			var num = $("#cartNumber").children(".icon-cart");
				   			num.text(result.cartNumber)
						}else{
							var num = $("#cartNumber").children(".icon-cart");
					   		num.text(1)
						}
					},"json");
					window.location.href="http://cart.tomtop.com";
				}
			},"json");
		}
	}*/
	if($('.showCaseWarp').length == 1){
		//new filterAttr.addToCart();
		new filterAttr.init();
		new filterAttr.closeAttr();
	}
		
})(jQuery);
