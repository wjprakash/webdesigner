package com.nayaware.webdesigner.mozilla;

import org.mozilla.interfaces.nsICommandManager;
import org.mozilla.interfaces.nsICommandParams;
import org.mozilla.interfaces.nsIDOMWindow;
import org.mozilla.interfaces.nsIInterfaceRequestor;
import org.mozilla.interfaces.nsIServiceManager;
import org.mozilla.xpcom.Mozilla;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class MozillaCommandManager {

	private nsICommandManager commandManager;
	private nsIDOMWindow domWindow;

	public MozillaCommandManager(MozillaBrowser mozillaBrowser) {
		nsIInterfaceRequestor requestor = (nsIInterfaceRequestor) mozillaBrowser
				.getWebBrowser().queryInterface(
						nsIInterfaceRequestor.NS_IINTERFACEREQUESTOR_IID);
		this.commandManager = (nsICommandManager) requestor
				.getInterface(nsICommandManager.NS_ICOMMANDMANAGER_IID);
		this.domWindow = mozillaBrowser.getDOMWindow();
	}

	public void executeCommand(MozillaCommand command, nsICommandParams params) {
		commandManager.doCommand(command.getCommand(), params, domWindow);
	}

	public boolean isCommandSupported(String command) {
		return commandManager.isCommandSupported(command, domWindow);
	}

	public boolean isCommandEnabled(MozillaCommand command) {
		return commandManager.isCommandEnabled(command.getCommand(), domWindow);
	}

	public MozillaCommandParameters getCommandState(MozillaCommand command) {
		nsICommandParams params = newCommandParams();
		commandManager.getCommandState(command.getCommand(), domWindow, params);
		return new MozillaCommandParameters(params);
	}

	public nsICommandParams newCommandParams() {
		nsIServiceManager serviceManager = Mozilla.getInstance()
				.getServiceManager();

		nsICommandParams commandParams = (nsICommandParams) serviceManager
				.getServiceByContractID(
						"@mozilla.org/embedcomp/command-params;1",
						nsICommandParams.NS_ICOMMANDPARAMS_IID);
		return commandParams;
	}
}
