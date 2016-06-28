package com.tomtop.utils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tomtop.entity.Currency;
import com.tomtop.entity.ProductCompute;
import com.tomtop.entity.ProductPrice;
import com.tomtop.entity.ProductSalePrice;
import com.tomtop.entity.index.PromotionPrice;
import com.tomtop.entity.index.SalePrice;

@Component
public class ProductComputeUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductComputeUtil.class);
	private static final String ymdhms = "yyyy-MM-dd HH:mm:ss";
	
	public ProductCompute getPrice(Double constPrice, Double price,
			List<PromotionPrice> promsPrice, Currency currency) {
		price = price >= constPrice ? price : constPrice;
		price = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
		constPrice = new BigDecimal(constPrice).setScale(2,
				BigDecimal.ROUND_HALF_UP).doubleValue();
		ProductCompute bo = new ProductCompute();
		bo.setOriginalPrice(new BigDecimal(price)
				.multiply(new BigDecimal(currency.getCurrentRate()))
				.setScale(currency.getCode().equals("JPY") ? 0 : 2,
						BigDecimal.ROUND_HALF_UP).toString());
		double dprice = 0.0;
		if (promsPrice != null && promsPrice.size() > 0) {
			for (PromotionPrice p : promsPrice) {
				try {
					Date begin = DateUtils.parseDate(p.getBeginDate(),
							"yyyy-MM-dd HH:mm:ss");
					Date end = DateUtils.parseDate(p.getEndDate(),
							"yyyy-MM-dd HH:mm:ss");
					Date day = DateUtils.parseDate(DateFormatUtils.formatUTC(
							new Date(), "yyyy-MM-dd HH:mm:ss"),
							"yyyy-MM-dd HH:mm:ss");
					if (day.before(end) && day.after(begin)) {
						dprice = p.getPrice();
						break;
					}
				} catch (Exception e) {
					logger.error("转换折扣价日期失败,errormsg:", e.getMessage());
				}
			}
		}
		if (dprice == 0.0) {
			dprice = price;
		} else {
			if (dprice > price) {
				dprice = price;
			} else if (dprice < constPrice) {
				dprice = constPrice;
			}
		}
		bo.setPrice(new BigDecimal(dprice)
				.multiply(new BigDecimal(currency.getCurrentRate()))
				.setScale(currency.getCode().equals("JPY") ? 0 : 2,
						BigDecimal.ROUND_HALF_UP).toString());
		return bo;
	}
	
	public ProductCompute getPriceEndDate(Double constPrice, Double price,
			List<PromotionPrice> promsPrice, Currency currency) {
		price = price >= constPrice ? price : constPrice;
		price = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
		constPrice = new BigDecimal(constPrice).setScale(2,
				BigDecimal.ROUND_HALF_UP).doubleValue();
		ProductCompute bo = new ProductCompute();
		bo.setOriginalPrice(new BigDecimal(price)
				.multiply(new BigDecimal(currency.getCurrentRate()))
				.setScale(currency.getCode().equals("JPY") ? 0 : 2,
						BigDecimal.ROUND_HALF_UP).toString());
		double dprice = 0.0;
		String endDate = "";
		if (promsPrice != null && promsPrice.size() > 0) {
			for (PromotionPrice p : promsPrice) {
				try {
					Date begin = DateUtils.parseDate(p.getBeginDate(),
							"yyyy-MM-dd HH:mm:ss");
					Date end = DateUtils.parseDate(p.getEndDate(),
							"yyyy-MM-dd HH:mm:ss");
					Date day = DateUtils.parseDate(DateFormatUtils.formatUTC(
							new Date(), "yyyy-MM-dd HH:mm:ss"),
							"yyyy-MM-dd HH:mm:ss");
					if (day.before(end) && day.after(begin)) {
						dprice = p.getPrice();
						endDate = p.getEndDate();
						break;
					}
				} catch (Exception e) {
					logger.error("转换折扣价日期失败,errormsg:", e.getMessage());
				}
			}
		}
		if (dprice == 0.0) {
			dprice = price;
		} else {
			if (dprice > price) {
				dprice = price;
			} else if (dprice < constPrice) {
				dprice = constPrice;
			}
		}
		bo.setPrice(new BigDecimal(dprice)
				.multiply(new BigDecimal(currency.getCurrentRate()))
				.setScale(currency.getCode().equals("JPY") ? 0 : 2,
						BigDecimal.ROUND_HALF_UP).toString());
		bo.setEndDate(endDate);
		return bo;
	}
	
	public boolean getIsSales(List<PromotionPrice> promsPrice){
		boolean isSale = false;
		if (promsPrice != null && promsPrice.size() > 0) {
			for (PromotionPrice p : promsPrice) {
				try {
					Date begin = DateUtils.parseDate(p.getBeginDate(),
							"yyyy-MM-dd HH:mm:ss");
					Date end = DateUtils.parseDate(p.getEndDate(),
							"yyyy-MM-dd HH:mm:ss");
					Date day = DateUtils.parseDate(DateFormatUtils.formatUTC(
							new Date(), "yyyy-MM-dd HH:mm:ss"),
							"yyyy-MM-dd HH:mm:ss");
					if (day.before(end) && day.after(begin)) {
						isSale = true;
						break;
					}
				} catch (Exception e) {
					logger.error("转换折扣价日期失败,errormsg:", e.getMessage());
				}
			}
		}
		return isSale;
	}
	
	public boolean getIsDepotSales(List<SalePrice> salePrice){
		boolean isSale = false;
		if (salePrice != null && salePrice.size() > 0) {
			for (SalePrice p : salePrice) {
				try {
					Date begin = DateUtils.parseDate(p.getBeginDate(),
							"yyyy-MM-dd HH:mm:ss");
					Date end = DateUtils.parseDate(p.getEndDate(),
							"yyyy-MM-dd HH:mm:ss");
					Date day = DateUtils.parseDate(DateFormatUtils.formatUTC(
							new Date(), "yyyy-MM-dd HH:mm:ss"),
							"yyyy-MM-dd HH:mm:ss");
					if (day.before(end) && day.after(begin)) {
						isSale = true;
						break;
					}
				} catch (Exception e) {
					logger.error("转换折扣价日期失败,errormsg:", e.getMessage());
				}
			}
		}
		return isSale;
	}
	
	public ProductPrice getProductPrice(Double constPrice, Double price,
			List<ProductSalePrice> promsPrice, Currency currency) {
		price = price >= constPrice ? price : constPrice;
		price = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
		constPrice = new BigDecimal(constPrice).setScale(2,
				BigDecimal.ROUND_HALF_UP).doubleValue();
		ProductPrice bo = new ProductPrice();
		bo.setOrigprice(new BigDecimal(price)
				.multiply(new BigDecimal(currency.getCurrentRate()))
				.setScale(currency.getCode().equals("JPY") ? 0 : 2,
						BigDecimal.ROUND_HALF_UP).toString());
		double dprice = 0.0;
		if (promsPrice != null && promsPrice.size() > 0) {
			for (ProductSalePrice p : promsPrice) {
				try {
					String beginStr = DateFormatUtils.format(p.getDbegindate(), ymdhms);
					String endStr = DateFormatUtils.format(p.getDenddate(),ymdhms);
					Date begin = DateUtil.StringToDate(beginStr, ymdhms);
					Date end = DateUtil.StringToDate(endStr, ymdhms);
					Date day = DateUtils.parseDate(DateFormatUtils.formatUTC(
							new Date(), "yyyy-MM-dd HH:mm:ss"),
							"yyyy-MM-dd HH:mm:ss");
					if (day.before(end) && day.after(begin)) {
						dprice = p.getFsaleprice();
						break;
					}
				} catch (Exception e) {
					logger.error("转换折扣价日期失败,errormsg:", e.getMessage());
				}
			}
		}
		if (dprice == 0.0) {
			dprice = price;
		} else {
			if (dprice > price) {
				dprice = price;
			} else if (dprice < constPrice) {
				dprice = constPrice;
			}
		}
		bo.setNowprice(new BigDecimal(dprice)
				.multiply(new BigDecimal(currency.getCurrentRate()))
				.setScale(currency.getCode().equals("JPY") ? 0 : 2,
						BigDecimal.ROUND_HALF_UP).toString());
		return bo;
	}
	
	public ProductPrice getProductPrice(Double constPrice, Double price, Currency currency) {
		price = price >= constPrice ? price : constPrice;
		price = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
		constPrice = new BigDecimal(constPrice).setScale(2,
				BigDecimal.ROUND_HALF_UP).doubleValue();
		ProductPrice bo = new ProductPrice();
		bo.setOrigprice(new BigDecimal(price)
				.multiply(new BigDecimal(currency.getCurrentRate()))
				.setScale(currency.getCode().equals("JPY") ? 0 : 2,
						BigDecimal.ROUND_HALF_UP).toString());
		double dprice = 0.0;
		if (dprice == 0.0) {
			dprice = price;
		} else {
			if (dprice > price) {
				dprice = price;
			} else if (dprice < constPrice) {
				dprice = constPrice;
			}
		}
		bo.setNowprice(new BigDecimal(dprice)
				.multiply(new BigDecimal(currency.getCurrentRate()))
				.setScale(currency.getCode().equals("JPY") ? 0 : 2,
						BigDecimal.ROUND_HALF_UP).toString());
		return bo;
	}
	
	
	public ProductCompute getDepotPrice(Double costPrice,Double price,
			List<SalePrice> salePrice, Currency currency) {
		price = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
		costPrice = new BigDecimal(costPrice).setScale(2,
				BigDecimal.ROUND_HALF_UP).doubleValue();
		ProductCompute bo = new ProductCompute();
		double dprice = price;
		String endDate = "";
		if (salePrice != null && salePrice.size() > 0) {
			for (SalePrice p : salePrice) {
				try {
					Date begin = DateUtils.parseDate(p.getBeginDate(),
							"yyyy-MM-dd HH:mm:ss");
					Date end = DateUtils.parseDate(p.getEndDate(),
							"yyyy-MM-dd HH:mm:ss");
					Date day = DateUtils.parseDate(DateFormatUtils.formatUTC(
							new Date(), "yyyy-MM-dd HH:mm:ss"),
							"yyyy-MM-dd HH:mm:ss");
					if (day.before(end) && day.after(begin)) {
						dprice = p.getPrice();
						endDate = p.getEndDate();
						break;
					}
				} catch (Exception e) {
					logger.error("转换折扣价日期失败,errormsg:", e.getMessage());
				}
			}
		}
		//dprice促销价 大于 原价 直接取原价
		if (dprice > price) {
			dprice = price;
			endDate = "";
		}
		//促销价 小于成本价 则取成本价
		if (dprice < costPrice) {
			dprice = costPrice;
			endDate = "";
			//设置成本价
			bo.setOriginalPrice(new BigDecimal(dprice)
			.multiply(new BigDecimal(currency.getCurrentRate()))
			.setScale(currency.getCode().equals("JPY") ? 0 : 2,
					BigDecimal.ROUND_HALF_UP).toString());
		}else{
			//设置原价
			bo.setOriginalPrice(new BigDecimal(price)
			.multiply(new BigDecimal(currency.getCurrentRate()))
			.setScale(currency.getCode().equals("JPY") ? 0 : 2,
					BigDecimal.ROUND_HALF_UP).toString());
		}
		//设置现价
		bo.setPrice(new BigDecimal(dprice)
				.multiply(new BigDecimal(currency.getCurrentRate()))
				.setScale(currency.getCode().equals("JPY") ? 0 : 2,
						BigDecimal.ROUND_HALF_UP).toString());
		bo.setEndDate(endDate);
		return bo;
	}
}
