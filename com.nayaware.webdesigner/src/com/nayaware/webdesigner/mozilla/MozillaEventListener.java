package com.nayaware.webdesigner.mozilla;

import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMEventListener;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsISelection;
import org.mozilla.interfaces.nsISelectionListener;
import org.mozilla.interfaces.nsISupports;

/**
 * Mozilla DOM Event Listener
 * 
 * https://developer.mozilla.org/en/NsIDOMEventListener
 * http://www.xulplanet.com/references/xpcomref/ifaces/nsISelectionListener.html
 * https://developer.mozilla.org/en/DOM_Client_Object_Cross-Reference/DOM_Events
 * https://developer.mozilla.org/En/XUL/Events
 * https://developer.mozilla.org/En/DragDrop/Drag_and_Drop#Drag_Events
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class MozillaEventListener implements nsIDOMEventListener,
		nsISelectionListener {

	public static final String EVENT_MOUSE_MOVE = "mousemove"; //$NON-NLS-1$
	public static final String EVENT_MOUSE_DOWN = "mousedown"; //$NON-NLS-1$
	public static final String EVENT_MOUSE_UP = "mouseup"; //$NON-NLS-1$
	public static final String EVENT_MOUSE_OVER = "mouseover"; //$NON-NLS-1$
	public static final String EVENT_MOUSE_OUT = "mouseout"; //$NON-NLS-1$
	public static final String EVENT_MOUSE_CLICK = "click"; //$NON-NLS-1$

	public static final String EVENT_KEY_PRESS = "keypress"; //$NON-NLS-1$
	public static final String EVENT_KEY_DOWN = "keydown"; //$NON-NLS-1$
	public static final String EVENT_KEY_UP = "keyup"; //$NON-NLS-1$

	public static final String EVENT_DRAG_START = "dragstart"; //$NON-NLS-1$
	public static final String EVENT_DRAG_ENTER = "dragenter"; //$NON-NLS-1$
	public static final String EVENT_DRAG_OVER = "dragover"; //$NON-NLS-1$
	public static final String EVENT_DRAG_LEAVE = "dragleave"; //$NON-NLS-1$
	public static final String EVENT_DRAG = "drag"; //$NON-NLS-1$
	public static final String EVENT_DROP = "drop"; //$NON-NLS-1$
	public static final String EVENT_DRAG_END = "dropend"; //$NON-NLS-1$
	public static final String EVENT_DRAG_GESTURE = "draggesture"; //$NON-NLS-1$
	public static final String EVENT_DRAG_DROP = "dragdrop"; //$NON-NLS-1$
	public static final String EVENT_DRAG_EXIT = "dragexit"; //$NON-NLS-1$

	public static final String DBLCLICK = "dblclick"; //$NON-NLS-1$
	public static final String CONTEXTMENUEVENTTYPE = "contextmenu"; //$NON-NLS-1$

	public static final int REASON_NONE = 0; //$NON-NLS-1$
	public static final int REASON_DRAG = 1; //$NON-NLS-1$
	public static final int REASON_MOUSEDOWN = 2; //$NON-NLS-1$
	public static final int REASON_MOUSEUP = 4; //$NON-NLS-1$
	public static final int REASON_KEYPRESS = 8; //$NON-NLS-1$
	public static final int REASON_SELECTALL = 16; //$NON-NLS-1$

	private MozillaBrowser mozillaBrowser;
	private MozillaDndSupport mozillaDndSupport;

	public MozillaEventListener(MozillaBrowser mozillaBrowser) {
		this.mozillaBrowser = mozillaBrowser;
		mozillaDndSupport = new MozillaDndSupport(mozillaBrowser);
	}

	public void handleEvent(nsIDOMEvent domEvent) {
		final String eventType = domEvent.getType();
		if (EVENT_DRAG_GESTURE.equals(eventType)) {
			System.err.println("dom event " + domEvent.getType()); //$NON-NLS-1$
			nsIDOMNode node = (nsIDOMNode) domEvent.getTarget().queryInterface(
					nsIDOMNode.NS_IDOMNODE_IID);
			if (node.getNodeName().toLowerCase().equals("html")
					|| node.getNodeName().toLowerCase().equals("body")) {
                return;
			}
			mozillaDndSupport.startDragSession(node);
			domEvent.stopPropagation();
			domEvent.preventDefault();
		} else if (EVENT_DRAG_DROP.equals(eventType)) {
			System.err.println("dom event " + domEvent.getType()); //$NON-NLS-1$
			// calls when drop event occure
			// getEditorDomEventListener().dragDrop(domEvent);
			domEvent.stopPropagation();
			domEvent.preventDefault();
		} else if (EVENT_MOUSE_CLICK.equals(eventType)) {
			nsIDOMNode node = (nsIDOMNode) domEvent.getTarget().queryInterface(
					nsIDOMNode.NS_IDOMNODE_IID);
			if (node.getNodeType() == nsIDOMNode.ELEMENT_NODE) {
				nsIDOMElement element = (nsIDOMElement) node
						.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
				mozillaBrowser.highlightElement(element);
			}
			domEvent.stopPropagation();
			domEvent.preventDefault();
		} else if (EVENT_MOUSE_OVER.equals(eventType)) {
			// calls when drop event occure
			// getEditorDomEventListener().dragDrop(domEvent);
			domEvent.stopPropagation();
			domEvent.preventDefault();
		} else if (EVENT_MOUSE_MOVE.equals(eventType)) {
			// if (mozillaBrowser.getEditor().getSelection().getAnchorNode().
			// getNodeType() != nsIDOMNode.TEXT_NODE) {
			// nsIDOMNode node = (nsIDOMNode) domEvent.getTarget()
			// .queryInterface(nsIDOMNode.NS_IDOMNODE_IID);
			// mozillaBrowser.addClipboardDragDropHook();
			// mozillaDndSupport.startDragSession(node);
			// }else{
			// //mozillaBrowser.removeClipboardDragDropHook();
			// }
			// calls when drop event occure
			// getEditorDomEventListener().dragDrop(domEvent);
			domEvent.stopPropagation();
			domEvent.preventDefault();
		}
	}

	public nsISupports queryInterface(String arg0) {

		return null;
	}

	public void notifySelectionChanged(nsIDOMDocument doucument,
			nsISelection selection, short reason) {

	}

}
