package com.tomtop.base.service.impl;

import java.util.List;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




import com.google.common.collect.Lists;
import com.tomtop.base.mappers.ClientBaseMapper;
import com.tomtop.base.models.bo.ClientBo;
import com.tomtop.base.models.dto.ClientBase;
import com.tomtop.base.service.IClientBaseService;
import com.tomtop.base.utils.CommonUtils;
import com.tomtop.framework.core.utils.BeanUtils;

/**
 * 客户端业务逻辑类
 * @author renyy
 *
 */
@Service
public class ClientBaseServiceImpl implements IClientBaseService {

	@Autowired
	ClientBaseMapper clientMapper;
	
	/**
	 * 获取所有客户端信息
	 * 
	 * @return List<ClientBo>
	 * @author renyy
	 *
	 */
	@Override
	public List<ClientBo> getClientList() {
		List<ClientBase> cbList = clientMapper.getAllClientBase();
		List<ClientBo> cboList = Lists.transform(cbList,  cb -> {
			ClientBo cbo = new ClientBo();
			BeanUtils.copyPropertys(cb, cbo);
				return cbo;
			});
		
		return cboList;
	}

	/**
	 * 获取客户端信息
	 * 
	 * @param ClientBase
	 * 			根据设定的对象的条件筛选
	 * 			
	 * @return List<ClientBo>
	 * @author renyy
	 *
	 */
	@Override
	public List<ClientBo> getClientList(ClientBase cb) {
		List<ClientBase> cbList = clientMapper.getClientBase(cb);
		List<ClientBo> cboList = Lists.transform(cbList,  cbl -> {
			 ClientBo cbo = new ClientBo();
			 BeanUtils.copyPropertys(cbl, cbo);
				return cbo;
			});
		return cboList;
	}
	
	/**
	 * 获取客户端信息
	 * 
	 * @param id 
	 * 		  主键
	 * 			
	 * @return ClientBo
	 * @author renyy
	 *
	 */
	@Override
	public ClientBo getClientById(Integer id) {
		ClientBase cb = new ClientBase();
		cb.setId(id);
		List<ClientBase> cbList = clientMapper.getClientBase(cb);
		ClientBo cbo = new ClientBo();
		if (cbList != null && cbList.size() > 0) {
			cb = cbList.get(0);
			BeanUtils.copyPropertys(cb, cbo);
			cbo.setRes(CommonUtils.SUCCESS_RES);
		}else{
			cbo.setRes(CommonUtils.NOT_DATA_RES);
			cbo.setMsg(CommonUtils.NOT_DATA_MSG_RES);
		}
		return cbo;
	}
}
