package com.tomtop.base.controllers;

import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.tomtop.base.models.bo.VisitLogBo;
import com.tomtop.base.models.dto.VisitLogDto;
import com.tomtop.base.models.vo.VisitLogVo;
import com.tomtop.base.service.IVisitLogService;
import com.tomtop.base.utils.CommonUtils;
import com.tomtop.framework.core.utils.Result;

/**
 * 访问记录接口
 * 
 * @author liulj
 *
 */
@RestController
public class VisitLogController {

	@Autowired
	private IVisitLogService service;

	@Deprecated
	@SuppressWarnings("unchecked")
	// @RequestMapping(method = RequestMethod.POST, value = "/base/visitLog/v1")
	public Result addRecord(@RequestBody String body, HttpServletRequest request) {
		VisitLogVo vo = JSON.parseObject(body, VisitLogVo.class);
		String host = "tomtopweb";
		if (vo != null) {
			String aid = "";
			String url = "";
			URL u = vo.getUrl();
			if (u != null) {
				MultiMap<String> values = new MultiMap<String>();
				String uquery = u.getQuery();
				if (uquery != null) {
					UrlEncoded.decodeTo(uquery, values, "UTF-8", 1000);
					@SuppressWarnings("rawtypes")
					Iterator iter = values.entrySet().iterator();
					while (iter.hasNext()) {
						Map.Entry<String, String> entry = (Map.Entry<String, String>) iter
								.next();
						String key = entry.getKey().toString();
						if ("aid".equals(key.toLowerCase())) {
							aid = values.getString(key);
						}
					}
				}
				url = vo.getUrl().toString();
			}
			int row = service.insert(new VisitLogDto(aid, vo.getClient(), vo
					.getIp(), url, host));
			if (row > 0) {
				return new Result(Result.SUCCESS, null);
			}
		}
		return new Result(Result.FAIL, null);
	}

	/**
	 * 添加访问记录
	 * 
	 * @param vlbo
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/base/visitLog/v1/add")
	public Result addVistLog(@RequestBody VisitLogBo bo) {
		return doAddVistLog(bo);
	}

    /**
     * 添加访问记录,支持jsonp请求
     * 
     * @param vlbo
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/base/visitLog/v1/add/jsonp")
    public Result addVistLogJSONP(VisitLogBo bo) {
        return doAddVistLog(bo);
    }
    
    private Result doAddVistLog(VisitLogBo bo)
    {
     // 参数校验
        Result r = new Result();
        r.setRet(Result.FAIL);
        if (StringUtils.isEmpty(bo.getAid())) {
            r.setErrCode(CommonUtils.ERROR_CODE_PARAM_IS_NULL);
            r.setErrMsg("Parameter 'aid' can't be null");
            return r;
        }
        if (StringUtils.isEmpty(bo.getUip())) {
            r.setErrCode(CommonUtils.ERROR_CODE_PARAM_IS_NULL);
            r.setErrMsg("Parameter 'uip' can't be null");
            return r;
        }
        if (StringUtils.isEmpty(bo.getPath())) {
            r.setErrCode(CommonUtils.ERROR_CODE_PARAM_IS_NULL);
            r.setErrMsg("Parameter 'path' can't be null");
            return r;
        }
        if (StringUtils.isEmpty(bo.getSource())) {
            r.setErrCode(CommonUtils.ERROR_CODE_PARAM_IS_NULL);
            r.setErrMsg("Parameter 'source' can't be null");
            return r;
        }

        // 插入数据
        service.insert(new VisitLogDto(bo.getAid(), bo.getWebsite(), bo
                .getUip(), bo.getPath(), bo.getSource()));

        r.setRet(Result.SUCCESS);
        return r;
    }
}
