$(function(){
    //当前时间
    var fTimeA=$('#serverTime').text();
    var re=/-/g;
    fTimeA=fTimeA.replace(re,"/");
    fTimeA=new Date(fTimeA).getTime()/1000;
    setInterval(function(){
        ++fTimeA;
    }, 1000);

    for(var i=0; i<$('.countdown').length; i++){
        //首次执行所有产品默认仓库倒计时
        $('.countdown').eq(i).attr('no','countdown'+i);
        fnCountdownD($('.countdown').eq(i));
    };

    $('.warehouseList span').click(function(){
        //切换仓库执行该仓库倒计时
        var sInfo=$(this).attr('data');
        var oInfo=strFun(sInfo);
        var sTimeB=oInfo.saleEndDate;
        //结束时间
        sTimeB=sTimeB.replace(re,"/");
        var fTimeB=new Date(sTimeB).getTime()/1000;
        var fNewSurplus=fTimeB-fTimeA;
        var obj=$(this).parent().siblings('.countdown');
        obj.find('input').val(fNewSurplus);
        fnCountdownD(obj);

        //是否免邮
        var sFree=oInfo.freeShipping;
        var oFree=$(this).parent().siblings('.freeShipping');
        if(sFree){
            oFree.css('display','block');
        } else{
            oFree.css('display','none');
        }

    });
});
function strFun(fn) {
    //字符串转对象
    var Fn = Function;
    return new Fn('return ' + fn)();
};
var object=new Object();
function fnCountdownD(obj){
    //计算当前剩余秒数
    var iCountdown=obj.attr('no');
    clearInterval(object[iCountdown]);
    var surplus=obj.find('input').val();
    if(surplus>0){
        obj.css('display','block');
        var objs=obj.find('span');
        object[iCountdown]=setInterval(function(){
            --surplus;
            fnCalculate(objs,surplus);
        }, 1000);
    } else{
        obj.css('display','none');
    };
};
function fnCalculate(objs,surplus){
    //总秒数换算天时分秒
    var second = Math.floor(surplus % 60);
    var minite = Math.floor((surplus / 60) % 60);
    var hour = Math.floor((surplus / 3600) % 24);
    var day = Math.floor((surplus / 3600) / 24);

    if(day<0){
        day=hour=minite=second=0;
    }
    objs.html(fnZeroize(day)+'d:'+fnZeroize(hour)+'h:'+fnZeroize(minite)+'m:'+fnZeroize(second)+'s');
};
function fnZeroize(nub){
    //0-9前面补0 fnZeroize(数字)
    var sNub=null;
    if(nub<10){
        sNub='0'+nub;
    } else{
        sNub=''+nub;
    };
    return sNub;
};