package com.tomtop.member.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.framework.core.utils.Result;
import com.tomtop.member.models.bo.Page;
import com.tomtop.member.models.bo.UpdateBo;
import com.tomtop.member.models.dto.Broadcast;
import com.tomtop.member.models.dto.Message;
import com.tomtop.member.models.dto.MessageInfo;
import com.tomtop.member.models.dto.MessageStatus;
import com.tomtop.member.service.IBroadcastService;
import com.tomtop.member.service.IMemberService;
import com.tomtop.member.service.IMessageService;
import com.tomtop.member.utils.CommonUtils;

/**
 * 站内信
 * 
 * @author renyy
 *
 */
@RestController
@RequestMapping(value = "/message")
public class MessageController {

	@Autowired
	IMessageService messageService;

	@Autowired
	IBroadcastService broadcastService;

	@Autowired
	IMemberService memberService;

	/**
	 * 获取站内信列表 (update by tony 20160414)
	 * @param website
	 * @param email
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping(value = "/v1/list", method = RequestMethod.GET)
	public Result getMessageList(
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "email") String email,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
		Result bb = new Result();
		try {
				Page<MessageInfo> list = this.messageService.getMyMessageForPage(
						email,website, page, size);
				if(list == null){
					bb.setRet(CommonUtils.ERROR_RES);
					bb.setErrMsg("not find message");
				}else{
					bb.setRet(CommonUtils.SUCCESS_RES);
					bb.setData(list);
				}
		} catch (Exception e) {
			bb.setRet(CommonUtils.EXCEPTION);
			bb.setErrMsg("getMessageList add faile" + e.getMessage());
			e.printStackTrace();
		}

		return bb;
	}

	/**
	 * 把消息设置为已读(update by tony 20160414)
	 * @param website
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "/v1/batch/read", method = RequestMethod.POST)
	public Result markedAsRead(@RequestBody UpdateBo ub) {
		Result bb = new Result();
		try {
			if(!"".equals(ub.getIds())){
				String[] list = ub.getIds().split(",");
				for (String string :list) {
					String[] strs = string.split(":");
					if(strs == null || strs.length > 2){
						bb.setRet(CommonUtils.MESSAGE_STR_ERR);
						bb.setErrMsg("ids format error");
						return bb;
					}
					if ("b".equals(strs[1])) {
						// 把消息设置为删除
						broadcastService.readMyBroadcastMessage(ub.getEmail(),strs[0]);
					}else if("i".equals(strs[1])){
						messageService.readMessage(strs[0]);
					}
				}
			}else{
				bb.setRet(CommonUtils.MESSAGE_IDS_NOT_FIND);
				bb.setErrMsg("ids is null");
			}
			
			bb.setRet(CommonUtils.SUCCESS_RES);
		} catch (Exception e) {
			bb.setRet(CommonUtils.EXCEPTION);
			bb.setErrMsg("message batch read faile" + e.getMessage());
			e.printStackTrace();
		}

		return bb;
	}

	/**
	 * 获取消息详情(update by tony 20160414)
	 * @param id
	 * @param website
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "/v1/dtl/{id}", method = RequestMethod.GET)
	public Result detail(
			@PathVariable("id") Integer id,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "email") String email,
			@RequestParam(value = "tab") String table,
			@RequestParam(value = "status") Integer status) {
		Result bb = new Result();
		try {
				if ("b".equals(table)) {
					Broadcast b = this.broadcastService.getDetail(id.toString());
					if (b == null) {
						bb.setRet(CommonUtils.MESSAGE_BRO_NOT_FIND);
						bb.setErrMsg("message id is not valid");
						return bb;
					}
					if(status == MessageStatus.PUBLISH.getCode()){
						// 把消息设置为已阅
						this.broadcastService.readMyBroadcastMessage(email,id.toString());
					}
					bb.setData(b);

				} else if ("i".equals(table)) {
					//获取消息详细
					Message m = this.messageService.getDetail(id);
					if (m == null) {
						bb.setRet(CommonUtils.ERROR_RES);
						bb.setErrMsg("message id is not valid");
						return bb;
					}
					if(status == MessageStatus.unread.getCode()){
						//设置为已读
						messageService.readMessage(id.toString());
					}
						bb.setRet(CommonUtils.SUCCESS_RES);
						bb.setData(m);
				}
		} catch (Exception e) {
			bb.setRet(CommonUtils.EXCEPTION);
			bb.setErrMsg("comment detail " + e.getMessage());
			e.printStackTrace();
		}
		return bb;
	}

	/**
	 * 消息删除(update by tony 20160414)
	 * @param UpdateBo
	 * @return
	 */
	@RequestMapping(value = "/v1/batch/delete", method = RequestMethod.POST)
	public Result deleteMessageByIds(@RequestBody UpdateBo ub) {
		Result bb = new Result();
		try {
			if(!"".equals(ub.getIds())){
				String[] list = ub.getIds().split(",");
				for (String string :list) {
					String[] strs = string.split(":");
					if(strs == null || strs.length > 2){
						bb.setRet(CommonUtils.MESSAGE_STR_ERR);
						bb.setErrMsg("ids format error");
						return bb;
					}
					if ("b".equals(strs[1])) {
						// 把消息设置为删除
						broadcastService.deleteMyBroadcastMessage(ub.getEmail(),Integer.parseInt(strs[0]));
					}else if("i".equals(strs[1])){
						messageService.deleteMessage(strs[0]);
					}
				}
			}else{
				bb.setRet(CommonUtils.MESSAGE_IDS_NOT_FIND);
				bb.setErrMsg("ids is null");
			}
			
			bb.setRet(CommonUtils.SUCCESS_RES);

		} catch (Exception e) {
			bb.setRet(CommonUtils.EXCEPTION);
			bb.setErrMsg("message batch  delete faile" + e.getMessage());
			e.printStackTrace();
		}
		return bb;
	}
	
	/**
	 * 获取未读取的消息数 (update by tony 20160513)
	 * @param website
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "/v1/unMsg/count", method = RequestMethod.GET)
	public Result getUnMessage(
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "email") String email) {
		Result bb = new Result();
		try {
			int num = this.messageService.getMyUnMessageTotal(email, website);
			bb.setRet(CommonUtils.SUCCESS_RES);
			bb.setData(num);
		} catch (Exception e) {
			bb.setRet(CommonUtils.EXCEPTION);
			bb.setErrMsg("getUnMessage faile" + e.getMessage());
			e.printStackTrace();
		}

		return bb;
	}

}
