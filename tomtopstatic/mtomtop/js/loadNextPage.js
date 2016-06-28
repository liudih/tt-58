var cur_page=1;//全局分页变量。
var r=true;//全局变量开关控制，避免一次滑动加载多次。
var cur_urlStr=window.location.href;
if(cur_urlStr.indexOf('?') == -1){
    cur_urlStr=cur_urlStr+"?page=";
}else{
    cur_urlStr=cur_urlStr+"&page=";
}


//栏目切换
function loadData(requestUrl)
{
    $.ajax({
        url: requestUrl,
        success:function(data)
        {
            cur_page=1;
            cur_urlStr =requestUrl;
            if(cur_urlStr.indexOf('?') == -1){
                cur_urlStr=cur_urlStr+"?page=";
            }else{
                cur_urlStr=cur_urlStr+"&page=";
            }
            $('.ajax-list-box').html(data);
            //判断是否收藏
            if(typeof(isFavorite)=='function')
            {
                isFavorite();
            }
            //切换货币
            if(typeof(switchCurrency)=='function')
            {
                switchCurrency(curr_currency);
            }
            r=true;
        }
    })
}
//滑到底部加载新的分页内容。
$(document).scroll(function(){
    var noRequire=$(".ajax-list-box").find(".no_data_tips").length;
    if($(document).scrollTop()>=$(".wrap").height()-$(window).height()+100){
        var pageTotal = $(document).find('.page-total-count').last().val();
        if(cur_page<pageTotal && r==true && !noRequire){//小于分页总数的时候停止加载。
            r=false;
            loadNextPage();
        }else{
            return false
        }
    }
})
//加载新的分页内容。
function loadNextPage(){
    cur_page++;
    $.ajax({
        url:cur_urlStr+cur_page ,
        type: "get",
        dataType: "html",
        success:function(data)
        {
            //$(".loading_button").remove();
            $(".ajax-list-box").append(data);
            //判断是否收藏
            if(typeof(isFavorite)=='function')
            {
                isFavorite();
            }
            //切换货币
            if(typeof(switchCurrency)=='function')
            {
                switchCurrency(curr_currency);
            }
            r=true;
        },
        error:function() {
            popError("网路请求异常！")
        }
    })
}