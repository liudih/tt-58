package com.tomtop.member.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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
import com.tomtop.member.mappers.interaction.InteractionCommentMapper;
import com.tomtop.member.mappers.interaction.InteractionProductMemberPhotosMapper;
import com.tomtop.member.mappers.order.OrderDetailMapper;
import com.tomtop.member.mappers.product.ProductUrlMapper;
import com.tomtop.member.models.bo.BaseBo;
import com.tomtop.member.models.bo.MemberReviewsBo;
import com.tomtop.member.models.bo.ReviewsBo;
import com.tomtop.member.models.dto.InteractionComment;
import com.tomtop.member.models.dto.InteractionProductMemberPhotos;
import com.tomtop.member.models.dto.InteractionProductMemberVideo;
import com.tomtop.member.models.dto.OrderDetailComment;
import com.tomtop.member.models.dto.Purl;
import com.tomtop.member.service.IInteractionProductPhotosService;
import com.tomtop.member.service.IInteractionProductVideoService;
import com.tomtop.member.service.IMemberReviewsService;
import com.tomtop.member.service.IReviewService;
import com.tomtop.member.utils.CommonUtils;

/**
 * 用户评论业务类
 * Add by 20160413
 * @author renyy
 *
 */
@Service
public class ReviewServiceImpl implements IReviewService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	IMemberReviewsService memberReviewService;
	@Autowired
	IInteractionProductVideoService videoService;
	@Autowired
	IInteractionProductPhotosService photoService;
	@Autowired
	InteractionCommentMapper commentMapper;
	@Autowired
	ProductUrlMapper productUrlMapper;
	@Autowired
	InteractionProductMemberPhotosMapper commentPhotosMapper;
	@Autowired
	OrderDetailMapper orderDetailMapper;
	
	@Value("${secure.token}")
	private String token;

	@Value("${cdnImageUrl}")
	private String cdnImageUrl;
	
	/**
	 * 获取评论列表
	 * Add by 20160414
	 * @author renyy
	 *
	 */
	public List<MemberReviewsBo> getMemberReviewsBo(String email,
			Integer page, Integer size, Integer status,
			Integer dateType, Integer website) {
		Date start = null;
		Date end = null;
		if (dateType != 0) {
			end = new Date();
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.add(Calendar.MONTH, (dateType * (-1)));
			start = calendar.getTime();
		}
		List<InteractionComment> reviews = commentMapper.getMyReviewsPageByEmail(email, page,
				size, status, start, end, website);

		List<MemberReviewsBo> reviewsInMemberCenters = getMemberReviewsBoList(reviews,website);
		return reviewsInMemberCenters;
	}
	
	/**
	 * 获取评论列表信息数据转化
	 * Add by 20160414
	 * @author renyy
	 *
	 */
	private List<MemberReviewsBo> getMemberReviewsBoList(List<InteractionComment> reviews,Integer website){
		List<MemberReviewsBo> mrboList = new ArrayList<MemberReviewsBo>();
		for (InteractionComment cs : reviews) {
			String videoUrl = "";
			String videoTitle = "";
			String url = "";
			String imgUrl = "";
			List<String> photosUrl = new ArrayList<String>();
			List<Purl> plist = productUrlMapper.getUrlByListing(cs.getClistingid(), website);
			if(plist != null){
				for (Purl pl : plist) {
					if("url".equals(pl.getType())){
						if("".equals(url)){
							url = pl.getUrl();
						}
					}else if("img".equals(pl.getType())){
						if("".equals(imgUrl)){
							imgUrl=pl.getUrl();
						}
					}
				}
			}
			List<Purl> cvlist = commentPhotosMapper.getUrlByListing(cs.getIid());
			if(cvlist != null){
				for (Purl cl : cvlist) {
					if("photo".equals(cl.getType())){
						photosUrl.add(cl.getUrl());
					}else if("video".equals(cl.getType())){
						videoUrl = cl.getUrl();
						videoTitle = cl.getTitle();
					}
				}
			}
			
			mrboList.add(new MemberReviewsBo(cs.getIid(), cs.getCcomment(), cs.getIprice(),
					cs.getIquality(), cs.getIshipping(), cs.getIusefulness(), cs.getFoverallrating(),
					videoUrl, videoTitle, cs.getIstate(), cs.getDcreatedate(), cs.getClistingid(),
					cs.getCsku(), url,imgUrl, photosUrl));
		}

		return mrboList;
	}

	/**
	 * 添加评论
	 * Add by 20160413
	 * @author renyy
	 *
	 */
	@Override
	public BaseBo addReview(ReviewsBo rb,MultipartHttpServletRequest request) throws Exception {
		BaseBo bb = new BaseBo();
		if(rb.getOid() == null || rb.getListingId() == null){
			bb.setRes(CommonUtils.COMMENT_ADD_NULL_ERR);
			bb.setMsg("(order id or list id) is null");
			return bb;
		}
		Integer status = orderDetailMapper.getOrderIdStatus(rb.getOid());
		if(status == null || status != CommonUtils.SEVEN){
			bb.setRes(CommonUtils.COMMENT_ORDER_NOT_COMPLETED_ERR);
			bb.setMsg("Order not completed");
			return bb;
		}
		OrderDetailComment odc = orderDetailMapper.getOrderDetailCommentId(rb.getOid(), rb.getListingId());
		if(odc == null){
			bb.setRes(CommonUtils.COMMENT_ORDER_DETAIL_ERR);
			bb.setMsg("detail not find error");
			return bb;
		}
		//判断是否已经评论过
		if(odc.getCid() != null){
			bb.setRes(CommonUtils.COMMENT_HAVE_BEEN_ERR);
			bb.setMsg("Product Reviews have been");
			return bb;
		}
		//listingId必须相同
		if(!odc.getListingId().equals(rb.getListingId())){
			bb.setRes(CommonUtils.COMMENT_ORDER_DETAIL_LISTINGID_ERR);
			bb.setMsg("detail review listingId not equals");
			return bb;
		}
		if(!odc.getSku().equals(rb.getSku())){
			bb.setRes(CommonUtils.COMMENT_ORDER_DETAIL_SKU_ERR);
			bb.setMsg("detail review sku not equals");
			return bb;
		}
		//添加评论
		InteractionComment ic = new InteractionComment();
		ic.setCcomment(rb.getComment());
		ic.setClistingid(rb.getListingId());
		ic.setCmemberemail(rb.getEmail());
		ic.setCsku(rb.getSku());
		ic.setIprice(rb.getPs());
		ic.setIquality(rb.getQs());
		ic.setIusefulness(rb.getUs());
		ic.setIshipping(rb.getSs());
		ic.setDcreatedate(new Date());
		ic.setIstate(0);
		ic.setIwebsiteid(rb.getWebsite());
		ic.setCcountry(rb.getCountryName());
		ic.setCplatform(rb.getPform());
		ic.setIorderid(rb.getOid());
		int icomment = memberReviewService.insertSelective(ic);

		if (icomment > 0) {
			//更新评论ID到订单详情对应的商品
			Map<String,Object> umap = new HashMap<String, Object>();
			umap.put("commentid", ic.getIid());
			umap.put("oid", rb.getOid());
			umap.put("listingId", rb.getListingId());
			int re = orderDetailMapper.updateOrderDetailCommentId(umap);
			if(re <= 0){
				logger.error("update order detail comment id error id= "+ 
							ic.getIid() + " oid=" + rb.getOid()  + " listingid=" + rb.getListingId());
			}
			if(rb.getVideoUrl() != null && !"".equals(rb.getVideoUrl())){
				// 评论视频
				InteractionProductMemberVideo video = new InteractionProductMemberVideo();
				video.setClistingid(rb.getListingId());
				video.setCsku(rb.getSku());
				video.setIwebsiteid(rb.getWebsite());
				video.setCmemberemail(rb.getEmail());
				video.setIcomment(icomment);
				video.setCvideourl(rb.getVideoUrl());
				if(rb.getVideoTitle() == null){
					video.setClabel("commentVideo");
				}else{
					video.setClabel(rb.getVideoTitle());
				}
				video.setIauditorstatus(0);// 审核状态(0未审、1通过、2未通过)
				this.videoService.insertSelective(video);
			}
			if(request != null){
				// 评论图片
				// 判断file数组不能为空并且长度大于0
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
									ipmp.setClistingid(rb.getListingId());
									ipmp.setCsku(rb.getSku());
									ipmp.setCmemberemail(rb.getEmail());
									ipmp.setIcommentid(icomment);
									ipmp.setCimageurl(n.get("path")
											.asText());
									ipmp.setIauditorstatus(0);// 审核状态(0未审、1通过、2未通过)
									ipmp.setCcontenttype(contentType);
									ipmp.setClabel("comment");
									ipmp.setIwebsiteid(rb.getWebsite());
									this.photoService.insertSelective(ipmp);
	
									bb.setRes(CommonUtils.SUCCESS_RES);
								}
							} catch (IOException e) {
								bb.setRes(CommonUtils.ERROR_RES);
								bb.setMsg("update image error "+ e.getMessage());
								e.printStackTrace();
							}
	
						}
					}
				}
			}
		} else {
			logger.error("insert comment  error id= "+ rb.getOid()  + " listingid=" + rb.getListingId());
			bb.setRes(CommonUtils.COMMENT_INSERT_ERR);
			bb.setMsg("insert comment error id= "+ rb.getOid()  + " sku=" + rb.getSku());
			return bb;
		}

		bb.setRes(CommonUtils.SUCCESS_RES);

		return bb;
	}

	/**
	 * 更新评论
	 * Add by 20160413
	 * @author renyy
	 *
	 */
	@Override
	public BaseBo updateReview(ReviewsBo rb,String[] imageUrls, MultipartHttpServletRequest request)
			throws Exception {
		BaseBo bb = new BaseBo();
		InteractionComment comment = memberReviewService.getReviewById(rb.getCommentId(),rb.getEmail());
			if (comment.getIstate() == 0) { // 状态为未审核时才能修改
				comment.setIid(rb.getCommentId());
				comment.setCcomment(rb.getComment());
				comment.setIprice(rb.getPs());
				comment.setIquality(rb.getQs());
				comment.setIshipping(rb.getSs());
				comment.setIusefulness(rb.getUs());
				boolean flag = memberReviewService.updateInteractionComment(comment);
				if (flag) {
					// 评论视频
					InteractionProductMemberVideo video = this.videoService.
							getVideoByCommentIdAndEmailAndSiteId(comment.getIid(), rb.getEmail(), rb.getWebsite());
					
					if(null == video){
						video= new InteractionProductMemberVideo();
						video.setClistingid(rb.getListingId());
						video.setCsku(rb.getSku());
						video.setIwebsiteid(rb.getWebsite());
						video.setCmemberemail(rb.getEmail());
						video.setIcomment(comment.getIid());
						video.setCvideourl(rb.getVideoUrl());
						if(rb.getVideoTitle() == null){
							video.setClabel("commentVideo");
						}else{
							video.setClabel(rb.getVideoTitle());
						}
						video.setIauditorstatus(0);// 审核状态(0未审、1通过、2未通过)
						this.videoService.insertSelective(video);
					}else{
						video.setClistingid(rb.getListingId());
						video.setCsku(rb.getSku());
						video.setIwebsiteid(rb.getWebsite());
						video.setCmemberemail(rb.getEmail());
						video.setIcomment(comment.getIid());
						video.setCvideourl(rb.getVideoUrl());
						if(rb.getVideoTitle() == null){
							video.setClabel("commentVideo");
						}else{
							video.setClabel(rb.getVideoTitle());
						}
						video.setIauditorstatus(0);// 审核状态(0未审、1通过、2未通过)
						this.videoService.updateByPrimaryKeySelective(video);
					}
					//需要判斷哪些圖片url存在，哪些不存在，不存在表示已經刪除，需要
						List<String> urlList = commentPhotosMapper.getCommentImgageByCommentId(comment.getIid());
						if (imageUrls != null && imageUrls.length > 0) {
							for (int i = 0; i < imageUrls.length; i++) {
								for (int j = 0; j < urlList.size(); j++) {
									if(imageUrls[i].equals(urlList.get(j))){
										urlList.remove(j);
									}
								}
							}
						}
						if(null != urlList && urlList.size()>0){
							for (String str : urlList) {
								this.photoService.deletePhotosByListingIdAndEmail(rb.getEmail(), rb.getWebsite(), rb.getListingId(), str);
							}
						}
					
					// 评论图片
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
										ipmp.setClistingid(rb.getListingId());
										ipmp.setCsku(rb.getSku());
										ipmp.setCmemberemail(rb.getEmail());
										ipmp.setIcommentid(comment.getIid());
										ipmp.setCimageurl(n.get("path")
												.asText());
										ipmp.setIauditorstatus(0);// 审核状态(0未审、1通过、2未通过)
										ipmp.setCcontenttype(contentType);
										ipmp.setClabel("comment");
										ipmp.setIwebsiteid(rb.getWebsite());
										this.photoService.insertSelective(ipmp);

										bb.setRes(CommonUtils.SUCCESS_RES);
									}
								} catch (IOException e) {
									bb.setRes(CommonUtils.ERROR_RES);
									bb.setMsg("update image error" + e.getMessage());
									e.printStackTrace();
								}

							}
						}
					}
					
					bb.setRes(CommonUtils.SUCCESS_RES);
				} else {
					bb.setRes(CommonUtils.ERROR_RES);
					bb.setMsg("comment  modify faile");
				}
			} else {
				bb.setRes(-1);
				bb.setMsg("comment status is not modify");
			}
			return bb;
	}

}
