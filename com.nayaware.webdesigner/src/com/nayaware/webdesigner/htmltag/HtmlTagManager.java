package com.nayaware.webdesigner.htmltag;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import com.nayaware.webdesigner.util.ErrorManager;


/**
 * Parses the HTML schema definition file and creates HTML tag information
 * 
 * @author Winston Prakash
 */
public class HtmlTagManager {

	private static HtmlTagManager tagManagerInstance;

	private Map<String, HtmlTag> htmlTags = new HashMap<String, HtmlTag>();
	private Map<String, HtmlAttributeGroup> htmlAttributeGroups = new HashMap<String, HtmlAttributeGroup>();
	private Map<String, HtmlTagGroup> tagGroups = new HashMap<String, HtmlTagGroup>();

	private HtmlTagManager() {
		parse("xhtml1-strict.xsd");
	}

	public static synchronized HtmlTagManager getInstance() {
		if (tagManagerInstance == null) {
			tagManagerInstance = new HtmlTagManager();
		}
		return tagManagerInstance;
	}

	public HtmlTag findTag(String tagName) {
		return htmlTags.get(tagName.toLowerCase(Locale.ENGLISH));
	}

	private void parse(String xsdFileName) {
		URL htmlXsdUrl = getClass().getResource(xsdFileName);
		try {
			// Create a builder factory
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setValidating(false);
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setErrorHandler(new ErrorHandler() {

				public void error(SAXParseException exc) {
					ErrorManager.showException(exc);
				}

				public void fatalError(SAXParseException exc) {
					ErrorManager.showException(exc);
				}

				public void warning(SAXParseException exc) {
					ErrorManager.showException(exc);
				}
			});

			// TODO: Create more meaningful entity resolver
			builder.setEntityResolver(new EmptyEntityResolver());

			// Create the builder and parse the file
			Document htmlSchemaDocument = factory.newDocumentBuilder().parse(
					htmlXsdUrl.openStream());

			// Find all the html element attribute group
			NodeList attributeGroupNodes = htmlSchemaDocument
					.getDocumentElement().getElementsByTagName(
							"xs:attributeGroup");
			for (int i = 0; i < attributeGroupNodes.getLength(); i++) {
				Element attributeGroupElement = (Element) attributeGroupNodes
						.item(i);
				if (attributeGroupElement.hasAttribute("name")) {
					HtmlAttributeGroup htmlAttributeGroup = new HtmlAttributeGroup(
							attributeGroupElement, htmlAttributeGroups);
					htmlAttributeGroups.put(htmlAttributeGroup.getName(),
							htmlAttributeGroup);
				}
			}

			// Find all the html element definitins
			NodeList elementNodes = htmlSchemaDocument.getDocumentElement()
					.getElementsByTagName("xs:element");
			for (int i = 0; i < elementNodes.getLength(); i++) {
				Element element = (Element) elementNodes.item(i);
				if (element.hasAttribute("name")) {
					HtmlTag htmlElement = new HtmlTag(element,
							htmlAttributeGroups);
					htmlTags.put(htmlElement.getName(), htmlElement);
				}
			}

			// Find the element groups defined
			NodeList groupNodes = htmlSchemaDocument.getDocumentElement()
					.getElementsByTagName("xs:group");

			for (int i = 0; i < groupNodes.getLength(); i++) {
				Element htmlElementGroup = (Element) groupNodes.item(i);
				if (htmlElementGroup.hasAttribute("name")) {
					HtmlTagGroup elementGroup = new HtmlTagGroup(
							htmlElementGroup, htmlTags, tagGroups);
					tagGroups.put(elementGroup.getName(), elementGroup);
					// elementGroup.dump();
				}
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	/**
	 * Empty entity resolver to avoid network access
	 */
	private static class EmptyEntityResolver implements EntityResolver {
		public InputSource resolveEntity(String pubid, String sysid) {
			return new InputSource(new ByteArrayInputStream(new byte[0]));
		}
	}

	public HtmlTag[] getHtmlTags() {
		Collection<HtmlTag> elementSet = htmlTags.values();
		return elementSet.toArray(new HtmlTag[] {});
	}

	public HtmlTagGroup[] getHtmlTagGroups() {
		Collection<HtmlTagGroup> elementGroupSet = tagGroups.values();
		return elementGroupSet.toArray(new HtmlTagGroup[] {});
	}
	
	public static void main(String[] args) {
		HtmlTagManager tagManager = HtmlTagManager.getInstance();
		HtmlTag[] elements = tagManager.getHtmlTags();
		for (HtmlTag element : elements) {
			element.dump("");
		}
	}
}
