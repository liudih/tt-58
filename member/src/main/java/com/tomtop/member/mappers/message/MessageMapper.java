package com.tomtop.member.mappers.message;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tomtop.member.models.dto.Message;
import com.tomtop.member.models.dto.MessageInfo;


/**
 * 我的消息映射类
 * 
 * @author lijun
 *
 */
public interface MessageMapper {

	/**
	 * 获取我的消息总条数,分页的时候会要用到
	 * 
	 * @return
	 */
	public int getMyMessageTotal(Map<String, Object> paras);

	/**
	 * 获取我的未读取的消息总条数
	 * 
	 * @return
	 */
	public int getMyUnMessageTotal(Map<String, Object> paras);
	
	/**
	 * 分页获取我的消息
	 * 
	 * @param paras
	 * @return
	 */
	public List<MessageInfo> getMyMessageForPage(Map<String, Object> paras);

	/**
	 * 更新我的消息状态(已阅 or 删除)
	 * 
	 * @param paras
	 * @return
	 */
	public int updateMessageStatus(Map<String, Object> paras);

	/**
	 * 获取详情
	 * 
	 * @param paras
	 * @return
	 */
	@Select({"select cfrom as from , csubject as subject, ccontent as content, dcreatedate createDate ",
			"from t_message_info where iid = #{0}"})
	public Message getDetail(Integer id);

	/**
	 * 记录是否已经存在
	 * 
	 * @param paras
	 * @return
	 */
	public int isExisted(Map<String, Object> paras);

	/**
	 * 插入操作
	 * @param paras
	 * @return
	 */
	public int insert(Message paras);

	
	/**
	 * 统计记录数
	 * @auther CJ
	 * @return
	 */
	@Select({
		"<script>",
			"select count(*) from t_message_info where isendid = #{adminId}",		
		"</script>" })
	int getCountPersonalMessages(@Param("adminId") int adminUserId);	
}
