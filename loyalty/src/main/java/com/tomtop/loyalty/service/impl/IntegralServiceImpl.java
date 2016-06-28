package com.tomtop.loyalty.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.loyalty.mappers.loyalty.IIntegralMapper;
import com.tomtop.loyalty.models.IntegralModel;
import com.tomtop.loyalty.models.Page;
import com.tomtop.loyalty.models.Pageable;
import com.tomtop.loyalty.models.enums.IntegralEnum;
import com.tomtop.loyalty.service.IIntegralService;

/**
 * 用户积分流水
 * @author zhangxiangquan
 */
@Service
public class IntegralServiceImpl implements IIntegralService {

	@Autowired
	private IIntegralMapper iintegralMapper;
	
	
	
	@Override
	public int addIntegralForUser(IntegralModel model,String type) {
		//根据规则获取赠送的规则
		int count = 0;
		try{
			int integralNum = this.searchByactivityCode(type);
			model.setIntegralNum(integralNum);
			count = iintegralMapper.save(model);
		}catch(Exception ex){
			//TODO积分获取失败，需要对失败进行处理
		}
		return count;
	}

	@Override
	public int countIntegralForUser(String email,int webSiteId) {
		return iintegralMapper.searchUserIntegralNumByUser(email,webSiteId);
	}

	@Override
	public Page<IntegralModel> searchIntegralForUser(Pageable condition) {
		Page<IntegralModel> page = null;
		try{
			List<IntegralModel> list = iintegralMapper.searchByUser(condition);
			int total = iintegralMapper.searchTotalByUser(condition);
			page = new Page<IntegralModel>(list,total,condition);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return page;
	}

	private int searchByactivityCode(String activityCode) {
		int integralNum = 0 ;
		if(StringUtils.isNotBlank(activityCode)){
			if(IntegralEnum.order_payment.equals(activityCode)){
				integralNum = 50;
			}else if(IntegralEnum.register.equals(activityCode)){
				integralNum = 50;
			}else if(IntegralEnum.subscriber.equals(activityCode)){
				integralNum = 50;
			}else if(IntegralEnum.upload_comment.equals(activityCode)){
				integralNum = 50;
			}else if(IntegralEnum.sign_in.equals(activityCode)){
				integralNum = 50;
			}else if(IntegralEnum.sign_up.equals(activityCode)){
				integralNum = 50;
			}else if(IntegralEnum.upload_video.equals(activityCode)){
				integralNum = 50;
			}
		}
		
		return integralNum;
	}

}
