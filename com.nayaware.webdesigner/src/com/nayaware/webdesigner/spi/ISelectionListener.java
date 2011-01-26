package com.nayaware.webdesigner.spi;

import java.util.EventListener;

/**
 * Embedded Browser selection listener
 * @author Winston Prakash
 * @version 1.0
 */
public interface ISelectionListener extends EventListener {
	public void selectionChanged(ISelection selection);
}
