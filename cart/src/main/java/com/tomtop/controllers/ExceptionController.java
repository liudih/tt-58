package com.tomtop.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tomtop.exceptions.BadRequestException;
import com.tomtop.exceptions.CreateOrderException;
import com.tomtop.exceptions.DiscountException;
import com.tomtop.exceptions.InventoryShortageException;
import com.tomtop.exceptions.MemberAddressException;
import com.tomtop.exceptions.OrderNocompleteException;
import com.tomtop.exceptions.UserNoLoginException;
import com.tomtop.utils.FoundationService;

@Controller
@RequestMapping("/exception")
public class ExceptionController {
	
	@Autowired
	private FoundationService foundationService;

	@RequestMapping("/discount")
	public ModelAndView discount(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> model) {
		DiscountException e = (DiscountException) request.getAttribute("javax.servlet.error.exception");
		String code = "discount";
		String msg = "coupon or promo or point lock failed";
		if(e!=null){
			msg = e.getMessage();
		}
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("errorCode", code);
		map.put("errorMsg", msg);
		return new ModelAndView("pay_fail",map);
	}

	@RequestMapping("/bad-request")
	public ModelAndView badRequest(HttpServletRequest request,HttpServletResponse response, Map<String, Object> model) {
		BadRequestException e = (BadRequestException) request.getAttribute("javax.servlet.error.exception");
		String msg = "BadRequest";
//		if(e!=null){
//			msg = e.getMessage();
//		}
//		return this.errorPage(response,"BadRequest", msg);
		return returnPage("500",msg,e,"/");
	}

	@RequestMapping("/create-order")
	public ModelAndView createOrder(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> model) {
		CreateOrderException e = (CreateOrderException) request.getAttribute("javax.servlet.error.exception");
		String msg = "create order error";
//		if(e!=null){
//			msg = e.getMessage();
//		}
//		return this.errorPage(response,"create order error", msg);
		return returnPage("500",msg,e,"pay_fail");
	}

	@RequestMapping("/invalid-storage")
	public ModelAndView invalidStorage(HttpServletRequest request,HttpServletResponse response, Map<String, Object> model) {

		Exception e = (Exception) request.getAttribute("javax.servlet.error.exception");
		String msg = "invalid storage";
//		if(e!=null){
//			msg = e.getMessage();
//		}
//		return this.errorPage(response,"invalid storage",msg);
		return returnPage("500",msg,e,"pay_fail");
	}

	@RequestMapping("/member-address")
	public ModelAndView memberAddress(HttpServletRequest request,HttpServletResponse response, Map<String, Object> model) {

		MemberAddressException e = (MemberAddressException) request.getAttribute("javax.servlet.error.exception");
		String msg = "invalid address";
//		if(e!=null){
//			msg = e.getMessage();
//		}
//		return this.errorPage(response,"invalid address", msg);
		return returnPage("500",msg,e,"pay_fail");
	}

	@RequestMapping("/illegal-argument")
	public ModelAndView illegalArgument(HttpServletRequest request,HttpServletResponse response, Map<String, Object> model) {

		IllegalArgumentException e = (IllegalArgumentException) request.getAttribute("javax.servlet.error.exception");
		String msg = "so sorry,error occured";
//		if(e!=null){
//			msg = e.getMessage();
//		}
		// 跳到失败页面
		return returnPage("500",msg,e,"500");
	}

	@RequestMapping("/order-no-complete")
	public ModelAndView orderNoComplete(HttpServletRequest request,HttpServletResponse response, Map<String, Object> model) {

		OrderNocompleteException e = (OrderNocompleteException) request.getAttribute("javax.servlet.error.exception");
		String msg = "orderNoComplete";
//		if(e!=null){
//			msg = e.getMessage();
//		}
//		return this.errorPage(response,"orderNoComplete",msg);
		return returnPage("500",msg,e,"pay_fail");
	}

	@RequestMapping("/user-no-login")
	public ModelAndView userNoLongin(HttpServletRequest request,HttpServletResponse response, Map<String, Object> model) {
		UserNoLoginException e = (UserNoLoginException) request.getAttribute("javax.servlet.error.exception");
		String msg = "noLogin";
//		if(e!=null){
//			msg = e.getMessage();
//		}
//		return this.errorPage(response,"noLogin", msg);
		return returnPage("500",msg,e,"pay_fail");
	}

	@RequestMapping("/null-pointer")
	public ModelAndView exception(HttpServletRequest request,HttpServletResponse response, Map<String, Object> model) {
		NullPointerException e = (NullPointerException) request.getAttribute("javax.servlet.error.exception");
		String msg ="so sorry,error occured";
//		if(e!=null){
//			msg = e.getMessage();
//		}
//		return this.errorPage(response,"null pointer", msg);
		
		return returnPage("500",msg,e,"pay_fail");
	}

	@RequestMapping("/notfound")
	public ModelAndView notfound(HttpServletRequest request,HttpServletResponse response, Map<String, Object> model)throws IOException {
		
		return new ModelAndView("/");
	}

	@RequestMapping("/server-error")
	public ModelAndView serverError(HttpServletRequest request,HttpServletResponse response, Map<String, Object> model)throws IOException {
		String errorMsg = "so sorry,error occured.";
		Exception e = (Exception) request.getAttribute("javax.servlet.error.exception");
		 
		return returnPage("500",errorMsg,e,"pay_fail");
	}

	
	private ModelAndView returnPage(String code,String msg,Exception ex,String url){
		Map<String,String> map = new HashMap<String,String>();
		if(ex!=null){
			msg = ex.getMessage();
		}
		map.put("errorCode", "500");
		map.put("errorMsg", msg);
		String firstdomain = foundationService.getSubdomains();
		if("m".equals(firstdomain)){
			return new ModelAndView("mobile/"+url,map);
		}
		return new ModelAndView(url,map);
	}
	
	
	
	/**
	 * 支付失败跳转
	 * @param response
	 * @param code
	 * @param message
	 * @return
	 */
	public ModelAndView errorPage(HttpServletResponse response, String code,String message) {
		code = code == null ? "" : code;
		// 跳转到支付失败页面
		StringBuilder errorurl = new StringBuilder("/payment-result/error");
		errorurl.append("?errorCode=").append(code);
		errorurl.append("&error=").append(message.trim());
		return new ModelAndView(errorurl.toString());
	}

	
	
	
	@RequestMapping("/inventory-shortage")
	public ModelAndView inventoryShortage(HttpServletRequest request,HttpServletResponse response, Map<String, Object> model) {
		InventoryShortageException e = (InventoryShortageException) request.getAttribute("javax.servlet.error.exception");
		String msg ="inventory shortage";
//		if(e!=null){
//			msg = e.getMessage();
//		}
//		return this.errorPage(response,"inventory shortage",msg);
		return returnPage("500",msg,e,"pay_fail");
	}

}
