package com.tomtop.loyalty.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tomtop.loyalty.controllers.CouponController;
import com.tomtop.loyalty.mappers.loyalty.PointMapper;
import com.tomtop.loyalty.models.MemberEvent;
import com.tomtop.loyalty.models.MemberIntegralHistory;
import com.tomtop.loyalty.models.Point;
import com.tomtop.loyalty.models.Prefer;
import com.tomtop.loyalty.service.IPointService;

@Service
public class PointService implements IPointService {

	@Autowired
	ThirdService thirdService;

	@Autowired
	PointMapper pointMapper;

	public static final String LOCK_TYPE = "lock";
	public static final String COST_TYPE = "cost";
	private static Logger log = LoggerFactory
			.getLogger(PointService.class);

	@Override
	public Integer getUserTotalPointByEmail(String email, Integer website) {
		Integer point = pointMapper.getUserTotalPointByEmail(email, website);
		return null == point ? 0 : point;
	}

	@Override
	public Prefer isPointAvailable(String email, Integer point,
			Integer website, String currency) {
		Prefer result = new Prefer();
		Integer totalPoint = this.getUserTotalPointByEmail(email, website);
		if (point > totalPoint) {
			result.setErrMsg("We're sorry that you don't have enough points");
			return result;
		}
		Double money = this.convertPointToMoney(point, currency);
		result.setIsSuccess(true);
		result.setPreferType(CouponController.LOYALTY_TYPE_POINT);
		result.setValue(money);
		return result;
	}

	@Override
	public Double convertPointToMoney(Integer point, String currency) {
		// 1美元兑换100积分
		Double usdMoney = (double) (point * (0.01) * -1);
		Double targetMoney = Double.valueOf(thirdService.exchangeCurrency(
				usdMoney, "USD", currency));
		return targetMoney;
	}

	@Override
	public List<Point> getUnusedPointHistory(String email, Integer website,
			int page, int size) {
		List<Point> result = pointMapper.getUnusedPointHistory(email, website,
				page, size);
		return result;
	}

	@Override
	public List<Point> getUsedPointHistory(String email, Integer website,
			int page, int size) {
		List<Point> result = pointMapper.getUsedPointHistory(email, website,
				page, size);
		return result;
	}

	@Override
	public Integer getUnusedTotalCountByEmail(String email, Integer website) {
		Integer result = pointMapper.getUnusedTotalCountByEmail(email, website);
		return null == result ? 0 : result;
	}

	@Override
	public Integer getUsedTotalCountByEmail(String email, Integer website) {
		Integer result = pointMapper.getUsedTotalCountByEmail(email, website);
		return null == result ? 0 : result;
	}

	@Override
	public Integer lockPoints(String email, Integer website, Integer point) {
		MemberIntegralHistory history = new MemberIntegralHistory();
		history.setIwebsiteid(website);
		history.setCemail(email);
		history.setCdotype(LOCK_TYPE);
		history.setIintegral(point * -1);
		history.setCremark("Pay for order.");
		history.setCsource("cost");
		if (this.insertIntegralHistory(history)) {
			return history.getIid();
		}
		return null;
	}

	@Override
	public boolean insertIntegralHistory(
			MemberIntegralHistory memberIntegralHistory) {
		int result = pointMapper.insert(memberIntegralHistory);
		return result > 0 ? true : false;
	}

	@Override
	public boolean updateRemarkById(String remark, Integer id) {
		int i = pointMapper.updateRemark(remark, id);
		if (i == 1) {
			return true;
		}
		return false;
	}

	@Override
	public Boolean givingPoint(MemberEvent member, Integer point, String type,
			String remark, String source) {
		MemberIntegralHistory mhistory = new MemberIntegralHistory();
		mhistory.setCemail(member.getEmail());
		mhistory.setCremark(remark);
		mhistory.setCdotype(type);
		mhistory.setIintegral(point);
		mhistory.setIwebsiteid(member.getWebsite());
		mhistory.setIstatus(1);
		mhistory.setCsource(source);
		log.info("--------mhistory1="+JSONObject.toJSONString(mhistory));
		return insertIntegralHistory(mhistory);
	}

	@Override
	public Integer getPointByType(Point point) {
		Integer id=pointMapper.getPointByType(point);
		return id;
	}

	@Override
	public int getByRemark(String remark) {
		log.info("---remark="+remark);
		return pointMapper.getByRemark(remark);
	}

	@Override
	public Map<String, Integer> getUserPointInfo(String email, Integer website) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		Integer TotalPoint = pointMapper.getPointAllSum(email, website);//获取的所有积分
		Integer AvailablePoints =pointMapper.getPointOnLock(email, website);// 剩余未使用积分，包括锁定积分在内 
		Integer  PendingPoints =pointMapper.getLockPointSum(email, website);//锁定的总积分 返回值为负数
		map.put("TotalPoints", TotalPoint==null?0:TotalPoint);
		map.put("AvailablePoints", AvailablePoints==null?0:AvailablePoints);
		map.put("PendingPoints", PendingPoints==null?0:PendingPoints);
		return map;
	}

}
