var country = new Country();
country.init("/country");
$("input[name=country_filter]").keyup(function() {
	country.search(this);
});



	//login-------------
	$("#loginForm").validate({
		//debug:true,
		submitHandler: function(form) {
			$.ajax({
		        type:"post",
		        cache:false,
		        url:"/cart/login",
		        data:$(form).serialize(),
		        dataType:"json",
		        success: function(data) {
		        	if(data.result=="success"){
		        		//pophtml("Success","Login Successful");
		        		$("#returnwrong").hide();
		        		location.reload();
		        	}else if(data.result=="accountwrong"){
		        		$("#returnwrong").show();
		        	}else{
		        		pophtml("Error",data.result);
		        	}
	        	}
		      });
			return false;
		},
	    errorClass : "edit_error",
		errorElement : "label",
	  	highlight: function (element,errorClass) { 
			//$(element).next().find("span:eq(0)").html(error.html());
			//$(element).next().show();
	    },
	    unhighlight: function (element) { 
	    	//$(element).next().hide();
	    },
		errorPlacement: function(error, element) {
			$(element).after(error);
		},
		rules: {
			email: {
				required:true,
				email:true,
				maxlength:50
			},
			password: {
				required: true,
				minlength: 6,
				maxlength:20,
				rangelength:[6,20]
			}
		},
		messages: {
			email: {
					required:"This field is required!",
					email: "The email you've entered is invalid. Please check your email and try again.",
					maxlength:"Please enter a valid email address!"
				},
			password: {
					required:"Provide a password.",
					minlength: jQuery.validator.format("Enter at least {0} characters."),
					maxlength: jQuery.validator.format("Please enter no more than {0} characters!"),
					rangelength: $.validator.format("Please enter a value between {6} and {20} characters long!")
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
	//reg---------------
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
		        		pophtml("Success","Successful registration, please login again!");
		        		$("#regForm")[0].reset();
		        		$(".ControlNa li:eq(0)").click();
		        	}else{
		        		pophtml("Error",data.result);
		        	}
	        	}
		      });
			return false;
		},
	    errorClass : "edit_error",
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
			},
			agree:{
				required: true
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

	function loginSubmit(){
		$("#loginForm").submit();
	}
	function closethis(node){
		$("#loginPopWarp").hide();
	}