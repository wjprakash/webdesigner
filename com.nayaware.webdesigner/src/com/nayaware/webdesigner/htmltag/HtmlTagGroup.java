package com.nayaware.webdesigner.htmltag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Represents the group of HTML tags in the HTML schema definition file
 * @author Winston Prakash
 */
public class HtmlTagGroup {

    private List<HtmlTag> tagList;
    private List<HtmlTagGroup> tagGroupList;
    private String tagGroupName;
    private Element htmlTagGroup;
    private Map<String, HtmlTag> htmlElements;
    private Map<String, HtmlTagGroup> htmlElementGroups;

    public HtmlTagGroup(Element elementGroup, Map<String, HtmlTag> elements, Map<String, HtmlTagGroup> elementGroups) {
        htmlTagGroup = elementGroup;
        htmlElements = elements;
        htmlElementGroups = elementGroups;
        tagGroupName = elementGroup.getAttribute("name");
    }

    public String getName() {
        return tagGroupName;
    }

    public List<HtmlTag> getTags() {
        if (tagList == null) {
            tagList = new ArrayList<HtmlTag>();
            NodeList elementNodes = htmlTagGroup.getElementsByTagName("xs:element");
            for (int i = 0; i < elementNodes.getLength(); i++) {
                Element element = (Element) elementNodes.item(i);
                if (element.hasAttribute("ref")) {
                    HtmlTag xtmlElement = htmlElements.get(element.getAttribute("ref"));
                    tagList.add(xtmlElement);
                }
            }
        }
        return tagList;
    }

    public List<HtmlTagGroup> getTagGroups() {
        if (tagGroupList == null) {
            tagGroupList = new ArrayList<HtmlTagGroup>();
            NodeList elementGroupNodes = htmlTagGroup.getElementsByTagName("xs:group");
            for (int i = 0; i < elementGroupNodes.getLength(); i++) {
                Element elementGroupElement = (Element) elementGroupNodes.item(i);
                if (elementGroupElement.hasAttribute("ref")) {
                    HtmlTagGroup elementGroup = htmlElementGroups.get(elementGroupElement.getAttribute("ref"));
                    tagGroupList.add(elementGroup);
                }
            }
        }
        return tagGroupList;
    }

    public void dump() {
        getTags();
        System.err.println("Element Group: " + tagGroupName);
        for (int i = 0; i < tagList.size(); i++) {
            HtmlTag htmlElement = tagList.get(i);
            System.out.println("   Element: " + htmlElement.getName());
        //htmlElement.dump("    ");
        }
        getTagGroups();
        for (int i = 0; i < tagGroupList.size(); i++) {
            HtmlTagGroup elementGroup = tagGroupList.get(i);
            System.err.println("   Element Group: " + elementGroup.getName());
        //htmlTagGroup.dump("    ");
        }

    }
}