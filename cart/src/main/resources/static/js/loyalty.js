
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

function getUsablePoint(){
	var islogin = $("#islogin").val();
	if(islogin=="false"){
		return;
	}
	 $.ajax({ 
	  type: 'GET',  
        url: "/loyalty/getpoint",
        cache:false,
        dataType:"json",
        success:function(data){
				if(data.ret == 1){
					$("#checkout_point_div").show();
					var point=data.data;
					$("#cart_usablepoint").text(point);
		           };
        }
  })
}

//获取当前优惠
function getUsableCoupon(){
	var islogin = $("#islogin").val();
	if(islogin=="false"){
		return;
	}
 	 var storageid = $("#cartlist_ul .sel").attr("storageid");
		$.ajax({ 
			type: "GET",  
		    url: "/loyalty/coupon?storageid="+storageid+"&time="+new Date(),
		    cache:false,
		    dataType: "json",
		    success:function(data){
		    	if(data.ret==1 && data.data.length>0){
		    		$("#checkout_coupon_div").show();
					var listdata=data.data;
					$("#coupon_input_show").hide();
		        	var listHtml='';  
		            for(var i=0 ; i < listdata.length ; i++){ 
		            	var isCash = listdata[i].cash;
		            	if(isCash){
		            		var pprice = parseFloat(listdata[i].value);
		            		listHtml += "<li " +"code="+listdata[i].code+">"+
			                (pprice<1?pprice*100+"%":(listdata[i].unit=='JPY'?parseInt(listdata[i].value):pprice.toFixed(2)))+" "+listdata[i].unit+
			                "</li>";
		            	}else{
		            		listHtml += "<li "+"code="+listdata[i].code+">"+listdata[i].value+" "+listdata[i].unit+"</li>";
		            	}
		            }  
		           
		            $("#checkout_coupon_apply").css("display","inline-block");
		        	$('#checkout_coupon_insert').html(listHtml);
		        	
		        	$("#checkout_coupon_insert li").click(function(){
	        		 	$(this).parents(".have_code_select").find(".current_list").hide();
	        	    	$("#checkout_coupon_code").text($(this).attr("code"));
	        	    });
		        	$("#checkout_coupon_code").unbind().click(function(){
 	        	        $(this).parent().find(".current_list").toggle();
 	        	    });
		    	}
	    	}
	    });
}

function undoPrefer(){
	$.ajax({ 
		type: "GET",  
	    url: "/loyalty/undoprefer"+"?time="+new Date(),
	    dataType: "json",
	    success:function(data){
	    	if(data.result=="success"){
	    		location.reload();
	    	}
	    }
	});
}

function undoPoint(){
	$.ajax({ 
		type: "GET",  
	    url: "/loyalty/undopoint"+"?time="+new Date(),
	    dataType: "json",
	    success:function(data){
	    	if(data.result=="success"){
	    		location.reload();
	    	}
	    }
	});
}





//获取当前用户所有优惠
function getCurrentPrefer(){
	var storageid = $("#cartlist_ul .sel").attr("storageid");
	storageid = storageid||1;
	 $.ajax({
 		  type: 'GET',  
 	        url: "/loyalty/allprefer",
 	        cache:false,
 	        async:false,
 	        dataType: "json",
 	        success:function(data){
 	        	if(data.result=="success"){
 	        		var listdata=data.data;
 	        		var discounttotal = 0;
 	        		if(listdata.length>0){
 	        			$(".have_code_input").show();
 	        			$(".have_code p span").text("-");
 	        		}
 	        		 for(var i=0 ; i < listdata.length ; i++){ 
 	        			var cell = listdata[i];
 	        			
	     	        	var total = $("#grandTotal").text();
	     	        	var discont = cell.discount;
	     	        	total = total.replace(/,/g,'');
 		     	  		//检查是否有小数点
 		     	  		if(total.indexOf('.') == -1){
 		     	  			total = parseInt(total);
 		     	  			discont = parseInt(discont);
 		     	  			discounttotal += discont;
 		     	  			total = total + discont;
 		     	  			$("#grandTotal").text(total);
 		     	  			$("#discount_total").text(discounttotal);
 		     	  		}else{
	 		     	  		total = parseFloat(total);
	 		     	  		discont = parseFloat(discont);
	 		     	  		discounttotal += discont;
	 		     	  		total = total + discont;
 		     	  			//保留两位小数
	 		     	  		total = Math.round(total * 100) / 100;
	 		     	  		$("#discount_total").text(toDecimal2(discounttotal));
 		     	  			$('#grandTotal').text(toDecimal2(total));
 		     	  		}
        				if(cell.type == 'coupon'){
 	        				$("#checkout_promo_div").hide();
 	        				$("#checkout_coupon_div").show();
 		     	        	$("#checkout_coupon_canel").show();
 		     	        	$("#checkout_coupon_apply").hide();
 		     	        	$("#checkout_coupon_code").text(cell.code);
 		     	        	$("#checkout_coupon_code").unbind();
 	        			 }
 	        			 else if(cell.type == 'promo'){
 	        				$("#checkout_coupon_div").hide();
 	        				$("#checkout_promo_apply").hide();
 		     	        	$("#checkout_promo_cancel").css("display","inline-block");
 		     	        	$("#checkout_promo_input").val(cell.code).attr("disabled","disabled");
 	        			 }
 	        			 else if(cell.type == 'point'){
 	        				$("#checkout_point_div").show();
  	        				$("#checkout_point_apply").hide();
  		     	        	$("#checkout_point_cancel").css("display","inline-block");
  		     	        	$("#checkout_point_input").val(cell.discount).attr("disabled","disabled");
  		     	        	$("#checkout_point_div .points_num").hide();
  	        			 }
		            }  
 	        		 if($('#checkout_point_div').is(':hidden')){
 	        			 getUsablePoint();
 	        		 }
 	        		 if(!$("#checkout_promo_input").attr('disabled')&&
 	        				 $('#checkout_coupon_code').text()=='Coupon Code'){
 	        			getUsableCoupon();
 	        		 }
 	        	}else{
 	        		$("#checkout_promo_input").attr("disabled",false);
 	        		$("#checkout_point_input").attr("disabled",false);
 	        		getUsablePoint();
 	        		getUsableCoupon();
 	        	}
 	        }
 	  })
}


//应用推广码
//$("#checkout_promo_apply").click(function(){
function applyPromo(){
	var promo=$("#checkout_promo_input").val().trim();
	if(!promo){
		return;
	}
	var storageid = $("#cartlist_ul .sel").attr("storageid");
	$.ajax({
		type : 'GET',
		dataType:"json",
		data : {code:$('#checkout_promo_input').val()},
		url : "/loyalty/applypromo?storageid="+storageid+"&time="+new Date(),
		success : function(data) {
			if (data.result == "success") {
				location.reload();
			}else{
				$("#checkout_promo_div .error_p").text(data.result).show();
			}
		}
	})
};

//应用优惠卷
function applyCoupon(){
	var code = $("#checkout_coupon_code").text();
	if(!code){
		return;
	}
	var storageid = $("#cartlist_ul .sel").attr("storageid");
	 $.ajax({ 
 		  type: 'GET',  
 	        url: "/loyalty/applycoupon?storageid="+storageid+"&time="+new Date(),
 	        data:{
 	        	"code": code
 	        },
 	        success:function(data){
 	        	if(data.result=='success'){
 	        		window.location.reload();
 	        	}else {
					$("#checkout_coupon_div .error_p").html(data.result).show();
				}
 	        }
 	  });
}


function applyPoint(){
	var islogin = $("#islogin").val();
	if(islogin=="false"){
		$(".blockPopup_box").show();
		return;
	}
	var cost = $('#checkout_point_input').val();
	if(!(cost&&cost>0)){
		return;
	}
	var storageid = $("#cartlist_ul .sel").attr("storageid");
	 $.ajax({ 
		  type: 'GET',  
	        url: "/loyalty/applypoint?storageid="+storageid+"&time="+new Date(),
	        dataType:"json",
	        data:{
	        	"point": cost
	        },
	        success:function(data){
	        	if(data.result=='success'){
	        		window.location.reload();
    	   		}else{
    	   			var error = $("#checkout_point_div .error_p");
    	   			error.text(data.result);
    	   			error.show();
    	   		}
	        }
	  })
};
    
    
$(function(){
	getCurrentPrefer();
    //getCurrentCoupon();
});


