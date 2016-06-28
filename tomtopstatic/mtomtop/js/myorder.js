function  getUrldate(){
    var urlArr,spage;
    var urlStr=window.location.href;
    if(urlStr.indexOf("&")==-1){
        return spage=0;
    }else{
        urlArr=urlStr.split("&");
        var status=urlArr[1];
        return spage=status.substring(7);
    }
}
$(function(){
    var i=getUrldate();
    switch (parseInt(i)) {
        case 0:
            $(".order_list_tt ul li").eq(0).addClass('cur').siblings().removeClass("cur");
            var L=$(".order_list_tt ul li").eq(0).offset().left;
            break;
        case 1:
            $(".order_list_tt ul li").eq(1).addClass('cur').siblings().removeClass("cur");
            var L=$(".order_list_tt ul li").eq(1).offset().left;
            break;
        case 6:
            $(".order_list_tt ul li").eq(2).addClass('cur').siblings().removeClass("cur");
            var L=$(".order_list_tt ul li").eq(2).offset().left;
            break;
        case 8:
            $(".order_list_tt ul li").eq(3).addClass('cur').siblings().removeClass("cur");
            var L=$(".order_list_tt ul li").eq(2).offset().left;
            break;
        default:
            $(".order_list_tt ul li").eq(0).addClass('cur').siblings().removeClass("cur");
            var L=$(".order_list_tt ul li").eq(0).offset().left;
            $(".order_list_tt ul").css('-webkit-transform', 'translateX("+(-L)+"px)');
            break;
    }
    var myscroll=new iScroll("wrapper",
        {   hScrollbar:false,
            vScrollbar:false,
            click:true,
            vScroll:false,
            bounce :false
        }
    );
})

