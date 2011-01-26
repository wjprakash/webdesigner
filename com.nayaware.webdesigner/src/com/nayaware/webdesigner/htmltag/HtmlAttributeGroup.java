package com.nayaware.webdesigner.htmltag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Represents the group of HTML attribute definition in the HTML schema definition file
 
 * @author Winston Prakash
 * @version 1.0
 */
public class HtmlAttributeGroup {

    private List<HtmlAttribute> attributeList;
    private List<HtmlAttributeGroup> attributeGroupList;
    private String attributeGroupName;
    private Element htmlAttributeGroupElement;
    private Map<String, HtmlAttributeGroup> htmlAttributeGroups;

    public HtmlAttributeGroup(Element attributeGroupElement, Map<String, HtmlAttributeGroup> attributeGroups) {
        htmlAttributeGroupElement = attributeGroupElement;
        htmlAttributeGroups = attributeGroups;
        attributeGroupName = htmlAttributeGroupElement.getAttribute("name");
    }

    public String getDisplayName() {
        return getName();
    }

    public String getName() {
        return attributeGroupName;
    }

    public synchronized List<HtmlAttribute> getAttributes() {
        if (attributeList == null) {
            attributeList = new ArrayList<HtmlAttribute>();
            NodeList attributeNodes = htmlAttributeGroupElement.getElementsByTagName("xs:attribute");
            for (int i = 0; i < attributeNodes.getLength(); i++) {
                Element attribute = (Element) attributeNodes.item(i);
                if (attribute.hasAttribute("name")) {
                    HtmlAttribute htmlAttribute = new HtmlAttribute(attribute);
                    attributeList.add(htmlAttribute);
                }
            }
        }
        return attributeList;
    }

    public List<HtmlAttributeGroup> getAttributeGroups() {
        if (attributeGroupList == null) {
            attributeGroupList = new ArrayList<HtmlAttributeGroup>();
            NodeList attributeGroupNode = htmlAttributeGroupElement.getElementsByTagName("xs:attributeGroup");
            for (int i = 0; i < attributeGroupNode.getLength(); i++) {
                Element attributeGroup = (Element) attributeGroupNode.item(i);
                if (attributeGroup.hasAttribute("ref")) {
                    HtmlAttributeGroup htmlAttributeGroup = htmlAttributeGroups.get(attributeGroup.getAttribute("ref"));
                    attributeGroupList.add(htmlAttributeGroup);
                }
            }
        }
        return attributeGroupList;
    }

    public void dump(String indent) {
        getAttributes();
        System.out.println(indent + "Attribute Group: " + attributeGroupName);
        for (int i = 0; i < attributeList.size(); i++) {
            HtmlAttribute htmlAttribute = attributeList.get(i);
            htmlAttribute.dump(indent + "    ");
        }
        getAttributeGroups();
        for (int i = 0; i < attributeGroupList.size(); i++) {
            HtmlAttributeGroup htmlAttributeGroup = attributeGroupList.get(i);
            htmlAttributeGroup.dump(indent + "    ");
        }
    }
}