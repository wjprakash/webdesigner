package com.nayaware.webdesigner.htmltag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Wraps the HTML tag definition in the HTML schema definition file
 * @author Winston Prakash
 */
public class HtmlTag {

    private String htmlTagName;
    private List<HtmlAttribute> attributeList = new ArrayList<HtmlAttribute>();
    private List<HtmlAttributeGroup> attributeGroupList = new ArrayList<HtmlAttributeGroup>();

    public HtmlTag(Element htmlElement, Map<String, HtmlAttributeGroup> htmlAttributeGroups) {
        htmlTagName = htmlElement.getAttribute("name");

        // Find the list of Attribute Groups        
        NodeList attributeNodes = htmlElement.getElementsByTagName("xs:attribute");
        for (int i = 0; i < attributeNodes.getLength(); i++) {
            Element element = (Element) attributeNodes.item(i);
            if (element.hasAttribute("name")) {
                HtmlAttribute htmlAttribute = new HtmlAttribute(element);
                attributeList.add(htmlAttribute);
            }
        }

        // Find the list of Attributes
        NodeList attributeGroupNodes = htmlElement.getElementsByTagName("xs:attributeGroup");
        for (int i = 0; i < attributeGroupNodes.getLength(); i++) {
            Element element = (Element) attributeGroupNodes.item(i);
            if (element.hasAttribute("ref")) {
                HtmlAttributeGroup htmlAttributeGroup = htmlAttributeGroups.get(element.getAttribute("ref"));
                attributeGroupList.add(htmlAttributeGroup);
            }
        }
    }

    public String getName() {
        return htmlTagName;
    }

    public List<HtmlAttribute> getAttributes() {
        return attributeList;
    }

    public List<HtmlAttributeGroup> getAttributeGroups() {
        return attributeGroupList;
    }

    public void dump(String indent) {
        System.out.println(indent + "Element: " + htmlTagName);
        for (int i = 0; i < attributeList.size(); i++) {
            HtmlAttribute htmlAttribute = attributeList.get(i);
            htmlAttribute.dump(indent + "   ");
        }

        for (int i = 0; i < attributeGroupList.size(); i++) {
            HtmlAttributeGroup htmlAttributeGroup = attributeGroupList.get(i);
            htmlAttributeGroup.dump(indent + "   ");
        }
    }

    public boolean isInlineTag() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
