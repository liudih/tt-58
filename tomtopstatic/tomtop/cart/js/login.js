function getCookie(name){
    var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
    if(arr != null){
        return unescape(arr[2]);
    }else{
        return null;
    }
}
function strFun(fn) {
    var Fn = Function;  //一个变量指向Function，防止有些前端编译工具报错
    return new Fn('return ' + fn)();
}
if(getCookie("PLAY_LANG")){
    var jsLanguage = getCookie("PLAY_LANG");
}else{
    var jsLanguage = "en";
}
/////======================登陆弹出公用框==================================
function loginPop(){
    var markup = [
        '<div id="loginPopWarp">',
        '<div class="signJoin lbBox">',
        '<h5>'+strFun("TT_language_"+jsLanguage)["tomtop.common.signIn"]+'</h5>',
        '<i class="icon-Close"> </i>',
        '<div class="lineBlock">',
        '<p class="marB5">'+strFun("TT_language_"+jsLanguage)["tomtop.common.signInWithTomTopAccount"]+'</p>',
        '<div class="controls">',
        '<i class="icon-email"> </i>',
        '<input id="sign_email" class="span8" type="text" placeholder="'+strFun("TT_language_"+jsLanguage)["tomtop.common.enterYourEmail"]+'">',
        '<span class="help-block"></span>',
        '</div>',
        '<div class="controls">',
        '<i class="icon-lock"> </i>',
        '<input id="sign_password" class="span8" type="password" placeholder="'+strFun("TT_language_"+jsLanguage)["tomtop.common.enterYourPassword"]+'">',
        '<span class="help-block"></span>',
        '</div>',
        '<input id="signins" type="button" value="SIGN IN" class="btn btn-primary">',
        '<a class="forgetPs" href="http://www.tomtop.com/member/findpass">'+strFun("TT_language_"+jsLanguage)["tomtop.common.forgotPassword"]+'?</a>',
        '<p class="marT15">'+strFun("TT_language_"+jsLanguage)["tomtop.common.signInWithAnExistingAccount"]+'</p>',
        '<div class="iconSign marT15">',
        '<a class="icon-f" href="https://www.facebook.com/dialog/oauth?client_id=284737934897635&redirect_uri=http://www.tomtop.com/ttfb&response_type=code&scope=email"> </a>',
        // '<a class="icon-p" href="javascript:;"> </a>',
        '<a class="icon-g" href="https://accounts.google.com/o/oauth2/auth?client_id=1031779384204-umd79kdejl4q5sdul1hjs32c93437jop.apps.googleusercontent.com&redirect_uri=http://www.tomtop.com/ttgoogle&response_type=code&state=state&scope=https://www.googleapis.com/auth/plus.login email"> </a>',
        '</div>',
        '</div>',
        '<div class="lineBlock rJoinToday">',
        '<p>New to TOMTOP?</p>',
        '<a class="btn btn-primary joinToday" href="http://www.tomtop.com/member/login">',
        strFun("TT_language_"+jsLanguage)["tomtop.common.joinToday"]+'!',
        '<i class="icon-free"> </i>',
        '</a>',
        '<p>'+strFun("TT_language_"+jsLanguage)["tomtop.common.membersOnlyService"]+'</p>',
        '<ol class="onlyService">',
        '<li>'+strFun("TT_language_"+jsLanguage)["tomtop.common.membersOnlyService1"]+'</li>',
        '<li>'+strFun("TT_language_"+jsLanguage)["tomtop.common.membersOnlyService2"]+'</li>',
        '<li>'+strFun("TT_language_"+jsLanguage)["tomtop.common.membersOnlyService3"]+'</li>',
        '<li>'+strFun("TT_language_"+jsLanguage)["tomtop.common.membersOnlyService4"]+'</li>',
        '<li>'+strFun("TT_language_"+jsLanguage)["tomtop.common.membersOnlyService5"]+'</li>',
        '</ol>',
        '</div>',
        '</div>',
        '<div class="pu_popBlack"></div>',
        '</div>'
    ].join('');
    $(markup).hide().appendTo('body').fadeIn();
    $(".icon-Close,.pu_popBlack").click(function(){
        $("#loginPopWarp").fadeOut(function(){$(this).remove();})
    })
    $('#signins').click(function(){
        ajaxSig($("#loginPopWarp"));
    });
}

//--------------------------------------------------登入验证---------------------------------------------
//ajax登录
function ajaxSig(logins){
    var email = logins.find('#sign_email').val();
    var pw = logins.find('#sign_password').val();
    var eErrHtml = logins.find("#sign_email").next(".help-block");
    var eErrCss = logins.find("#sign_email").parents(".controls");
    var pErrHtml = logins.find("#sign_password").next(".help-block");
    var pErrCss = logins.find("#sign_password").parents(".controls");
    var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
    $.ajax({
        type : 'post',
        url : domain+'index.php?r=member/default/login',
        data : {email:email,pw:pw},
        success : function(result){
            if(result.status==1){
                var url = window.location.href;
                $('.signJoin .controls').addClass('success');
                location.href = url;
                pErrHtml.html("");
                eErrHtml.html("");
            }else{
                if (email != "") {
                    isok= reg.test(email );
                    if (!isok) {
                        eErrHtml.html(strFun("TT_language_"+jsLanguage)["tomtop.message.wrongEmail"]);//您输入的邮箱格式不正确
                        eErrCss.addClass("error");
                    }else{
                        eErrCss.removeClass("error");
                        eErrHtml.html("");
                    }
                }else{
                    eErrHtml.html(strFun("TT_language_"+jsLanguage)["tomtop.message.enterYourEmail"]);//请输入您的邮箱
                    eErrCss.addClass("error");
                }
                if(pw == ""){
                    pErrHtml.html(strFun("TT_language_"+jsLanguage)["tomtop.message.enterYourPassword"]);//请输入您的密码
                    pErrCss.addClass("error");
                }else{
                    pErrCss.removeClass("error");
                    pErrHtml.html("");
                }
                if(reg.test(email )&&pw != ""){
                    eErrCss.addClass("error");
                    pErrCss.addClass("error");
                    pErrHtml.html(strFun("TT_language_"+jsLanguage)["tomtop.message.usernameOrPassword"]);//您输入的用户名或密码不正确
                }
                logins.find("#sign_email,#sign_password").focus(function(){
                    $(this).siblings(".help-block").text("");
                    $(this).parents(".controls").removeClass("error")
                })
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            logins.find('.signJoin .controls').addClass('error');
            logins.find('#sign_password').next('.help-block').html(errorThrown);
        },
        dataType : 'json'
    });
}


