package com.nayaware.webdesigner.util;

import org.mozilla.interfaces.nsIDOMElement;

/**
 * @author Winston Prakash
 * @version 1.0
 */

public class WebDesignerUtils {
	public static boolean isResizableElement(nsIDOMElement node) {
		String name = node.getNodeName().toLowerCase();
		if (name.equals("div") || name.equals("p") || name.equals("td")
				|| name.equals("table") || name.equals("li")
				|| name.equals("ul") || name.equals("img")
				|| name.equals("textarea") || name.equals("iframe")
				|| name.equals("form")) {
			return true;
		} else {
			return false;
		}
	}
}
