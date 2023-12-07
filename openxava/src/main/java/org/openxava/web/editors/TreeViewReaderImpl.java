/**
 * 
 */
package org.openxava.web.editors;

import java.util.*;

import org.apache.commons.beanutils.*;
import org.openxava.model.*;

/**
 * Implements the Tree view reader
 * @author Federico Alcantara
 *
 */
public class TreeViewReaderImpl implements ITreeViewReader {

	private String collectionModelName;
	
	@SuppressWarnings("rawtypes")
	private Map[] allKeys;
	
	private String[] columnNames;
	
	private int lastReadRow = -1;
	
	private Object lastReadObject = null;
	
	/**
	 * @see org.openxava.web.editors.ITreeViewReader#initialize(java.lang.String, java.lang.String[], java.util.Map, java.util.Map[])
	 */
	@SuppressWarnings("rawtypes")
	public void initialize(String parentModelName, Map parentKey, String collectionModelName,  Map[] allKeys) {
		this.collectionModelName = collectionModelName;
		this.allKeys = allKeys;
	}

	/**
	 * @see org.openxava.web.editors.ITreeViewReader#close()
	 */
	public void close() throws Exception {
	}

	/**
	 * @see org.openxava.web.editors.ITreeViewReader#getRowObject()
	 */
	public Object getObjectAt(int rowIndex) throws Exception {
		if (rowIndex != lastReadRow) {
			lastReadObject = MapFacade.findEntity(collectionModelName, allKeys[rowIndex]);
			lastReadRow = rowIndex;
		}
		return lastReadObject;
	}

	/**
	 * @see org.openxava.web.editors.ITreeViewReader#getValueAt(int, int)
	 */
	@Deprecated
	public Object getValueAt(int rowIndex, int columnIndex) throws Exception {
		Object rowObject = getObjectAt(rowIndex);
		return PropertyUtils.getProperty(rowObject, columnNames[columnIndex]);
	}

}
