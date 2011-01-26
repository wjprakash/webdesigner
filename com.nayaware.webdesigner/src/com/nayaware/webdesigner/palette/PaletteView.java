package com.nayaware.webdesigner.palette;

import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import com.nayaware.webdesigner.util.ResourceUtils;


/**
 * The Palette View
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class PaletteView extends ViewPart {

	private Color prevForeGroundColor;
	private Color prevBackGoundColor;

	private CLabel prevHoveredPaletteLabel;
	private CLabel prevSelectedPaletteLabel;

	public static final String ID = "com.nayaware.webdesigner.palette.PaletteView"; //$NON-NLS-1$

	/**
	 * Create contents of the view part
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		FormToolkit toolkit = new FormToolkit(Display.getCurrent());
		Composite container = toolkit.createComposite(parent, SWT.NONE);
		container.setLayout(new FillLayout());
		toolkit.paintBordersFor(container);

		ExpandBar expandBar = new ExpandBar(container, SWT.V_SCROLL);

		URL htmlPaletteXmlUrl = this.getClass().getClassLoader().getResource(
				"/com/nayaware/webdesigner/palette/HtmlPalette.xml");

		Palette palette = PaletteManager.getInstance().loadPalette(
				htmlPaletteXmlUrl);

		for (PaletteCategory paletteCategory : palette.getPaletteCategories()) {
			createExpandItem(paletteCategory, expandBar);
		}
	}

	private void createExpandItem(PaletteCategory paletteCategory, ExpandBar bar) {
		Composite composite = new Composite(bar, SWT.NONE);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		composite.setLayout(gridLayout);

		for (final PaletteItem paletteItem : paletteCategory.getPaletteItems()) {
			final CLabel label = new CLabel(composite, SWT.NONE);
			label
					.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
							false));
			label.setImage(paletteItem.getIcon());
			label.setText(paletteItem.getDisplayName());
			
			Transfer[] transferTypes = new Transfer[] {TextTransfer.getInstance()};
			//Transfer[] transferTypes = new Transfer[] {PaletteItemTransfer.getInstance()};
			int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
			
			DragSource source = new DragSource(label, operations);
			source.setTransfer(transferTypes);
			
			source.addDragListener (new DragSourceAdapter () {
				public void dragSetData (DragSourceEvent event) {
					//event.data = new PaletteItem [] {paletteItem};
					event.data = paletteItem.getName();
				}
			});

			label.addMouseTrackListener(new MouseTrackAdapter() {
				public void mouseEnter(MouseEvent e) {
					if (prevHoveredPaletteLabel != null) {
						prevHoveredPaletteLabel
								.setForeground(prevForeGroundColor);
					}
					prevForeGroundColor = label.getForeground();
					label.setForeground(ResourceUtils
							.getColor(SWT.COLOR_DARK_GREEN));
					prevHoveredPaletteLabel = label;
				}
			});
			label.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseDown(MouseEvent e) {
					if (prevSelectedPaletteLabel != null) {
						prevSelectedPaletteLabel
								.setBackground(prevBackGoundColor);
					}
					prevBackGoundColor = label.getBackground();
					label.setBackground(ResourceUtils
							.getColor(SWT.COLOR_DARK_GRAY));
					prevSelectedPaletteLabel = label;
				}
			});
		}

		ExpandItem expandItem = new ExpandItem(bar, SWT.NONE, 0);
		expandItem.setText(paletteCategory.getDisplayName());
		expandItem.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		expandItem.setControl(composite);
		expandItem.setExpanded(true);
	}

	@Override
	public void setFocus() {
		 
	}
}
