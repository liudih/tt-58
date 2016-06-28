package com.tomtop.loyalty.models.third;

import com.tomtop.framework.core.utils.Result;
import com.tomtop.loyalty.models.bo.MemberUidBo;

public class Member extends Result{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MemberUidBo data;

	public MemberUidBo getData() {
		return data;
	}

	public void setData(MemberUidBo data) {
		this.data = data;
	}
	
}
