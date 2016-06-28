package com.tomtop.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.entity.BaseBean;
import com.tomtop.entity.CollectCount;
import com.tomtop.entity.PrdouctDesc;
import com.tomtop.entity.ProductBase;
import com.tomtop.entity.ProductBaseDepotDtl;
import com.tomtop.entity.ProductBaseDtl;
import com.tomtop.entity.ProductCollectVo;
import com.tomtop.entity.ProductDetails;
import com.tomtop.entity.ProductPrice;
import com.tomtop.entity.ProductSeo;
import com.tomtop.entity.ReportErrorVo;
import com.tomtop.entity.TopicPageBo;
import com.tomtop.entity.WholesaleInquiryVo;
import com.tomtop.entity.WholesaleProductVo;
import com.tomtop.entity.index.ReviewStartNum;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.IDropshipProductService;
import com.tomtop.services.IEsProductDetailService;
import com.tomtop.services.IFeedbackService;
import com.tomtop.services.IProductCollectService;
import com.tomtop.services.IProductDetailService;
import com.tomtop.services.IProductPriceService;
import com.tomtop.services.ITopicPageService;
import com.tomtop.services.IWholeSaleProductService;
import com.tomtop.utils.CommonsUtil;

/**
 * 产品详情控制类
 * 
 * @author renyy
 *
 */
@RestController
@RequestMapping(value = "/ic")
public class ProductDetailsController {

	//private static final Logger logger = LoggerFactory
	//		.getLogger(ProductDetailsController.class);
	@Autowired
	IProductDetailService productDetailService;
	@Autowired
	IProductCollectService productCollectService;
	@Autowired
	ITopicPageService topicPageService;
	@Autowired
	IFeedbackService feedbackService;
	@Autowired
	IDropshipProductService dropshipProductService;
	@Autowired
	IWholeSaleProductService wholeSaleProductService;
	@Autowired
	IEsProductDetailService esPrdocutDetailService;
	@Autowired
	IProductPriceService productPriceService;
	
	private final static String paymentExplain = "paymentexplain";
	
	private final static String warrantyExplain = "warrantyexplain";
	
	/**
	 * 获取商品详情基本信息(第三版)
	 * 
	 * @param sku
	 *            分别可以为 商品sku or listingId or url
	 * @param lang
	 *            语言ID
     * @param client
	 *            客户端ID
	 * 	@param website
	 *            站点ID
	 * @param client
	 *            币种
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v3/product/base")
	public Result getProductDtlSearchV3(
			@RequestParam("key") String key,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency) {
		Result res = new Result();
		if(key == null || "".equals(key.trim())){
			res.setRet(Result.FAIL);
			res.setErrCode("404");
			res.setErrMsg("key is null");
			return res;
		}
		key = CommonsUtil.checkSpecialChar(key);
		ProductBaseDepotDtl pbdtl = esPrdocutDetailService.getProductBaseDepotDtl(key, lang, website, currency);

		if(pbdtl.getRes() == Result.SUCCESS){
			res.setRet(Result.SUCCESS);
			pbdtl.setRes(null);
			res.setData(pbdtl);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode(res.getErrCode());
			res.setErrMsg(res.getErrMsg());
		}
		return res;
	}
	
	/**
	 * 获取商品Hot标签  top (第二版)
	 * 
	 * @param type
	 *            type
	 * @param lang
	 *            语言ID
	 * @param client
	 *            客户端ID
	 * 	@param website
	 *            站点ID
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v2/product/hot")
	public Result getProductHot(
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "size", required = false, defaultValue = "5") Integer size,
			@RequestParam(value = "depotName", required = false, defaultValue = "CN") String depotName) {
		List<ProductBase> phboList = esPrdocutDetailService.getProductHotBoList(languageid, website, currency,size,depotName);
		Result res = new Result();
		if(phboList != null && phboList.size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(phboList);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("33013");
			res.setErrMsg("product hot not find");
		}
		return res;
	}
	
	/**
	 * 获取商品详情基本信息
	 * 
	 * @param sku
	 *            分别可以为 商品sku or listingId or url
	 * @param lang
	 *            语言ID
     * @param client
	 *            客户端ID
	 * 	@param website
	 *            站点ID
	 * @param client
	 *            币种
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v2/product/base")
	public Result getProductDtlSearch(
			@RequestParam("key") String key,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency) {
		Result res = new Result();
		if(key == null || "".equals(key.trim())){
			res.setRet(Result.FAIL);
			res.setErrCode("404");
			res.setErrMsg("key is null");
			return res;
		}
		key = CommonsUtil.checkSpecialChar(key);
		ProductBaseDtl pbdvo = esPrdocutDetailService.getProductBaseDtlVo(key, lang, website, currency);

		if(pbdvo.getRes() == Result.SUCCESS){
			res.setRet(Result.SUCCESS);
			pbdvo.setRes(null);
			res.setData(pbdvo);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode(res.getErrCode());
			res.setErrMsg(res.getErrMsg());
		}
		return res;
	}
	
	/**
	 * 获取商品详情基本信息
	 * 
	 * @param sku
	 *            分别可以为 商品sku or listingId or url
	 * @param lang
	 *            语言ID
	 * @param client
	 *            客户端ID
	 * 	@param website
	 *            站点ID
	 * @param client
	 *            币种
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/base/{sku}")
	public Result getProductDtl(
			@PathVariable("sku") String sku,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency) {
		List<ProductDetails> pdbList = esPrdocutDetailService.getProductDetailsBoList(sku, languageid, website, currency);
		Result res = new Result();
		if(pdbList != null && pdbList.size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(pdbList);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("33001");
			res.setErrMsg("details not find");
		}
		return res;
	}
	/**
	 * 获取商品的desc
	 * 
	 * @param sku
	 *             分别可以为 商品sku or listingId or url
	 * @param lang
	 *            语言ID
	 * @param client
	 *            客户端ID
	 * 	@param website
	 *            站点ID
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/desc/{sku}")
	public Result getProductDescription(
			@PathVariable("sku") String sku,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website) {
		PrdouctDesc pdb = esPrdocutDetailService.getPrdouctDescBo(sku, languageid, website);
		Result res = new Result();
		if(pdb != null && pdb.getRes() == 1){
			String payEx = productDetailService.getProductExplainByType(paymentExplain, website, languageid);
			String warEx = productDetailService.getProductExplainByType(warrantyExplain, website, languageid);
			pdb.setShippingPayment(payEx);
			pdb.setWarranty(warEx);
			res.setRet(Result.SUCCESS);
			pdb.setRes(null);
			res.setData(pdb);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("33002");
			res.setErrMsg("desc not find");
		}
		return res;
	}
	
	/**
	 * 获取商品的SEO
	 * 
	 * @param sku
	 *             分别可以为 商品sku or listingId or url
	 * @param lang
	 *            语言ID
	 * @param client
	 *            客户端ID
	 * 	@param website
	 *            站点ID
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/seo/{sku}")
	public Result getProductSeo(
			@PathVariable("sku") String sku,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website) {
		ProductSeo psb = esPrdocutDetailService.getProductSeoBo(sku, languageid, website);
		Result res = new Result();
		if(psb != null && psb.getRes() == 1){
			res.setRet(Result.SUCCESS);
			psb.setRes(null);
			res.setData(psb);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("33003");
			res.setErrMsg("seo not find");
		}
		return res;
	}
	
	/**
	 * 获取商品评论星级和数量(从搜索引擎找)
	 * 
	 * @param listingId
	 *            商品listingId
	 * @param lang
	 *            语言ID
	 * @param client
	 *            客户端ID
	 * 	@param website
	 *            站点ID
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/review/start/{listingId}")
	public Result getProductStartNum(
			@PathVariable("listingId") String listingId,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website) {
		ReviewStartNum rsnbo = esPrdocutDetailService.getReviewStartNumBoById(listingId,languageid,website);
		Result res = new Result();
		if(rsnbo != null){
			res.setRet(Result.SUCCESS);
			res.setData(rsnbo);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("33004");
			res.setErrMsg("product start num not find");
		}
		return res;
	}
	
	/**
	 * 获取商品收藏数(从数据库里找)
	 * 
	 * @param listingId
	 *            商品listingId
	 * @param lang
	 *            语言ID
	 * @param client
	 *            客户端ID
	 * 	@param website
	 *            站点ID
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/collect/{listingId}")
	public Result getProductCollectNum(
			@PathVariable("listingId") String listingId,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website) {
		CollectCount ccb = productCollectService.getCollectCountByListingId(listingId,website);
		Result res = new Result();
		if(ccb != null){
			res.setRet(Result.SUCCESS);
			ccb.setRes(null);
			res.setData(ccb);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("33005");
			res.setErrMsg("product collect num not find");
		}
		return res;
	}
	
	/**
	 * 获取商品价格接口
	 * 
	 * @param listingId
	 *            商品listingId
	 * @param lang
	 *            语言ID
	 * @param client
	 *            客户端ID
	 * 	@param website
	 *            站点ID
	 * @param client
	 *            币种
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/price/{listingId}")
	public Result getProductPrice(
			@PathVariable("listingId") String listingId,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency) {
		Result res = new Result();
		if(listingId == null){
			res.setRet(Result.FAIL);
			res.setErrCode("33006");
			res.setErrMsg("listingid is null");
			return res;
		}
		ProductPrice pp = productPriceService.getProductPrice(listingId, website, currency);
		if(pp != null){
			res.setRet(Result.SUCCESS);
			res.setData(pp);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("33007");
			res.setErrMsg("product price not find");
		}
		return res;
	}
	
	/**
	 * 获取商品详情explain
	 * 
	 * @param type
	 *            type
	 * @param lang
	 *            语言ID
	 * @param client
	 *            客户端ID
	 * 	@param website
	 *            站点ID
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/explain/{type}")
	public Result getProductExplainShippingPayment(
			@PathVariable("type") String type,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website) {
		String explain = productDetailService.getProductExplainByType(type, website, languageid);
		Result res = new Result();
		if(explain != null && !"".equals(explain)){
			res.setRet(Result.SUCCESS);
			res.setData(explain);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("33010");
			res.setErrMsg("product explain not find");
		}
		return res;
	}
	
	/**
	 * 获取商品Hot Events最新5个专题
	 * 
	 * @param type
	 *            type
	 * @param lang
	 *            语言ID
	 * @param client
	 *            客户端ID
	 * 	@param website
	 *            站点ID
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/topic")
	public Result getTopicPage(
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "size", required = false, defaultValue = "5") Integer size) {
		List<TopicPageBo> tpboList = topicPageService.filterTopicPage(type, website, languageid, null, null, size);
		Result res = new Result();
		if(tpboList != null && tpboList.size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(tpboList);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("33012");
			res.setErrMsg("product topic page not find");
		}
		return res;
	}
	
	/**
	 * 获取商品Hot标签  top 5个
	 * 
	 * @param type
	 *            type
	 * @param lang
	 *            语言ID
	 * @param client
	 *            客户端ID
	 * 	@param website
	 *            站点ID
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/hot")
	public Result getProductHot(
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "size", required = false, defaultValue = "5") Integer size) {
		List<ProductBase> phboList = esPrdocutDetailService.getProductHotBoList(languageid, website, currency,size);
		Result res = new Result();
		if(phboList != null && phboList.size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(phboList);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("33013");
			res.setErrMsg("product hot not find");
		}
		return res;
	}
	
	/**
	 * 详情页面中 添加 Wholesale Inquiry 
	 * 
	 * @param WholesaleInquiryVo
	 * 
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/v1/product/wholesaleInquiry")
	public Result putWholesaleInquiry(@RequestBody WholesaleInquiryVo wivo) {
		BaseBean bb = feedbackService.addWholesaleInquiry(wivo);
		Result res = new Result();
		if(bb.getRes() == Result.SUCCESS){
			res.setRet(Result.SUCCESS);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode(bb.getRes().toString());
			res.setErrMsg(bb.getMsg());
		}
		return res;
	}
	
	/**
	 * 详情页面中 添加 Report Error
	 * 
	 * @param ReportErrorVo
	 * 
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/v1/product/reportError")
	public Result putReportError(@RequestBody ReportErrorVo revo) {
		BaseBean bb = feedbackService.addReportError(revo);
		Result res = new Result();
		if(bb.getRes() == Result.SUCCESS){
			res.setRet(Result.SUCCESS);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode(bb.getRes().toString());
			res.setErrMsg(bb.getMsg());
		}
		return res;
	}
	
	/**
	 * 详情页面中 添加 收藏
	 * 
	 * @param ProductCollectVo
	 * 
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/v1/product/collect/add")
	public Result putProductCollect(@RequestBody ProductCollectVo pcvo) {
		BaseBean bb = productCollectService.addCollectCount(pcvo.getListingId(), pcvo.getEmail(),pcvo.getWebsite());
		Result res = new Result();
		if(bb.getRes() == Result.SUCCESS){
			res.setRet(Result.SUCCESS);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode(bb.getRes().toString());
			res.setErrMsg(bb.getMsg());
		}
		return res;
	}
	
	/**
	 * 详情页面中 添加 dropship 
	 * 
	 * @param ProductCollectVo
	 * 
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/v1/product/dropship/add")
	public Result putProductDropship(@RequestBody ProductCollectVo pcvo) {
		BaseBean bb = dropshipProductService.addProductDropshipBySku(pcvo.getEmail(), pcvo.getSku(), pcvo.getWebsite());
		Result res = new Result();
		if(bb.getRes() == Result.SUCCESS){
			res.setRet(Result.SUCCESS);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode(bb.getRes().toString());
			res.setErrMsg(bb.getMsg());
		}
		return res;
	}
	
	/**
	 * 详情页面中 添加 WholeSaleProduct 
	 * 
	 * @param ProductCollectVo
	 * 
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/v1/product/wholesale/add")
	public Result putWholeSaleProduct(@RequestBody WholesaleProductVo wpvo) {
		String email = wpvo.getEmail();
		String sku = wpvo.getSku();
		Integer qty = wpvo.getQty();
		Integer client = wpvo.getClient();
		BaseBean bb = wholeSaleProductService.addWholeSaleProduct(email, sku, qty, client);
		Result res = new Result();
		if(bb.getRes() == Result.SUCCESS){
			res.setRet(Result.SUCCESS);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode(bb.getRes().toString());
			res.setErrMsg(bb.getMsg());
		}
		return res;
	}
	
	/**
	 * 获取库存接口
	 * 
	 * @param listingId
	 *            商品listingId
	 * @param client
	 *            客户端ID
	 * 	@param website
	 *            站点ID         
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/qty/{listingId}")
	public Result getProductQty(@PathVariable("listingId") String listingId,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website) {
		Result res = new Result();
		if(listingId == null){
			res.setRet(Result.FAIL);
			res.setErrCode("33015");
			res.setErrMsg("listingid is null");
			return res;
		}
		Integer qty = productDetailService.getProductQty(listingId,website);
		if(qty == null){
			qty = 0;
		}
		res.setRet(Result.SUCCESS);
		res.setData(qty);
		return res;
	}
}
