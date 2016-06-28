$(function(){
    //＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝评论图片
    var swiper = new Swiper('.swiper-container', {
       loop : true,
       autoplay : 3000
    })
    $(".comment_pic").click(function(){
        $("#dialogs").css({display:"block"});
        $(this).siblings(".check_img").addClass("show_x")
    })
    $(".check_img").click(function(){
        $(this).removeClass("show_x");
        $("#dialogs").css({display:"none"})
    })
    $(".check_img img").click(function(e){
        e.stopPropagation();
    })
    //删除地址
    $(document).on('click','.delete-sure-btn',function(){
        var id = $(this).parents('.confirm_box').find('.delete-id').val();
        _delete(id);
        if($(".ajax-list-box").find("li").length<1){
            window.location.reload();//删除数据小于1条的时候就重新刷新加载页面。
        }
    })
    function _delete(id)
    {
        $.ajax({
            url:'/index.php?r=myreview/delete',
            type:'post',
            dataType:'json',
            data:{'rid':id},
            success:function(data)
            {
                if (data.ret == 1)
                {
                    $('.rid-'+id).remove();
                }
                else
                {
                    popError(data.errMsg);
                }
            }
        })
    }
});
