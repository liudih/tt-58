
$(function(){
	//左边类目选择
	$(".dirSelectList").click(function(){
		$(this).toggleClass("selectOrange")
		$(this).children(".multi-select").toggleClass("multiAci")
	})
	//banner下面的导航 选择显示方式
	$(".icon-showBlock").click(function(){
		$(this).addClass("active");
		$(".icon-showList").removeClass("active");
		//产品显示方式切换
		$(".categoryProductList").addClass("categoryProductBlock");
	})
	$(".icon-showList").click(function(){
		$(this).addClass("active");
		$(".icon-showBlock").removeClass("active");
		//产品显示方式切换
		$(".categoryProductList").removeClass("categoryProductBlock");
	})
	//select  传值
	$(".selectWarp").click(function(e){
		$(this).toggleClass("active")
		if($(this).hasClass("active")){
			$(this).children(".selectValue").show();
		}else{
			$(this).children(".selectValue").hide();
		}
		e.stopPropagation();
	})
	$(".selectValue li").click(function(){
		var selectV = $(this).text();
		var urlV = $(this).attr("data-rel");
		var url = window.location.href;
		$(".selectTxt").text(selectV);
		
		if(url.indexOf("&p=") != -1||url.indexOf("?p=") != -1){
			if(url.indexOf("?")== -1){
				var sort = url+"?sort="+urlV;
				var page = changeURLArg(sort,'p',1);
				window.location = page;
			}else if(url.indexOf("?") != -1 && url.indexOf("?sort") == -1 && url.indexOf("&sort=")== -1){
				var sort = url+"&sort="+urlV;
				var page = changeURLArg(sort,'p',1);
				window.location = page;
			}else{
				var sort = changeURLArg(url,'sort',urlV);
				var page = changeURLArg(sort,'p',1);
				window.location = page;
			}
		}else{
			if(url.indexOf("?")== -1){
				window.location = url+"?sort="+urlV;
			}else if(url.indexOf("?") != -1 && url.indexOf("?sort") == -1 && url.indexOf("&sort=")== -1){
				window.location = url+"&sort="+urlV;
			}else{
				window.location = changeURLArg(url,'sort',urlV);
			}
		}
	})
	var seleTxt = $(".selectValue").children(".active").text();
	$(".selectTxt").text(seleTxt);
	$("body").click(function(e){
		$(".selectValue").hide();
		$(".selectWarp ").removeClass("active");
		e.stopPropagation()
	})
	//=======================选择仓库========================
	
	var jsonCook = [];
	var listid = [];
	$('.warehouseList span').click(function(){
		var currency = getCookie('TT_CURR');
		var label = currencyLabel[currency];
		var rate = currencyRate[currency];
		var data = parseJSON($(this).attr('data'));
		var box = $(this).parents('.productClass');
		var origprice = box.find('.productCost');
		var nowprice = box.find('.productPrice');
		var origAmount = $(this).attr('us-origprice');
		var nowAmount = $(this).attr('us-nowprice');
		var listingId =box.find('.likes').attr('data-rel');
		$.each(data,function(key,val){
			depotName = data.depotName;
			freeShipping = data.freeShipping;
		})
		if(getCookie('TT_WAREHOUSE')){
			var wareCook = getCookie('TT_WAREHOUSE');
			var tt_ware = parseJSON(wareCook);
			var diffListId = [];
			var sameListId = [];
			var jsonCooks = [];
			$.each(tt_ware,function(waKey,waVal){
				$.each(waVal,function(key,val){
					listid.push(waVal['listingId']);
					if(waVal[key] == listingId){
						sameListId = {listingId:listingId,depotName:depotName};
						jsonCooks.push(JSON.stringify(sameListId));
						return false;
					}else{
						diffListId = {listingId:waVal['listingId'],depotName:waVal['depotName']};
						jsonCooks.push(JSON.stringify(diffListId));
						return false;
					}
				})
			})
			if($.inArray(listingId, listid) == -1){
				jsonCook.push(JSON.stringify({"listingId":listingId,"depotName":depotName}));
			}
		}else{
			jsonCook = [];
			jsonCook.push(JSON.stringify({"listingId":listingId,"depotName":depotName})); 
		}
		
		if($.inArray(listingId, listid) != -1 && getCookie('TT_WAREHOUSE')){
			delCookie("TT_WAREHOUSE");
			temCookie("TT_WAREHOUSE",jsonCooks);
		}else{
			temCookie("TT_WAREHOUSE",jsonCook);
		}
		
		
		if(freeShipping == true){
			var boxs = $(this).parents('.productClass');
			boxs.find('.freeShipping').show();
		}else{
			var boxs = $(this).parents('.productClass');
			boxs.find('.freeShipping').hide();
		}
		
		origprice.text(label+switchPrice(origAmount,rate));
		nowprice.text(label+switchPrice(nowAmount,rate));
		origprice.attr('usvalue',origAmount);
		nowprice.attr('usvalue',nowAmount);
		if(origAmount == nowAmount){
			var boxs = $(this).parents('.productClass');
			boxs.find('.productCost').hide();
			boxs.find('.icon-sale').hide();
		}else{
			var boxs = $(this).parents('.productClass');
			var off = 100-Math.round(nowAmount/origAmount*100);
			boxs.find('.productCost').show();
			boxs.find('.icon-sale').show().text(off);
		}
		$(this).siblings().removeClass('active');
		$(this).addClass('active');
	})
})
//=======================初始化 类目滚动条 现在改为了点击=================================

$(function(){
	$(".dirToggle").each(function(){
		var scrollBoxH = $(this).height();
		if(scrollBoxH>131){
			$(this).css({"height":"130px"})
			$(this).after("<a class='scrMore' href='javascript:;'>"+strFun('TT_language_'+jsLanguage)['tomtop.common.viewMore']+"<i class='icon-GrLtArr'></i></a>")
		}
	})
	$(".scrMore").click(function(){
		var _this = $(this).prev(".dirToggle").children("a")
		var moveH = _this.length*_this.outerHeight(true);
		if($(this).hasClass("scrLess")==false){
			$(this).prev(".dirToggle").animate({"height":moveH})
			$(this).html("View Less<i class='icon-jArr'></i>")
		}else{
			$(this).prev(".dirToggle").animate({"height":130})
			$(this).html("View More<i class='icon-GrLtArr'></i>")
		}
		$(this).toggleClass("scrLess")
	})
	//////////////*****单选类目******//////////
	$('.radioA').click(function(){
		$(this).addClass('radioSelect');
		$(this).siblings().removeClass('radioSelect');
	})
})

//  likes
$(function(){
	$(".likes").click(function(){
		if($("#logout").length==1){
			var hartNum = $(this).children(".addHeartNum");
			var listingId = $(this).attr('data-rel');
			var TT_UUID = getCookie("TT_UUID");
			var data = {listingId:listingId ,TT_UUID:TT_UUID};
			$.post(domain+"index.php?r=details/default/ajaxcollect",data,function(result){
				if(result.status == 1){
					hartNum.text(parseInt(hartNum.text())+1);
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

////============================track order================================











