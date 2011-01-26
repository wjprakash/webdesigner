package com.nayaware.webdesigner.editors.html;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class XMLWhitespaceDetector implements IWhitespaceDetector {

	public boolean isWhitespace(char c) {
		return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
	}
}
