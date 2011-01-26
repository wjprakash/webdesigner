package com.nayaware.webdesigner.spi;

import org.eclipse.swt.widgets.Composite;

import com.nayaware.webdesigner.mozilla.MozillaBrowser;


/**
 * Factory to get the Embedded Browser to be used in the Web designer
 * 
 * @author Winston Prakash
 * @version 1.0
 */

public class EmbeddedBrowserFactory {

	public static EmbeddedBrowser getEmbeddedBrowser(Composite parent, int type) {
		switch (type) {
		case EmbeddedBrowser.MOZILLA:
			return new MozillaBrowser(parent);
		default:
			return null;
		}
	}
}
