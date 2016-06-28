$(function(){
    $(".product_x dt").click(function(){
        $(".product_x dt").find(".radio").removeClass("radioChecked");
        $(this).find(".radio").addClass("radioChecked");
    });

    $(".product_x dd .checkbox").click(function(){
        $(this).toggleClass("checkboxed");
    });

//    $(".collect").click(function(){
//        if ($(this).hasClass("icon_wishlist")) {
//            $(this).removeClass().addClass("icon_wishlists");
//        } else {
//            $(this).removeClass().addClass("icon_wishlist");
//        }
//    });
//    $(".icon_delete2").click(function(){
//        $("#dialogs").css("display",'block');
//        $(".confirm_box").css("display",'block');
//    });
    $(".btn_cancel,.btn_sure").click(function(){
        $("#dialogs").css("display",'none');
        $(".confirm_box").css("display",'none');
    });
});
