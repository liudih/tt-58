package test;


import java.util.Date;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.tomtop.loyalty.Application;
import com.tomtop.loyalty.models.IntegralModel;
import com.tomtop.loyalty.models.Page;
import com.tomtop.loyalty.models.Pageable;
import com.tomtop.loyalty.service.IIntegralService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class Test {
	
	@Autowired
	private IIntegralService iintegralService;
	
	@org.junit.Test
	public void testAddIntegralForUser(){
		IntegralModel model = new IntegralModel();
		model.setCreateDate(new Date());
		model.setDotype("test");
		model.setEmail("test@test.com");
		model.setIntegralNum(100);
		model.setRemark("desc");
		model.setSource("test11111");
		model.setStatus(1);
		model.setWebSiteId(10);
		
		int count = iintegralService.addIntegralForUser(model,"sign_up");
		System.out.println(count);
	}
	
//	@org.junit.Test
	public void testSearchIntegralForUser(){
		IntegralModel model = new IntegralModel();
		model.setCreateDate(new Date());
//		model.setDotype("test");
		model.setEmail("test@test.com");
		model.setIntegralNum(100);
		model.setRemark("desc");
		model.setSource("test");
		model.setStatus(1);
		model.setWebSiteId(1);
		
		Pageable page = new Pageable(0,10,model);
		Page<IntegralModel> result = iintegralService.searchIntegralForUser(page);
		System.out.println(result.getTotal()+"===================>");
		result.getContent().forEach( o->{
			System.out.println(o.getId()+"===>"+o.getIntegralNum());
		});
	}
	
//	@org.junit.Test
	public void testCountIntegralForUser(){
		int count = iintegralService.countIntegralForUser("test@test.com",10);
		System.out.println(count+"================");
	}
}

