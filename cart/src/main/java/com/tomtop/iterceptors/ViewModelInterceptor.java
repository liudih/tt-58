package com.tomtop.iterceptors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.tomtop.services.cart.ICookieCartService;
import com.tomtop.services.impl.LoyaltyService;
import com.tomtop.utils.FoundationService;
import com.tomtop.utils.HttpClientUtil;
import com.tomtop.utils.Request;
import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.base.LoginContext;

/**
 * viewModel interceptor
 * 
 * @author lijun
 *
 */
@Service
public class ViewModelInterceptor implements HandlerInterceptor {

	@Autowired
	FoundationService foundationService;

	@Autowired
	ICookieCartService cartService;

	@Autowired
	LoyaltyService loyaltyService;
	
	@Autowired
	ICookieCartService cookieCartService;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		String view = "";
		if (modelAndView != null && modelAndView.getModel() != null) {
			modelAndView.getModel().put("cdn-url",this.foundationService.getStaticResourcePrefix());
			modelAndView.getModel().put("mainurl", this.foundationService.getMainDomain());
			// 站点名称
			modelAndView.getModel().put("host-name",this.foundationService.getHostName());

			modelAndView.getModel().put("imgUrlPrefix",this.foundationService.getImgUrlPrefix());

			modelAndView.getModel().put("ico", foundationService.getIco());
			LoginContext loginCtx = this.foundationService.getLoginContext();
			modelAndView.getModel().put("islogin", loginCtx.isLogin());
			if(loginCtx.isLogin()){
				modelAndView.getModel().put("current_email", loginCtx.getEmail());
			}
			
			//版本号
			modelAndView.getModel().put("version", this.foundationService.getVersion());
			view = modelAndView.getViewName();
		}
		// 支付成功后清掉cookie

		if ("/order/pay_success2".equals(view) || "/mobile/order/pay_success2".equals(view)) {
			cartService.deleteStorageItems(null);
			loyaltyService.clearAllDiscount();
		}
		
		//m端加东西
		String firstdomain = foundationService.getSubdomains();
		if("m".equals(firstdomain) && 
				modelAndView != null && modelAndView.getModel() != null){
			
			modelAndView.getModel().put("islogin", foundationService.getLoginContext().isLogin());
			//当前货币
			modelAndView.getModel().put("currency", foundationService.getCurrency());
			//购物车数量
			int lang = foundationService.getLang();
			int siteid = foundationService.getSiteID();
			String currency = foundationService.getCurrency();
			List<CartItem> cartItemlist = cookieCartService.getCookiesAllItems(siteid,
					lang, currency);
			modelAndView.getModel().put(
					"cartsize",
					CollectionUtils.isNotEmpty(cartItemlist) ? cartItemlist
							.size() : 0);
			
			//过去全部货币
			String currencyUrl = FoundationService.BASE_URL+"/base/currency/v1";
			String curdata = HttpClientUtil.doGet(currencyUrl);
			if(curdata!=null && !"".equals(curdata)){
				JSONObject object = JSON.parseObject(curdata);
				Integer ret = object.getInteger("ret");
				if(ret!=null && ret==1){
					JSONArray jdata = object.getJSONArray("data");
					List<Map<String,Object>> curlist = Lists.newArrayList();
					for(int i=0;i<jdata.size();i++){
						JSONObject jb = jdata.getJSONObject(i);
						String symbolCode = jb.getString("symbolCode");
						String curcode = jb.getString("code");
						Map<String,Object> curmap = new HashMap<String, Object>();
						curmap.put("symbolCode", symbolCode);
						curmap.put("code", curcode);
						curlist.add(curmap);
					}
					modelAndView.getModel().put("curlist", curlist);
				}
			}
		}
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		Request.destroy();
	}

}
