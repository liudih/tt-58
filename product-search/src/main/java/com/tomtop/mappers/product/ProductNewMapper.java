package com.tomtop.mappers.product;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tomtop.entity.NewArrivalsAgg;

public interface ProductNewMapper {

	@Select("select date_trunc('day', pb.dcreatedate) as dateStr,date_trunc('day', pb.dcreatedate) as dateName, count(pb.iid) as num from t_product_base pb,t_product_label pl "
			+ "where pb.clistingid=pl.clistingid and pl.ctype='new' and pb.istatus=1 and pb.bvisible=true and pb.iwebsiteid=#{website,jdbcType=INTEGER}"
			+ " and  pb.dcreatedate >= #{startDate,jdbcType=TIMESTAMP} "
			+ " GROUP BY datestr ORDER BY dateStr desc")
	public List<NewArrivalsAgg> getNewArrivalsAgg(@Param("website") Integer website,@Param("startDate") Date startDate);

	@Select("select #{timeName,jdbcType=VARCHAR} as dateStr, count(pb.iid) as num from t_product_base pb,t_product_label pl "
			+ "where pb.clistingid=pl.clistingid and pl.ctype='new' and pb.istatus=1 and pb.bvisible=true and pb.iwebsiteid=#{website,jdbcType=INTEGER}"
			+ " and  pb.dcreatedate >= #{startDate,jdbcType=TIMESTAMP} ")
	public NewArrivalsAgg getNewArrivalsAggWeek(@Param("website") Integer website,@Param("startDate") Date startDate,@Param("timeName") String timeName);

}
