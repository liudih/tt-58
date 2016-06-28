$(function(){
	// 支付方式
	var paymentId = $("#cpaymentidHide").val() || 'paypal';
	// 激活对应支付方式
	if(paymentId){
		$(".payment dt[payment='"+paymentId+"']").trigger('click');
	}
		
	//绑定提交事件
	$("[tag=submit_button]").click(function() {
		var paymentId = $("[name='paymentId']").val();
		if('oceanpayment_qiwi' == paymentId){
			var qiwiAccount = $('input[name=qiwiAccount]').val();
			if(!qiwiAccount){
				errorTip('введите пожалуйста Номер телефона');
				$('input[name=qiwiAccount]').focus();
				return;
			}
		}
		$("#payment_form").submit();
	});
	
	//订单收货地址
	var shipAddr = $("[name='addressDiv']").html();
	//异步加载账单地址列表
	getAllBillAddr();
	
	//根据checkbox控制账单地址为收货地址或默认账单地址
	$(".billing").click(function(){
		var addressDiv_ = $(this).parents("dd").find("[name='addressDiv']");
		// 勾选复选框时,显示收货地址
        if($(this).is(':checked')) {
        	addressDiv_.empty().append(shipAddr);
		}
        // 否则显示用户默认账单地址
        else{
        	getUserDefaultBillAddr(addressDiv_);
		}
    });
	
	
	// 新增账单地址表单校验与提交
	$("#addBillAddressForm").validate({
		ignore: '' //验证隐藏元素,默认ignore: ':hidden', :hidden elements are now ignored by default
		,submitHandler: function(form) {
			$.ajax({
		        type:"post",
		        cache:false,
		        url:"/member/addAddress",
		        data:$(form).serialize(),
		        dataType:"json",
		        success: function(data) {
		        	if(data.ret=="1"){
		        		// 关闭新增账单地址弹出框
		        		$('.addre').removeClass('show_y');
		                $("#dialogs").css("display",'none');
		                
		                // 支付方式
		                var paymenType = $("[name='paymentId']").val();
		                if("oceanpayment_credit" == paymenType){
		                	// 清空信用卡选择的账单地址id
		                	$("[name='chooseBillAddrIdCredit']").val('');
		                }else if("oceanpayment_jcb" == paymenType){
		                	// 清空信用卡选择的账单地址id
		                	$("[name='chooseBillAddrIdJcb']").val('');
		                }
		                
		                //由于新增了地址,重新加载默认账单地址 
		                getUserDefaultBillAddr();
		                //由于新增了地址,重新加载账单地址列表
		                getAllBillAddr();
		        	}
	        	}
		      });
			return false;
		}
		,rules: {
			cfirstname: {
				required:true
				,maxlength:100
			}
			,clastname: {
				required:true
				,maxlength:100
			}
			,icountry: {
				required:true
			}
			,cstreetaddress: {
				required:true
				,maxlength:150
			}
			,cprovince: {
				required:true
				,maxlength:500
			}
			,ccity: {
				required:true
				,maxlength:80
			}
			,cpostalcode: {
				required:true
				,maxlength:80
			}
			,ctelephone: {
				required:true
				,maxlength:40
			}
	  	}
		,messages:{
			cfirstname: {
				required:"Please input your first name."
			}
			,clastname: {
				required:"Please input your last name."
			}
			,icountry: {
				required:"Please select your country."
			}
			,cstreetaddress: {
				required:"Please input your street address."
			}
			,cprovince: {
				required:"Please input your province."
			}
			,ccity: {
				required:"Please input your city."
			}
			,cpostalcode: {
				required:"Please input your postal code."
			}
			,ctelephone: {
				required:"Please input your phone number."
			}
		}
		,errorElement : "p"
		,errorPlacement: function(error, element) {
			// 错误信息放到元素父div后面
			element.parent().after(error);
		}
		,highlight: function(element, errorClass) {
			// 元素增加红色边框
			$(element).parent().addClass("error");  
        }
        ,unhighlight:function(element, errorClass){  
        	// 元素去掉红色边框
        	$(element).parent().removeClass("error");
        }  
	});
	
});

//异步加载默认账单地址
function getUserDefaultBillAddr (addressDiv_){
	$.ajax({  
	    url:'/member/getUserDefaultBillAddr',
	    data:{},  
	    type:'get',  
	    cache:false,  
	    dataType:'json',  
	    success:function(data) {
	        if(data){  
	        	var addr = data;
	        	if(!addressDiv_){
		        	 // 支付方式
	                var paymenType = $("[name='paymentId']").val();
	                if("oceanpayment_credit" == paymenType){
	                	addressDiv_ = $("[payment='oceanpayment_credit']").parents("li").find("[name='addressDiv']");
	                }else if("oceanpayment_jcb" == paymenType){
	                	addressDiv_ = $("[payment='oceanpayment_jcb']").parents("li").find("[name='addressDiv']");
	                }
	        	}
	        	addressDiv_.empty().append(
           			'<sapn class="user_name">'+addr.cfirstname+' '+addr.clastname+'</sapn>'+
                    '<span>'+addr.cstreetaddress+' '+addr.ccity +' '+addr.cprovince +' '+addr.countryFullName+' '+ addr.cpostalcode+'</span>'+
                    '<span>Phone: '+addr.ctelephone+'</span>'
               	);
	        } 
	    }  
	});
}

//异步加载账单地址列表
function getAllBillAddr(){
	$.ajax({  
		url:'/member/getAllBillAddr',
		data:{},  
		type:'get',  
		cache:false,  
		dataType:'json',  
		success:function(data) {
			//console.log(data);
			if(data && data.ret == "1" && data.data){  
	        	var billAddrList = data.data;
	        	var html = "";
	        	for(var i=0;i<billAddrList.length;i++){
	        		var item = billAddrList[i];
	        		html += 
	        			'<li class="clearfix">'+
		                    '<div class="chooseOneBox lineBlock">'+
		                        '<label>'+
		                            '<input type="radio" name="sex" value="Females">'+
		                            '<div class="radio lineBlock"><i class="icon_check"></i></div>'+
		                        '</label>'+
		                    '</div>'+
		                    '<div class="address_info_c">'+
		                    	'<input type="hidden" name="billAddrId" value="'+item.iid+'"/>'+
		                        '<span class="user_name">'+item.cfirstname +' '+item.clastname+'</span>'+
		                        '<span>'+item.cstreetaddress+' '+ item.ccity +' '+item.cprovince +' '+item.countryFullName +' '+item.cpostalcode+'</span>'+
		                        '<span>Phone: '+item.ctelephone+'</span>'+
		                    '</div>'+
		                '</li>';
	        	}
	        	
	        	$("[name='billAddrUl']").empty().append(html);
			} 
		}  
	});
}

var timerNo=null;
function errorTip(tip){
	clearTimeout(timerNo);
    $(".toast").addClass("hint_slide").html(tip);
    timerNo=setTimeout(function ()
    {
        $(".toast").removeClass("hint_slide");
    }, 3000);
}

