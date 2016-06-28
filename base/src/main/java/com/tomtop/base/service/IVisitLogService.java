package com.tomtop.base.service;

import com.tomtop.base.models.dto.VisitLogDto;

/**
 * 访问记录service
 * 
 * @author liulj
 *
 */
public interface IVisitLogService {
	public int insert(VisitLogDto visitLog);
}
