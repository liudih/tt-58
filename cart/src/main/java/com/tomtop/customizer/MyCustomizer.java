package com.tomtop.customizer;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.http.HttpStatus;

import com.tomtop.exceptions.BadRequestException;
import com.tomtop.exceptions.CreateOrderException;
import com.tomtop.exceptions.DiscountException;
import com.tomtop.exceptions.InvalidStorageException;
import com.tomtop.exceptions.InventoryShortageException;
import com.tomtop.exceptions.MemberAddressException;
import com.tomtop.exceptions.OrderNocompleteException;
import com.tomtop.exceptions.UserNoLoginException;

/**
 * 自定义异常跳转，该类在ApplicationConfigurations 里面配置启用
 * @author Administrator
 *
 */
public   class  MyCustomizer implements EmbeddedServletContainerCustomizer {
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		
		container.addErrorPages(new ErrorPage(DiscountException.class,"/exception/discount"));

		container.addErrorPages(new ErrorPage(BadRequestException.class,"/exception/bad-request"));

		container.addErrorPages(new ErrorPage(CreateOrderException.class,"/exception/create-order"));

		container.addErrorPages(new ErrorPage(InvalidStorageException.class,"/exception/invalid-storage"));

		container.addErrorPages(new ErrorPage(MemberAddressException.class,"/exception/member-address"));
		
		container.addErrorPages(new ErrorPage(IllegalArgumentException.class,"/exception/illegal-argument"));
		
		container.addErrorPages(new ErrorPage(OrderNocompleteException.class,"/exception/order-no-complete"));
		
		container.addErrorPages(new ErrorPage(UserNoLoginException.class,"/exception/user-no-login"));
		
		container.addErrorPages(new ErrorPage(NullPointerException.class,"/exception/null-pointer"));
		
		container.addErrorPages(new ErrorPage(InventoryShortageException.class,"/exception/inventory-shortage"));
		
		container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,"/exception/notfound"));
		
		container.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/exception/exception"));
		
		container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,"/exception/server-error"));
		
		container.addErrorPages(new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED,"/exception/server-error"));
		
		container.addErrorPages(new ErrorPage(HttpStatus.BAD_GATEWAY,"/exception/server-error"));
	}
}
