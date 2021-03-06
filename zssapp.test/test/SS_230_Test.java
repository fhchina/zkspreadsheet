import org.zkoss.ztl.JQuery;


//use mouse to auto fill
//input 1 in cell k13, 2 in cell k14
//
public class SS_230_Test extends SSAbstractTestCase {
	@Override
	protected void executeTest() {
		selectCells(10, 12, 10, 12);
		type(jq("$formulaEditor"), "1");
		waitResponse();
		selectCells(10, 13, 10, 13);
		type(jq("$formulaEditor"), "2");
		waitResponse();
		selectCells(10, 12, 10, 13);
		
		mouseOver(jq(".zsseldot"));
		waitResponse();
		mouseMove(jq(".zsseldot"));
		waitResponse();
		mouseDown(jq(".zsseldot"));
		waitResponse();
		mouseMoveAt(getSpecifiedCell(11, 17),"-2,-2");
		waitResponse();
		mouseUpAt(getSpecifiedCell(11, 17),"-2,-2");
		waitResponse();
		
		String k15value = getCellText(10,14);
		String k16value = getCellText(10,15);
		String k17value = getCellText(10,16);
		verifyEquals(k15value,"3");
		verifyEquals(k16value,"4");
		verifyEquals(k17value,"5");
	}
}



