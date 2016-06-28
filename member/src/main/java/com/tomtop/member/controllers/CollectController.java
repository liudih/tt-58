package com.tomtop.member.controllers;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.member.models.bo.UpdateBo;
import com.tomtop.member.models.dto.ProductCollect;
import com.tomtop.member.service.ICollectService;
import com.tomtop.member.service.IMemberService;
import com.tomtop.member.utils.CommonUtils;

/**
 * 用户商品收藏
 * 
 * @author xcf
 *
 */
@RestController
@RequestMapping(value = "/collect")
public class CollectController {

	@Autowired
	ICollectService collectService;

	@Autowired
	IMemberService memberService;

	@RequestMapping(value = "/v1/collects", method = RequestMethod.POST)
	public Result addComment(@RequestBody String jsonParam) {
		Result bb = new Result();
		try {
			ObjectMapper om = new ObjectMapper();
			JsonNode jsonNode = om.readTree(jsonParam);

			String uuid = jsonNode.get("uuid").asText();
			Integer websiteId = jsonNode.get("client").asInt();
			String email = jsonNode.get("email").asText();

			boolean flagV = memberService.validate(websiteId, email, uuid);
			if (flagV) {
				JsonNode listingIdsNode = jsonNode.get("listingIds");
				if (listingIdsNode.isArray()) {
					Iterator<JsonNode> nodeiterator = listingIdsNode.iterator();
					while (nodeiterator.hasNext()) {
						String listingId = nodeiterator.next().asText();

						ProductCollect collect = this.collectService
								.getProductCollectByListingIdAndEmail(
										listingId, email, websiteId);

						if (null == collect) {
							// 添加收藏之前需要判定此产品是否已经收藏
							boolean flag = this.collectService.addCollect(
									listingId, email, websiteId);
							if (flag) {
								bb.setRet(1);
							} else {
								bb.setRet(-1);
							}
						} else {
							bb.setRet(-1);
							bb.setData("This product has been Collect");
						}

					}
				}

			} else {
				bb.setRet(-1);
				bb.setData("uuid is not valid");
			}

		} catch (Exception e) {
			bb.setRet(-1);
			bb.setData("addComment add faile" + e.getMessage());
			e.printStackTrace();
		}

		return bb;
	}

	@RequestMapping(value = "/v1/collects/_delete/_ids", method = RequestMethod.POST)
	public Result deleteCommentByIds(@RequestBody String jsonParam) {

		Result bb = new Result();
		try {
			ObjectMapper om = new ObjectMapper();
			JsonNode jsonNode = om.readTree(jsonParam);

			String uuid = jsonNode.get("uuid").asText();
			Integer websiteId = jsonNode.get("client").asInt();
			String email = jsonNode.get("email").asText();

			boolean flagV = memberService.validate(websiteId, email, uuid);
			if (flagV) {
				JsonNode idsNode = jsonNode.get("ids");
				if (idsNode.isArray()) {
					Iterator<JsonNode> nodeiterator = idsNode.iterator();
					while (nodeiterator.hasNext()) {
						Integer id = nodeiterator.next().asInt();

						ProductCollect collect = this.collectService
								.getProductCollectByIid(id);
						if (null == collect) {
							bb.setRet(-1);
							bb.setData("The collection id does not exist" + id);
							continue;
						}
						if (email.equals(collect.getCemail())) {
							// 删除之前需要确认此收藏是否属于此会员的
							boolean flag = this.collectService
									.delCollectByIid(id);
							if (flag) {
								bb.setRet(1);
							} else {
								bb.setRet(-1);
							}
						} else {
							bb.setRet(-1);
							bb.setData("email is not valid, Delete does not belong to their own collection,id:"
									+ id);
							break;

						}
					}
				}

			} else {
				bb.setRet(-1);
				bb.setData("uuid is not valid");
			}

		} catch (Exception e) {
			bb.setRet(-1);
			bb.setData("comment delete faile" + e.getMessage());
			e.printStackTrace();
		}
		return bb;
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/v1/collects/_delete/_listingIds", method = RequestMethod.POST)
	public Result deleteCommentByListingIds(@RequestBody String jsonParam) {

		Result bb = new Result();
		try {
			ObjectMapper om = new ObjectMapper();
			JsonNode jsonNode = om.readTree(jsonParam);

			String uuid = jsonNode.get("uuid").asText();
			Integer websiteId = jsonNode.get("client").asInt();
			String email = jsonNode.get("email").asText();

			boolean flagV = memberService.validate(websiteId, email, uuid);
			if (flagV) {
				JsonNode idsNode = jsonNode.get("listingIds");
				if (idsNode.isArray()) {
					Iterator<JsonNode> nodeiterator = idsNode.iterator();
					while (nodeiterator.hasNext()) {
						String listingId = nodeiterator.next().asText();

						ProductCollect collect = this.collectService
								.getProductCollectByListingIdAndEmail(listingId, email, websiteId);
						if (null == collect) {
							bb.setRet(-1);
							bb.setData("The collection id does not exist" + listingId+"-"+email);
							continue;
						}
						if (email.equals(collect.getCemail())) {
							// 删除之前需要确认此收藏是否属于此会员的
							boolean flag = this.collectService
									.delCollect(listingId, email, websiteId);
							if (flag) {
								bb.setRet(1);
							} else {
								bb.setRet(-1);
							}
						} else {
							bb.setRet(-1);
							bb.setData("email is not valid, Delete does not belong to their own collection,id:"
									+ listingId);
							break;

						}
					}
				}

			} else {
				bb.setRet(-1);
				bb.setData("uuid is not valid");
			}

		} catch (Exception e) {
			bb.setRet(-1);
			bb.setData("comment delete faile" + e.getMessage());
			e.printStackTrace();
		}
		return bb;
	}
	
	/**
	 * 用户删除收藏商品
	 * 
	 * @author renyy
	 *
	 */
	@RequestMapping(value = "/v1/collects/delete", method = RequestMethod.POST)
	public Result deleteCollect(@RequestBody UpdateBo ub) {
		Result bb = new Result();
		try {
			if(!"".equals(ub.getIds())){
				String[] list = ub.getIds().split(","); 
				for (String listid : list) {
					collectService.delCollect(listid, ub.getEmail(), ub.getWebsite());
				}
			}else{
				bb.setRet(CommonUtils.COLLECT_IDS_NOT_FIND);
				bb.setErrMsg("ids is null");
			}
		
			bb.setRet(CommonUtils.SUCCESS_RES);
		} catch (Exception e) {
			bb.setRet(CommonUtils.EXCEPTION);
			bb.setData("delete collects faile" + e.getMessage());
			e.printStackTrace();
		}
		return bb;
	}

}
