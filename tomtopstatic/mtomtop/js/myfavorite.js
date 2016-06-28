//删除一个
$(function(){
    ////////////////////////////////////////我的收藏。
    $('.delete-sure-btn').click(function(){
        var listingIds = $(this).parents('.confirm_box').find('.delete-id').val() + ',';
        _delete(listingIds);
        if($(".ajax-list-box").find("li").length<1){
            window.location.reload();//删除数据小于1条的时候就重新刷新加载页面。
        }
    })

    function _delete(listingIds)
    {
        $.ajax({
            url:'/index.php?r=myfavorite/delete',
            type:'post',
            dataType:'json',
            data:{'listingIds':listingIds},
            success:function(data)
            {
                if (data.ret == 1)
                {
                    var arr = listingIds.split(",")
                    for (var i = 0; i < arr.length; i++)
                    {
                        $(".listingid-" + arr[i]).remove();
                    }
                }
                else
                {
                    alert(data.errMsg);
                }
            }
        })
    }
})
