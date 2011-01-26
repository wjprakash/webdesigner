package com.nayaware.webdesigner.spi;

/**
 * Various commands to be performed by the designer
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public interface Command {

	public static final String BOLD = "bold";
	public static final String ITALIC = "italic";
	public static final String UNDERLINE = "underline";
	public static final String STRIKE_THROUGH = "strikethrough";
	public static final String ABBR = "abbr";
	public static final String SUBSCRIPT = "subscript";
	public static final String SUPERSCRIPT = "superscript";
	public static final String STRONG = "strong";
	public static final String TT = "tt";
	public static final String ACRONYM = "acronym";
	public static final String CITE = "cite";
	public static final String EM = "em";
	
	public static final String OL = "ol";
	public static final String UL = "ul";
	
	public static final String NOBREAK = "nobreak";
	
	public static final String REDO = "redo";
	public static final String UNDO = "undo";
	public static final String CUT = "cut";
	public static final String COPY = "copy";
	public static final String PASTE = "paste";
	public static final String SELECT_ALL = "selectAll";
	
	public static final String INDENT = "indent";
	public static final String OUTDENT = "outdent";
	
	public static final String ALIGN_LEFT = "";
	public static final String ALIGN_CENTER = "align_center";
	public static final String ALIGN_JUSTIFY = "align_justify";
	public static final String ALIGN_RIGHT = "align_right";
	
	public static final String INCREASE_FONT = "increaseFont";
	public static final String DECREASE_FONT = "decreaseFont";
	
	public static final String WORD_NEXT = "wordNext";
	public static final String WORD_PREVIOUS = "wordPrevious";
	public static final String SELECT_WORD_PREVIOUS = "selectWordPrevious";
	public static final String SELECT_WORD_NEXT = "selectWordNext";
	
	public static final String BRING_FRONT = "increaseZIndex";
	public static final String SEND_BACK = "decreaseZIndex";
	public static final String ABS_POS = "absPos";
	
	public static final String REMOVE_STYLES = "removeStyles";
	public static final String REMOVE_LINKS = "removeLinks";
	
	public static final String PARAGRAPH_BODY = "paragraphBody";
	public static final String PARAGRAPH_P = "paragraphPara";
	public static final String PARAGRAPH_H1 = "paragraphH1";
	public static final String PARAGRAPH_H2 = "paragraphH2";
	public static final String PARAGRAPH_H3 = "paragraphH3";
	public static final String PARAGRAPH_H4 = "paragraphH4";
	public static final String PARAGRAPH_H5 = "paragraphH5";
	public static final String PARAGRAPH_H6 = "paragraphH6";
	public static final String PARAGRAPH_ADDR = "paragraphAddr";
	public static final String PARAGRAPH_PRE = "paragraphPre";
	
	public static final String TABLE_INSERT = "insertTable";
	public static final String TABLE_SELECT = "selectTable";
	public static final String TABLE_DELETE = "deleteTable";
	
	public static final String TABLE_INSERT_ROW_BEFORE = "insertRowBefore";
	public static final String TABLE_INSERT_ROW_AFTER = "insertRowAfter";
	public static final String TABLE_SELECT_ROW = "selectRow";
	public static final String TABLE_DELETE_ROW = "deleteRow";
	
	public static final String TABLE_INSERT_COLUMN_BEFORE = "insertColumnBefore";
	public static final String TABLE_INSERT_COLUMN_AFTER = "insertColumnAfter";
	public static final String TABLE_SELECT_COLUMN = "selectColumn";
	public static final String TABLE_DELETE_COLUMN = "deleteColumn";
	
	public static final String TABLE_INSERT_CELL_BEFORE = "insertCellBefore";
	public static final String TABLE_INSERT_CELL_AFTER = "insertCellAfter";
	public static final String TABLE_SELECT_CELL = "selectCell";
	public static final String TABLE_DELETE_CELL = "deleteCell";
	public static final String TABLE_SPLIT_CELL = "splitCell";
	public static final String TABLE_MERGE_CELLS = "mergeCells";
	
	public static final int STATE_DISABLED = 0;
	public static final int STATE_ENABLED = 1;
	public static final int STATE_MIXED = 2;
	public static final int STATE_ALL = 3;
	
	public static final String PARAGRAPH_STATE = "paragraphState";
	
	public static final String CLIPBOARD_DRAG_DROP_HOOK = "clipboardDragDropHook";

	public String getName();
}
