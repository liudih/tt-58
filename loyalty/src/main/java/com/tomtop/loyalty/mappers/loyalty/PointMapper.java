package com.tomtop.loyalty.mappers.loyalty;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.tomtop.loyalty.models.MemberIntegralHistory;
import com.tomtop.loyalty.models.OrderPoints;
import com.tomtop.loyalty.models.Point;

public interface PointMapper {
	/**
	 * 获取用户全部积分
	 * 
	 * @param email
	 * @param website
	 * @return
	 */
	@Select("select sum(iintegral) from t_member_integral_history where cemail = #{0} and iwebsiteid = #{1} and istatus = 1")
	public Integer getUserTotalPointByEmail(String email, Integer website);

	/**
	 * 已用积分历史
	 * 
	 * @param email
	 * @param website
	 * @param page
	 * @param size
	 * @return
	 */
	public List<Point> getUsedPointHistory(@Param("email") String email,
			@Param("website") Integer website, @Param("page") int page,
			@Param("size") int size);

	/**
	 * 未用积分历史
	 * 
	 * @param email
	 * @param website
	 * @param page
	 * @param size
	 * @return
	 */
	public List<Point> getUnusedPointHistory(@Param("email") String email,
			@Param("website") Integer website, @Param("page") int page,
			@Param("size") int size);

	/**
	 * 未使用总数
	 * 
	 * @param email
	 * @param website
	 * @return
	 */
	public Integer getUnusedTotalCountByEmail(@Param("email") String email,
			@Param("website") Integer website);
	
	/**
	 * 
	  * @Description: 锁定的总积分 返回值为负数
	  * @param @param email
	  * @param @param website
	  * @param @return  
	  * @return Integer  
	  * @author liuyufeng 
	  * @date 2016年5月17日 下午2:55:53
	 */
	public Integer getLockPointSum(@Param("email") String email,
			@Param("website") Integer website);
	
	/**
	 * 
	  * @Description: 获取所有获得的积分
	  * @param @param email
	  * @param @param website
	  * @param @return  
	  * @return Integer  
	  * @author liuyufeng 
	  * @date 2016年5月17日 下午2:55:58
	 */
	public Integer getPointAllSum(@Param("email") String email,
			@Param("website") Integer website);
	
	/**
	 * 
	  * @Description: 剩余未使用积分，包括锁定积分在内
	  * @param @param email
	  * @param @param website
	  * @param @return  
	  * @return Integer  
	  * @author liuyufeng 
	  * @date 2016年5月17日 下午4:17:02
	 */
	public Integer getPointOnLock(@Param("email") String email,
			@Param("website") Integer website);

	/**
	 * 已使用总数
	 * 
	 * @param email
	 * @param website
	 * @return
	 */
	public Integer getUsedTotalCountByEmail(@Param("email") String email,
			@Param("website") Integer website);
	
	@Insert("<script>INSERT INTO t_member_integral_history(iwebsiteid,cemail,cdotype,iintegral,cremark<if test=\"istatus != null\">,istatus</if>, csource) "
			+ "VALUES(#{iwebsiteid},#{cemail},#{cdotype},#{iintegral},#{cremark}<if test=\"istatus != null\">,#{istatus}</if>, #{csource})</script>")
	@Options(useGeneratedKeys = true, keyProperty = "iid", keyColumn = "iid")
	int insert(MemberIntegralHistory memberIntegralHistory);
	
	@Insert("INSERT INTO t_order_points (ipointsid, fparvalue, istatus, cemail) VALUES "
			+ "(#{ipointsid}, #{fparvalue}, #{istatus},  #{cemail})")
	int insertOrderPoint(OrderPoints points);
	
	@Update("update t_member_integral_history set cdotype='cost',cremark = #{remark} where iid = #{id}")
	int updateRemark(@Param("remark")String remark, @Param("id")Integer id);
	
	@Select("select iid from t_member_integral_history where cemail = #{email} and iwebsiteid = #{website} and cdotype=#{dotype} and istatus = 1")
	Integer getPointByType(Point point);
	
	@Select("select count(*) from t_member_integral_history where cremark = #{remark}")
	int getByRemark(@Param("remark")String remark);
}
