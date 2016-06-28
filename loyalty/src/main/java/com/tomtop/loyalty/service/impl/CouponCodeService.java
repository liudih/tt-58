package com.tomtop.loyalty.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.loyalty.mappers.loyalty.CodeMapper;
import com.tomtop.loyalty.models.CouponCode;
import com.tomtop.loyalty.models.Rule;
import com.tomtop.loyalty.models.enums.CouponCodeEnum;
import com.tomtop.loyalty.models.enums.PreferRuleEnum;
import com.tomtop.loyalty.service.ICouponCodeService;
import com.tomtop.loyalty.utils.RandomNumberUtil;

@Service
public class CouponCodeService implements ICouponCodeService {

	@Autowired
	PreferRuleService ruleService;

	@Autowired
	CodeMapper codeMappper;
	// 当后台未登录时，规则创建的用户id默认值
	private static final int NULL_CREATEOR = 0;

	// 因随机数碰撞导致重新生成随机数的次数
	private static final int GENERATE_TIME = 100;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public CouponCode getCouponCodeByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCodeIdByRuleId(int ruleId, boolean status, int websiteId,
			int creatorId) {
		// 所有外部调用都是分配出去的
		return this.createCodeIdByRuleId(ruleId, true, websiteId, creatorId);
	}

	/**
	 * 通过ruleId创建code
	 * 
	 * @author xiaoch
	 * @param ruleId
	 *            规则id
	 * @param status
	 *            true：新创建的code是要分配出去的 false：没有分配给人
	 * @param websiteId
	 * @param creator
	 *            后台用户id
	 * @return
	 */
	private int createCodeIdByRuleId(int ruleId, boolean status, int websiteId,
			Integer creator) {
		int result = 0;
		Rule couponRule = ruleService.getRuleByRuleid(ruleId);
		if (null != couponRule
				&& couponRule.getIstatus() == PreferRuleEnum.Status.ON
						.getStatusid()) {
			// 应用平台判断
			if (!org.apache.commons.lang3.StringUtils.isEmpty(couponRule
					.getCwebsiteid())) {
				String[] loginTerCheck = org.apache.commons.lang3.StringUtils
						.split(couponRule.getCwebsiteid(), ",");
				List<Integer> checks = new ArrayList<>(loginTerCheck.length);
				for (int i = 0; i < loginTerCheck.length; i++) {
					checks.add(Integer.parseInt(loginTerCheck[i]));
				}
				CouponCode couponCode = new CouponCode();
				couponCode.setIcouponruleid(ruleId);
				couponCode.setCcode(String.valueOf(RandomNumberUtil
						.getRandomNumber()));
				// 为code 生成不同的使用状态
				if (status) {
					couponCode.setIusestatus(CouponCodeEnum.UseStatus.ALLOT
							.getStatusid());
				} else {
					couponCode.setIusestatus(CouponCodeEnum.UseStatus.UNALLOT
							.getStatusid());
				}
				// Integer creator = null;
				// 获取code创建人
				try {
					// creator = iAdminuserProvider.getCurrentUser();
					if (null != creator) {
						couponCode.setIcreator(creator);
					} else {
						couponCode.setIcreator(NULL_CREATEOR);
					}
				} catch (Exception e) {
					couponCode.setIcreator(NULL_CREATEOR);
				}
				int repeatTime = 0;
				do {
					try {
						codeMappper.add(couponCode);
						result = couponCode.getIid();
					} catch (Exception e) {
						repeatTime++;
						logger.error("Random number collision ! Repeat number="
								+ repeatTime);
						couponCode.setCcode(String.valueOf(RandomNumberUtil
								.getRandomNumber()));
					}
				} while (result <= 0 && repeatTime < GENERATE_TIME);

			}

		}

		return result;

	}

	@Override
	/**
	 * 根据id查询code
	 * 
	 * @param id
	 * @return
	 */
	public String getCodeById(Integer id) {
		return codeMappper.getCodeById(id);
	}

}
