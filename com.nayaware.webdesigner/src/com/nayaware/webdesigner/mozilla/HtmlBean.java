package com.nayaware.webdesigner.mozilla;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.model.IWorkbenchAdapter2;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;

import com.nayaware.webdesigner.htmltag.HtmlAttribute;
import com.nayaware.webdesigner.htmltag.HtmlAttributeGroup;
import com.nayaware.webdesigner.htmltag.HtmlTag;
import com.nayaware.webdesigner.htmltag.HtmlTagManager;
import com.nayaware.webdesigner.util.ImageUtils;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class HtmlBean implements IAdaptable, IPropertySource,
		IPropertySourceProvider, IWorkbenchAdapter, IWorkbenchAdapter2 {

	private nsIDOMNode domNode;
	private HtmlTag htmlTag;

	public HtmlBean(nsIDOMNode element, HtmlTag htmlTag) {
		this.domNode = element;
		this.htmlTag = htmlTag;
	}

	public Object getAdapter(Class adapter) {
		if (adapter == IPropertySource.class) {
			return this;
		}
		if (adapter == IWorkbenchAdapter.class) {
			return this;
		}
		if (adapter == IWorkbenchAdapter2.class) {
			return this;
		}
		return null;
	}

	public Object getEditableValue() {
		return this;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<PropertyDescriptor> propertyDescriptors = new ArrayList<PropertyDescriptor>();

		List<HtmlAttribute> attributes = htmlTag.getAttributes();
		addPropertyDescriptors(attributes, propertyDescriptors, "General");

		List<HtmlAttributeGroup> attributeGroups = htmlTag.getAttributeGroups();
		for (HtmlAttributeGroup attrGroup : attributeGroups) {
			addPropertyDescriptorsFromGroup(attrGroup, propertyDescriptors,
					attrGroup.getDisplayName());
		}

		return propertyDescriptors
				.toArray(new IPropertyDescriptor[propertyDescriptors.size()]);
	}

	private void addPropertyDescriptorsFromGroup(
			HtmlAttributeGroup attributeGroup,
			List<PropertyDescriptor> propertyDescriptors, String categoryName) {

		List<HtmlAttribute> attributes = attributeGroup.getAttributes();
		addPropertyDescriptors(attributes, propertyDescriptors, attributeGroup
				.getDisplayName());
		List<HtmlAttributeGroup> attributeGroups = attributeGroup.getAttributeGroups();
		for (HtmlAttributeGroup attrGroup : attributeGroups) {
			addPropertyDescriptorsFromGroup(attrGroup, propertyDescriptors,
					attrGroup.getDisplayName());
		}
	}

	private void addPropertyDescriptors(List<HtmlAttribute> attributes,
			List<PropertyDescriptor> propertyDescriptors, String categoryName) {
		for (HtmlAttribute htmlAttribute : attributes) {
			PropertyDescriptor descriptor = new TextPropertyDescriptor(
					htmlAttribute.getName(), htmlAttribute.getDisplayName()); //$NON-NLS-1$
			descriptor.setCategory(categoryName);
			propertyDescriptors.add(descriptor);
		}

	}

	public Object getPropertyValue(Object id) {
		if (nsIDOMNode.ELEMENT_NODE == domNode.getNodeType()) {
			nsIDOMElement element = (nsIDOMElement) domNode
					.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
			String value = element.getAttribute((String) id);
			if ((value != null) && (value.length() > 0)) {
				return value;
			}
		}
		return "";
	}

	public boolean isPropertySet(Object id) {
		if (nsIDOMNode.ELEMENT_NODE == domNode.getNodeType()) {
			nsIDOMElement element = (nsIDOMElement) domNode
					.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
			String attributeValue = element.getAttribute((String) id);
			return (attributeValue != null) && (attributeValue.length() > 0);
		}
		return true;
	}

	public void resetPropertyValue(Object id) {
		// TODO Auto-generated method stub

	}

	public void setPropertyValue(Object id, Object value) {
		if (nsIDOMNode.ELEMENT_NODE == domNode.getNodeType()) {
			nsIDOMElement element = (nsIDOMElement) domNode
					.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID);
			element.setAttribute((String) id, (String) value);
		}
	}

	public Object[] getChildren(Object obj) {
		if (obj instanceof HtmlBean) {
			HtmlBean parentBean = (HtmlBean) obj;
			nsIDOMNode parentDomNode = parentBean.getDomNode();
			if (parentDomNode.hasChildNodes()) {
				nsIDOMNodeList children = parentDomNode.getChildNodes();
				List<HtmlBean> childBeans = new ArrayList<HtmlBean>();
				for (int i = 0; i < children.getLength(); i++) {
					nsIDOMNode child = children.item(i);
					if (nsIDOMNode.ELEMENT_NODE == child.getNodeType()) {
						HtmlTag htmlTag = HtmlTagManager.getInstance().findTag(
								child.getNodeName());
						if (htmlTag != null) {
							childBeans.add(new HtmlBean(child, htmlTag));
						}
					}
				}
				return childBeans.toArray(new HtmlBean[childBeans.size()]);
			}
		}
		return new HtmlBean[0];
	}

	public nsIDOMNode getDomNode() {
		return domNode;
	}

	public void setDomNode(nsIDOMNode domNode) {
		this.domNode = domNode;
	}

	public HtmlTag getHtmlTag() {
		return htmlTag;
	}

	public void setHtmlTag(HtmlTag htmlTag) {
		this.htmlTag = htmlTag;
	}

	public ImageDescriptor getImageDescriptor(Object object) {
		return ImageUtils.getImageDescriptor(ImageUtils.HTML);
	}

	public String getLabel(Object o) {
		return htmlTag.getName();
	}

	public Object getParent(Object o) {
		return domNode.getParentNode();
	}

	public void dump(String space) {
		System.out.println(domNode.getNodeName());
		HtmlBean[] children = (HtmlBean[]) this.getChildren(this);
		for (HtmlBean htmlBean : children) {
			htmlBean.dump(space + " ");
		}
	}

	public RGB getBackground(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	public FontData getFont(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	public RGB getForeground(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	public IPropertySource getPropertySource(Object object) {
		return this;
	}
}
