$(function(){
    $(".login_register span").click(function(){
        $(".login_register span").removeClass("cur");
        $(this).addClass("cur");
        var index=$(".login_register span").index($(this));

        if(index){
            $(".login").css({display:"none"});
            $(".register").css({display:"block"});
        } else{
            $(".login").css({display:"block"});
            $(".register").css({display:"none"});
        }
    });
    //登录
    $('.join-login .login_but').click(function(){
        var obj = {};
        obj.email = $('.join-login input[name="email"]').val();
        obj.pwd = $('.join-login input[name="pwd"]').val();
        obj.type = 'login';
        if (!obj.email)
        {
            showError('login-email-li', 'Email Can Not Empty!');
        }else if(!isEmail(obj.email)){
            showError('login-email-li', 'Email is Error!');
        }
        else if(!obj.pwd)
        {
            showError('login-pwd-li', 'Password Can Not Empty!');
        }
        else
        {
            hideError('login-email-li');
            hideError('login-pwd-li');
            doAjax(obj);
        }
    })
    //注册
    $('.register .register_but').click(function(){
        var obj = {};
        obj.email = $('.register input[name="email"]').val();
        obj.authcode = $('.register input[name="authcode"]').val();
        obj.type = 'register';
        var pwd1 = $('.register input[name="pwd1"]').val();
        var pwd2 = $('.register input[name="pwd2"]').val();
        if (!obj.email)
        {
            showError('register-email-li', 'Email Can Not Empty!');
        }else if(!isEmail(obj.email)){
            showError('register-email-li', 'Email is Error!');
        }
        else if(!pwd1)
        {
            showError('register-pwd1-li', 'Password Can Not Empty!');
        }
        else if(pwd1 != pwd2)
        {
            showError('register-pwd2-li', 'Two passwords are not consistent!');
        }
        else if(!obj.authcode)
        {
            showError('register-authcode-li', 'Authcode Can Not Empty!');
        }
        else
        {
            hideError('register-email-li');
            hideError('register-pwd1-li');
            hideError('register-pwd2-li');
            hideError('register-authcode-li');
            obj.pwd = pwd1;
            doAjax(obj);
        }
    })
});
//发送
function doAjax(obj)
{
    $.ajax({
        url:'/index.php?r=join/login',
        type:'post',
        dataType:'json',
        data:obj,
        success:function(response)
        {
            if (response.ret == 1)
            {
                if(obj.type == 'register'){
                    $(".checkInfo").show();
                    $(".checkInfo").find(".you_email").text(obj.email);
                    setTimeout(function(){
                        var url = '/index.php?r=join/login';
                        window.location.href = url;
                        $(".checkInfo").hide();
                    },5000)
                }else{
                    var url = '/index.php?r=member/index';
                    window.location.href = url;
                }
            }
            else
            {

                if(obj.type == 'register' && response.ret == -2)
                {
                    $('.authcode-img').attr('src','/index.php?r=join/authcode&tm='+Math.random());
                }
                //错误弹窗
                popError(response.errMsg);

            }
        }
    });
}
