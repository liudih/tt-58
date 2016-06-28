package com.tomtop.services.product;

import java.util.List;

import org.apache.commons.chain.web.WebContext;

import com.tomtop.dto.product.ProductBase;
import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.product.Product4API;
import com.tomtop.valueobjects.product.ProductLite;
import com.tomtop.valueobjects.product.Weight;
import com.tomtop.valueobjects.product.price.PriceNew;

public interface IProductService {

	ProductBase getBaseByListingIdAndLanguage(String listingId,
			Integer languageId);

	ProductBase getBaseByListingId(String listingId);

	String getListingIdByParentSkuAndWebsiteIdAndStatusAndIsMain(
			String parentsku, Integer isstatus, Integer websiteId,
			boolean ismain);

	List<ProductBase> getProductsWithSameParentSkuMatchingAttributes(
			String listingID, WebContext context);

	List<String> selectListingidBySearchNameAndSort(WebContext context,
			String searchname, String sort, Integer categoryId,
			List<String> pcListingIds);

	/**
	 * 获取描述外的信息
	 * 
	 * @param listingId
	 * @param languageId
	 * @return
	 */
	ProductBase getProductByListingIdAndLanguageWithdoutDesc(String listingId,
			Integer languageId);

	/**
	 * 分段获取描述，描述信息量大一次获取不全
	 * 
	 * @param listingId
	 * @param languageId
	 * @param begin
	 * @param len
	 * @return
	 */
	String getProductDescByListingIdAndLanguagePart(String listingId,
			Integer languageId, int begin, int len);

	/**
	 * @param listingids
	 * @return
	 */
	List<ProductBase> getProductBasesByListingIds(List<String> listingids);

	public List<ProductLite> getProductLiteByListingIDs(
			List<String> listingIDs, int websiteID, int languageID);

	/**
	 * 
	 * @Title: getProductBaseBySkus
	 * @Description: TODO(通过SKU列表查询产品列表)
	 * @param @param skus
	 * @param @param siteid
	 * @param @return
	 * @return List<ProductBase>
	 * @throws
	 * @author yinfei
	 */
	public List<ProductBase> getProductBaseBySkus(List<String> skus,
			Integer siteid);

	public int getCountBundleProduct(String main, String bundle);

	public List<Weight> getWeightList(List<String> listingIds);

	public boolean checkInventory(String listingID, Integer qty);

	/**
	 * 从接口获取批量产品信息集合
	 * 
	 * @param listingIdsStr
	 *            产品唯一标识字符串,多个逗号分隔
	 * @param lang
	 *            语言标识
	 * @param website
	 *            站点标识
	 * @param storage
	 *            仓库标识
	 * @return 产品信息集合
	 * @author shuliangxing
	 * @date 2016年6月3日 下午5:46:07
	 */
	public List<Product4API> getProductListFromAPI(String listingIdsStr,
			int lang, int website, int storage);

	/**
	 * 从接口获取批量产品信息集合,实现缓存
	 * 
	 * @param listingIds
	 *            产品唯一标识集合
	 * @param lang
	 *            语言标识
	 * @param website
	 *            站点标识
	 * @param storage
	 *            仓库标识
	 * @return 产品信息集合
	 * @author shuliangxing
	 * @date 2016年6月3日 下午5:46:07
	 */
	public List<Product4API> getProductListFromAPI(List<CartItem> items,
			int lang, int website, int storage);

	/**
	 * 从接口获取批量产品信息集合,实现缓存
	 * 
	 * @param listingIds
	 *            产品唯一标识集合
	 * @param lang
	 *            语言标识
	 * @param website
	 *            站点标识
	 * @param storage
	 *            仓库标识
	 * @return 产品信息集合
	 * @author shuliangxing
	 * @date 2016年6月3日 下午5:46:07
	 */
	public List<ProductLite> getProductLiteListFromAPI(List<CartItem> items,
			int lang, int website, int storage);
	
	/**
	 * 从缓存获取单个产品信息
	 * 
	 * @param listingId
	 *            产品唯一标识字符串
	 * @param lang
	 *            语言标识
	 * @param website
	 *            站点标识
	 * @param storage
	 *            仓库标识
	 * @return 产品信息集合
	 * @author shuliangxing
	 * @date 2016年6月3日 下午5:46:07
	 */
	public Product4API getProductFromCache(String listingId, int lang,
			int website, int storage);

	/**
	 * 更新缓存
	 * @param product4API 产品对象
	 * @param lang 语言
	 * @param website 站点
	 * @param storage 仓库
	 * @return
	 */
	public Product4API putProductCache(Product4API product4API, int lang,
			int website, int storage);

	/**
	 * 根据产品标识和仓库id查询价格集合
	 * 
	 * @param listingList
	 *            产品标识集合
	 * @param storageId
	 *            仓库id
	 * @return 价格集合
	 * @author shuliangxing
	 * @date 2016年6月8日 下午5:39:15
	 */
	List<PriceNew> queryPrice(List<String> listingList, Integer storageId);

	/**
	 * 根据产品标识和仓库id查询价格
	 * 
	 * @param listingId
	 *            产品标识
	 * @param storageId
	 *            仓库id
	 * @return 价格
	 * @author shuliangxing
	 * @date 2016年6月8日 下午5:39:15
	 */
	PriceNew queryPrice(String listingId, Integer storageId);
}
