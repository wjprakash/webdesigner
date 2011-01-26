package com.nayaware.webdesigner.mozilla;

import org.mozilla.interfaces.nsIClipboardDragDropHooks;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDragSession;
import org.mozilla.interfaces.nsIFormatConverter;
import org.mozilla.interfaces.nsISupports;
import org.mozilla.interfaces.nsISupportsString;
import org.mozilla.interfaces.nsITransferable;
import org.mozilla.xpcom.Mozilla;

import sun.misc.BASE64Decoder;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class DnDEditorHook implements nsIClipboardDragDropHooks {
	private static final String TEXT_UNICODE = "text/unicode";

	private final MozillaBrowser browserImpl;
	
	PaletteItemDropHandler paletteItemDropHandler;

	DnDEditorHook(MozillaBrowser browserImpl) {
		this.browserImpl = browserImpl;
		paletteItemDropHandler = new PaletteItemDropHandler(browserImpl);
	}

	public boolean allowDrop(nsIDOMEvent event, nsIDragSession session) {
		if (!session.isDataFlavorSupported(TEXT_UNICODE))
			return false;

		nsIDOMNode target = MozillaUtils
				.qi(event.getTarget(), nsIDOMNode.class);
		if (target == null)
			return false;

		// allow drop only on <A> elements
		// String name = target.getLocalName();
		// if (name==null || !name.equalsIgnoreCase("a")) return false;

		// XXX This seems to cause strange slow down.
		// nsITransferable t =
		// XPCOMUtils.create("@mozilla.org/widget/transferable;1",
		// nsITransferable.class);
		// t.addDataFlavor(TEXT_UNICODE);
		// session.getData(t, 0);
		// String data = getStringData(t);
		// byte[] b = getByteData(data);
		// System.err.println("\nallow drag, bytes.length=" + (b == null ? -1 :
		// b.length)); // TEMP
		// browserImpl.fireDragOver(target, b);

		return true;
	}

	public boolean allowStartDrag(nsIDOMEvent event) {
		return true;
	}

	public boolean onCopyOrDrag(nsIDOMEvent event, nsITransferable trans) {
		return false;
	}

	public boolean onPasteOrDrop(nsIDOMEvent event, nsITransferable trans) {
		try {
			String s = getStringData(trans);

			nsIDOMNode target = MozillaUtils.qi(event.getTarget(),
					nsIDOMNode.class);

			doPasteOrDrop(target, s);
		} finally {
			// cancel default action
			return false;
		}
	}

	public void doPasteOrDrop(nsIDOMNode targetNode, String data) {
		
		System.err.println("dropped into mozilla: " + data);
		
		nsIDOMNode target = MozillaUtils.qi(targetNode, nsIDOMElement.class);
		if (target == null)
			return;
		paletteItemDropHandler.handleDrop(target, data);
		

//		byte[] b = getByteData(data);
//		if (b == null)
//			return;

		// FIXME bussines logic here...
//		System.err.printf("dropped into mozilla: java object (%d bytes)\n",
//				b.length);
		// ObjectInputStream is = new ObjectInputStream(new
		// ByteArrayInputStream(b));
		// o = is.readObject();
//		browserImpl.fireDrop(targetNode, b);

		//
//		nsIDOMElement targetParent = MozillaUtils.qi(target.getParentNode(),
//				nsIDOMElement.class);
//		if (targetParent == null)
//			return; // do default action
//
//		nsIDOMDocument doc = targetParent.getOwnerDocument();
//
//		nsIDOMElement e = doc.createElement(data);
//		// e.setAttribute("href", "about:");
//		// nsIDOMText t = doc.createTextNode(data);
//		// e.appendChild(t);
//		targetParent.appendChild(e);
//		//browserImpl.removeClipboardDragDropHook();
	}

	private static String getStringData(nsITransferable trans) {
		nsIFormatConverter fc = trans.getConverter();
		assert fc == null;
		trans.setConverter(new DnDFormatConverter());

		nsISupports[] aData = new nsISupports[] { null };
		long[] aDataLen = new long[] { 0 };
		trans.getTransferData(TEXT_UNICODE, aData, aDataLen);
		nsISupportsString ss = MozillaUtils.qi(aData[0],
				nsISupportsString.class);
		return ss.getData();
	}

	private static byte[] getByteData(String encodedData) {
		try {
			// try if the content is in base64 encoding
			return new BASE64Decoder().decodeBuffer(encodedData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public nsISupports queryInterface(String uuid) {
		return Mozilla.queryInterface(this, uuid);
	}
}
