$(function(){
    //============================================================banner轮播
    var swiper = new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        paginationClickable: true,
        autoplay : 3000,
        autoplayDisableOnInteraction : false,
        scrollbar:'.swiper-scrollbar'
     });
    scroll('.scrollContainer');
    var myscroll=new iScroll("wrapper",
        {   hScrollbar:false,
            vScrollbar:false,
            click:true,
            vScroll:false,
            bounce :false
        }
    );
});

