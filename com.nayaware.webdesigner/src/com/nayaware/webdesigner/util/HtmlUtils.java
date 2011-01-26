package com.nayaware.webdesigner.util;

import com.nayaware.webdesigner.palette.PaletteItem;

/**
 * Some HTML related utilities
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class HtmlUtils {

	private static String visual_aid = "outline-color: gray; outline-style: dotted; outline-width: thin;";

	public static String getTableHtml() {
		return "<table style=\"" + visual_aid
				+ "\" width=\"100%\" border=\"1\">" + "<tr>"
				+ "<td>row 1, cell 1</td>" + "<td>row 1, cell 2</td>" + "</tr>"
				+ "<tr>" + "<td>row 2, cell 1</td>" + "<td>row 2, cell 2</td>"
				+ "</tr>" + "</table>";
	}

	public static String getDivHtml() {
		return "<div style=\"" + visual_aid + "\" > <br/> </div>";
	}

	public static String getHyperlinkHtml() {
		return "<a href=\"\" > Hyperlink </a>";
	}

	public static String getHrHtml() {
		return "<hr />";
	}

	public static String getFormHtml() {
		return "<form  method=\"post\" action=\"\"> </form>";
	}

	public static String getRadioButtonGroupHtml() {
		return "<p>"
				+ "<label>"
				+ "<input type=\"radio\" name=\"RadioGroup1\" value=\"radio\" id=\"RadioGroup1_1\" />"
				+ "Radio Button1</label>"
				+ "<br />"
				+ "<label>"
				+ "<input type=\"radio\" name=\"RadioGroup1\" value=\"radio\" id=\"RadioGroup1_2\" />"
				+ "Radio Button2</label>"
				+ "<br />"
				+ "<label>"
				+ "<input type=\"radio\" name=\"RadioGroup1\" value=\"radio\" id=\"RadioGroup1_2\" />"
				+ "Radio Button3</label>" + "<br />" + "</p>";
	}

	public static String getRadioButtonHtml() {
		return "<p>"
				+ "<label>"
				+ "<input type=\"radio\" name=\"RadioGroup1\" value=\"radio\" id=\"RadioGroup1_1\" />"
				+ "Radio Button</label>" + "</p>";
	}

	public static String getTextFieldHtml() {
		return "<input type=\"text\" />";
	}

	public static String getLabelHtml() {
		return "<label>Label</label>";
	}

	public static String getButtonHtml() {
		return "<input type=\"button\" value=\"Button\" />";
	}

	private static String getDropdownHtml() {
		return "<select>" + "<option value=\"Option1\">Option1</option>"
				+ "<option value=\"Option2\">Option2</option>"
				+ "<option value=\"Option3\">Option3</option>" + "</select>";
	}

	private static String getListBoxHtml() {
		return "<select  size=\"5\"> "
				+ "<option value=\"Option1\">Option1</option>"
				+ "<option value=\"Option2\">Option2</option>"
				+ "<option value=\"Option3\">Option3</option>" + "</select>";
	}

	private static String getHiddenFieldHtml() {
		return "<input type=\"hidden\" value=\"hiddenField1\" />";
	}

	private static String getPasswordFieldHtml() {
		return "<input type=\"password\" value=\"password\" size=\"10\" maxlength=\"20\" />";
	}

	private static String getCheckboxGroupHtml() {
		return "<p>" + "<label>"
				+ "<input type=\"checkbox\" value=\"checkbox1\" />"
				+ "Checkbox1</label><br />" + "<label>"
				+ "<input type=\"checkbox\" value=\"checkbox2\" />"
				+ "Checkbox</label><br />" + "</p>";
	}

	private static String getCheckboxHtml() {
		return "<input type=\"checkbox\" value=\"checkBox\" checked > Checkbox </input>";
	}

	private static String getTextAreaHtml() {
		return "<textarea  cols=\"45\" rows=\"5\"></textarea>";
	}

	public static String getHtmlForTag(String data) {
		if (PaletteItem.TABLE.equals(data)) {
			return getTableHtml();
		} else if (PaletteItem.HR.equals(data)) {
			return getHrHtml();
		} else if (PaletteItem.DIV.equals(data)) {
			return HtmlUtils.getDivHtml();
		} else if (PaletteItem.HYPERLINK.equals(data)) {
			return HtmlUtils.getHyperlinkHtml();
		} else if (PaletteItem.FORM.equals(data)) {
			return HtmlUtils.getFormHtml();
		} else if (PaletteItem.LABEL.equals(data)) {
			return HtmlUtils.getLabelHtml();
		} else if (PaletteItem.BUTTON.equals(data)) {
			return HtmlUtils.getButtonHtml();
		} else if (PaletteItem.DROP_DOWN.equals(data)) {
			return HtmlUtils.getDropdownHtml();
		} else if (PaletteItem.LIST_BOX.equals(data)) {
			return HtmlUtils.getListBoxHtml();
		} else if (PaletteItem.HIDDEN_FIELD.equals(data)) {
			return HtmlUtils.getHiddenFieldHtml();
		} else if (PaletteItem.PASSWORD_FIELD.equals(data)) {
			return HtmlUtils.getPasswordFieldHtml();
		} else if (PaletteItem.RADIO_BUTTON.equals(data)) {
			return HtmlUtils.getRadioButtonHtml();
		} else if (PaletteItem.RADIO_BUTTON_GROUP.equals(data)) {
			return HtmlUtils.getRadioButtonGroupHtml();
		} else if (PaletteItem.CHECKBOX.equals(data)) {
			return HtmlUtils.getCheckboxHtml();
		} else if (PaletteItem.CHECKBOX_GROUP.equals(data)) {
			return HtmlUtils.getCheckboxGroupHtml();
		} else if (PaletteItem.TEXT_FIELD.equals(data)) {
			return HtmlUtils.getTextFieldHtml();
		} else if (PaletteItem.TEXT_AREA.equals(data)) {
			return HtmlUtils.getTextAreaHtml();
		}
		return null;
	}
}
