package com.nayaware.webdesigner.mozilla;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class DomContentOutlinePage extends ContentOutlinePage {
	private IAdaptable model;

	/**
	 * Create a new instance of the receiver using adapatable as the model.
	 */
	public DomContentOutlinePage(IAdaptable adaptable) {
		this.model = adaptable;
	}

	/**
	 * Creates the control and registers the popup menu for this page Menu
	 */
	public void createControl(Composite parent) {
		super.createControl(parent);
		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new WorkbenchContentProvider());
		viewer.setLabelProvider(new WorkbenchLabelProvider());
		viewer.setInput(this.model);
		viewer.expandToLevel(3);

		// Configure the context menu.
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS
				+ "-end")); //$NON-NLS-1$

		Menu menu = menuMgr.createContextMenu(viewer.getTree());
		viewer.getTree().setMenu(menu);
		// Be sure to register it so that other plug-ins can add actions.
		getSite()
				.registerContextMenu(
						"com.nayaware.webdesigner.mozilla.outline", menuMgr, viewer); //$NON-NLS-1$
	}
}
