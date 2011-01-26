package com.nayaware.webdesigner.mozilla;

import com.nayaware.webdesigner.spi.Command;

/**
 * Various Mozilla Commands as defined in
 * https://developer.mozilla.org/en/Editor_Embedding_Guide
 * #Index_of_Commands_and_Parameters
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class MozillaCommand implements Command {
	public static final MozillaCommand BOLD = new MozillaCommand(Command.BOLD,
			"cmd_bold", false, true);
	public static final MozillaCommand ITALIC = new MozillaCommand(
			Command.ITALIC, "cmd_italic", false, true);
	public static final MozillaCommand UNDERLINE = new MozillaCommand(
			Command.UNDERLINE, "cmd_underline", false, true);
	public static final MozillaCommand STRIKE_THROUGH = new MozillaCommand(
			Command.STRIKE_THROUGH, "cmd_strikethrough", false, true);
	public static final MozillaCommand TT = new MozillaCommand(Command.TT,
			"cmd_tt", false, true);

	public static final MozillaCommand EM = new MozillaCommand(Command.EM,
			"cmd_em", false, true);
	public static final MozillaCommand STRONG = new MozillaCommand(
			Command.STRONG, "cmd_strong", false, true);
	public static final MozillaCommand CITE = new MozillaCommand(Command.CITE,
			"cmd_cite", false, true);
	public static final MozillaCommand ACRONYM = new MozillaCommand(
			Command.ACRONYM, "cmd_acronym", false, true);
	public static final MozillaCommand ABBR = new MozillaCommand(Command.ABBR,
			"cmd_abbr", false, true);
	public static final MozillaCommand SUBSCRIPT = new MozillaCommand(
			Command.SUBSCRIPT, "cmd_subscript", false, true);
	public static final MozillaCommand SUPERSCRIPT = new MozillaCommand(
			Command.SUPERSCRIPT, "cmd_superscript", false, true);
	public static final MozillaCommand NOBREAK = new MozillaCommand(
			Command.NOBREAK, "cmd_nobreak", false, true);

	public static final MozillaCommand CUT = new MozillaCommand(Command.CUT,
			"cmd_cut", false);
	public static final MozillaCommand COPY = new MozillaCommand(Command.COPY,
			"cmd_copy", false);
	public static final MozillaCommand PASTE = new MozillaCommand(
			Command.PASTE, "cmd_paste", false);

	public static final MozillaCommand SELECT_ALL = new MozillaCommand(
			Command.SELECT_ALL, "cmd_selectAll", false);
	public static final MozillaCommand UNDO = new MozillaCommand(Command.UNDO,
			"cmd_undo", false);
	public static final MozillaCommand REDO = new MozillaCommand(Command.REDO,
			"cmd_redo", false);
	
	public static final MozillaCommand OL = new MozillaCommand(Command.OL,
			"cmd_ol", false, true);
	public static final MozillaCommand UL = new MozillaCommand(Command.UL,
			"cmd_ul", false, true);
	public static final MozillaCommand INDENT = new MozillaCommand(
			Command.INDENT, "cmd_indent", false);
	public static final MozillaCommand OUTDENT = new MozillaCommand(
			Command.OUTDENT, "cmd_outdent", false);
	public static final MozillaCommand ALIGN_LEFT = new MozillaCommand(
			Command.ALIGN_LEFT, "cmd_align", "left");
	public static final MozillaCommand ALIGN_RIGHT = new MozillaCommand(
			Command.ALIGN_RIGHT, "cmd_align", "right");
	public static final MozillaCommand ALIGN_CENTER = new MozillaCommand(
			Command.ALIGN_CENTER, "cmd_align", "center");
	public static final MozillaCommand ALIGN_JUSTIFY = new MozillaCommand(
			Command.ALIGN_JUSTIFY, "cmd_align", "justify");
	public static final MozillaCommand INCREASE_FONT = new MozillaCommand(
			Command.INCREASE_FONT, "cmd_increaseFont", false);
	public static final MozillaCommand DECREASE_FONT = new MozillaCommand(
			Command.DECREASE_FONT, "cmd_decreaseFont", false);
	
	public static final MozillaCommand WORD_PREVIOUS = new MozillaCommand(
			Command.WORD_PREVIOUS, "cmd_wordPrevious", false);
	public static final MozillaCommand WORD_NEXT = new MozillaCommand(
			Command.WORD_NEXT, "cmd_wordNext", false);
	public static final MozillaCommand SELECT_WORD_PREVIOUS = new MozillaCommand(
			Command.SELECT_WORD_PREVIOUS, "cmd_selectWordPrevious", false);
	public static final MozillaCommand SELECT_WORD_NEXT = new MozillaCommand(
			Command.SELECT_WORD_NEXT, "cmd_selectWordNext", false);
	public static final MozillaCommand ABS_POS = new MozillaCommand(
			Command.ABS_POS, "cmd_absPos", "absolute");
	public static final MozillaCommand BRING_FRONT = new MozillaCommand(
			Command.BRING_FRONT, "cmd_increaseZIndex", false);
	public static final MozillaCommand SEND_BACK = new MozillaCommand(
			Command.SEND_BACK, "cmd_decreaseZIndex", false);
	public static final MozillaCommand REMOVE_STYLES = new MozillaCommand(
			Command.REMOVE_STYLES, "cmd_removeStyles", false);
	public static final MozillaCommand REMOVE_LINKS = new MozillaCommand(
			Command.REMOVE_LINKS, "cmd_removeLinks", false);

	public static final MozillaCommand PARAGRAPH_BODY = new MozillaCommand(
			Command.PARAGRAPH_BODY, "cmd_paragraphState", "");
	public static final MozillaCommand PARAGRAPH_P = new MozillaCommand(
			Command.PARAGRAPH_P, "cmd_paragraphState", "p");
	public static final MozillaCommand PARAGRAPH_H1 = new MozillaCommand(
			Command.PARAGRAPH_H1, "cmd_paragraphState", "h1");
	public static final MozillaCommand PARAGRAPH_H2 = new MozillaCommand(
			Command.PARAGRAPH_H2, "cmd_paragraphState", "h2");
	public static final MozillaCommand PARAGRAPH_H3 = new MozillaCommand(
			Command.PARAGRAPH_H3, "cmd_paragraphState", "h3");
	public static final MozillaCommand PARAGRAPH_H4 = new MozillaCommand(
			Command.PARAGRAPH_H4, "cmd_paragraphState", "h4");
	public static final MozillaCommand PARAGRAPH_H5 = new MozillaCommand(
			Command.PARAGRAPH_H5, "cmd_paragraphState", "h5");
	public static final MozillaCommand PARAGRAPH_H6 = new MozillaCommand(
			Command.PARAGRAPH_H6, "cmd_paragraphState", "h6");
	public static final MozillaCommand PARAGRAPH_ADDR = new MozillaCommand(
			Command.PARAGRAPH_ADDR, "cmd_paragraphState", "address");
	public static final MozillaCommand PARAGRAPH_PRE = new MozillaCommand(
			Command.PARAGRAPH_PRE, "cmd_paragraphState", "pre");

	public static final MozillaCommand PARAGRAPH_STATE = new MozillaCommand(
			Command.PARAGRAPH_STATE, "cmd_paragraphState", "");
	
	public static final MozillaCommand CLIPBOARD_DRAG_DROP_HOOK = new MozillaCommand(
			Command.CLIPBOARD_DRAG_DROP_HOOK, "cmd_clipboardDragDropHook", true);

	public static final String STATE_ATTRIBUTE = "state_attribute";
	public static final String STATE_ALL = "state_all";
	public static final String STATE_MIXED = "state_mixed";
	public static final String STATE_ENABLED = "state_enabled";

	private static MozillaCommand[] commands = { BOLD, ITALIC, UNDERLINE,
			STRIKE_THROUGH, TT, EM, STRONG, CITE, ACRONYM, ABBR, SUBSCRIPT,
			SUPERSCRIPT, NOBREAK, CUT, COPY, PASTE, SELECT_ALL, UNDO, REDO, OL,
			UL, INDENT, OUTDENT, ALIGN_LEFT, ALIGN_RIGHT, ALIGN_CENTER,
			ALIGN_JUSTIFY, INCREASE_FONT, DECREASE_FONT, WORD_PREVIOUS,
			WORD_NEXT, SELECT_WORD_PREVIOUS, SELECT_WORD_NEXT, ABS_POS,
			BRING_FRONT, SEND_BACK, REMOVE_STYLES, REMOVE_LINKS,
			PARAGRAPH_BODY, PARAGRAPH_P, PARAGRAPH_H1, PARAGRAPH_H2,
			PARAGRAPH_H3, PARAGRAPH_H4, PARAGRAPH_H5, PARAGRAPH_H6,
			PARAGRAPH_ADDR, PARAGRAPH_PRE, PARAGRAPH_STATE };

	private String name;
	private String command;
	private boolean hasParams;
	private boolean mixedState;
	private Object attribute;

	private MozillaCommand(String name, String command, Object attribute) {
		this(name, command, true, false);
		this.attribute = attribute;
	}

	private MozillaCommand(String name, String command, boolean hasParams) {
		this(name, command, hasParams, false);
	}

	private MozillaCommand(String name, String command, boolean hasParams,
			boolean mixedState) {
		this.name = name;
		this.command = command;
		this.hasParams = hasParams;
		this.mixedState = mixedState;
	}

	public String getName() {
		return name;
	}

	public String getCommand() {
		return command;
	}

	public boolean hasParameters() {
		return hasParams;
	}

	public boolean isMixedState() {
		return mixedState;
	}

	public Object getAttribute() {
		return attribute;
	}

	public static MozillaCommand findCommand(String commandName) {
		for (MozillaCommand command : commands) {
			if (command.getName().equals(commandName)) {
				return command;
			}
		}
		return null;
	}
}
