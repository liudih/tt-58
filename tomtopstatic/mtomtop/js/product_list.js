
$(function(){
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
    //============================================================选择仓库
    $(document).on('click', ".warehouse em", function(){
        $("#dialogs").css({display:"block"});
        $(this).parent().siblings(".warehouse_more").addClass("show_y");
    });
    var i=0;
    $(document).on('click','.warehouse i', function(){
        selectWhouse($(this));
    });
    $(document).on('click','.warehouse_more_c i',function(){
        selectWhouse($(this));
        $("#dialogs").css({display:"none"});
        $(".warehouse_more").removeClass("show_y");
    });
    function selectWhouse(othis)
    {
        i=othis.index();
        othis.siblings().removeClass("select");
        othis.addClass("select");
        othis.parents(".product").find(".warehouse_more_c").find("i").removeClass("select");
        othis.parents(".product").find(".warehouse_more_c").find("i").eq(i).addClass("select");
        warehouse(othis);        
    }
    function warehouse(oThis)
    {
        var depot_nowprice  = oThis.find('.depot_nowprice').val();
        var depot_origprice = oThis.find('.depot_origprice').val();
        var product_price = oThis.parents('li.product_box').find('.product_price');
        var discount = oThis.parents('li.product_box').find('.off');
        if(depot_origprice && depot_nowprice/depot_origprice < 1)
        {
            var off = Math.ceil((1-depot_nowprice/depot_origprice)*100);
            discount.text(off).show();
            product_price.find('.original_price').attr('data', depot_origprice).show();
        }
        else
        {
            discount.hide();
            product_price.find('.original_price').hide();
        }
        product_price.find('.current_price').attr('data', depot_nowprice);
        switchOne(product_price, curr_currency);  
    };
    

});

//异步加载收藏列表的listingId，循环对比当前登录用户是否已经收藏
var myfavorite = null;
$(function(){
    if (is_login) 
    {
        $.ajax({
            url:'/index.php?r=myfavorite/listingidlist',
            dataType:'json',
            success:function(data)
            {
                if (data.ret == 1) 
                {
                    myfavorite = data.data;
                    isFavorite();
                }
            }
        });
    }
})

//循环对比当前用户是否已收藏
function isFavorite()
{
    var icons = $(document).find('i.no-preg-wishlist');
    for (var i = 0; i < icons.length; i++) 
    {
        var listingId = icons.eq(i).parents('a.favorites').attr('data-id');
        for(var j in myfavorite)
        {
            if(listingId == myfavorite[j])
            {
                icons.eq(i).addClass('icon_wishlists').removeClass('icon_wishlist').removeClass('no-preg-wishlist');
            }
        }
    }
}



//收藏
function addWishlist(listingId,obj)
{
    $(".hint").removeClass("hint_slide");
    if(is_login)
    {
        $.ajax({
            url: '/index.php?r=myfavorite/add',
            type:'post',
            dataType:'json',
            data:{'listingId':listingId},
            success:function(data)
            {
                if (data.ret == 1) //添加成功
                {
                    popError('add success!');
                    $(obj).find('i.icon_wishlist').removeClass("icon_wishlist").addClass('icon_wishlists');
                }
                else if(data.ret == -2) //未登录
                {
                    popLogin();
                }
                else
                {
                    popError(data.errMsg);

                }
            }
        })        
    }
    else
    {
        popLogin();
    }
}
//加载购物车选择部件
function loadCart(url, listingId)
{
    $.ajax({
        url:url,
        success:function(data)
        {
            $("#dialogs").css({display:"block"});
            $('.pop-cart-box-'+listingId).html(data).addClass('show_y');
        }
    })
}
