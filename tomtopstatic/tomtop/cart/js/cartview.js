$(function(){
	$("#ns_loading_box").show();
	$('.nw_btn_paypal').click(function(){
		var url = $(this).attr('url');
		skippay(url,false);
	});
	$('.nw_btn_place').click(function(){
		var url = $(this).attr('url');
		skippay(url,true);
	});
	
	//save for later显示
	var cartsize = $("#cartsize").val();
	var latesize = $("#latesize").val();
	if(cartsize!="" && parseInt(cartsize)==0 && latesize!="" && parseInt(latesize)>0){
		$(".warehouse_later_list .myshop_wares").show();
	}
	
	//设置默认仓库
	var thestorageid = $("#thestorageid").val();
	var ishave = false;
	if(thestorageid!=null && thestorageid!="" && !isNaN(thestorageid)){
		var t0 = parseInt(thestorageid);
		$("#cartlist_ul .warehouse_sel").each(function(i,e){
			var t = parseInt($(this).attr("storageid"));
			if(t==t0){
				$(this).addClass("sel");
				setStorageid(t);
				ishave = true;
				return false;
			}
		});
	}
	if(ishave==false){
		var sl = $("#cartlist_ul .storage_products>.sel");
		if(sl.length==0){
			$("#cartlist_ul li:eq(0)").find(".warehouse_sel").addClass("sel");
			var storageid=$("#cartlist_ul li:eq(0)").find(".warehouse_sel").attr("storageid");
			if(storageid==null || storageid==undefined || isNaN(storageid)){
				storageid = "";
			}
			setStorageid(storageid);
		}else{
			var sstid = sl.attr("storageid");
			setStorageid(sstid);
		}
	}
	 
	// 设置drop shipping order
	//所在仓库没有drop就去掉勾
	var isExistDrop = false;
	$("#cartlist_ul .storage_products>.sel").parent().find(".csku").each(function(i, e) {
		if ($(this).html() == "X01") {
			isExistDrop = true;
			return false;
		}
	});
	if(!isExistDrop){
		$(".nw_drop_order").removeClass("sel");
	}else{
		$(".nw_drop_order").addClass("sel");
	}
	
	//设置所选仓库的金额
	var chooseStorage = $("#cartlist_ul .storage_products>.sel").parent();
	if(chooseStorage!=null){
		getStorageTotal(chooseStorage);
	}
	
	//jpy日元判断
	var currencycode = $("#currencycode").val();
	if(currencycode=="JPY"){
		var dis_price = parseInt($("#discount_total").html());
		$("#discount_total").html(dis_price);
	}
	
	setTimeout(function () { 
	    $("#ns_loading_box").hide();
    }, 500);
})

//选择仓库
$("#cartlist_ul .warehouse_sel").click(function(){
	if($(this).hasClass("sel")){
		return;
	}
	$("#ns_loading_box").show();
	
	var pnode= $(this).parent();
	//是否存在drop，否则就去掉勾
	var isExistDrop = false;
	pnode.find(".csku").each(function(i, e) {
		if ($(this).html() == "X01") {
			isExistDrop = true;
			return false;
		}
	});
	if(!isExistDrop){
		$(".nw_drop_order").removeClass("sel");
	}else{
		$(".nw_drop_order").addClass("sel");
	}
	updatePrice($(this));
	
	setTimeout(function () { 
	    $("#ns_loading_box").hide();
    }, 500);
});

//更新价格面板
function updatePrice(selectnode){
	var pnode = selectnode.parent();
	getStorageTotal(pnode);
	var storageid=selectnode.filter(".warehouse_sel").attr("storageid");
	if(storageid){
		setStorageid(storageid);
	}
	//重置优惠卷
	$("#checkout_coupon_div").hide();
  	$("#checkout_coupon_canel").hide();
  	$("#checkout_coupon_apply").show();
  	$("#checkout_coupon_code").html("Coupon Code");
  	
  	$("#checkout_promo_div").show();
	$("#checkout_promo_apply").show();
  	$("#checkout_promo_cancel").hide();
  	$("#checkout_promo_input").val("").removeAttr("disabled");
  	
  	$("#checkout_point_div").hide();
	$("#checkout_point_apply").show();
   	$("#checkout_point_cancel").hide();
   	$("#checkout_point_input").val("").removeAttr("disabled");
   	$("#checkout_point_div .points_num").show();
   	
   	getCurrentPrefer();
}

//获取一个仓库的价格
function getStorageTotal(pnode){
	var price = 0;
	pnode.find(".theprice").each(function(i,e){
		var thep = $(this).val();
		thep = thep.replace(/[,]/g,"");
		price += parseFloat(thep);
	});
	var currency = $("#currencycode").val();
	var isJPY = 'JPY' == currency;
	price = isJPY ? Math.floor(price) : price.toFixed(2);
	var defaultDiscount = isJPY ? "0" : "0.00";
	$("#subtotal").html(price);
	$("#grandTotal").html(price);
	$("#discount_total").html(defaultDiscount);
	
}

//刷新页面
function reloadPage(){
//	var stid = $("#cartlist_ul .storage_products>.sel").attr("storageid");
//	if(!stid){
//		stid = 1;
//	}
	location.reload();
}

function additem(cid){
	var list = [];
	var map = {};
	map['clistingid'] = cid;
	map['qty'] = 1;
	map['storageid'] = 1;
	list[0] = map;
	var dd = $.toJSON(list);
	var url = "/savecartitem";
	$.ajax({
		url: url,
		type: "POST",
		dataType: "json",
		data: {"data": dd},
		success: function(data){
			if(data.result=="success"){
				reloadPage();
			}else if(data.result=="no-enough"){
				pophtml("Error","Out of stock!");
			}else if(data.result=="sold-out"){
				pophtml("Error","Sold out!");
			}else {
				pophtml("Error","add error!");
			}
		}
	});
}

//删除购物车
function delitem(node){
	//confirmHtml("Confirm","Remove from your cart?",function(){
		var pnode = $(node).closest(".itemline");
		var sid = pnode.find(".storageid:eq(0)").val();
		sid = isNaN(sid) ? 1 : sid;
		var list = [];
		pnode.find(".clistingid").each(function(i,e){
			var map = {};
			map['clistingid'] = $(this).val();
			map['storageid'] = sid;
			list[list.length] = map;
		});
		var dd = $.toJSON(list);
		var url = "/delcartitem";
		$.ajax({
			url: url,
			type: "POST",
			dataType: "json",
			data: {"data": dd},
			success: function(data){
				if(data.result=="success"){
					reloadPage();
				}
			}
		});
	//});
}

//弹出删除购物车
function popDel(node){
	$(node).next().toggle();
}
//隐藏删除框
function hideDel(node){
	$(node).closest(".toggleDel").hide();
}


function updateItem(type,node){
	var num = 1;
	if(type=="sub"){
		num = parseInt($(node).next().find("input:eq(0)").val());
		if (num > 1) {
			num = num - 1;
		}else{
			return;
		}
		$(node).next().find("input:eq(0)").val(num);
	}else if(type=="add"){
		num = parseInt($(node).prev().find("input:eq(0)").val());
		if (num < 999) {
			num = num + 1;
		}else{
			return;
		}
		$(node).prev().find("input:eq(0)").val(num);
	}
	updateQtyCommon(node,num);
}

//失去焦点的数量判断
$(".input_num").blur(function() {
	var n = this.value;
	var oldnum = $(this).closest(".itemline").find(".iqty:eq(0)").val();
	if (isNaN(n) || n < 1 || n > 999) {
		n = 1;
	}
	if(parseInt(n)==parseInt(oldnum)){
		this.value = n;
		return ;
	}
	this.value = n;
	updateQtyCommon(this,n);
});

// 按下Enter键
$(".input_num").keydown(function(event) {
	if (event.keyCode == 13) {
		var n = this.value;
		if (isNaN(n) || n < 1 || n > 999) {
			n = 1;
		}
		this.value = n;
		updateQtyCommon(this,n);
	}
});

//更新数量
function updateQtyCommon(node, num){
	$("#ns_loading_box").show();
	var pnode = $(node).closest(".itemline");
	var sid = pnode.find(".storageid:eq(0)").val();
	sid = isNaN(sid) ? 1 : sid;
	var list = [];
	pnode.find(".clistingid").each(function(i,e){
		var map = {};
		map['clistingid'] = $(this).val();
		map['storageid'] = sid;
		map['qty'] = num;
		list[list.length] = map;
	});
	var dd = $.toJSON(list);
	$.ajax({
		url: "/updatecartitem",
		type: "POST",
		dataType: "json",
		data: {"data": dd},
		success: function(data){
			if(data.result=="success"){
				var item = data.cartItem;
				if(item){
					var baseprice = item.price.unitBasePrice;
					var nowprice = item.price.unitPrice;
					var qty = item.price.spec.qty;
					var dis = (baseprice-nowprice)*qty;
					var currencycode = $("#currencycode").val();
					pnode.find(".showTotal:eq(0)").html(item.price.priceStr);
					if(currencycode!="JPY"){
						pnode.find(".theprice").val(item.price.price.toFixed(2));
						pnode.find(".showDiscountTotal:eq(0)").html(dis.toFixed(2));
					}else{
						pnode.find(".theprice").val(Math.floor(item.price.price));
						pnode.find(".showDiscountTotal:eq(0)").html(Math.floor(dis));
					}
					var selectnode = pnode.closest(".storage_products").find(".warehouse_sel:eq(0)");
					if(selectnode.hasClass("sel")){
						updatePrice(selectnode);
					}
				}
			}else if(data.result=="no-enough"){
				pophtml("Error","Out of stock!");
				resetNum(node);
			} else {
				pophtml("Error","update error!");
				resetNum(node);
			}
			setTimeout(function () { 
			    $("#ns_loading_box").hide();
		    }, 500);
		},
		error:function(data){
			setTimeout(function () { 
			    $("#ns_loading_box").hide();
		    }, 500);
		}
	});
}

//还原数量
function resetNum(node){
	var pnode = $(node).closest(".itemline");
	var num = pnode.find(".iqty:eq(0)").val();
	pnode.find(".input_num:eq(0)").val(num);
}

//--------------save for later---------------

function addLaterCart(node){	
	var pnode = $(node).closest(".itemline");
	var sid = pnode.find(".storageid:eq(0)").val();
	var num = pnode.find(".iqty:eq(0)").val();
	var list = [];
	pnode.find(".clistingid").each(function(i,e){
		var map = {};
		map['clistingid'] = $(this).val();
		map['storageid'] = sid;
		map['qty'] = num;
		list[list.length] = map;
	});
	var dd = $.toJSON(list);
	var url = "/savelatercartitem";
	$.ajax({
		url: url,
		type: "POST",
		dataType: "json",
		data: {"data": dd},
		success: function(data){
			if(data.result=="success"){
				reloadPage();
			}
		}
	});
}

function moveToCart(node){	
	var pnode = $(node).closest(".itemline");
	var sid = pnode.find(".storageid:eq(0)").val();
	var num = pnode.find(".iqty:eq(0)").val();
	var list = [];
	pnode.find(".clistingid").each(function(i,e){
		var map = {};
		map['clistingid'] = $(this).val();
		map['storageid'] = sid;
		map['qty'] = num;
		list[list.length] = map;
	});
	var dd = $.toJSON(list);
	var url = "/latertocart";
	$.ajax({
		url: url,
		type: "POST",
		dataType: "json",
		//contentType: "application/json",
		data: {"data": dd},
		success: function(data){
			if(data.result=="success"){
				reloadPage();
			}else if(data.result=="no-enough"){
				pophtml("Error","Out of stock!");
			}else if(data.result=="sold-out"){
				pophtml("Error","Sold out!");
			}else {
				pophtml("Error","add error!");
			}
		}
	});
}
function delLateritem(node){
	//confirmHtml("Confirm","Are you sure you wish to delete the selected items?",function(){
		var pnode = $(node).closest(".itemline");
		var sid = pnode.find(".storageid:eq(0)").val();
		var num = pnode.find(".iqty:eq(0)").val();
		var list = [];
		pnode.find(".clistingid").each(function(i,e){
			var map = {};
			map['clistingid'] = $(this).val();
			map['storageid'] = sid;
			map['qty'] = num;
			list[list.length] = map;
		});
		var dd = $.toJSON(list);
		var url = "/dellatercart";
		$.ajax({
			url: url,
			type: "POST",
			dataType: "json",
			data: {"data": dd},
			success: function(data){
				if(data.result=="success"){
					reloadPage();
				}
			}
		});
	//});
}

function addcollect(lis,action){
	var islogin = $("#islogin").val();
	if(islogin=="false"){
		$(".blockPopup_box").show();
		return;
	}
	var theemail = $("#theemail").val();
	var map = {};
	map['listingId'] = lis;
	map['email'] = theemail;
	var dd = $.toJSON(map);
	$.ajax({
		url: "/collect",
		type: "POST",
		dataType: "json",
		data: dd,
		success: function(data){
			if (data.result == "nologin") {
				$(".blockPopup_box").show();
				return;
			}
			if (data.result == "success") {
				pophtml("Success","Success!");
			} else {
				pophtml("Error", data.result);
			}
		}
	});
}

function skippay(url,needLoggen){
	//判断选择仓库
	var storageid = $("#cartlist_ul .sel").attr("storageid");
	if(storageid==null || storageid==undefined || storageid==""){
		pophtml("Error","Please choose the warehouse!");
		return;
	}
	url += "?storageid="+storageid
	var paramlogin = 0;
	if(needLoggen){
		paramlogin = 1;
		var islogin = $("#islogin").val();
		if(islogin=="false"){
			$("#loginPopWarp").show();
			return;
		}
	}
	//判断0元订单
	var grandtotal = parseFloat($("#grandTotal").html()); 
	if(grandtotal<=0){
		pophtml("Error","Total can not be zero!");
		return;
	}
	var listingids = "";
	var storageid = $("#cartlist_ul .sel").attr("storageid");
	var chooseStorage = $("#cartlist_ul .storage_products>.sel").parent();
	chooseStorage.find(".itemline").each(function(i,e){
		var nn = $(e);
		var listing = nn.find(".clistingid:eq(0)").val();
		var qqty = nn.find(".iqty:eq(0)").val();
		listingids += listing+":"+qqty+",";
	});
	
	//判断产品是否是在售状态和库存
	$.ajax({
		url: "/checkstatus",
		type: "GET",
		dataType: "json",
		data:{
			"islogin": paramlogin,
			"storageid": storageid,
			"listingids": listingids
		},
		//async:false,
		success: function(data){
			if(data.result=="success"){
				$("#ns_loading_box").show();
				window.location.href = url;
			}else if(data.result=="no-login"){
				$("#loginPopWarp").show();
			}else if(data.result=="no-qty"){
				pophtml("Error","quantity shortage");
			}
		}
	});
}

function pophtml(title, text){
	var errorBox = $('#errorBox');
	if(errorBox.length == 0){
		var html = [];
		html[html.length] = '<div style="display: block" class="blockPopup_box" id="errorBox" >';
		html[html.length] = '<div class="ns_pop_box"><div class="btn_pop_close" id="popClose" ></div>';
		html[html.length] = '<div class="pop_title"><h3>'+title+'</h3></div>';
		html[html.length] = '<div class="pop_con"><p id="errorBoxTxt">'+text+'</p></div>';
		html[html.length] = '<div class="pop_input_box">';
		html[html.length] = '<input type="button" class="pop_input_confirm" value="OK" id="errorBoxOkBtn"/></div></div>';
		html[html.length] = '<div class="blockPopup_black"></div></div>';
		$('body').append(html.join(''));
		$('#errorBoxOkBtn,#popClose').click(function() {
			$('#errorBox').remove();
		});
	}
	$('#errorBox').show();
	$('#errorBoxTxt').text((text || ''));
}

function confirmHtml(title, text, fun){
	var errorBox = $('#errorBox');
	if(errorBox.length == 0){
		var html = [];
		html[html.length] = '<div style="display: block" class="blockPopup_box" id="errorBox" >';
		html[html.length] = '<div class="ns_pop_box"><div class="btn_pop_close" id="popClose"></div>';
		html[html.length] = '<div class="pop_title"><h3>'+title+'</h3></div>';
		html[html.length] = '<div class="pop_con"><p id="errorBoxTxt">'+text+'</p></div>';
		html[html.length] = '<div class="pop_input_box">';
		html[html.length] = '<input type="button" class="pop_input_close" id="errorBoxOkBtn" value="CANCEL" />';
		html[html.length] = '<input type="button" class="pop_input_confirm" value="OK" id="confirmBtn" /></div></div>';
		html[html.length] = '<div class="blockPopup_black"></div></div>';
		$('body').append(html.join(''));
		$('#errorBoxOkBtn,#popClose').click(function() {
			$('#errorBox').remove();
		});
		$('#confirmBtn').click(function() {
			fun();
		});
	}
	$('#errorBox').show();
	$('#errorBoxTxt').text((text || ''));
}

function addDropshipping(node){
	var the = $(node);
	var isc = the.hasClass("sel");
	var dropstorageDom = $("#cartlist_ul .storage_products>.sel");
	var dropStorageid = dropstorageDom.attr("storageid");
	dropStorageid = dropStorageid==null ? 1 : dropStorageid;
	var listingid = "";
	dropstorageDom.parent().find(".csku").each(function(i, e) {
		if ($(this).html() == "X01") {
			var pnode = $(this).closest(".itemline");
			listingid = pnode.find(".clistingid:eq(0)").val();
			return false;
		}
	});
	
	if(isc && listingid!=""){
		var sid = dropStorageid;
		var list = [];
		var map = {};
		map['clistingid'] = listingid;
		map['storageid'] = sid;
		list[list.length] = map;
		var dd = $.toJSON(list);
		var url = "/delcartitem";
		$.ajax({
			url: url,
			type: "POST",
			dataType: "json",
			contentType: "application/json",
			data: dd,
			success: function(data){
				if(data.result=="success"){
					the.removeClass("sel");
					reloadPage();
				}
			}
		});
	}else if(!isc){
		var url = "/adddropshipping";
		$.ajax({
			url : url,
			type : "GET",
			dataType : "json",
			data:{
				"storageid": dropStorageid
			},
			async : true,
			success : function(data) {
				if (data.result == "success") {
					the.addClass("sel");
					reloadPage();
				}
			},
			complete : function() {
			}
		});
		
	}
}
// 设置仓库id
function setStorageid(storageid){
	setCookie("storageid", "", -1); 
	$.ajax({
		url : "/setstorageid",
		type : "GET",
		dataType : "json",
		async:false,
		data:{
			"storageid": storageid
		},
		success : function(data) {
		}
	});
}

function addCookie(name,value,days,path){   /**添加设置cookie**/  
    var name = escape(name);  
    var value = escape(value);  
    var expires = new Date();  
    expires.setTime(expires.getTime() + days * 3600000 * 24);  
    path = path == "" ? "" : ";path=" + path;  
    var _expires = (typeof days) == "string" ? "" : ";expires=" + expires.toUTCString();  
    var hostname = $("#hostname").val() || "tomtop";
    document.cookie = name + "=" + value + _expires + path + ";domain=."+hostname+".com";  
}  

function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires+ ";domain=."+hostname+".com";
}

function removeLoyaltyCookie() {
	setCookie("loyalty", "", -1); 
	setCookie("point", "", -1); 
}