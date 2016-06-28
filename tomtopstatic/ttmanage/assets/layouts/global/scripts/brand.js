$(function(){
   var windowH=$(window).height();
   $("#brand_sort .modal-body,#brand_intro .modal-body").height(windowH-220);//所有弹窗的高度不能超出屏幕的高度。
   $("#brand_on_off .dropdown-menu li a").click(function(){
      $('#brand_on_off .brand_on').text($(this).text())
   })
   $("#brand_name").mouseleave(function(){
       $(this).find(".dropdown-menu").hide()
   })
   $("#brand_name .form-control").keyup(function(event){//品牌名称模糊搜索
        txt=$(this).val().toLowerCase( ) ;
        $("#brand_name .dropdown-menu li a").each(function () {
            var ctTxt = $(this).text().toLowerCase( );
            if (ctTxt.search(txt) != -1) {
                $(this).parent().show();
            } else {
                $(this).parent().hide();
            }
        });
   })
   $("#brand_name .form-control").click(function(){
       $("#brand_name .dropdown-menu").show();
   })
   $("#brand_name .dropdown-menu li a").click(function(){
       $("#brand_name .form-control").val($(this).text());
       $(".dropdown-menu").hide();
   })
   $(".group-checkable").click(function(){
       var op=$(this).parents("table").find('tbody');
       if($(this).prop('checked')){
          op.find(".checkboxes").prop('checked',true)
       }else{
          op.find(".checkboxes").prop('checked',false)
       }
   })
   //简介超出部分字符显示省略号。
   $(".brand_intro").each(function(ele,index){
        var Oheight=$(this).height();
        if(Oheight==50){
          $(this).append("<span>...</span>");
        }
   })
   $(".brand_intro").mouseenter(function(event){
       var ev = ev || event;
       var x=ev.pageX-25;
       var y=ev.pageY-95;
       var str=$(this).find("p").text();
       var Oheight=$(this).height();
       if(Oheight==50){
           $(this).after("<div class='all_info' style='left:"+x+"px;top:"+y+"px'><div>"+str+"</div></div>");
       }else{
          return false;
       }
   })
   $(document).mousemove(function(event){
       var ev = ev || event;
       var x=ev.pageX-25;
       var y=ev.pageY-95;
       console.log(x+":"+y)
       $(".all_info").css({'left':x+10,'top':y+10})
   })
   $(".brand_intro").mouseleave(function(event){
       $(this).parent().find(".all_info").remove();
   })
   $("#brand_sort .move_top_btn").click(function(){//移到第一个
       var op=$(this).parents("tr");
       var oTarget=$(this).parents("tbody").find("tr").eq(0);
       if(op.index()!==0){
          op.insertBefore(oTarget);
       }else{
           alert('已经是第一个了');
           return false;
       }
    })
    $("#brand_sort .green").click(function(){//往上移动一个。
       var op=$(this).parents("tr");
       var oTarget=$(this).parents("tbody").find("tr").eq(op.index()-1);
       if(op.index()!==0){
          op.insertBefore(oTarget);
       }else{
           alert('已经是第一个了');
           return false;
       }
    })
    $("#brand_sort .purple").click(function(){//往下移动一个。
       var trLength=$(this).parents("tbody").find("tr").length-1;
       var op=$(this).parents("tr");
       var oTarget=$(this).parents("tbody").find("tr").eq(op.index()+1);
       if(op.index()!==trLength){
          op.insertAfter(oTarget);
       }else{
           alert('已经是最后一个了');
           return false;
       }
    })
    $(".move_bottom_btn").click(function(){//移到最后一个。
       var trLength=$(this).parents("tbody").find("tr").length-1;
       var op=$(this).parents("tr");
       var oTarget=$(this).parents("tbody").find("tr").eq(trLength);
       if(op.index()!==trLength){
          op.insertAfter(oTarget);
       }else{
           alert('已经是最后一个了');
           return false;
       }
    })
})
