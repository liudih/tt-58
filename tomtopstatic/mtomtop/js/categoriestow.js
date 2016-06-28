//当前地址
var arr = window.location.href.split("?");
var url = arr[0];

//构建请求
function myLocation(sort)
{
    var obj = {};
    var r =  getQueryString('r');
    obj.q =  getQueryString('q');
    obj.startPrice  = $('input[name="startPrice"]').val();
    obj.endPrice    = $('input[name="endPrice"]').val();
    obj.tagName     = $('input[name="tagName"]').val();
    obj.depotName     = $('input[name="depotName"]').val();
    obj.sort = sort ? sort : getQueryString('sort');//alert(obj.sort);return;
    var para = '';
    for (var i in obj) 
    {
        if (obj[i]) 
        {
            para += i + '=' + obj[i] + '&';
        }
    }

    if(para)
    {
        para = para.substring(0,para.length-1);
    }
    if (r) 
    {
        var requestUrl = url + '?r=' + r + '&'; 
    }
    else
    {
        requestUrl = url + '?';
    }
    requestUrl += para;
    window.location.href = requestUrl;
}

$(function(){
    //============================================================分类滑动
    scroll(".scrollcontainer");
    if($("#wrapper").length>0){
        var myscroll=new iScroll("wrapper",
            {   hScrollbar:false,
                vScrollbar:false,
                click:true,
                vScroll:false,
                bounce :false
            }
        );
    }
    //============================================================启动Sort by
    $(".sort_by").click(function(){
        $("#dialogs").css({display:"block"});
        $(".sort_by_content").addClass("show_x");
    });
    $(".sort_by_content li").click(function(){
        $(".sort_by_content li").removeClass("select");
        $(this).addClass("select");
    });
    //============================================================启动filter
    $(".filter").click(function(){
        $("#dialogs").css({display:"block"});
        $(".filter_content").addClass("show_x");
    });
    //============================================================filter开关按钮
    $(".on_off").click(function(){
        $(this).toggleClass("open");
    });
    //============================================================filter清除选项
    $(".reset").click(function(){
        $('inputp[name="startPrice"]').val(0.00);
        $('inputp[name="endPrice"]').val(0.00);
        $('inputp[name="tagName"]').val('');
        $(".on_off").removeClass("open");
    })
    //============================================================filter应用并关闭
    $(".apply").click(function(e){
        $(".filter_content").removeClass("show_x");
        $("#dialogs").css({display:"none"});
        e.stopPropagation();
    });
    $(".nav_tow li").click(function(){
        $(this).addClass("cur").siblings().removeClass("cur");
    })

    //过滤选择
    $('.filter_content li span.animate').click(function(){
        var open = $('.filter_content li span.open');
        var str = '';
        if (open.length)
        {
            for (var i = 0; i < open.length; i++) {
                str  += open.eq(i).attr('data') + ',';
            }
            str = str.substring(0,str.length-1);
        }
        $('.filter_content input[name="tagName"]').val(str);
    })

});

