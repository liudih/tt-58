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
		$("#pointContent").hide();
		return;
	}
	 $.ajax({ 
	  type: 'GET',  
        url: "/loyalty/getpoint",
        dataType:"json",
        success:function(data){
			if(data.ret == 1){
				$("#pointContent").show();
				var point=data.data;
				if(point<=0){
					$("#pointContent").hide();
				}
				$("#cart_usablepoint").text(point);
	        }else{
	        	$("#pointContent").hide();
	        }
        }
  })
}


//获取当前优惠
function getUsableCoupon(){
	var islogin = $("#islogin").val();
	if(islogin=="false"){
		$("#couponContent").hide();
		return;
	}
 	var storageid = $("#cartlist_ul .radioChecked:eq(0)").attr("storageid");
	$.ajax({ 
		type: "GET",  
	    url: "/loyalty/coupon?storageid="+storageid,
	    dataType: "json",
	    success:function(data){
	    	if(data.ret==1){
	    		$("#couponContent").show();
				var listdata=data.data;
				if(listdata==null || listdata.length==0){
					$("#couponContent").hide();
					return;
				}else{
					$("#couponContent").show();
				}
	        	var listHtml='';
	        	var curr = $("#loyaltyCurrency").val();
	            for(var i=0 ; i < listdata.length ; i++){ 
	            	var isCash = listdata[i].isCash;
	            	var pprice = parseFloat(listdata[i].value);
	            	var showval = listdata[i].value;
	            	var showtitle = showval+' '+listdata[i].unit;
	            	if(isCash){
	            		showval = (pprice<1?pprice*100+"%":(listdata[i].unit=='JPY'?parseInt(listdata[i].value):pprice.toFixed(2)));
	            		showtitle = curr + ' ' + showval;
	            	}
	            	listHtml += '<li class="clearfix" code="'+listdata[i].code+'" onclick="tapCoupon(this);" >'
                        +'<div class="content">'
                        +'<span class="off">'+showtitle+'</span>'
                        +'<span class="adorn"></span>'
                        +'<span class="order">'
                        +'<p>'+showtitle+' orders of '+curr+ listdata[i].minAmount+' or more</p>'
                        +'<em>Valid date: '+listdata[i].validEndDate+'</em>'
                        +'</span>'
                        +'</div>'
                        +'<div class="chooseOneBox lineBlock">'
                        +'<label><div class="radio lineBlock "><i class="icon_check"></i></div></label>'
                        +'</div></li>';
	            }  
	            $('#couponLiShow').html(listHtml);
	            $("#checkout_coupon_apply").show();
	            $("#checkout_coupon_canel").hide();
	            
	        	$("#couponLiShow li").click(function(){
	        		$("#dialogs").hide();
	    		 	$(".coupons").removeClass("show_y");
	    	    	$("#couponText").val($(this).attr("code"));
	    	    	//$("#couponText").attr("disabled","disabled");
	    	    });
	    	}
    	}
    });
}

function applyPoint(){
	var islogin = $("#islogin").val();
	if(islogin=="false"){
		return;
	}
	var cost = $('#checkout_point_input').val();
	if(!(cost&&cost>0)){
		return;
	}
	var storageid = $("#cartlist_ul .radioChecked:eq(0)").attr("storageid");
	 $.ajax({ 
		  type: 'GET',  
	        url: "/loyalty/applypoint?storageid="+storageid,
	        dataType:"json",
	        data:{
	        	"point": cost
	        },
	        success:function(data){
	        	if(data.result=='success'){
	        		window.location.reload();
    	   		}else{
    	   			popError(data.result);
    	   		}
	        }
	  })
};

//应用优惠卷
function applyCoupon(){
	var code = $("#couponText").val();
	if(!code){
		return;
	}
	var storageid = $("#cartlist_ul .radioChecked:eq(0)").attr("storageid");
	 $.ajax({ 
 		  type: 'GET',  
 	        url: "/loyalty/applycoupon?storageid="+storageid,
 	        data:{
 	        	"code": code
 	        },
 	        success:function(data){
 	        	if(data.result=='success'){
 	        		window.location.reload();
 	        	}else {
					popError(data.result);
				}
 	        }
 	  });
}

function undoPoint(){
	$.ajax({ 
		type: "GET",  
	    url: "/loyalty/undopoint",
	    dataType: "json",
	    success:function(data){
	    	if(data.result=="success"){
	    		location.reload();
	    	}
	    }
	});
}


//应用推广码
function applyPromo(){
	var promo=$("#checkout_promo_input").val().trim();
	if(!promo){
		return;
	}
	var storageid = $("#cartlist_ul .radioChecked:eq(0)").attr("storageid");
	$.ajax({
		type : 'GET',
		data : {code:$('#checkout_promo_input').val()},
		url : "/loyalty/applypromo?storageid="+storageid,
		success : function(data) {
			if (data.result == "success") {
				location.reload();
			}else{
				popError(data.result);
			}
		}
	})
};

function undoPrefer(){
	$.ajax({ 
		type: "GET",  
	    url: "/loyalty/undoprefer",
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
 	        url: "/loyalty/allprefer?storageid="+storageid,
 	        async:false,
 	        dataType: "json",
 	        success:function(data){
 	        	if(data.result=="success"){
 	        		var listdata=data.data;
 	        		var discounttotal = 0;
 	        		if(listdata.length>0){
 	        			$(".have_code p").click();
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
 	        				$("#promoContent").hide();
 	        				$("#couponContent").show();
 	        				
 		     	        	$("#checkout_coupon_canel").show();
 		     	        	$("#checkout_coupon_apply").hide();
 		     	        	$("#couponText").val(cell.code).attr("disabled","disabled");
 	        			 }
 	        			 else if(cell.type == 'promo'){
 	        				$("#couponContent").hide();
 	        				$("#checkout_promo_apply").hide();
 		     	        	$("#checkout_promo_cancel").show();
 		     	        	$("#checkout_promo_input").val(cell.code).attr("disabled","disabled");
 	        			 }
 	        			 else if(cell.type == 'point'){
 	        				$("#pointContent").show();
  	        				$("#checkout_point_apply").hide();
  		     	        	$("#checkout_point_cancel").show();
  		     	        	$("#checkout_point_input").val(parseFloat(cell.discount).toFixed(2)).attr("disabled","disabled");
  		     	        	$("#pointContent .points").hide();
  	        			 }
 	        		 }  
 	        		 if(!$("#checkout_point_input").attr('disabled')){
 	        			 getUsablePoint();
 	        		 }
 	        		 if(!$("#checkout_promo_input").attr('disabled') && !$("#couponText").attr('disabled')){
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

$(function(){
	getCurrentPrefer();
});

//选中优惠券
function tapCoupon(node){
	var jnode = $(node);
	$("#couponLiShow .radio").removeClass("radioChecked");
	jnode.find(".radio:eq(0)").addClass("radioChecked");
}