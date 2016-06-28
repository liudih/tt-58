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
import com.tomtop.exceptions.NotFindPaymentException;
import com.tomtop.exceptions.OrderNocompleteException;
import com.tomtop.exceptions.UserNoLoginException;

public class CustomizerErrorPage implements EmbeddedServletContainerCustomizer {

	
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		
		container.addErrorPages(new ErrorPage(BadRequestException.class, "/exception/server-exception"));
		
		container.addErrorPages(new ErrorPage(NullPointerException.class, "/exception/server-exception"));
		
		container.addErrorPages(new ErrorPage(CreateOrderException.class, "/exception/server-exception"));
		
		container.addErrorPages(new ErrorPage(InvalidStorageException.class, "/exception/server-exception"));
		
		container.addErrorPages(new ErrorPage(InventoryShortageException.class, "/exception/server-exception"));
		
		container.addErrorPages(new ErrorPage(MemberAddressException.class, "/exception/server-exception"));
		
		container.addErrorPages(new ErrorPage(NotFindPaymentException.class, "/exception/server-exception"));
		
		container.addErrorPages(new ErrorPage(OrderNocompleteException.class, "/exception/server-exception"));
		
		container.addErrorPages(new ErrorPage(UserNoLoginException.class, "/exception/server-exception"));
		
		container.addErrorPages(new ErrorPage(DiscountException.class, "/exception/server-exception"));
		
		
		
		
		container.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/exception/exception"));
		
		container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/exception/not-found"));
		
		
	}

}
