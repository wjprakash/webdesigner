package com.nayaware.webdesigner.palette;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

import com.nayaware.webdesigner.util.ErrorManager;


/**
 * Palette Item Transfer  to transfer Palette Item for drag and drop
 * @author Winston Prakash
 * @version 1.0
 */
public class PaletteItemTransfer extends ByteArrayTransfer {

	private static final String MYTYPENAME = "my_type_name";
	private static final int MYTYPEID = registerType(MYTYPENAME);
	private static PaletteItemTransfer _instance = new PaletteItemTransfer();

	private PaletteItemTransfer() {
	}

	public static PaletteItemTransfer getInstance() {
		return _instance;
	}

	public void javaToNative(Object object, TransferData transferData) {
		if (object == null || !(object instanceof PaletteItem[]))
			return;

		if (isSupportedType(transferData)) {
			PaletteItem[] myTypes = (PaletteItem[]) object;
			ByteArrayOutputStream out = null;
			ObjectOutputStream writeOut = null;
			try {
				// write data to a byte array and then ask super to convert
				// to pMedium
				out = new ByteArrayOutputStream();
				writeOut = new ObjectOutputStream(out);
				writeOut.writeObject(myTypes[0]);
				byte[] buffer = out.toByteArray();
				super.javaToNative(buffer, transferData);

			} catch (IOException exc) {
				ErrorManager.showException(exc);
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException exc) {
						ErrorManager.showException(exc);
					}
				}
				if (writeOut != null) {
					try {
						writeOut.close();
					} catch (IOException exc) {
						ErrorManager.showException(exc);
					}
				}
			}
		}
	}

	public Object nativeToJava(TransferData transferData) {

		if (isSupportedType(transferData)) {

			byte[] buffer = (byte[]) super.nativeToJava(transferData);
			if (buffer == null)
				return null;

			PaletteItem[] myData = new PaletteItem[0];
			ByteArrayInputStream in = null;
			ObjectInputStream readIn = null;

			try {
				in = new ByteArrayInputStream(buffer);
				readIn = new ObjectInputStream(in);
			} catch (IOException exc) {
				ErrorManager.showException(exc);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException exc) {
						ErrorManager.showException(exc);
					}
				}
				if (readIn != null) {
					try {
						readIn.close();
					} catch (IOException exc) {
						ErrorManager.showException(exc);
					}
				}
			}
			return myData;
		}

		return null;
	}

	protected String[] getTypeNames() {
		return new String[] { MYTYPENAME };
	}

	protected int[] getTypeIds() {
		return new int[] { MYTYPEID };
	}
}