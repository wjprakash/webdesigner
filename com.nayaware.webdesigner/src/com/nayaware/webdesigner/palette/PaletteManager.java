package com.nayaware.webdesigner.palette;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.nayaware.webdesigner.util.ErrorManager;


/**
 * Manager that manager the Palette. Reads and creates the palette from XML
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class PaletteManager {
	
	private static PaletteManager instance;

	public static synchronized PaletteManager getInstance() {
		if (instance == null) {
			instance = new PaletteManager();
		}
		return instance;
	}

	public Palette loadPalette(URL paletteXmlUrl) {
		Document document = parseXml(paletteXmlUrl);
		Element paletteElement = (Element) document.getElementsByTagName(Palette.TAG_NAME).item(0);
		String paletteName = paletteElement.getAttribute("name");
		Palette palette = new Palette(paletteName);
		loadPaletteCategories(palette, paletteElement);
		return palette;
	}
	
	private void loadPaletteCategories(Palette palette, Element paletteElement){
		NodeList paletteCategories =  paletteElement.getElementsByTagName(PaletteCategory.TAG_NAME);
		for (int i = 0; i < paletteCategories.getLength(); i++) {
			Element paletteCategoryElement = (Element) paletteCategories.item(i);
			String paletteCategoryName = paletteCategoryElement.getAttribute(PaletteCategory.ATTR_NAME);
			String paletteCategoryDisplayName = paletteCategoryElement.getAttribute(PaletteCategory.ATTR_DISPLAY_NAME);
			PaletteCategory paletteCategory = new PaletteCategory(paletteCategoryName, paletteCategoryDisplayName);
			palette.addPaletteCategory(paletteCategory);
			loadPaletteItems(paletteCategory, paletteCategoryElement);
		}
	}
	
	private void loadPaletteItems(PaletteCategory paletteCategory, Element paletteCategoryElement){
		NodeList paletteItems =  paletteCategoryElement.getElementsByTagName(PaletteItem.TAG_NAME);
		for (int i = 0; i < paletteItems.getLength(); i++) {
			Element paletteItemElement = (Element) paletteItems.item(i);
			String paletteItemName = paletteItemElement.getAttribute(PaletteItem.ATTR_NAME);
			String paletteItemDisplayName = paletteItemElement.getAttribute(PaletteItem.ATTR_DISPLAY_NAME);
			String paletteItemIcon = paletteItemElement.getAttribute(PaletteItem.ATTR_ICON);
			PaletteItem paletteItem = new PaletteItem(paletteItemName, paletteItemDisplayName, paletteItemIcon);
			paletteCategory.addPaletteItem(paletteItem);
		}
	}

	public static Document parseXml(URL url) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			//factory.setValidating(true);

			Document doc = factory.newDocumentBuilder().parse(url.openStream());
			return doc;
		} catch (SAXException exc) {
			ErrorManager.showException(exc);
		} catch (ParserConfigurationException exc) {
			ErrorManager.showException(exc);
		} catch (IOException exc) {
			ErrorManager.showException(exc);
		}
		return null;
	}
}
