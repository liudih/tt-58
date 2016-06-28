package com.tomtop.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.common.collect.Lists;
import com.tomtop.bo.CountryBo;
import com.tomtop.bo.CurrencyBo;
import com.tomtop.configuration.CartSettings;
import com.tomtop.dto.Country;
import com.tomtop.dto.ISOCountry;
import com.tomtop.dto.base.Website;
import com.tomtop.services.base.IWebsiteService;
import com.tomtop.valueobjects.Constants;
import com.tomtop.valueobjects.base.LoginContext;
import com.tomtop.valueobjects.product.ProductBasePriceReviewInfoVo;

@Service
public class FoundationService {

	public static final String COOKIE_LANG = "TT_LANG";
	// public static final String PLAY_LANG = "PLAY_LANG";
	public static final String COOKIE_DEVICE = "TT_DEVICE";
	public static final String COOKIE_CURRENCY = "TT_CURR";
	public static final String COOKIE_COUNTRY = "TT_COUN";
	public static final String COOKIE_SHIPTO = "TT_SHIPTO";

	public static final String TT_LTC = "TT_LTC";
	public static final String TT_TOKEN = "TT_TOKEN";
	public static final String TT_UUID = "TT_UUID";
	public static final String STORAGE_ID = "storageid";
	public static final String WEB_HISTORY = "WEB-history";

	public static final String DOMAIN = ".chicuu.com";
	public static final String HOST = "chicuu.com";

	public static final String BASE_URL = "http://base.api.tomtop.com";
	public static final String MEMBER_URL = "http://member.api.tomtop.com";
	public static final String PRODUCT_URL = "http://product.api.tomtop.com";
	public static final String LOYALTY_URL = "http://loyalty.api.chicuu.com";

//	public static final Pattern PATTERN = Pattern
//			.compile("\\w+\\.([\\w]+)\\..*");
	public static final Pattern PATTERN = Pattern
			.compile("(\\w+\\.)*([\\w]+)\\.");
	
	// 提取host倒数第二个"."和倒数第一个"."中间的字符串,移动端以"m."开头
	// 如:移动="m.cart.chicuu.com:9005/",pc="cart.chicuu.com:9005/"
	public static final Pattern PATTERN_MOBILE = Pattern
			.compile("(\\w+\\.)*([\\w]+)\\.");

	private List<ISOCountry> isocountryList;

	Logger logger = LoggerFactory.getLogger(FoundationService.class);

	@Autowired
	HttpRequestFactory requestFactory;

	@Autowired
	IWebsiteService wsService;

	@Value("${getLoginContextUrl}")
	String getLoginContextUrl;

	@Value("${payment.logo}")
	String logo;

	@Value("${chicuu.ico}")
	String chicuuIco;

	@Value("${tomtop.ico}")
	String tomtopIco;
	
	@Value("${rcmoment.ico}")
	String rcmomentIco;

	@Autowired
	CartSettings cartSettings;

	/**
	 * 获取当前语言
	 * 
	 * @return
	 */
	public int getLang() {
		int defaultSite = 1;
		String cookie = CookieUtils.getCookie(COOKIE_LANG);
		if(StringUtils.isNotBlank(cookie)){
			try{
				defaultSite =  Integer.parseInt(cookie);
			}catch(Exception ex){
				logger.error("当前站点的值有问题 ------------->",cookie);
			}
		}
		return defaultSite;
	}

	/**
	 * 获取当前站点id
	 * 
	 * @return
	 */
	public int getSiteID() {
		return this.getSite();
	}
	
	/**
	 * TODO 缓存读取数据，读取不到在从数据库读取
	 * 根据终端类型获取当前站点id
	 * @param terminalType 访问终端类型,pc端或移动端
	 * @return
	 */
	public int getSiteID(String terminalType) {
		Website site = null;
		if(Constants.TERMINAL_TYPE_PC.equals(terminalType)){
			site = wsService.getWebsite(this.getVhost());
		}else{
			site = wsService.getWebsite(this.getMobileVhost());
		}
		if (site != null) {
			return site.getIid();
		}
		return 1;
	}

	
	public int getSite(){
		String hostname = this.getHostName();
		int site = 1;
		if(StringUtils.equals("tomtop",hostname)){
			site = 1;
		}else if(StringUtils.equals("rcmoment", hostname)){
			site = 13;
		}else if(StringUtils.equals("chicuu", hostname)){
			site = 10;
		}
		return site;
	}
	
	
	/**
	 * 获取当前客户端id
	 * TODO 基础数据需要从缓存读取，获取不到从接口里面读取数据
	 * @return
	 */
	public int getClientID() {
		return 10;
	}

	/**
	 * 获取当前货币字符串
	 * 
	 * @return
	 */
	public String getCurrency() {
		String cookie = CookieUtils.getCookie(COOKIE_CURRENCY);
		if (StringUtils.isEmpty(cookie)) {
			return "USD";
		} else {
			return cookie;
		}
	}

	/**要修改
	 * 获取当前货币对象
	 * TODO 需要从直接redis里面读取数据，读取不到在数据库里面获取
	 * @return
	 */
	public CurrencyBo getCurrencyBo() {
		String curr = getCurrency();
		String cur = HttpClientUtil.doGet(BASE_URL
				+ "/base/currency/v1/where?code=" + curr);
		if (cur == null || "".equals(cur)) {
			return null;
		}
		JSONObject object = JSON.parseObject(cur);
		String arr = object.getJSONArray("data").getJSONObject(0).toJSONString();
		CurrencyBo currbo = JSON.parseObject(arr, CurrencyBo.class);
		return currbo;
	}


	/**要修改
	 * 获取所有国家信息，（国家下拉框作用）
	 * TODO 直接冲redis里面读取数据，数据读取不到在从数据库拿数据
	 * @return
	 */
	public List<ISOCountry> getAllCountries() {
		if (this.isocountryList != null && this.isocountryList.size() > 0) {
			return this.isocountryList;
		} else {
			
			String cur = HttpClientUtil.doGet(BASE_URL + "/base/country/v1");
			if (cur == null || "".equals(cur)) {
				return Lists.newArrayList();
			}
			JSONObject object = JSON.parseObject(cur);
			JSONArray arr = object.getJSONArray("data");
			List<ISOCountry> clist = Lists.newArrayList();
			arr.forEach(c -> {
				CountryBo co = JSON.parseObject(c.toString(), CountryBo.class);
				clist.add(new ISOCountry(co.getId(), co.getName(), co
						.getIsoCodeTwo()));
			});
			this.isocountryList = clist;
			return this.isocountryList;
		}
	}

	/**
	 * 获取当前国家对象
	 * 
	 */
	public Country getCountryObj() {
		Country country = new Country();
		String co = CookieUtils.getCookie(COOKIE_SHIPTO);
		if (StringUtils.isEmpty(co)) {
			country.setCname("United States");
			country.setCshortname("US");
			return country;
		}
		try {
			co = URLDecoder.decode(co, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
		}
		String[] arr = co.split("|");
		if (arr.length > 0) {
			country.setCname(arr[0]);
			country.setCshortname(arr[1]);
		} else {
			country.setCname("United States");
			country.setCshortname("US");
		}
		return country;
	}

	/**
	 * 获取浏览历史记录
	 * 
	 * @return
	 */
	public List<ProductBasePriceReviewInfoVo> getHistoryProducts() {
		String co = CookieUtils.getCookie("WEB-history");
		if (StringUtils.isNotEmpty(co)) {
			String history = co;
			try {
				history = URLDecoder.decode(history, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.error(e.toString());
			}
			int lang = getLang();
			int siteid = getSiteID();
			String currency = getCurrency();
			String data = HttpClientUtil.doGet(PRODUCT_URL
					+ "/ic/v1/products?listingIds=" + history + "&lang=" + lang
					+ "&client=" + siteid + "&currency=" + currency);
			if (data != null) {
				logger.debug("dd===" + data);
				JSONObject object = JSON.parseObject(data);
				JSONArray arr = object.getJSONArray("data");
				List<ProductBasePriceReviewInfoVo> list = JSON.parseArray(
						arr.toJSONString(), ProductBasePriceReviewInfoVo.class);
				return list;
			}
			return Lists.newArrayList();
		} else {
			return Lists.newArrayList();
		}
	}

	public LoginContext getLoginContext() {
		// do cache
		LoginContext ctx = Request.currentLoginCtx();

		if (ctx == null) {

			ctx = new LoginContext(null, null, null, 1, null);

			HttpServletRequest crequest = Request.currentRequest();
			try {
				// 调用member接口取登录信息

				String urlString = getLoginContextUrl;

				if (urlString == null || urlString.length() == 0) {
					throw new NullPointerException(
							"can not get config:sendEventUrl");
				}
				GenericUrl url = new GenericUrl(urlString);

				HttpRequest request = requestFactory.buildGetRequest(url);
				HttpHeaders headers = new HttpHeaders();
				String cookie = crequest.getHeader("Cookie");
				headers.setCookie(cookie);
				request.setHeaders(headers);

				String result = request.execute().parseAsString();
				JSONObject json = JSONObject.parseObject(result);
				Integer ret = json.getInteger("ret");
				if (1 == ret) {
					String data = json.getString("data");
					ctx = JSONObject.parseObject(data, LoginContext.class);
				}
			} catch (Exception e) {
				logger.error("getLoginContext from member error", e);
			}
			Request.setLoginCtx(ctx);
		}
		return ctx;
	}


	public int getLanguage() {
		return this.getLang();
	}

	public String getVhost() {
		String host = Request.currentRequest().getHeader(Constants.HOST);
		Matcher matcher = PATTERN.matcher(host);
		if (matcher.find()) {
			//如果是m端
			String hostarr[] = host.split("\\.");
			if(hostarr.length>0 && "m".equals(hostarr[0])){
				return "m." + matcher.group(2) + ".com";
			}
			return "www." + matcher.group(2) + ".com";
		}
		return host;
	}
	
	/**
	 * 获取移动端访问的域名(提取host倒数第二个"."和倒数第一个"."中间的字符串)
	 * @return
	 */
	public String getMobileVhost() {
		String host = Request.currentRequest().getHeader(Constants.HOST);
		Matcher matcher = PATTERN_MOBILE.matcher(host);
		if (matcher.find()) {
			return "www." + matcher.group(2) + ".com";
		}
		
		return host;
	}

	public String getClientIP() {
		String ip = Request.currentRequest().getRemoteAddr();
		return ip;
	}


	/**
	 * 获取主站域名,购物车回到主站时要通过该方法来获取主站域名
	 * 
	 * @author lijun
	 * @return
	 */
	public String getMainDomain() {
		return "http://" + this.getVhost();
	}

	/**
	 * 获取静态资源前缀
	 * 
	 * @author lijun
	 */
	//FIXME  配置re静态
	public String getStaticResourcePrefix() {
		String host = this.getVhost();
		Matcher matcher = PATTERN.matcher(host);
		if (matcher.find()) {
			String hostname = matcher.group(2);
			if ("tomtop".equalsIgnoreCase(hostname)) {
				String[] hostarr = host.split("\\.");
				if(hostarr.length>0 && "m".equals(hostarr[0])){
					return "//static.tomtop-cdn.com/mtomtop/cart/";
				}
				return "//static.tomtop-cdn.com/tomtop/cart/";
			} else {
				String[] hostarr = host.split("\\.");
				if(hostarr.length>0 && "m".equals(hostarr[0])){
					return "//static."+hostname+".com/m"+hostname+"/cart/";
				}
				return "//static." + hostname + ".com/"+hostname+"/cart/";
//				return "//static." + hostname + ".com/cart/";
			}
		}
		return "/";
	}

	public String getHost() {
		String host = Request.currentRequest().getHeader(Constants.HOST);
		return host;
	}

	/**
	 * 获取hostname
	 * 
	 * @return
	 */
	public String getHostName() {
		String host = this.getVhost();
		Matcher matcher = PATTERN.matcher(host);
		if (matcher.find()) {
			return matcher.group(2);
		} else {
			return "tomtop";
		}
	}
	
	/**
	 * 获取移动端访问的hostname,移动端url以"m."开头
	 * 
	 * @return
	 */
	public String getMobileHostName() {
		String host = this.getMobileVhost();
		Matcher matcher = PATTERN_MOBILE.matcher(host);
		if (matcher.find()) {
			return matcher.group(2);
		} else {
			return "tomtop";
		}
	}

	/**
	 * 获取ico图片地址
	 * 
	 * @return
	 */
	public String getIco() {
		String host = this.getHostName();
		if ("chicuu".equals(host)) {
			return chicuuIco;
		}else if ("rcmoment".equals(host)) {
			return rcmomentIco;
		} else {
			return tomtopIco;
		}
	}

	/**
	 * 获取logo用于支付
	 * 
	 * @return
	 */
	public String getLogo() {
		String prefix = this.getStaticResourcePrefix();
		String result = null;
		if (!prefix.endsWith("/")) {
			result = prefix + "/" + logo;
		} else {
			result = prefix + logo;
		}

		return result;
	}

	public String getImgUrlPrefix() {
		String domain = this.getHostName();
		String purl = cartSettings.getPrefix(domain);
		if(purl==null){
			return cartSettings.getPrefix("tomtop");
		}else{
			return purl;
		}
	}

	/**
	 * 获取广告联盟AID
	 * 
	 * @author lijun
	 * @return
	 */
	public String getOrigin() {
		return CookieUtils.getCookie("AID");
	}
	
	/**
	 * 获取域名的开头
	 */
	public String getSubdomains() {
		String host = this.getHost();
		if(host!=null){
			String[] hostarr = host.split("\\.");
			if(hostarr.length>0){
				return hostarr[0];
			}else{
				return "cart";
			}
		}
		return "cart";
	}
	
	/**
	 * 通过Email获取收藏列表的listingd
	 * @param email
	 * @param siteid
	 * @return
	 */
	public String getCollectsByEmail(String email, Integer siteid){
		String collecturl = "/ic/v1/collect/list?email="+email+"&website="+siteid;
		String cur = HttpClientUtil.doGet(FoundationService.PRODUCT_URL + collecturl);
		if(cur!=null && !"".equals(cur)){
			JSONObject object = JSON.parseObject(cur);
			Integer ret = object.getInteger("ret");
			if(ret!=null && ret==1){
				JSONArray js = object.getJSONArray("data");
				Object[] aa = js.toArray();
				StringBuilder sb = new StringBuilder();
				for(Object a : aa){
					sb.append(a.toString()).append(",");
				}
				return sb.toString();
			}else{
				return "";
			}
		}else{
			return "";
		}
	}
	
	/**
	 * 获取资源版本号
	 * @return
	 */
	public String getVersion(){
		return Constants.RESOURCES_VERSION;
	}
	
	/**
	 * 获取第三方登录链接
	 * @return
	 */
	public Map<String,String> getLoginUrl(){
		int siteid = this.getSiteID();
		StringBuffer logingUrlsb = new StringBuffer(MEMBER_URL);
		logingUrlsb.append("/other/v1/signIn?website=").append(siteid);
		String cur = HttpClientUtil.doGet(logingUrlsb.toString());
		if(cur!=null && !"".equals(cur)){
			JSONObject object = JSON.parseObject(cur);
			Integer ret = object.getInteger("ret");
			if(ret!=null && ret==1){
				JSONArray js = object.getJSONArray("data");
				Map<String, String> urlmap = new HashMap<String, String>();
				for(int i=0;i<js.size();i++){
					JSONObject jo = js.getJSONObject(i);
					urlmap.put(jo.getString("type"), jo.getString("url"));
				}
				return urlmap;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
}
