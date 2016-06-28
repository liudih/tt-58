
var LoadingTime=null;
function hideLoading(delayTime){
    clearInterval(LoadingTime);
    LoadingTime=setTimeout(function(){
        $(".loading").hide();
    },delayTime);
};
//============================================================判断当前是大小写
function fnCapslock(e,obj){
    if(e.keyCode>=65 & e.keyCode<=90){
        var Sval=obj.val();
        Sval=Sval.substring(Sval.length-1);
        if(/[A-Z]/.test(Sval)){
            popError('Caps Lock');
        }else if(/[a-z]/.test(Sval)){
            $(".toast").removeClass("hint_slide");
        }
    }
};
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
$(document).scroll(function(){//顶部固定。
    var scrollTop=$(document).scrollTop();
    if(scrollTop>0){
        $("#indextop .main").addClass("fixed");
    } else{
        $("#indextop .main").removeClass("fixed");
    }
    if(scrollTop>100){//返回到顶部。
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
//获取cookie
function getCookie(name) 
{ 
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
 
    if(arr=document.cookie.match(reg))
 
        return unescape(arr[2]);
    else 
        return null; 
} 

//获取购物车数量
function countCart()
{
    var count = 0;
    var plist = getCookie('plist');
    plist = JSON.parse(plist);
    for(var i in plist)
    {
        for(var j in plist[i])
        {
            count++;
        }
    }
    var dom = $(document).find('.cart-quantity');
    for (var k = 0; k < dom.length; k++) 
    {
        dom.eq(k).text(count);
    }
    return count;
}
countCart();

//设置cookie
function setCookies(cname, cvalue, exdays) 
{
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires + "; domain=.tomtop.com";
}
//获取地址栏参数
function getQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}
var SysSecond;
var InterValObj;
function SetRemainTime() {
    if (SysSecond > 0) {
        SysSecond = SysSecond - 1;
        var second = Math.floor(SysSecond % 60);
        var minite = Math.floor((SysSecond / 60) % 60);
        var hour = Math.floor((SysSecond / 3600) % 24);
        var day = Math.floor((SysSecond / 3600) / 24);
        $("#cutdown_time").html(toDouble(hour) + ":" + toDouble(minite) + ":" + toDouble(second));
    } else {
        window.clearInterval(InterValObj);
    }
}
$(function() {
    SysSecond = parseInt($('#utc-time').val());
    InterValObj = window.setInterval(SetRemainTime, 1000);
})
function toDouble(n){//不足两位，补全一位。
    if(n<10){
        return "0"+n;
    }else{
        return n;
    }
}
//////////////////////////////////////////////倒计时功能结束。

///////////////////////////////////////////////////////后端写的头部的js开始。
//邮箱验证
function isEmail(str){
    var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
    return reg.test(str);
}
//错误提示
function showError(myclass, mytext)
{

    $("." + myclass).find('p.hint').addClass('error').text(mytext);
    $("." + myclass).find('input.p').addClass('error');
}
//隐藏错误
function hideError(myclass)
{
    $('.' + myclass).find('p.hint').removeClass('error').text('');
    $('.' + myclass).find('input.p').removeClass('error');
}
//弹出登录框
function popLogin()
{
    $("#dialogs").css({display:"block"});
    $(".popup_login").addClass("show_y");
}

var is_login = false;
$(function(){
    is_login = $('input[name="M_TOMTOP_UUID"]').val()?true:false;//是否登入
})
//下载app
function downLoadApp()
{
    var ua = navigator.userAgent.toLowerCase();
    if (/iphone|ipad|ipod/.test(ua))
    {
        // alert("iphone");
        window.location.href = 'http://itms-apps://itunes.apple.com/app/1027264288';
    }
    else if (/android/.test(ua))
    {
        // alert('android');
        window.location.href = 'http://www.tomtop.com/img/downloads/tomtop_app.apk';
    }
}
//加载热搜关键字
function getHotKey()
{
    if (!$('.hot-key-box dd').size())
    {
        $.ajax({
            url:'/index.php?r=product/hotkey',
            type:'get',
            dataType:'json',
            success:function(data)
            {
                if (data.ret == 1)
                {
                    var list = data.list;
                    _showLists(list);
                }
            }
        })
    }
}
//搜索联想
function getGuess()
{
    var word = $('.search-input').val();
    if(word)
    {
        $.ajax({
            url:'/index.php?r=search/guess',
            type:'get',
            dataType:'json',
            data:{'word':word},
            success:function(data)
            {
                if (data.ret == 1)
                {
                    var list = data.list;
                    _showLists(list);
                }
            }
        })
    }
}
//赋值搜索下拉列表
function _showLists(list)
{
    $('.hot-key-box').html('');
    if (list.length > 0)
    {
        for (var i = 0; i < list.length; i++)
        {
            var s = list[i].keyword === undefined ? list[i] :  list[i].keyword;
            $('.hot-key-box').append('<dd><a href="/product?q='+s+'"><i>'+(i+1)+'</i>'+s+'</a></dd>');
        }
    }
}
$(function(){
//失去焦点隐藏错误
$('input.p').blur(function(){
    $(this).removeClass('error').parents('li').find('p.hint').removeClass('error').text('');
});
//搜索框回车| 联想
    $(".search-input").keyup(function (event) {
        if(event.keyCode==13)
        {
            window.location.href = '/product?q='+$('.search-input').val();
        }
        else
        {
            getGuess();
        }
    })

    //点击搜索
    $('.click-search-btn').click(function(){
        window.location.href = '/product?q='+$('.search-input').val();
    })
    //弹框登录
    $('.pop-login-btn').click(function(){
        var obj = {};
        obj.email = $('.pop-login input[name="email"]').val();
        obj.pwd = $('.pop-login input[name="pwd"]').val();
        obj.type='login';
        if (!obj.email)
        {
            showError('login-email-li', 'Email Can Not Empty!');
        }else if(!isEmail(obj.email)){
            showError('login-email-li', 'Email is Error!');
        }
        else if(!obj.pwd)
        {
            showError('login-pwd-li', 'Password Can Not Empty!');
        }
        else
        {
            hideError('login-email-li');
            hideError('login-pwd-li');
            $.ajax({
                url:'/index.php?r=join/login',
                type:'post',
                dataType:'json',
                data:obj,
                success:function(response)
                {
                    if (response.ret == 1)
                    {
                        is_login = true;
                        $('.popup_login').removeClass('show_y');
                        $("#dialogs").css({display:"none"});
                    }
                    else
                    {
                        popError(response.errMsg);
                    }
                }
            });
        }
    })
})
///////////////////////////////////////////////////////后端写的头部的js结束。




$(function(){
    //============================================================关闭顶部
    $(".close_tomtop").click(function(){
        $(this).parents(".top").slideUp();
    });
    //============================================================展开登入面板
    //var login=$('input[name="M_TOMTOP_UUID"]').val()?true:false;    //是否登入
    var isLogin = false;
    $("#menu").click(function(){
        var uuid = getCookie('TT_UUID');
        var loginTag = $('input[name="ajax-login-tag"]');
        if(isLogin)
        {
            showSlide();
        }
        else if (uuid && !isLogin) 
        {
            $.ajax({
                url: '/index.php?r=site/loginfo',
                dataType: 'json',
                success:function(res)
                {
                    if (res.ret == 1) 
                    {
                        var data = res.data;
                        var cfirstname = data.userInfo.cfirstname;
                        var clastname = data.userInfo.clastname;
                        var cemail = data.userInfo.cemail;
                        var showName = cfirstname || clastname ? cfirstname + clastname : cemail;
                        var orderAll = data.orderStatistics.all;
                        var orderPending = data.orderStatistics.pending;
                        var orderDispatched = data.orderStatistics.dispatched;
                        $('.sideslip_B .user_name').text(showName);
                        $('.sideslip_B .order-all i').text(orderAll);
                        $('.sideslip_B .order-payment i').text(orderPending);
                        $('.sideslip_B .order-dispatched i').text(orderDispatched);
                        isLogin = true;
                    }
                    showSlide();
                }
            })
        }
        else
        {
            showSlide();
        }
    });
    //飘出左侧栏
    function showSlide()
    {
        $("#dialogs").css({display:"block"});
        if (isLogin) 
        {
            $('.sideslip_B').addClass('show_x');
        }
        else
        {
            $('.sideslip_A').addClass('show_x');
        }
    }
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
    //============================================================展开圆转转
    $("#opencircle").click(function(){
        $("#dialogs").css({display:"block"});
        $("#circle").addClass("scale_1");
    });
    //全局监听ajax。
    $(document).ajaxStart(function() {
        $(".loading").show();//有发送请求就显示loading加载动画。
        hideLoading(5000);
    });
    $(document).ajaxComplete(function() {
        $(".loading").hide();//请求完成的时候隐藏loading加载动画。
    });
    $(document).ajaxError(function() {
        $(".loading").hide();//出错的时候隐藏loading加载动画。
    });
    //============================================================判断当前是大小写
    $(':password').keyup(function(e){
        fnCapslock(e,$(this));
    });

});