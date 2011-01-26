package com.nayaware.webdesigner.mozilla;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.mozilla.interfaces.nsIClipboardHelper;
import org.mozilla.interfaces.nsICommandManager;
import org.mozilla.interfaces.nsICommandParams;
import org.mozilla.interfaces.nsIComponentManager;
import org.mozilla.interfaces.nsIContentFilter;
import org.mozilla.interfaces.nsIDOMCharacterData;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMEventListener;
import org.mozilla.interfaces.nsIDOMEventTarget;
import org.mozilla.interfaces.nsIDOMHTMLFrameElement;
import org.mozilla.interfaces.nsIDOMHTMLIFrameElement;
import org.mozilla.interfaces.nsIDOMHTMLTableCellElement;
import org.mozilla.interfaces.nsIDOMHTMLTableElement;
import org.mozilla.interfaces.nsIDOMHTMLTableRowElement;
import org.mozilla.interfaces.nsIDOMNSDocument;
import org.mozilla.interfaces.nsIDOMNSHTMLDocument;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.mozilla.interfaces.nsIDOMRange;
import org.mozilla.interfaces.nsIDOMSerializer;
import org.mozilla.interfaces.nsIDOMWindow;
import org.mozilla.interfaces.nsIDOMWindow2;
import org.mozilla.interfaces.nsIDocShell;
import org.mozilla.interfaces.nsIDocumentStateListener;
import org.mozilla.interfaces.nsIDragService;
import org.mozilla.interfaces.nsIDragSession;
import org.mozilla.interfaces.nsIEditActionListener;
import org.mozilla.interfaces.nsIEditingSession;
import org.mozilla.interfaces.nsIEditor;
import org.mozilla.interfaces.nsIEditorStyleSheets;
import org.mozilla.interfaces.nsIHTMLAbsPosEditor;
import org.mozilla.interfaces.nsIHTMLEditor;
import org.mozilla.interfaces.nsIHTMLInlineTableEditor;
import org.mozilla.interfaces.nsIHTMLObjectResizeListener;
import org.mozilla.interfaces.nsIHTMLObjectResizer;
import org.mozilla.interfaces.nsIInterfaceRequestor;
import org.mozilla.interfaces.nsIRequest;
import org.mozilla.interfaces.nsISelection;
import org.mozilla.interfaces.nsISelectionController;
import org.mozilla.interfaces.nsISelectionDisplay;
import org.mozilla.interfaces.nsIServiceManager;
import org.mozilla.interfaces.nsISupports;
import org.mozilla.interfaces.nsISupportsString;
import org.mozilla.interfaces.nsITableEditor;
import org.mozilla.interfaces.nsITransferable;
import org.mozilla.interfaces.nsIURI;
import org.mozilla.interfaces.nsIURL;
import org.mozilla.interfaces.nsIWebBrowser;
import org.mozilla.interfaces.nsIWebBrowserFocus;
import org.mozilla.interfaces.nsIWebNavigation;
import org.mozilla.interfaces.nsIWebProgress;
import org.mozilla.interfaces.nsIWebProgressListener;
import org.mozilla.xpcom.Mozilla;
import org.osgi.framework.Bundle;

import com.nayaware.webdesigner.htmltag.HtmlTag;
import com.nayaware.webdesigner.htmltag.HtmlTagManager;
import com.nayaware.webdesigner.spi.Command;
import com.nayaware.webdesigner.spi.EmbeddedBrowser;
import com.nayaware.webdesigner.spi.IModificationListener;
import com.nayaware.webdesigner.spi.ISelectionListener;
import com.nayaware.webdesigner.util.ErrorManager;

import sun.misc.BASE64Decoder;


/**
 * Embedded browser implementation based on Mozilla interfaces
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class MozillaBrowser implements EmbeddedBrowser {
	private Browser browser;

	private nsIEditor editor;
	private nsIHTMLEditor htmlEditor;
	private nsIHTMLObjectResizer htmlObjectResizer;
	private boolean htmlEditingEnabled = false;
	private nsIHTMLInlineTableEditor htmlInlineTableEditor;

	private Mozilla mozilla;

	private final DnDEditorHook dndHook = new DnDEditorHook(this);

	private nsIDOMElement currentSelectedElement;
	private nsIDOMElement currentHighlightedElement;

	List<ISelectionListener> selectionListeners = new CopyOnWriteArrayList<ISelectionListener>();
	List<IModificationListener> modificationListeners = new CopyOnWriteArrayList<IModificationListener>();

	MozillaCommandManager commandManager;

	private String selectionHighlight = "outline-color: red; outline-style: dotted; outline-width: thin;";
	private nsIWebProgress webProgress;

	private static String XULRUNNER_BUNDLE;
	private static String XULRUNNER_ENTRY = "/xulrunner";

	// (@see org.eclipse.swt.browser.Mozilla)
	static final String XULRUNNER_INITIALIZED = "org.eclipse.swt.browser.XULRunnerInitialized"; //$NON-NLS-1$
	static final String XULRUNNER_PATH = "org.eclipse.swt.browser.XULRunnerPath"; //$NON-NLS-1$

	// private String hoverHighlight =
	// "outline-color: red; outline-style: dotted; outline-width: thin;";
	
	static {
		XULRUNNER_BUNDLE = (new StringBuffer("org.mozilla.xulrunner")) // $NON-NLS-1$
			.append(".").append(Platform.getWS()) // $NON-NLS-1$
			.append(".").append(Platform.getOS()) // $NON-NLS-1$
			.toString();
		
		if (!Platform.OS_MACOSX.equals(Platform.getOS())) {
			XULRUNNER_BUNDLE = (new StringBuffer(XULRUNNER_BUNDLE))
				.append(".").append(Platform.getOSArch())
				.toString();
		}
	}


	public MozillaBrowser(Composite parent) {
		try {
			mozilla = Mozilla.getInstance();
			
			//Use this when we bundle XulRunner
//			try {
//				initializeXulRunner();
//			} catch (XulRunnerException e) {
//				e.printStackTrace();
//			}
			browser = new Browser(parent, SWT.MOZILLA);
			browser.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
					true, true, 2, 1));
			getWebBrowser();
			enableJavaScript(true);
			nsIServiceManager serviceManager = mozilla.getServiceManager();
			webProgress = (nsIWebProgress) serviceManager
					.getServiceByContractID("@mozilla.org/docloaderservice;1",
							nsIWebProgress.NS_IWEBPROGRESS_IID);
			webProgress.addProgressListener(webProgressListener,
					nsIWebProgress.NOTIFY_ALL);
			commandManager = new MozillaCommandManager(this);
		} catch (SWTError e) {
			System.out.println("Could not instantiate Browser: "
					+ e.getMessage());
			return;
		}
	}

	private void initializeXulRunner() throws XulRunnerException {
		String xulRunnerPath = getXulRunnerPath(); 
		Boolean isXulRunnerInitialized = "true".equals(System
				.getProperty(XULRUNNER_INITIALIZED)); // $NON-NLS-1$
		if (!isXulRunnerInitialized) {
			File file = new File(xulRunnerPath);
			mozilla.initialize(file);
			mozilla.initEmbedding(file, file, new AppFileLocProvider(file), null);
			System.setProperty(XULRUNNER_INITIALIZED, "true"); // $NON-NLS-1$
		}

	}
	
	public static String getXulRunnerBundle() {
		return XULRUNNER_BUNDLE;
	}
	
	private String getXulRunnerPath() throws XulRunnerException {
		String xulRunnerPath = System.getProperty(XULRUNNER_PATH);
		if (xulRunnerPath == null) {
			
			Bundle fragment = Platform.getBundle(getXulRunnerBundle());
			if (fragment == null) {
				throw new XulRunnerException("Bundle " + getXulRunnerBundle() + " is not found.");
			}
			
			URL url = fragment.getEntry(XULRUNNER_ENTRY);
			if (url == null) {
				throw new XulRunnerException("Bundle " + getXulRunnerBundle() + " doesn't contain /xulrunner");
			}
			
			
			try {
				URL url1 = FileLocator.resolve(url);
				File file = new File(FileLocator.toFileURL(url1).getFile());
				xulRunnerPath = file.getAbsolutePath();
				System.setProperty(XULRUNNER_PATH, xulRunnerPath);
			} catch (IOException ioe) {
				throw new XulRunnerException(ioe);
			}
		}
		
		return xulRunnerPath;
	}


	public void addSelectionListener(ISelectionListener selectionListener) {
		selectionListeners.add(selectionListener);
	}

	public void addModificationListener(
			IModificationListener modificationListener) {
		modificationListeners.add(modificationListener);
	}

	private nsIWebProgressListener webProgressListener = new nsIWebProgressListener() {

		public void onLocationChange(nsIWebProgress arg0, nsIRequest request,
				nsIURI uri) {
			// System.out.println("nsIWebProgressListener:onLocationChange" +
			// uri.toString());
		}

		public void onProgressChange(nsIWebProgress arg0, nsIRequest arg1,
				int arg2, int arg3, int arg4, int arg5) {
		}

		public void onSecurityChange(nsIWebProgress arg0, nsIRequest arg1,
				long arg2) {
		}

		public void onStateChange(nsIWebProgress aWebProgress,
				nsIRequest aRequest, long aStateFlags, long aStatus) {
			// System.out.println("nsIWebProgressListener:onStateChange");
			// System.out.println("State Flags:" + aStateFlags);
			// System.out.println("Status:" + aStatus);

			if ((aStateFlags & nsIWebProgressListener.STATE_IS_NETWORK) != 0
					&& (aStateFlags & nsIWebProgressListener.STATE_START) != 0) {
				// started();
			}

			if ((aStateFlags & nsIWebProgressListener.STATE_IS_NETWORK) != 0
					&& (aStateFlags & nsIWebProgressListener.STATE_STOP) != 0) {
				System.out.println("Initializing Editing");
				enableEditing(true);
				enableJavaScript(false);
				addKeyListener();
				addEventListener();
				addClipboardDragDropHook();

				// enableEditorInlineTableEditing(false);
				// enableEditorInlineObjectResizing(false);

				// webProgress.removeProgressListener(webProgressListener);
			}
		}

		public void onStatusChange(nsIWebProgress arg0, nsIRequest arg1,
				long status, String statusMessage) {
			// System.out.println("nsIWebProgressListener:onStateChange2");
			// System.out.println("Status:" + status);
			// System.out.println("Status Message:" + statusMessage);
		}

		public nsISupports queryInterface(String arg0) {
			return null;
		}
	};

	public void highlightElement(nsIDOMElement element) {
		if (element == currentSelectedElement) {
			return;
		}
		if (currentSelectedElement != null) {
			String style = currentSelectedElement.getAttribute("style");
			style = style.replaceAll(selectionHighlight, "");
			if (style.length() > 0) {
				currentSelectedElement.setAttribute("style", style);
			} else {
				currentSelectedElement.removeAttribute("style");
			}
			showEditorResizers(currentSelectedElement, false);
		}
		String style = element.getAttribute("style");
		if ((style != null) && (style.length() > 0)) {
			style = style + selectionHighlight;
		} else {
			style = selectionHighlight;
		}
		element.setAttribute("style", style);
		currentSelectedElement = element;
		// if (WebDesignerUtils.isResizableElement(element)) {
		// showEditorResizers(currentSelectedElement, true);
		// }
	}

	private void addKeyListener() {
		browser.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent arg0) {
				fireDocumentModified();
			}

			public void keyReleased(KeyEvent arg0) {
			}
		});
	}

	private void fireDocumentModified() {
		for (IModificationListener modificationListener : modificationListeners) {
			modificationListener.modified();
		}
	}

	private void addEventListener() {
		nsIWebBrowser webBrowser = this.getWebBrowser();
		nsIDOMWindow2 win = MozillaUtils.qi(webBrowser.getContentDOMWindow(),
				nsIDOMWindow2.class);
		nsIDOMEventTarget et = win.getWindowRoot();
		setEvenListeners(et);
		// MozDnDListener dndl = new MozDnDListener();
		//				et.addEventListener("dragdrop", dndl, true); //$NON-NLS-1$
	}

	public void loadUrl(String url) {
		browser.setUrl(url);
	}

	public nsIComponentManager getComponentManager() {
		return mozilla.getComponentManager();
	}

	public nsIWebBrowser getWebBrowser() {
		return (nsIWebBrowser) browser.getWebBrowser();
	}

	public nsIDOMWindow getDOMWindow() {
		return getWebBrowser().getContentDOMWindow();
	}

	public nsIDOMDocument getDOMDocument() {
		return getDOMWindow().getDocument();
	}

	private nsIDOMElement getElementFromPoint(final int x, final int y) {
		nsIDOMDocument currDoc = getDOMDocument();
		nsIDOMElement currEl = null;
		while (currDoc != null) {
			nsIDOMNSDocument nsdoc = MozillaUtils.qi(currDoc,
					nsIDOMNSDocument.class);

			nsIDOMElement el = nsdoc.elementFromPoint(x, y);
			if (el == null)
				break; // not from nsdoc
			currEl = el;

			nsIDOMHTMLFrameElement fel = MozillaUtils.qi(el,
					nsIDOMHTMLFrameElement.class);
			if (fel != null) {
				currDoc = fel.getContentDocument();
				continue;
			}
			nsIDOMHTMLIFrameElement iel = MozillaUtils.qi(el,
					nsIDOMHTMLIFrameElement.class);
			if (iel != null) {
				currDoc = iel.getContentDocument();
				continue;
			}
			// not an (i)frame element
			currDoc = null;
		}
		return currEl;
	}

	public static final String NS_EDITINGSESSION_COMPONENT_ID = "@mozilla.org/editor/editingsession;1"; //$NON-NLS-1$

	public nsIEditingSession getEditingSession() {
		return (nsIEditingSession) getComponentManager()
				.createInstanceByContractID(NS_EDITINGSESSION_COMPONENT_ID,
						null, nsIEditingSession.NS_IEDITINGSESSION_IID);
	}

	public void makeEditable(boolean editable) {
		nsIDOMDocument contentDoc = getDOMWindow().getDocument();
		nsIDOMNSHTMLDocument nsDOMDoc = (nsIDOMNSHTMLDocument) contentDoc
				.queryInterface(nsIDOMNSHTMLDocument.NS_IDOMNSHTMLDOCUMENT_IID);
		nsDOMDoc.setDesignMode("on");
	}

	public nsIEditor getEditor() {
		return getEditingSession().getEditorForWindow(getDOMWindow());
	}

	public nsIHTMLEditor getHTMLEditor() {
		return (nsIHTMLEditor) getEditor().queryInterface(
				nsIHTMLEditor.NS_IHTMLEDITOR_IID);
	}

	public boolean setDesignMode(String value) {
		makeEditable(true);
		return true;
	}

	public void enableEditing(final boolean enableJavaScript) {

		makeEditable(true);

		// Get the generic Editor
		editor = getEditor();

		// Get the HTML Editor Object querying the Generic HTML Editor
		htmlEditor = getHTMLEditor();

		editor.addDocumentStateListener(new nsIDocumentStateListener() {

			public void notifyDocumentCreated() {
				System.out
						.println("nsIDocumentStateListener: Document Created");
			}

			public void notifyDocumentStateChanged(boolean arg0) {
				System.out
						.println("nsIDocumentStateListener: Document State Changed");
				for (IModificationListener modificationListener : modificationListeners) {
					modificationListener.modified();
				}
			}

			public void notifyDocumentWillBeDestroyed() {
				System.out
						.println("nsIDocumentStateListener: Document will be destroyed");
			}

			public nsISupports queryInterface(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}

		});

		editor.addEditActionListener(new nsIEditActionListener() {

			private nsIDOMNode splittedNode;

			public void willCreateNode(String tag, nsIDOMNode parent,
					int position) {
				System.err.println("\nwillCreateNode"); // TEMP
				System.err.println("tag=" + tag); // TEMP
				System.err.println("parent=" + formatNode(parent)); // TEMP
				System.err.println("position=" + position); // TEMP
			}

			public void didCreateNode(String tag, nsIDOMNode createdNode,
					nsIDOMNode parentNode, int position, long result) {
				System.err.println("\ndidCreateNode"); // TEMP
				System.err.println("tag=" + tag); // TEMP
				System.err.println("createdNode=" + formatNode(createdNode)); // TEMP
				System.err.println("parentNode=" + formatNode(parentNode)); // TEMP
				System.err.println("position=" + position); // TEMP
				System.err.println("result=" + result); // TEMP
				// fireNodeCreated(tag, createdNode, parentNode, position);
			}

			public void willInsertNode(nsIDOMNode node, nsIDOMNode parent,
					int position) {
				System.err.println("\nwillInsertNode"); // TEMP
				System.err.println("node=" + formatNode(node)); // TEMP
				System.err.println("parent=" + formatNode(parent)); // TEMP
				System.err.println("position=" + position); // TEMP
			}

			public void didInsertNode(nsIDOMNode insertedNode,
					nsIDOMNode parentNode, int position, long result) {
				System.err.println("\ndidInsertNode"); // TEMP
				System.err.println("insertedNode=" + formatNode(insertedNode)); // TEMP
				System.err.println("parentNode=" + formatNode(parentNode)); // TEMP
				// fireNodeInserted(insertedNode, parentNode, position);
			}

			public void willDeleteNode(nsIDOMNode child) {
				System.err.println("\nwillDeleteNode"); // TEMP
				System.err.println("child=" + formatNode(child)); // TEMP
			}

			public void didDeleteNode(nsIDOMNode deletedNode, long result) {
				System.err.println("\ndidDeleteNode"); // TEMP
				System.err.println("deletedNode=" + formatNode(deletedNode)); // TEMP
				System.err.println("result=" + result); // TEMP
				// fireNodeDeleted(deletedNode);
			}

			public void willSplitNode(nsIDOMNode node, int offset) {
				System.err.println("\nwillSplitNode"); // TEMP
				System.err.println("node=" + formatNode(node)); // TEMP
				System.err.println("offset=" + offset); // TEMP
				splittedNode = node;
			}

			public void didSplitNode(nsIDOMNode rightNode, int offset,
					nsIDOMNode leftNode, long result) {
				System.err.println("\ndidSplitNode"); // TEMP
				System.err.println("rightNode=" + formatNode(rightNode)); // TEMP
				System.err.println("leftNode=" + formatNode(leftNode)); // TEMP
				System.err.println("result=" + result); // TEMP
				// fireNodeSplitted(splittedNode, rightNode, offset, leftNode);
			}

			public void willJoinNodes(nsIDOMNode leftNode,
					nsIDOMNode rightNode, nsIDOMNode parent) {
				System.err.println("\nwillJoinNodes"); // TEMP
				System.err.println("leftNode=" + formatNode(leftNode)); // TEMP
				System.err.println("rightNode=" + formatNode(rightNode)); // TEMP
				System.err.println("parent=" + parent); // TEMP
			}

			public void didJoinNodes(nsIDOMNode leftNode, nsIDOMNode rightNode,
					nsIDOMNode parent, long result) {
				System.err.println("\ndidJoinNodes"); // TEMP
				System.err.println("leftNode=" + formatNode(leftNode)); // TEMP
				System.err.println("rightNode=" + formatNode(rightNode)); // TEMP
				System.err.println("parent=" + formatNode(parent)); // TEMP
				System.err.println("result=" + result); // TEMP
				// fireNodesJoined(leftNode, rightNode, parent);
			}

			public void willInsertText(nsIDOMCharacterData textNode,
					int offset, String insertedText) {
				System.err.println("\nwillInsertText"); // TEMP
				System.err.println("textNode=" + formatNode(textNode)); // TEMP
				System.err.println("offset=" + offset); // TEMP
				System.err.println("insertedText=" + insertedText); // TEMP
			}

			public void didInsertText(nsIDOMCharacterData textNode, int offset,
					String insertedText, long result) {
				System.err.println("\ndidInsertText"); // TEMP
				System.err.println("textNode=" + formatNode(textNode)); // TEMP
				System.err.println("offset=" + offset); // TEMP
				System.err.println("insertedText=" + insertedText); // TEMP
				System.err.println("result=" + result); // TEMP
				// fireTextInserted(textNode, offset, insertedText);
			}

			public void willDeleteText(nsIDOMCharacterData textNode,
					int offset, int length) {
				System.err.println("\nwillDeleteText"); // TEMP
				System.err.println("textNode=" + formatNode(textNode)); // TEMP
				System.err.println("offset=" + offset); // TEMP
				System.err.println("length=" + length); // TEMP
			}

			public void didDeleteText(nsIDOMCharacterData textNode, int offset,
					int length, long result) {
				System.err.println("\ndidDeleteText"); // TEMP
				System.err.println("textNode=" + formatNode(textNode)); // TEMP
				System.err.println("offset=" + offset); // TEMP
				System.err.println("length=" + length); // TEMP
				System.err.println("result=" + result); // TEMP
				// fireTextDeleted(textNode, offset, length);
			}

			public void willDeleteSelection(nsISelection selection) {
				System.err.println("\nwillDeleteSelection"); // TEMP
				System.err.println("selection=" + selection); // TEMP
				for (int i = 0; i < selection.getRangeCount(); i++) {
					nsIDOMRange range = selection.getRangeAt(i);
					System.out.println("Start Container - "
							+ range.getStartContainer().getNodeName() + " "
							+ range.getStartContainer().getNodeValue());
					System.out.println("Start Offset- "
							+ range.getStartOffset());
					System.out.println("End Container - "
							+ range.getStartContainer().getNodeName() + " "
							+ range.getStartContainer().getNodeValue());
					System.out.println("End Offset- " + range.getEndOffset());
				}
				// fireSelectionDeleted(selection);
			}

			public void didDeleteSelection(nsISelection selection) {
				System.err.println("\ndidDeleteSelection"); // TEMP
				System.err.println("selection=" + selection); // TEMP
			}

			public nsISupports queryInterface(String uuid) {
				return Mozilla.queryInterface(this, uuid);
			}

		});

		htmlEditor.addInsertionListener(new nsIContentFilter() {

			public void notifyOfInsertion(String arg0, nsIURL arg1,
					nsIDOMDocument arg2, boolean arg3, nsIDOMNode[] arg4,
					nsIDOMNode[] arg5, int[] arg6, nsIDOMNode[] arg7,
					int[] arg8, nsIDOMNode[] arg9, int[] arg10, boolean[] arg11) {
				System.out.println("Insertion Occured. Adding DND Hook");
			}

			public nsISupports queryInterface(String uuid) {
				return Mozilla.queryInterface(this, uuid);
			}

		});

		htmlObjectResizer = MozillaUtils.qi(htmlEditor,
				nsIHTMLObjectResizer.class);
		htmlObjectResizer
				.addObjectResizeEventListener(new nsIHTMLObjectResizeListener() {

					public void onStartResizing(nsIDOMElement arg0) {
						System.out.println("Starting Resizing");
					}

					public void onEndResizing(nsIDOMElement resizedElement,
							int oldWidth, int oldHeight, int newWidth,
							int newHeight) {
						fireElementResized(resizedElement, oldWidth, oldHeight,
								newWidth, newHeight);
						System.out.println("Ending Resizing "
								+ resizedElement.getLocalName() + " "
								+ oldWidth + " " + oldHeight + " " + newWidth
								+ " " + newHeight);
					}

					public nsISupports queryInterface(String uuid) {
						return Mozilla.queryInterface(this, uuid);
					}
				});

		// enableJavaScript(enableJavaScript);

		// Get the HTML Table Editor
		htmlInlineTableEditor = MozillaUtils.qi(htmlEditor,
				nsIHTMLInlineTableEditor.class);

		htmlEditor.ignoreSpuriousDragEvent(true);
		htmlEditor.setReturnInParagraphCreatesNewParagraph(true);

		htmlEditor.ignoreSpuriousDragEvent(true);
		htmlEditor.setReturnInParagraphCreatesNewParagraph(true);

		final String kContentEditableStyleSheet = "resource://gre/res/contenteditable.css";
		nsIEditorStyleSheets editorStyles = MozillaUtils.qi(editor,
				nsIEditorStyleSheets.class);
		editorStyles.addOverrideStyleSheet(kContentEditableStyleSheet);

		htmlEditingEnabled = true;
	}

	public void addClipboardDragDropHook() {
		nsICommandManager cm = (nsICommandManager) getRequestor().getInterface(
				nsICommandManager.NS_ICOMMANDMANAGER_IID);
		nsICommandParams p = MozillaUtils.create(
				"@mozilla.org/embedcomp/command-params;1",
				nsICommandParams.class);
		p.setISupportsValue("addhook", dndHook);
		cm.doCommand("cmd_clipboardDragDropHook", p, getDOMWindow());

		// TODO: Why this doesn't work
		// nsICommandParams params = commandManager.newCommandParams();
		// params.setISupportsValue("addhook", dndHook);
		// MozillaCommand dragDropHookCommand = MozillaCommand
		// .findCommand(Command.CLIPBOARD_DRAG_DROP_HOOK);
		// commandManager.executeCommand(dragDropHookCommand, params);
	}

	private nsIInterfaceRequestor getRequestor() {
		return (nsIInterfaceRequestor) getWebBrowser().queryInterface(
				nsIInterfaceRequestor.NS_IINTERFACEREQUESTOR_IID);
	}

	public void removeClipboardDragDropHook() {
		nsICommandManager cm = (nsICommandManager) getRequestor().getInterface(
				nsICommandManager.NS_ICOMMANDMANAGER_IID);
		nsICommandParams p = MozillaUtils.create(
				"@mozilla.org/embedcomp/command-params;1",
				nsICommandParams.class);
		p.setISupportsValue("removehook", dndHook);
		cm.doCommand("cmd_clipboardDragDropHook", p, getDOMWindow());

		// TODO: Why this doesn't work
		// nsICommandParams params = commandManager.newCommandParams();
		// params.setISupportsValue("removehook", dndHook);
		// MozillaCommand dragDropHookCommand = MozillaCommand
		// .findCommand(Command.CLIPBOARD_DRAG_DROP_HOOK);
		// commandManager.executeCommand(dragDropHookCommand, params);
	}

	void fireDragOver(nsIDOMNode targetNode, byte[] data) {
		// Browser.DndEvent evt = createDndEvent(targetNode, data);
		// final Browser.DndListener[] listeners = dndListeners.toArray(new
		// Browser.DndListener[dndListeners.size()]);
		// for (Browser.DndListener listener : listeners) {
		// listener.dragOver(evt);
		// }
	}

	void fireDrop(nsIDOMNode targetNode, byte[] data) {
		// Browser.DndEvent evt = createDndEvent(targetNode, data);
		// final Browser.DndListener[] listeners = dndListeners.toArray(new
		// Browser.DndListener[dndListeners.size()]);
		// for (Browser.DndListener listener : listeners) {
		// listener.drop(evt);
		// }
	}

	private static String formatNode(nsIDOMNode nsNode) {
		return nsNode == null ? null : "[" + nsNode.getNodeName() + "]"
				+ nsNode.getNodeValue();
	}

	private void fireElementResized(nsIDOMElement resizedElement,
			final int oldWidth, final int oldHeight, final int newWidth,
			final int newHeight) {

	}

	public void enableJavaScript(final boolean enable) {
		nsIWebBrowser webBrowser = (nsIWebBrowser) browser.getWebBrowser();
		nsIInterfaceRequestor ir = MozillaUtils.qi(webBrowser,
				nsIInterfaceRequestor.class);
		nsIDocShell docShell = (nsIDocShell) ir
				.getInterface(nsIDocShell.NS_IDOCSHELL_IID);
		docShell.setAllowJavascript(enable);
	}

	public void executeJavaScript(final String script) {
		nsIWebBrowser webBrowser = (nsIWebBrowser) browser.getWebBrowser();
		nsIWebNavigation nav = MozillaUtils.qi(webBrowser,
				nsIWebNavigation.class);
		nav.loadURI("javascript:" + script, nsIWebNavigation.LOAD_FLAGS_NONE,
				null, null, null);
	}

	/**
	 * Show the Html Editor Resizers for the particular element
	 * 
	 * @param element
	 */
	public void showEditorResizers(final nsIDOMElement element,
			final boolean show) {
		if (htmlEditingEnabled) {

			if (show) {
				htmlObjectResizer.showResizers(element);
			} else {
				htmlObjectResizer.hideResizers();
			}

		}
	}

	public static Object decodeToObject(String encodedObject) {
		// Decode and gunzip if necessary

		try {
			byte[] objBytes = new BASE64Decoder().decodeBuffer(encodedObject);

			java.io.ByteArrayInputStream bais = null;
			java.io.ObjectInputStream ois = null;
			Object obj = null;

			bais = new java.io.ByteArrayInputStream(objBytes);
			ois = new java.io.ObjectInputStream(bais);

			obj = ois.readObject();

		} catch (IOException exc) {
			ErrorManager.showException(exc);
		} catch (ClassNotFoundException exc) {
			ErrorManager.showException(exc);
		}

		return null;
	} // end decodeObject

	/**
	 * Show the Html Editor Resizers for the particular element
	 * 
	 * @param element
	 */
	public void showEditorTableEditingUI(final nsIDOMElement tableElement,
			final boolean show) {
		if (htmlEditingEnabled) {

			if (show) {
				htmlInlineTableEditor.showInlineTableEditingUI(tableElement);
			} else {
				htmlInlineTableEditor.hideInlineTableEditingUI();
			}

		}
	}

	/**
	 * Enable or disable inline Object resizing UI However,
	 * showResizers(element) can be called explicitly even if the inline object
	 * resizing is disabled
	 * 
	 * @param enable
	 */
	public void enableEditorInlineObjectResizing(final boolean enable) {
		if (htmlEditingEnabled) {
			htmlObjectResizer.setObjectResizingEnabled(enable);
			if (enable) {
				htmlObjectResizer.refreshResizers();
			} else {
				htmlObjectResizer.hideResizers();
			}
		}
	}

	/**
	 * Enable or disable inline table editing UI However,
	 * showInlineTableEditingUI(tableElement) can be called explicitly even if
	 * the inline table editing is disabled
	 * 
	 * @param enable
	 */
	public void enableEditorInlineTableEditing(final boolean enable) {
		if (htmlEditingEnabled) {
			htmlInlineTableEditor.setInlineTableEditingEnabled(enable);
			if (enable) {
				htmlInlineTableEditor.refreshInlineTableEditingUI();
			} else {
				htmlInlineTableEditor.hideInlineTableEditingUI();
			}
		}
	}

	public void enabledInlineEditing(boolean enable) {
		getSelectionController().setCaretEnabled(enable);
	}

	public nsISelectionController getSelectionController() {
		nsIInterfaceRequestor requestor = (nsIInterfaceRequestor) getWebBrowser()
				.queryInterface(
						nsIInterfaceRequestor.NS_IINTERFACEREQUESTOR_IID);
		nsISelectionDisplay display = (nsISelectionDisplay) requestor
				.getInterface(nsISelectionDisplay.NS_ISELECTIONDISPLAY_IID);
		return (nsISelectionController) display
				.queryInterface(nsISelectionController.NS_ISELECTIONCONTROLLER_IID);
	}

	public boolean isCommandEnabled(String commandName) {
		MozillaCommand command = MozillaCommand.findCommand(commandName);
		return commandManager.isCommandEnabled(command);
	}

	public int getCommandState(String commandName) {
		MozillaCommand command = MozillaCommand.findCommand(commandName);
		MozillaCommandParameters commandStates = commandManager
				.getCommandState(command);
		if (commandStates.hasKey(MozillaCommand.STATE_ENABLED)) {
			if (commandStates.getBooleanValue(MozillaCommand.STATE_ENABLED)) {
				return Command.STATE_ENABLED;
			} else {
				return Command.STATE_DISABLED;
			}
		} else if (commandStates.hasKey(MozillaCommand.STATE_MIXED)) {
			if (commandStates.getBooleanValue(MozillaCommand.STATE_MIXED)) {
				return Command.STATE_ENABLED;
			} else {
				return Command.STATE_DISABLED;
			}
		} else if (commandStates.hasKey(MozillaCommand.STATE_ALL)) {
			if (commandStates.getBooleanValue(MozillaCommand.STATE_ALL)) {
				return Command.STATE_ENABLED;
			} else {
				return Command.STATE_DISABLED;
			}
		}
		return Command.STATE_DISABLED;
	}

	public String getHtml() {
		nsIDOMSerializer s = (nsIDOMSerializer) Mozilla.getInstance()
				.getComponentManager().createInstanceByContractID(
						"@mozilla.org/xmlextras/xmlserializer;1", null,
						nsIDOMSerializer.NS_IDOMSERIALIZER_IID);
		String html = s.serializeToString(getDOMDocument()
				.getElementsByTagName("html").item(0));

		// Pattern pattern = Pattern
		// .compile(
		// "</?\\w+((\\s+\\w+(\\s*=\\s*(?:\".*?\"|'.*?'|[^'\">\\s]+))?)+\\s*|\\s*)/?>"
		// );
		// Matcher matcher = pattern.matcher(html);
		//
		// while (matcher.find()) {
		// String group = matcher.group();
		// html = html.replaceAll(group, group.toLowerCase());
		// }
		//
		// pattern = Pattern.compile("( _moz_[^=]+)=\"([^\"]*)\"");
		// matcher = pattern.matcher(html);
		//
		// while (matcher.find()) {
		// String group = matcher.group();
		// html = html.replaceAll(group, "");
		// }
		//
		// html = html.replaceAll(" type=\"_moz\"", "");

		return html;
	}

	public void enableVisualAid(boolean enable) {
		nsIDOMDocument doc = getDOMDocument();
		nsIDOMNodeList tableList = doc.getElementsByTagName("table");

		for (int t = 0; t < tableList.getLength(); t++) {
			nsIDOMHTMLTableElement table = (nsIDOMHTMLTableElement) tableList
					.item(t).queryInterface(
							nsIDOMHTMLTableElement.NS_IDOMHTMLTABLEELEMENT_IID);
			if (table.getBorder().equals("0") || table.getBorder().equals("")) {
				if (enable)
					table.setClassName("visual_aid");
				else
					table.removeAttribute("class");
			}
		}
	}

	public void executeCommand(String commandName) {
		MozillaCommand command = MozillaCommand.findCommand(commandName);
		if (command != null) {
			nsICommandParams params = null;
			if (commandManager.isCommandEnabled(command)) {
				if (command.hasParameters()) {
					Object attr = command.getAttribute();
					params = commandManager.newCommandParams();

					if (attr != null) {
						params.setStringValue(MozillaCommand.STATE_ATTRIBUTE,
								(String) attr);
					}
				}

				commandManager.executeCommand(command, params);
				// changed = true;
			}
			return;
		}
		// Check if it is a table command
		MozillaTableCommand tableCommand = MozillaTableCommand
				.findCommand(commandName);
		if (tableCommand != null) {
			tableCommand.execute(getTableEditor());
		}
	}

	public nsITableEditor getTableEditor() {
		return (nsITableEditor) htmlEditor
				.queryInterface(nsITableEditor.NS_ITABLEEDITOR_IID);
	}

	// public nsICommandManager getCommandManager() {
	// nsIInterfaceRequestor requestor = (nsIInterfaceRequestor) getWebBrowser()
	// .queryInterface(
	// nsIInterfaceRequestor.NS_IINTERFACEREQUESTOR_IID);
	// return (nsICommandManager) requestor
	// .getInterface(nsICommandManager.NS_ICOMMANDMANAGER_IID);
	// }

	private static class MozDnDListener implements nsIDOMEventListener {
		public void handleEvent(nsIDOMEvent ev) {
			System.err.println("dom event " + ev.getType()); //$NON-NLS-1$
			nsIDragService ds = MozillaUtils.getService(
					"@mozilla.org/widget/dragservice;1", nsIDragService.class); //$NON-NLS-1$
			nsIDragSession dragSession = ds.getCurrentSession();

			String f = "text/unicode"; //$NON-NLS-1$
			if (dragSession.isDataFlavorSupported(f)) {
				nsITransferable t = MozillaUtils
						.create(
								"@mozilla.org/widget/transferable;1", nsITransferable.class); //$NON-NLS-1$
				t.addDataFlavor(f);

				dragSession.getData(t, 0);
				nsISupports[] aData = new nsISupports[] { null };
				long[] aDataLen = new long[] { 0 };
				t.getTransferData(f, aData, aDataLen);
				nsISupportsString ss = MozillaUtils.qi(aData[0],
						nsISupportsString.class);

				String s = ss.getData();
				Object o;
				try {
					// try if the content is in base64 encoding
					o = decodeToObject(s);
				} catch (Exception e) {
					o = s;
				}
				final Object data = o;

				final nsIDOMEventTarget target = ev.getTarget();
				final nsIDOMNode source = dragSession.getSourceNode();

				onDrop(source, target, data);

				ev.stopPropagation();
			}
		}

		private void onDrop(nsIDOMNode source, nsIDOMEventTarget target,
				Object data) {

		}

		public nsISupports queryInterface(String uuid) {
			return Mozilla.queryInterface(this, uuid);
		}
	}

	public String getParagraphState() {
		MozillaCommand command = MozillaCommand
				.findCommand(Command.PARAGRAPH_STATE);
		MozillaCommandParameters params = commandManager
				.getCommandState(command);
		return params.getStringValue(MozillaCommand.STATE_ATTRIBUTE);
	}

	public void insertTable(int rows, int columns, String width, String border) {
		insertTable(null, rows, columns, width, border);
	}

	public void insertTable(nsIDOMNode parent, int rows, int columns,
			String width, String border) {
		nsIDOMDocument document = getDOMDocument();
		nsIDOMHTMLTableElement table = (nsIDOMHTMLTableElement) document
				.createElement("table").queryInterface(
						nsIDOMHTMLTableElement.NS_IDOMHTMLTABLEELEMENT_IID);
		if (width != null) {
			table.setWidth(width);
		}

		if (border != null) {
			table.setBorder(border);
		}

		nsIDOMElement tbody = document.createElement("tbody");
		table.appendChild(tbody);

		for (int row = 0; row < rows; ++row) {
			nsIDOMHTMLTableRowElement tr = (nsIDOMHTMLTableRowElement) document
					.createElement("tr")
					.queryInterface(
							nsIDOMHTMLTableRowElement.NS_IDOMHTMLTABLEROWELEMENT_IID);
			tbody.appendChild(tr);

			for (int col = 0; col < columns; ++col) {
				nsIDOMHTMLTableCellElement td = (nsIDOMHTMLTableCellElement) document
						.createElement("td")
						.queryInterface(
								nsIDOMHTMLTableCellElement.NS_IDOMHTMLTABLECELLELEMENT_IID);
				tr.appendChild(td);
			}
		}

		// if (parent != null) {
		// parent.appendChild(table);
		// }else{
		htmlEditor.insertElementAtSelection(table, false);
		// }
		fireDocumentModified();
	}

	public void insertHTML(String html) {
		htmlEditor.insertHTML(html);
	}

	public void insertHTML(String html, nsIDOMNode targetNode) {
		htmlEditor.setCaretAfterElement(MozillaUtils
				.getElementFromNode(targetNode));
		htmlEditor.insertHTMLWithContext(html, "", "", "", null, targetNode, 0,
				false);
		fireDocumentModified();
	}

	public void insertLinkAroundSelection(nsIDOMElement element) {
		htmlEditor.insertLinkAroundSelection(element);
		fireDocumentModified();
	}

	public nsIDOMElement createElement(String element) {
		return getDOMDocument().createElement(element);
	}

	public nsIDOMNode createNode(String tag, nsIDOMNode parent, int position) {
		return editor.createNode(tag, parent, position);
	}

	private nsIHTMLAbsPosEditor getAbsPosEditor() {
		return (nsIHTMLAbsPosEditor) htmlEditor
				.queryInterface(nsIHTMLAbsPosEditor.NS_IHTMLABSPOSEDITOR_IID);
	}

	public void refreshResizers() {
		htmlObjectResizer.refreshResizers();
	}

	public void refreshGrabber() {
		nsIHTMLAbsPosEditor abs = getAbsPosEditor();
		abs.getSelectionContainerAbsolutelyPositioned();
		abs.refreshGrabber();
	}

	public void hideGrabber() {
		nsIHTMLAbsPosEditor abs = getAbsPosEditor();
		abs.getSelectionContainerAbsolutelyPositioned();
		abs.hideGrabber();
	}

	public void setParagraph(boolean enabled) {
		htmlEditor.setReturnInParagraphCreatesNewParagraph(enabled);
		htmlEditor.setParagraphFormat("p");
	}

	public void setCSSEnabled(boolean enabled) {
		htmlEditor.setIsCSSEnabled(enabled);
	}

	public nsIDOMElement getSelectedElement() {
		return htmlEditor.getSelectedElement(null);
	}

	public void activate(boolean activate) {
		nsIWebBrowserFocus webBrowserFocus = (nsIWebBrowserFocus) getWebBrowser()
				.queryInterface(nsIWebBrowserFocus.NS_IWEBBROWSERFOCUS_IID);
		if (activate) {
			webBrowserFocus.activate();
		} else {
			webBrowserFocus.deactivate();
		}
	}

	private void setEvenListeners(nsIDOMEventTarget domEventTarget) {
		MozillaEventListener mozillaEventListener = new MozillaEventListener(
				this);
		domEventTarget.addEventListener(MozillaEventListener.EVENT_DRAG_DROP,
				mozillaEventListener, false);
		domEventTarget.addEventListener(
				MozillaEventListener.EVENT_DRAG_GESTURE, mozillaEventListener,
				false);
		domEventTarget.addEventListener(MozillaEventListener.EVENT_DRAG_START,
				mozillaEventListener, false);
		// domEventTarget.addEventListener(MozillaEventListener.EVENT_DRAG_OVER,
		// mozillaEventListener, false);
		// domEventTarget.addEventListener(MozillaEventListener.EVENT_DRAG_ENTER,
		// mozillaEventListener, false);
		// domEventTarget.addEventListener(MozillaEventListener.EVENT_DRAG_EXIT,
		// mozillaEventListener, false);
		// domEventTarget.addEventListener(MozillaEventListener.EVENT_DRAG_LEAVE,
		// mozillaEventListener, false);
		// domEventTarget.addEventListener(MozillaEventListener.EVENT_DRAG_END,
		// mozillaEventListener, false);
		// domEventTarget.addEventListener(MozillaEventListener.EVENT_DRAG,
		// mozillaEventListener, false);
		domEventTarget.addEventListener(MozillaEventListener.EVENT_DROP,
				mozillaEventListener, false);

		domEventTarget.addEventListener(MozillaEventListener.EVENT_MOUSE_CLICK,
				mozillaEventListener, false);
		// domEventTarget.addEventListener(MozillaEventListener.EVENT_MOUSE_DOWN,
		// mozillaEventListener, false);
		// domEventTarget.addEventListener(MozillaEventListener.EVENT_MOUSE_MOVE,
		// mozillaEventListener, false);
		// domEventTarget.addEventListener(MozillaEventListener.EVENT_MOUSE_OUT,
		// mozillaEventListener, false);
		// domEventTarget.addEventListener(MozillaEventListener.EVENT_MOUSE_OVER,
		// mozillaEventListener, false);
		// domEventTarget.addEventListener(MozillaEventListener.EVENT_MOUSE_UP,
		// mozillaEventListener, false);
	}

	public nsIDragService getDragService() {
		return (nsIDragService) getServiceManager().getServiceByContractID(
				MozillaUtils.NS_DRAGSERVICE_CONTRACTID,
				nsIDragService.NS_IDRAGSERVICE_IID);
	}

	public nsIClipboardHelper getClipboardHelper() {
		return (nsIClipboardHelper) getServiceManager().getServiceByContractID(
				MozillaUtils.NS_CLIPBORADHELPER_CONTRACTID,
				nsIClipboardHelper.NS_ICLIPBOARDHELPER_IID);
	}

	private nsIServiceManager getServiceManager() {
		return mozilla.getServiceManager();
	}

	public ContentOutlinePage getContentOutlinePage() {
		nsIDOMNode bodyElement = getBodyElement();
		HtmlTag htmlTag = HtmlTagManager.getInstance().findTag(
				bodyElement.getNodeName());
		DomContentOutlinePage domContentOutlinePage = new DomContentOutlinePage(
				new HtmlBean(bodyElement, htmlTag));
		domContentOutlinePage
				.addSelectionChangedListener(new ISelectionChangedListener() {

					public void selectionChanged(SelectionChangedEvent event) {
						if (event.getSelection() instanceof StructuredSelection) {
							StructuredSelection selection = (StructuredSelection) event
									.getSelection();
							if (selection.getFirstElement() instanceof HtmlBean) {
								HtmlBean beanNode = (HtmlBean) selection
										.getFirstElement();
								highlightElement(MozillaUtils
										.getElementFromNode(beanNode
												.getDomNode()));
							}
						}

					}
				});
		return domContentOutlinePage;
	}

	public nsIDOMNode getBodyElement() {
		nsIDOMNodeList nodeList = getDOMDocument().getDocumentElement()
				.getElementsByTagName("body");
		if ((nodeList != null) && nodeList.getLength() > 0) {
			return nodeList.item(0);
		} else {
			nsIDOMNode bodyNode = getDOMDocument().createElement("body");
			getDOMDocument().getDocumentElement().appendChild(bodyNode);
			return bodyNode;
		}
	}

	public void rebuildFromSource(String htmlSource) {
		htmlEditor.rebuildDocumentFromSource(htmlSource);
	}

	public void dispose() {
		// mozilla.shutdownXPCOM(mozilla.getServiceManager());
	}
}
