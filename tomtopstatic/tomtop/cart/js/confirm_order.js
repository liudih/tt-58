$(function() {
	//告知用户邮寄国家不支持
	var shipToCountryCode = $("#shipToCountryCode").val();
	if(shipToCountryCode == null || shipToCountryCode==""){
		ttAlert('shipping unavailable');
	}else{
		refreshShipMethod();
	}
	
	var ns_left=parseInt($(".newshopping_box_right").offset().left)+"px";
    var ns_top=$(".newshopping_box_right").offset().top;
    var ns_position=$(".shopping_bottom").offset().top;
    var ns_h2=ns_position-$(".newshopping_box_right").height();
    $(window).scroll(ns_top, function(event) {
        if($(this).scrollTop()>event.data&&$(this).scrollTop()<ns_h2){
            $(".newshopping_box_right").css({"position":"fixed","z-index":"99","left":ns_left,"top":"40px","margin-top":"0px"})
        }else{
            $(".newshopping_box_right").css({"position":"inherit","margin-top":"70px"});
        }
    });
  //jpy日元判断
	var currencycode = $("#currencycode").val();
	if(currencycode=="JPY"){
		var discount_total = parseInt($("#discount_total").html());
		$("#discount_total").html(discount_total);
		var shipCost = parseInt($("#shipCost").html());
		$("#shipCost").html(shipCost);
	}
});



function ttAlert(tip){
	var errorBox = $('#errorBox');
	if(errorBox.length == 0){
		var ele = [];
		ele.push('<div id="errorBox" style="display: block" class="pu_pop popNone_s">');
		ele.push('<div class="ns_pop_box">');
		ele.push('<div class="btn_pop_close"></div>');
		ele.push('<div class="pop_title">');
		ele.push('<h3>Error</h3>');
		ele.push('</div>');
		
		ele.push('<div class="pop_con">');
		ele.push('<p id="errorBoxTxt"></p>');
		ele.push('</div>');
		ele.push('<div class="pop_input_box">');
		ele.push('<input id="errorBoxOkBtn" type="button" class="pop_input_confirm" value="OK">');
		ele.push('</div>');
		ele.push('</div>');
		ele.push('<div class="blockPopup_black"></div>');
		ele.push('</div>');
		$('body').append(ele.join(''));
		$('#errorBoxOkBtn').click(function() {
			$('#errorBox').hide();
		});
		$('#errorBox').find('.btn_pop_close').click(function() {
			$('#errorBox').hide();
		});
	}
	
	$('#errorBox').show();
	$('#errorBoxTxt').text((tip || ''));
}
function submitOrder(){
	$('#placeYourOrder').attr('disabled','disabled');
	
	var addressData = {};
	$("#defaultAddress span[name]").each(function(){
		var name = $(this).attr('name');
		var value = $(this).text();
		addressData[name] = value;
	});
	//验证数据是否完整
	var valid = true;
	for (var attr in addressData){
		if('address2' != attr && 'iid'!=attr){
			var value = addressData[attr];
			if(!value){
				$('#shipAddressEdit').trigger('click');
				$("#province_list_id").hide();
				valid = false;
				break;
			}
		}
	}
	if(!valid){
		$('#placeYourOrder').removeAttr('disabled');
		$("#shipAddressEdit").click();
		return;
	}
	//邮寄方式
	var shipMethod = $('#shipping_method .select').attr("code");
    if(!shipMethod){
    	ttAlert('please select shipping method');
		$('#placeYourOrder').removeAttr('disabled');
		return;
    }else{
    	$("#shipMethodCode").val(shipMethod);
    }
	
	if(valid){
		
		$('#confirmOrderForm input[name]').each(function(){
			var node = $(this);
			var name = $(this).attr('name');
			for(var p in addressData){
				if(name==p){
					node.val(addressData[name]);	
				}
			}
		});
		var leaveMessage = $('#leaveMessage').val();
		$('#confirmOrderForm [name=leaveMessage]').val(leaveMessage);
		
		$('#confirmOrderForm').submit();
	}
	$('#placeYourOrder').removeAttr('disabled');
}



$('#placeYourOrder').click(function() {
	submitOrder();
});

$('#ship_to_new_address_form').find('input[name]').blur(function() {
	var value = $(this).val();
	if (value) {
		$(this).next().hide();
	}
});

$("#ship_to_new_address").click(function() {
	$("#pop_address").show();
});
$("#cancel_btn").click(function() {
	$("#pop_address").hide();
});
$("#ok_btn").click(function() {
	var valid = true;
	var datas = {};
	$('#addShipAddressForm').find('input[name]').each(function(){
		var value = $(this).val();
		var name = $(this).attr('name');
		if(name !='iid' && !value){
			valid = false;
			if('countryCode' == name){
				$(this).parent().siblings('label').show();
			}else{
				$(this).next('label').show();
			}
		}else{
			$(this).next('label').hide();
		}
		datas[name] = value;
	});
	if(!valid){
		return;
	}
	
	var address = {};
	$('#addShipAddressForm input[name]').each(function() {
		var name = $(this).attr('name');
		var value = $(this).val();
		address[name] = value || '';
	});
	var countryCodeInput = $("#defaultAddress input[name='countryCode']");
	var oldcountryCode = countryCodeInput.val();
	//存国家代码
	if(oldcountryCode != address.countryCode){
		countryCodeInput.val(address["countryCode"]);
		$("#defaultAddress span[name='countryCode']").html(address["countryCode"]);
		refreshShipMethod();
	}
	$("#defaultAddress span").each(function(i,e){
		var thenode = $(this);
		thenode.html(address[thenode.attr("name")]);
	});
	
	$("#pop_address").hide();
});
