package com.nayaware.webdesigner.editors.html;

import org.eclipse.ui.editors.text.TextEditor;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class HTMLEditor extends TextEditor {

	private ColorManager colorManager;

	public HTMLEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new XMLConfiguration(colorManager));
		setDocumentProvider(new XMLDocumentProvider());
	}
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
