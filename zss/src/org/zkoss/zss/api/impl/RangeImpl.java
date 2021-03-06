/* RangeImpl.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		2013/5/1 , Created by dennis
}}IS_NOTE

Copyright (C) 2013 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkoss.zss.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.poi.ss.formula.FormulaParseException;
import org.zkoss.poi.ss.usermodel.Cell;
import org.zkoss.poi.ss.usermodel.ClientAnchor;
import org.zkoss.poi.ss.usermodel.Row;
import org.zkoss.poi.ss.util.CellRangeAddress;
import org.zkoss.zss.api.CellVisitor;
import org.zkoss.zss.api.IllegalFormulaException;
import org.zkoss.zss.api.IllegalOpArgumentException;
import org.zkoss.zss.api.Range;
import org.zkoss.zss.api.RangeRunner;
import org.zkoss.zss.api.Ranges;
import org.zkoss.zss.api.SheetAnchor;
import org.zkoss.zss.api.model.Book;
import org.zkoss.zss.api.model.CellData;
import org.zkoss.zss.api.model.CellStyle;
import org.zkoss.zss.api.model.CellStyle.BorderType;
import org.zkoss.zss.api.model.Chart;
import org.zkoss.zss.api.model.Chart.Grouping;
import org.zkoss.zss.api.model.Chart.LegendPosition;
import org.zkoss.zss.api.model.Chart.Type;
import org.zkoss.zss.api.model.ChartData;
import org.zkoss.zss.api.model.Hyperlink;
import org.zkoss.zss.api.model.Hyperlink.HyperlinkType;
import org.zkoss.zss.api.model.Picture;
import org.zkoss.zss.api.model.Picture.Format;
import org.zkoss.zss.api.model.Sheet;
import org.zkoss.zss.api.model.impl.BookImpl;
import org.zkoss.zss.api.model.impl.CellDataImpl;
import org.zkoss.zss.api.model.impl.CellStyleImpl;
import org.zkoss.zss.api.model.impl.ChartDataImpl;
import org.zkoss.zss.api.model.impl.ChartImpl;
import org.zkoss.zss.api.model.impl.EnumUtil;
import org.zkoss.zss.api.model.impl.HyperlinkImpl;
import org.zkoss.zss.api.model.impl.ModelRef;
import org.zkoss.zss.api.model.impl.PictureImpl;
import org.zkoss.zss.api.model.impl.SheetImpl;
import org.zkoss.zss.api.model.impl.SimpleRef;
import org.zkoss.zss.model.sys.XBook;
import org.zkoss.zss.model.sys.XRange;
import org.zkoss.zss.model.sys.XRanges;
import org.zkoss.zss.model.sys.XSheet;
import org.zkoss.zss.model.sys.impl.BookHelper;
import org.zkoss.zss.ui.impl.XUtils;

/**
 * 1.Range is not handling the protection issue, if you have handle it yourself before calling the api(by calling {@code #isProtected()})
 * @author dennis
 * @since 3.0.0
 */
public class RangeImpl implements Range{
	
	private XRange _range;
	
	private SyncLevel _syncLevel = SyncLevel.BOOK;
	
	private CellStyleHelper _cellStyleHelper;
	private CellData _cellData;
	
	public void setSyncLevel(SyncLevel syncLevel){
		this._syncLevel = syncLevel;
	}
	
	private SharedContext _sharedCtx;
	
	public RangeImpl(XRange range,Sheet sheet) {
		this._range = range;
		_sharedCtx = new SharedContext(sheet);
	}
	private RangeImpl(XRange range,SharedContext ctx) {
		this._range = range;
		_sharedCtx = ctx;
	}
	
	
	public CellStyleHelper getCellStyleHelper(){
		if(_cellStyleHelper==null){
			_cellStyleHelper = new CellStyleHelperImpl(getBook());
		}
		return _cellStyleHelper;
	}
	
	public CellData getCellData(){
		if(_cellData==null){
			_cellData = new CellDataImpl(this);
		}
		return _cellData;
	}
	
	public XRange getNative(){
		return _range;
	}
	
	
	
	private List<MergeArea> getMergeAreas(){
		XSheet xsheet = ((SheetImpl)_sharedCtx.getSheet()).getNative();
		int sz = xsheet.getNumMergedRegions();
		List<MergeArea> mergeAreas = new ArrayList<MergeArea>(sz);
		for(int j = sz - 1; j >= 0; --j) {
			final CellRangeAddress addr = xsheet.getMergedRegion(j);
			mergeAreas.add(new MergeArea(addr.getFirstRow(),addr.getFirstColumn(), addr.getLastRow(),addr.getLastColumn()));
		}
		return mergeAreas;
	}
	
	private static class SharedContext{
		Sheet _sheet;
		
		private SharedContext(Sheet sheet){
			this._sheet = sheet;
		}
		
		public Sheet getSheet(){
			return _sheet;
		}
		
		public Book getBook(){
			return _sheet.getBook();
		}
	}
	private static class MergeArea{
		int _row,_lastRow,_column,_lastColumn;
		public MergeArea(int r,int c,int lr,int lc){
			this._column = c;
			this._row = r;
			this._lastColumn = lc;
			this._lastRow = lr;
		}

		public boolean contains(int row,int col) {
			return col >= this._column && col <= this._lastColumn && row >= this._row && row <= this._lastRow;
		}
		
		public boolean equals(int row, int column, int lastRow, int lastColumn){
			return _row == row && _column==column && _lastRow==lastRow && _lastColumn == lastColumn;
		}

		public boolean contains(int row, int column, int lastRow, int lastColumn) {
			return contains(row, column) || contains(lastRow, lastColumn)
					|| contains(row, lastColumn) || contains(lastRow, column);
		}
		public boolean contains(MergeArea ma) {
			return contains(ma._row,ma._column,ma._lastRow,ma._lastColumn);
		}
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_range == null) ? 0 : _range.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RangeImpl other = (RangeImpl) obj;
		if (_range == null) {
			if (other._range != null)
				return false;
		} else if (!_range.equals(other._range))
			return false;
		return true;
	}

	public boolean isProtected() {
		return getSheet().isProtected();
	}	

//	public boolean isAnyCellProtected(){
//		return range.isAnyCellProtected();
//	}
	
	public Range paste(Range dest, boolean cut) {
		XRange r = _range.copy(((RangeImpl)dest).getNative(), cut);
		return new RangeImpl(r, dest.getSheet());
	}

	/* short-cut for pasteSpecial, it is original Range.copy*/
	public Range paste(Range dest) {
		XRange r = _range.copy(((RangeImpl)dest).getNative());
		return new RangeImpl(r, dest.getSheet());
	}
	
	public Range pasteSpecial(Range dest,PasteType type,PasteOperation op,boolean skipBlanks,boolean transpose) {
//		if(!isAnyCellProtected()){ // ranges seems this in copy/paste already
		//TODO the syncLevel
		XRange r = _range.pasteSpecial(((RangeImpl)dest).getNative(), EnumUtil.toRangePasteTypeNative(type), EnumUtil.toRangePasteOpNative(op), skipBlanks, transpose);
		return new RangeImpl(r, dest.getSheet());
//		}
	}


	public void clearContents() {
		//TODO the syncLevel
		_range.clearContents();		
	}
	
	public Sheet getSheet(){
		return _sharedCtx.getSheet();
	}

 
	public void clearStyles() {
		setCellStyle(null);//will use default book cell style		
	}

	public void setCellStyle(final CellStyle nstyle) {
		//TODO the syncLevel
		_range.setStyle(nstyle==null?null:((CellStyleImpl)nstyle).getNative());
	}


	public int getColumn() {
		return _range.getColumn();
	}
	public int getRow() {
		return _range.getRow();
	}
	public int getLastColumn() {
		return _range.getLastColumn();
	}
	public int getLastRow() {
		return _range.getLastRow();
	}
	
	public void sync(RangeRunner run){
		switch(_syncLevel){
		case NONE:
			run.run(this);
			return;
		case BOOK:
			synchronized(_sharedCtx.getBook().getSync()){//it just show concept, we have to has a betterway to do read-write lock
				run.run(this);
			}
			return;
		}
	}
	/**
	 * visit all cells in this range, make sure you call this in a limited range, 
	 * don't use it for all row/column selection, it will spend much time to iterate the cell 
	 * @param visitor the visitor 
	 * @param create create cell if it doesn't exist, if it is true, it will also lock the sheet
	 */
	public void visit(CellVisitor visitor){
		visit0(visitor,_syncLevel);
	}
	private void visit0(final CellVisitor visitor,SyncLevel sync){
		final int r=getRow();
		final int lr=getLastRow();
		final int c=getColumn();
		final int lc=getLastColumn();
		
		Runnable run = new Runnable(){
			public void run(){
				for(int i=r;i<=lr;i++){
					for(int j=c;j<=lc;j++){
						if(!visitCell(visitor,i,j))
							break;
					}
				}
			}
		};
		
		switch(sync){
		case NONE:
			run.run();
			return;
		case BOOK:
			synchronized(_sharedCtx.getBook().getSync()){
				run.run();
			}
			return;
		}
	}
	
	private boolean visitCell(CellVisitor visitor,int r, int c){
		boolean ignore = false;
		boolean ignoreSet = false;
		boolean create = false;
		boolean createSet = false;
		XSheet sheet = _range.getSheet();
		Row row = sheet.getRow(r);
		if(row==null){
			ignore = visitor.ignoreIfNotExist(r,c);
			ignoreSet = true;
			if(!ignore){
				create = visitor.createIfNotExist(r, c);
				createSet = true;
				if(create){
					row = sheet.createRow(r);
				}
			}else{
				return true;
			}
		}
		Cell cell = row.getCell(c);
		if(cell==null){
			if(!ignoreSet){
				ignore = visitor.ignoreIfNotExist(r,c);
				ignoreSet = true;
			}
			if(!ignore){
				if(!createSet){
					create = visitor.createIfNotExist(r, c);
					createSet = true;
				}
				if(create){
					cell = row.createCell(c);
				}
			}else{
				return true;
			}
		}
		return visitor.visit(new RangeImpl(XRanges.range(_range.getSheet(),r,c),_sharedCtx));
	}

	public Book getBook() {
		return getSheet().getBook();
	}
	
	public void applyBordersAround(BorderType borderType,String htmlColor){
		applyBorders(ApplyBorderType.OUTLINE,borderType, htmlColor);
	}
	
	public void applyBorders(ApplyBorderType type,BorderType borderType,String htmlColor){
		//TODO the syncLevel
		_range.setBorders(EnumUtil.toRangeApplyBorderType(type), EnumUtil.toRangeBorderType(borderType), htmlColor);
	}

	
	public boolean hasMergedCell(){
		MergeArea curr = new MergeArea(getRow(),getColumn(),getLastRow(),getLastColumn());
		for(MergeArea ma:getMergeAreas()){
			if(curr.contains(ma)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isMergedCell(){
		for(MergeArea ma:getMergeAreas()){
			if(ma.equals(getRow(),getColumn(),getLastRow(),getLastColumn())){
				return true;
			}
		}
		return false;
	}
	
	
	static private class Result<T> {
		T r;
		Result(){}
		Result(T r){
			this.r = r;
		}
		
		public T get(){
			return r;
		}
		
		public void set(T r){
			this.r = r;
		}
	}

	public void merge(boolean across){
		//TODO the syncLevel
		_range.merge(across);
	}
	
	public void unmerge(){
		//TODO the syncLevel
		_range.unMerge();
	}

	
	public RangeImpl toShiftedRange(int rowOffset,int colOffset){
		RangeImpl offsetRange = new RangeImpl(_range.getOffset(rowOffset, colOffset),_sharedCtx);
		return offsetRange;
	}
	
	
	public RangeImpl toCellRange(int rowOffset,int colOffset){
		RangeImpl cellRange = new RangeImpl(XRanges.range(_range.getSheet(),getRow()+rowOffset,getColumn()+colOffset),_sharedCtx);
		return cellRange;
	}
	
	/** get the top-left cell range of this range**/
	public RangeImpl getLeftTop() {
		return toCellRange(0,0);
	}
	
	/**
	 *  Return a range that represents all columns and between the first-row and last-row of this range
	 **/
	public RangeImpl toRowRange(){
		return new RangeImpl(_range.getRows(),_sharedCtx);
	}
	
	/**
	 *  Return a range that represents all rows and between the first-column and last-column of this range
	 **/
	public RangeImpl toColumnRange(){
		return new RangeImpl(_range.getColumns(),_sharedCtx);
	}
	
	/**
	 * Check if this range represents a whole column, which mean all rows are included, 
	 */
	public boolean isWholeColumn(){
		return _range.isWholeColumn();
	}
	/**
	 * Check if this range represents a whole row, which mean all column are included, 
	 */
	public boolean isWholeRow(){
		return _range.isWholeRow();
	}
	/**
	 * Check if this range represents a whole sheet, which mean all column and row are included, 
	 */
	public boolean isWholeSheet(){
		return _range.isWholeSheet();
	}
	
	public void insert(InsertShift shift,InsertCopyOrigin copyOrigin){
		//TODO the syncLevel
		_range.insert(EnumUtil.toRangeInsertShift(shift), EnumUtil.toRangeInsertCopyOrigin(copyOrigin));
	}
	
	public void delete(DeleteShift shift){
		//TODO the syncLevel
		_range.delete(EnumUtil.toRangeDeleteShift(shift));
	}
	
	public void sort(boolean desc){	
		sort(desc,false,false,false,null);
	}
	
	public void sort(boolean desc,
			boolean header, 
			boolean matchCase, 
			boolean sortByRows, 
			SortDataOption dataOption){
		Range index = null;
		int r = getRow();
		int c = getColumn();
		int lr = getLastRow();
		int lc = getLastColumn();
		
		index = Ranges.range(this.getSheet(),r,c,sortByRows?r:lr,sortByRows?lc:c);
		
		sort(index,desc,dataOption,
			null,false,null,
			null,false,null,
			header,matchCase,sortByRows);
	}
	
	public void sort(Range index1,boolean desc1,SortDataOption dataOption1,
			Range index2,boolean desc2,SortDataOption dataOption2,
			Range index3,boolean desc3,SortDataOption dataOption3,
			boolean header, 
			/*int orderCustom, //not implement*/
			boolean matchCase, 
			boolean sortByRows
			/*int sortMethod, //not implement*/){
		
		//TODO the syncLevel
		
		//TODO review the full impl for range1,range2,range3
		
		_range.sort(index1==null?null:((RangeImpl)index1).getNative(), desc1, 
				index2==null?null:((RangeImpl)index2).getNative()/*rng2*/, -1 /*type*/, desc2/*desc2*/, 
				index3==null?null:((RangeImpl)index3).getNative()/*rng3*/, desc3/*desc3*/,
				header?BookHelper.SORT_HEADER_YES:BookHelper.SORT_HEADER_NO/*header*/,
				-1/*orderCustom*/, matchCase, sortByRows, -1/*sortMethod*/, 
				dataOption1==null?BookHelper.SORT_NORMAL_DEFAULT:EnumUtil.toRangeSortDataOption(dataOption1)/*dataOption1*/,
				dataOption2==null?BookHelper.SORT_NORMAL_DEFAULT:EnumUtil.toRangeSortDataOption(dataOption2)/*dataOption2*/,
				dataOption3==null?BookHelper.SORT_NORMAL_DEFAULT:EnumUtil.toRangeSortDataOption(dataOption3)/*dataOption3*/);
	}
	
	/** check if auto filter is enable or not.**/
	public boolean isAutoFilterEnabled(){
		return getSheet().isAutoFilterEnabled();
	}
	
	// ZSS-246: give an API for user checking the auto-filtering range before applying it.
	public Range findAutoFilterRange() {
		XRange r = _range.findAutoFilterRange();
		if(r != null) {
			return Ranges.range(getSheet(), r.getRow(), r.getColumn(), r.getLastRow(), r.getLastColumn());
		} else {
			return null;
		}
	}

	/** enable/disable autofilter of the sheet**/
	public void enableAutoFilter(boolean enable){
		//TODO the syncLevel
		if(isAutoFilterEnabled() == enable){
			return ;
		}
		
		_range.autoFilter();//toggle on/off automatically
	}
	/** enable filter with condition **/
	//TODO have to review this after I know more detail
	public void enableAutoFilter(int field, AutoFilterOperation filterOp, Object criteria1, Object criteria2, Boolean visibleDropDown){
		//TODO the syncLevel
		_range.autoFilter(field,criteria1,EnumUtil.toRangeAutoFilterOperation(filterOp),criteria2,visibleDropDown);
	}
	
	/** clear criteria of all filters, show all the data**/
	public void resetAutoFilter(){
		//TODO the syncLevel
		_range.showAllData();
	}
	
	/** re-apply existing criteria of filters **/
	public void applyAutoFilter(){
		//TODO the syncLevel
		_range.applyFilter();
	}
	
	/** enable sheet protection and apply a password**/
	public void protectSheet(String password){
		//TODO the syncLevel
		_range.protectSheet(password);
	}
	
	public void autoFill(Range dest,AutoFillType fillType){
		//TODO the syncLevel
		_range.autoFill(((RangeImpl)dest).getNative(), EnumUtil.toRangeAutoFillType(fillType));
	}
	
	public void fillDown(){
		//TODO the syncLevel
		_range.fillDown();
	}
	
	public void fillLeft(){
		//TODO the syncLevel
		_range.fillLeft();
	}
	
	public void fillUp(){
		//TODO the syncLevel
		_range.fillUp();
	}
	
	public void fillRight(){
		//TODO the syncLevel
		_range.fillRight();
	}
	
	/** shift this range with a offset row and column**/
	public void shift(int rowOffset,int colOffset){
		//TODO the syncLevel
		_range.move(rowOffset, colOffset);
	}
	
	public String getCellEditText(){
		String txt = _range.getEditText();
		return txt==null?"":txt;
	}
	
	public void setCellEditText(String editText){
		//TODO the syncLevel
		try{
			_range.setEditText(editText);
		}catch(FormulaParseException x){
			throw new IllegalFormulaException(x.getMessage(),x);
		}
	}
	
	public String getCellFormatText(){
		//I don't create my way, use the same way from Spreadsheet implementation as possible
		String txt = XUtils.getCellFormatText(getNative().getSheet(), getRow(), getColumn());
		return txt==null?"":txt;
	}
	
	//TODO need to verify the object type
	public Object getCellValue(){
		return _range.getValue();
	}
	
	public void setDisplaySheetGridlines(boolean enable){
		//TODO the syncLevel
		_range.setDisplayGridlines(enable);
	}
	
	public boolean isDisplaySheetGridlines(){
		return getSheet().isDisplayGridlines();
	}
	
	public void setHidden(boolean hidden){
		//TODO the syncLevel
		_range.setHidden(hidden);
	}
	
	public void setCellHyperlink(HyperlinkType type,String address,String display){
		//TODO the syncLevel
		_range.setHyperlink(EnumUtil.toHyperlinkType(type), address, display);
	}
	
	public Hyperlink getCellHyperlink(){
		org.zkoss.poi.ss.usermodel.Hyperlink l = _range.getHyperlink();
		//NOTE current hyperlink implementation can't provide correct label, so I get it form cell text directly 
		return l==null?null:new HyperlinkImpl(new SimpleRef<org.zkoss.poi.ss.usermodel.Hyperlink>(l),getCellEditText());
	}
	
	public void setSheetName(String name){
		//TODO the syncLevel
		_range.setSheetName(name);
	}
	
	public String getSheetName(){
		return getSheet().getSheetName();
	}
	
	public void setSheetOrder(int pos){
		//TODO the syncLevel
		_range.setSheetOrder(pos);
	}
	
	public int getSheetOrder(){
		return getBook().getSheetIndex(getSheet());
	}
	
	public void setCellValue(Object value){
		//TODO the syncLevel
		_range.setValue(value);
	}
	
	private ModelRef<XBook> getBookRef(){
		return ((BookImpl)getBook()).getRef();
	}
	
	private ModelRef<XSheet> getSheetRef(){
		return ((SheetImpl)getSheet()).getRef();
	}
	

	/**
	 * get the first cell style of this range
	 * 
	 * @return cell style if cell is exist, the check row style and column cell style if cell not found, if row and column style is not exist, then return default style of sheet
	 */
	public CellStyle getCellStyle() {
		XSheet sheet = _range.getSheet();
		XBook book = sheet.getBook();
		
		int r = _range.getRow();
		int c = _range.getColumn();
		org.zkoss.poi.ss.usermodel.CellStyle style = null;
		Row row = sheet.getRow(r);
		if (row != null){
			Cell cell = row.getCell(c);
			
			if (cell != null){//cell style
				style = cell.getCellStyle();
			}
			if(style==null && row.isFormatted()){//row sytle
				style = row.getRowStyle();
			}
		}
		if(style==null){//col style
			style = sheet.getColumnStyle(c);
		}
		if(style==null){//default
			style = book.getCellStyleAt((short) 0);
		}
		
		return new CellStyleImpl(getBookRef(), new SimpleRef<org.zkoss.poi.ss.usermodel.CellStyle>(style));		
	}

	
	public Picture addPicture(SheetAnchor anchor,byte[] image,Format format){
		ClientAnchor an = SheetImpl.toClientAnchor(getSheet().getPoiSheet(),anchor);
		org.zkoss.poi.ss.usermodel.Picture pic = _range.addPicture(an, image, EnumUtil.toPictureFormat(format));
		return new PictureImpl(getSheetRef(), new SimpleRef<org.zkoss.poi.ss.usermodel.Picture>(pic));
	}
	
	public void deletePicture(Picture picture){
		//TODO the syncLevel
		_range.deletePicture(((PictureImpl)picture).getNative());
	}
	
	public void movePicture(SheetAnchor anchor,Picture picture){
		//TODO the syncLevel
		ClientAnchor an = SheetImpl.toClientAnchor(getSheet().getPoiSheet(),anchor);
		_range.movePicture(((PictureImpl)picture).getNative(), an);
	}
	
	//currently, we only support to modify chart in XSSF
	public Chart addChart(SheetAnchor anchor,ChartData data,Type type, Grouping grouping, LegendPosition pos){
		//TODO the syncLevel
		ClientAnchor an = SheetImpl.toClientAnchor(getSheet().getPoiSheet(),anchor);
		org.zkoss.poi.ss.usermodel.charts.ChartData cdata = ((ChartDataImpl)data).getNative();
		org.zkoss.poi.ss.usermodel.Chart chart = _range.addChart(an, cdata, EnumUtil.toChartType(type), EnumUtil.toChartGrouping(grouping), EnumUtil.toLegendPosition(pos));
		return new ChartImpl(getSheetRef(), new SimpleRef<org.zkoss.poi.ss.usermodel.Chart>(chart));
	}
	
	//currently, we only support to modify chart in XSSF
	public void deleteChart(Chart chart){
		//TODO the syncLevel
		_range.deleteChart(((ChartImpl)chart).getNative());
	}
	
	//currently, we only support to modify chart in XSSF
	public void moveChart(SheetAnchor anchor,Chart chart){
		//TODO the syncLevel
		ClientAnchor an = SheetImpl.toClientAnchor(getSheet().getPoiSheet(),anchor);
		_range.moveChart(((ChartImpl)chart).getNative(), an);
	}
	
	
	public Sheet createSheet(String name){
		//TODO the syncLevel
		XBook book = ((BookImpl)getBook()).getNative();
		int n = book.getNumberOfSheets();
		_range.createSheet(name);
		
		XSheet sheet = book.getWorksheetAt(n);
		return new SheetImpl(((BookImpl)_sharedCtx._sheet.getBook()).getRef(),new SimpleRef<XSheet>(sheet));
		
	}
	
	public void deleteSheet(){
		//TODO the syncLevel
		//ZSS-493 check if last sheet throws illegal op argument exception
		XSheet sheet = _range.getSheet();
		if(sheet.getBook().getNumberOfSheets()==1){
			throw new IllegalOpArgumentException("Cannot delete last sheet");
		}
		_range.deleteSheet();
	}
	
	
	@Override
	public void setColumnWidth(int widthPx) {
		XRange r = _range.isWholeColumn()?_range:_range.getColumns();
		r.setColumnWidth(XUtils.pxToFileChar256(widthPx, ((XBook)_range.getSheet().getWorkbook()).getDefaultCharWidth()));
	}
	@Override
	public void setRowHeight(int heightPx) {
		XRange r = _range.isWholeRow()?_range:_range.getRows();
		r.setRowHeight(XUtils.pxToPoint(heightPx));
	}
	
	//api that need special object wrap
	
	
	private void apiSpecialWrapObject(){

//		range.getFormatText();//FormatText
//		range.getHyperlink();//Hyperlink
//		
//		range.getRichEditText();//RichTextString
//		range.getText();//RichTextString (what is the difference of getRichEditText)
//		
		
//		range.validate("");//DataValidation
		
	}
	
	
	private void api4Internal(){
		_range.notifyDeleteFriendFocus(null);//by Spreadsheet
		_range.notifyMoveFriendFocus(null);//
	}
	
	
	//API of range that no one use it.
	
	
	private void apiNoOneUse(){
		
		_range.getCount();
		_range.getCurrentRegion();
		_range.getDependents();
		_range.getDirectDependents();
		_range.getPrecedents();
		
		_range.isCustomHeight();
		
		//range.pasteSpecial(pasteType, pasteOp, SkipBlanks, transpose);		
	}
	
	public String toString(){
		return Ranges.getAreaRefString(getSheet(), getRow(),getColumn(),getLastRow(),getLastColumn());
	}
	
	/**
	 * Notify this range has been changed.
	 */
	public void notifyChange(){
		_range.notifyChange();
	}
	
	public void notifyChange(String[] variables){
		((XBook)getBook().getPoiBook()).notifyChange(variables);
	}
	
	@Override
	public void setFreezePanel(int rowfreeze, int columnfreeze) {
		_range.setFreezePanel(rowfreeze, columnfreeze);
	}
	@Override
	public int getRowCount() {
		return _range.getLastRow()-_range.getRow()+1;
	}
	@Override
	public int getColumnCount() {
		return _range.getLastColumn()-_range.getColumn()+1;
	}
	@Override
	public String asString() {
		return Ranges.getAreaRefString(getSheet(), getRow(),getColumn(),getLastRow(),getLastColumn());
	}
		

}
