
$(function(){
    //============================================================打开产品属性
    $('.buy').click(function(){
        $(this).parents(".product_box").find(".product_attr").addClass("show_y");
        $("#dialogs").css({display:"block"});
    });
    //============================================================关闭产品属性
    $(document).on('click',".close_attr",function(){
        $(".product_attr").removeClass("show_y");
        $("#dialogs").css({display:"none"});
    });
    //============================================================关闭登入弹窗
    $(".close_login").click(function(){
        $(".popup_login").removeClass("show_y");
        $("#dialogs").css({display:"none"});
    });
    $("#dialogs").click(function(){
        //==========关闭产品属性
        if($(".product_attr").hasClass("attr_slide")){
            $(".product_attr").removeClass("attr_slide");
        };
        $("#dialogs").css({display:"none"});
    });
    //============================================================选择属性
    $(".attr_val i").click(function(){
        $(this).siblings("i").removeClass("current");
        $(this).addClass("current");
    });
    //============================================================购买产品数量
    $(".qty_cut").click(function(){
        var iNub=$(this).siblings(".qty_txt").val();
        if(iNub>1){
            $(this).siblings(".qty_txt").val(--iNub);
        }
    });
    $(".qty_add").click(function(){
        var iNub=parseInt($(this).siblings(".qty_txt").val());
        $(this).siblings(".qty_txt").val(++iNub);
    });
    //============================================================选择仓库
    $(document).on('click', ".warehouse em", function(){
        $("#dialogs").css({display:"block"});
        $(this).parent().siblings(".warehouse_more").addClass("show_y");
    });

    var i=0;
    $(document).on('click','.warehouse i', function(){
        i=$(this).index();
        $(this).siblings().removeClass("select");
        $(this).addClass("select");
        $(this).parents(".product").find(".warehouse_more_c").find("i").removeClass("select");
        $(this).parents(".product").find(".warehouse_more_c").find("i").eq(i).addClass("select");
    });
    $(document).on('click','.warehouse_more_c i',function(){
        i=$(this).index();
        $(this).siblings().removeClass("select");
        $(this).addClass("select");
        $(this).parents(".product").find(".warehouse").find("i").removeClass("select");
        $(this).parents(".product").find(".warehouse").find("i").eq(i-2).addClass("select");
        warehouse($(this));
        $("#dialogs").css({display:"none"});
        $(".warehouse_more").removeClass("show_y");
    });
    function warehouse(oThis){
        //价钱切换
        var priceDom = $(this).parents('.product').find('.product_price');
        priceDom.find('.current_price em').text($(this).find('.depot_nowprice').val());
        priceDom.find('.original_price em').text($(this).find('.depot_origprice').val());
    };
});