package member;

import java.util.List;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tomtop.framework.core.utils.Page;
import com.tomtop.member.Application;
import com.tomtop.member.models.bo.BaseBo;
import com.tomtop.member.models.bo.MemberReviewsBo;
import com.tomtop.member.models.bo.ReviewsBo;
import com.tomtop.member.service.IMemberReviewsService;
import com.tomtop.member.service.IReviewService;
/**
 * 用户评论单元测试
 * @author renyy
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class UserReview {
	
	   //private MockMvc mvc;  
		@Autowired
		IMemberReviewsService memberReviewService;
		@Autowired
		IReviewService reviewService;
		
		public String email="2853789707@qq.com";//测试邮箱
		public Integer status = 1;//测试默认状态
		public Integer website = 1;//测试默认站点
		public Integer page = 1;//测试默认当前页
		public Integer size = 10;//测试默认获取记录数大小
		
		/**
		 * 测试获取用户评论列表
		 */
	   @Test 
	   public void testMemberReview(){
		   Integer dateType = 0;
			//获取记录总数
			Integer totalReviewsCount = memberReviewService
					.getTotalReviewsCountByMemberEmailAndSiteId(email,
							status, dateType, website);
		
			List<MemberReviewsBo> mrbolist = reviewService.getMemberReviewsBo(email, page, size, status, dateType, website);
			
			Page pageObj = Page.getPage(page, totalReviewsCount, size);
			System.out.println("当前获取记录数:" + mrbolist.size());
			System.out.println("总记录数:" + pageObj.getTotalRecord());
	   }
	   
		/**
		 * 测试获取用户评论列表
		 * 必须购买了商品后并完成订单 才可评论 并且 商品只能够评论一次
		 * 目前上传图片无法进行测试
		 * 设置listingId,sku,email,订单的iid
		 * @throws Exception 
		 */
	   @Test 
	   public void testAddMemberReview() throws Exception{
		  //MultipartHttpServletRequest request = new HttpServletRequest();
		   ReviewsBo rb = new ReviewsBo();
		   rb.setListingId("");
		   rb.setSku("");
		   rb.setComment("test comment ");//评论内容
		   rb.setPs(5);//价格评分
		   rb.setQs(5);//质量评分
		   rb.setSs(5);//物流评分
		   rb.setUs(5);//有用评级
		   rb.setEmail(email);
		   rb.setCountryName("US");//国家名称
		   rb.setPform("test");//来源
		   rb.setOid(1234);//订单的iid
		   BaseBo bb = reviewService.addReview(rb, null);
			System.out.println("状态:" + bb.getRes());
			System.out.println("msg:" + bb.getMsg());
			
		   }
	   
	/*   @Before  
	   public void setUp() {  
	       mvc = MockMvcBuilders.standaloneSetup(new MemberReviewController()).build();  
	   }  
	   
	   @Test
	   public void contextLoads() throws Exception {
		   ResultActions actions = mvc.perform(MockMvcRequestBuilders.get("/member/v1/review/list?email=2853789707@qq.com").accept(MediaType.APPLICATION_JSON));  
	       //actions.andExpect(status().isOk());  
		   actions.andExpect(status().isOk()); 
		   actions.andExpect(content().string(equalTo("Hello world")));  
	   }
	   */
	/*   public void run(String... args) throws Exception {  
	        RestTemplate template = new RestTemplate();  
	        Result greeting = template.getForObject("http://localhost:9003/member/v1/review/list", Result.class);  
	        System.err.println(greeting);         
	   }  */
}
