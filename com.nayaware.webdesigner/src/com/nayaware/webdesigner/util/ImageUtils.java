package com.nayaware.webdesigner.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.nayaware.webdesigner.WebDesignerPlugin;


/**
 * Defines the Keys to identify the Images in the plugin and provide utilities
 * to obtain the images
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ImageUtils {

	/**
	 * Image Registry to register the loaded images
	 */
	private static final ImageRegistry imageRegistry = new ImageRegistry();

	/**
	 * Maps images to image decorators
	 */
	private static HashMap<Image, HashMap<Image, Image>> imageToDecoratorMap = new HashMap<Image, HashMap<Image, Image>>();

	private static final int MISSING_IMAGE_SIZE = 10;

	/**
	 * Image Key Constants
	 */
	public static final String NONE = ""; //$NON-NLS-1$ 
	
	public static final String BOLD = "bold.gif"; //$NON-NLS-1$
	public static final String BOLD_DISABLED = "bold_na.gif"; //$NON-NLS-1$
	public static final String ITALIC = "italic.gif"; //$NON-NLS-1$
	public static final String ITALIC_DISABLED = "italic_na.gif"; //$NON-NLS-1$
	public static final String UNDERLINE = "underline.gif"; //$NON-NLS-1$
	public static final String UNDERLINE_DISABLED = "underline_na.gif"; //$NON-NLS-1$
	public static final String STRIKE_THROUGH = "strikethrough.gif"; //$NON-NLS-1$
	public static final String STRIKE_THROUGH_DISABLED = "strikethrough_na.gif"; //$NON-NLS-1$
	
	public static final String ALIGN_LEFT = "alignleft.gif"; //$NON-NLS-1$
	public static final String ALIGN_LEFT_DISABLED = "alignleft_na.gif"; //$NON-NLS-1$
	public static final String ALIGN_CENTER = "aligncenter.gif"; //$NON-NLS-1$
	public static final String ALIGN_CENTER_DISABLED = "aligncenter_na.gif"; //$NON-NLS-1$
	public static final String ALIGN_RIGHT = "alignright.gif"; //$NON-NLS-1$
	public static final String ALIGN_RIGHT_DISABLED = "alignright_na.gif"; //$NON-NLS-1$
	public static final String ALIGN_JUSTIFY = "alignjustify.gif"; //$NON-NLS-1$
	public static final String ALIGN_JUSTIFY_DISABLED = "alignjustify_na.gif"; //$NON-NLS-1$
	
	public static final String UNDO = "undo.gif"; //$NON-NLS-1$
	public static final String UNDO_DISABLED = "undo_na.gif"; //$NON-NLS-1$
	public static final String REDO = "redo.gif"; //$NON-NLS-1$
	public static final String REDO_DISABLED = "redo_na.gif"; //$NON-NLS-1$
	
	public static final String COPY = "copy.gif";
	public static final String COPY_DISABLED = "copy_disabled.gif";;
	public static final String CUT = "cut.png";
	public static final String CUT_DISABLED = "cut_disabled.gif";
	public static final String PASTE = "paste.gif";
	public static final String PASTE_DISABLED = "paste_disabled.gif";
	
	public static final String UL = "unorderlist.gif";
	public static final String UL_DISABLED = "unorderlist_na.gif";
	public static final String OL = "orderlist.gif";
	public static final String OL_DISABLED = "orderlist_na.gif";
	
	public static final String SUBSCRIPT = "subscript.gif";
	public static final String SUBSCRIPT_DISABLED = "subscript_na.gif";
	public static final String SUPERSCRIPT_DISABLED = "superscript.gif";
	public static final String SUPERSCRIPT = "superscript.gif";
	
	public static final String INDENT = "indent.gif";
	public static final String INDENT_DISABLED = "indent_na.gif";
	public static final String OUTDENT = "outdent.gif";
	public static final String OUTDENT_DISABLED = "outdent_na.gif";

	public static final String TABLE_INSERT = "table.gif";
	public static final String TABLE_INSERT_DISABLED = "table_na.gif";
	public static final String TABLE_DELETE = "table_delete.gif";
	public static final String TABLE_DELETE_DISABLED = "table_delete_na.gif";

	public static final String TABLE_INSERT_ROW_BEFORE = "table_insert_row_before.gif";
	public static final String TABLE_INSERT_ROW_BEFORE_DISABLED = "table_insert_row_before_na.gif";
	public static final String TABLE_INSERT_ROW_AFTER = "table_insert_row_after.gif";
	public static final String TABLE_INSERT_ROW_AFTER_DISABLED = "table_insert_row_after_na.gif";
	public static final String TABLE_DELETE_ROW = "table_delete_row.gif";
	public static final String TABLE_DELETE_ROW_DISABLED = "table_delete_row_na.gif";

	
	public static final String TABLE_INSERT_COLUMN_BEFORE = "table_insert_col_before.gif";
	public static final String TABLE_INSERT_COLUMN_BEFORE_DISABLED = "table_insert_col_before_na.gif";
	public static final String TABLE_INSERT_COLUMN_AFTER = "table_insert_col_after.gif";
	public static final String TABLE_INSERT_COLUMN_AFTER_DISABLED = "table_insert_col_after_na.gif";
	public static final String TABLE_DELETE_COLUMN = "table_delete_col.gif";
	public static final String TABLE_DELETE_COLUMN_DISABLED = "table_delete_col_na.gif";
	 
	public static final String TABLE_MERGE_CELLS = "table_merge_cells.gif";
	public static final String TABLE_MERGE_CELLS_DISABLED = "table_merge_cells_na.gif";
	
	public static final String TABLE_SPLIT_CELL = "table_split_cells.gif";
	public static final String TABLE_SPLIT_CELL_DISABLED = "table_split_cells_na.gif";
	
	public static final String HTML = "html.png";

	/**
	 * Style constant for placing decorator image in top left corner of base
	 * image.
	 */
	public static final int TOP_LEFT = 1;
	/**
	 * Style constant for placing decorator image in top right corner of base
	 * image.
	 */
	public static final int TOP_RIGHT = 2;
	/**
	 * Style constant for placing decorator image in bottom left corner of base
	 * image.
	 */
	public static final int BOTTOM_LEFT = 3;
	/**
	 * Style constant for placing decorator image in bottom right corner of base
	 * image.
	 */
	public static final int BOTTOM_RIGHT = 4;
	
	public static Image missingImage;

	public static synchronized Image getIcon(String imageKey) {
		if (imageRegistry.get(imageKey) == null) {

			try {
				imageRegistry.put(imageKey, getImageDescriptor(imageKey)
						.createImage(true));
			} catch (Exception exc) {
				// TODOD: Do not use exception catching for logic
				if (missingImage == null) {
					missingImage = getMissingImage();
				}
				return missingImage;
			}
		}
		return imageRegistry.get(imageKey);
	}

	public static Image getIcon(Class<?> clazz, String imageKey) {
		if (imageRegistry.get(imageKey) == null) {

			try {
				imageRegistry.put(imageKey, getImage(clazz
						.getResourceAsStream(imageKey)));
			} catch (Exception exc) {
				// TODOD: Do not use exception catching for logic
				if (missingImage == null) {
					missingImage = getMissingImage();
				}
				return missingImage;
			}
		}
		return imageRegistry.get(imageKey);
	}

	protected static Image getImage(InputStream is) {
		Display display = Display.getCurrent();
		ImageData data = new ImageData(is);
		if (data.transparentPixel > 0)
			return new Image(display, data, data.getTransparencyMask());
		return new Image(display, data);
	}

	public static ImageDescriptor getImageDescriptor(String imageKey) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(
				WebDesignerPlugin.PLUGIN_ID, "icons/" + imageKey); //$NON-NLS-1$
	}

	/**
	 * Returns an image composed of a base image decorated by another image
	 * 
	 * @param baseImage
	 *            Image The base image that should be decorated
	 * @param decorator
	 *            Image The image to decorate the base image
	 * @param corner
	 *            The corner to place decorator image
	 * @return Image The resulting decorated image
	 */
	public static Image decorateImage(final Image baseImage,
			final Image decorator, final int corner) {
		HashMap<Image, Image> decoratedMap = imageToDecoratorMap.get(baseImage);
		if (decoratedMap == null) {
			decoratedMap = new HashMap<Image, Image>();
			imageToDecoratorMap.put(baseImage, decoratedMap);
		}
		Image result = decoratedMap.get(decorator);
		if (result == null) {
			final Rectangle bid = baseImage.getBounds();
			final Rectangle did = decorator.getBounds();
			final Point baseImageSize = new Point(bid.width, bid.height);
			CompositeImageDescriptor compositImageDesc = new CompositeImageDescriptor() {
				@Override
				protected void drawCompositeImage(int width, int height) {
					drawImage(baseImage.getImageData(), 0, 0);
					if (corner == TOP_LEFT) {
						drawImage(decorator.getImageData(), 0, 0);
					} else if (corner == TOP_RIGHT) {
						drawImage(decorator.getImageData(), bid.width
								- did.width - 1, 0);
					} else if (corner == BOTTOM_LEFT) {
						drawImage(decorator.getImageData(), 0, bid.height
								- did.height - 1);
					} else if (corner == BOTTOM_RIGHT) {
						drawImage(decorator.getImageData(), bid.width
								- did.width - 1, bid.height - did.height - 1);
					}
				}

				@Override
				protected Point getSize() {
					return baseImageSize;
				}
			};
			result = compositImageDesc.createImage();
			decoratedMap.put(decorator, result);
		}
		return result;
	}

	private static Image getMissingImage() {
		Image image = new Image(Display.getCurrent(), MISSING_IMAGE_SIZE,
				MISSING_IMAGE_SIZE);
		//
		GC gc = new GC(image);
		gc.setBackground(ResourceUtils.getColor(SWT.COLOR_RED));
		gc.fillRectangle(0, 0, MISSING_IMAGE_SIZE, MISSING_IMAGE_SIZE);
		gc.dispose();
		//
		return image;
	}

	/**
	 * Dispose all of the cached images
	 */
	public static void dispose() {
		imageRegistry.dispose();
		for (Iterator<HashMap<Image, Image>> I = imageToDecoratorMap.values()
				.iterator(); I.hasNext();) {
			HashMap<Image, Image> decoratedMap = I.next();
			for (Iterator<Image> J = decoratedMap.values().iterator(); J
					.hasNext();) {
				Image image = J.next();
				image.dispose();
			}
		}
	}
}
