package com.tomtop.member.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.MultipartContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.common.collect.Maps;
import com.tomtop.framework.core.utils.Page;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.member.models.bo.InteractionPhotoBo;
import com.tomtop.member.models.bo.InteractionVideoBo;
import com.tomtop.member.models.dto.InteractionComment;
import com.tomtop.member.models.dto.InteractionProductMemberPhotos;
import com.tomtop.member.models.dto.InteractionProductMemberVideo;
import com.tomtop.member.models.dto.ReviewsInMemberCenter;
import com.tomtop.member.service.IInteractionProductPhotosService;
import com.tomtop.member.service.IInteractionProductVideoService;
import com.tomtop.member.service.IMemberReviewsService;
import com.tomtop.member.service.IMemberService;
import com.tomtop.member.service.IPointsService;
import com.tomtop.member.utils.CommonUtils;

/**
 * 用户评论类
 * 
 * @author xcf
 *
 */
@RestController
@RequestMapping(value = "/review")
public class CommentController {

	@Autowired
	IMemberReviewsService memberReviewsService;

	@Autowired
	IMemberService memberService;

	@Autowired
	IPointsService pointsService;

	@Value("${secure.token}")
	private String token;

	@Value("${cdnImageUrl}")
	private String cdnImageUrl;

	@Autowired
	IInteractionProductPhotosService photoService;

	@Autowired
	IInteractionProductVideoService videoService;

	/**
	 * 用户email站点信息获取会员评论信息
	 * 
	 * @param uuid
	 */
	@ResponseBody
	@RequestMapping(value = "/v1/reviews", method = RequestMethod.GET)
	public Result getMemberReviewsByEmailAndWebsiteId(
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "uuid") String uuid,
			@RequestParam(value = "email") String email,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
			@RequestParam(value = "status", required = false, defaultValue = "-1") Integer status,
			@RequestParam(value = "dateType", required = false, defaultValue = "0") Integer dateType) {
		Result res = new Result();
		try {
			boolean flagV = memberService.validate(client, email, uuid);
			if (flagV) {
				//获取记录总数
				Integer totalReviewsCount = memberReviewsService
						.getTotalReviewsCountByMemberEmailAndSiteId(email,
								status, dateType, client);
				//获取评论列表
				List<ReviewsInMemberCenter> base = this.memberReviewsService
						.getMyReviewsPageByEmail(email, page, limit, status,
								dateType, client);

				Page pageObj = Page.getPage(page, totalReviewsCount, limit);

				res.setPage(pageObj);
				res.setData(base);
				res.setRet(CommonUtils.SUCCESS_RES);
			} else {
				res.setRet(CommonUtils.ERROR_RES);
				res.setErrCode("500");
				res.setErrMsg("email is not valid");
			}

		} catch (Exception e) {
			e.printStackTrace();
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode("500");
			res.setErrMsg("getMemberReviews error" + e.getMessage());
		}
		return res;
	}

/*	@ResponseBody
	@RequestMapping(value = "/v1/reviews/_delete/{iid}/{uuid}", method = RequestMethod.POST)
	public Result deleteCommentById(@PathVariable("iid") Integer iid,
			@PathVariable("uuid") String uuid) {

		Result bb = new Result();

		try {
			InteractionComment comment = memberReviewsService
					.getReviewById(iid);

			if(null == comment){
				bb.setRet(-1);
				bb.setData("iid is not valid");
				return bb;
			}
			boolean flagV = memberService.validate(comment.getIwebsiteid(),
					comment.getCmemberemail(), uuid);
			if (flagV) {
				if (comment.getIstate() == 0) { // 状态为未审核时才能删除
					boolean flag = memberReviewsService
							.deleteInteractionComment(iid);
					if (flag) {
						bb.setRet(1);
					} else {
						bb.setRet(0);
					}
				} else {
					bb.setRet(-1);
					bb.setErrMsg("comment status is not delete");
				}
			} else {
				bb.setRet(-2);
				bb.setErrMsg("uuid is not valid");
			}

		} catch (Exception e) {
			bb.setRet(-3);
			bb.setErrCode("500");
			bb.setErrMsg("comment delete faile" + e.getMessage());
			e.printStackTrace();
		}
		return bb;
	}*/
	
	@RequestMapping(value = "/v1/review/_update", method = RequestMethod.POST)
	public Result updateCommentById(
			@RequestParam("clistingid") String clistingid,
			@RequestParam(value = "csku", defaultValue = "", required = false) String csku,
			@RequestParam("ccomment") String ccomment,
			@RequestParam("ipriceStarWidth") Integer iprice,
			@RequestParam("iqualityStarWidth") Integer iquality,
			@RequestParam("ishippingStarWidth") Integer ishipping,
			@RequestParam("iusefulness") Integer iusefulness,
			@RequestParam(value = "commentVideoUrl", defaultValue = "", required = false) String videoUrl,
			@RequestParam(value = "videoTitle", defaultValue = "", required = false) String videoTitle,
			@RequestParam("uuid") String uuid,
			@RequestParam("client") Integer websiteId,
			@RequestParam("email") String email,
			@RequestParam("commentid") Integer iid,
			@RequestParam(value = "commentPhotosUrl", defaultValue = "", required = false) String[] imageUrls,
			MultipartHttpServletRequest request) {
		Result res = new Result();
		try {
			if(ccomment == null || ccomment.length() > 500 ){
				res.setRet(CommonUtils.COMMENT_LENT_ERR);
				res.setErrMsg("comment length over ");
				return res;
			}
			InteractionComment comment = memberReviewsService.getReviewById(iid,email);
			if(null == comment){
				res.setRet(-1);
				res.setErrMsg("iid is not valid");
				return res;
			}
			boolean flagV = memberService.validate(comment.getClient(),
					comment.getCmemberemail(), uuid);
			if (flagV) {
				if (comment.getIstate() == 0) { // 状态为未审核时才能修改
					comment.setIid(iid);
					comment.setCcomment(ccomment);
					comment.setIprice(iprice);
					comment.setIquality(iquality);
					comment.setIshipping(ishipping);
					comment.setIusefulness(iusefulness);
					boolean flag = memberReviewsService.updateInteractionComment(comment);
					if (flag) {
						
						// 评论视频
						InteractionProductMemberVideo video = this.videoService.
								getVideoByCommentIdAndEmailAndSiteId(comment.getIid(), email, websiteId);
						
						if(null == video){
							video= new InteractionProductMemberVideo();
							video.setClistingid(clistingid);
							video.setCsku(csku);
							video.setClabel("comment");
							video.setIwebsiteid(websiteId);
							video.setCmemberemail(email);
							video.setIcomment(comment.getIid());
							video.setCvideourl(videoUrl);
							video.setClabel(videoTitle);
							video.setIauditorstatus(0);// 审核状态(0未审、1通过、2未通过)
							this.videoService.insertSelective(video);
						}else{
							video.setClistingid(clistingid);
							video.setCsku(csku);
							video.setClabel("comment");
							video.setIwebsiteid(websiteId);
							video.setCmemberemail(email);
							video.setIcomment(comment.getIid());
							video.setCvideourl(videoUrl);
							video.setClabel(videoTitle);
							video.setIauditorstatus(0);// 审核状态(0未审、1通过、2未通过)
							this.videoService.updateByPrimaryKeySelective(video);
						}
						
						//需要判斷哪些圖片url存在，哪些不存在，不存在表示已經刪除，需要
						if (imageUrls != null && imageUrls.length > 0) {
							List<String> urlList = this.photoService.getCommentImgageByCommentIdLimit5(comment.getIid());
							for (int i = 0; i < imageUrls.length; i++) {
								for (int j = 0; j < urlList.size(); j++) {
									if(imageUrls[i].equals(urlList.get(j))){
										urlList.remove(j);
									}
								}
							}
							if(null != urlList && urlList.size()>0){
								for (String str : urlList) {
									this.photoService.deletePhotosByListingIdAndEmail(email, websiteId, clistingid, str);
								}
							}
						}

						// 评论图片
						// 判断file数组不能为空并且长度大于0
						//if (files != null && files.length > 0) {
							// 循环获取file数组中得文件
						//	for (int i = 0; i < files.length; i++) {
						Map<String, MultipartFile> mumap = request.getFileMap();
						if (mumap != null && mumap.size() > 0) {
							for (Map.Entry<String, MultipartFile> entry : mumap.entrySet()) {
								MultipartFile file = entry.getValue();
								// 保存文件
								if (file != null && !file.isEmpty()) {

									NetHttpTransport transport = new NetHttpTransport();
									HttpRequestFactory httpRequestFactory = transport
											.createRequestFactory();

									// 保存文件
									String fileName = file.getOriginalFilename();
									Map<String, String> parameters = Maps
											.newHashMap();
									parameters.put("token", token);
									// Add parameters
									MultipartContent content = new MultipartContent()
											.setMediaType(new HttpMediaType(
													"multipart/form-data")
													.setParameter("boundary",
															"__END_OF_PART__"));
									for (String name : parameters.keySet()) {
										MultipartContent.Part part = new MultipartContent.Part(
												new ByteArrayContent(null,
														parameters.get(name)
																.getBytes()));
										part.setHeaders(new HttpHeaders().set(
												"Content-Disposition",
												String.format(
														"form-data; name=\"%s\"",
														name)));
										content.addPart(part);
									}
									// Add file
									HttpContent fileContent = new ByteArrayContent(
											"image/jpeg", file.getBytes());

									MultipartContent.Part part = new MultipartContent.Part(
											fileContent);
									part.setHeaders(new HttpHeaders().set(
											"Content-Disposition",
											String.format(
													"form-data; name=\"file\"; filename=\"%s\"",
													fileName)));
									content.addPart(part);
									try {
										GenericUrl url = new GenericUrl(cdnImageUrl);
										String response = httpRequestFactory
												.buildPostRequest(url, content)
												.execute().parseAsString();

										ObjectMapper om = new ObjectMapper();
										JsonNode n = om.readTree(response);
										Boolean r = n.get("succeed").asBoolean();
										if (r) {
											String contentType = file
													.getContentType();
											InteractionProductMemberPhotos ipmp = new InteractionProductMemberPhotos();
											ipmp.setClistingid(clistingid);
											ipmp.setCsku(csku);
											ipmp.setCmemberemail(email);
											ipmp.setIcommentid(comment.getIid());
											ipmp.setCimageurl(n.get("path")
													.asText());
											ipmp.setIauditorstatus(0);// 审核状态(0未审、1通过、2未通过)
											ipmp.setCcontenttype(contentType);
											ipmp.setClabel("comment");
											ipmp.setIwebsiteid(websiteId);
											this.photoService.insertSelective(ipmp);

											res.setRet(CommonUtils.SUCCESS_RES);
										}
									} catch (IOException e) {
										res.setRet(CommonUtils.ERROR_RES);
										res.setErrCode("-1");
										res.setErrMsg("update image error"
												+ e.getMessage());
										e.printStackTrace();
									}

								}
							}
						}

					
						
						res.setRet(1);
					} else {
						res.setRet(-1);
						res.setErrMsg("comment  modify faile");
					}
				} else {
					res.setRet(-1);
					res.setErrMsg("comment status is not modify");
				}
			} else {
				res.setRet(-1);
				res.setErrMsg("emial invalid");
			}

		} catch (Exception e) {
			res.setRet(CommonUtils.EXCEPTION);
			res.setErrCode("500");
			res.setErrMsg("comment modify faile" + e.getMessage());
			e.printStackTrace();
		}

		return res;
	}

	@ResponseBody
	@RequestMapping(value = "/v1/reviews", method = RequestMethod.POST)
	public Result addReviews(
			@RequestParam("clistingid") String clistingid,
			@RequestParam("csku") String csku,
			@RequestParam("ccomment") String ccomment,
			@RequestParam("ipriceStarWidth") Integer iprice,
			@RequestParam("iqualityStarWidth") Integer iquality,
			@RequestParam("ishippingStarWidth") Integer ishipping,
			@RequestParam("iusefulness") Integer iusefulness,
			@RequestParam(value = "commentVideoUrl", defaultValue = "", required = false) String videoUrl,
			@RequestParam(value = "videoTitle", defaultValue = "", required = false) String videoTitle,
			@RequestParam("uuid") String uuid,
			@RequestParam("client") Integer websiteId,
			@RequestParam("email") String email,
			@RequestParam(value = "ccountry") String ccountry,
			@RequestParam(value = "cplatform") String cplatform,
			MultipartHttpServletRequest request) {
		Result res = new Result();
		try {
			if(ccomment == null || ccomment.length() > 500 ){
				res.setRet(CommonUtils.COMMENT_LENT_ERR);
				res.setErrMsg("comment length over ");
				return res;
			}
			boolean flagV = memberService.validate(websiteId, email, uuid);
			if (flagV) {

				InteractionComment ic = new InteractionComment();
				ic.setCcomment(ccomment);
				ic.setClistingid(clistingid);
				ic.setCmemberemail(email);
				ic.setCsku(csku);
				ic.setIprice(iprice);
				ic.setIquality(iquality);
				ic.setIusefulness(iusefulness);
				ic.setIshipping(ishipping);
				ic.setDcreatedate(new Date());
				ic.setIstate(0);
				ic.setIwebsiteid(websiteId);
				ic.setCcountry(ccountry);
				ic.setCplatform(cplatform);
				int icomment = memberReviewsService.insertSelective(ic);

				if (icomment > 0) {
					// 评论视频
					InteractionProductMemberVideo video = new InteractionProductMemberVideo();
					video.setClistingid(clistingid);
					video.setCsku(csku);
					video.setClabel("comment");
					video.setIwebsiteid(websiteId);
					video.setCmemberemail(email);
					video.setIcomment(icomment);
					video.setCvideourl(videoUrl);
					video.setClabel(videoTitle);
					video.setIauditorstatus(0);// 审核状态(0未审、1通过、2未通过)
					this.videoService.insertSelective(video);

					// 评论图片
					// 判断file数组不能为空并且长度大于0
					//if (files != null && files.length > 0) {
						// 循环获取file数组中得文件
					//	for (int i = 0; i < files.length; i++) {
					Map<String, MultipartFile> mumap = request.getFileMap();
					if (mumap != null && mumap.size() > 0) {
						for (Map.Entry<String, MultipartFile> entry : mumap.entrySet()) {
							MultipartFile file = entry.getValue();
							// 保存文件
							if (file != null && !file.isEmpty()) {

								NetHttpTransport transport = new NetHttpTransport();
								HttpRequestFactory httpRequestFactory = transport
										.createRequestFactory();

								// 保存文件
								String fileName = file.getOriginalFilename();
								Map<String, String> parameters = Maps
										.newHashMap();
								parameters.put("token", token);
								// Add parameters
								MultipartContent content = new MultipartContent()
										.setMediaType(new HttpMediaType(
												"multipart/form-data")
												.setParameter("boundary",
														"__END_OF_PART__"));
								for (String name : parameters.keySet()) {
									MultipartContent.Part part = new MultipartContent.Part(
											new ByteArrayContent(null,
													parameters.get(name)
															.getBytes()));
									part.setHeaders(new HttpHeaders().set(
											"Content-Disposition",
											String.format(
													"form-data; name=\"%s\"",
													name)));
									content.addPart(part);
								}
								// Add file
								HttpContent fileContent = new ByteArrayContent(
										"image/jpeg", file.getBytes());

								MultipartContent.Part part = new MultipartContent.Part(
										fileContent);
								part.setHeaders(new HttpHeaders().set(
										"Content-Disposition",
										String.format(
												"form-data; name=\"file\"; filename=\"%s\"",
												fileName)));
								content.addPart(part);
								try {
									GenericUrl url = new GenericUrl(cdnImageUrl);
									String response = httpRequestFactory
											.buildPostRequest(url, content)
											.execute().parseAsString();

									ObjectMapper om = new ObjectMapper();
									JsonNode n = om.readTree(response);
									Boolean r = n.get("succeed").asBoolean();
									if (r) {
										String contentType = file
												.getContentType();
										InteractionProductMemberPhotos ipmp = new InteractionProductMemberPhotos();
										ipmp.setClistingid(clistingid);
										ipmp.setCsku(csku);
										ipmp.setCmemberemail(email);
										ipmp.setIcommentid(icomment);
										ipmp.setCimageurl(n.get("path")
												.asText());
										ipmp.setIauditorstatus(0);// 审核状态(0未审、1通过、2未通过)
										ipmp.setCcontenttype(contentType);
										ipmp.setClabel("comment");
										ipmp.setIwebsiteid(websiteId);
										this.photoService.insertSelective(ipmp);

										res.setRet(CommonUtils.SUCCESS_RES);
									}
								} catch (IOException e) {
									res.setRet(CommonUtils.ERROR_RES);
									res.setErrCode("-1");
									res.setErrMsg("update image error"
											+ e.getMessage());
									e.printStackTrace();
								}

							}
						}
					}

					res.setRet(1);
				} else {
					res.setRet(-1);
				}

			} else {
				res.setRet(-1);
				res.setErrMsg("email is not valid");
			}

		} catch (Exception e) {
			res.setRet(CommonUtils.EXCEPTION);
			res.setErrCode("500");
			res.setErrMsg("comment add faile" + e.getMessage());
			e.printStackTrace();
		}

		return res;
	}

	@ResponseBody
	@RequestMapping(value = "/v1/reviews/statistics", method = RequestMethod.GET)
	public Result reviewsStatistics(
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "uuid") String uuid,
			@RequestParam(value = "email") String email) {
		Result bb = new Result();
		try {

			boolean flagV = memberService.validate(client, email, uuid);
			if (flagV) {
				Integer totalReviewsCount = memberReviewsService
						.getTotalReviewsCountByMemberEmailAndSiteId(email, -1,
								0, client);

				Integer pendingReviewsCount = memberReviewsService
						.getPendingReviewsCountByMemberEmailAndSiteId(email,
								client);
				Integer approvedReviewsCount = memberReviewsService
						.getApprovedReviewsCountByMemberEmailAndSiteId(email,
								client);
				Integer failedReviewsCount = memberReviewsService
						.getFailedReviewsCountByMemberEmailAndSiteId(email,
								client);
				Integer pointsTotal = this.pointsService.getUsefulPoints(email,
						client);

				JSONObject json = new JSONObject();
				json.put("totalReviewsCount", totalReviewsCount);
				json.put("pendingReviewsCount", pendingReviewsCount);
				json.put("approvedReviewsCount", approvedReviewsCount);
				json.put("failedReviewsCount", failedReviewsCount);
				json.put("pointsTotal", pointsTotal);
				bb.setData(json.toJSONString());
				bb.setRet(1);
			} else {
				bb.setRet(-1);
				bb.setErrMsg("email is not valid");
			}

		} catch (Exception e) {
			bb.setRet(-1);
			bb.setErrMsg("reviews statistics faile" + e.getMessage());
			e.printStackTrace();
		}

		return bb;
	}

	/**
	 * 用户email站点信息获取会员信息
	 * 
	 * @param uuid
	 */
	@ResponseBody
	@RequestMapping(value = "/v1/reviews/photos", method = RequestMethod.GET)
	public Result getMemberReviewsPhotosByEmailAndWebsiteId(
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "uuid") String uuid,
			@RequestParam(value = "email") String email,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
			@RequestParam(value = "status", required = false, defaultValue = "-1") Integer status,
			@RequestParam(value = "dateType", required = false, defaultValue = "0") Integer dateType) {
		Result res = new Result();
		try {
			boolean flagV = memberService.validate(client, email, uuid);
			if (flagV) {
				Integer totalReviewsCount = photoService
						.getTotalPhotoCountByMemberEmailAndSiteId(email, client);

				List<InteractionPhotoBo> base = this.photoService
						.getMyReviewsPhotos(email, totalReviewsCount, page,
								limit, status, dateType, client);

				Page pageObj = Page.getPage(page, totalReviewsCount, limit);

				res.setPage(pageObj);
				res.setData(base);
				res.setRet(CommonUtils.SUCCESS_RES);
			} else {
				res.setRet(CommonUtils.ERROR_RES);
				res.setErrCode("500");
				res.setErrMsg("email is not valid");

			}

		} catch (Exception e) {
			e.printStackTrace();
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode("500");
			res.setErrMsg("getMemberReviews error" + e.getMessage());
		}
		return res;
	}

	@ResponseBody
	@RequestMapping(value = "/v1/reviews/video", method = RequestMethod.POST)
	public Result addReviewsVideo(@RequestBody String jsonParam) {
		Result res = new Result();
		try {

			ObjectMapper om = new ObjectMapper();
			JsonNode jsonNode = om.readTree(jsonParam);

			String uuid = jsonNode.get("uuid").asText();
			Integer websiteId = jsonNode.get("client").asInt();
			String email = jsonNode.get("email").asText();

			String listingId = jsonNode.get("listingId").asText();
			String csku = jsonNode.get("csku").asText();
			String videoUrl = jsonNode.get("videoUrl").asText();
			String videoTitle = jsonNode.get("videoTitle").asText();

			boolean flagV = memberService.validate(websiteId, email, uuid);
			if (flagV) {

				// 评论视频
				InteractionProductMemberVideo video = new InteractionProductMemberVideo();
				video.setClistingid(listingId);
				video.setCsku(csku);
				video.setClabel("video");
				video.setIwebsiteid(websiteId);
				video.setCmemberemail(email);
				video.setIcomment(null);
				video.setCvideourl(videoUrl);
				video.setClabel(videoTitle);
				video.setIauditorstatus(0); // 审核状态(0未审、1通过、2未通过)
				this.videoService.insertSelective(video);
				res.setRet(1);

			} else {
				res.setRet(-1);
				res.setErrMsg("email is not valid");
			}

		} catch (Exception e) {
			res.setRet(-1);
			res.setErrCode("500");
			res.setErrMsg("addReviewsVideo faile" + e.getMessage());
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 用户email站点信息获取会员信息
	 * 
	 * @param uuid
	 */
	@ResponseBody
	@RequestMapping(value = "/v1/reviews/videos", method = RequestMethod.GET)
	public Result getMemberReviewsVideosByEmailAndWebsiteId(
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "uuid") String uuid,
			@RequestParam(value = "email") String email,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
			@RequestParam(value = "status", required = false, defaultValue = "-1") Integer status,
			@RequestParam(value = "dateType", required = false, defaultValue = "0") Integer dateType) {
		Result res = new Result();
		try {
			boolean flagV = memberService.validate(client, email, uuid);
			if (flagV) {
				Integer totalReviewsCount = this.videoService
						.getTotalVideoCountByMemberEmailAndSiteId(email, client);

				List<InteractionVideoBo> base = this.videoService
						.getMyReviewsVideos(email, totalReviewsCount, page,
								limit, status, dateType, client);

				Page pageObj = Page.getPage(page, totalReviewsCount, limit);

				res.setPage(pageObj);
				res.setData(base);
				res.setRet(CommonUtils.SUCCESS_RES);
			} else {
				res.setRet(CommonUtils.ERROR_RES);
				res.setErrCode("500");
				res.setErrMsg("email is not valid");

			}

		} catch (Exception e) {
			e.printStackTrace();
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode("500");
			res.setErrMsg("getMemberReviews error" + e.getMessage());
		}
		return res;
	}

	@ResponseBody
	@RequestMapping(value = "/v1/reviews/videos", method = RequestMethod.POST)
	public Result deleteVideoByListingIds(@RequestBody String jsonParam) {

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

						// 删除之前需要确认此收藏是否属于此会员的
						boolean flag = this.videoService
								.deleteVideosByListingIdAndEmail(email,
										websiteId, listingId);
						if (flag) {
							bb.setRet(1);
						} else {
							bb.setRet(-1);
						}
					}
				}

			} else {
				bb.setRet(-1);
				bb.setErrMsg("uuid is not valid");
			}

		} catch (Exception e) {
			bb.setRet(-1);
			bb.setErrMsg("Video delete faile" + e.getMessage());
			e.printStackTrace();
		}
		return bb;
	}

	@ResponseBody
	@RequestMapping(value = "/v1/reviews/photos", method = RequestMethod.POST)
	public Result deletePhotoByListingIds(@RequestBody String jsonParam) {

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

						// 删除之前需要确认此评论图片是否属于此会员的
						boolean flag = this.photoService
								.deletePhotosByListingIdAndEmail(email,
										websiteId, listingId);
						if (flag) {
							bb.setRet(1);
						} else {
							bb.setRet(-1);
						}
					}
				}

			} else {
				bb.setRet(-1);
				bb.setErrMsg("uuid is not valid");
			}

		} catch (Exception e) {
			bb.setRet(-1);
			bb.setErrMsg("photos delete faile" + e.getMessage());
			e.printStackTrace();
		}
		return bb;
	}

	/**
	 * 用户email站点信息获取会员评论信息
	 * 
	 * @param uuid
	 */
	@ResponseBody
	@RequestMapping(value = "/v1/review", method = RequestMethod.GET)
	public Result getMemberReviewByIid(
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "uuid") String uuid,
			@RequestParam(value = "email") String email,
			@RequestParam(value = "iid") Integer iid) {
		Result res = new Result();
		try {
			boolean flagV = memberService.validate(client, email, uuid);
			if (flagV) {

				ReviewsInMemberCenter base = this.memberReviewsService.getReviewsInMemberCenterById(iid,email);
				if(null == base){
					res.setRet(CommonUtils.ERROR_RES);
					res.setErrCode("-1");
					res.setErrMsg("data is null");
					return res;
				}
				res.setData(base);
				res.setRet(CommonUtils.SUCCESS_RES);
			} else {
				res.setRet(CommonUtils.ERROR_RES);
				res.setErrCode("500");
				res.setErrMsg("email is not valid");

			}

		} catch (Exception e) {
			e.printStackTrace();
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode("500");
			res.setErrMsg("getMemberReviewByIid error" + e.getMessage());
		}
		return res;
	}

	/**
	 * 用户email站点信息获取会员评论信息 .v2 支持website属性
	 * 
	 * @param uuid
	 */
	@ResponseBody
	@RequestMapping(value = "/v2/review", method = RequestMethod.GET)
	public Result getMemberReviewByIidV2(
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "uuid") String uuid,
			@RequestParam(value = "email") String email,
			@RequestParam(value = "iid") Integer iid) {
		Result res = new Result();
		try {
			boolean flagV = memberService.validate(website, email, uuid);
			if (flagV) {

				ReviewsInMemberCenter base = this.memberReviewsService.getReviewsInMemberCenterById(iid,email);
				if(null == base){
					res.setRet(CommonUtils.ERROR_RES);
					res.setErrCode("-1");
					res.setErrMsg("data is null");
					return res;
				}
				res.setData(base);
				res.setRet(CommonUtils.SUCCESS_RES);
			} else {
				res.setRet(CommonUtils.ERROR_RES);
				res.setErrCode("500");
				res.setErrMsg("email is not valid");

			}

		} catch (Exception e) {
			e.printStackTrace();
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode("500");
			res.setErrMsg("getMemberReviewByIid error" + e.getMessage());
		}
		return res;
	}
}
