// var currencyCfg = {};
// currencyCfg.USD = {'rate':1,'label':'US$'};
// currencyCfg.EUR = {'rate':0.969,'label':'€'};
// currencyCfg.RUB = {'rate':78.0995,'label':'руб.'};
// currencyCfg.JPY = {'rate':118.56,'label':'JP¥'};
// currencyCfg.GBP = {'rate':0.6987,'label':'£'};
// currencyCfg.BRL = {'rate':4.0269,'label':'R$'};
// currencyCfg.AUD = {'rate':1.4764,'label':'AU$'};
// currencyCfg.PLN = {'rate':4.2321,'label':'zl'};
// currencyCfg.SEK = {'rate':9.2603,'label':'Kr'};
// currencyCfg.CHF = {'rate':1.09,'label':'SFr'};
// currencyCfg.CAD = {'rate':1.45,'label':'C$'};
// currencyCfg.DKK = {'rate':6.693,'label':'Kr'};
// currencyCfg.INR = {'rate':70.2419,'label':'Rs'};
// currencyCfg.TRY = {'rate':3.008,'label':'YTL'};
// currencyCfg.MXN = {'rate':17.5876,'label':'MXP'};
// currencyCfg.NOK = {'rate':9.1788,'label':'Kr'};
// currencyCfg.CZK = {'rate':25.51,'label':'CZK'};

//记录是否手工切换
var switchRecord = false;

//当前货币
var url_currency = getQueryString('currency');
var curr_currency = getCookie('TT_CURR');
curr_currency = curr_currency ? curr_currency : 'USD';
curr_currency = isCurrency(url_currency) ? url_currency : curr_currency;

//清除老cookie
$(function(){
	var date = new Date();
	date.setTime(date.getTime() - 3600*24*7*1000);
	expires = date.toGMTString();
	document.cookie = "TT_CURR=USD;expires=" + expires + ";domain=.m.tomtop.com";
	document.cookie = "TT_CURR=JPY;expires=" + expires + ";domain=m.tomtop.com";
})


//货币是否正确
function isCurrency(curr)
{
	for(var i in currencyCfg)
	{
		if (i==curr) 
		{
			return true;
		}
	}

	return false;
}

//加载货币列表
function loadCurrency()
{
	var showBox = $('.currency-list');
	var li = '';
	for(var i in currencyCfg)
	{
		if(i==curr_currency)
		{
			li = '<li><a class="current" href="javascript:void(0)"><em class="name">'+i+'</em><em class="label">'+currencyCfg[i].label+'</em></a></li>';
		}
		else
		{
			li = '<li><a href="javascript:void(0)"><em class="name">'+i+'</em><em class="label">'+currencyCfg[i].label+'</em></a></li>';
		}
		showBox.append(li);
	}
}

//汇率计算
function calculateRate(will_currency, now_price, us_price)
{
	will_currency = will_currency.replace(/^\s+|\s+$/g,"");
	var price = us_price * currencyCfg[will_currency].rate;
	// alert('will_currency='+will_currency+', now_price='+now_price+', price='+price);
	return will_currency  == 'JPY' ? Math.round(price) : price.toFixed(2);
}
//关闭货币选择框
function closeCurr()
{
    $(".currency_box").removeClass("show_y");
    $("#dialogs").css({display:"none"});	
}

//切换所有货币
function switchCurrency(will_currency)
{
	var label = currencyCfg[will_currency].label;
	$('.show-currency-select').text(label);
	var product_price = $('.product_price');
	if (product_price.length) 
	{
		for (var i = 0; i < product_price.length; i++) 
		{
			switchOne(product_price.eq(i), will_currency);
		}
	}
	switchRecord = true;
}

//切换一个指定的货币
function switchOne(dom, will_currency)
{
	var label = currencyCfg[will_currency].label;
	var original_price_dom 	= dom.find('.original_price');
	var current_price_dom 	= dom.find('.current_price');
	var original_price 		= original_price_dom.find('em').text();
	var current_price 		= current_price_dom.find('em').text();
	var us_ori_price 		= original_price_dom.attr('data');
	var us_curr_price 		= current_price_dom.attr('data');
	var original_price2 = calculateRate(will_currency, original_price, us_ori_price);
	var current_price2  = calculateRate(will_currency, current_price, us_curr_price);//alert(current_price+'---'+current_price2);
	original_price_dom.html(label + '<em>'+original_price2+'</em>');
	current_price_dom.html(label + '<em>'+current_price2+'</em>');
}

$(function(){

	//加载货币列表
	loadCurrency();

	//页面加载时根据cookie切换货币
	switchCurrency(curr_currency);

	//展开
    $(".currency").click(function(){
        $("#dialogs").css({display:"block"});
        $(".currency_box").addClass("show_y");
    });

    //关闭	
    $(".close_currency").click(function(){
    	closeCurr();
    });
	//手动切换
    $(document).on('click','.currency-list li',function(){
		$(this).find('a').addClass('current');
    	$(this).siblings('li').find('a').removeClass('current');
		var will_currency = $(this).find('em.name').text();
		$("#usd").text(will_currency);
		setCookies('TT_CURR', will_currency, 7);
		// setCookies('PLAY_LANG', will_currency, 7);
		switchCurrency(will_currency);
		curr_currency = will_currency;
    	closeCurr(); 
    })
	// //手动切换
	// $(document).on('touchstart','.currency-list li',function(){
	// 	$(this).find('a').addClass('current');
	// 	$(this).siblings('li').find('a').removeClass('current');
	// 	var will_currency = $(this).find('em.name').text();
	// 	$("#usd").text(will_currency);
	// 	setCookies('TT_CURR', will_currency, 7);
	// 	// setCookies('PLAY_LANG', will_currency, 7);
	// 	switchCurrency(will_currency);
	// 	curr_currency = will_currency;
	// 	closeCurr();
	// })
})



