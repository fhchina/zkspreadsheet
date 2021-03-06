/* FullWeekStep.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Mar 29, 2011 2:37:53 PM, Created by henrichen
}}IS_NOTE

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/


package org.zkoss.zss.model.sys.impl;

import java.util.Locale;

import org.zkoss.poi.ss.usermodel.Cell;

/**
 * Full month Step.
 * @author henrichen
 *
 */
/*package*/ class FullMonthStep implements Step {
	private final CircularStep _innerStep;
	private final int _type;
	public FullMonthStep(int initial, int step, int type, int datatype, Locale locale) { //ZSS-69
		_innerStep = new CircularStep(initial, step, FullMonthData.getInstance(type, locale)); 
		_type = datatype;
	}
	@Override
	public int getDataType() {
		return _type;
	}
	@Override
	public Object next(Cell srcCell) {
		return _innerStep.next(srcCell);
	}
}
