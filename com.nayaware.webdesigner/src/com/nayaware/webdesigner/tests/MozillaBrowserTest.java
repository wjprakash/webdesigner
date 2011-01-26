package com.nayaware.webdesigner.tests;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.nayaware.webdesigner.mozilla.MozillaBrowser;


/**
 * @author Winston Prakash
 * @version 1.0
 */
public class MozillaBrowserTest {

	static MozillaBrowser mozillaBrowser;

	public static void main(String[] args) {
		try {
			Display display = new Display();
			Shell shell = new Shell(display);
			shell.setLayout(new GridLayout(2, true));
			shell.setText("Use Mozilla's Design Mode");
			mozillaBrowser = new MozillaBrowser(shell);

			final Button offButton = new Button(shell, SWT.RADIO);
			offButton.setText("Design Mode Off");
			offButton.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					if (!offButton.getSelection())
						return;
					mozillaBrowser.setDesignMode("off");
				}
			});
			final Button onButton = new Button(shell, SWT.RADIO);
			onButton.setText("Design Mode On");
			onButton.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					try {
						mozillaBrowser.setDesignMode("on");
					} catch (Exception exc) {
						exc.printStackTrace();
					}
					// if (!onButton.getSelection()) return;
					// boolean success = mozillaBrowser.setDesignMode("on");
					// if (!success) {
					// onButton.setSelection(false);
					// offButton.setSelection(true);
					// }
				}
			});
			offButton.setSelection(true);

			mozillaBrowser.loadUrl("http://www.sun.com");
			shell.setSize(400, 400);
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
			display.dispose();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
}
