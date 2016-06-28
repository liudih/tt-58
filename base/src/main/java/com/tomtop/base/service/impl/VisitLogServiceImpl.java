package com.tomtop.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.base.models.dto.VisitLogDto;
import com.tomtop.base.producttracking.mappers.VisitLogMapper;
import com.tomtop.base.service.IVisitLogService;

/**
 * 访问记录
 * 
 * @author liulj
 *
 */
@Service("visitLogService")
public class VisitLogServiceImpl implements IVisitLogService {

	@Autowired
	private VisitLogMapper mapper;

	@Override
	public int insert(VisitLogDto visitLog) {
		return mapper.insert(visitLog);
	}
}
