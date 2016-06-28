package com.tomtop.valueobjects.payment;

import java.util.List;

import com.tomtop.valueobjects.Discount;
import com.tomtop.valueobjects.DiscountUsedState;

/**
 * 结算明细
 * 
 * @author lijun
 *
 */
public class CheckoutDetails {
	// 小计
	private final double subTotal;
	// 实付金额
	private final double total;
	// 折扣
	private final double discount;
	// 折扣明细
	private final List<Discount> usedDiscount;

	public CheckoutDetails(double total, double subTotal, double discount,
			List<Discount> usedDiscount) {
		this.subTotal = subTotal;
		this.discount = discount;
		this.usedDiscount = usedDiscount;
		this.total = total;
	}

	public double getSubTotal() {
		return subTotal;
	}

	public double getDiscount() {
		return discount;
	}

	public double getTotal() {
		return total;
	}

	public List<Discount> getUsedDiscount() {
		return usedDiscount;
	}

}
