//////下拉框公用
$(function(){
	$("body").click(function(e){
		$(".btn-group").removeClass("open");
		e.stopPropagation(); 
	})
	$(".dropdown-toggle").click(function(e){
		if($(this).parent(".btn-group").hasClass("open")){
			$(this).parent(".btn-group").removeClass("open");
		}else{
			$(".btn-group").removeClass("open");
			$(this).parent(".btn-group").addClass("open");
		}
		e.stopPropagation(); 
	})
})

////主导航js

$(function(){
	$(".subSecondA").hover(function(){
		var index = $(this).index();
		var thisSib = $(this).parents(".subSecond").siblings(".subThird");
		$(this).siblings().removeClass("subSecondAci");
		$(this).addClass("subSecondAci");
		thisSib.removeClass("thirdBlock");
		thisSib.eq(index).addClass("thirdBlock");
	})
})

////banner
$(document).ready(function()
{
	var interval=0;
	var now=0;
	var ready=true;
	var banners=$(".imgBanner");
	var bannerBox=$(".bannerwrap");
	var leftC =$(".bannerLeft_click");
	var rightC =$(".bannerRight_click");
	function playBanner()
	{
		clearInterval(interval);
		interval=setInterval(function()
		{
			//$("img.lazy").lazyload({event:'sporty'})
			if(banners.length <= 1) clearInterval(interval);
			next= now+1 >= banners.length ? 0 : now+1;
			var currObj=banners.eq(now);
			var nextObj=banners.eq(next);
			currObj.stop(true).fadeOut(1000);
			nextObj.stop(true).fadeIn(1000);
			
			$(".bannerPoint b").removeClass("point-on");
			$(".bannerPoint b").eq(next).addClass("point-on");
			now=next;
		},5000)
	};
	playBanner();
	leftC.hover(function(){
		$(this).addClass("leftArrAci");
	},function(){
		$(this).removeClass("leftArrAci");
	})
	rightC.hover(function(){
		$(this).addClass("rightArrAci");
	},function(){
		$(this).removeClass("rightArrAci");
	})
	$(".bannerPoint b").bind("click",function()
	{
		var index=$(this).prevAll("b").length;
		if(index == now) return;
		clearInterval(interval);
		
		var currObj=banners.eq(now);
		var nextObj=banners.eq(index);
		currObj.stop(true).fadeOut(1000);
		nextObj.stop(true).fadeIn(1000);
		
		$(".bannerPoint b").removeClass("point-on");
		$(this).addClass("point-on");
		now=index;
		playBanner();
	})
	bannerBox.mouseover(function(){
		leftC.fadeIn();
		rightC.fadeIn();
		clearInterval(interval);
	})
	bannerBox.mouseleave(function(){
		leftC.fadeOut();
		rightC.fadeOut();
		playBanner();
	})
	leftC.bind("click",function(){
		var index=$(".point-on").index();
		prev= index-1 < -1 ? banners.length : index-1;
		var currObj=banners.eq(now);
		var nextObj=banners.eq(prev);
		if(!ready)return;
		ready=false;
		currObj.stop(true).fadeOut(1000);
		nextObj.stop(true).fadeIn(1000,function(){ready=true;});
		$(".bannerPoint b").removeClass("point-on");
		$(".bannerPoint b").eq(prev).addClass("point-on");
		now=prev;
		playBanner();
	})
	rightC.bind("click",function(){
		var index=$(".point-on").index();
		next= now+1 >= banners.length ? 0 : now+1;
		var currObj=banners.eq(now);
		var nextObj=banners.eq(next);
		if(!ready)return;
		ready=false;
		currObj.stop(true).fadeOut(1000);
		nextObj.stop(true).fadeIn(1000,function(){ready=true;});
		$(".bannerPoint b").removeClass("point-on");
		$(".bannerPoint b").eq(next).addClass("point-on");
		now=next;
		playBanner();
	})
})

////move 左右点击切换产品
$(function(){
	//try{moveBox($(".moveWarp"));}catch(e){}    //logo 切换
	//try{moveBox($(".dealsWarp"));}catch(e){}   // 超级打折
	try{moveBox($(".topSellers"));}catch(e){}	//Top Sellers 
	try{moveBox($(".newArrivals"));}catch(e){} //新品
	try{moveBox($(".ItemsConsider"));}catch(e){} //More Items to Consider
	try{moveBox($(".topBandsWarp"))}catch(e){}
})

function moveBox(warp){
	var page = 1;
	var listWarp = warp;
	var leftC = listWarp.find(".moveLeftClick");
	var rightC = listWarp.find(".moveRightClick");
	var moveBox = listWarp.find(".moveBox");             //列表容器
	var warpW = listWarp.find(".moveHidden").width(); //容器宽度
	var moveList = listWarp.find(".moveList");
	var moveListW = moveList.outerWidth(true);           //列表宽度 包括margin
	var moveBoxW = moveListW*moveList.length;            //列表的总宽度
	var position = moveBox.position().left;               //容器运动距离
	var page_number = Math.ceil(moveBoxW/warpW);           //总共多少页 向上取整
	var page_n = listWarp.siblings().find(".page");        //当前第几页
	var page_s = listWarp.siblings().find(".pages");       //总共多少页
	var dragWarp = listWarp.find(".feed-scrollbar");       //拖拽容器
	
	moveBox.css({"width":moveBoxW})
	leftC.addClass("leftArrAci");
	rightC.addClass("rightArrAci");
	if(moveBoxW<=warpW){                    //如果移动距离小于容器宽度  左右按钮隐藏
		leftC.hide();
		rightC.hide();
		dragWarp.hide();                   //拖拽隐藏
	}
	// else{
		// rightC.addClass("rightArrAci");
	// }
	//moveBanner();
	rightC.bind("click",function(){
		//$("img.lazy").lazyload({event:'sporty'})
		if (!moveBox.is(":animated")) {      //判断是否在运动
			if(scrol.length>0){                  //判断是否有 scroll 拉条
				if(dragW>warpW-(scrol.position().left+dragW)&&parseInt(dragW)!=parseInt(warpW-(scrol.position().left))){      //拖拽按钮宽度 > 容器宽度 - 拉条距离左边的距离 +拖拽按钮宽度
					scrol.animate({
						left:'+='+(warpW-scrol.position().left-dragW)
					})
					moveBox.animate({
						left:-(moveBoxW-position-warpW)  //移动距离 = 总宽度 -移动的距离-容器宽度
					})//.find('img').trigger('sporty');
				}else if(parseInt(dragW)==parseInt(warpW-(scrol.position().left))){
					scrol.animate({
						left:0
					})
					moveBox.animate({
						left:0
					})
				}else{
					var mo = moveBox.position().left%moveListW;
					var smo = moveBox.position().left%(warpW/moveList.length);
					if(mo == 0){
						scrol.animate({
							left:'+='+(dragW)
						})
						moveBox.animate({
							left:'-='+(warpW)                   //否则 移动距离为 容器宽度
						})//.find('img').trigger('sporty');
					}else{
						scrol.animate({
							left:'+='+(dragW+smo)
						})
						moveBox.animate({
							left:'-='+(warpW+mo)                   //否则 移动距离为 容器宽度
						})//.find('img').trigger('sporty');
					}
					
				}
				var pages = scrol.position().left+1; 
				var num = Math.ceil(pages/dragW);
				if(num >= page_number && parseInt(dragW) != parseInt(warpW-(scrol.position().left))){                        //页码显示数字
					num=page_number;
					page_n.html(num);
				}else if(parseInt(dragW) == parseInt(warpW-(scrol.position().left))){
					page_n.html(1);
				}else{
					page_n.html(num+1);
				}
				// if(num>=page_number-1){
					// rightC.removeClass("rightArrAci");
				// }
			}else{
				if(page>=page_number-1){          //如果当前页面=总页面-1
					moveBox.animate({
						left:-(moveBoxW-position-warpW)  //移动距离 = 总宽度 -移动的距离-容器宽度
					}).find('img').trigger('sporty');
				}else{
					moveBox.animate({
						left:'-='+(warpW)                   //否则 移动距离为 容器宽度
					}).find('img').trigger('sporty');
				}
				page++;                                    //第几页计算
				if(page>=page_number){
					page=page_number;
					page_n.html(page);
				}else{
					page_n.html(page);
				}
				// if(page>=page_number){
					// rightC.removeClass("rightArrAci");
				// }
			}
			//leftC.addClass("leftArrAci");              //当前按钮
		}
	})
	leftC.bind("click",function(){
		if (!moveBox.is(":animated")) {
			if(scrol.length>0){                  //判断是否有 scroll 拉条
				if(scrol.position().left<dragW && scrol.position().left!=0){
					scrol.animate({
						left:0
					})          
					moveBox.animate({
						left:0  
					})
				}else if(scrol.position().left==0){
					scrol.animate({
						left:(warpW-dragW)
					})          
					moveBox.animate({
						left:-(moveBoxW-warpW)
					})
				}else{
					var mo = moveListW+moveBox.position().left%moveListW;
					var smo = warpW/moveList.length+moveBox.position().left%(warpW/moveList.length);
					if(mo == 0){
						scrol.animate({
							left:'-='+(dragW)
						})
						moveBox.animate({
							left:'+='+(warpW)                   
						})
					}else{
						scrol.animate({
							left:'-='+(dragW-smo)
						})
						moveBox.animate({
							left:'+='+(warpW-mo)                   
						})
					}
				}
				//判断第几页
				var pages = scrol.position().left; 
				var num = Math.ceil(pages/dragW);
				if(num<=1&&scrol.position().left!=0){
					num=1;
					page_n.html(num);
				}else if(scrol.position().left==0){
					page_n.html(page_number);
				}else{
					page_n.html(num);
				}
				// if(num<=1){
					// leftC.removeClass("leftArrAci");
				// }
			}else{
				if(page<=2){          
					moveBox.animate({
						left:0  
					})
				}else{
					moveBox.animate({
						left:'+='+(warpW)                   
					})
				}
				page--;                                 //第几页计算
				if(page<=1){
					page=1;
					page_n.html(page);
				}else{
					page_n.html(page);
				}
				// if(page<=1){
					// leftC.removeClass("leftArrAci");
				// }
			}
			//rightC.addClass("rightArrAci");
		}
	})
	
	///////////拖拽//////
	var scrolBox = listWarp.find(".feed-scrollbar");
	var scrol = listWarp.find(".feed-scrollbar-thumb");
	var dragW = warpW/(moveBoxW/warpW);                  //拖拽按钮宽度
	var disX = 0;
	scrol.css({"width":dragW});
	
	function mousemove(ev)
	{
		var scrolL = scrol.position().left;
		var oEvent = ev || event;
		var scrolBox_H = oEvent.clientX - disX;
		if(scrolBox_H < 0)
		{
			scrolBox_H = 0;
		}
		else if(scrolBox_H > scrolBox.width()-dragW)
		{
			scrolBox_H = scrolBox.width()-dragW;
		}
		
		var scale = scrolBox_H / (scrolBox.width()-dragW);
		
		moveBox.css({"left":-scale * (moveBoxW-warpW)});   // 产品移动
		scrol.css({"left":scrolBox_H});                    // scroll移动
		
		var pages = scrol.position().left; 
		var num = Math.ceil(pages/dragW);
		page_n.html(num+1);
		//拖拽判断 左右按钮点击当前情况
		// if(pages==0){
			// leftC.removeClass("leftArrAci");
		// }else{
			// leftC.addClass("leftArrAci");
		// }
		// if(pages==warpW-scrol.width()){
			// rightC.removeClass("rightArrAci");
		// }else{
			// rightC.addClass("rightArrAci");
		// }
	}
	function mouseup(ev)
	{
		this.onmousemove = null;
		this.onmouseup = null;
	    scrol.removeClass("block")
		if(scrol.releaseCapture)
		{
		   scrol.releaseCapture();
		}
		//$("img.lazy").lazyload({event:'sporty'})
	}
	
	scrol.mousedown(function(ev)
	{
        var oEvent = ev || event;
	    disX = oEvent.clientX-scrol.position().left;
	    scrol.addClass("block")
		if(scrol.setCapture)
		{
			scrol.onmousemove= mousemove;
			scrol.onmouseup = mouseup;
			scrol.setCapture();
		}
		else
		{
			document.onmousemove= mousemove;
			document.onmouseup = mouseup
		}
	    return false;
	})
}



/**
 * @desc 折扣轮播 
 * @desc 首页
 */
function moveBanner(moveWarp)
{
	var interval=0;
	var now=0;
	var listWarp = moveWarp;
	var leftC = listWarp.find(".moveLeftClick");
	var rightC = listWarp.find(".moveRightClick");
	var banners = moveWarp.find(".moveList")
	var moveW = banners.outerWidth(true);
	var moveBox = moveWarp.find(".moveBox")
	var page_s = listWarp.siblings().find(".pages");       //总共多少页
	var page_number = banners.length;           //总共多少页 向上取整
	var page_n = listWarp.siblings().find(".page");        //当前第几页
	page_s.html(page_number);
	moveBox.css({"width":moveW*banners.length})
	
	if(page_number<=1){
		leftC.hide();
		rightC.hide();
	}
	
	function playBanner()
	{
	$("img.lazy").lazyload({event:'sporty'})
		clearInterval(interval);
		interval=setInterval(function()
		{
			now++
			if(now>=banners.length){now=0}
			moveBox.animate({
				left:-moveW*now
			}).find('img').trigger('sporty');
			page_n.html(now+1);
			// if(now>=banners.length-1){
				// rightC.removeClass("rightArrAci");
			// }else if(now<=0){
				// leftC.removeClass("leftArrAci");
			// }
			// if(now>0){
				// leftC.addClass("leftArrAci");
			// }
			// if(now<banners.length-1){
				// rightC.addClass("rightArrAci");
			// }
		},7000)
	};
	playBanner()
	listWarp.hover(function(){
		clearInterval(interval);
		},function(){playBanner()
	})
	rightC.click(function(){
	$("img.lazy").lazyload({event:'sporty'})
		if (!moveBox.is(":animated")) {
			now++
			if(now>=banners.length){
				now=0;
				//rightC.removeClass("rightArrAci");
			}
				moveBox.animate({
					left:-moveW*now
				}).find('img').trigger('sporty');
			page_n.html(now+1);
		}
		//leftC.addClass("leftArrAci");
	})
	leftC.click(function(){
	$("img.lazy").lazyload({event:'sporty'})
		if (!moveBox.is(":animated")) {
			now--;
			if(now<0){
				now=banners.length-1;
				//leftC.removeClass("leftArrAci");
			}
				moveBox.animate({
					left:-moveW*now
				}).find('img').trigger('sporty');
			page_n.html(now+1);
		}
		//rightC.addClass("rightArrAci");
	})
};



//初始化 无缝滚动
// $(function(){
	// $(".scrollRightWarp").myScroll({
		// speed:3000, //数值越大，速度越慢
		// rowHeight:82 //li的高度
	// });
// });

//////图片加载
$(function() {
	$("img.lazy").lazyload();
  	 //$("img.lazy").lazyload({effect : "fadeIn"});
});


/**
 * 邮箱验证  
 * @param SIGN UP FOR SAVINGS!
 */
function ischeckemail(){
	var email = document.getElementById("subEmail").value;
	if (email != "") {
		var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
		isok= reg.test(email );
		if (!isok) {
			Dialog(function(){
				var con = $(this).parents(".pu_pop");
				con.fadeOut(function(){
					con.remove();
					$("#subEmail").focus();
				});
			},strFun("TT_language_"+jsLanguage)["tomtop.common.tip"],strFun("TT_language_"+jsLanguage)["tomtop.message.wrongEmail"]);
		}else{
			window.location.href="http://www.tomtop.com/loyalty/subscribe?email="+email;
		}
	}else{
		Dialog(function(){
			var con = $(this).parents(".pu_pop");
			con.fadeOut(function(){
				con.remove();
				$("#subEmail").focus();
			});
		},strFun("TT_language_"+jsLanguage)["tomtop.common.tip"],strFun("TT_language_"+jsLanguage)["tomtop.message.enterYourEmail"]);
	}
}

/**
 * 确认框
 * @param fun 点击确认后要执行的函数
 * @param contents 提示框显示的内容
 */
function Dialog(fun,titles,contents){
	//debugger;
	var params ={title : titles || '',content : contents || '',button1:strFun("TT_language_"+jsLanguage)["tomtop.product.cancel"],button2:strFun("TT_language_"+jsLanguage)["tomtop.product.ok"]};	
	params = $.extend({'position':{'zone':'center'},'overlay':true}, params);
	var id = 'dialogBox_' + Math.floor(Math.random() * 1e9);
	var markup = [
        '<div id="' + id + '"  class="pu_pop">',
			'<div class="pu_popWarp">',
				'<p class="pu_popTitle">',
					params.title,
				'</p>',
				'<div class="pu_popCon">',
					params.content,
				'</div>',
				'<p class="pu_popBtn">',
					'<a class="btn cancelC" href="javascript:;">',params.button1,'</a>',
					'<a class="btn btn-primary" tag="ok" href="javascript:;">',params.button2,'</a>',
				'</p>',
			'</div>',
			'<p class="empty"> </p>',
			'<div class="pu_popBlack"> </div>',
		'</div>'
    ].join('');
    $(markup).hide().appendTo('body').fadeIn();
    if($.isFunction(fun)){
    	$('#' + id).find('a[tag=ok]').click(fun);
    }
	$(".closePop").click(function(){
		$(".pu_pop").fadeOut(function(){
			$(this).remove();
		});
	})
	$(".pu_popBlack,.cancelC").click(function(){
		var con = $(this).parents(".pu_pop");
		con.fadeOut(function(){
			con.remove();
		});
	})
}


//class ajax加载产品
$(function(){
	var classMouse = $(".categoriesClass").children("li");
	var listBloxk = $(".categoriesList");
	classMouse.click(function(){
		var index = $(this).index();
		var ids = $(this).attr("cat_id");
		var intList =$(".categoriesList").eq(index);
		var isNone = intList.html();
		$(this).siblings().removeClass("categoriesAci");
		$(this).addClass("categoriesAci");
		
		if(intList.children().length==0){                                      //判断是否有内容
			if(intList.parent().find(".loading-ajax").length==0){intList.parent().append("<div class='loading-ajax' />");}
			$.ajax({
			   type: "GET",
			   cache : false,
			   url: domain+"index.php?r=site/ajax",
			   timeout : 10000,
			   data: "cate_id="+ids,
			   dataType:'html',
			   success: function(html){
				 intList.html(html);
			   	 intList.find('img').trigger('sporty');
			   	 intList.find('.lazy').lazyload({event:'sporty'});
				 listBloxk.hide();
				 listBloxk.eq(index).show();
				 $(".loading-ajax").remove();
			   }
			   
			});
		}else{
			listBloxk.hide();
			listBloxk.eq(index).show();
		}
	})
})
/////======================WEB-history==================================
$(function(){
 if(getCookie("WEB-history") && $("#mightLike").length == 1){
	 function webHistory(){
		 $contentLoadTriggered = true;
			 var likeIds = getCookie("WEB-history");
			 var likeId = likeIds.split(",");
			 $.ajax({
			    type: "GET",
				cache : false,
			    url: domain+"index.php?r=site/ajaxyoumightlike&position=top",
			    data: "listing_id="+likeId[0],
			    dataType:'html',
				 success: function (html) 
				 {
				   	  $("#mightLike").html(html)
					  $(function(){try{moveBox($(".mightLike"))}catch(e){};})
					  //eachImg($("#mightLike"))
				   	 $("#mightLike").find('img').trigger('sporty')
				   	 $("#mightLike").find('.lazy').lazyload({event:'sporty'})
					 $contentLoadTriggered = true;
				 }
			 });
	 }
	 $contentLoadTriggered = false;
	 $(window).scroll(function(){
		 var scr = $("#mightLike").offset().top;
		 if(scr >= $(window).scrollTop() && scr <($(window).scrollTop()+$(window).height()) && $contentLoadTriggered == false)
		 {
			 webHistory()
		 }
	 });
	if($("#mightLike").offset().top-$("html,body").scrollTop()<$(window).height() && $contentLoadTriggered == false){
		webHistory()
	}
 }
})
/////======================首页 右边循环滚动==================================
$(function(){
	 $(".scrollRightWarp").myScroll({
		speed:3000, //数值越大，速度越慢
		rowHeight:82 //li的高度
	});
	 // function scrollRightWarp(){
		 // $.ajax({
		    // type: "GET",
		    // cache : false,
		    // url: domain+"index.php?r=site/rorders",
		    // dataType:'html',
			 // success: function (html) 
			 // {
			   	 // $(".scrollRightWarp").html(html)
			   	 // $(".scrollRightWarp").myScroll({
					// speed:3000, //数值越大，速度越慢
					// rowHeight:82 //li的高度
				// });
			 // }
		 // });
	 // }
	 // scrollRightWarp()
})
/////======================Deals==================================
$(function(){
	 function dayDeals(){
		 $.ajax({
		    type: "GET",
			cache : false,
		    url: domain+"index.php?r=site/daily",
		    dataType:'html',
			 success: function (html) 
			 {
			   	 $(".dailyDeals").html(html)
			   	 $("img.lazy").lazyload();
			   	 moveBanner($(".dealsWarp"));
			   	 try{
					// var myDate = new Date();
					// var dates = myDate.toLocaleDateString();
					var dates = $("#serverTime").text();
					$('.countdown').downCount({
						date: dates+' 23:59:59'
						//offset: +8
					});
				}catch(e){}
			 }
		 });
	 }
	 dayDeals();
})
/////======================rightBanner==================================
// $(function(){
	 // function rightBanner(){
		 // $.ajax({
		    // type: "GET",
		    // url: domain+"index.php?r=site/hot",
		    // dataType:'html',
			 // success: function (html) 
			 // {
			   	 // $(".rightBanner").html(html)
			   	 // $(".rightBanner").find('img').trigger('sporty')
			   	 // $(".rightBanner").find('.lazy').lazyload({event:'sporty'})
			 // }
		 // });
	 // }
	 // rightBanner()
// })
/////======================topBands==================================
// $(function(){
	 // function topBands(){
		 // $.ajax({
		    // type: "GET",
		    // url: domain+"index.php?r=site/brand",
		    // dataType:'html',
			 // success: function (html) 
			 // {
			   	 // $("#index_top_bands").html(html)
				 // $(function(){try{moveBox($(".topBandsWarp"))}catch(e){};})
				 // $("img.lazy").lazyload();
			 // }
		 // });
	 // }
	 // topBands()
// })
