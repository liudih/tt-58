/**
 * Created by Administrator on 2016/5/18.
 */
$(function(){
    // 标记已读
    $('.read_accDh').click(function(){
        handler('read');
    })
    // 删除确认信息。
    $('.delete-sure-btn').click(function(){
        handler('delete');
        if($(".ajax-list-box").find("li").length<1){
            window.location.reload();//删除数据小于1条的时候就重新刷新加载页面。
        }
    })
    function handler(action)
    {
        var readed = $('.tr-list .checkboxed');
        if (readed.size())
        {
            var ids = '';
            for (var i = 0; i < readed.length; i++)
            {
                ids += readed.eq(i).parents('.tr-list').attr('data') + ',';
            }
            $.ajax({
                url:'/index.php?r=mymessage/handler',
                type:'post',
                dataType:'json',
                data:{'ids':ids,'action':action},
                success:function(data)
                {
                    if (data.ret == 1)
                    {
                        for (var i = 0; i < readed.length; i++)
                        {
                            if (action == 'delete')
                            {
                                readed.eq(i).parents('.tr-list').remove();
                            }
                            else
                            {
                                readed.eq(i).parents('.tr-list').find('.elips').addClass('visited');
                            }
                        }
                    }
                    else
                    {
                        popError(data.msg)
                    }
                }
            })
        }
    }
})

