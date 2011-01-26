package com.nayaware.webdesigner.spi;

import org.eclipse.ui.views.contentoutline.ContentOutlinePage;


/**
 * Embedded Browser Service Provider Interface
 * @author Winston Prakash
 * @version 1.0
 */

public interface EmbeddedBrowser {
	public static final int MOZILLA = 1;

	public void loadUrl(String url);

	public String getHtml();

	public void executeCommand(String command);

	public String getParagraphState();
	
	public void addSelectionListener(ISelectionListener selectionListener);

	public void addModificationListener(IModificationListener modificationListener);

	public boolean isCommandEnabled(String command);

	public int getCommandState(String command);

	public void enableVisualAid(boolean enable);
	
	public void insertTable(int rows, int column, String width, String border);
	
	public void insertHTML(String html);
	
	public ContentOutlinePage getContentOutlinePage();

	public void rebuildFromSource(String html);

	public void activate(boolean activate);

	public void dispose();
}
