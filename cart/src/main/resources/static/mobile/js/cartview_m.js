$(function(){
	
	//设置默认仓库
	var thestorageid = $("#thestorageid").val();
	var ishave = false;
	if(thestorageid!=null && thestorageid!="" && !isNaN(thestorageid)){
		var t0 = parseInt(thestorageid);
		$(".product_x_c").each(function(i,e){
			var t = parseInt($(this).attr("storageid"));
			if(t==t0){
				$(this).find(".radio:eq(0)").addClass("radioChecked");
				setStorageid(t);
				ishave = true;
				return false;
			}
		});
	}
	if(ishave==false){
		$(".product_x_c:eq(0)").find(".radio:eq(0)").addClass("radioChecked");
		var storageid=$(".product_x_c:eq(0)").attr("storageid");
		if(storageid==null || storageid==undefined || isNaN(storageid)){
			storageid = "";
		}
		setStorageid(storageid);
		//reloadPage(storageid);
	}
	
	//设置所选仓库的金额
	var chooseStorage = $("#cartlist_ul .radioChecked:eq(0)").closest(".product_x_c");
	if(chooseStorage!=null){
		getStorageTotal(chooseStorage);
	}
	
	//jpy日元判断
	var currencycode = $("#currencycode").val();
	if(currencycode=="JPY"){
		var dis_price = parseInt($("#discount_total").html());
		$("#discount_total").html(dis_price);
	}
	
	//收藏列表
	var collectlist = $("#collectlist").val();
	var colarr = collectlist.split(",");
	$("#cartlist_ul .clistingid").each(function(i,e){
		var jnode = $(e);
		var lsid = jnode.val();
		for(var i=0;i<colarr.length;i++){
			if(lsid==colarr[i]){
				jnode.parent().find(".collectcon i:eq(0)").removeClass().addClass("icon_wishlists");
				continue;
			}
		}
	});
	
	var oldcurr = getCookie("TT_CURR");
	//货币选中高亮
	$("#currencylist li").each(function(i,e){
		var lia = $(e).find("a:eq(0)");
		var thecode = lia.attr("code");
		if(thecode==oldcurr){
			lia.addClass("current");
			return false;
		}
	});
	
})

//选择仓库
$(".product_x dt").click(function(){
	var pnode= $(this).parent();
	getStorageTotal(pnode);
	var storageid=pnode.attr("storageid");
	if(storageid){
		setStorageid(storageid);
		removeLoyaltyCookie();
		reloadPageUsingSId(storageid);
	}
});
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
	$("#cart_subtotal").html(price);
	$("#grandTotal").html(price);
}

//刷新页面
function reloadPage(){
	var stid = $("#cartlist_ul .storage_products>.sel").attr("storageid");
	if(!stid){
		stid = 1;
	}
	window.location.href = "/?storageid="+stid;
}
function reloadPageUsingSId(id){
	window.location.href = "/?storageid="+id;
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
		//contentType: "application/json",
		data: {"data": dd},
		success: function(data){
			if(data.result=="success"){
				reloadPage();
			}else if(data.result=="no-enough"){
				popError("Out of stock!");
			}else if(data.result=="sold-out"){
				popError("Sold out!");
			}else {
				popError("add error!");
			}
		}
	});
}

//删除购物车
function delSubmit(){
	var pd = $("#confirm_box");
	var lid = pd.find(".deleteid:eq(0)").val();
	var sid = pd.find(".deleteStorageid:eq(0)").val();
	var list = [];
	var map = {};
	map['clistingid'] = lid;
	map['storageid'] = sid;
	list[list.length] = map;	
	
	var dd = $.toJSON(list);
	var url = "/delcartitem";
	$.ajax({
		url: url,
		type: "POST",
		dataType: "json",
		//contentType: "application/json",
		data: {"data": dd},
		success: function(data){
			if(data.result=="success"){
				reloadPage();
			}
		}
	});
}

//弹出删除购物车
function popDel(sid, lid){
	var pd = $("#confirm_box");
	pd.find(".deleteid:eq(0)").val(lid);
	pd.find(".deleteStorageid:eq(0)").val(sid);
	//$("#dialogs").show();
	pd.show();
}
//隐藏删除框
function hideDel(node){
	$(node).closest(".toggleDel").hide();
}


function updateItem(type,node){
	var num = 1;
	if(type=="sub"){
		num = parseInt($(node).next().val());
		if (num > 1) {
			num = num - 1;
		}else{
			return;
		}
		$(node).next().find("input:eq(0)").val(num);
	}else if(type=="add"){
		num = parseInt($(node).prev().val());
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
		//contentType: "application/json",
		data: {"data": dd},
		success: function(data){
			if(data.result=="success"){
				reloadPage();
			}else if(data.result=="no-enough"){
				popError("Out of stock!");
				resetNum(node);
			} else {
				popError("update error!");
				resetNum(node);
			}
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
		//contentType: "application/json",
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
				popError("Out of stock!");
			}else if(data.result=="sold-out"){
				popError("Sold out!");
			}else {
				popError("add error!");
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
			//contentType: "application/json",
			data: {"data": dd},
			success: function(data){
				if(data.result=="success"){
					reloadPage();
				}
			}
		});
	//});
}


//添加收藏
function addcollect(lis,node){
	var islogin = $("#islogin").val();
	if(islogin=="false"){
	    var hostname = $("#hostname").val() || "tomtop";
	    if("tomtop" == hostname){
	    	location.href = "http://m.tomtop.com/index.php?r=join/login";
			return;
	    }else{
	    	location.href = "http://m.chicuu.com/member/login";
	    	reutrn;
	    }
	}
	var jnode = $(node);
	var iscollect = false;
	var type = "add";
	if(jnode.hasClass("icon_wishlists")){
		iscollect = true;
		type = "del";
	}
	var theemail = $("#theemail").val();
	$.ajax({
		url: "/collect",
		type: "get",
		dataType: "json",
		data: {
			"listingId": lis,
			"email": theemail,
			"type": type
		},
		success: function(data){
			if (data.result == "nologin") {
				popError("no login!");
				return;
			}
			if (data.result == "success") {
				if(iscollect){
					jnode.removeClass().addClass("icon_wishlist")
				}else{
					jnode.removeClass().addClass("icon_wishlists")
				}
				popError("Success!");
			} else {
				popError(data.result);
			}
		}
	});
}

function skippay(node,needLoggen){
	//测试
	//$("#islogin").val("true");
	var jnode = $(node);
	var url = jnode.attr("url");
	
	//判断选择仓库
	var storageid = $("#cartlist_ul .radioChecked:eq(0)").attr("storageid");
	
	if(storageid==null || storageid==undefined || storageid==""){
		popError("Please choose the warehouse!");
		return;
	}
	url += "?storageid="+storageid
	var paramlogin = 0;
	var hostname = $("#hostname").val();
	if(needLoggen){
		paramlogin = 1;
		var islogin = $("#islogin").val();
		if(islogin=="false"){
			popError("no login!");
			location.href = "http://m."+hostname+".com/index.php?r=join/login";
			return;
		}
	}
	//判断0元订单
	var grandtotal = parseFloat($("#grandTotal").html()); 
	if(grandtotal<=0){
		popError("Total can not be zero!");
		return;
	}
	//传递商品信息
	var listingids = "";
	var chooseStorage = $("#cartlist_ul .radioChecked:eq(0)").closest(".product_x_c");
	chooseStorage.find(".itemline").each(function(i,e){
		var nn = $(e);
		var listing = nn.find(".clistingid:eq(0)").val();
		var qqty = nn.find(".iqty:eq(0)").val();
		listingids += listing+":"+qqty+",";
	});
	
	var storageid = $("#cartlist_ul .radioChecked:eq(0)").attr("storageid");
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
		success: function(data){
			if(data.result=="success"){
				//$("#dialogs").show();
				window.location.href = url;
			}else if(data.result=="no-login"){
				popError("no login");
			}else if(data.result=="no-qty"){
				popError("quantity shortage");
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
	//addCookie("storageid",storageid,365, "/");
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
    var domain = ";domain=."+$("#hostname").val()+".com";
    document.cookie = name + "=" + value + _expires + path + domain;  
}  

function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
}

function removeLoyaltyCookie() {
	setCookie("loyalty", "", -1); 
	setCookie("point", "", -1); 
}

function getCookie(name){
	var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
	if(arr=document.cookie.match(reg))
	return unescape(arr[2]);
	else
	return null;
}

