package com.nayaware.webdesigner.palette;

import org.eclipse.swt.graphics.Image;

import com.nayaware.webdesigner.util.ImageUtils;


/**
 * Model for the Palette Item
 * @author Winston Prakash
 * @version 1.0
 */
public class PaletteItem {
	
	public static final String DIV = "div";
	public static final String HR = "hr";
	public static final String TABLE = "table";
	public static final String HYPERLINK = "hyperlink";
	
	public static final String TAG_NAME = "item"; 
	public static final String ATTR_NAME = "name"; 
	public static final String ATTR_DISPLAY_NAME = "displayName"; 
	public static final String ATTR_ICON = "icon";
	public static final String RADIO_BUTTON_GROUP = "radioButtonGroup";
	public static final String RADIO_BUTTON = "radioButton";
	public static final String FORM = "form";
	public static final String LABEL = "label";
	public static final String BUTTON = "button";
	public static final String DROP_DOWN = "dropdown";
	public static final String LIST_BOX = "listBox";
	public static final String HIDDEN_FIELD = "hiddenField";
	public static final String PASSWORD_FIELD = "passwordField";
	public static final String CHECKBOX = "checkbox";
	public static final String CHECKBOX_GROUP = "checkboxGroup";
	public static final String TEXT_FIELD = "textField";
	public static final String TEXT_AREA = "textArea";

	private String name;
	private String displayName;
	private String iconName;
	private String data;
	
	public PaletteItem(String name, String displayName, String iconName) {
		this.displayName = displayName;
		this.iconName = iconName;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
	
	public Image getIcon(){
		return ImageUtils.getIcon(iconName);
	}

	public void setData(String data) {
		this.data = data;
	}

	public Object getData() {
		return data;
	}
}
