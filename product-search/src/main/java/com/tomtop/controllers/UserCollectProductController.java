package com.tomtop.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.entity.UserCollectProduct;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.IUserCollectProductService;

/**
 * member模块调用 
 * 用户收藏商品详细类
 * 
 * @author renyy
 *
 */
@RestController
public class UserCollectProductController {

	
	@Autowired
	IUserCollectProductService userCollectProductService;
	
	/**
	 * 根据邮箱获取用户收藏的商品列表
	 * @param email 邮箱
	 * @param categoryId 品类Id
	 * @param sort 排序方式
	 * @param productKey 关键字
	 * @param lang 语言Id
	 * @param client 客户端Id
	 * @param currency 币种
	 * @param page 页数
	 * @param size 大小 
	 * @author renyy
	 *
	 */
	@RequestMapping(value = "/ic/v1/user/collect", method = RequestMethod.GET)
	public Result getUserCollectProduct(
			@RequestParam(value="email",required = false, defaultValue = "") String email,
			@RequestParam(value="categoryId", required = false, defaultValue = "0") Integer categoryId,
			@RequestParam(value="sort", required = false, defaultValue = "") String sort,
			@RequestParam(value="productKey", required = false, defaultValue = "") String productKey,
			@RequestParam(value="lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value="client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value="website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value="currency",required = false, defaultValue = "USD") String currency,
			@RequestParam(value="page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value="size", required = false, defaultValue = "10") Integer size
			) {
		UserCollectProduct ucpvo = userCollectProductService.getUserCollectProductList(email, categoryId, sort, productKey, lang, website, currency, page, size);
		Result res = new Result();
		if(ucpvo != null && ucpvo.getUcpList() != null && ucpvo.getUcpList().size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(ucpvo.getUcpList());
			res.setPage(ucpvo.getPage());
		}else{
			res.setRet(Result.FAIL);
			res.setErrMsg("not find collect product");
		}
		
		return res;
	}
}
