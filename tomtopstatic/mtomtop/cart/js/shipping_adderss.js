$(function(){
    //添加地址
    $('.address_but').click(function(){
        $("#dialogs").css("display",'block');
        $('.addre').addClass('show_y');
    });
    $('.ship_address_btn').click(function(){
    	$(".addre .save_but").unbind();
    	$(".addre .save_but").click(function(){
            validShipAddressForm(function(id){
            	location.href=changeURLArg(location.href,"addressId",id);
        	},function(){
        		errorTip('Submit error!');
        	});
        });
    });
    $('.addre .cancel_but').click(function(){
        $('.addre').removeClass('show_y');
        $("#dialogs").css("display",'none');
    });
    $(".addre .save_but").click(function(){
        validShipAddressForm(function(id){
        	location.href=changeURLArg(location.href,"addressId",id);
    	},function(){
    		errorTip('Submit error!');
    	});
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
        oCountry_c.parent().removeClass('error');
        oCountry_c.parent().next('p').hide();
        oCountry_c.hide();
        var cid = $(this).attr("cid");
        var countryCode = $(this).attr("ccode");
        $("#icountry").val(cid);
        $("#countryCode").val(countryCode);
    });
    //选择更多地址
    $(".more_ship_address .icon_adressbook").click(function(){
        $("#dialogs").css({display:"block"});
        $(this).parent().siblings(".more_address").addClass("show_y");
    });
    //关闭更多地址
    $(".close_more_address").click(function(){
        $(".more_address").removeClass("show_y");
        $("#dialogs").css({display:"none"});
    });
    //选择优惠卷
    $(".drop_down").click(function(){
    	var lis = $("#couponLiShow li");
    	if(lis==null || lis.length==0){
    		popError("no data!");
    		return;
    	}
        $("#dialogs").css({display:"block"});
        $(".coupons").addClass("show_y");
    });
    $(".coupons li").click(function(){
        $(".coupons li .radio").removeClass("radioChecked");
        $(this).find(".radio").addClass("radioChecked");
        $(".coupon_off").val($(this).find(".off").text()+"Coupon");
        $(".coupons").removeClass("show_y");
        $("#dialogs").css({display:"none"});
    });
    //留言
    $(".message_select").click(function(){
        $(".message_text").toggle();
    });
    
    $('#addShipAddressForm').find('input[name]').blur(function() {
    	var value = $(this).val();
    	if (value) {
    		$(this).parent().removeClass('error');
			$(this).parent().next('p').hide();
    	}else{
    		$(this).parent().addClass('error');
			$(this).parent().next('p').show();
    	}
    });
    
  //选择支付方式
    $(".payment dt").click(function(){
        $(".payment dl").removeClass("select");
        $(this).parents("dl").addClass("select");
        $(".payment .lineBlock").removeClass("radioChecked");
        $(this).find(".lineBlock").addClass("radioChecked");
        $("#paymentId").val($(this).parents("li").attr("class"));
    });
    
    $(".chose_state .cur").click(function(){
        $(".state").toggle();
    });
    $(".state li").click(function(){
        var sPlace=$(this).find("i").attr("class");
        $(".chose_state .cur i").removeClass().addClass(sPlace);
        var sText=$(this).find("b").text();
        $(".chose_state .cur b").text(sText);
        $("#qiwiCountry").val(sText);
        $(".state").toggle();
    });
    
    $(".billing").click(function(){
    	var billingNode = this;
        $(billingNode).parents("dd").find(".more_address_but").toggle();
        $(billingNode).parents("dd").find(".visa_address").toggle();
        if($(".billing").is(":checked")){
        	$(".ship_address_info").find("[name]").each(function(){
        		var name = $(this).attr('name');
        		var value = $(this).text();
        		$(billingNode).parent().siblings('.bill_address_info').find('[name='+name+']').text(value);
        	});
        	$("#billAddressId").val($("#shipAddressId").val());
        }
    });
    $(".more_bill_address .icon_adressbook").click(function(){
    	loadBillAddress(this);
    	$("#dialogs").css({display:"block"});
        $(this).parent().siblings(".more_address").addClass("show_y");
    });
    
    $(".bill_address_but").click(function(){
    	var billAddressInfoNode = $(this).parents("dd").find('.bill_address_info');
        $(".addre .save_but").unbind();
    	$(".addre .save_but").click(function(){
    		var valid = true;
    		var datas = {};
    		$('#addShipAddressForm').find('input[name]').each(function(){
    			var value = $(this).val();
    			var name = $(this).attr('name');
    			if(name !='iid' && !value){
    				valid = false;
    				$(this).parent().addClass('error');
    				$(this).parent().next('p').show();
    			}else{
    				$(this).parent().removeClass('error');
    				$(this).parent().next('p').hide();
    			}
    			datas[name] = value;
    		});
    		if(valid){
    			datas.cstreetaddress = datas.address1 + (datas.address2 || '');
    			$.ajax({url: "/member/savebilladdress", 
    				type: 'POST', 
    				data: datas, 
    				dataType: 'json', 
    				timeout: 1000 * 20, 
    				error: function(){}, 
    				success: function(data){
    					if(data.result!="success"){
    			            errorTip('Submit error!');
    					}else{
    						$(billAddressInfoNode).find('[name]').each(function(){
    							var name = $(this).attr('name');
    							$(this).text(datas[name]);
    						});
    						$(billAddressInfoNode).show();
    						$("#billAddressId").val(data.id);
    						$(".addre").removeClass("show_y");
    				        $("#dialogs").css("display",'none');
    					}
    				} 
    			});
    		}
        });
    });
    
    $("#ship_address_ul li:has('div .radioChecked')").trigger('click');
});

function tapShipDefault(node){
	changeShipAddress(node);
	refreshShipMethod();
	$(".more_address").removeClass("show_y");
    $("#dialogs").css({display:"none"});
}

function changeShipAddress(node){
	$(node).find("[name]").each(function(){
		var name = $(this).attr('name');
		var value = $(this).text();
		$('.ship_address_info').find('[name='+name+']').text(value);
	});
	$(node).parent().find(".radio").removeClass("radioChecked");
    $(node).find(".radio").addClass("radioChecked");
	$("#ship_countryCode").val($(node).find("[name=countryCode]").text());
	$("#shipAddressId").val($(node).find("[name=address_id]").text());
}

function refreshShipMethod(){
	var country = $('#ship_countryCode').val();
	var subtotal = parseFloat($('#subtotal').html().replace(/,/g,''));
	var discount = parseFloat($('#discount_total').html().replace(/,/g,''));
	var total = subtotal + discount;
	var ordernumber = $("#orderNum").val() || "";
	$.ajax({url: '/ship-method', 
		type: 'POST', 
		contentType : 'text/plain',
		data:$.toJSON({'country':country,'totalPrice':total,'ordernumber':ordernumber}), 
		dataType: 'json', 
		timeout: 30000, 
		success: function(result){
			var ele = [];
			var selected = false;
			var selected1 = false;
			for(var i = 0 ; i < result.length; i++){
				var cell = result[i];
				ele.push('<li');
				//判断是否能够选择
				if(!cell.isShow){
					ele.push(' class="no" error-info="');
					ele.push(cell.errorDescription);
					ele.push('"')
				}else if(!selected){
					ele.push(' class="choose"');
					$("#shipMethodCode").val(cell.code);
					selected = true;
				}
				ele.push(' code="');
				ele.push(cell.code);
				ele.push('">');
				ele.push('<div class="method_info">')
				ele.push('<h4>');
	            ele.push(cell.title);
	            ele.push('</h4>');
				ele.push('<p><span>');
				ele.push(cell.description);
				ele.push('</span><em class="shipprice">');
				ele.push(cell.price);
				ele.push('</em></p></div>');
                ele.push('<div class="chooseOneBox lineBlock">');
                ele.push('<label>');
				ele.push('<div class="radio lineBlock ');
				if(!cell.isShow){
					ele.push('forbidChoice');
				}else if(!selected1){
					ele.push('radioChecked');
					selected1=true;
				}
				ele.push('"><i class="icon_check"></i></div>');
				ele.push('</label></div>');  
			}
			$('#shipping_method').html(ele.join(''));
			calculateTotal();
			//bind shipping method select event
			$(".method li").not(".no").click(function(){
		        $(".method li").removeClass("choose");
		        $(".method li .radio").removeClass("radioChecked");
		        $(this).addClass("choose");
		        $(this).find(".radio").addClass("radioChecked");
		        calculateTotal();
		        $("#shipMethodCode").val($(this).attr("code"));
		    });
		    
		    $(".method li.no").click(function(){
		        errorTip($(this).attr("error-info"));
		    });
		},
		complete: function(){
//			$('#ns_loading_box').hide();
		}
	});
}

function validate(){
	var valid = true;
	$('#addShipAddressForm').find('input[name]').each(function(){
		var value = $(this).val();
		var name = $(this).attr('name');
		if(name !='iid' && !value){
			valid = false;
			$(this).parent().addClass('error');
			$(this).parent().next('p').show();
		}else{
			$(this).parent().removeClass('error');
			$(this).parent().next('p').hide();
		}
	});
	return valid;
}

function validShipAddressForm(succeedCallback,failedCallback){
	var valid = true;
	var datas = {};
	$('#addShipAddressForm').find('input[name]').each(function(){
		var value = $(this).val();
		var name = $(this).attr('name');
		if(name !='iid' && !value){
			valid = false;
			$(this).parent().addClass('error');
			$(this).parent().next('p').show();
		}else{
			$(this).parent().removeClass('error');
			$(this).parent().next('p').hide();
		}
		datas[name] = value;
	});
	if(valid){
		datas.cstreetaddress = datas.address1 + (datas.address2 || '');
		$.ajax({url: "/member/addshipaddress", 
			type: 'POST', 
			data: datas, 
			dataType: 'json', 
			timeout: 1000 * 20, 
			error: function(){}, 
			success: function(data){
				if(data.result!="success"){
					if($.isFunction(failedCallback)){
						failedCallback();
					}
				}else{
					if($.isFunction(succeedCallback)){
						succeedCallback(data.id);
					}
				}
			} 
			});
	}
}

function calculateTotal(){
	var price = $("#shipping_method .choose").find(".shipprice").html();
	if(price){
		var grandTotal;
		var subtotal = $('#subtotal').html();
		var discount = parseFloat($('#discount_total').html());
		subtotal = subtotal.replace(/,/g,'');
		price = price.replace(/,/g,'');
		//检查是否有小数点
		if(subtotal.indexOf('.') == -1){
			subtotal = parseInt(subtotal);
			subtotal += parseInt(discount+"");
			price = parseInt(price);
			grandTotal = subtotal + price;
			$('#shipCost').text(price);
		}else{
			subtotal = parseFloat(subtotal);
			subtotal += discount;
			price = parseFloat(price);
			grandTotal = subtotal + price;
			//保留两位小数
			grandTotal = Math.round(grandTotal * 100) / 100;
			
			$('#shipCost').text(toDecimal2(price));
		}
		$('#grandTotal').html(grandTotal);
	}
}

function toDecimal2(x) {    
	var f = parseFloat(x);
    f = Math.round(f * 100) / 100;    
    var s = f.toString();    
    var rs = s.indexOf('.');    
    if (rs < 0) {    
        rs = s.length;    
        s += '.';    
    }    
    while (s.length <= rs + 2) {    
        s += '0';    
    }    
    return s;    
}

//购物车下单
function placeOrderBtn(){
	var shipAddressId = $("#shipAddressId").val();
	if(!shipAddressId){
		errorTip('Shipping Address can not be null!');
		$(".address_but").trigger('click');
		$(".ship_address_btn").trigger('click');
		return;
	}
	var paymentMethod = $(".payment_c").find("dl").filter(".needBill").filter(".select");
	var billAddressId = $("#billAddressId").val();
	//需要账单，但没有账单信息，提示错误
	if(paymentMethod.length > 0 && !billAddressId){
		errorTip('Billing Address can not be null!');
		return;
	}
	var paymentId = $("#paymentId").val();
	if('oceanpayment_qiwi' == paymentId){
		var qiwiAccount = $('input[name=qiwiAccount]').val();
		if(!qiwiAccount){
			errorTip('введите пожалуйста Номер телефона');
			$('input[name=qiwiAccount]').focus();
			return;
		}
	}
	//邮寄方式
	var shipMethod = $('#shipping_method .choose').attr("code");
    if(!shipMethod){
    	errorTip('please select shipping method');
		return;
    }
    $("#message").val($("#leaveMessage").val());
	$('#placeOrderForm').submit();
};

var timerNo=null;
function errorTip(tip){
	clearTimeout(timerNo);
    $(".toast").addClass("hint_slide").html(tip);
    timerNo=setTimeout(function ()
    {
        $(".toast").removeClass("hint_slide");
    }, 3000);
}

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

function shipAddressEdit(node){
	var datas = {};
	var tnode = $(node);
	tnode.parents('.address_c').find('[name]').each(function(){
		var name = $(this).attr('name');
		var value = $(this).text();
		datas[name] = value;
	});
	
	$('#addShipAddressForm').find('input[name]').each(function(){
		var name = $(this).attr('name');
		var value = datas[name] || '';
		$(this).val(value);
	});
	$("#dialogs").css("display",'block');
    $('.addre').addClass('show_y');	
	var countryName = datas.countryName;
	$('.country_c li').each(function(){
		var text = $(this).text();
		if(countryName == text){
			$(this).trigger('click');
			return false;
		}
	});
	$(".addre .save_but").unbind();
	$(".addre .save_but").click(function(){
		if(!validate()){
			return;
		}
		
		$('#addShipAddressForm').find('input[name]').each(function(){
			var name = $(this).attr('name');
			var value = $(this).val();
			$('.address_c').find('[name='+name+']').text(value);
			$('#confirmOrderForm').find('input[name='+name+']').val(value);
		});
		
		$("#ship_countryCode").val($("#countryCode").val());
		refreshShipMethod();
		$('.addre').removeClass('show_y');
        $("#dialogs").css("display",'none');
    });
}

//ec支付下单
function submitOrder(){
	var valid = true;
	$('#placeYourOrder').attr('disabled','disabled');
	$('#confirmOrderForm').find('input[name!="address2"]').each(function(){
		var value = $(this).val();
		if(!value){
	    	$('.icon_edite').trigger('click');
	    	valid = false;
	    	return;
		}
	});
	//邮寄方式
	var shipMethod = $('#shipping_method .choose').attr("code");
    if(!shipMethod){
    	errorTip('please select shipping method');
    	valid = false;
    }
    if(!valid){
		$('#placeYourOrder').removeAttr('disabled');
		return;
    }
	$('#confirmOrderForm').submit();
}

function loadBillAddress(obj){
	$.ajax({url: "/member/billaddresses", 
		type: 'GET', 
		dataType: 'json', 
		timeout: 1000 * 20, 
		error: function(){}, 
		success: function(data){
			if(data.result !='success'){
				return;
			}
			var result = data.addresses;
			var ele = [];
			var selected = false;
			var selected1 = false;
			for(var i = 0 ; i < result.length; i++){
				var cell = result[i];
				ele.push('<li onclick="changeBillAddress(this)" class="clearfix" addressId="');
				ele.push(cell.iid);
				ele.push('">');
				ele.push('<div class="chooseOneBox lineBlock">');
				ele.push('<label><div class="radio lineBlock">');
				ele.push('<i class="icon_check"></i></div>');
				ele.push('</label></div>');
				ele.push('<div class="user_info address_info_c">');
				ele.push('<span class="user_name">');
				ele.push('<label name="cfirstname">');
	            ele.push(cell.cfirstname);
	            ele.push('</label>&nbsp;');
	            ele.push('<label name="clastname">');
	            ele.push(cell.clastname);
				ele.push('</label></span><span>');
				ele.push('<label name="address1">');
				ele.push(cell.cstreetaddress);
				ele.push('</label>,&nbsp;');
				ele.push('<label name="ccity">');
				ele.push(cell.ccity);
				ele.push('</label>,&nbsp;');
				ele.push('<label name="cprovince">');
				ele.push(cell.cprovince);
				ele.push('</label>,&nbsp;');
				ele.push('<label name="countryName">');
				ele.push(cell.countryFullName);
				ele.push('</label>(');
				ele.push('<label name="cpostalcode">');
				ele.push(cell.cpostalcode);
				ele.push('</label>)');
				ele.push('</span><span>');
				ele.push('<label name="ctelephone">');
				ele.push(cell.ctelephone);
				ele.push('</label></span></div></li>');
			}
			$(obj).parent().siblings(".more_address").find(".bill_address_ul").html(ele.join(''));
		} 
	});
}

function changeBillAddress(obj){
	var billAddressInfoNode = $(obj).parents("dd").find(".bill_address_info");
	$(obj).parent().find(".radio").removeClass("radioChecked");
    $(obj).find(".radio").addClass("radioChecked");
    $(".more_address").removeClass("show_y");
    $("#dialogs").css({display:"none"});
	$("#billAddressId").val($(obj).attr("addressId"));
	$(obj).find("[name]").each(function(){
		var name = $(this).attr('name');
		var value = $(this).text();
		$(billAddressInfoNode).find('[name='+name+']').text(value);
	});
	$(billAddressInfoNode).show();
}