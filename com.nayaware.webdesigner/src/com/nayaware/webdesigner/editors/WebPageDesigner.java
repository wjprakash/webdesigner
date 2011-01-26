package com.nayaware.webdesigner.editors;

/**
 * Web Page designer
 * @author Winston Prakash
 * @version 1.0
 */
import java.net.MalformedURLException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;

import com.nayaware.webdesigner.spi.EmbeddedBrowser;
import com.nayaware.webdesigner.spi.EmbeddedBrowserFactory;
import com.nayaware.webdesigner.spi.IModificationListener;
import com.nayaware.webdesigner.spi.ISelection;
import com.nayaware.webdesigner.spi.ISelectionListener;
import com.nayaware.webdesigner.util.ErrorManager;


public class WebPageDesigner extends EditorPart {

	private EmbeddedBrowser embeddedBrowser;
	private WebDesignerToolbar webDesignerToolbar;
	private boolean dirty;

	public WebPageDesigner() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		if (getEditorInput() instanceof FileEditorInput) {
			FileEditorInput fileEditorInput = (FileEditorInput) getEditorInput();
			this
					.setPartName("Web Designer (" + fileEditorInput.getName()
							+ ")");
		}
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		// Setting Layout for the parent Composite
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		layout.marginBottom = 0;
		parent.setLayout(layout);

		// Editors and Toolbar composite
		Composite toolbarComposite = new Composite(parent, SWT.BORDER
				| SWT.EMBEDDED);
		GridLayout layoutEdTl = new GridLayout(1, false);
		layoutEdTl.horizontalSpacing = 0;
		layoutEdTl.verticalSpacing = 0;
		layoutEdTl.marginHeight = 0;
		layoutEdTl.marginBottom = 0;
		layoutEdTl.marginWidth = 0;
		toolbarComposite.setLayout(layoutEdTl);
		toolbarComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		webDesignerToolbar = new WebDesignerToolbar(toolbarComposite, this);

		embeddedBrowser = EmbeddedBrowserFactory.getEmbeddedBrowser(parent,
				EmbeddedBrowser.MOZILLA);
		if (getEditorInput() instanceof FileEditorInput) {
			FileEditorInput fileEditorInput = (FileEditorInput) getEditorInput();
			try {
				embeddedBrowser.loadUrl(fileEditorInput.getURI().toURL()
						.toString());
			} catch (MalformedURLException exc) {
				ErrorManager.showException(exc);
			}
		}
		embeddedBrowser.enableVisualAid(true);
		addEmbeddedBrowserListeners();
		
//		getSite().setSelectionProvider(new ISelectionProvider(){
//
//			public void addSelectionChangedListener(
//					ISelectionChangedListener listener) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			public org.eclipse.jface.viewers.ISelection getSelection() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			public void removeSelectionChangedListener(
//					ISelectionChangedListener listener) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			public void setSelection(
//					org.eclipse.jface.viewers.ISelection selection) {
//				// TODO Auto-generated method stub
//				
//			}});
	}

	private void addEmbeddedBrowserListeners() {
		embeddedBrowser.addSelectionListener(new ISelectionListener() {
			public void selectionChanged(ISelection selection) {
				webDesignerToolbar.update();
			}
		});

		embeddedBrowser.addModificationListener(new IModificationListener() {
			public void modified() {
				setDirty(true);
			}
		});
	}

	public String getHtml() {
		return embeddedBrowser.getHtml();
	}

	@Override
	public void setFocus() {
	}

	public EmbeddedBrowser getEmbeddedBrowser() {
		return embeddedBrowser;
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	protected void setDirty(boolean value) {
		dirty = value;
		firePropertyChange(PROP_DIRTY);
	}

	public void enableVisualAid(boolean enable) {
		embeddedBrowser.enableVisualAid(enable);
	}

	public Object getAdapter(Class adapter) {
		if (adapter.equals(IContentOutlinePage.class)) {
			return getContentOutline();
		}
		if (adapter.equals(IPropertySheetPage.class)) {
			return getPropertySheet();
		}
		return super.getAdapter(adapter);
	}

	/**
	 * Returns the content outline.
	 */
	protected ContentOutlinePage getContentOutline() {
		return embeddedBrowser.getContentOutlinePage();
	}

	/**
	 * Returns the property sheet.
	 */
	protected IPropertySheetPage getPropertySheet() {
		return new PropertySheetPage();
	}

	public void rebuildFromSource(String html) {
		embeddedBrowser.rebuildFromSource(html);
	}

	public void activate(boolean activate) {
		embeddedBrowser.activate(activate);
	}
	
	public void dispose() {
		embeddedBrowser.dispose();
		super.dispose();
	}
}
