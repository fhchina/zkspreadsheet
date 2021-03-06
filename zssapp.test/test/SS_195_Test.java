/* order_test_1Test.java

	Purpose:
		
	Description:
		
	History:
		Sep, 7, 2010 17:30:59 PM

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

This program is distributed under Apache License Version 2.0 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/

//sort ==> custom sort B13~B17, remove first condition
public class SS_195_Test extends SSAbstractTestCase {
	@Override
	protected void executeTest() {
		rightClickCells(1,12,1,16);
	
		mouseOver(jq("a.z-menu-cnt:eq(3)"));		
		waitResponse();
		click(jq("$customSort a.z-menu-item-cnt"));
		waitResponse();
		
		//verify
		verifyTrue(jq("$_customSortDialog").isVisible());		

		//verify
		verifyTrue(jq("$_customSortDialog @combobox i.z-combobox-rounded-btn-readonly:eq(1)").exists());
		
		click(jq("$_customSortDialog @listcell:eq(0)"));
		waitResponse();
		click(jq("$delBtn td.z-button-cm"));
		waitResponse();
		
		//verify
		verifyFalse(jq("$_customSortDialog @combobox i.z-combobox-rounded-btn-readonly:eq(1)").exists());
	}
}



