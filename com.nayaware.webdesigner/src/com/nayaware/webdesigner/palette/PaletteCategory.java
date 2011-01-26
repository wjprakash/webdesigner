package com.nayaware.webdesigner.palette;

import java.util.ArrayList;
import java.util.List;

/**
 * Palette Category Model
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class PaletteCategory {

	public static final String TAG_NAME = "category";
	public static final String ATTR_NAME = "name";
	public static final String ATTR_DISPLAY_NAME = "displayName";

	private String name;
	private String displayName;

	private List<PaletteItem> paletteItems = new ArrayList<PaletteItem>();

	public PaletteCategory(String name, String displayName) {
		super();
		this.displayName = displayName;
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PaletteItem> getPaletteItems() {
		return paletteItems;
	}

	public void setPaletteItems(List<PaletteItem> paletteItems) {
		this.paletteItems = paletteItems;
	}

	public void addPaletteItem(PaletteItem paletteItem) {
		paletteItems.add(paletteItem);
	}

}
