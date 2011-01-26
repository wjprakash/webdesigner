package com.nayaware.webdesigner.mozilla;

import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDragService;
import org.mozilla.interfaces.nsISupportsArray;
import org.mozilla.interfaces.nsISupportsString;
import org.mozilla.interfaces.nsITransferable;

/**
 * Mozilla Drag and drop support
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class MozillaDndSupport {
	
	private static final String DEFAULT_TRANSFER_DATA = ""; //$NON-NLS-1$

	
	private MozillaBrowser mozillaBrowser;

	public MozillaDndSupport(MozillaBrowser mozillaBrowser) {
		this.mozillaBrowser = mozillaBrowser;
	}
	
	public void startDragSession(nsIDOMNode node) {
		nsISupportsArray transArray = (nsISupportsArray) mozillaBrowser
				.getComponentManager().createInstanceByContractID(
						MozillaUtils.NS_SUPPORTSARRAY_CONTRACTID, null,
						nsISupportsArray.NS_ISUPPORTSARRAY_IID);
		transArray.appendElement(createTransferable(node));
		mozillaBrowser.getDragService().invokeDragSession(
				node,
				transArray,
				null,
				nsIDragService.DRAGDROP_ACTION_MOVE
						| nsIDragService.DRAGDROP_ACTION_COPY
						| nsIDragService.DRAGDROP_ACTION_LINK);
	}

	public nsITransferable createTransferable(nsIDOMNode node) {

		nsITransferable iTransferable = (nsITransferable) mozillaBrowser
				.getComponentManager().createInstanceByContractID(
						MozillaUtils.NS_TRANSFERABLE_CONTRACTID, null,
						nsITransferable.NS_ITRANSFERABLE_IID);
		nsISupportsString transferData = (nsISupportsString) mozillaBrowser
				.getComponentManager().createInstanceByContractID(
						MozillaUtils.NS_SUPPORTSSTRING_CONTRACTID, null,
						nsISupportsString.NS_ISUPPORTSSTRING_IID);
		String data = node.getNodeName();
		transferData.setData("Test String");
		// iTransferable.setTransferData(VpeController.MODEL_FLAVOR,
		// transferData, data.length());
		iTransferable
				.setTransferData("text/plain", transferData, data.length()); //$NON-NLS-1$
		iTransferable.setTransferData(
				"text/unicode", transferData, data.length() * 2); //$NON-NLS-1$
		iTransferable.setTransferData(
				"text/html", transferData, data.length() * 2); //$NON-NLS-1$
		iTransferable.setTransferData(
				"text/xml", transferData, data.length() * 2); //$NON-NLS-1$
		iTransferable.setTransferData(
				"text/rtf", transferData, data.length() * 2); //$NON-NLS-1$
		iTransferable.setTransferData(
				"text/enriched", transferData, data.length() * 2); //$NON-NLS-1$
		iTransferable.setTransferData(
				"text/richtext", transferData, data.length() * 2); //$NON-NLS-1$
		iTransferable.setTransferData(
				"text/t140", transferData, data.length() * 2); //$NON-NLS-1$

		return iTransferable;
	}

}
