package com.tomtop.utils;

import com.tomtop.valueobjects.HtmlMisc;

/**
 * Used to manipulate HTML head section and before body, used to insert
 * stylesheets/scripts.
 * 
 * @author lijun
 *
 */
public class HtmlUtils {
	private static final ThreadLocal<HtmlMisc> LOCALS = new ThreadLocal<HtmlMisc>();

	public static HtmlMisc currentMisc() {
		HtmlMisc misc = LOCALS.get();
		if (misc == null) {
			misc = new HtmlMisc();
			LOCALS.set(misc);
		}
		return misc;
	}
}
