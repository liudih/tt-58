package member;


import com.alibaba.fastjson.JSONObject;
import com.tomtop.member.utils.HttpSendRequest;
/**
 * 用户测试用例
 * @author renyy
 *
 */
public class UserCoupon {
	
	public static String email = "2853789707@qq.com";//用户邮箱
	public static Integer website = 10;//站点
	//public static String commUrl = "http://localhost:9003";
	public static String commUrl = "http://192.168.220.53:8003/";
	public static String activationSendEmail = commUrl + "member/v1/activate/email";//发送激活邮件地址
	public static String activationEmail = commUrl + "member/v1/activate";//激活用户
	public static String subscribe = commUrl +"member/v1/subscribe";//邮件订阅
	public static String addReview = commUrl + "/v1/review/add";//添加评论
	
	/*
	 
	 public static void main(String[] args) throws Exception {
		String url = activationSendEmail + "?email=" + email + "&website="+website;
		System.out.println(url);
		String res = HttpClientUtil.doGet(url);
		System.out.println(res);
	}*/
	
	/*
	 public static void main(String[] args) throws Exception {
		String code = "d5eec38688d74c96aec38688d77c96bd";
		String url = activationEmail + "?code=" + code + "&website="+website;
		System.out.println(url);
		String res = HttpClientUtil.doGet(url);
		System.out.println(res);
	}*/
	
/*	 public static void main(String[] args) throws Exception {
		 
			String url = subscribe;
//		 	String cars = "TOPS,BOTTOMS";
//			String url = subscribe;
//			url = url + "?email=" + email +"&website=" + website + "lang=1&categoryArr=" + cars;
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("lang", 1);
			jsonObj.put("website", website);
			jsonObj.put("email", email);
			String[] cars = {"TOPS","BOTTOMS"};
			jsonObj.put("categoryArr", cars);
			System.out.println(jsonObj.toJSONString());
			boolean res = HttpSendRequest.sendPost(url, jsonObj.toJSONString());
			System.out.println(res);
	 }*/
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String tag = "new,freeShipping";
		
		String[] tags = tag.split(",");
		for (int i = 0; i < tags.length; i++) {
			System.out.println(tags[i]);
		}
	}

	
}
