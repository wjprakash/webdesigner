package com.nayaware.webdesigner.mozilla;

import org.mozilla.interfaces.nsITableEditor;

import com.nayaware.webdesigner.spi.Command;


/**
 * Mozilla Table Commands. Directly delegate the commands to the Mozilla Table
 * Editor
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class MozillaTableCommand implements Command {

	public static final MozillaTableCommand SELECT_TABLE = new MozillaTableCommand(
			Command.TABLE_SELECT);
	public static final MozillaTableCommand DELETE_TABLE = new MozillaTableCommand(
			Command.TABLE_DELETE);

	public static final MozillaTableCommand INSERT_ROW_BEFORE = new MozillaTableCommand(
			Command.TABLE_INSERT_ROW_BEFORE);
	public static final MozillaTableCommand INSERT_ROW_AFTER = new MozillaTableCommand(
			Command.TABLE_INSERT_ROW_AFTER);
	public static final MozillaTableCommand SELECT_ROW = new MozillaTableCommand(
			Command.TABLE_SELECT_ROW);
	public static final MozillaTableCommand DELETE_ROW = new MozillaTableCommand(
			Command.TABLE_DELETE_ROW);

	public static final MozillaTableCommand INSERT_COLUMN_BEFORE = new MozillaTableCommand(
			Command.TABLE_INSERT_COLUMN_BEFORE);
	public static final MozillaTableCommand INSERT_COLUMN_AFTER = new MozillaTableCommand(
			Command.TABLE_INSERT_COLUMN_AFTER);
	public static final MozillaTableCommand SELECT_COLUMN = new MozillaTableCommand(
			Command.TABLE_SELECT_COLUMN);
	public static final MozillaTableCommand DELETE_COLUMN = new MozillaTableCommand(
			Command.TABLE_DELETE_COLUMN);

	public static final MozillaTableCommand INSERT_CELL_BEFORE = new MozillaTableCommand(
			Command.TABLE_INSERT_CELL_BEFORE);
	public static final MozillaTableCommand INSERT_CELL_AFTER = new MozillaTableCommand(
			Command.TABLE_INSERT_CELL_AFTER);
	public static final MozillaTableCommand SELECT_CELL = new MozillaTableCommand(
			Command.TABLE_SELECT_CELL);
	public static final MozillaTableCommand DELETE_CELL = new MozillaTableCommand(
			Command.TABLE_DELETE_CELL);
	public static final MozillaTableCommand SPLIT_CELL = new MozillaTableCommand(
			Command.TABLE_SPLIT_CELL);
	public static final MozillaTableCommand MERGE_CELLS = new MozillaTableCommand(
			Command.TABLE_MERGE_CELLS);

	private static MozillaTableCommand[] commands = { SELECT_TABLE,
			DELETE_TABLE, INSERT_ROW_BEFORE, INSERT_ROW_AFTER, SELECT_ROW,
			DELETE_ROW, INSERT_COLUMN_BEFORE, INSERT_COLUMN_AFTER,
			SELECT_COLUMN, DELETE_COLUMN, INSERT_CELL_BEFORE,
			INSERT_CELL_AFTER, SELECT_CELL, DELETE_CELL, SPLIT_CELL, MERGE_CELLS };

	private String name;

	private MozillaTableCommand(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void execute(nsITableEditor tableEditor) {
		if (Command.TABLE_SELECT.equals(getName())) {
			tableEditor.selectTable();
		} else if (Command.TABLE_DELETE.equals(getName())) {
			tableEditor.deleteTable();
		} else if (Command.TABLE_INSERT_ROW_BEFORE.equals(getName())) {
			tableEditor.insertTableRow(1, false);
		} else if (Command.TABLE_INSERT_ROW_AFTER.equals(getName())) {
			tableEditor.insertTableRow(1, true);
		} else if (Command.TABLE_SELECT_ROW.equals(getName())) {
			tableEditor.selectTableRow();
		} else if (Command.TABLE_DELETE_ROW.equals(getName())) {
			tableEditor.deleteTableRow(1);
		} else if (Command.TABLE_INSERT_COLUMN_BEFORE.equals(getName())) {
			tableEditor.insertTableColumn(1, false);
		} else if (Command.TABLE_INSERT_COLUMN_AFTER.equals(getName())) {
			tableEditor.insertTableColumn(1, true);
		} else if (Command.TABLE_SELECT_COLUMN.equals(getName())) {
			tableEditor.selectTableColumn();
		} else if (Command.TABLE_DELETE_COLUMN.equals(getName())) {
			tableEditor.deleteTableColumn(1);
		} else if (Command.TABLE_INSERT_CELL_BEFORE.equals(getName())) {
			tableEditor.insertTableCell(1, false);
		} else if (Command.TABLE_INSERT_CELL_AFTER.equals(getName())) {
			tableEditor.insertTableCell(1, true);
		} else if (Command.TABLE_SELECT_CELL.equals(getName())) {
			tableEditor.selectTableCell();
		} else if (Command.TABLE_DELETE_CELL.equals(getName())) {
			tableEditor.deleteTableCell(1);
		} else if (Command.TABLE_SPLIT_CELL.equals(getName())) {
			tableEditor.splitTableCell();
		} else if (Command.TABLE_MERGE_CELLS.equals(getName())) {
			tableEditor.joinTableCells(true);
		}
	}

	public static MozillaTableCommand findCommand(String commandName) {
		for (MozillaTableCommand command : commands) {
			if (command.getName().equals(commandName)) {
				return command;
			}
		}
		return null;
	}
}
