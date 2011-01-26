package com.nayaware.webdesigner.mozilla;

import org.mozilla.interfaces.nsIDOMNode;

import com.nayaware.webdesigner.util.HtmlUtils;


/**
 * Handler to handle the Palette items dropped on to the designer
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class PaletteItemDropHandler {

	MozillaBrowser mozillaBrowser;

	public PaletteItemDropHandler(MozillaBrowser mozillaBrowser) {
		this.mozillaBrowser = mozillaBrowser;
	}

	public void handleDrop(nsIDOMNode target, String data) {

		// mozillaBrowser.insertTable(target, 3, 3, "100%", "1");
		
		mozillaBrowser.insertHTML(HtmlUtils.getHtmlForTag(data));
		
//		mozillaBrowser.insertHTML(HtmlUtils.getHtmlForTag(data), target
//				.getParentNode());

	}
}
