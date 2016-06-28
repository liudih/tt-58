$(function(){
	//选择支付方式
    $(".payment dt").click(function(){
        $(".payment dl").removeClass();
        $(this).parents("dl").addClass("select");
        $(".payment .lineBlock").removeClass("radioChecked");
        $(this).find(".lineBlock").addClass("radioChecked");
        $("[name='paymentId']").val($(this).attr("payment"));
        
    });
    //qiwi选择国家
   $(".chose_state .cur").click(function(){
        $(".state").toggle();
   });
   //qiwi下拉框选择事件
    $(".state li").click(function(){
        var sPlace=$(this).find("i").attr("class");
        $(".chose_state .cur i").removeClass().addClass(sPlace);
        var sText=$(this).find("b").text();
        $(".chose_state .cur b").text(sText);
        $("#qiwiCountry").val(sText);
        $(".state").toggle();
    });
    $(".billing").click(function(){
    	// 清空选择的账单地址id
        $("[name='chooseBillAddrIdCredit']").val('');
        $("[name='chooseBillAddrIdJcb']").val('');
    	if($(this).is(":checked")){
    		$(this).parents("dd").find(".more_address_but").hide();
    		$(this).parents("dd").find(".visa_address").hide();
    	}else{
    		$(this).parents("dd").find(".more_address_but").show();
    		$(this).parents("dd").find(".visa_address").show();
    	}
    });
    
    // ===>start 移动端再次支付页面js
    // 移动端再次支付页面-添加账单地址按钮
    $('.address_but').click(function(){
        $("#dialogs").css("display",'block');
        $('.addre').addClass('show_y');
    });
    
    // 移动端再次支付页面-添加账单地址弹出框取消按钮
    $('.addre .cancel_but').click(function(){
        $('.addre').removeClass('show_y');
        $("#dialogs").css("display",'none');
    });
    
    $('.addre input').click(function(){
        $(this).siblings(".icon_cross").css({opacity:1});
    }).blur(function(){
        $(this).siblings(".icon_cross").css({opacity:0});
    });
    $('.addre .icon_cross').click(function(){
        $(this).siblings("input").val("");
    });
    $(".addre .country").click(function(){
        $(this).siblings(".country_c").toggle();
    });
    $(".addre .country_c li").click(function(){
        var oCountry_c=$(this).parents(".country_c");
        oCountry_c.siblings(".country").val($(this).text());
        oCountry_c.siblings("[name='icountry']").val($(this).val());
        oCountry_c.hide();
    });
    //选择更多地址
    $(".more_address_but").click(function(){
        $("#dialogs").css({display:"block"});
        $(this).siblings(".more_address").addClass("show_y");
    });
    // 使未来添加的li元素也会有click事件,这样写相当于旧版本的live()
    $(document).on("click",".more_address li",function(){
    	// 支付方式
        var paymenType = $("[name='paymentId']").val();
        //把选中的账单地址赋值到页面
        if("oceanpayment_credit" == paymenType){
        	$("[name='chooseBillAddrIdCredit']").val($(this).find("[name='billAddrId']").val());
        }else if("oceanpayment_jcb" == paymenType){
        	$("[name='chooseBillAddrIdJcb']").val($(this).find("[name='billAddrId']").val());
        }
        $(".more_address li .radio").removeClass("radioChecked");
        $(this).find(".radio").addClass("radioChecked");
        var sCuraddress=$(this).find(".address_info_c").html();
        $(this).parents(".more_address").siblings(".address_info_c").html(sCuraddress);
        $(this).parents(".more_address").removeClass("show_y");
        $("#dialogs").css({display:"none"});
    });
    //关闭更多地址
    $(".close_more_address").click(function(){
        $(".more_address").removeClass("show_y");
        $("#dialogs").css({display:"none"});
    });
    
    // <===end 移动端再次支付页面js
});