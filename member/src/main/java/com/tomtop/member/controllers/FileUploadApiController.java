package com.tomtop.member.controllers;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
import com.tomtop.framework.core.utils.Result;
import com.tomtop.member.models.bo.MemberUidBo;
import com.tomtop.member.models.dto.InteractionProductMemberPhotos;
import com.tomtop.member.service.IInteractionProductPhotosService;
import com.tomtop.member.service.IMemberPhotoService;
import com.tomtop.member.service.IMemberService;
import com.tomtop.member.utils.CommonUtils;

/**
 * 图片上传接口
 *
 */
@RestController
@RequestMapping(value = "/image")
public class FileUploadApiController {

	@Autowired
	IMemberService memberService;

	@Autowired
	IMemberPhotoService memberPhotoService;
	
	@Autowired
	IInteractionProductPhotosService photoService;

	@Value("${cdnImageUrl}")
	private String cdnImageUrl;
 
	@Value("${secure.token}")
	private String token;

	@RequestMapping(value = "/v1/upload_cdn", method = RequestMethod.POST)
	@ResponseBody
	public Result uploadImage(@RequestParam("uuid")String uuid, @RequestParam("client")Integer websiteId, 
			@RequestParam("email")String email,
			@RequestParam("type")String uploadType,@RequestParam("file") MultipartFile file) throws IOException {
		
		Result res = new Result();
		try {
			ObjectMapper om = new ObjectMapper();
			MemberUidBo member = memberService.getMemberEmailByUUid(uuid,
					websiteId);

			if(null == member){
				res.setRet(CommonUtils.ERROR_RES);
				res.setErrCode("-1");
				res.setErrMsg("email is not valid");
				return res;
			}
			if (email.equals(member.getEmail())) {
				if (file != null && !file.isEmpty()) {
					
					NetHttpTransport transport = new NetHttpTransport();
					HttpRequestFactory httpRequestFactory = transport
							.createRequestFactory();
					
					// 保存文件
					String fileName = file.getOriginalFilename();
					Map<String, String> parameters = Maps.newHashMap();
					parameters.put("token", token);
					// Add parameters
					MultipartContent content = new MultipartContent()
							.setMediaType(new HttpMediaType("multipart/form-data")
									.setParameter("boundary", "__END_OF_PART__"));
					for (String name : parameters.keySet()) {
						MultipartContent.Part part = new MultipartContent.Part(
								new ByteArrayContent(null, parameters.get(name)
										.getBytes()));
						part.setHeaders(new HttpHeaders().set(
								"Content-Disposition",
								String.format("form-data; name=\"%s\"", name)));
						content.addPart(part);
					}
					// Add file
					HttpContent fileContent = new ByteArrayContent("image/jpeg",
							file.getBytes());

					MultipartContent.Part part = new MultipartContent.Part(
							fileContent);
					part.setHeaders(new HttpHeaders().set("Content-Disposition",
							String.format(
									"form-data; name=\"file\"; filename=\"%s\"",
									fileName)));
					content.addPart(part);
					try {
						GenericUrl url = new GenericUrl(cdnImageUrl);
						String response = httpRequestFactory
								.buildPostRequest(url, content).execute()
								.parseAsString();
						System.out.println(response);
						JsonNode n = om.readTree(response);
						Boolean r = n.get("succeed").asBoolean();
						if(r){
							if("memberHead".equalsIgnoreCase(uploadType)){
								String contentType = file.getContentType();
								memberPhotoService.updatePhoto(null, contentType, email,
										websiteId, n.get("path").asText());
								res.setRet(CommonUtils.SUCCESS_RES);
								res.setData(n);
							}else if("reviewPhoto".equalsIgnoreCase(uploadType)){
								String contentType = file
										.getContentType();
								InteractionProductMemberPhotos ipmp = new InteractionProductMemberPhotos();
								ipmp.setClistingid(n.get("clistingId").asText());
								ipmp.setCsku(n.get("csku").asText());
								ipmp.setCmemberemail(email);
								ipmp.setIcommentid(null);
								ipmp.setCimageurl(n.get("path")
										.asText());
								ipmp.setIauditorstatus(0);// 审核状态(0未审、1通过、2未通过)
								ipmp.setCcontenttype(contentType);
								ipmp.setClabel("comment");
								ipmp.setIwebsiteid(websiteId);
								this.photoService.insertSelective(ipmp);

								res.setRet(CommonUtils.SUCCESS_RES);
							}
						}
					} catch (IOException e) {
						res.setRet(CommonUtils.ERROR_RES);
						res.setErrCode("-1");
						res.setErrMsg("update image error"+e.getMessage());
						e.printStackTrace();
					}

				} else {
					res.setRet(CommonUtils.ERROR_RES);
					res.setErrCode("-1");
					res.setErrMsg("update image file is null");
				}
			} else {
				res.setRet(CommonUtils.ERROR_RES);
				res.setErrCode("-1");
				res.setErrMsg("email is not valid");
			}

		} catch (Exception e) {
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode("500");
			res.setErrMsg("upload file error" + e.getMessage());
		}
		return res;
	}

}
