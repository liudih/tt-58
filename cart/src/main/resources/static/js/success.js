$("#regForm").validate({
	submitHandler: function(form) {
		$.ajax({
	        type:"post",
	        cache:false,
	        url:"/cart/reg",
	        data:$(form).serialize(),
	        dataType:"json",
	        success: function(data) {
	        	if(data.result=="success"){
	        		location.href = "http://www.tomtop.com/member/login";
	        	}else{
	        		errorTip("Error", data.result);
	        	}
        	}
	      });
		return false;
	},
	//错误提示
    errorClass : "lable_error",
	errorElement : "label",
  	highlight: function (element,errorClass) { 
    },
    unhighlight: function (element) { 
    },
	errorPlacement: function(error, element) {
		if($(element).attr("name")=="agree"){
			$(element).next().after(error);
		}else{
			$(element).after(error);
		}
	},
	rules: {
		email: {
			required:true,
			email:true,
			noexist:true,
			maxlength:50
		},
		passwd: {
			required: true,
			minlength: 6,
			maxlength: 20,
			rangelength:[6,20]
		},
		confirm_password: {
			required: true,
			minlength: 6,
			maxlength: 20,
			equalTo: "#regpassword"
		}
  	},
	messages: {
		email: {
			required: "This field is required!",
			noexist:'An account already exists for this email address!',
			maxlength:"Please enter a valid email address!"
		},
		passwd: {
			required: "Provide a password!",
			minlength: jQuery.validator.format("Please enter at least {0} characters!"),
			maxlength: jQuery.validator.format("Please enter no more than {0} characters!"),
			rangelength: $.validator.format("Please enter a value between {6} and {20} characters long!"),
			nosame:"Your password and account information too coincidence, stolen risk, please change a password!"
		},
		confirm_password: {
			required: "Repeat your password!",
			minlength: jQuery.validator.format("Please enter at least {0} characters!"),
			maxlength: jQuery.validator.format("Please enter no more than {0} characters!"),
			equalTo: "Enter the same password as above!"
		},
		agree:{
			required: "Please make sure you agree to our Terms and Conditions!"
		},
		captcha:{
			required: "Please enter the verification code!"
		}
	},
  	/*showErrors:function(errorMap,errorList) {
        console.log(errorMap);
        console.log(errorList);
	},*/
	success: function (e,element) {
		e.removeClass('edit_error').addClass('suceess');
	}
});
//ajax获取邮箱是否注册
jQuery.validator.addMethod("noexist", function(value, element) { 
	var s= s || {};
	var flag=null;
	s.url= "/cart/validEmail"+"?email="+value;
	s.data={};
	s.type='get';
	s.async=false;
	s.success=function(data){
		if(data){
			if(data.errorCode===1){
				flag=false;
			}else if(data.errorCode===0){
				flag=true;
			}
		}
	}
	$.ajax(s);
    var result=this.optional(element) || flag; 
    return result;
});


function errorTip(title, tip){
	var errorBox = $('#errorBox');
	if(errorBox.length == 0){
		var ele = [];
		ele.push('<div id="errorBox" style="display: block" class="blockPopup_box">');
		ele.push('<div class="ns_pop_box">');
		ele.push('<div class="btn_pop_close"></div>');
		ele.push('<div class="pop_title">');
		ele.push('<h3>'+title+'</h3>');
		ele.push('</div>');
		
		ele.push('<div class="pop_con">');
		ele.push('<p id="errorBoxTxt"></p>');
		ele.push('</div>');
		ele.push('<div class="pop_input_box">');
		//ele.push('<input type="button" class="pop_input_close" value="CLOSE">');
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
$(function(){
//	var url = "/loyalty/coupon/checkordernum?ordernumber=@ordernumber";
//	$.ajax({
//		url: url, 
//		type: 'GET',
//		dataType: 'json',
//		success: function(data){
//			if(data.result=="success"){
//				errorTip('@Messages("tomtop.sendcoupon")');
//			}
//		} 
//	});
})