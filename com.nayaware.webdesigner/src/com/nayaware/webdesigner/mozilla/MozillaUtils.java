package com.nayaware.webdesigner.mozilla;

import java.lang.reflect.Field;
import java.util.Locale;

import org.mozilla.interfaces.nsIComponentManager;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIInterfaceRequestor;
import org.mozilla.interfaces.nsIServiceManager;
import org.mozilla.interfaces.nsISupports;
import org.mozilla.xpcom.IXPCOMError;
import org.mozilla.xpcom.Mozilla;
import org.mozilla.xpcom.XPCOMException;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class MozillaUtils {
	
	public static final String NS_DRAGSERVICE_CONTRACTID = "@mozilla.org/widget/dragservice;1"; //$NON-NLS-1$
	public static final String NS_TRANSFERABLE_CONTRACTID = "@mozilla.org/widget/transferable;1"; //$NON-NLS-1$
	public static final String NS_WINDOWWATCHER_CONTRACTID = "@mozilla.org/embedcomp/window-watcher;1"; //$NON-NLS-1$
	public static final String NS_PREFSERVICE_CONTRACTID = "@mozilla.org/preferences-service;1"; //$NON-NLS-1$
	public static final String NS_SUPPORTSSTRING_CONTRACTID = "@mozilla.org/supports-string;1"; //$NON-NLS-1$
	public static final String NS_SUPPORTSARRAY_CONTRACTID = "@mozilla.org/supports-array;1"; //$NON-NLS-1$
	public static final String NS_CLIPBORADHELPER_CONTRACTID = "@mozilla.org/widget/clipboardhelper;1"; //$NON-NLS-1$

	private nsIInterfaceRequestor requestor;
	
	public static nsIDOMElement getElementFromNode(nsIDOMNode domNode) {
		return (nsIDOMElement) domNode
				.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
	}

	private static String guessIID(Class c) {
		try {
			String name = c.getName();
			String baseName = c.getSimpleName();
			final String iidFieldName;
			if (name.startsWith("org.mozilla.interfaces.ns")) { //$NON-NLS-1$
				iidFieldName = String
						.format(
								"NS_%s_IID", baseName.substring(2).toUpperCase(Locale.US)); //bug #19841  //$NON-NLS-1$
			} else {
				iidFieldName = String.format(
						"%s_IID", baseName.toUpperCase(Locale.US)); //bug #19841 //$NON-NLS-1$
			}

			Field f = c.getDeclaredField(iidFieldName);
			String iid = (String) f.get(c);
			return iid;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T extends nsISupports> T qi(nsISupports obj, Class<T> c) {
		try {
			if (obj == null)
				return null;
			String iid = guessIID(c);
			T t = (T) obj.queryInterface(iid);
			return t;
		} catch (XPCOMException e) {
			// do not print an error if
			// obj does not implement the interface
			if (e.errorcode != IXPCOMError.NS_ERROR_NO_INTERFACE) {
				e.printStackTrace();
			}
			return null;
		} catch (Throwable e) {
			//log.error("failed to query-interface an XPCOM object", e); //$NON-NLS-1$
			return null;
		}
	}

	public static <T extends nsISupports> T create(String contractID, Class<T> c) {
		try {
			Mozilla moz = Mozilla.getInstance();
			String iid = guessIID(c);
			nsIComponentManager componentManager = moz.getComponentManager();

			T t = (T) componentManager.createInstanceByContractID(contractID,
					null, iid);

			return t;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T extends nsISupports> T getService(String contractID,
			Class<T> c) {
		try {
			Mozilla moz = Mozilla.getInstance();
			String iid = guessIID(c);
			nsIServiceManager serviceManager = moz.getServiceManager();

			T t = (T) serviceManager.getServiceByContractID(contractID, iid);

			return t;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
}
