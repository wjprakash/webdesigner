package com.nayaware.webdesigner.mozilla;

import org.mozilla.interfaces.nsICommandParams;

/**
 * Wrapper for Mozilla Command Paremeter (nsICommandParams)
 * https://developer.mozilla.org/en/Editor_Embedding_Guide#nsICommandParams
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class MozillaCommandParameters {
	private nsICommandParams params;

	public MozillaCommandParameters(nsICommandParams params) {
		this.params = params;
	}

	public boolean hasKey(String key) {
		boolean result = false;
		while (params.hasMoreElements()) {
			if (params.getNext().equals(key)) {
				result = true;
				break;
			}
		}

		params.first();
		return result;
	}

	public String getStringValue(String key) {
		if (hasKey(key)) {
			if (params.getValueType(key) == nsICommandParams.eStringType) {
				return params.getCStringValue(key);
			} else if (params.getValueType(key) == nsICommandParams.eWStringType) {
				return params.getStringValue(key);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public boolean getBooleanValue(String key) {
		if (hasKey(key)) {
			return params.getBooleanValue(key);
		} else {
			return false;
		}
	}
}
