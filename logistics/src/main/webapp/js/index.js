$(function(){
	var cacheUrl="/report/base/123";
	var getUrl="/report/get/123";
	var getUrl="/report/get/123";
	var postUrl="/report/post/123";
	var postData="{'name':'test'}";
	var deleteUrl="/report/delete/123";
	var functionUrl="/report/function/get/test";
	$("#addData").bind("click",function(){
		testProduct(functionUrl);
	});
})
function testProduct(url,params){
	if($.trim(params)==""){
		$.ajax({
			type: "GET",
			url: url,
			dataType:'text',
			contentType: 'application/json;charset=UTF-8',
			success: function(msg){
				alert( "Data : " + msg );
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				alert("error1");
			}
		});

	}else if($.trim(params)=="d"){
		$.ajax({
			type: "delete",
			url: url,
			dataType:'text',
			contentType: 'application/json;charset=UTF-8',
			success: function(msg){
				//alert( "Data Saved: " + msg );
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				alert("delete error");
			}
		});
	}else{
		
		$.ajax({
			type: "POST",
			url: url,
			data: JSON.stringify(params),
			dataType:'text',
			contentType: 'application/json;charset=UTF-8',
			success: function(msg){
				alert( "Data Saved: " + msg );
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				alert("error2");
			}
		});
	}
}