package com.nayaware.webdesigner.tests;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.mozilla.interfaces.nsIDOMNode;

import com.nayaware.webdesigner.htmltag.HtmlTag;
import com.nayaware.webdesigner.htmltag.HtmlTagManager;
import com.nayaware.webdesigner.mozilla.HtmlBean;
import com.nayaware.webdesigner.mozilla.MozillaBrowser;


/**
 * @author Winston Prakash
 * @version 1.0
 */
public class MozillaBrowserDumpDomTest {

	static MozillaBrowser mozillaBrowser;

	public static void main(String[] args) {
		try {
			Display display = new Display();
			Shell shell = new Shell(display);

			mozillaBrowser = new MozillaBrowser(shell);
			mozillaBrowser.loadUrl("http://www.sun.com");
			nsIDOMNode bodyElement = mozillaBrowser.getBodyElement();
			HtmlTag htmlTag = HtmlTagManager.getInstance().findTag(
					bodyElement.getNodeName());
			HtmlBean bean = new HtmlBean(bodyElement, htmlTag);
			bean.dump("");

		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
}
