package com.nayaware.webdesigner.palette;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for the Palette
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class Palette {
	
	public static final String TAG_NAME = "palette"; 
	
	private String name;
	private List<PaletteCategory> paletteCategories = new ArrayList<PaletteCategory>();

	public Palette(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PaletteCategory> getPaletteCategories() {
		return paletteCategories;
	}

	public void setPaletteCategories(List<PaletteCategory> paletteCategories) {
		this.paletteCategories = paletteCategories;
	}
	
	public void addPaletteCategory(PaletteCategory paletteCategory) {
		paletteCategories.add(paletteCategory);
	}
}
