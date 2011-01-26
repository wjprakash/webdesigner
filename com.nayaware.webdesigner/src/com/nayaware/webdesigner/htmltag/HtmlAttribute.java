package com.nayaware.webdesigner.htmltag;

import org.w3c.dom.Element;

/**
 * Represents the HTML attribute definition in the HTML schema definition file

 * @author Winston Prakash
 * @version 1.0
 */
public class HtmlAttribute {

    private String attributeName;
    private String attributeType;

    public HtmlAttribute(Element htmlAttribute) {
        attributeName = htmlAttribute.getAttribute("name");
        attributeType = htmlAttribute.getAttribute("type");
    }

    public String getName() {
        return attributeName;
    }

    public String getDisplayName() {
        return getName();
    }

    public String getShortDescription() {
        return getDisplayName();
    }

    public Class getType() {
        //return attributeType;
        return String.class;
    }

    public void dump(String indent) {
        System.out.println(indent + attributeName + " (" + attributeType + ")");
    }
}

