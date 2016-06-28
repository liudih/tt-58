$(function(){
    //序号
    //for(var i=0; i<$('.drag_c dd').length; i++){
        //$('.drag_c dd').eq(i).find('.sku').html('sku:<span>'+(i+1)+'</span>');
    //};
//============================================================================================
    //最多N个
    var iMaxNub=200;

    //一页有N行
    var iPtr=10;

    //一行有N个
    var iTrN=4;

    //一页有N个
    var iPageN=iTrN*iPtr;

    fnTrn(iTrN);
    function fnTrn(iTrN){
        $('.drag_c dd').css('width',100/iTrN+'%');
        fnPageN(iPageN);
    };


    //需要页数
    var objsLen=$('.drag_c dd').length,iPage=$('.drag_c .page').length;
    function fnPageN(iPageN){
        //计算一共需要多少页  fnPageN(目标一页数量)
        objsLen=$('.drag_c dd').length;
        iPage=Math.ceil(objsLen/iPageN); //理想页数
        fnDividing(iPage);
    };

    //分页
    function fnDividing(iPage){
        //重新规划页数 fnDividing(目标页数);
        var iCurPage=$('.page').length;  //当前页数
        var iCiPage=iCurPage-iPage;      //页数差
        if(iCiPage>0){
            fnReset(iCurPage,iPageN);
            for(var i=iCurPage; i>=iPage; i--){
                $('.page').eq(i).remove();
            };
        } else if(iCiPage<0){
            var iW=$('.page').attr('style');
            for(var i=iCurPage; i<iPage; i++){
                var newPage=$('<div style="'+iW+'" class="page page_' +fnZeroize(i+1)+ '"><dl class="clearfix"><dt><span>第' +(i+1)+ '页</span><i class="delete fa fa-trash delete_page"></i></dt></dl></div>');
                $('.drag_c').append(newPage);
            };
            fnReset(iPage,iPageN);
        }else{
            fnReset(iPage,iPageN);
        };
    };


    //每页的数量
    function fnReset(P,PN){
        //重新规划每页的数量  fnReset(当前总页数,一页目标数量)
        for(var i=0; i<P; i++){
            var iPageNub=$('.page').eq(i).find('dd').length;  //第 i 页的数量
            var iC=iPageNub-PN;   //第 i 页的数量差
            if(iC<0){
                var iC=Math.abs(iC);
                for(var j=0; j<iC; j++){
                    var took=false;
                    for(var s=1; s<P-i; s++){
                        if($('.drag_c dl').eq(i+s).find('dd').length!==0){
                            var newEmems=$('.drag_c dl').eq(i+s).find('dd').first().clone();
                            var a=i+s;
                            took=true;
                            break;
                        };
                    };
                    if(took){
                        $('.drag_c dl').eq(i).append(newEmems);
                        $('.drag_c dl').eq(a).find('dd').first().remove();
                    };
                };
            }else if(iC>0){
                for(var j=0; j<iC; j++){
                    var newEmems=$('.drag_c dl').eq(i).find('dd').last().clone();  //本页的最后一个
                    if($('.drag_c dl').eq(i+1).find('dd').length==0){
                        //如果是新增的页面是没有 DD 的
                        $('.drag_c dl').eq(i+1).find('dt').last().after(newEmems);
                    }else{
                        $('.drag_c dl').eq(i+1).find('dd').first().before(newEmems);
                    };
                    $('.drag_c dl').eq(i).find('dd').last().remove();
                };
            };
        };
    };

    function fnZeroize(n){
        //0-9前面补0 fnZeroize(数字)
        var sNub=null;
        if(n<10){
            sNub='0'+n;
        } else{
            sNub=''+n;
        };
        return sNub;
    };

    //一排有N页
    var iTrPage=2;
    fnTrPages(iTrPage);
    function fnTrPages(iTrPage){
        //重新规划第一行的页数 fnTrPages(目标页数)
        $('.page').css('width',100/iTrPage+'%');
    };

    //重制页数
    function fnPageText(){
        var iCurP=$('.page').length;
        for(var i=0; i<iCurP; i++){
            $('.page').eq(i).find('dt').find('span').text('第'+(i+1)+'页');
            $('.page').eq(i).removeClass().addClass('page page_'+fnZeroize(i+1));
        };
    };


    //开始排序
    $(document).on('mousedown','.drag_c .product',function(e){
        var objsLen=$('.drag_c dd').length;
        var obj=$(this).parent();
        var iObjsW=obj.width();
        var iObjsH=obj.height();
        var iMaxL=$('.drag_c').width()-iObjsW;
        var iMaxT=$('.drag_c').height()-iObjsH;
        //获取鼠标坐标
        var iDisX=e.pageX;
        var iDisY=e.pageY;
        //创建虚似对象
        var iLeft=obj.position().left;
        var iTop=obj.position().top;
        var oVirtual=obj.html();
        oVirtual=$('<dd class="virtual_obj" style="width:'+iObjsW+'px; height:'+iObjsH+'px; left:'+iLeft+'px; top:'+iTop+'px;">'+oVirtual+'</dd>');
        $('.drag_c dd').last().after(oVirtual);
        //开始拖动
        var iL=null,iT=null;
        $(document).mousemove(function(e){
            //计算移动的距离
            var iMouseL=e.pageX-iDisX;
            var iMouseT=e.pageY-iDisY;
            //虚似元素目标坐标
            iL=iLeft+iMouseL;
            iT=iTop+iMouseT;
            if(iL>iMaxL){
                iL=iMaxL;
            } else if(iL<0){
                iL=0;
            };
            if(iT>iMaxT){
                iT=iMaxT;
            } else if(iT<0){
                iT=0;
            };
            oVirtual.css({'left':iL,'top':iT});
        }).mouseup(function(){
            var sort=null;
            var BGoNo=false;
            for(var i=0; i<objsLen; i++){
                var iObjsT=$('.drag_c dd').eq(i).position().top;  //每一个top
                var iMinT=Math.abs(iT-iObjsT);
                if(iMinT>=0 & iMinT<iObjsH/2){
                    //定位到相同行
                    for(var j=i; j<i+iTrN; j++){
                        var iObjsL=$('.drag_c dd').eq(j).position().left;
                        var iMinL=iL-iObjsL;

                        if((iMinL>=0 & iMinL<iObjsW/2.5) || (iMinL<0 & iMinL>-iObjsW/2.5)){
                            //在右侧 定位到列                在左侧
                            //再判断一次 防止本页不够到下一页去了
                            iObjsT=$('.drag_c dd').eq(j).position().top;  //每一个top
                            iMinT=Math.abs(iT-iObjsT);
                            if(iMinT>=0 & iMinT<iObjsH/2){
                                sort=j;
                                BGoNo=true;
                                break;
                            };
                        } else if(iMinL>iObjsW/2.5 & iMinL<iObjsW){
                            //再判断一次
                            iObjsT=$('.drag_c dd').eq(j).position().top;  //每一个top
                            iMinT=Math.abs(iT-iObjsT);
                            if(iMinT>=0 & iMinT<iObjsH/2){
                                sort=j+1;
                                BGoNo=true;
                                break;
                            };
                        };
                    };
                };
                if(BGoNo){
                    break;
                };
            };
            if(!(iT==null || iL==null || sort==null)){
                var newobj=obj.clone();
                $('.drag_c dd').eq(sort).before(newobj);
                //删除被移走的元素
                obj.remove();
            };
            //删除虚似对象
            oVirtual.remove();
            //重新规划每页的数量
            fnReset(iPage,iPageN);
            //解除事件
            $(document).unbind("mouseup");
            $(document).unbind("mousemove");
        });
        return false;
    });

    //手动删除单个
    $(document).on('click','.drag_c .delete_cur',function(){
        $(this).parents('dd').remove();
        fnPageN(iPageN);
    });

    //手动删除整页

    $(document).on('click','.drag_c .delete_page',function(){
        var iDeletePage=$('.drag_c .delete_page').index($(this));
        var text='您是否要删除第'+(iDeletePage+1)+'页?';
        var objDelete=$(this).parents('.page');
        bootbox.confirm(text, function(o) {
            if(o){
                objDelete.remove();
                fnPageText();
            }
        });
    });

    //手动添加
    var iAdd=null;
    $(document).on('click','.drag_c .add',function() {
        var iCurNub=$('.drag_c dd').length;
        if(iCurNub<iMaxNub){
            iAdd=$('.drag_c .add').index($(this));
            var iW=$('dd').attr('style');
            bootbox.prompt("请输入SKU", function(o) {
                if(null === o){

                }else{
                    alert("输入的SKU为" + o +'请添加到索引为'+iAdd+'的dd前面');
                    //    //<dd style="+iW+">
                };
            });
        }else{
            bootbox.alert("SKU已达上限,不能添加")
        };
    });



//=====================================================================================================================================
    //一页的行数
    $('.p_tr select').click(function(){
        iPtr=$(this).find("option:selected").text();
        iPageN=iTrN*iPtr;
        fnPageN(iPageN);
    });

    //一排的页数
    $('.tr_p select').click(function(){
        iTrPage=$(this).find("option:selected").text();
        fnTrPages(iTrPage);
    });


    //一行的数量
    $('.tr_n select').click(function(){
        iTrN=$(this).find("option:selected").text();
        iPageN=iTrN*iPtr;
        fnTrn(iTrN);
    });

    $('.textarea_text textarea').keyup(function(){
        var sku=$(this).val();
        $('.textarea_text .textarea').html(sku);
    });

});





