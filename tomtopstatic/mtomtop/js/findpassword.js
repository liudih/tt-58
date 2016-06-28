/**
 * Created by Administrator on 2016/5/18.
 */
$(function(){
    ////////////////////////////////////找回密码。
    $('.blue_lg_btn').click(function(){
        var email = $('.reset_psw input[name="email"]').val();
        var reg =  /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
        if (reg.test(email))
        {
            $(".error_info").hide();
            $('.input_control').removeClass('error');
            $.ajax({
                url:'/index.php?r=join/findpassword',
                type:'post',
                dataType:'json',
                data:{'email':email},
                success:function(data)
                {
                    if (data.ret == 1)
                    {

                        $('.checkInfo').show();
                        $(".emill_adr").text(email);

                    }
                    else
                    {
                        popError(data.errMsg)
                    }
                }
            })
        }
        else
        {
            $('.input_control').addClass('error');
            $(".error_info").show();
        }
    })
})