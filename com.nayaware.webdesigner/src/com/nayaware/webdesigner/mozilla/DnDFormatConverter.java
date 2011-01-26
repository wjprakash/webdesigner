package com.nayaware.webdesigner.mozilla;

import java.util.Arrays;

import org.mozilla.interfaces.nsIFormatConverter;
import org.mozilla.interfaces.nsISupports;
import org.mozilla.interfaces.nsISupportsArray;
import org.mozilla.interfaces.nsISupportsCString;
import org.mozilla.interfaces.nsISupportsString;
import org.mozilla.xpcom.Mozilla;


/**
 * @author Winston Prakash
 * @version 1.0
 */
public class DnDFormatConverter implements nsIFormatConverter {

	private static final String[] fromMimes = new String[] {
			"application/x-moz-nativehtml", "text/html", "text/unicode", };

	private static final String[] toMimes = new String[] { "text/unicode", };

	public boolean canConvert(String fromDataFlavor, String toDataFlavor) {
		return Arrays.asList(fromMimes).contains(fromDataFlavor)
				&& Arrays.asList(toMimes).contains(toDataFlavor);
	}

	public void convert(String fromDataFlavor, nsISupports fromData,
			long dataLen, String toDataFlavor, nsISupports[] toData,
			long[] dataToLen) {
		String d = "";

		nsISupportsCString s1 = MozillaUtils.qi(fromData,
				nsISupportsCString.class);
		if (s1 != null)
			d = s1.getData();
		else {
			nsISupportsString s2 = MozillaUtils.qi(fromData,
					nsISupportsString.class);
			if (s2 != null)
				d = s2.getData();
		}

		nsISupportsString s = MozillaUtils.create(
				"@mozilla.org/supports-string;1", nsISupportsString.class);
		s.setData(d);
		toData[0] = s;
		dataToLen[0] = d.length();
	}

	public nsISupportsArray getInputDataFlavors() {
		nsISupportsArray a = MozillaUtils.create("@mozilla.org/supports-array;1",
				nsISupportsArray.class);
		for (String mime : fromMimes) {
			nsISupportsCString s = MozillaUtils
					.create("@mozilla.org/supports-cstring;1",
							nsISupportsCString.class);
			s.setData(mime);
			a.appendElement(s);
		}
		return a;
	}

	public nsISupportsArray getOutputDataFlavors() {
		nsISupportsArray a = MozillaUtils.create("@mozilla.org/supports-array;1",
				nsISupportsArray.class);
		for (String mime : toMimes) {
			nsISupportsCString s = MozillaUtils
					.create("@mozilla.org/supports-cstring;1",
							nsISupportsCString.class);
			s.setData(mime);
			a.appendElement(s);
		}
		return a;
	}

	public nsISupports queryInterface(String uuid) {
		return Mozilla.queryInterface(this, uuid);
	}

}
