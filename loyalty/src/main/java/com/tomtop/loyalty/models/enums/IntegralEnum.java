package com.tomtop.loyalty.models.enums;

/**
 * 赠送积分行为的枚举
 * @author ztiny
 *
 */
public enum IntegralEnum {
	register{
		public String get(){
			return "register";
		}
	},
	order_payment{
		public String get(){
			return "order_payment";
		}
	},
	subscriber{
		public String get(){
			return "subscriber";
		}
	},
	upload_comment{
		public String get(){
			return "upload_comment";
		}
	},
	sign_in{
		public String get(){
			return "sign_in";
		}
	},
	sign_up{
		public String get(){
			return "sign_up";
		}
	},
	upload_video{
		public String get(){
			return "upload_video";
		}
	};
	 public abstract String get(); 
}

