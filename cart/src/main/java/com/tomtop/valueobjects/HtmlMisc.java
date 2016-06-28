package com.tomtop.valueobjects;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class HtmlMisc {

	final List<String> headString = new LinkedList<String>();
	boolean headRendered = false;

	final List<String> tailString = new LinkedList<String>();

	/**
	 * 添加script 该方法的存在是为了解决添加脚本的真正唯一性
	 * addHeadOnce这个方法不能保证添加的唯一性，比如多个空格和少个空格，那么这两个String肯定不一样
	 * 
	 * @author lijun
	 * @param src
	 *            脚本的src
	 */
	public void addHeadScriptOnce(String src) {
		if (StringUtils.isEmpty(src)) {
			return;
		}
		src = src.trim();
		if (!headString.contains(src)) {
			headString.add(src);
			String html = "<script type='text/javascript' src='" + src
					+ "'></script>";
			this.headString.add(html);
		}
	}

	public void addTailScriptOnce(String src) {
		if (StringUtils.isEmpty(src)) {
			return;
		}
		src = src.trim();
		if (!headString.contains(src)) {
			headString.add(src);
			String html = "<script type='text/javascript' src='" + src
					+ "'></script>";
			this.tailString.add(html);
		}
	}

	/**
	 * 添加css
	 * 
	 * @author lijun
	 * @param src
	 *            css的src
	 */
	public void addHeadCssOnce(String src) {
		if (StringUtils.isEmpty(src)) {
			return;
		}
		src = src.trim();
		if (!headString.contains(src)) {
			headString.add(src);
			String html = "<link href='" + src
					+ "' rel='stylesheet' type='text/css'/>";
			this.headString.add(html);
		}
	}

}
