$(function(){
	$(".currency").click(function(){
		$('.currency_box').addClass('show_y');
		$('#dialogs').css({display:'block'});
	});
	$(".close_currency").click(function(){
		$('.currency_box').removeClass('show_y');
		$('#dialogs').css({display:'none'});
	});
	
	
    //============================================================关闭顶部
    $(".close_tomtop").click(function(){
        $(this).parents(".top").slideUp();
    });
    //============================================================展开登入面板
    var login=$('input[name="M_TOMTOP_UUID"]').val()?true:false;    //是否登入
    $("#menu").click(function(){
        $("#dialogs").css({display:"block"});
        if(login){
            $(".sideslip_B").addClass("show_x");
        } else{
            $(".sideslip_A").addClass("show_x");
        };
    });
    //============================================================演示已登入
    $(".entertrue").click(function(){
        $("#dialogs").css({display:"block"});
        $(".sideslip_B").addClass("show_x");
    });
    //============================================================展开搜索面板
    $(".skip_search").click(function(){
        search();
    });
    $("#circle").find(".sector_2").click(function(){
        $("#circle").removeClass("scale_1");
        search();
    });
    function search(){
        $("#dialogs").css({display:"block"});
        $("#search").addClass("search_show");
        $("#search .main").addClass("fixed");
    }
    //============================================================显示清除按钮
    $(".seeks .seek").focus(function(){
        $(this).siblings(".clear_seek").css({opacity:1});
    }).blur(function(){
        $(this).siblings(".clear_seek").css({opacity:0});
    });
    //============================================================清除搜索框
    $(".seeks .clear_seek").click(function(){
        $(this).siblings(".seek").val(null);
    });
    //============================================================关闭搜索面板
    $("#quit_search").click(function(){
        $("#search .main").removeClass("fixed");
        $("#search").removeClass("search_show");
        $("#dialogs").css({display:"none"});
    });
    //============================================================跳转到前一个URL
    $("#previous_page").click(function(){
        window.history.back();
    });
    //============================================================返回顶部
    $("#nav_top").click(function(){
        $('html,body').animate({scrollTop:0});
    });
    $(".currency_content li a").click(function(){
        $(this).addClass("current").parents("li").siblings().find("a").removeClass('current');
    })
    //============================================================展开圆转转
    $("#opencircle").click(function(){
        $("#dialogs").css({display:"block"});
        $("#circle").addClass("scale_1");
    });
    //全局监听ajax。
    $(document).ajaxStart(function() {
        $(".loading").show();//有发送请求就显示loading加载动画。
        hideLoading(3000);
    });
    $(document).ajaxComplete(function() {
        $(".loading").hide();//请求完成的时候隐藏loading加载动画。
    });
    $(document).ajaxError(function() {
        $(".loading").hide();//出错的时候隐藏loading加载动画。
    });
    function hideLoading(delayTime){
        setTimeout(function(){
            $(".loading").hide();
        })
    }
});
//===========================================================点击黑底关闭弹窗
$(function(){
    $("#dialogs").click(function(){
        if($(".hide_t").hasClass("show_y")){
            $(".hide_t").removeClass("show_y");
        };
        if($(".hide_b").hasClass("show_y")){
            $(".hide_b").removeClass("show_y");
        };
        if($(".hide_l").hasClass("show_x")){
            $(".hide_l").removeClass("show_x");
        };
        if($(".hide_r").hasClass("show_x")){
            $(".hide_r").removeClass("show_x");
        };
        //==========关闭搜索
        if($("#search").hasClass("search_show")){
            $("#search .main").removeClass("fixed");
            $("#search").removeClass("search_show");
        }
        //==========圆转转
        if($("#circle").hasClass("scale_1")){
            $("#circle").removeClass("scale_1");
        }
        //关闭确认
        if($(".confirm_box").css("display")=="block"){
            $(".confirm_box").css("display",'none');
        }
        $("#dialogs").css({display:"none"});
    });
});
//============================================================滚动
$(document).scroll(function(){
    var scrollTop=$(document).scrollTop();
    if(scrollTop>0){
        $("#indextop .main").addClass("fixed");
    } else{
        $("#indextop .main").removeClass("fixed");
    }
    if(scrollTop>100){
        $("#nav_top").show()
    }else{
        $("#nav_top").hide();;
    }
});
function scroll(scrollContainer){//手机上touch滚动函数。
    var oliW=0;
    var uW=0;
    var iNub=$(scrollContainer).find("li").length;
    for(var i=0;i<iNub+1;i++){
        oliW=$(scrollContainer+" li").eq(i).outerWidth();
        uW+=oliW;
    }
    $(scrollContainer).width(uW+10);
}

//错误图片替换
function handleImgError(img)
{
    img.attr('src','/img/no_img.jpg');
}
//错误弹层
var timer=null;
function popError(msg)
{
    clearTimeout(timer);
    $(".toast").addClass("hint_slide").html(msg);
    timer=setTimeout(hideError,3000);
    function hideError()
    {
        $(".toast").removeClass("hint_slide");
    }
}

