package com.tomtop.services;

import com.tomtop.entity.BaseBean;
import com.tomtop.entity.ReportErrorVo;
import com.tomtop.entity.WholesaleInquiryVo;

public interface IFeedbackService {

	public BaseBean addWholesaleInquiry(WholesaleInquiryVo wivo);
	
	public BaseBean addReportError(ReportErrorVo revo);
}
