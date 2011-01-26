package com.nayaware.webdesigner.editors;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.nayaware.webdesigner.spi.Command;
import com.nayaware.webdesigner.util.ImageUtils;
import com.nayaware.webdesigner.util.ResourceUtils;


/**
 * Toolbar for the Web Designer
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class WebDesignerToolbar {

	private Map<String, ToolItem> toolItems;
	private Combo paragraphFormatCombo;
	private WebPageDesigner webPageDesigner;

	private Map<String, String> paragraphFormatMap = new HashMap<String, String>();

	public WebDesignerToolbar(Composite parent, WebPageDesigner webPageDesigner) {
		this.webPageDesigner = webPageDesigner;
		toolItems = new HashMap<String, ToolItem>();

		CTabFolder tabFolder = new CTabFolder(parent, SWT.FLAT | SWT.BORDER);
		tabFolder
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		createFormatToolbar(tabFolder);
		createTableToolbar(tabFolder);
		
		tabFolder.setSelection(0);
	}

	private void createFormatToolbar(CTabFolder tabFolder) {
		final CTabItem formatTabItem = new CTabItem(tabFolder, SWT.NONE);
		formatTabItem.setText("Format");
		formatTabItem.setFont(ResourceUtils.getFont("Arial", 9, SWT.NONE));

		ToolBar formatToolbar = new ToolBar(tabFolder, SWT.FLAT);
		formatTabItem.setControl(formatToolbar);
		
		createToolItem(formatToolbar, SWT.PUSH, Command.COPY, ImageUtils.COPY,
				ImageUtils.COPY_DISABLED, "Copy");
		createToolItem(formatToolbar, SWT.PUSH, Command.PASTE,
				ImageUtils.PASTE, ImageUtils.PASTE_DISABLED, "Paste");
		createToolItem(formatToolbar, SWT.PUSH, Command.CUT, ImageUtils.CUT,
				ImageUtils.CUT_DISABLED, "Cut");
		
		createSeparator(formatToolbar);
		
		createToolItem(formatToolbar, SWT.PUSH, Command.UNDO, ImageUtils.UNDO,
				ImageUtils.UNDO_DISABLED, "Undo");
		createToolItem(formatToolbar, SWT.PUSH, Command.REDO, ImageUtils.REDO,
				ImageUtils.REDO_DISABLED, "Redo");
		
		createSeparator(formatToolbar);

		createParagraphFormatToolItem(formatToolbar);

		createSeparator(formatToolbar);

		// TODO: Change the type to CHECK when we are able to find the state of
		// the command for selection
		createToolItem(formatToolbar, SWT.PUSH, Command.BOLD, ImageUtils.BOLD,
				ImageUtils.BOLD_DISABLED, "Bold");
		createToolItem(formatToolbar, SWT.PUSH, Command.ITALIC,
				ImageUtils.ITALIC, ImageUtils.ITALIC_DISABLED, "Italic");
		createToolItem(formatToolbar, SWT.PUSH, Command.UNDERLINE,
				ImageUtils.UNDERLINE, ImageUtils.UNDERLINE_DISABLED,
				"Underline");
		createToolItem(formatToolbar, SWT.PUSH, Command.STRIKE_THROUGH,
				ImageUtils.STRIKE_THROUGH, ImageUtils.STRIKE_THROUGH_DISABLED,
				"Strikethrough");
		createToolItem(formatToolbar, SWT.PUSH, Command.SUBSCRIPT,
				ImageUtils.SUBSCRIPT, ImageUtils.SUBSCRIPT_DISABLED,
				"Subscript");
		createToolItem(formatToolbar, SWT.PUSH, Command.SUPERSCRIPT,
				ImageUtils.SUPERSCRIPT, ImageUtils.SUPERSCRIPT_DISABLED,
				"Superscript");

		createSeparator(formatToolbar);

		createToolItem(formatToolbar, SWT.PUSH, Command.ALIGN_LEFT,
				ImageUtils.ALIGN_LEFT, ImageUtils.ALIGN_LEFT_DISABLED,
				"Align Left");
		createToolItem(formatToolbar, SWT.PUSH, Command.ALIGN_CENTER,
				ImageUtils.ALIGN_CENTER, ImageUtils.ALIGN_CENTER_DISABLED,
				"Align Center");
		createToolItem(formatToolbar, SWT.PUSH, Command.ALIGN_RIGHT,
				ImageUtils.ALIGN_RIGHT, ImageUtils.ALIGN_RIGHT_DISABLED,
				"Align Right");
		createToolItem(formatToolbar, SWT.PUSH, Command.ALIGN_JUSTIFY,
				ImageUtils.ALIGN_JUSTIFY, ImageUtils.ALIGN_JUSTIFY_DISABLED,
				"Align Justify");
		createToolItem(formatToolbar, SWT.PUSH, Command.INDENT,
				ImageUtils.INDENT, ImageUtils.INDENT_DISABLED, "Indent");
		createToolItem(formatToolbar, SWT.PUSH, Command.OUTDENT,
				ImageUtils.OUTDENT, ImageUtils.OUTDENT_DISABLED, "Outdent");

		createSeparator(formatToolbar);

		createToolItem(formatToolbar, SWT.PUSH, Command.UL, ImageUtils.UL,
				ImageUtils.UL_DISABLED, "Unordered list");
		createToolItem(formatToolbar, SWT.PUSH, Command.OL, ImageUtils.OL,
				ImageUtils.OL_DISABLED, "Ordered list");
	}

	private void createTableToolbar(CTabFolder tabFolder) {
		CTabItem tableTabItem = new CTabItem(tabFolder, SWT.NONE);
		tableTabItem.setText("Table");
		tableTabItem.setFont(ResourceUtils.getFont("Arial", 9, SWT.NONE));

		ToolBar tableToolbar = new ToolBar(tabFolder, SWT.FLAT);
		tableTabItem.setControl(tableToolbar);

		createToolItem(tableToolbar, SWT.PUSH, Command.TABLE_INSERT,
				ImageUtils.TABLE_INSERT, ImageUtils.TABLE_INSERT,
				"Insert Table");
		createToolItem(tableToolbar, SWT.PUSH, Command.TABLE_DELETE,
				ImageUtils.TABLE_DELETE, ImageUtils.TABLE_DELETE_DISABLED,
				"Delete Table");

		createSeparator(tableToolbar);

		createToolItem(tableToolbar, SWT.PUSH, Command.TABLE_INSERT_ROW_BEFORE,
				ImageUtils.TABLE_INSERT_ROW_BEFORE,
				ImageUtils.TABLE_INSERT_ROW_BEFORE_DISABLED,
				"Insert Table Row Before");
		createToolItem(tableToolbar, SWT.PUSH, Command.TABLE_INSERT_ROW_AFTER,
				ImageUtils.TABLE_INSERT_ROW_AFTER,
				ImageUtils.TABLE_INSERT_ROW_AFTER_DISABLED,
				"Insert Table Row After");
		createToolItem(tableToolbar, SWT.PUSH, Command.TABLE_DELETE_ROW,
				ImageUtils.TABLE_DELETE_ROW,
				ImageUtils.TABLE_DELETE_ROW_DISABLED, "Delete Table Row");

		createSeparator(tableToolbar);

		createToolItem(tableToolbar, SWT.PUSH,
				Command.TABLE_INSERT_COLUMN_BEFORE,
				ImageUtils.TABLE_INSERT_COLUMN_BEFORE,
				ImageUtils.TABLE_INSERT_COLUMN_BEFORE_DISABLED,
				"Insert Table Column Before");
		createToolItem(tableToolbar, SWT.PUSH,
				Command.TABLE_INSERT_COLUMN_AFTER,
				ImageUtils.TABLE_INSERT_COLUMN_AFTER,
				ImageUtils.TABLE_INSERT_COLUMN_AFTER_DISABLED,
				"Insert Table Column After");
		createToolItem(tableToolbar, SWT.PUSH, Command.TABLE_DELETE_COLUMN,
				ImageUtils.TABLE_DELETE_COLUMN,
				ImageUtils.TABLE_DELETE_COLUMN_DISABLED, "Delete Table Row");

		createSeparator(tableToolbar);

		createToolItem(tableToolbar, SWT.PUSH, Command.TABLE_SPLIT_CELL,
				ImageUtils.TABLE_SPLIT_CELL,
				ImageUtils.TABLE_SPLIT_CELL_DISABLED, "Split Table Cell");
		createToolItem(tableToolbar, SWT.PUSH, Command.TABLE_MERGE_CELLS,
				ImageUtils.TABLE_MERGE_CELLS,
				ImageUtils.TABLE_MERGE_CELLS_DISABLED, "Merge Table Cells");

	}

	private void createSeparator(Object parent) {
		if (parent instanceof ToolBar) {
			new ToolItem((ToolBar) parent, SWT.SEPARATOR);
		} else if (parent instanceof Menu) {
			new MenuItem((Menu) parent, SWT.SEPARATOR);
		}
	}

	private ToolItem createToolItem(ToolBar toolbar, int type,
			final String command, String icon, String disabledIcon,
			String tooltip) {
		ToolItem button = new ToolItem(toolbar, type);
		button.setToolTipText(tooltip);

		if (!ImageUtils.NONE.equals(icon)) {
			button.setImage(ImageUtils.getIcon(icon));
		}
		if (!ImageUtils.NONE.equals(disabledIcon)) {
			button.setDisabledImage(ImageUtils.getIcon(disabledIcon));
		}
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (Command.TABLE_INSERT.equals(command)) {
					webPageDesigner.getEmbeddedBrowser().insertTable(3, 5,
							"100%", "1");
				} else {
					webPageDesigner.getEmbeddedBrowser()
							.executeCommand(command);
				}
				webPageDesigner.setDirty(true);
				webPageDesigner.activate(true);
			}
		});

		toolItems.put(command, button);
		return button;
	}

	private void createParagraphFormatToolItem(ToolBar toolbar) {
		ToolItem sep = new ToolItem(toolbar, SWT.SEPARATOR);
		paragraphFormatCombo = new Combo(toolbar, SWT.READ_ONLY);

		addParagraphFormatComboItem("", "Body Text", Command.PARAGRAPH_BODY);
		addParagraphFormatComboItem("p", "Paragraph", Command.PARAGRAPH_P);
		addParagraphFormatComboItem("h1", "Header 1", Command.PARAGRAPH_H1);
		addParagraphFormatComboItem("h2", "Header 2", Command.PARAGRAPH_H2);
		addParagraphFormatComboItem("h3", "Header 3", Command.PARAGRAPH_H3);
		addParagraphFormatComboItem("h3", "Header 4", Command.PARAGRAPH_H4);
		addParagraphFormatComboItem("h3", "Header 5", Command.PARAGRAPH_H5);
		addParagraphFormatComboItem("h3", "Header 6", Command.PARAGRAPH_H6);
		addParagraphFormatComboItem("pre", "Preformat", Command.PARAGRAPH_PRE);
		addParagraphFormatComboItem("addr", "address", Command.PARAGRAPH_ADDR);

		paragraphFormatCombo.setBackground(paragraphFormatCombo.getDisplay()
				.getSystemColor(SWT.COLOR_WHITE));
		FontData[] data = paragraphFormatCombo.getFont().getFontData();
		for (FontData d : data) {
			d.setHeight(11);
		}
		paragraphFormatCombo.setFont(new Font(toolbar.getDisplay(), data));

		paragraphFormatCombo.select(0);
		paragraphFormatCombo.pack();
		sep.setWidth(paragraphFormatCombo.getSize().x);
		sep.setControl(paragraphFormatCombo);

		paragraphFormatCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String selectedItem = paragraphFormatCombo
						.getItem(paragraphFormatCombo.getSelectionIndex());
				String command = (String) paragraphFormatCombo
						.getData(selectedItem);
				webPageDesigner.getEmbeddedBrowser().executeCommand(command);
				webPageDesigner.setDirty(true);
				webPageDesigner.activate(true);
			}
		});
	}

	private void addParagraphFormatComboItem(String name, String label,
			String data) {
		paragraphFormatCombo.add(label);
		paragraphFormatCombo.setData(label, data);
		paragraphFormatMap.put(name, label);
	}

	public void update() {
		webPageDesigner.activate(true);
		// TODO: Not working investigate
		// updateToolbar();
		updateParagraphFormatToolItem();
	}

	private void updateToolbar() {
		updateToolItem(Command.BOLD);
		updateToolItem(Command.ITALIC);
		updateToolItem(Command.UNDERLINE);
		updateToolItem(Command.STRIKE_THROUGH);
	}

	private void updateToolItem(String command) {
		ToolItem button = toolItems.get(command);
		int commandState = webPageDesigner.getEmbeddedBrowser()
				.getCommandState(command);
		boolean enabled = (commandState == Command.STATE_ENABLED);
		if (button.getSelection() != enabled) {
			button.setSelection(enabled);
		}
	}

	public void updateParagraphFormatToolItem() {
		String key = webPageDesigner.getEmbeddedBrowser().getParagraphState();

		if (paragraphFormatMap.containsKey(key)) {
			String value = paragraphFormatMap.get(key);
			String[] values = paragraphFormatCombo.getItems();
			boolean found = false;

			for (int i = 0; i < values.length && !found; i++) {
				if (value.equals(values[i])) {
					paragraphFormatCombo.select(i);
					found = true;
				}
			}

			if (!found) {
				paragraphFormatCombo.select(0);
			}
		} else {
			paragraphFormatCombo.select(0);
		}
	}
}
