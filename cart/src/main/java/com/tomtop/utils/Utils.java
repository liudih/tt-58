package com.tomtop.utils;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;

public class Utils {

	public static String percent(Double percent) {
		if (percent == null) {
			return "";
		}
		percent = Math.ceil(percent * 100);
		DecimalFormat df = new DecimalFormat("#0");
		return df.format(percent);
	}

	public static String money(Double money) {
		return money(money, null);
	}

	public static String money(Double money, String currencyCode) {
		if (money == null) {
			return "";
		}
		if (currencyCode != null && "JPY".equals(currencyCode)) {
			// 四舍五入
			long moneyFloat = Math.round(money);
			DecimalFormat df = new DecimalFormat("#,##0");
			return df.format(moneyFloat);
		}
		DecimalFormat df = new DecimalFormat("#,##0.00");
		return df.format(money);
	}

	/**
	 * 
	 * @param money
	 * @param currencyCode
	 * @return
	 */
	public static String moneyNoComma(Double money, String currencyCode) {
		if (money == null) {
			return "";
		}
		if (currencyCode != null && "JPY".equals(currencyCode)) {
			// 四舍五入
			long moneyFloat = Math.round(money);
			DecimalFormat df = new DecimalFormat("###0");
			return df.format(moneyFloat);
		}
		DecimalFormat df = new DecimalFormat("###0.00");
		return df.format(money);
	}

	public static <T> List<List<T>> partition(List<T> list, int partitionSize) {
		List<List<T>> partitioned = Lists.newLinkedList();
		for (int i = 0; i < list.size(); i += partitionSize) {
			partitioned.add(list.subList(i,
					i + Math.min(partitionSize, list.size() - i)));
		}
		return partitioned;
	}

	public static <T> List<T> flatten(List<List<T>> list) {
		List<T> all = Lists.newLinkedList();
		for (List<T> o : list) {
			if (o != null) {
				all.addAll(o);
			}
		}
		return all;
	}

	public static String getIncompleteEmail(String email) {
		StringBuffer sBuffer = new StringBuffer();
		if (null != email && !"".equals(email)) {
			sBuffer.append(email.substring(0, 1));
			sBuffer.append("***");
			Integer index = email.indexOf("@");
			String incompleteEmailTolower = email.substring(index,
					email.length()).toLowerCase();
			if (incompleteEmailTolower.contains("tomtop")) {
				incompleteEmailTolower = incompleteEmailTolower.replace(
						"tomtop", "******");
			}
			sBuffer.append(incompleteEmailTolower);
		}
		return sBuffer.toString();
	}

	/**
	 * 集合转换为逗号分隔的字符串
	 * 
	 * @param list
	 * @return
	 * @author shuliangxing
	 * @date 2016年6月3日 下午5:21:31
	 */
	public static String list2String(List<String> list) {
		if (list == null || list.isEmpty()) {
			return "";
		}

		StringBuilder str = new StringBuilder();
		boolean flag = false;
		for (String string : list) {
			if (StringUtils.isBlank(string)) {
				continue;
			}

			if (flag) {
				str.append(",");
			} else {
				flag = true;
			}
			str.append(string);
		}
		return str.toString();
	}

}