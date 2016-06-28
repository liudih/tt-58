$(function(){
	var inN = $(".infW").length+4;
	var inW = 940/inN;
	$(".infW").css({"width":inW})
	$(".accInfs ol li").css({"width":inW-1})
	$(".accInfs").css({"width":inW*4-1})
	
	$(".accInfs ol li").mouseover(function()
	{
		$(".parents").addClass("parentsStyle")
		$(".accInfs").css({"border-color":"#219ced"})
	})
	$(".accInfs ol li").mouseout(function()
	{
		$(".parents").removeClass("parentsStyle")
		$(".accInfs").css({"border-color":"#e8e8e8"})
	})
	//===================修改 形象图片============================
	$(".clicks").click(function()
	{
		$(this).siblings(".clickPop").fadeIn();
		$(this).after("<div class='bkBlack'></div>")
	})
	$(".closePop").click(function()
	{
		$(this).parents(".clickPop").fadeOut();
		$(".bkBlack").fadeOut(function(){$(".bkBlack").remove()});
	})
	$(document).on("click",".bkBlack",function(){
		$(this).siblings(".clickPop").fadeOut();
		$(this).fadeOut(function(){$(".bkBlack").remove()});
	})
	$(".black").click(function(){
		$(this).parents(".blockPopup_box").fadeOut();
	})
	//======================================================
	$(".rightAll").children("span").click(function(){
		$(".rightThis").children("span").addClass("afters");
		$(this).toggleClass("aftersAll")
		$(this).parents(".choWishlist").siblings(".choWishlist").find(".rightAll").children("span").toggleClass("aftersAll")
		if($(this).hasClass("aftersAll")==false){
			$(".rightThis").children("span").toggleClass("afters");
		}
		//$(".filterGray").find(".rightThis").children("span").removeClass("afters");
	})
	$(document).on("click",".rightThis",function(){ 
		$(this).children("span").toggleClass("afters");
		if($(this).children("span").hasClass('afters')){
			$(this).children(".joinCheck").attr("checked", true);
			$(this).children(".joinCheck").prop("checked",true);
		}else{
			$(this).children(".joinCheck").attr("checked", false);
		}
	})
	$(document).on("click",".delete,.heart",function(){
		if($(this).hasClass("bannedClick")==false)
		{
			$(".cartListUL,.listOl_bd").css({"z-index":"1"})
			$(this).parents(".cartListUL,.listOl_bd,.cartListLI").css({"z-index":"2"})
			$(this).children(".deletePop").toggle();
		}else{
			$(this).unbind("click");
		}
	})
	//=============rightPaly_addToCart=================
	$(".rightPaly_addToCart,.buttCart").click(function()
	{
		$(this).siblings(".addTo_cartHide").css({"display":"block"})
	})
	$(".addTo_close").click(function()
	{
		$(this).parents(".addTo_cartHide").css({"display":"none"})
	})
	//===================================================
	$(".addCart").click(function(){
		if($(this).children(".TTCart").length==0){
			$(this).append('<div class="TTCart"><i class="icon-upArr"></i><a class="icon-Close" href="javascript:void(0)"></a><em class="lineBlock"> </em><p class="cart_title lineBlock"><span class="productTotal">1</span>item added to cart</p><p class="cart_txt"><b class="success-qty">1</b>total item in your cart<br><span>Total</span><span class="fz_orange">US$5.99</span></p><a class="toCartBtn btn btn-orange" href="javascript:void(0)">View Cart</a><a class="toShoppingBtn btn" href="javascript:void(0)">Continue Shopping</a></div>')
		}
		$(".TTCart").css({"left":-120,"top":35,"position":"absolute"})
	})
	$(document).on("click",".icon-Close,.toShoppingBtn",function(){
		$(".TTCart").remove();
	})
	//=============================
	$(".agreeTT").click(function(){
		$(".ContinueSpay").toggleClass("ContinueNo");
		$(".checkouts").toggle();
	})
})




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


//==========================个人中心 等级动画效果==================================
$(function(){
	function consMove(){
		var w = $(".consumption").width();
		var wz = w - $(".currentWZ").width()/2;
		$(".consMove").animate({width:w},1000,function(){
			$(".currentWZ").css({"display":"block","left":wz})
			var i= 1;
		    setInterval(function() {
				if (i > 36) {
					i = 1;
				}
				var postion = i + "px 0px";
				$(".consMove").css('background-position',postion);
				i++;}, 200);
		})
	}
	var amount = 19999;
	$(".currentWZ").append("$"+amount)
	$('.progressPoint li').each(function(){
	   var from=$(this).data('from');
	   var to=$(this).data('to');
	   if(amount>=from&&amount<=to){
		   if($(this).index()==0){
				$(".consumption").animate({
					width:amount/to*146
			  	},function(){consMove()})
			}else{
			   $(".consumption").animate({
					width:146*($(this).index())+(amount-from)/(to-from)*146
			 	 },function(){consMove()})
			}
	   }else if(amount>=100000)
	   {
			$(".consumption").animate({
				width:730
			},function(){consMove()})
		}
	});
})
//===========================个人中心 签到===============
$(function(){
	$(".dailyPoints_tt").click(function(){
		$(this).addClass("dailyPoints_gray");
		$(this).siblings("p").fadeOut();
		$(".score_tt").animate({
			top:"0px",
			opacity:"0"
		});
		$(".dailyPoints_gray").unbind("click");
	})
	$(".dailyPoints_tt").hover(function(){
		if($(this).hasClass("dailyPoints_gray")==false){
			$(this).siblings(".dailyPoints_tt_C").fadeIn();
			$(this).siblings(".dailyPoints_tt_NC").fadeOut();
		}else{
			$(this).siblings(".dailyPoints_tt_NC").fadeIn();
			$(this).siblings(".dailyPoints_tt_C").fadeOut();
		}
	},function(){
		$(this).siblings("p").fadeOut();
	})
})
function Country() {}
Country.prototype = {
	// 得到所有国家
	search : function(q) {
		var regstr = $(q).val() + ".*";
		var reg = new RegExp(regstr, "i");
		$(".edit_country .country_list li").each(function(i) {
			$(this).hide();
		});
		$(".edit_country .country_list li").each(function(i) {
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
	$(".edit_country .country_list li").click(function() {
		var countryText = $(this).find("span").text();
		var countryCode = $(this).find("span").attr("data");
		var texts = $(this).parentsUntil(".select_country", "div.country_all").prev("h3");
		texts.children("#currents_flage").removeClass().addClass("flag_" + countryCode);
		texts.children(".flag_Txt").text(countryText);
		$(this).parents(".country_all").hide();
	});
	$("input[name=country_filter]").keyup(function() {
		country.search(this);
	});
	$(".edit_country .select_country h3").click(function(e){
		$(this).siblings(".country_all").toggle();
		e.stopPropagation();
	})
	$(".search_country").click(function(e){
		e.stopPropagation();
	})
	$("body").click(function(){
		$(".country_all").hide();
	})
	//====================================
	$(".defADD").click(function(){
		$(".defADD").removeClass("defActi");
		$(this).addClass("defActi");
		$(".removeADD").removeClass("bannedClick");
		$(this).siblings(".removeADD").addClass("bannedClick");
	})
	//=================地址编辑=================
	$(document).on("click",".deitADD",function()
	{
		$(this).siblings(".newAddressBox").after("<div class='bkBlack'></div>");
		$(this).siblings(".newAddressBox").addClass("addPop");
		$(this).siblings(".newAddressBox").fadeIn()
	})
	///=====================================
	$(".newAddress").click(function()
	{
		$(this).parents(".addChAll").siblings(".newAddressBox").fadeToggle();
		$(this).parents(".addChAll").siblings(".newAddressBox").after("<div class='bkBlack'></div>");
	})
})



$(function()
{
	$(".blackXXK li,.hsXXK li").click(function()
	{
		var index = $(this).index();
		if($(this).hasClass("Recycle")==false){
			$(this).addClass("xxkActi");
			$(this).siblings().removeClass("xxkActi");
			$(this).parents().siblings(".xxkBOX").hide();
			$(this).parents().siblings(".xxkBOX").eq(index).fadeIn();
		}
	})
	$(".postPhotosA").click(function()
	{
		$(this).parents(".postPhotos").siblings(".blockPopup_box").fadeIn();
		mH = $(this).parents().siblings(".blockPopup_box").find(".scrollWZ").height();
		mY = $(this).parents().siblings(".blockPopup_box").find(".scrollY").height();
		$(".scrollWZ").css({"margin-top":-mH/2})
		if(mY>mH)
		{
			$(this).parents().siblings(".blockPopup_box").find(".scrollBox").css({"overflow-y":"scroll"})
		}
		else{
			$(this).parents().siblings(".blockPopup_box").find(".scrollBox").css({"overflow-y":"auto"})
		}
	})
})