/**
 * Created by Administrator on 2016/4/28.
 */
/*=================会员中心================*/
$(function(){
    $(".chooseOneBox").click(function(){//单选框
        $(this).parent().find(".radio").removeClass('radioChecked');
        $(this).find(".radio").addClass('radioChecked');
    })
    $(".txtInput,.tel").focus(function(){//点击输入框，出现删除按钮
        $(this).parent().find(".sClean").css("opacity",1);
    })
    $(".txtInput,.tel").blur(function(){//点击输入框，出现删除按钮
        $(this).parent().find(".sClean").css("opacity",0);
    })
    $(".sClean").click(function(){//输入框的删除文本
        if($(this).parent().find(".txtInput").val()){
            $(this).parent().find(".txtInput").val('')
        }else{
            $(this).parent().find(".tel").val('')
        }
        $(this).parent().find(".txtInput").val('');
        $(this).css("opacity",0);
    })
    /////////////////////////////////////////////////////日历插件初始化。
    var currYear = (new Date()).getFullYear();
    var opt={};
    opt.date = {preset : 'date'};
    opt.datetime = {preset : 'datetime'};
    opt.time = {preset : 'time'};
    opt.defaults = {
        theme: 'android-ics light',
        display: 'modal',
        mode: 'scroller',
        //dateFormat: 'yyyy-mm-dd',
        lang: 'zh',
        showNow: true,
        startYear: currYear - 60,
        endYear: currYear + 0 //�������
    };
    try{
        $("#appDate").mobiscroll($.extend(opt['date'], opt['default']));
    }catch(e){};
    ///////////////////////////////////////////////////////
    $(".country_input").click(function(){//城市选择
        $(this).parent().find(".country_down").addClass("show");
    })
    $(".icon_down").click(function(){
        var ob=$(this).parents(".input_control").find(".country_down");
        if(ob.hasClass("show")){
            ob.removeClass("show");
        }else{
            ob.addClass("show");
        }

    })
    $(".country_down li").click(function(){
        var text=$(this).attr('data-id');
        var oTxt=$(this).text();
        $(this).parents(".input_control").find(".country_input").val(oTxt);
        $(".hidden-input").val(text);
        $(".country_down").removeClass("show");
    })
    $(".country_input").keyup(function(event){//城市筛选。
        txt=$(this).val().toLowerCase( ) ;
        $(".country_list li").each(function () {
            var ctTxt = $(this).text().toLowerCase( );
            if (ctTxt.search(txt) != -1) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    })
    
    $(".chooseClick").click(function(){
        $(this).toggleClass('checkboxed');
    })
    $(".chooseMoreClick").click(function(){
        var tgClass = $(this).parents(".acount_msg_bd").find(".checkbox");
        if($(this).hasClass("checkboxed")==true){
            tgClass.removeClass("checkboxed");
            $(this).removeClass('checkboxed');
        }else{
            tgClass.addClass("checkboxed");
            $(this).addClass('checkboxed');
        }

    });
    //
    $('.order_list_tt li').click(function(){
        var i=$(this).index();
        $(this).addClass('cur').siblings().removeClass('cur');
        //$('.order_wrap .tab_container').hide();
        //$('.order_wrap .tab_container').eq(i).show();

    });
    //////////////////////////////////////////myreview默认星星给分。
    $(".leveItem").not(".totle_star.leveItem").each(function(){
        var sn=$(this).find("input").val();
        for(var i=0;i<sn;i++){
            $(this).find(".icon_stars").eq(i).addClass("on");
        }
        var avgs=$(".avrg_s").val();
        var p=(avgs/5)*100+'%';
        $(".pwidth").css({'width':p});
    })
    $(".oneclick .icon_stars").click(function(){//星星算分
        var n=$(this).index();
        $(this).parents(".product_review").find('input').val(n+1);
        $(this).parents('.oneclick').find('.icon_stars').removeClass('on');
        for(var i=0;i<=n;i++){
            $(this).parents('.oneclick').find('.icon_stars').eq(i).addClass('on');
        }
        var m=$('.LeveStar_wrap').find(".icon_stars.on").length;
        var p=(m/20)*100+'%'
        $(".pwidth").css({'width':p})
        var avgs=(m/20)*5;
        $(".avrg_s").val(avgs);
    });
    //删除确认框按钮js统一。
    $(document).on('click','.delete_cofirmBtn',function(){
        $("#dialogs").css("display",'block');
        var id=$(this).parents(".deleteParent").attr('data');
        $(".confirm_box").css("display",'block').find('.delete-id').val(id);

    })
    //myMessage删除确认框按钮js。
    $(".delete_accDh").click(function(){
        var checked=$(".tr-list .chooseClick").hasClass("checkboxed");
        if(checked){
            $("#dialogs").css("display",'block');
            $(".confirm_box").css("display",'block');
        }else{
            popError('Please select a column first');
        }
    })
    $(".confirm_box .btn_cancel,.confirm_box .btn_sure").click(function(){
        $("#dialogs").css("display",'none');
        $(".confirm_box").css("display",'none');
    })
    $('.add_ship_adr').click(function(){
        $(".addressform input[type='text']").val('');
        $(".addressform input[type='tel']").val('');
    })
    $('.add_ship_adr,.bt_btn .edit,.address_but').click(function(){
        $('.addressform').addClass('show_y');
        $("#dialogs").css("display",'block');
    })
    $(".address_btn .defaul_btn").click(function(){
        $('.addressform').removeClass('show_y');
        $("#dialogs").css("display",'none');
        $(".addressform input").removeClass("error");
        $(".addressform .error_info").removeClass("error");
    })
    var conHeight=0;//
    $(document).on('click','.view_more',function(){
        var thisval=$(this).text();
        if(thisval=='View more'){
            conHeight=$(this).prev(".conmment_wrap").height();
            $(this).prev(".conmment_wrap").css({'height':'auto'})
            $(this).text('View less');
        }else{
            $(this).prev(".conmment_wrap").css({'height':conHeight+'px'})
            $(this).text('View more');
        }
    })
    ////////////////////////////////////////////////////////////上传图片预览功能
    //定义模板。
    var modules = '<li class="addPic lineBlock" style="display:none"><img><input type="file" name="files[]" style="display:none"><i class="icon_cross"></i></li>';
    var box = $('.addPic_Box');
    //点击选择
    $('#addPic').click(function(){
        if ($(this).siblings('li').length < 5)
        {
            $(this).before(modules);
            var files = box.find('li input[name="files[]"]');
            for (var i = 0; i < files.length; i++) {
                if (!files.eq(i).val())
                {
                    files.eq(i).click();
                    break;
                }
            }
        }else{
            $(this).hide();
        }
    })
    //选择后
    $(document).on('change','.addPic_Box input[name="files[]"]', function(){
        var files = event.target.files;
        if (files && files.length > 0) {
            file = files[0];
            var URL = window.URL || window.webkitURL;
            var imgURL = URL.createObjectURL(file);
            $(this).siblings('img').attr('src', imgURL);
            $(this).parent('li').show();
        }
    })
    //删除图片
    $(document).on('click','.addPic .icon_cross',function(){
        $(this).parents('li').remove();
        $("#addPic").show();
    })
    ////////////////////////////////////////myprofile
    //选择国家
    $('.profile-box .country_list li').click(function(){
        var id = $(this).attr('data-id');
        var name = $(this).text();
        $('.profile-box input[name="country"]').val(name);
        $('.profile-box input[name="contry_id"]').val(id);
    })
    //提交修改
    $('.blue_profile_btn').click(function(){
        var obj = {};
        obj.account = $('.profile-box input[name="nickName"]').val();
        obj.fname = $('.profile-box input[name="firstNmae"]').val();
        obj.lname = $('.profile-box input[name="lastName"]').val();
        obj.gender = $('.profile-box .radioChecked').attr('data');
        obj.countryName = $('.profile-box input[name="contry_id"]').val();
        obj.birth = $('.profile-box input[name="birthday"]').val();
        $.ajax({
            url:'/index.php?r=member/profile',
            type:'post',
            dataType:'json',
            data:obj,
            success:function(data)
            {
                if (data.ret == 1)
                {
                    window.location.href= '/index.php?r=member/index';
                }
                else
                {
                    popError(data.errMsg);
                }
            }
        })

    })
    //////////////////////////////////////////////////////找回密码
    $('.change_psw_btn').click(function(){
        var obj = {};
        obj.oldPassword = $('.password-box input[name="oldPassword"]').val();
        obj.newPassword1 = $('.password-box input[name="newPassword1"]').val();
        obj.newPassword2 = $('.password-box input[name="newPassword2"]').val();
        if (!obj.oldPassword)
        {
            showError("input_wrap_old",'old password can not empty')
        }
        else if(!obj.newPassword1)
        {
            showError("input_wrap_new",'new password can not empty')
        }
        else if (!obj.newPassword2)
        {
            showError("input_wrap_newcf",'new password can not empty')
        }
        else if (obj.newPassword1 != obj.newPassword2)
        {
            showError("input_wrap_newcf",'Please enter the same password as above!')
        }
        else
        {
            hideError('input_wrap_old');
            hideError('input_wrap_new');
            hideError('input_wrap_newcf');
            $.ajax({
                url:'/index.php?r=member/password',
                type:'post',
                dataType:'json',
                data:obj,
                success:function(data)
                {
                    if (data.ret == 1)
                    {
                        window.location.href = '/index.php?r=join/logout&backUrl=' + escape('/index.php?r=join/login');
                    }
                    else
                    {
                        popError(data.errMsg)
                    }
                }
            });

        }
    })
    //////////////////////////////////////////////////
})



