$(function(){
    //============================================================产品图轮播
    bannerscroll();
    //============================================================分享
    $(".share_but").click(function(){
        $("#dialogs").css({display:"block"});
        $(".share").addClass("show_y");
    });
    $(".share li").click(function(){
        var sUrl=window.location.href;
        if($(this).hasClass("facebook")){
            window.open("https://api.addthis.com/oexchange/0.8/forward/facebook/offer?url=" + sUrl + "&pubid=ra-5688799e3f7e90e3&ct=1&title&pco=tbxnj-1.0", "height=500px, width=750px, top=150px, left=250px, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");
            return;
        } else if($(this).hasClass("twitter")){
            window.open("https://api.addthis.com/oexchange/0.8/forward/twitter/offer?url=" + sUrl + "&pubid=ra-5688799e3f7e90e3&ct=1&title=&pco=tbxnj-1.0", "height=500px, width=750px, top=150px, left=250px, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");
            return;
        } else if($(this).hasClass("google")){
            window.open("https://api.addthis.com/oexchange/0.8/forward/google_plusone_share/offer?url=" + sUrl + "&pubid=ra-5688799e3f7e90e3&ct=1&title=&pco=tbxnj-1.0", "height=500px, width=750px, top=150px, left=250px, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");
            return;
        }
        //Pinterest
        //window.open("https://api.addthis.com/oexchange/0.8/forward/pinterest/offer?url=" + URL + "&pubid=ra-5688799e3f7e90e3&ct=1&title=" + b + "&pco=tbxnj-1.0", "height=500px, width=750px, top=150px, left=250px, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");
        //VKontakte
        //window.open("https://api.addthis.com/oexchange/0.8/forward/vk/offer?url=" + URL + "&pubid=ra-5688799e3f7e90e3&ct=1&title=" + b + "&pco=tbxnj-1.0", "height=500px, width=750px, top=150px, left=250px, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");
        //Odnoklassniki
        //window.open("https://api.addthis.com/oexchange/0.8/forward/odnoklassniki_ru/offer?url=" + URL + "&pubid=ra-5688799e3f7e90e3&ct=1&title=" + b + "&pco=tbxnj-1.0", "height=500px, width=750px, top=150px, left=250px, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");
    });



    //============================================================关闭分享
    $(".close_share").click(function(){
        $(".share").removeClass("show_y");
        $("#dialogs").css({display:"none"});
    });
    //===========================================商品详情
    $("#pro_dec").click(function(){
        $(this).find(".icon_plus").toggleClass("icon_minus");
        $("#product_det").slideToggle();
    })
});
function bannerscroll(){
    var swiper2 = new Swiper('#swiper-container2', {//轮播切换
        pagination: '.swiper-pagination',
        //autoplay : 3000,
        loop:false,
        scrollbar:'.swiper-scrollbar'
    });
};
$(document).scroll(function(){
    var iStopTop=$('.chunk_product').offset().top-$(window).height();
    var scrollTop=$(document).scrollTop();
    if(scrollTop>iStopTop){
        $('.product_main .cat_but').removeClass('fixed');
        $('.suspend_rb').css('bottom','0.83rem');
    }else{
        $('.product_main .cat_but').addClass('fixed');
        $('.suspend_rb').css('bottom','5.00rem');
    }
});
