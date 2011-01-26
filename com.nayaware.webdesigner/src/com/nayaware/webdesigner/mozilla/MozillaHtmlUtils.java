package com.nayaware.webdesigner.mozilla;

import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class MozillaHtmlUtils {

	public static boolean isContainerNode(nsIDOMNode node) {
		if (isElement(node)) {
			String name = node.getNodeName();
			if (name.equals("body") || name.equals("div") || name.equals("p")
					|| name.equals("td") || name.equals("form")
					|| name.equals("li") || name.equals("ul")) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public static nsIDOMElement getElelement(nsIDOMNode node) {
		if (isElement(node)) {
			return (nsIDOMElement) node
					.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
		} else {
			return null;
		}
	}

	public static boolean isElement(nsIDOMNode node) {
		if (nsIDOMNode.ELEMENT_NODE == node.getNodeType()) {
			return true;
		} else {
			return false;
		}
	}
}
